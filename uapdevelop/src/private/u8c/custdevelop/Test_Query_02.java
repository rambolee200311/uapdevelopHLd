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
 * @description 	XXX��ѯ�ӿڣ��ͻ�����������ʾ��
 * @author 			liuyxp
 * @date			2021-5-14 ����10:47:45
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
		// ��һ������������
		String obj = null;
		try {
			obj = this.getRequestPostStr(request.getInputStream());
			// �ڶ�������������֮����������Ŀ���Լ���ҵ����
			JSONObject parameJson = JSONObject.parseObject(obj);
			// ��ѯ�������Ǹ��ݱ��롢��˾��ѯ
			String sSql = "select * from bd_areacl where areaclcode = '" + parameJson.get("areaclcode")
					+ "' and pk_corp = '" + parameJson.get("pk_corp") + "'";
			// ��������ִ�в�ѯ
			Map<String, Object> mepreturn = (Map<String, Object>) getBaseDAO().executeQuery(sSql, new MapProcessor());
			// У����
			if (mepreturn != null) {
				return "���������ѯ�ӿڣ���������������" + obj + "---��ѯ���---" + mepreturn.toString();
			}else {
				return "���������ѯ�ӿڣ���������������" + obj + " ---��ʾ��Ϣ--- ���Ǹ�������û�в�ѯ�����ݣ����鴫�Ĳ�����";
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "������������ӿڱ���ɹ�����������������" + obj;
	}

	private String getRequestPostStr(InputStream tInputStream) throws IOException {
		String retStr = null;
		if (tInputStream != null) {
			BufferedReader br = null;
			InputStreamReader isr = null;
			try {
				isr = new InputStreamReader(tInputStream, "UTF-8");// �ַ���
				br = new BufferedReader(isr);// ������
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
