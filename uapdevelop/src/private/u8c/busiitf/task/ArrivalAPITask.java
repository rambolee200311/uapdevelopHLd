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
import u8c.vo.entity.CustVO;
import u8c.vo.entity.CorpVO;
import u8c.vo.respmsg.*;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.jdbc.framework.processor.BeanProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.ResultSetProcessor;
import nc.bs.pub.taskcenter.BgWorkingContext;
import nc.vo.pub.BusinessException;
import nc.vo.scm.pattern.exception.ExceptionUtils;
import nc.vo.pub.lang.UFDouble;
import nc.vo.bd.b07.AreaclVO;
import nc.vo.ep.dj.DJZBHeaderVO;
import nc.vo.ep.dj.DJZBItemVO;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import u8c.vo.arrival.*;
import u8c.server.HttpURLConnectionDemo;
public class ArrivalAPITask  implements nc.bs.pub.taskcenter.IBackgroundWorkPlugin{	
 
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
		String strTemp="";
		// 拿到参数
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyyMMddHHmmss");
		String strDate=dateFormat.format(new Date());
		String strDHMS=dateFormat1.format(new Date());
		
		LinkedHashMap<String, Object> para = param.getKeyMap();
		String strbilltype = (String) para.get("billtype");
		String strDdate = (String) para.get("ddate");
		if ((strDdate==null)||(strDdate.equals(null))||(strDdate.isEmpty())){
			strDdate=strDate;
		}
		
		String sql1="select [bbje], [budgetcheck], [ddhbbm], [deinvdate], [djbh], [djdl], [djkjnd], [djkjqj], [djlxbm], [djrq], [djzt], [dr], [dwbm], [dyvouchid], [dzrq], [effectdate], [enduser], [fbje], [fcounteractflag], [feinvstatus], [finvoicetype], [fj], [fktjbm], [hzbz], [inner_effect_date], [isjszxzf], [isnetready], [isonlinepay], [ispaid], [isreded], [isselectedpay], [jszxzf], [kmbm], [kskhyh], [lastshr], [lasttzr], [lrr], [lybz], [officialprintdate], [officialprintuser], [outbusitype], [paydate], [payman], [pj_jsfs], [pj_num], [pj_oid], [prepay], [pzglh], [qcbz], [qrr], [scomment], [settlenum], [sfkr], [shkjnd], [shkjqj], [shr], [shrq], [shzd], [specflag], [spzt], [ssbh], [sscause], [sxbz], [sxkjnd], [sxkjqj], [sxr], [sxrq], [ts], [veinvcode], [veinvfailnote], [veinvnumber], [vouchid], [vsplitrecord], [vsrceinvcode], [vsrceinvnumber], [xslxbm], [ybje], [yhqrkjnd], [yhqrkjqj], [yhqrr], [yhqrrq], [ywbm], [zdr], [zdrq], [zgyf], [zyx1], [zyx10], [zyx11], [zyx12], [zyx13], [zyx14], [zyx15], [zyx16], [zyx17], [zyx18], [zyx19], [zyx2], [zyx20], [zyx21], [zyx22], [zyx23], [zyx24], [zyx25], [zyx26], [zyx27], [zyx28], [zyx29], [zyx3], [zyx30], [zyx4], [zyx5], [zyx6], [zyx7], [zyx8], [zyx9], [zzzt], [inccontype] " +
					" from arap_djzb " +
					" where djrq='"+strDdate+"' and djdl='sk' and djlxbm='"+strbilltype+"' and dr=0";
		//初始化报文
		Arrival arrival=new Arrival();
		ArrivalHeader arrivalHeader=new ArrivalHeader();
		arrivalHeader.setUserName("username");
		arrivalHeader.setPassWord("password");
		
		arrivalHeader.setRequestDate(strDate);
		arrivalHeader.setSeqNO(strDHMS);
		arrival.setHeader(arrivalHeader);
		ArrivalMessage arrivalMessage=new ArrivalMessage();
		
		ArrivalDataRoot arrivalDataRoot=new ArrivalDataRoot();
		List<ArrivalData> listArrivalData=new ArrayList();
		//主表vo
		ArrayList<DJZBHeaderVO> vos =(ArrayList<DJZBHeaderVO>) getDao().executeQuery(sql1, new BeanListProcessor(DJZBHeaderVO.class));
		
