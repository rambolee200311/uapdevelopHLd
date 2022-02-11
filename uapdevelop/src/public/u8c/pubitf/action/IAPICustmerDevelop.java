package u8c.pubitf.action;

import javax.servlet.http.HttpServletRequest;

import nc.vo.pub.BusinessException;
import u8c.bs.exception.ConfigException;


/**
 * @description 	客户化API接口开发的接口类
 * @author 			liuyxp
 * @date			2021-5-13 下午7:47:58
 */
public interface IAPICustmerDevelop {
	
	public String doAction(HttpServletRequest request) throws BusinessException, ConfigException;

}
