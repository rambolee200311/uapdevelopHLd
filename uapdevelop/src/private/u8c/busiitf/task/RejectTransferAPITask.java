package u8c.busiitf.task;
import java.util.List;
import nc.bs.dao.BaseDAO;
import java.util.ArrayList;
import u8c.bs.exception.SecurityException;
import u8c.vo.reject.paymentApply.Transfer;
import u8c.vo.reject.paymentApply.TransferData;
import u8c.vo.reject.paymentApply.TransferDataRoot;
import u8c.vo.reject.paymentApply.TransferHeader;
import u8c.vo.reject.paymentApply.TransferMessage;
import u8c.vo.arrival.EncryptHelper;
import nc.bs.pub.taskcenter.BgWorkingContext;
import nc.vo.pub.BusinessException;
import com.alibaba.fastjson.JSON;
import u8c.server.HttpURLConnectionDemo;
public class RejectTransferAPITask implements nc.bs.pub.taskcenter.IBackgroundWorkPlugin{
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
		Transfer transfer=new Transfer();
		TransferHeader transferHead=new TransferHeader();
		transferHead.setUserName("username");
		transferHead.setPassWord("password");
		transferHead.setRequestDate("2021-12-08");
		transferHead.setSeqNO("100120211208");
		transfer.setHeader(transferHead);
		TransferMessage transferMessage=new TransferMessage();
		
		TransferDataRoot transferDataRoot=new TransferDataRoot();
		List<TransferData> listTransferData=new ArrayList();		
		
		//����data
		TransferData transferData=new TransferData();		
		transferData.setTransferApplyNo("1234567890");//��ҵ��ϵͳ������ҵ��ϵͳһ��
		transferData.setTransferApplyDate("2022-01-12");
		transferData.setTransferBusinType("1");
		transferData.setOperatorCode("13501036623");
		transferData.setCurrency("CNY");//���� �����
		transferData.setTransferAmount(950.030);//ת�����		
		listTransferData.add(transferData);		
		transferDataRoot.setTotalNum(1);
		transferDataRoot.setTransferBusinType("1");
		transferDataRoot.setTransferData(listTransferData);		
		//����
		EncryptHelper encryptHelper=new EncryptHelper();
		String encryptString="";
		try {
			encryptString = encryptHelper.encrypt(JSON.toJSONString(transferDataRoot));
			
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		transferMessage.setData(encryptString);
		transfer.setMessage(transferMessage);
		
		//http post
		strResult=HttpURLConnectionDemo.httpPostWithJson(
				"http://10.0.0.107:38030/api/reject/PaymentApply",
				JSON.toJSONString(transfer));
		
		return strResult;
	}

}
