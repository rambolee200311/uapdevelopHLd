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
 * @功能描述：CircularlyAccessibleValueObject类型批量保存抽象类
 * @since u8c2.1
 * @version 2018-3-16 上午9:40:50
 * @author xuxq3
 * @param <E>
 */

public abstract class AbstractBatchSaveAction<E> implements IAction,
    IActionExcel, IChgToJson<E> {

  /**
   * 构建返回信息
   * 
   * @param vos
   * @return
   * @throws BusinessException
   */
  public String buildReturnInfo(E[] vos) throws BusinessException {
    return this.getJsonStrFromVOS(vos);
  }

  /**
   * 业务处理，主要是保存等业务
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
    // 保存
    E[] vos = this.save(json, tranType);
    
    //检查唯一性
    JSONObject jsonObj = JSONObject.fromObject(json);
    if(jsonObj.containsKey(InputDataVO.UNIQUEKEY)){
    	 this.checkUnique(jsonObj.getString(InputDataVO.UNIQUEKEY));
    }
    
    // 构建返回信息
    return this.buildReturnInfo(vos);
  }
  
  //检查唯一性
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
			throw new BusinessException("U8C根据传入的uniquekey("+uniqueKey+")判断当前操作为重复操作");
		}
		// TODO Auto-generated catch block
		throw new BusinessException(e.getMessage());
	}
	  
  }
  
  @Override
  public String doAction(String[][] datas) throws BusinessException {
    // 转换
    E[] vos = this.convert(datas);
    // 翻译
    this.translate(vos);
    // 业务操作
    vos = this.businessProcess(vos);
    // 构建返回信息
    return this.buildReturnInfo(vos);

  }

  /**
   * 将传入的json，转换成vo，翻译，保存
   * 
   * @param json
   * @param tranType
   * @return
   * @throws BusinessException
   * @throws ConfigException
   */
  public E[] save(String json, String tranType) throws BusinessException,
      ConfigException {
	
    // 适配API Link代码
    String jsonStr = this.getJSONFromAPILink(json);
    // 转换
    E[] vos = this.convert(jsonStr);
    // 翻译
    if (tranType == null || TranType.CODE.toString().equals(tranType)) {
      this.translate(vos);
    }
    UFBoolean needMiddleFile = this.getGlobalParameter().getNeedMiddleFile();
    // 根据参数留下翻译后的文件
    if (needMiddleFile.booleanValue()) {
      this.writeMiddleFile(vos, APIConst.TRANSLATEDPATH);
    }
    // 业务操作
    vos = this.businessProcess(vos);
    // 根据参数留下数据库的文件
    if (needMiddleFile.booleanValue()) {
      this.writeMiddleFile(vos, APIConst.RETURNDATAPATH);
    }
    return vos;
  }

  /**
   * 设置参数可以保存落地文件
   * 
   * @return
   * @throws ConfigException
   */
  private GlobalSetVO getGlobalParameter() throws ConfigException {
    return NCLocator.getInstance().lookup(IAPIConfigFileService.class)
        .getGlobalParameter();

  }

  /**
   * 由于从apilink平台过来的数据外层多了{bill：[]}
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
    // 对转入的类型做个判断统一以JSONArray方式传入
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
   * 转换json成单据
   * 
   * @param json
   * @return E[]
   * @throws BusinessException
   */
  protected abstract E[] convert(String json) throws BusinessException;

  /**
   * 转换二维数组成单据
   * 
   * @param datas
   * @return
   * @throws BusinessException
   */
  protected abstract E[] convert(String[][] datas) throws BusinessException;

  /**
   * 单据标识
   * 
   * @return String
   */
  protected abstract String getBillMark();

  /**
   * 翻译成json
   * 
   * @param vos
   * @return
   * @throws BusinessException
   */
  protected abstract String getJsonStrFromVOS(E[] vos) throws BusinessException;

  /**
   * 业务保存
   * 
   * @param vos
   * @return String
   * @throws BusinessException
   */
  protected abstract E[] save(E[] vos) throws BusinessException;

  /**
   * 默认的翻译
   * 
   * @param vos
   * @param tranType
   * @throws BusinessException
   */
  protected abstract void translate(E[] vos) throws BusinessException;

}