		//if(strbilltype.equals("F2-01")){
			int iRow=0;
			for(DJZBHeaderVO vo : vos){
				String sql2="select [accountid], [assetpactno], [bankrollprojet], [bbhl], [bbpjlx], [bbtxfy], [bbye], [bfyhzh], [billdate], [bjdwhsdj], [bjdwsl], [bjdwwsdj], [bjjldw], [blargessflag], [bz_date], [bz_kjnd], [bz_kjqj], [bzbm], [cashitem], [chbm_cl], [checkflag], [chmc], [cinventoryid], [ckbm], [ckdh], [ckdid], [cksqsh], [clbh], [commonflag], [contractno], [ctzhtlx_pk], [ddh], [ddhh], [ddhid], [ddlx], [deptid], [dfbbje], [dfbbsj], [dfbbwsje], [dffbje], [dffbsj], [dfjs], [dfshl], [dfybje], [dfybsj], [dfybwsje], [dfyhzh], [discountmny], [dj], [djbh], [djdl], [djlxbm], [djxtflag], [dr], [dstlsubcs], [dwbm], [encode], [equipmentcode], [facardbh], [fb_oid], [fbhl], [fbpjlx], [fbtxfy], [fbye], [fjldw], [fkyhdz], [fkyhmc], [flbh], [fph], [fphid], [freeitemid], [fx], [ggxh], [groupnum], [hbbm], [hsdj], [hsl], [htbh], [htmc], [innerorderno], [issfkxychanged], [isverifyfinished], [item_bill_pk], [itemstyle], [jfbbje], [jfbbsj], [jffbje], [jffbsj], [jfjs], [jfshl], [jfybje], [jfybsj], [jfybwsje], [jfzkfbje], [jfzkybje], [jobid], [jobphaseid], [jsfsbm], [jshj], [kmbm], [kprq], [ksbm_cl], [kslb], [kxyt], [notetype], [occupationmny], [old_flag], [old_sys_flag], [ordercusmandoc], [othersysflag], [pausetransact], [paydate], [payflag], [payman], [pch], [ph], [pj_jsfs], [pjdirection], [pjh], [pk_jobobjpha], [pk_taxclass], [produceorder], [productline], [pzflh], [qxrq], [sanhu], [seqnum], [sfbz], [sfkxyh], [shlye], [skyhdz], [skyhmc], [sl], [spzt], [srbz], [szxmid], [task], [tax_num], [tbbh], [ts], [txlx_bbje], [txlx_fbje], [txlx_ybje], [usedept], [verifyfinisheddate], [vouchid], [wbfbbje], [wbffbje], [wbfybje], [wldx], [xbbm3], [xgbh], [xm], [xmbm2], [xmbm4], [xmys], [xyzh], [ybpjlx], [ybtxfy], [ybye], [ycskrq], [ysbbye], [ysfbye], [ysybye], [ywbm], [ywxz], [ywybm], [zjldw], [zkl], [zrdeptid], [zy], [zyx1], [zyx10], [zyx11], [zyx12], [zyx13], [zyx14], [zyx15], [zyx16], [zyx17], [zyx18], [zyx19], [zyx2], [zyx20], [zyx21], [zyx22], [zyx23], [zyx24], [zyx25], [zyx26], [zyx27], [zyx28], [zyx29], [zyx3], [zyx30], [zyx4], [zyx5], [zyx6], [zyx7], [zyx8], [zyx9] " +
						"from arap_djfb " +
						"where vouchid='"+vo.getVouchid()+"';";
				//子表 vob
				ArrayList<DJZBItemVO> vobs =(ArrayList<DJZBItemVO>) getDao().executeQuery(sql2, new BeanListProcessor(DJZBItemVO.class));
				for(nc.vo.ep.dj.DJZBItemVO vob : vobs){
					//客户
					String sql3="select custcode,custname,conaddr,phone1,taxpayerid from bd_cubasdoc where pk_cubasdoc='"+vob.getHbbm()+"'";
					CustVO custVO=(CustVO)getDao().executeQuery(sql3, new BeanProcessor(CustVO.class));		
					//公司
					sql3="select unitcode,unitname from bd_corp where pk_corp='"+vo.getDwbm()+"'";
					CorpVO corpVO=(CorpVO)getDao().executeQuery(sql3, new BeanProcessor(CorpVO.class));
					//操作员
					sql3="select user_code from sm_user where cuserid='"+vo.getLrr()+"'";
					String userCode=(String)getDao().executeQuery(sql3, new ColumnProcessor());
					//币种
					sql3="select currtypecode from bd_currtype where pk_currtype='"+vob.getBzbm()+"'";
					String currency=(String)getDao().executeQuery(sql3, new ColumnProcessor());
					//数据data
					ArrivalData arrivalData=new ArrivalData();
					arrivalData.setArrivalRegiCode(vob.getDdhh());
					arrivalData.setPayerCode(custVO.getCustcode());
					arrivalData.setPayerName(custVO.getCustname());
					arrivalData.setArrivalPurpose(vob.getZy());
					arrivalData.setArrivalAmount(vob.getYbye().doubleValue());
					arrivalData.setComCode(corpVO.getUnitcode());
					arrivalData.setComName(corpVO.getUnitname());
					arrivalData.setOperatorCode(userCode);
					arrivalData.setCurrency(currency);
					arrivalData.setArrivalRegiCode(vo.getVouchid());
					listArrivalData.add(arrivalData);
				}			
				
				iRow++;
			}
			
			arrivalDataRoot.setTotalNum(iRow);
			switch(strbilltype){
				case "F2-01":
					arrivalDataRoot.setArrivalBusinType("1");
					break;
				case "F2-02":
					arrivalDataRoot.setArrivalBusinType("2");
					break;	
			}
			
			arrivalDataRoot.setArrivalData(listArrivalData);
			//加密
			EncryptHelper encryptHelper=new EncryptHelper();
			String encryptString=JSON.toJSONString(arrivalDataRoot);
			strTemp = encryptString;
			try {
				encryptString = encryptHelper.encrypt(encryptString);
				
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			arrivalMessage.setData(encryptString);
			//解密
			/*try {
				encryptString=encryptHelper.decrypt(encryptString);
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
			//httpPost
			
			arrival.setMessage(arrivalMessage);
			strResult=HttpURLConnectionDemo.httpPostWithJson(
					"http://10.0.0.107:38030/api/agent/ArrivalApi",
					JSON.toJSONString(arrival));
			JSONObject parameJson = JSONObject.parseObject(strResult);
			RespMsg respMsg=JSON.toJavaObject(parameJson, RespMsg.class);
			
			try {
				strResult=encryptHelper.decrypt(respMsg.getMessage().getData());
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				
				e.printStackTrace();
			}
			
		//}
		
		//strResult=JSON.toJSONString(arrival);
		//return strTemp+"  "+strResult;//"Success";
		return strResult;
	}
}
