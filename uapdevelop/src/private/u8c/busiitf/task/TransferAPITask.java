package u8c.busiitf.task;
import java.util.List;
import nc.bs.dao.BaseDAO;
import java.util.ArrayList;
import u8c.bs.exception.SecurityException;
import u8c.vo.transfer.Transfer;
import u8c.vo.transfer.TransferData;
import u8c.vo.transfer.TransferDataRoot;
import u8c.vo.transfer.TransferHeader;
import u8c.vo.transfer.TransferMessage;
import u8c.vo.arrival.EncryptHelper;
import nc.bs.pub.taskcenter.BgWorkingContext;
import nc.vo.pub.BusinessException;
import com.alibaba.fastjson.JSON;
import u8c.server.HttpURLConnectionDemo;
public class TransferAPITask implements nc.bs.pub.taskcenter.IBackgroundWorkPlugin{
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
		transferData.setPaymentReviewNo("0987654321AWDS");//U8C����������ˮ��
		transferData.setInsuranceCode("01000050");//U8Cϵͳ���̱��
		transferData.setInsuranceName("��̩�Ʋ����չɷ����޹�˾");//U8Cϵͳ���̱������
		transferData.setCurrency("CNY");//���� �����
		transferData.setTransferAmount(950.030);//ת�����
		transferData.setAcceptDateTime("2021-12-09");//��������
		transferData.setTransferComCode("A01");//�����˹������� ��˾����
		//transferData.setTransferComName("�������վ������޹�˾");
		transferData.setTransferPersonCode("13501036623");
		transferData.setRemark("ժҪ��;˵����ע");//���񸶿��ע
		transferData.setTransferState("0");//0-�ɹ���1-ʧ�ܣ�N-ԭ����
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
				"http://10.0.0.107:38030/api/agent/TransferApi",
				JSON.toJSONString(transfer));
		
		return strResult;
	}

}
