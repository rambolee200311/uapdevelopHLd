package u8c.busiitf.task;

import java.util.LinkedHashMap;

import nc.bs.pub.taskcenter.BgWorkingContext;
import nc.vo.pub.BusinessException;

public class XmlConfigTestTask   implements nc.bs.pub.taskcenter.IBackgroundWorkPlugin {
	@Override
	public String executeTask(BgWorkingContext param) throws BusinessException {
		String strResult="";
		LinkedHashMap<String, Object> para = param.getKeyMap();
		strResult = (String) para.get("temp");
		strResult=u8c.server.XmlConfig.getUrl(strResult);
		return strResult;
	}
}
