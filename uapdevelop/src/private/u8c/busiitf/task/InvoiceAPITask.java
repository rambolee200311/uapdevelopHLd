package u8c.busiitf.task;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import nc.bs.dao.BaseDAO;
import java.math.BigDecimal;
import java.util.ArrayList;
import u8c.bs.exception.ConfigException;
import u8c.bs.exception.SecurityException;
import u8c.vo.entity.CorpVO;
import u8c.vo.entity.CustVO;
import u8c.vo.invoice.Invoice;
import u8c.vo.invoice.InvoiceData;
import u8c.vo.invoice.InvoiceDataRoot;
import u8c.vo.invoice.InvoiceHead;
import u8c.vo.invoice.InvoiceMessage;
import u8c.vo.respmsg.RespMsg;
import u8c.vo.arrival.EncryptHelper;
import nc.bs.logging.Log;
import nc.bs.logging.Logger;
import nc.bs.pub.taskcenter.BgWorkingContext;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.jdbc.framework.processor.BeanProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.vo.ep.dj.DJZBHeaderVO;
import nc.vo.ep.dj.DJZBItemVO;
import nc.vo.pub.BusinessException;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import u8c.server.HttpURLConnectionDemo;

public class InvoiceAPITask implements nc.bs.pub.taskcenter.IBackgroundWorkPlugin{
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
		Logger.init("hanglianAPI");
		//logger.init("hanglianTest"); 
		
		// 拿到参数
		LinkedHashMap<String, Object> para = param.getKeyMap();
		String strDjbh=(String) para.get("djbh");
		String strbilltype = (String) para.get("billtype");
		String sql1="select [bbje], [budgetcheck], [ddhbbm], [deinvdate], [djbh], [djdl], [djkjnd], [djkjqj], [djlxbm], [djrq], [djzt], [dr], [dwbm], [dyvouchid], [dzrq], [effectdate], [enduser], [fbje], [fcounteractflag], [feinvstatus], [finvoicetype], [fj], [fktjbm], [hzbz], [inner_effect_date], [isjszxzf], [isnetready], [isonlinepay], [ispaid], [isreded], [isselectedpay], [jszxzf], [kmbm], [kskhyh], [lastshr], [lasttzr], [lrr], [lybz], [officialprintdate], [officialprintuser], [outbusitype], [paydate], [payman], [pj_jsfs], [pj_num], [pj_oid], [prepay], [pzglh], [qcbz], [qrr], [scomment], [settlenum], [sfkr], [shkjnd], [shkjqj], [shr], [shrq], [shzd], [specflag], [spzt], [ssbh], [sscause], [sxbz], [sxkjnd], [sxkjqj], [sxr], [sxrq], [ts], [veinvcode], [veinvfailnote], [veinvnumber], [vouchid], [vsplitrecord], [vsrceinvcode], [vsrceinvnumber], [xslxbm], [ybje], [yhqrkjnd], [yhqrkjqj], [yhqrr], [yhqrrq], [ywbm], [zdr], [zdrq], [zgyf], [zyx1], [zyx10], [zyx11], [zyx12], [zyx13], [zyx14], [zyx15], [zyx16], [zyx17], [zyx18], [zyx19], [zyx2], [zyx20], [zyx21], [zyx22], [zyx23], [zyx24], [zyx25], [zyx26], [zyx27], [zyx28], [zyx29], [zyx3], [zyx30], [zyx4], [zyx5], [zyx6], [zyx7], [zyx8], [zyx9], [zzzt], [inccontype] " +
					" from arap_djzb " +
					" where djdl='ys' and djlxbm='"+strbilltype+"' and dr=0 and isnull(zyx1,'')!=''";
		if (!(strDjbh==null)&&(!strDjbh.equals(null))&&(!strDjbh.isEmpty())){
			sql1+=" and djbh='"+strDjbh+"'";
		}		
		
		//主表vo
		ArrayList<DJZBHeaderVO> vos =(ArrayList<DJZBHeaderVO>) getDao().executeQuery(sql1, new BeanListProcessor(DJZBHeaderVO.class));		
		//初始化报文		
		Invoice invoice=new Invoice();
		InvoiceHead invoiceHead=new InvoiceHead();
		invoiceHead.setUserName("username");
		invoiceHead.setPassWord("password");
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyyMMddHHmmss");
		String strDate=dateFormat.format(new Date());
		String strDHMS=dateFormat1.format(new Date());
		invoiceHead.setRequestDate(strDate);
		invoiceHead.setSeqNO(strDHMS);
		invoice.setHeader(invoiceHead);
		InvoiceMessage invoiceMessage=new InvoiceMessage();
		
