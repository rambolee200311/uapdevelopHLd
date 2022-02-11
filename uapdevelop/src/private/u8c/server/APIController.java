package u8c.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.http.HttpServletRequest;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.framework.mx.thread.ThreadTracer;
import nc.bs.logging.Logger;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.sm.login.NCEnv;
import net.sf.json.JSONObject;
import u8c.bs.APIConst;
import u8c.bs.auth.AuthQueryer;
import u8c.bs.exception.ConfigException;
import u8c.bs.exception.SecurityException;
import u8c.bs.json.JsonFactory;
import u8c.bs.json.bind.IJson;
import u8c.bs.message.MessageTools;
import u8c.bs.utils.EncryptUtil;
import u8c.bs.utils.FileUtils;
import u8c.bs.utils.PubAppTool;
import u8c.itf.oip.task.IAPITaskService;
import u8c.pubitf.config.IAPIConfigFileService;
import u8c.util.oip.APILogUtil;
import u8c.util.oip.APIOutSysUtil;
import u8c.util.oip.APISerDefUtil;
import u8c.vo.entity.InputDataVO;
import u8c.vo.entity.enumtype.IAppErrCode;
import u8c.vo.oip.def.APISerDefVO;
import u8c.vo.oip.log.APILogHeadVO;
import u8c.vo.oip.log.APILogTableVO;
import u8c.vo.oip.outsys.APIOutSysVO;
import u8c.vo.oip.task.APITask;
import u8c.vo.pub.APIMessageVO;

import com.yonyou.yht.sdk.SDKUtils;

/**
 * @����������API���ÿ�����
 * @since u8c2.1
 * @version 2018-4-25 ����9:07:43
 * @author xuxq3
 */

public class APIController {

  private static final IJson gson = JsonFactory.create();

  /**
   * api������
   * 
   * @param request
   * @return
   */
  public String forWard(HttpServletRequest request) {

    long requestTime = System.currentTimeMillis();
    APIMessageVO messageVO = new APIMessageVO();
    String rettext = null;
    String errorMessage = null;
    // ��ʼ������
    InputDataVO inputData = this.parseInputParameter(request);

    // ��ȡ��־���ò���
    UFBoolean needMiddleFile = null;
    try {
      needMiddleFile =
          NCLocator.getInstance().lookup(IAPIConfigFileService.class)
              .getGlobalParameter().getNeedMiddleFile();
    }
    catch (ConfigException e) {
      return MessageTools.getFailureMessage(messageVO, e,
          UFBoolean.valueOf(inputData.getNeedStackTrace()));
    }

    // ��������Դ
    if (NCEnv.isDebug()) {
      InvocationInfoProxy.getInstance().setUserDataSource(APIConst.DESIGN);
    }
    else {
      InvocationInfoProxy.getInstance().setUserDataSource(APIConst.DEFALUTDB);
    }

    // ��������
    APITask task = new APITask(inputData);
    String taskNumber = task.getTaskNumber();
    messageVO.setTaskNumber(taskNumber);
    APISerDefVO serdef = null;
    try {
      APILogTableVO logTableVO = APILogUtil.addTaskNumber(taskNumber);
      task.setLogTableVO(logTableVO);
      // У���û�����
      String errorMsg = checkUser(inputData);
      if (errorMsg != null) {
        throw new SecurityException("��ȫ��֤����" + errorMsg,
            IAppErrCode.FAILURE_USER);
      }
      // ������Ȩ�ܿ��ڿ��Žӿ�ƽ̨
      if (!AuthQueryer.haveAuth("1250")) {
        throw new SecurityException(
            "û�й���U8 cloud���Žӿ�ƽ̨��Ȩ������ϵU8Cʵʩ��Ա��������Ա����U8cloud���Žӿ�ƽ̨��1250��ģ����Ȩ��",
            IAppErrCode.FAILURE_NOAUTH);
      }
      this.parseData(request, inputData);
      serdef = APISerDefUtil.querySerDefByCode(inputData.getPathInfo());
      // ��¼������־
      String[] fields =
          {
            InputDataVO.URL, InputDataVO.PATHINFO, InputDataVO.TRANTYPE,
            InputDataVO.INDATA
          };
      APILogUtil.logData(inputData.convertToJson(fields).toString(),
          taskNumber, APILogHeadVO.LOGTYPE_INVOKEOUTSYSSER, serdef);

      // д�������м��ļ�
      if (needMiddleFile.booleanValue()) {
        this.writeMiddleFile(APIConst.INDOCPATH + inputData.getPathInfo(),
            APIController.gson.toJson(inputData));
      }
      // ִ������
      IAPITaskService service =
          NCLocator.getInstance().lookup(IAPITaskService.class);
      String retStr = service.executeTask(task);

      // ����
      String encryptData = this.encrypt(inputData.isEncrypt(), retStr);

      // �����ķ���
      rettext = MessageTools.getSuccessMessage(messageVO, encryptData);
    }
    catch (SecurityException e) {
      errorMessage =
          MessageTools.getFailureMessage(messageVO, e, UFBoolean.FALSE);
    }
    catch (Exception e) {
      errorMessage =
          MessageTools.getFailureMessage(messageVO, e,
              UFBoolean.valueOf(inputData.getNeedStackTrace()));
    }

    finally {
      Logger.debug("After Invoke: " + request.getPathInfo() + " "
          + (System.currentTimeMillis() - requestTime));
      ThreadTracer.getInstance().endThreadMonitor();
    }
    UFBoolean issuccess = UFBoolean.TRUE;
    if (null != errorMessage) {
      rettext = errorMessage;
      issuccess = UFBoolean.FALSE;
    }
    // ��¼������־
    APILogUtil.logData(rettext, taskNumber,
        APILogHeadVO.LOGTYPE_RETOUTSYSRESULT, serdef, issuccess);
    APILogUtil.removeTaskNumber(taskNumber);
    // д�������Ϣ
    if (needMiddleFile.booleanValue()) {
      try {
        if (null != errorMessage) {
          this.writeMiddleFile(
              APIConst.RETURNDATAPATH + inputData.getPathInfo(), errorMessage);
        }
      }
      catch (Exception e) {
        rettext =
            MessageTools.getFailureMessage(messageVO, e,
                UFBoolean.valueOf(inputData.getNeedStackTrace()));
      }
    }
    return rettext;
  }

