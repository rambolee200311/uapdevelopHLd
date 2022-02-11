package u8c.bs.action.save;

import java.io.IOException;
import java.util.Iterator;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.framework.common.NCLocator;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDateTime;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.util.JSONTokener;
import u8c.bs.APIConst;
import u8c.bs.exception.ConfigException;
import u8c.bs.utils.FileUtils;
import u8c.itf.oip.chg.IChgToJson;
import u8c.pubitf.action.IAction;
import u8c.pubitf.action.IActionExcel;
import u8c.pubitf.config.IAPIConfigFileService;
import u8c.vo.entity.GlobalSetVO;
import u8c.vo.entity.IdempotentVO;
import u8c.vo.entity.InputDataVO;
import u8c.vo.entity.enumtype.TranType;

/**
 * @����������CircularlyAccessibleValueObject�����������������
 * @since u8c2.1
 * @version 2018-3-16 ����9:40:50
 * @author xuxq3
 * @param <E>
 */

public abstract class AbstractBatchSaveAction<E> implements IAction,
    IActionExcel, IChgToJson<E> {

  /**
   * ����������Ϣ
   * 
   * @param vos
   * @return
   * @throws BusinessException
   */
  public String buildReturnInfo(E[] vos) throws BusinessException {
    return this.getJsonStrFromVOS(vos);
  }

  /**
   * ҵ������Ҫ�Ǳ����ҵ��
   * 
   * @param vos
   * @return
   * @throws BusinessException
   */
  public E[] businessProcess(E[] vos) throws BusinessException {
    return this.save(vos);
  }

  @Override
  public String changeToJson(E[] vos) throws BusinessException {
    return getJsonStrFromVOS(vos);
  }

  @Override
  public String doAction(String json, String tranType)
      throws BusinessException, ConfigException {
    // ����
    E[] vos = this.save(json, tranType);
    
    //���Ψһ��
    JSONObject jsonObj = JSONObject.fromObject(json);
    if(jsonObj.containsKey(InputDataVO.UNIQUEKEY)){
    	 this.checkUnique(jsonObj.getString(InputDataVO.UNIQUEKEY));
    }
    
    // ����������Ϣ
    return this.buildReturnInfo(vos);
  }
  
  //���Ψһ��
  private void checkUnique(String uniqueKey) throws BusinessException{
	  if(uniqueKey==null)
		  return;
	  IdempotentVO vo = new IdempotentVO();
	  vo.setUniquekey(uniqueKey);
	  vo.setBilltype(this.getBillMark());
	  vo.setStatus(VOStatus.NEW);
	  try {
		new BaseDAO().insertVO(vo);
	} catch (DAOException e) {
		// TODO Auto-generated catch block
		if(e.getMessage().contains("UNIQUE KEY")||e.getMessage().contains("unique key")||e.getMessage().contains("ORA-00001")){
			throw new BusinessException("U8C���ݴ����uniquekey("+uniqueKey+")�жϵ�ǰ����Ϊ�ظ�����");
		}
		// TODO Auto-generated catch block
		throw new BusinessException(e.getMessage());
	}
	  
  }
  
  @Override
  public String doAction(String[][] datas) throws BusinessException {
    // ת��
    E[] vos = this.convert(datas);
    // ����
    this.translate(vos);
    // ҵ�����
    vos = this.businessProcess(vos);
    // ����������Ϣ
    return this.buildReturnInfo(vos);

  }

  /**
   * �������json��ת����vo�����룬����
   * 
   * @param json
   * @param tranType
   * @return
   * @throws BusinessException
   * @throws ConfigException
   */
  public E[] save(String json, String tranType) throws BusinessException,
      ConfigException {
	
    // ����API Link����
    String jsonStr = this.getJSONFromAPILink(json);
    // ת��
    E[] vos = this.convert(jsonStr);
    // ����
    if (tranType == null || TranType.CODE.toString().equals(tranType)) {
      this.translate(vos);
    }
    UFBoolean needMiddleFile = this.getGlobalParameter().getNeedMiddleFile();
    // ���ݲ������·������ļ�
    if (needMiddleFile.booleanValue()) {
      this.writeMiddleFile(vos, APIConst.TRANSLATEDPATH);
    }
    // ҵ�����
    vos = this.businessProcess(vos);
    // ���ݲ����������ݿ���ļ�
    if (needMiddleFile.booleanValue()) {
      this.writeMiddleFile(vos, APIConst.RETURNDATAPATH);
    }
    return vos;
  }

  /**
   * ���ò������Ա�������ļ�
   * 
   * @return
   * @throws ConfigException
   */
  private GlobalSetVO getGlobalParameter() throws ConfigException {
    return NCLocator.getInstance().lookup(IAPIConfigFileService.class)
        .getGlobalParameter();

  }

  /**
   * ���ڴ�apilinkƽ̨����������������{bill��[]}
   * 
   * @param json
   * @return
   */
  private String getJSONFromAPILink(String json) {
    JSONObject jsonObj = JSONObject.fromObject(json);
    String returnObj = null;
    Iterator<?> it = jsonObj.keys();
    while (it.hasNext()) {
      returnObj = jsonObj.getString((String) it.next());
      break;
    }
    // ��ת������������ж�ͳһ��JSONArray��ʽ����
    Object jsona = new JSONTokener(returnObj).nextValue();
    JSONArray jsonArray = new JSONArray();
    if (jsona instanceof JSONObject) {
      jsonArray.add(jsona);

    }
    else if (jsona instanceof JSONArray) {
      jsonArray = (JSONArray) jsona;
    }
    return jsonArray.toString();
  }

  /**
   * @param vos
   * @throws BusinessException
   */
  private void writeMiddleFile(E[] vos, String filePath)
      throws BusinessException {
    String str = this.getJsonStrFromVOS(vos);
    String[] date =
        new UFDateTime(System.currentTimeMillis()).toString().split(" ");
    String fileName =
        filePath + this.getBillMark() + "-" + date[0] + "-"
            + date[1].replaceAll(":", "-") + ".txt";
    try {
      new FileUtils().writeBytesToFile(str.getBytes("UTF-8"), fileName);
    }
    catch (IOException e) {
      throw new BusinessException(e.getMessage(), e.getCause());
    }
  }

  /**
   * ת��json�ɵ���
   * 
   * @param json
   * @return E[]
   * @throws BusinessException
   */
  protected abstract E[] convert(String json) throws BusinessException;

  /**
   * ת����ά����ɵ���
   * 
   * @param datas
   * @return
   * @throws BusinessException
   */
  protected abstract E[] convert(String[][] datas) throws BusinessException;

  /**
   * ���ݱ�ʶ
   * 
   * @return String
   */
  protected abstract String getBillMark();

  /**
   * �����json
   * 
   * @param vos
   * @return
   * @throws BusinessException
   */
  protected abstract String getJsonStrFromVOS(E[] vos) throws BusinessException;

  /**
   * ҵ�񱣴�
   * 
   * @param vos
   * @return String
   * @throws BusinessException
   */
  protected abstract E[] save(E[] vos) throws BusinessException;

  /**
   * Ĭ�ϵķ���
   * 
   * @param vos
   * @param tranType
   * @throws BusinessException
   */
  protected abstract void translate(E[] vos) throws BusinessException;

}
