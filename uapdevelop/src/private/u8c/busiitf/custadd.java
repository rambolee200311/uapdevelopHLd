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
import nc.bs.dao.DAOException;
import nc.bs.logging.Logger;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDateTime;
import u8c.bs.APIConst;
import u8c.bs.exception.ConfigException;
import u8c.bs.exception.SecurityException;
import u8c.bs.utils.FileUtils;
import u8c.pubitf.action.IAPICustmerDevelop;
import u8c.vo.pub.APIMessageVO;
import u8c.server.HttpURLConnectionDemo;

import u8c.vo.arrival.EncryptHelper;
import u8c.vo.cusmandoc.Cusmandoc;
import u8c.vo.cusmandoc.Billvo;
import u8c.vo.cusmandoc.Parentvo;
import u8c.vo.cusmandoc.Childrenvo;

import u8c.vo.cusmandoc.BanksVO;
import u8c.vo.cusmandoc.BaseBankVO;
import u8c.vo.cusmandoc.CustBankVO;
import u8c.vo.cusmandoc.CustMessage;
import u8c.vo.cusmandoc.CustData;
import u8c.vo.cusmandoc.CusRequest;
import u8c.vo.cusmandoc.CusBankRequest;
import u8c.vo.cusmandoc.CusResponse;
import u8c.vo.cusmandoc.DataResponse;
import u8c.vo.cusmandoc.BankDocVO;
import u8c.vo.cusmandoc.BankDocRootVO;
public class custadd implements IAPICustmerDevelop{
	private BaseDAO dao; 
	private BaseDAO getDao() {
		if (dao == null) {
			dao = new BaseDAO();
		}
		return dao;
	}
	@Override
	public String doAction(HttpServletRequest request)  throws BusinessException, ConfigException{
		String obj = "";
		String strBody = "";
		String strTemp = "";
		CustData dataResult=new CustData();
		try {
			// ��ʼ������	
			obj = this.getRequestPostStr(request.getInputStream());
			// д�������м��ļ�
			writeMiddleFile(APIConst.INDOCPATH + "u8c.busiitf.custadd", obj);
			// �ڶ�������������֮����������Ŀ���Լ���ҵ����
			JSONObject parameJson = JSONObject.parseObject(obj);
			
			CustMessage custMessage= JSON.toJavaObject(parameJson, CustMessage.class);
			CustData custData=custMessage.getMessage();
			obj=custData.getData();
			
			//����������
			EncryptHelper encryptHelper=new EncryptHelper();
			strBody=encryptHelper.decrypt(obj);
			strTemp+=obj+"\n \n "+strBody; 
			// д�������м��ļ�
			writeMiddleFile(APIConst.INDOCPATH + "u8c.busiitf.custadd", strTemp);
			
			List<CusRequest> listCusRequest=JSON.parseArray(strBody,CusRequest.class);
			List<CusResponse> listCusResponse=new ArrayList();//���ؽ��
			for(CusRequest cusRequest:listCusRequest){
				CusResponse cusResponse=setPostResultCust(cusRequest);
				listCusResponse.add(cusResponse);
			}
			
			// �����������ؽ��
			obj=JSON.toJSONString(listCusResponse);
			strTemp+="\n \n "+obj;
			strBody=encryptHelper.encrypt(obj);
			dataResult.setData(strBody);
			//encryptHelper.decrypt(strBody);
			strBody=JSON.toJSONString(dataResult);
			strTemp+="\n \n "+strBody;
			// д�뷵���м��ļ�
			writeMiddleFile(APIConst.RETURNDATAPATH + "u8c.busiitf.custadd",strTemp);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return strBody;
	}
	
	private CusResponse setPostResultCust(CusRequest cusRequest)
	{
		CusResponse cusResponse=new CusResponse();
		cusResponse.setCustCode(cusRequest.getCustCode());
		cusResponse.setComCode(cusRequest.getComCode());
		String strBody="";
		boolean isAdd=true;
		try{
			// ��һ������װ����
			Cusmandoc cusmandoc=new Cusmandoc();
			List<Billvo> listBillvo=new ArrayList();
			
			Billvo billvo=new Billvo();
			Parentvo parentvo=new Parentvo();
			List<Childrenvo> listChildrenvo=new ArrayList();
			//�жϿ��������Ƿ��ظ�
			String sql1="select b.custcode from bd_cumandoc a inner join bd_cubasdoc b on a.pk_cubasdoc=b.pk_cubasdoc inner join bd_corp c on a.pk_corp=c.pk_corp " +
					"		where c.unitcode='"+cusRequest.getComCode()+"' and b.custname='"+cusRequest.getCustName()+"'";
			String custCode=(String)getDao().executeQuery(sql1, new ColumnProcessor());
			if ((custCode!=null)&&(custCode.length()!=0)){
				cusResponse.setStatus("success");
				cusResponse.setU8cCode(custCode);
				cusResponse.setComCode(cusRequest.getComCode());
				
				isAdd=false;
				
				//return cusResponse;
			}
			//else
			//{
				//parentvo
				parentvo.setPk_corp(cusRequest.getComCode());
				parentvo.setCustcode(cusRequest.getCustCode());
				parentvo.setCustname(cusRequest.getCustName());
				parentvo.setCustshortname(cusRequest.getCustName());
				parentvo.setPk_areacl("10");
				parentvo.setConaddr(cusRequest.getConAddr());
				parentvo.setPhone1(cusRequest.getPhone1());
				parentvo.setPhone2(cusRequest.getPhone2());
				parentvo.setPhone3(cusRequest.getPhone3());
			
				//childrenvo
				
				Childrenvo childrenvo2=new Childrenvo();
				childrenvo2.setCustflag("2");
				listChildrenvo.add(childrenvo2);
				Childrenvo childrenvo3=new Childrenvo();
				childrenvo3.setCustflag("3");
				listChildrenvo.add(childrenvo3);
			//}
			//custbankvo
			List<CustBankVO> listCustBankVO=new ArrayList();
			
			for(CusBankRequest cusBankRequest:cusRequest.getBank()){
				if ((cusBankRequest.getBkCode()!=null)
						&&(cusBankRequest.getBkCode().length()!=0)
						&&(cusBankRequest.getBkName()!=null)
						&&(cusBankRequest.getBkName().length()!=0)){
					String sql2="select pk_banktype from bd_pcombineinfo " +
							"where pcombinenum='"+cusBankRequest.getBkCode()+"' " +
							"and pcombinename='"+cusBankRequest.getBkName()+"'";
					String pk_banktype=(String)getDao().executeQuery(sql2, new ColumnProcessor());
					if ((pk_banktype==null)||(pk_banktype.length()==0)){
						cusResponse.setStatus("fail");
						cusResponse.setU8cCode("");
						cusResponse.setComCode(cusRequest.getComCode());
						cusResponse.setDesc(cusBankRequest.getBkCode()+"-"+cusBankRequest.getBkName()+" ������������Ϣ�ﲻ����");
						return cusResponse;
					}else
					{
						listCustBankVO.add(setCustBankVO(cusBankRequest,cusRequest.getCustName()));
					}
				}
			}
			
					
			
			
			billvo.setParentvo(parentvo);
			billvo.setChildrenvo(listChildrenvo);
			if (!listCustBankVO.isEmpty()){
				billvo.setCustBanks(listCustBankVO);
			}
			listBillvo.add(billvo);
			cusmandoc.setBillvo(listBillvo);
			
			// �ڶ������ύ��API				
			// ���������ʵ�ַ���˿�,���� http://ip:port
			
			String serviceUrl = u8c.server.XmlConfig.getUrl("u8cuapbdcustmandocinsert");
			if (!isAdd){
				serviceUrl = u8c.server.XmlConfig.getUrl("u8cuapbdcustmandocmodifyacc");
			}
			//"http://127.0.0.1:9099/u8cloud/api/uapbd/custmandoc/insert";
			// ʹ��U8cloudϵͳ�����ã�����ڵ�·��Ϊ��
			// Ӧ�ü��� - ϵͳ����ƽ̨ - ϵͳ��Ϣ����
			// ������Ϣ�о������ԵĶ��չ�ϵ���£�
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("trantype", "code"); // �������뷽ʽ��ö��ֵΪ��������¼�� code�� ������¼�� name�� ������¼�� pk
			map.put("system", "busiitf"); // ϵͳ����
			map.put("usercode", "busiuser"); // �û�
			map.put("password", "bbbed85aa52a7dc74fc4b4bca8423394"); // ����1qazWSX����Ҫ MD5 ���ܺ�¼��				
			map.put("uniquekey",cusRequest.getComCode()+"-"+cusRequest.getCustCode());
			
			strBody=HttpURLConnectionDemo.operator(serviceUrl, map,JSON.toJSONString(cusmandoc));
			
			// ��������������	
			JSONObject jsonResult =JSON.parseObject(strBody);
			DataResponse dataResponse=JSON.toJavaObject(jsonResult, DataResponse.class);
			if (dataResponse.getStatus().equals("success")){// �����ķ���
				cusResponse.setStatus(dataResponse.getStatus());		
				List<Billvo> billvoResult=JSON.parseArray(dataResponse.getData(),Billvo.class);
				cusResponse.setU8cCode(billvoResult.get(0).getParentvo().getCustcode());
				cusResponse.setComCode(billvoResult.get(0).getParentvo().getCorp_code());
			}else{// �쳣�ķ���
				//postResult.setStatus(dataResponse.getStatus());
				cusResponse.setStatus("fail");
				cusResponse.setDesc(dataResponse.getErrorcode()+"-"+dataResponse.getErrormsg());
			}
		}catch(Exception e){
			cusResponse.setStatus("fail");			
			cusResponse.setDesc(e.getMessage());
			e.printStackTrace();
		}
		return cusResponse;
	}
	
	// ���������˺�
	private CustBankVO setCustBankVO(CusBankRequest cusBankRequest,String custName) throws DAOException{
		
			CustBankVO custBankVO=new CustBankVO();
			BaseBankVO baseBankVO=new BaseBankVO();
			BanksVO banksVO=new BanksVO();
			String pk_banktype="";
			String bankTypeCode="";
			String bankCode=cusBankRequest.getBkCode();
			String bankName=cusBankRequest.getBkName();
			String sql2="select pk_banktype from bd_pcombineinfo " +
					"where pcombinenum='"+bankCode+"' " +
					"and pcombinename='"+bankName+"'";
			pk_banktype=(String)getDao().executeQuery(sql2, new ColumnProcessor());
			if ((pk_banktype==null)||(pk_banktype.length()==0)){
				/*cusResponse.setStatus("fail");
				cusResponse.setU8cCode("");
				cusResponse.setComCode(cusRequest.getComCode());
				cusResponse.setDesc(bankCode+"-"+bankName+" ������������Ϣ�ﲻ����");
				return cusResponse;*/
			}else{
				String sql3="select banktypecode from bd_banktype where pk_banktype='"+pk_banktype+"'";
				pk_banktype=(String)getDao().executeQuery(sql3, new ColumnProcessor());
				//�������е���
				setBankDoc(bankCode,bankName,pk_banktype);
				
				baseBankVO.setAccount(cusBankRequest.getAcCode());
				baseBankVO.setAccountcode(cusBankRequest.getAcCode());
				baseBankVO.setAccountname(custName);
				baseBankVO.setPk_bankdoc(bankCode);
				baseBankVO.setPk_banktype(pk_banktype);
				baseBankVO.setPk_currtype("CNY");
				baseBankVO.setCombineaccnum(bankCode);
				
				banksVO.setPk_accbank(cusBankRequest.getAcCode());
				banksVO.setPk_bankdoc(bankCode);
				banksVO.setPk_banktype(pk_banktype);
				banksVO.setAccount(cusBankRequest.getAcCode());
				banksVO.setAccountname(custName);
	            banksVO.setDefflag("Y");
				
				custBankVO.setBanks(banksVO);
				custBankVO.setBaseBank(baseBankVO);
				
			}
		
		return custBankVO;
	}
	
	//�������е���
	private void setBankDoc(String bankCode,String bankName,String pk_banktype){
		// ��һ������װ����
		BankDocRootVO bankDocRootVO=new BankDocRootVO();
		List<BankDocVO> listBankdoc=new ArrayList();
		BankDocVO bankDoc=new BankDocVO();
		bankDoc.setPk_corp("0001");
		bankDoc.setUnitcode("0001");
		bankDoc.setBankdoccode(bankCode);
		bankDoc.setBankdocname(bankName);
		bankDoc.setPk_banktype(pk_banktype);
		bankDoc.setSealflag(false);
		bankDoc.setShortname(bankName);
		bankDoc.setPcombinenum(bankCode);
		bankDoc.setPcombinename(bankName);
		bankDoc.setIscustbank(false);
		listBankdoc.add(bankDoc);
		bankDocRootVO.setBankdoc(listBankdoc);
		// �ڶ������ύ��API				
		// ���������ʵ�ַ���˿�,���� http://ip:port
		String serviceUrl = u8c.server.XmlConfig.getUrl("u8cuapbdbdbankdocsave");
		//"http://127.0.0.1:9099/u8cloud/api/uapbd/bdbankdoc/save";
		// ʹ��U8cloudϵͳ�����ã�����ڵ�·��Ϊ��
		// Ӧ�ü��� - ϵͳ����ƽ̨ - ϵͳ��Ϣ����
		// ������Ϣ�о������ԵĶ��չ�ϵ���£�
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("trantype", "code"); // �������뷽ʽ��ö��ֵΪ��������¼�� code�� ������¼�� name�� ������¼�� pk
		map.put("system", "busiitf"); // ϵͳ����
		map.put("usercode", "busiuser"); // �û�
		map.put("password", "bbbed85aa52a7dc74fc4b4bca8423394"); // ����1qazWSX����Ҫ MD5 ���ܺ�¼��
		String strBody=HttpURLConnectionDemo.operator(serviceUrl, map,JSON.toJSONString(bankDocRootVO));
		
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