  protected String checkUser(InputDataVO inputData) throws BusinessException {
    String system = inputData.getSystem();
    // ϵͳΪ�գ��������ϵͳֱ�ӷ��أ������״�
    String path = "�����U8Cϵͳ��ϵͳ����ƽ̨-ϵͳ��Ϣ���á��ڵ��м����������ò����Ƿ�һ�£�";
    if (system == null) {
      if (APIOutSysUtil.isOldSystem()) {
        return null;
      }
      return "����ͷ��û����ϵͳ���롾system��,�����U8Cϵͳ��ϵͳ����ƽ̨-ϵͳ��Ϣ���á��ڵ������ã�";
    }
    // �����1��2��3˵����apilink���õģ�����У���û�����
    if (system.equals("1") || system.equals("2") || system.equals("3")) {
      return null;
    }
    APIOutSysVO outsysvo = APIOutSysUtil.getOutSysVOByCode(system);
    if (outsysvo == null) {
      return "��ϵͳ���롾system�������ڣ�" + path;
    }
    String usercode = inputData.getUsercode();
    String password = inputData.getPassword();
    if (usercode == null || password == null) {
      return "����ͷ�����û�����Ϊ�գ�" + path;
    }
    if (!outsysvo.getSysuser().equals(usercode)) {
      return "����ͷ��������û����롾usercode������ȷ��" + path;
    }
    if (!outsysvo.getSyspassword().equals(password)) {
      return "����ͷ��������û����롾password������ȷ��" + path;
    }
    // �û�������֤ͨ���Ժ󣬽��Ƿ��������ΪN
    inputData.setIsEncrypt("N");
    return null;
  }

  /**
   * @param obj
   * @param isEncrypt
   * @return
   * @throws UnsupportedEncodingException
   * @throws BusinessException
   */
  private String decrypt(String obj, String isEncrypt) throws SecurityException {
    String data = null;
    try {
      if (!PubAppTool.isEqual(isEncrypt, "N")) {
        data =
            EncryptUtil.decryptForAESECB256(obj,
                SDKUtils.encodeUsingMD5(APIParamerConst.u8cloudapi));
      }
      else {
        data = obj;
      }
    }
    catch (InvalidKeyException | NoSuchAlgorithmException
        | NoSuchPaddingException | IllegalBlockSizeException
        | BadPaddingException | NoSuchProviderException
        | UnsupportedEncodingException e) {
      throw new SecurityException("�޷�������Դ����,�����������API Linkƽ̨�����API,�빺�����ʹ�ã�",
          e.getCause());
    }
    return data;
  }

