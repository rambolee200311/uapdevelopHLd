package u8c.custdevelop;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import javax.servlet.http.HttpServletRequest;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import nc.bs.framework.common.NCLocator;
import nc.itf.uap.bd.area.IAreaClassAndAddressOperate;
import nc.vo.bd.b07.AreaclVO;
import nc.vo.pub.BusinessException;
import u8c.bs.exception.ConfigException;
import u8c.pubitf.action.IAPICustmerDevelop;


/**
 * @description 	XXX新增接口，客户化开发代码示例
 * @author 			liuyxp
 * @date			2021-5-14 上午10:13:10
 */
public class Test_Insert_01 implements IAPICustmerDevelop{

	@Override
	public String doAction(HttpServletRequest request) throws BusinessException, ConfigException {
		// 第一步：解析数据
		String obj = null;
		try {
			obj = this.getRequestPostStr(request.getInputStream());
			// 第二步：解析数据之后做后续项目上自己的业务处理
			JSONObject parameJson = JSONObject.parseObject(obj);
			// 这里用地区分类做示例
			AreaclVO areavo = JSON.toJavaObject(parameJson, AreaclVO.class);
			// 第三步：保存到数据库
			IAreaClassAndAddressOperate iAeal = NCLocator.getInstance().lookup(IAreaClassAndAddressOperate.class);
			iAeal.insertAreacl(areavo);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "新增地区分类接口保存成功，解析到的数据是" + obj;
	}

	private String getRequestPostStr(InputStream tInputStream) throws IOException {
		String retStr = null;
		if (tInputStream != null) {
			BufferedReader br = null;
			InputStreamReader isr = null;
			try {
				isr = new InputStreamReader(tInputStream, "UTF-8");// 字符流
				br = new BufferedReader(isr);// 缓冲流
				StringBuffer tStringBuffer = new StringBuffer();
				String sTempOneLine = new String("");
				while ((sTempOneLine = br.readLine()) != null) {
					tStringBuffer.append(sTempOneLine);
				}
				retStr = tStringBuffer.toString();
			} finally {
				if (null != br) {
					br.close();
				}
				if (null != isr) {
					isr.close();
				}
			}
		}
		return retStr;
	}

}
