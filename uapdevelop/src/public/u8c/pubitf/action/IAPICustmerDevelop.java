package u8c.pubitf.action;

import javax.servlet.http.HttpServletRequest;

import nc.vo.pub.BusinessException;
import u8c.bs.exception.ConfigException;


/**
 * @description 	�ͻ���API�ӿڿ����Ľӿ���
 * @author 			liuyxp
 * @date			2021-5-13 ����7:47:58
 */
public interface IAPICustmerDevelop {
	
	public String doAction(HttpServletRequest request) throws BusinessException, ConfigException;

}
