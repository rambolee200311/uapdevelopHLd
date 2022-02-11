package u8c.vo.entity;

import net.sf.json.JSONObject;

/**
 * @������������������vo
 * @since u8c2.1
 * @version 2018-4-25 ����3:02:54
 * @author xuxq3
 */

public class InputDataVO extends APIPubVO {
	
  //�ݵ�key
  public final static String UNIQUEKEY = "uniquekey";

  /** �������� */
  public final static String INDATA = "indata";

  /** �Ƿ���Ҫ���� */
  public final static String ISENCRYPT = "isEncrypt";

  /** �Ƿ���Ҫ��ջ��Ϣ */
  public final static String NEEDSTACKTRACE = "needStackTrace";

  /** ���� */
  public final static String PASSWORD = "password";

  /** ·����Ϣ */
  public final static String PATHINFO = "pathInfo";

  /** ϵͳ���� */
  public final static String SYSTEM = "system";

  /** ���뷽ʽ */
  public final static String TRANTYPE = "trantype";

  /** ���õ�URL */
  public final static String URL = "url";

  /** �û� */
  public final static String USERCODE = "usercode";

  private static final long serialVersionUID = 7622095588264291302L;

  /** ��ȡ�������� */
  public String getIndata() {
	return(String) this.getAttributeValue(INDATA);
  }

  /** ��ȡ�Ƿ���Ҫ��ջ��Ϣ */
  public String getNeedStackTrace() {
    return (String) this.getAttributeValue(NEEDSTACKTRACE);
  }

  /** ��ȡ���� */
  public String getPassword() {
    return (String) this.getAttributeValue(PASSWORD);
  }

  /** ��ȡ·����Ϣ */
  public String getPathInfo() {
    return (String) this.getAttributeValue(PATHINFO);
  }

  /** ��ȡϵͳ���� */
  public String getSystem() {
    return (String) this.getAttributeValue(SYSTEM);
  }

  /** �ݵ�key */
  public String getUniquekey() {
    return (String) this.getAttributeValue(UNIQUEKEY);
  }
  
  /** �ݵ�key */
  public void setUniquekey(String key ) {
     this.setAttributeValue(UNIQUEKEY, key);
  }

  /** ��ȡ���뷽ʽ */
  public String getTrantype() {
    return (String) this.getAttributeValue(TRANTYPE);
  }
  
  /** ��ȡ���õ�URL */
  public String getUrl() {
    return (String) this.getAttributeValue(URL);
  }

  /** ��ȡ�û� */
  public String getUsercode() {
    return (String) this.getAttributeValue(USERCODE);
  }

  /** �Ƿ���Ҫ���� */
  public String isEncrypt() {
    return (String) this.getAttributeValue(ISENCRYPT);
  }

  /** ���ô������� */
  public void setIndata(String indata) {
    this.setAttributeValue(INDATA, indata);
  }

  /** �����Ƿ���Ҫ���� */
  public void setIsEncrypt(String isEncrypt) {
    this.setAttributeValue(ISENCRYPT, isEncrypt);
  }

  /** �����Ƿ���Ҫ��ջ��Ϣ */
  public void setNeedStackTrace(String needStackTrace) {
    this.setAttributeValue(NEEDSTACKTRACE, needStackTrace);
  }

  /** �������� */
  public void setPassword(String password) {
    this.setAttributeValue(PASSWORD, password);
  }

  /** ����·����Ϣ */
  public void setPathInfo(String pathInfo) {
    this.setAttributeValue(PATHINFO, pathInfo);
  }

  /** ����ϵͳ���� */
  public void setSystem(String system) {
    this.setAttributeValue(SYSTEM, system);
  }

  /** ���÷��뷽ʽ */
  public void setTrantype(String trantype) {
    this.setAttributeValue(TRANTYPE, trantype);
  }

  /** ���õ��õ�URL */
  public void setUrl(String url) {
    this.setAttributeValue(URL, url);
  }

  /** �����û� */
  public void setUsercode(String usercode) {
    this.setAttributeValue(USERCODE, usercode);
  }

}
