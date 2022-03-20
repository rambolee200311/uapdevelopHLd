package u8c.busiitf.task;
import java.util.List;
import nc.bs.dao.BaseDAO;
import java.util.ArrayList;
import u8c.bs.exception.SecurityException;
import u8c.vo.reject.invoiceApply.Invoice;
import u8c.vo.reject.invoiceApply.InvoiceData;
import u8c.vo.reject.invoiceApply.InvoiceDataRoot;
import u8c.vo.reject.invoiceApply.InvoiceHead;
import u8c.vo.reject.invoiceApply.InvoiceMessage;
import u8c.vo.arrival.EncryptHelper;
import nc.bs.pub.taskcenter.BgWorkingContext;
import nc.vo.pub.BusinessException;
import com.alibaba.fastjson.JSON;
import u8c.server.HttpURLConnectionDemo;

public class RejectInvoiceAPITask implements nc.bs.pub.taskcenter.IBackgroundWorkPlugin{
	private BaseDAO dao; 
	private BaseDAO getDao() {
		if (dao == null) {
			dao = new BaseDAO();
		}
		return dao;
	}
	@Override
	public String executeTask(BgWorkingContext param) throws BusinessException {
		String strResult="";
		//��ʼ������		
		Invoice invoice=new Invoice();
		InvoiceHead invoiceHead=new InvoiceHead();
		invoiceHead.setUserName("username");
		invoiceHead.setPassWord("password");
		invoiceHead.setRequestDate("2021-12-08");
		invoiceHead.setSeqNO("100120211208");
		invoice.setHeader(invoiceHead);
		InvoiceMessage invoiceMessage=new InvoiceMessage();
		
		InvoiceDataRoot invoiceDataRoot=new InvoiceDataRoot();
		List<InvoiceData> listInvoiceData=new ArrayList();		
		
		//����data
		InvoiceData invoiceData=new InvoiceData();
		
		invoiceData.setAdviceNote("0987654321");//֪ͨ���� ��Ʊ֪ͨ���ţ�Ψһ��
		invoiceData.setAdviceDate("2022-01-14");
		invoiceData.setBusiType("1");
		invoiceData.setTotleAmount(10009.64);
		invoiceData.setValue1("abc");
		invoiceData.setCurrency("CNY");//���� �����		
		invoiceData.setOperatorCode("13501036623");	
		listInvoiceData.add(invoiceData);
		
		invoiceDataRoot.setTotalNum(1);
		invoiceDataRoot.setInvoiceBusinType("1");
		invoiceDataRoot.setInvoiceData(listInvoiceData);
		
		//����
		EncryptHelper encryptHelper=new EncryptHelper();
		String encryptString="";
		try {
			encryptString = encryptHelper.encrypt(JSON.toJSONString(invoiceDataRoot));
			
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		invoiceMessage.setData(encryptString);
		invoice.setMessage(invoiceMessage);
		
		//http post
		strResult=HttpURLConnectionDemo.httpPostWithJson(
				//"http://10.0.0.107:38030/api/reject/InvoiceApply",
				u8c.server.XmlConfig.getUrl("rejectInvoiceApply"),
				JSON.toJSONString(invoice));
		
		return strResult;
	}
}
