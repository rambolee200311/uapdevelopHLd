package u8c.pubitf.action;

import u8c.bs.exception.ConfigException;

import nc.vo.pub.BusinessException;

/**
 * @功能描述： 操作接口
 * @author xuxq3
 * @date 2018-1-5 下午12:55:55
 */
public interface IAction {

  public String doAction(String json, String tranType)
      throws BusinessException, ConfigException;
}
