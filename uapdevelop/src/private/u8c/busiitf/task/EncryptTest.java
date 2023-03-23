package u8c.busiitf.task;
import java.util.LinkedHashMap;

import u8c.bs.exception.SecurityException;
import u8c.vo.arrival.EncryptHelper;
import com.alibaba.fastjson.JSON;

import nc.bs.logging.Logger;
import nc.bs.pub.taskcenter.BgWorkingContext;
import nc.vo.pub.BusinessException;

public class EncryptTest implements nc.bs.pub.taskcenter.IBackgroundWorkPlugin {
	@Override
	public String executeTask(BgWorkingContext param) throws BusinessException {
		String strResult="";
		Logger.init("hanglianAPI");
		LinkedHashMap<String, Object> para = param.getKeyMap();
		//strResult = (String) para.get("temp");
		//strResult="[{'adviceNote': 'sn00944seb','adviceDate': '2022-03-05','reverseInvoiceNo': '777777','inclusiveMoney': 6000,'inclusiveRMB': 6000,'currency': 'CNY','reverseInclusiveMoney': -6000,'reverseInclusiveRMB': -6000,'operatorCode': '13810812328','comCode': 'A01','comName': '航联保险经纪有限公司','busiType': 1}]";
		//strResult="[{'billID': 'abcd-435',	'billDate': '2022-03-20','arrivalRegiCode': '1001ZZ1000000000291W','payerCode': '05000003','payerName': '澳门航空公司','operatorCode': '13810812328','comCode': 'A01','comName': '航联保险经纪有限公司','zyx1': 'ABC','zyx2': 'BDE','busiType': 3,'apCode': '01000078','apName': '深圳机场航空城运营管理有限公司','currency': 'CNY','arrivalAmount': 7000.000,'curRate': 1,'arrivalRMB': 7000.000,'dpName': '信息管理部','smName': '武勇','pmName': 'AAA项目'}]";
		strResult="[{'bank': [{'bkCode': '103437605216','bkName': '中国农业银行乐安县支行','acCode': '123456789012'}],'comCode': 'A01','comName': '航联保险经纪有限公司','conAddr': '北京','custCode': 'DWKHODTE20221111092612','custName': '测试客户111101','phone1': '13501023321','phone2': '13601089967','zyx1': '','zyx2': '','zyx3': '','zyx4': '','zyx5': ''}]";
		strResult=" [{'bank':[{'bkCode': '103437605216','bkName': '中国农业银行乐安县支行','acCode': '123456789014'},{'bkCode': '103437605216','bkName': '中国农业银行乐安县支行','acCode': '123456789013'}],'comCode':'A0101','comName':'航联保险经纪有限公司','conAddr':'北京市朝阳区航安路23号','custCode':'DWKHWU8D20221021100534','custName':'北京首都机场物业管理有限公司','phone1':'4455677','phone2':'334244242','zyx1':'','zyx2':'','zyx3':'','zyx4':'','zyx5':''}]";
		Logger.debug(strResult);
		//jia密
		EncryptHelper encryptHelper=new EncryptHelper();
		try {
			strResult=encryptHelper.encrypt(strResult);
		} catch (SecurityException e) {
			Logger.error(e.getMessage(),e);
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Logger.debug(strResult);
		return strResult;
	}
}