  /**
   * @param response
   * @param rettext
   * @param isEncrypt
   * @param retStr
   * @return
   * @throws UnsupportedEncodingException
   * @throws BusinessException
   */
  protected String encrypt(String isEncrypt, String retStr)
      throws SecurityException {
    if (null == retStr) {
      return retStr;
    }
    String rettext = null;
    try {
      if (!PubAppTool.isEqual(isEncrypt, "N")) {
        rettext =
            EncryptUtil.encryptForAESEBC256(retStr,
                SDKUtils.encodeUsingMD5(APIParamerConst.u8cloudapi));
      }
      else {
        rettext = retStr;
      }
    }
    catch (InvalidKeyException | NoSuchAlgorithmException
        | NoSuchPaddingException | IllegalBlockSizeException
        | BadPaddingException | NoSuchProviderException
        | UnsupportedEncodingException e) {
      throw new SecurityException(e.getMessage(), e.getCause());
    }
    return rettext;
  }


  private String getRequestPostStr(InputStream tInputStream) throws IOException {
    String retStr = null;

    if (tInputStream != null) {
      BufferedReader br = null;
      InputStreamReader isr = null;
      try {
        isr = new InputStreamReader(tInputStream, "UTF-8");// �ַ���
        br = new BufferedReader(isr);// ������
        StringBuffer tStringBuffer = new StringBuffer();
        String sTempOneLine = new String("");
        while ((sTempOneLine = br.readLine()) != null) {
          tStringBuffer.append(sTempOneLine);
        }
        retStr = tStringBuffer.toString();
      }
      finally {
        if (null != br) {
          br.close();
        }
        if (null != isr) {
          isr.close();
        }
      }
    }
    return retStr;

  }

  /**
   * @param request
   * @param inPutData
   * @throws IOException
   * @throws SecurityException
   * @throws BusinessException
   */
  private void parseData(HttpServletRequest request, InputDataVO inPutData)
      throws IOException, SecurityException, ConfigException, BusinessException {
    String pathInfo = APIPathUtil.getPath(request.getPathInfo());
    if (PubAppTool.isNull(pathInfo)) {
      throw new ConfigException("���õ�url·������Ϊ�գ�");
    }
    inPutData.setPathInfo(pathInfo);

    String obj = this.getRequestPostStr(request.getInputStream());
    if (PubAppTool.isNull(obj)) {
      throw new BusinessException("��������ݲ���Ϊ�գ�");
    }
    // ����
    String decryptData = this.decrypt(obj, inPutData.isEncrypt());
    if(null!=decryptData){
		JSONObject jsonObj = JSONObject.fromObject(decryptData);
		jsonObj.put(u8c.vo.entity.InputDataVO.UNIQUEKEY, inPutData.getUniquekey());
		inPutData.setIndata(jsonObj.toString());
	}
  }

  protected InputDataVO parseInputParameter(HttpServletRequest request) {
    InputDataVO inputData = new InputDataVO();
    for (String key : APIParamerConst.headKeys) {
      String value = request.getHeader(key);
      inputData.setAttributeValue(key, value);
    }
    // URL
    inputData.setUrl(request.getRemoteAddr() + ":" + request.getRemotePort());
    return inputData;
  }

  /**
   * @param inPutData
   * @throws ConfigException
   * @throws IOException
   * @throws UnsupportedEncodingException
   */
  protected void writeMiddleFile(String path, String info) throws IOException,
      UnsupportedEncodingException {
    String[] date =
        new UFDateTime(System.currentTimeMillis()).toString().split(" ");
    String fileName =
        path + "-" + date[0] + "-" + date[1].replaceAll(":", "-") + ".txt";
    FileUtils util = new FileUtils();
    if (info != null) {
      util.writeBytesToFile(info.getBytes("UTF-8"), fileName);
    }
  }
}
