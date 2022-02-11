package u8c.vo.entity;

import net.sf.json.JSONObject;

/**
 * @功能描述：传入数据vo
 * @since u8c2.1
 * @version 2018-4-25 下午3:02:54
 * @author xuxq3
 */

public class InputDataVO extends APIPubVO {
	
  //幂等key
  public final static String UNIQUEKEY = "uniquekey";

  /** 传入数据 */
  public final static String INDATA = "indata";

  /** 是否需要加密 */
  public final static String ISENCRYPT = "isEncrypt";

  /** 是否需要堆栈信息 */
  public final static String NEEDSTACKTRACE = "needStackTrace";

  /** 密码 */
  public final static String PASSWORD = "password";

  /** 路径信息 */
  public final static String PATHINFO = "pathInfo";

  /** 系统编码 */
  public final static String SYSTEM = "system";

  /** 翻译方式 */
  public final static String TRANTYPE = "trantype";

  /** 调用的URL */
  public final static String URL = "url";

  /** 用户 */
  public final static String USERCODE = "usercode";

  private static final long serialVersionUID = 7622095588264291302L;

  /** 获取传入数据 */
  public String getIndata() {
	return(String) this.getAttributeValue(INDATA);
  }

  /** 获取是否需要堆栈信息 */
  public String getNeedStackTrace() {
    return (String) this.getAttributeValue(NEEDSTACKTRACE);
  }

  /** 获取密码 */
  public String getPassword() {
    return (String) this.getAttributeValue(PASSWORD);
  }

  /** 获取路径信息 */
  public String getPathInfo() {
    return (String) this.getAttributeValue(PATHINFO);
  }

  /** 获取系统编码 */
  public String getSystem() {
    return (String) this.getAttributeValue(SYSTEM);
  }

  /** 幂等key */
  public String getUniquekey() {
    return (String) this.getAttributeValue(UNIQUEKEY);
  }
  
  /** 幂等key */
  public void setUniquekey(String key ) {
     this.setAttributeValue(UNIQUEKEY, key);
  }

  /** 获取翻译方式 */
  public String getTrantype() {
    return (String) this.getAttributeValue(TRANTYPE);
  }
  
  /** 获取调用的URL */
  public String getUrl() {
    return (String) this.getAttributeValue(URL);
  }

  /** 获取用户 */
  public String getUsercode() {
    return (String) this.getAttributeValue(USERCODE);
  }

  /** 是否需要加密 */
  public String isEncrypt() {
    return (String) this.getAttributeValue(ISENCRYPT);
  }

  /** 设置传入数据 */
  public void setIndata(String indata) {
    this.setAttributeValue(INDATA, indata);
  }

  /** 设置是否需要加密 */
  public void setIsEncrypt(String isEncrypt) {
    this.setAttributeValue(ISENCRYPT, isEncrypt);
  }

  /** 设置是否需要堆栈信息 */
  public void setNeedStackTrace(String needStackTrace) {
    this.setAttributeValue(NEEDSTACKTRACE, needStackTrace);
  }

  /** 设置密码 */
  public void setPassword(String password) {
    this.setAttributeValue(PASSWORD, password);
  }

  /** 设置路径信息 */
  public void setPathInfo(String pathInfo) {
    this.setAttributeValue(PATHINFO, pathInfo);
  }

  /** 设置系统编码 */
  public void setSystem(String system) {
    this.setAttributeValue(SYSTEM, system);
  }

  /** 设置翻译方式 */
  public void setTrantype(String trantype) {
    this.setAttributeValue(TRANTYPE, trantype);
  }

  /** 设置调用的URL */
  public void setUrl(String url) {
    this.setAttributeValue(URL, url);
  }

  /** 设置用户 */
  public void setUsercode(String usercode) {
    this.setAttributeValue(USERCODE, usercode);
  }

}
