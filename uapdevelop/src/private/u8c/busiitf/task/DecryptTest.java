package u8c.busiitf.task;

import java.util.LinkedHashMap;

import u8c.bs.exception.SecurityException;
import u8c.vo.arrival.EncryptHelper;
import com.alibaba.fastjson.JSON;

import nc.bs.logging.Logger;
import nc.bs.pub.taskcenter.BgWorkingContext;
import nc.vo.pub.BusinessException;

public class DecryptTest implements nc.bs.pub.taskcenter.IBackgroundWorkPlugin {
	@Override
	public String executeTask(BgWorkingContext param) throws BusinessException {
		String strResult="";
		Logger.init("hanglianAPI");
		LinkedHashMap<String, Object> para = param.getKeyMap();
		strResult = (String) para.get("temp");
		strResult="[{'adviceNote': 'sn00944seb','adviceDate': '2021-12-22','reverseInvoiceNo': '34432133432','inclusiveMoney': 6000,'inclusiveRMB': 6000,'currency': 'CNY','reverseInclusiveMoney': -6000,'reverseInclusiveRMB': -6000,'operatorCode': '13810812328','comCode': 'A01','comName': '航联保险经纪有限公司','busiType': 1}]";
		Logger.debug(strResult);
		//jie密
		EncryptHelper encryptHelper=new EncryptHelper();
		try {
			strResult=encryptHelper.decrypt(strResult);
		} catch (SecurityException e) {
			Logger.error(e.getMessage(),e);
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Logger.debug(strResult);
		return strResult;
	}
}
