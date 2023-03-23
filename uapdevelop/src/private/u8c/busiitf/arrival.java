package u8c.busiitf;
import java.util.HashMap;
import java.util.Map;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import nc.bs.dao.BaseDAO;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDateTime;
import u8c.bs.APIConst;
import u8c.bs.exception.ConfigException;
import u8c.bs.exception.SecurityException;
import u8c.bs.utils.FileUtils;
import u8c.pubitf.action.IAPICustmerDevelop;
import u8c.vo.comfirmArrival.ConfirmArrivalMessage;
import u8c.vo.comfirmArrival.ConfirmArrivalData;
import u8c.vo.arrival.EncryptHelper;
import u8c.vo.comfirmArrival.BillRootVO;
import u8c.vo.comfirmArrival.BillVO;
import u8c.vo.comfirmArrival.ConfirmArrivalBody;
import u8c.vo.comfirmArrival.ParentVO;
import u8c.vo.comfirmArrival.ChildrenVO;
import u8c.vo.comfirmArrival.PostResult;
import u8c.vo.pub.APIMessageVO;
import u8c.server.HttpURLConnectionDemo;
public class arrival implements IAPICustmerDevelop{
	private BaseDAO dao; 
	private BaseDAO getDao() {
		if (dao == null) {
			dao = new BaseDAO();
		}
		return dao;
	}
	@Override
	public String doAction(HttpServletRequest request)  throws BusinessException, ConfigException{
		// ��һ������������
		String obj = "";
		String strBody = "";
		String strTemp = "";
		APIMessageVO messageVO = new APIMessageVO();
		try {
			// ��ʼ������	
			obj = this.getRequestPostStr(request.getInputStream());
			// �ڶ�������������֮����������Ŀ���Լ���ҵ����
			JSONObject parameJson = JSONObject.parseObject(obj);
						
			ConfirmArrivalMessage confirmArrivalMessage= JSON.toJavaObject(parameJson, ConfirmArrivalMessage.class);
			ConfirmArrivalData confirmArrivalData=confirmArrivalMessage.getMessage();
			obj=confirmArrivalData.getData();
			
			//����������
			EncryptHelper encryptHelper=new EncryptHelper();
			strBody=encryptHelper.decrypt(obj);
			strTemp+=obj+"\n \n "+strBody; 
			// д�������м��ļ�
			writeMiddleFile(APIConst.INDOCPATH + "u8c.busiitf.arrival", strTemp);
			
			List<ConfirmArrivalBody> listConfirmArrivalBody=JSON.parseArray(strBody,ConfirmArrivalBody.class);
			List<PostResult> listPostResult=new ArrayList();//���ؽ��
			ConfirmArrivalData dataResult=new ConfirmArrivalData();	
			
			for(ConfirmArrivalBody confirmArrivalBody:listConfirmArrivalBody){
				PostResult postResult=setPostResultYF(confirmArrivalBody);
				listPostResult.add(postResult);
				if (postResult.getStatus().equals("success")){
					PostResult postResult1=setPostResultSKHZ(confirmArrivalBody);
					listPostResult.add(postResult1);
				}
				
			}
			// �����������ؽ��
			obj=JSON.toJSONString(listPostResult);
			strTemp+="\n \n "+obj;
			strBody=encryptHelper.encrypt(obj);
			dataResult.setData(strBody);
			//encryptHelper.decrypt(strBody);
			strBody=JSON.toJSONString(dataResult);
			strTemp+="\n \n "+strBody;
			// д�������м��ļ�
			writeMiddleFile(APIConst.RETURNDATAPATH + "u8c.busiitf.arrival",strTemp);
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return strBody;
	}
	//����data

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
	
	private BillVO getBillVO(){
		BillVO billVO=new BillVO();
		//parentvo
		ParentVO parentVO=new ParentVO();
		parentVO.setBzbm("CNY");
		
		//childrenvo
		List<ChildrenVO> listChildrenVO=new ArrayList();
		ChildrenVO childrenVO=new ChildrenVO();
		childrenVO.setHbbm("01000078");
		
		listChildrenVO.add(childrenVO);
		
		billVO.setParentvo(parentVO);
		billVO.setChildren(listChildrenVO);
		return billVO;
	}
	//���������տ���� F2-03
	private PostResult setPostResultSKHZ(ConfirmArrivalBody confirmArrivalBody){
		PostResult postResult=new PostResult();
		postResult.setBillID(confirmArrivalBody.getBillID());
		postResult.setArrivalRegiCode(confirmArrivalBody.getArrivalRegiCode());
		String strBody="";
		try{
			// ��һ������װ����
			BillRootVO billRootVO=new BillRootVO();			
			List<BillVO> listBillVO=new ArrayList();
			String sql="select custcode from bd_cubasdoc a" 
				+" inner join arap_djfb b on a.pk_cubasdoc=b.hbbm"
				+" inner join arap_djzb c on b.vouchid=c.vouchid"
				+" where c.vouchid='"+confirmArrivalBody.getArrivalRegiCode()+"'";
			String custcode="";
			custcode=(String)getDao().executeQuery(sql, new ColumnProcessor());
			//���������տ�
			BillVO billVO=new BillVO();		
			//����ͷ
			ParentVO parentvo=new ParentVO();
			parentvo.setBzbm(confirmArrivalBody.getCurrency());			
			parentvo.setDjlxbm("F2-03");
			parentvo.setDjrq(confirmArrivalBody.getBillDate());
			parentvo.setDwbm(confirmArrivalBody.getComCode());
			//parentvo.setHbbm(confirmArrivalBody.getPayerCode());
			parentvo.setHbbm(custcode);
			parentvo.setLrr("13501036623");
			parentvo.setPrepay(false);
			parentvo.setScomment(confirmArrivalBody.getZyx1());
			parentvo.setWldx("0");
			parentvo.setXslxbm("ap01");
			parentvo.setZyxl(confirmArrivalBody.getBillID());
			parentvo.setZyx2(confirmArrivalBody.getArrivalRegiCode());
			billVO.setParentvo(parentvo);
			//������
			List<ChildrenVO> children=new ArrayList();
			ChildrenVO childrenvo=new ChildrenVO();
			if (!confirmArrivalBody.getCurrency().equals("CNY")){
				childrenvo.setBzbm(confirmArrivalBody.getCurrency());
				childrenvo.setBbhl(confirmArrivalBody.getCurRate());
			}else{
				childrenvo.setDfbbje(confirmArrivalBody.getArrivalRMB()*-1);
			}
			childrenvo.setDfybje(confirmArrivalBody.getArrivalAmount()*-1);
			
			//childrenvo.setHbbm(confirmArrivalBody.getApCode());
			childrenvo.setHbbm(custcode);
			children.add(childrenvo);
			billVO.setChildren(children);
			listBillVO.add(billVO);
			billRootVO.setBillvo(listBillVO);
			
			// �ڶ������ύ��API				
			// ���������ʵ�ַ���˿�,���� http://ip:port
			String serviceUrl =u8c.server.XmlConfig.getUrl("u8carapskinsert");
			//"http://127.0.0.1:9099/u8cloud/api/arap/sk/insert";
			// ʹ��U8cloudϵͳ�����ã�����ڵ�·��Ϊ��
			// Ӧ�ü��� - ϵͳ����ƽ̨ - ϵͳ��Ϣ����
			// ������Ϣ�о������ԵĶ��չ�ϵ���£�
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("trantype", "code"); // �������뷽ʽ��ö��ֵΪ��������¼�� code�� ������¼�� name�� ������¼�� pk
			map.put("system", "busiitf"); // ϵͳ����
			map.put("usercode", "busiuser"); // �û�
			map.put("password", "bbbed85aa52a7dc74fc4b4bca8423394"); // ����1qazWSX����Ҫ MD5 ���ܺ�¼��				
			map.put("uniquekey",confirmArrivalBody.getBillID());
			strBody=HttpURLConnectionDemo.operator(serviceUrl, map,JSON.toJSONString(billRootVO));
			
			// ��������������	
			JSONObject jsonResult =JSON.parseObject(strBody);
			u8c.vo.comfirmArrival.DataResponse dataResponse=JSON.toJavaObject(jsonResult, u8c.vo.comfirmArrival.DataResponse.class);		
			if (dataResponse.getStatus().equals("success")){// �����ķ���
				postResult.setStatus(dataResponse.getStatus());		
				List<BillVO> billvoResult=JSON.parseArray(dataResponse.getData(),BillVO.class);
				postResult.setU8cCode(billvoResult.get(0).getParentvo().getDjbh());
			}else{// �쳣�ķ���
				//postResult.setStatus(dataResponse.getStatus());
				postResult.setStatus("fail");
				postResult.setU8cCode(dataResponse.getErrorcode()+"-"+dataResponse.getErrormsg());
			}
		}catch(Exception e){
			postResult.setStatus("fail");			
			postResult.setU8cCode(e.getMessage());
			e.printStackTrace();
		}
		return postResult;
	}
	//��������Ӧ�� F1-01
	private PostResult setPostResultYF(ConfirmArrivalBody confirmArrivalBody){
		PostResult postResult=new PostResult();
		postResult.setBillID(confirmArrivalBody.getBillID());
		postResult.setArrivalRegiCode(confirmArrivalBody.getArrivalRegiCode());
		String strBody="";
		try{
			// ��һ������װ����
			BillRootVO billRootVO=new BillRootVO();			
			List<BillVO> listBillVO=new ArrayList();
			//��������Ӧ��
			BillVO billVO1=new BillVO();
			//����ͷ
			ParentVO parentvo1=new ParentVO();
			parentvo1.setBzbm(confirmArrivalBody.getCurrency());			
			parentvo1.setDjlxbm("F1-01");
			parentvo1.setDjrq(confirmArrivalBody.getBillDate());
			parentvo1.setDwbm(confirmArrivalBody.getComCode());
			parentvo1.setHbbm(confirmArrivalBody.getPayerCode());
			parentvo1.setLrr("13501036623");
			parentvo1.setPrepay(false);
			parentvo1.setScomment(confirmArrivalBody.getZyx1());
			parentvo1.setWldx("0");
			parentvo1.setXslxbm("ap01");
			parentvo1.setZyxl(confirmArrivalBody.getBillID());
			parentvo1.setZyx2(confirmArrivalBody.getArrivalRegiCode());
			billVO1.setParentvo(parentvo1);
			//������
			List<ChildrenVO> children1=new ArrayList();
			ChildrenVO childrenvo1=new ChildrenVO();
			if (!confirmArrivalBody.getCurrency().equals("CNY")){
				childrenvo1.setBzbm(confirmArrivalBody.getCurrency());
				childrenvo1.setBbhl(confirmArrivalBody.getCurRate());
			}else{
				childrenvo1.setDfbbje(confirmArrivalBody.getArrivalRMB());
			}
			childrenvo1.setDfybje(confirmArrivalBody.getArrivalAmount());
			
			childrenvo1.setHbbm(confirmArrivalBody.getApCode());
			children1.add(childrenvo1);
			billVO1.setChildren(children1);
			
			listBillVO.add(billVO1);
			
			billRootVO.setBillvo(listBillVO);
			
			// �ڶ������ύ��API				
			// ���������ʵ�ַ���˿�,���� http://ip:port
			String serviceUrl =u8c.server.XmlConfig.getUrl("u8carapyfinsert");
			//"http://127.0.0.1:9099/u8cloud/api/arap/yf/insert";
			// ʹ��U8cloudϵͳ�����ã�����ڵ�·��Ϊ��
			// Ӧ�ü��� - ϵͳ����ƽ̨ - ϵͳ��Ϣ����
			// ������Ϣ�о������ԵĶ��չ�ϵ���£�
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("trantype", "code"); // �������뷽ʽ��ö��ֵΪ��������¼�� code�� ������¼�� name�� ������¼�� pk
			map.put("system", "busiitf"); // ϵͳ����
			map.put("usercode", "busiuser"); // �û�
			map.put("password", "bbbed85aa52a7dc74fc4b4bca8423394"); // ����1qazWSX����Ҫ MD5 ���ܺ�¼��				
			map.put("uniquekey",confirmArrivalBody.getBillID());
			strBody=HttpURLConnectionDemo.operator(serviceUrl, map,JSON.toJSONString(billRootVO));
			
			// ��������������	
			JSONObject jsonResult =JSON.parseObject(strBody);
			u8c.vo.comfirmArrival.DataResponse dataResponse=JSON.toJavaObject(jsonResult, u8c.vo.comfirmArrival.DataResponse.class);		
			if (dataResponse.getStatus().equals("success")){// �����ķ���
				postResult.setStatus(dataResponse.getStatus());		
				List<BillVO> billvoResult=JSON.parseArray(dataResponse.getData(),BillVO.class);
				postResult.setU8cCode(billvoResult.get(0).getParentvo().getDjbh());
			}else{// �쳣�ķ���
				//postResult.setStatus(dataResponse.getStatus());
				postResult.setStatus("fail");
				postResult.setU8cCode(dataResponse.getErrorcode()+"-"+dataResponse.getErrormsg());
			}
			}catch(Exception e){
				postResult.setStatus("fail");			
				postResult.setU8cCode(e.getMessage());
				e.printStackTrace();
			}
			return postResult;
		}
	protected void writeMiddleFile(String path, String info) throws IOException,
    UnsupportedEncodingException {
	  String[] date =
	      new UFDateTime(System.currentTimeMillis()).toString().split(" ");
	  String fileName =
	      path + "-" + date[0] + "-" + date[1].replaceAll(":", "-") + ".txt";
	  FileUtils util = new FileUtils();
	  info=date[0]+" " +date[1]+"\n \n "+info;
	  if (info != null) {
	    util.writeBytesToFile(info.getBytes("UTF-8"), fileName);
	  }
}
}

