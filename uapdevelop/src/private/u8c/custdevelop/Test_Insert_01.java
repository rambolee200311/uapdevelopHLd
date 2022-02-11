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
 * @description 	XXX�����ӿڣ��ͻ�����������ʾ��
 * @author 			liuyxp
 * @date			2021-5-14 ����10:13:10
 */
public class Test_Insert_01 implements IAPICustmerDevelop{

	@Override
	public String doAction(HttpServletRequest request) throws BusinessException, ConfigException {
		// ��һ������������
		String obj = null;
		try {
			obj = this.getRequestPostStr(request.getInputStream());
			// �ڶ�������������֮����������Ŀ���Լ���ҵ����
			JSONObject parameJson = JSONObject.parseObject(obj);
			// �����õ���������ʾ��
			AreaclVO areavo = JSON.toJavaObject(parameJson, AreaclVO.class);
			// �����������浽���ݿ�
			IAreaClassAndAddressOperate iAeal = NCLocator.getInstance().lookup(IAreaClassAndAddressOperate.class);
			iAeal.insertAreacl(areavo);
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