		InvoiceDataRoot invoiceDataRoot=new InvoiceDataRoot();
		List<InvoiceData> listInvoiceData=new ArrayList();
		int iRow=0;
		for(DJZBHeaderVO vo : vos){
			String sql2="select [accountid], [assetpactno], [bankrollprojet], [bbhl], [bbpjlx], [bbtxfy], [bbye], [bfyhzh], [billdate], [bjdwhsdj], [bjdwsl], [bjdwwsdj], [bjjldw], [blargessflag], [bz_date], [bz_kjnd], [bz_kjqj], [bzbm], [cashitem], [chbm_cl], [checkflag], [chmc], [cinventoryid], [ckbm], [ckdh], [ckdid], [cksqsh], [clbh], [commonflag], [contractno], [ctzhtlx_pk], [ddh], [ddhh], [ddhid], [ddlx], [deptid], [dfbbje], [dfbbsj], [dfbbwsje], [dffbje], [dffbsj], [dfjs], [dfshl], [dfybje], [dfybsj], [dfybwsje], [dfyhzh], [discountmny], [dj], [djbh], [djdl], [djlxbm], [djxtflag], [dr], [dstlsubcs], [dwbm], [encode], [equipmentcode], [facardbh], [fb_oid], [fbhl], [fbpjlx], [fbtxfy], [fbye], [fjldw], [fkyhdz], [fkyhmc], [flbh], [fph], [fphid], [freeitemid], [fx], [ggxh], [groupnum], [hbbm], [hsdj], [hsl], [htbh], [htmc], [innerorderno], [issfkxychanged], [isverifyfinished], [item_bill_pk], [itemstyle], [jfbbje], [jfbbsj], [jffbje], [jffbsj], [jfjs], [jfshl], [jfybje], [jfybsj], [jfybwsje], [jfzkfbje], [jfzkybje], [jobid], [jobphaseid], [jsfsbm], [jshj], [kmbm], [kprq], [ksbm_cl], [kslb], [kxyt], [notetype], [occupationmny], [old_flag], [old_sys_flag], [ordercusmandoc], [othersysflag], [pausetransact], [paydate], [payflag], [payman], [pch], [ph], [pj_jsfs], [pjdirection], [pjh], [pk_jobobjpha], [pk_taxclass], [produceorder], [productline], [pzflh], [qxrq], [sanhu], [seqnum], [sfbz], [sfkxyh], [shlye], [skyhdz], [skyhmc], [sl], [spzt], [srbz], [szxmid], [task], [tax_num], [tbbh], [ts], [txlx_bbje], [txlx_fbje], [txlx_ybje], [usedept], [verifyfinisheddate], [vouchid], [wbfbbje], [wbffbje], [wbfybje], [wldx], [xbbm3], [xgbh], [xm], [xmbm2], [xmbm4], [xmys], [xyzh], [ybpjlx], [ybtxfy], [ybye], [ycskrq], [ysbbye], [ysfbye], [ysybye], [ywbm], [ywxz], [ywybm], [zjldw], [zkl], [zrdeptid], [zy], [zyx1], [zyx10], [zyx11], [zyx12], [zyx13], [zyx14], [zyx15], [zyx16], [zyx17], [zyx18], [zyx19], [zyx2], [zyx20], [zyx21], [zyx22], [zyx23], [zyx24], [zyx25], [zyx26], [zyx27], [zyx28], [zyx29], [zyx3], [zyx30], [zyx4], [zyx5], [zyx6], [zyx7], [zyx8], [zyx9] " +
					"from arap_djfb " +
					"where vouchid='"+vo.getVouchid()+"';";
			//子表 vob
			ArrayList<DJZBItemVO> vobs =(ArrayList<DJZBItemVO>) getDao().executeQuery(sql2, new BeanListProcessor(DJZBItemVO.class));
			CustVO custVO=null;
			CorpVO corpVO=null;
			String userCode="";
			Double inclusiveMoney=(double) 0;
			Double inclusive=(double) 0;
			Double noInclusiveMoney=(double) 0;
			Double specialMoney=(double) 0;
			
				for(nc.vo.ep.dj.DJZBItemVO vob : vobs){
					//客户
					String sql3="select custcode,custname,conaddr,phone1,taxpayerid from bd_cubasdoc where pk_cubasdoc='"+vob.getHbbm()+"'";
					custVO=(CustVO)getDao().executeQuery(sql3, new BeanProcessor(CustVO.class));		
					//公司
					sql3="select unitcode,unitname from bd_corp where pk_corp='"+vo.getDwbm()+"'";
					corpVO=(CorpVO)getDao().executeQuery(sql3, new BeanProcessor(CorpVO.class));
					//操作员
					sql3="select user_code from sm_user where cuserid='"+vo.getLrr()+"'";
					userCode=(String)getDao().executeQuery(sql3, new ColumnProcessor());
					inclusive=vob.getSl().toDouble();
					inclusiveMoney+=vob.getJfbbje().toDouble();
					specialMoney+=vob.getJfbbsj().toDouble();
					//noInclusiveMoney+=(vob.getJfybwsje().toDouble()*vob.getBbhl().toDouble());
					
					
					
				}
				
				//数据data
				String operationType="1";
				InvoiceData invoiceData=new InvoiceData();
				int isVatInvoice=0;
				switch(vo.getZyx4()){
					case "蓝票":
						operationType="1";
						isVatInvoice=0;
						break;
					case "红票":
						operationType="2";
						invoiceData.setReverseInvoiceNo(vo.getZyx8());
						isVatInvoice=1;
						break;
					case "作废":
						operationType="3";
						invoiceData.setReverseInvoiceNo(vo.getZyx8());
						isVatInvoice=0;
						break;
					default:
						operationType="1";
				}
				invoiceData.setInvoiceSeqNo(vo.getDjbh());//发票流水号 U8C发票号
				invoiceData.setInvoiceNo(vo.getZyx2());//发票号  金税发票号
				invoiceData.setAdviceNote(vo.getZyx1());//通知书编号 开票通知书编号（唯一）
				invoiceData.setAdviceType(Integer.valueOf(vo.getZyx7()));//通知书类型 业务类型 (1-保司经纪费,2-客户经纪费,3-咨询经纪费,4-转付保费到账,5-再保结算到站,6-理赔款结算,7-再保理赔,8-退保经纪费）
				invoiceData.setInvoiceType("增值税");//发票类型 （普通发票、增值税）
				invoiceData.setCurrency("CNY");//币种 人民币
				invoiceData.setInclusiveMoney(inclusiveMoney);//发票含税金额 金额	
				invoiceData.setInclusive(inclusive);//含税税率 整数（6，13）
				invoiceData.setSpecialMoney(specialMoney);//增值税金额 税额
				invoiceData.setNoInclusiveMoney(inclusive-specialMoney);//不含税金额 金额
				invoiceData.setPayerCode(custVO.getCustcode());//支付方编号 u8c客商编号
				invoiceData.setPayerName(custVO.getCustname());//支付方名称 u8c客商名称
				invoiceData.setInvoiceTime(vo.getZyx3());//发票日期
				invoiceData.setIsVatInvoice(isVatInvoice);//是否红票 是否红票
				invoiceData.setRemark(vo.getScomment());
				invoiceData.setVatPayerId(custVO.getTaxpayerid());//纳税人识别号 u8c税号
				invoiceData.setComCode(corpVO.getUnitcode());//经办人归属机构 公司编码
				invoiceData.setComName(corpVO.getUnitname());
				invoiceData.setOperatorCode(userCode);	
				invoiceData.setOperationType(operationType);
				
				
				listInvoiceData.add(invoiceData);
				iRow++;
		}
		invoiceDataRoot.setTotalNum(iRow);
		//invoiceDataRoot.setInvoiceBusinType("1");
		invoiceDataRoot.setInvoiceData(listInvoiceData);
		
