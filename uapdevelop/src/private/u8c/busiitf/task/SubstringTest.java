package u8c.busiitf.task;

import java.util.LinkedHashMap;

import nc.bs.logging.Logger;
import nc.bs.pub.taskcenter.BgWorkingContext;
import nc.vo.pub.BusinessException;
import u8c.bs.exception.SecurityException;
import u8c.vo.arrival.EncryptHelper;

public class SubstringTest  implements nc.bs.pub.taskcenter.IBackgroundWorkPlugin {
	@Override
	public String executeTask(BgWorkingContext param) throws BusinessException {
		String strResult="";
		Logger.init("hanglianAPI");
		LinkedHashMap<String, Object> para = param.getKeyMap();
		strResult = (String) para.get("temp");
		Logger.debug(strResult);
		//jie��
		//strResult="һ�����������߰˾�ʮһ�����������߰˾�ʮ";
		
		try {
			strResult=getSubString(strResult,30);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Logger.error(e.getMessage(),e);
			e.printStackTrace();
		}
		Logger.debug(strResult);
		return strResult;
	}
	public String getSubString(String str, int length) throws Exception {
		int i;	
		int n;	
		byte[] bytes = str.getBytes("Unicode");      //ʹ��Unicode�ַ������ַ��������byte����	
		i = 2;      //bytes��ǰ�����ֽ��Ǳ�־λ��bytes[0] = -2, bytes[1] = -1, �ʴӵڶ�λ��ʼ	
		n = 0;	
		for(i=2; i < bytes.length && n < length;i++) {	
			if(i % 2 == 1) {	
				n++;	
			} else {	
				if(bytes[i] != 0) {	
					n++;	
				}	
			}	
		}
	
		//ȥ���������
	
		if(i % 2 == 1) {	
			if(bytes[i-1] != 0) {	
				i = i -1;	
			} else {	
				i = i + 1;	
			}	
		}	 
	
		return new String(bytes, 0, i, "Unicode");

	}
}