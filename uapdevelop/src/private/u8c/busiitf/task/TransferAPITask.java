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
		
		//初始化报文		
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
		
		//数据data
		TransferData transferData=new TransferData();		
		transferData.setTransferApplyNo("1234567890");//由业务系统生成与业务系统一致
		transferData.setPaymentReviewNo("0987654321AWDS");//U8C划拨记账流水号
		transferData.setInsuranceCode("01000050");//U8C系统客商编号
		transferData.setInsuranceName("锦泰财产保险股份有限公司");//U8C系统客商编号名称
		transferData.setCurrency("CNY");//币种 人民币
		transferData.setTransferAmount(950.030);//转付金额
		transferData.setAcceptDateTime("2021-12-09");//付款日期
		transferData.setTransferComCode("A01");//经办人归属机构 公司编码
		//transferData.setTransferComName("航联保险经纪有限公司");
		transferData.setTransferPersonCode("13501036623");
		transferData.setRemark("摘要用途说明备注");//财务付款单备注
		transferData.setTransferState("0");//0-成功，1-失败，N-原因不明
		listTransferData.add(transferData);
		
		transferDataRoot.setTotalNum(1);
		transferDataRoot.setTransferBusinType("1");
		transferDataRoot.setTransferData(listTransferData);
		
		//加密
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
