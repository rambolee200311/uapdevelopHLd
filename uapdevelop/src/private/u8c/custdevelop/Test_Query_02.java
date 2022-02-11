package u8c.custdevelop;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

import nc.bs.dao.BaseDAO;
import nc.jdbc.framework.processor.MapProcessor;
import nc.vo.pub.BusinessException;

import com.alibaba.fastjson.JSONObject;
import u8c.bs.exception.ConfigException;
import u8c.pubitf.action.IAPICustmerDevelop;


/**
 * @description 	XXX查询接口，客户化开发代码示例
 * @author 			liuyxp
 * @date			2021-5-14 上午10:47:45
 */
public class Test_Query_02 implements IAPICustmerDevelop{
	
	private BaseDAO baseDAO;
	
	public BaseDAO getBaseDAO() {
		if(baseDAO == null){
			baseDAO = new BaseDAO();
		}
		return baseDAO;
	}

	@Override
	public String doAction(HttpServletRequest request) throws BusinessException, ConfigException {
		// 第一步：解析数据
		String obj = null;
		try {
			obj = this.getRequestPostStr(request.getInputStream());
			// 第二步：解析数据之后做后续项目上自己的业务处理
			JSONObject parameJson = JSONObject.parseObject(obj);
			// 查询的需求是根据编码、公司查询
			String sSql = "select * from bd_areacl where areaclcode = '" + parameJson.get("areaclcode")
					+ "' and pk_corp = '" + parameJson.get("pk_corp") + "'";
			// 第三步：执行查询
			Map<String, Object> mepreturn = (Map<String, Object>) getBaseDAO().executeQuery(sSql, new MapProcessor());
			// 校验结果
			if (mepreturn != null) {
				return "地区分类查询接口，解析到的数据是" + obj + "---查询结果---" + mepreturn.toString();
			}else {
				return "地区分类查询接口，解析到的数据是" + obj + " ---提示信息--- 但是根据条件没有查询到数据，请检查传的参数。";
			}
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
