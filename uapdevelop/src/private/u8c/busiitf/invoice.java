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
import nc.jdbc.framework.processor.BeanProcessor;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDateTime;
import u8c.bs.APIConst;
import u8c.bs.exception.ConfigException;
import u8c.bs.exception.SecurityException;
import u8c.bs.utils.FileUtils;
import u8c.pubitf.action.IAPICustmerDevelop;
import u8c.vo.arrival.EncryptHelper;
import u8c.vo.pub.APIMessageVO;
import u8c.server.HttpURLConnectionDemo;
import u8c.vo.entity.CorpVO;
import u8c.vo.entity.CostsubjVO;
import u8c.vo.applyInvoice.*;

import u8c.bs.convert.JSONConvertor;

public class invoice  implements IAPICustmerDevelop{
	private BaseDAO dao; 
	private BaseDAO getDao() {
		if (dao == null) {
			dao = new BaseDAO();
		}
		return dao;
	}
	@Override
	public String doAction(HttpServletRequest request)  throws BusinessException, ConfigException{
		// 第一步：解析数据
		String obj = "";
		String strBody = "";
		String strTemp = "";
		APIMessageVO messageVO = new APIMessageVO();
		try {
			// 初始化参数	
			obj = this.getRequestPostStr(request.getInputStream());
			strTemp+=obj;
			// 第二步：解析数据之后做后续项目上自己的业务处理
			JSONObject parameJson = JSONObject.parseObject(obj);
						
			ApplyInvoiceMessage message= JSON.toJavaObject(parameJson, ApplyInvoiceMessage.class);
			ApplyInvoiceData data=message.getMessage();
			obj=data.getData();
			
			//解密数据体
			EncryptHelper encryptHelper=new EncryptHelper();
			strBody=encryptHelper.decrypt(obj);
			strTemp+=obj+"\n \n "+strBody; 
			// 写入输入中间文件
			writeMiddleFile(APIConst.INDOCPATH + "u8c.busiitf.invoice", strTemp);
			
			List<ApplyInvoiceBody> bodys=JSON.parseArray(strBody,ApplyInvoiceBody.class);
			
			List<PostResult> listPostResult=new ArrayList();//返回结果
			ApplyInvoiceData dataResult=new ApplyInvoiceData();
			
			for(ApplyInvoiceBody body:bodys){
				PostResult postResult=setPostResult(body);
				listPostResult.add(postResult);
			}
			// 第三步：返回结果
			obj=JSON.toJSONString(listPostResult);
			strTemp+="\n \n "+obj;
			strBody=encryptHelper.encrypt(obj);
			dataResult.setData(strBody);
			//encryptHelper.decrypt(strBody);
			strBody=JSON.toJSONString(dataResult);
			strTemp+="\n \n "+strBody;
			// 写入输入中间文件
			writeMiddleFile(APIConst.RETURNDATAPATH + "u8c.busiitf.invoice",strTemp);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return strBody;
	}
	//处理data


	private PostResult setPostResult(ApplyInvoiceBody body){
		PostResult postResult=new PostResult();
		postResult.setBillID(body.getAdviceNote());		
		String sql3="";
		String strBody="";
		CostsubjVO costsubjVO;
		try{
			// 第一步：组装数据
			BillRootVO billRootVO=new BillRootVO();			
			List<BillVO> listBillVO=new ArrayList();			
			BillVO billVO=new BillVO();		
			//单据头
			ParentVO parentvo=new ParentVO();			
			parentvo.setDjlxbm("F0-01");
			parentvo.setDjrq(body.getAdviceDate());
			
			parentvo.setDwbm(body.getComCode());			
			parentvo.setLrr("13501036623");
			parentvo.setPrepay(false);
			parentvo.setQcbz(false);
			parentvo.setScomment(body.getZyx1());
			parentvo.setXslxbm("arap");
			parentvo.setZyx1(body.getAdviceNote());//自定义1 发票申请单号
			parentvo.setZyx4("蓝票");//自定义4 操作类型
			parentvo.setZyx5(body.getPmName());//自定义5 项目
			parentvo.setZyx6(body.getSmName());//自定义6 业务员
			parentvo.setZyx7(String.valueOf(body.getBusiType()));//自定义7 BusiType
			billVO.setParentvo(parentvo);
			//单据体
			List<ChildrenVO> children=new ArrayList();
			for(ApplyInvoiceDetail detail:body.getDetail()){
				ChildrenVO childrenvo=new ChildrenVO();
				childrenvo.setHbbm(body.getPayerCode());
				if(!detail.getCurrency().equals("CNY")){
					//childrenvo.setBbhl(detail.getCurRate());
					//childrenvo.setBzbm(detail.getCurrency());
					childrenvo.setZyx1(detail.getCurrency().toString()
							+"金额:"
							+detail.getInclusiveMoney().toString()
							+"汇率:"
							+detail.getCurRate().toString());
				}
				
				//收支项目
				sql3="select pk_costsubj,costcode,costname from bd_costsubj where pk_corp=(select pk_corp from bd_corp where unitcode='"+body.getComCode().toString()+"') and costname='"+detail.getInsurTypeName().toString()+"'";
				costsubjVO=(CostsubjVO)getDao().executeQuery(sql3, new BeanProcessor(CostsubjVO.class));
				if (costsubjVO!=null){
					childrenvo.setSzxmid(costsubjVO.getPk_costsubj());
					childrenvo.setSzxmid_code(costsubjVO.getCostcode());
					childrenvo.setSzxmid_name(costsubjVO.getCostname());
				}
				
				childrenvo.setSl(detail.getTaxRate());
				childrenvo.setJfbbje(Double.toString(detail.getInclusiveRMB()));
				childrenvo.setJfybje(Double.toString(detail.getInclusiveRMB()));
				childrenvo.setZyx1(detail.getInsurTypeCode());//自定义1 险种编码
				childrenvo.setZyx2(detail.getInsurTypeName());//自定义2 险种名称
				//childrenvo.setJfybje(detail.getInclusiveMoney());
				childrenvo.setSzxmid("A01001001");
				children.add(childrenvo);
			}
			billVO.setChildren(children);
			
			listBillVO.add(billVO);
			billRootVO.setBillvo(listBillVO);
			
			// 第二步：提交到API				
			// 服务器访问地址及端口,例如 http://ip:port
			String serviceUrl =u8c.server.XmlConfig.getUrl("u8carapysinsert");
			//"http://127.0.0.1:9099/u8cloud/api/arap/ys/insert";
			// 使用U8cloud系统中设置，具体节点路径为：
			// 应用集成 - 系统集成平台 - 系统信息设置
			// 设置信息中具体属性的对照关系如下：
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("trantype", "code"); // 档案翻译方式，枚举值为：编码请录入 code， 名称请录入 name， 主键请录入 pk
			map.put("system", "busiitf"); // 系统编码
			map.put("usercode", "busiuser"); // 用户
			map.put("password", "bbbed85aa52a7dc74fc4b4bca8423394"); // 密码1qazWSX，需要 MD5 加密后录入				
			map.put("uniquekey", body.getAdviceNote());
			strBody=HttpURLConnectionDemo.operator(serviceUrl, map,JSON.toJSONString(billRootVO));
			
			// 第三步：处理结果	
			JSONObject jsonResult =JSON.parseObject(strBody);
			u8c.vo.applyInvoice.DataResponse dataResponse=JSON.toJavaObject(jsonResult, u8c.vo.applyInvoice.DataResponse.class);		
			if (dataResponse.getStatus().equals("success")){// 正常的返回
				postResult.setStatus(dataResponse.getStatus());		
				List<BillVO> billvoResult=JSON.parseArray(dataResponse.getData(),BillVO.class);
				postResult.setU8cCode(billvoResult.get(0).getParentvo().getDjbh());
			}else{// 异常的返回
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

	private String getRequestPostStr(InputStream tInputStream) throws IOException {
		String retStr = null;
		if (tInputStream != null) {
			BufferedReader br = null;
			InputStreamReader isr = null;
			try {
				isr = new InputStreamReader(tInputStream, "UTF-8");// 字符流
				br = new BufferedReader(isr);// 缓冲流
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