		//加密
		EncryptHelper encryptHelper=new EncryptHelper();
		String encryptString="";
		try {
			strResult=JSON.toJSONString(invoiceDataRoot);
			Logger.debug(strResult);
			Log.getInstance("hanglianAPI").debug(strResult);
			encryptString = encryptHelper.encrypt(strResult);
			
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			
			Logger.error(e.getMessage(),e);
			Log.getInstance("hanglianAPI").error(e.getMessage(),e);
			e.printStackTrace();
		}
		invoiceMessage.setData(encryptString);
		invoice.setMessage(invoiceMessage);
		
		//http post
		strResult=HttpURLConnectionDemo.httpPostWithJson(
				//"http://10.0.0.107:38030/api/agent/InvoiceApi",
				u8c.server.XmlConfig.getUrl("InvoiceApi"),
				JSON.toJSONString(invoice));
		JSONObject parameJson = JSONObject.parseObject(strResult);
		RespMsg respMsg=JSON.toJavaObject(parameJson, RespMsg.class);
		
		try {
			strResult=encryptHelper.decrypt(respMsg.getMessage().getData());
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			Logger.error(e.getMessage(),e);
			Log.getInstance("hanglianAPI").error(e.getMessage(),e);
			e.printStackTrace();
		}
		Logger.debug(strResult);
		Log.getInstance("hanglianAPI").debug(strResult);
		return strResult;
	}
}
