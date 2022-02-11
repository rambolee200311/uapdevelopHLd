package u8c.vo.oip.task;

import java.io.Serializable;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import u8c.bs.exception.ConfigException;
import u8c.bs.message.MessageTools;
import u8c.pubitf.invoke.IInvokeWithJSon;
import u8c.util.oip.APILogUtil;
import u8c.util.oip.APIOutSysUtil;
import u8c.util.oip.APISerChgUtil;
import u8c.util.oip.APISerDefUtil;
import u8c.vo.entity.InputDataVO;
import u8c.vo.oip.chg.APISerChgVO;
import u8c.vo.oip.def.APISerDefVO;
import u8c.vo.oip.log.APILogHeadVO;
import u8c.vo.oip.log.APILogTableVO;
import u8c.vo.oip.outsys.APIOutSysVO;

/**
 * API接口调用任务类
 * 
 * @since U8C 2.7
 * @version 2019-7-18 下午1:45:58
 * @author luojw
 */
public class APITask implements Runnable, Serializable {

  private static final long serialVersionUID = 1296798955947882264L;

  /** 线程执行时，要用原来的数据源 */
  private String dataSource = InvocationInfoProxy.getInstance()
      .getUserDataSource();

  private InputDataVO inputData;

  private APILogTableVO logTableVO;

  private APISerDefVO outsysSerdef;

  private String taskNumber;

  public APITask(InputDataVO inputData) {
    this.inputData = inputData;
    taskNumber = TaskNumberGenerator.newTaskNumber();
  }

  public APITask(InputDataVO inputData, String taskNumber) {
    this.inputData = inputData;
    this.taskNumber = taskNumber;
  }

  public String execute() throws BusinessException {
    String json = inputData.getIndata();
    String trantype = inputData.getTrantype();
    String serCode = inputData.getPathInfo();
    String system = inputData.getSystem();
    APILogUtil.addTaskNumber(taskNumber);
    // 锁住系统
    APIOutSysVO sysvo = APIOutSysUtil.getOutSysVOByCode(system);
    if (sysvo != null) {
      if (!APIOutSysUtil.lockID(sysvo.getPk_outsys())) {
        throw new BusinessException("其他的外部系统正在调用接口，请稍等！");
      }
    }
    APISerDefVO innerSerdef = null;
    // 处理外系统服务
    if (outsysSerdef != null) {
      APISerChgVO serchg =
          APISerChgUtil.queryChgVOBySrcser(outsysSerdef.getPk_serdef());
      if (serchg != null) {
        String pk_destserdef = serchg.getHeadVO().getPk_destserdef();
        innerSerdef = new APISerDefVO(pk_destserdef);
        serCode = serchg.getHeadVO().getDestsercode();
        json = serchg.convertJson(json);
        APILogUtil.logData(json, taskNumber, APILogHeadVO.LOGTYPE_CHGTOU8CJSON,
            outsysSerdef);
      }
    }

    // 进入框架处理
    IInvokeWithJSon invokeFrame =
        NCLocator.getInstance().lookup(IInvokeWithJSon.class);
    String result;
    if (innerSerdef == null) {
      innerSerdef = APISerDefUtil.queryInnerSerDefByCode(serCode);
    }
    UFBoolean isSuccess = UFBoolean.TRUE;
    try {
      APILogUtil.logData(json, taskNumber, APILogHeadVO.LOGTYPE_INVOKEU8CSER,
          innerSerdef);
      result = invokeFrame.invoke(serCode, json, trantype);
      APILogUtil.logData(result, taskNumber, APILogHeadVO.LOGTYPE_RETU8CRESULT,
          innerSerdef);
    }
    catch (ConfigException | BusinessException e) {
      isSuccess = UFBoolean.FALSE;
      String error = MessageTools.getFailureMessage(e, UFBoolean.TRUE);
      APILogUtil.logData(error, this.taskNumber, APILogHeadVO.LOGTYPE_FAIL,
          outsysSerdef);
      if (e instanceof BusinessException) {
        throw (BusinessException) e;
      }
      throw new BusinessException(e);
    }
    finally {
      // 释放系统
      if (sysvo != null) {
        APIOutSysUtil.releaseID(sysvo.getPk_outsys());
      }
      APILogUtil.updateTaskStatus(this.taskNumber, isSuccess);
      APILogUtil.removeTaskNumber(taskNumber);
    }
    // 转换成外系统数据结构
    if (outsysSerdef != null && outsysSerdef.getPk_backserdef() != null) {
      result =
          APIOutSysUtil.convertToOutSysJson(result,
              outsysSerdef.getPk_backserdef());
      APILogUtil.addTaskNumber(taskNumber, logTableVO);
      APILogUtil.logData(result, taskNumber,
          APILogHeadVO.LOGTYPE_CHGTOOUTSYSJSON, outsysSerdef);
      try {
        APIOutSysUtil.sendResultToOutSys(result, taskNumber,
            outsysSerdef.getPk_backserdef(), true);
      }
      catch (BusinessException e) {
        APILogUtil.logData(e.getMessage(), taskNumber,
            APILogHeadVO.LOGTYPE_RETOUTSYSRESULT, outsysSerdef);
      }
      APILogUtil.removeTaskNumber(taskNumber);
    }
    return result;
  }

  public InputDataVO getInputData() {
    return this.inputData;
  }

  public APILogTableVO getLogTableVO() {
    return this.logTableVO;
  }

  public String getTaskNumber() {
    return this.taskNumber;
  }

  @Override
  public void run() {
    // 设置数据源
    InvocationInfoProxy.getInstance().setUserDataSource(dataSource);
    try {
      this.execute();
    }
    catch (BusinessException e) {
      MessageTools.getFailureMessage(e,
          UFBoolean.valueOf(inputData.getNeedStackTrace()));
    }
  }

  public void setLogTableVO(APILogTableVO logTableVO) {
    this.logTableVO = logTableVO;
  }

  public void setSerdef(APISerDefVO serdef) {
    this.outsysSerdef = serdef;
  }

  public void setTaskNumber(String taskNumber) {
    this.taskNumber = taskNumber;
  }

}
