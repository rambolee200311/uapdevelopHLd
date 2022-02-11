package u8c.bs.translate.bp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.vo.bd.access.BddataVO;
import nc.vo.bd.access.BdinfoManagerServer;
import nc.vo.bd.access.BdinfoVO;
import nc.vo.bd.access.IBdinfoConst;
import nc.vo.pub.BusinessException;
import nc.vo.pub.ValueObject;
import net.sf.json.JSONArray;
import u8c.bs.config.RegInfoManager;
import u8c.bs.translate.QueryIDMappingUtil;
import u8c.bs.translate.TranslateUtil;
import u8c.bs.utils.PropertyHelper;
import u8c.util.oip.APIOutSysUtil;
import u8c.vo.entity.APIPubVO;
import u8c.vo.entity.refinfo.RegInfoBVO;
import u8c.vo.entity.refinfo.RegInfoBill;
import u8c.vo.oip.chg.APIDocChgVO;
import u8c.vo.oip.outsys.APIOutSysVO;
import u8c.vo.pub.MapList;
import u8c.vo.pub.MapSet;
import convert.handler.NoteXmlHandler;

/**
 * @功能描述：翻译的基础模板 <ul>
 *               <li>1、用billmark查找翻译信息
 *               <li>2、先查询出主组织的pk
 *               <li>3、初始化maplist<String,String>key:pk_bdinfo(翻译档案类型pk)，value:
 *               需要翻译的档案code
 *               <li>4、查询出每个档案的map
 *               <li>5、翻译每个code，将属性设置为pk
 *               <li>6、抛出错误信息
 *               </ul>
 * @author xuxq3
 * @date 2018-1-9 下午12:44:26
 * @param <E>
 */

public abstract class AbstractTranBP<E> {

	/**
	 * @param bills
	 * @param billmark
	 * @throws BusinessException
	 */
	public void translate(E[] bills, String billMark) throws BusinessException {

		// 用billmark查找翻译信息初始化
		Map<String, RegInfoBill> regInfo = this.queryRegInfo(billMark);

		// 初始化主组织
		String[] orgValues = this.queryOrgs(billMark, bills[0]);
		// 获取档案的maplist
		MapSet<String, String> docValues = this.getTranValues(bills, regInfo);
		//记录翻译方式
		Map<String,Integer> trantypes = new HashMap<>();// 查询所有档案的值对应的pk
		// 查询所有档案的值对应的pk
		Map<String, Map<String, BddataVO>> docMap = this.queryDocByValue(
				docValues, orgValues,trantypes);
		//解决不翻译0001的问题
		String bdinfo_corp = "00010000000000000031";
		if(docValues.containsKey(bdinfo_corp)){
			Map<String, BddataVO> docMapValue = docMap.get(bdinfo_corp)!=null?
					docMap.get(bdinfo_corp):new HashMap<String, BddataVO>();
			BddataVO bddata = new BddataVO();
			bddata.setPk("0001");
			bddata.setName("集团");
			docMapValue.put("0001", bddata);
			docMap.put(bdinfo_corp, docMapValue);
		}
		// 翻译code
		MapList<String, String> errorCodes = this.tranDocByValue(bills, docMap,
				regInfo);
		// 抛出错误档案信息
		this.buildErrMessage(errorCodes,trantypes);

	}

	/**
	 * @param vos
	 * @param billmark
	 * @throws BusinessException
	 */
	public String translateToJson(E[] vos, String billMark)
			throws BusinessException {
		// 用billmark查找翻译信息初始化
		Map<String, RegInfoBill> regInfo = this.queryRegInfo(billMark);
		// 初始化主组织
		String[] orgValues = this.getOrgs(billMark, vos[0]);
		// 获取编码的maplist
		MapSet<String, String> docIds = this.getTranValues(vos, regInfo);
		Map<String, String[]> otherProperty = this.getOtherPropery(regInfo);
		// 查询所有档案的pk对应的档案
		Map<String, Map<String, BddataVO>> docDatas = this.queryBDVOById(
				docIds, orgValues, otherProperty);
		// 翻译code
		return this.convertToJson(vos, docDatas, regInfo);
	}

	/**
	 * 抛出错误档案信息
	 * 
	 * @throws BusinessException
	 */
	protected void buildErrMessage(MapList<String, String> errorCodes,Map<String,Integer> trantypes)
			throws BusinessException {
		String dataSource = InvocationInfoProxy.getInstance()
				.getUserDataSource();
		if (null != errorCodes && errorCodes.size() > 0) {
			StringBuilder message = new StringBuilder();
			message.append("下面的档案通过传入的以下信息在U8C系统中找不到，请检查！" +
					"可能的原因：" +
					"1、当前单据使用的公司没有启用单据所在的模块；" +
					"2、如果是【存货/项目/客商/人员/收支项目】档案，请检查是否没有分配到当前使用的公司；" +
					"3、如果是【会计科目】档案，请检查是否没有分配到当前使用的会计主体账簿。" +
					"找不到的档案列表：");
			int count=1;
			for (Entry<String, List<String>> entry : errorCodes.entrySet()) {
				String pk_bdinfo = entry.getKey();
				List<String> codes = entry.getValue();
				BdinfoVO bdinfovo = BdinfoManagerServer.getBdInfoVO(dataSource,
						pk_bdinfo);
				message.append(count+"、"+"档案类型【" + bdinfovo.getBdname());
				message.append("】，");
				String valueType="";
				if(trantypes.get(pk_bdinfo).intValue()==APIOutSysVO.TRANTYPE_CODE.intValue()){
					valueType = "档案编码列表";
				}else if(trantypes.get(pk_bdinfo).intValue()==APIOutSysVO.TRANTYPE_NAME.intValue()){
					valueType = "档案名称列表";
				}
				message.append(valueType+"【");
				Set<String> repcodes = new HashSet<String>();
				repcodes.addAll(codes);
				for(String value : repcodes){
					message.append(value);
					message.append("，");
				}
				message.deleteCharAt(message.lastIndexOf("，"));
				message.append("】；");
				count++;
			}
			String retmessage = message.toString();
			throw new BusinessException(retmessage);
		}
	}

	protected abstract String convertToJson(E[] bills,
			Map<String, Map<String, BddataVO>> docDatas,
			Map<String, RegInfoBill> regInfo);

	/**
	 * @desc 获取主组织字段
	 * @return String[]
	 */
	protected abstract String[] getOrgs(String billMark, E bill)
			throws BusinessException;

	/**
	 * 直接从VO中获取组织字段
	 * 
	 * @param billMark
	 * @param vo
	 * @return
	 * @throws BusinessException
	 */
	protected String[] getOrgsFromVO(String[] fields, ValueObject vo)
			throws BusinessException {
		String[] values = new String[2];
		if (fields[0] != null) {
			values[0] = (String) PropertyHelper.getProperty(vo, fields[0]);
		}
		if (fields[1] != null) {
			values[1] = (String) PropertyHelper.getProperty(vo, fields[1]);
		}
		return values;
	}

	protected Map<String, String[]> getOtherPropery(
			Map<String, RegInfoBill> regInfo) {
		Map<String, String[]> otherProperty = new HashMap<>();
		for (RegInfoBill info : regInfo.values()) {
			RegInfoBVO[] binfos = info.getChildrenVO();
			if (binfos != null) {
				for (RegInfoBVO binfo : binfos) {
					String othoerPropery = binfo.getOtherProperty();
					if (binfo.getPk_bdinfo() != null && othoerPropery != null) {
						otherProperty.put(binfo.getPk_bdinfo(),
								othoerPropery.split(","));
					}
				}
			}
		}
		return otherProperty;
	}

	/**
	 * @desc 获取到maplist<String ,String>
	 *       key:pk_bdinfo(翻译档案类型pk)，value:需要翻译的档案code
	 * @return void
	 */
	protected abstract MapSet<String, String> getTranValues(E[] bills,
			Map<String, RegInfoBill> regInfo);

	/**
	 * 通过code查询档案的pk
	 * 
	 * @param orgValues
	 * @param otherProperty
	 * @param codes
	 * @return
	 * @throws BusinessException
	 */
	protected Map<String, Map<String, BddataVO>> queryBDVOById(
			MapSet<String, String> docids, String[] orgValues,
			Map<String, String[]> otherProperty) throws BusinessException {
		Map<String, Map<String, BddataVO>> pkMap = new HashMap<>();
		String pk_corp = null;
		String pk_glorgbook = null;
		if (orgValues != null) {
			pk_corp = orgValues[0];
			pk_glorgbook = orgValues[1];
		}
		for (Entry<String, Set<String>> entry : docids.entrySet()) {
			String pk_bdinfo = entry.getKey();
			Set<String> ids = entry.getValue();
			Map<String, BddataVO> docMap = QueryIDMappingUtil.queryIDMapping(
					pk_bdinfo, pk_corp, pk_glorgbook,
					ids.toArray(new String[0]), otherProperty);
			if (docMap.isEmpty()) {
				continue;
			}
			Map<String, BddataVO> existMap = pkMap.get(pk_bdinfo);
			if (existMap == null) {
				pkMap.put(pk_bdinfo, docMap);
			} else {
				existMap.putAll(docMap);
			}
		}
		return pkMap;

	}

	/**
	 * 通过code查询档案的pk
	 * 
	 * @param codes
	 * @return
	 * @throws BusinessException
	 */
	protected Map<String, Map<String, BddataVO>> queryDocByValue(
			MapSet<String, String> docValues, String[] orgValues,Map<String,Integer>trantypes)
			throws BusinessException {
		Map<String, Map<String, BddataVO>> dataTypeMap = new HashMap<String, Map<String, BddataVO>>();
		for (Entry<String, Set<String>> entry : docValues.entrySet()) {
			String pk_bdinfo = entry.getKey();
			Set<String> values = entry.getValue();
			String pk_corp = null;
			String pk_glorgbook = null;
			if (null != orgValues && orgValues.length > 0) {
				pk_corp = orgValues[0];
				pk_glorgbook = orgValues[1];
			}
			 // 获取翻译方式
		    APIDocChgVO docchgvo = APIOutSysUtil.getDocChgVOByBdinfo(pk_bdinfo);
		    Integer trantype = APIOutSysVO.TRANTYPE_CODE;
		    if (docchgvo != null) {
		      trantype = docchgvo.getHeadVO().getTrantype(); 
		    }
		    else {
		      APIOutSysVO outsysvo = APIOutSysUtil.getCurrentOutSysVO();
		      if (outsysvo != null) {
		        trantype = outsysvo.getTrantype();
		      }
		    }
		    trantypes.put(pk_bdinfo, trantype);
			Map<String, BddataVO> docMap = QueryIDMappingUtil
					.queryIDMappingByValues(pk_bdinfo, pk_corp, pk_glorgbook,
							values.toArray(new String[0]),trantype);
			if (null != docMap && !docMap.isEmpty()) {
				dataTypeMap.put(pk_bdinfo, docMap);
			}
		}
		return dataTypeMap;

	}
	
	

	/**
	 * @desc 查询主组织字段
	 * @return String[]
	 */
	protected abstract String[] queryOrgs(String billMark, E bill)
			throws BusinessException;

	protected String[] queryOrgsFromVO(String[] fields, ValueObject vo)
			throws BusinessException {
		String[] values = new String[2];
		if (null != fields[0]) {
			String pk_corp = (String) PropertyHelper.getProperty(vo, fields[0]);
			values[0] = TranslateUtil.getBdPKByDocValue(IBdinfoConst.CORP,
					pk_corp);
		}
		if (null != fields[1]) {
			String pk_glorgbook = (String) PropertyHelper.getProperty(vo,
					fields[1]);
			values[1] = TranslateUtil.getBdPKByDocValue(IBdinfoConst.GLORGBOOK,
					pk_glorgbook);
		}
		return values;
	}

	/**
	 * @desc 翻译注册信息
	 * @return Map<String,RegInfoBill>
	 */
	protected Map<String, RegInfoBill> queryRegInfo(String billmark)
			throws BusinessException {
		Map<String, RegInfoBill> billRegInfo = RegInfoManager.getInstance()
				.queryClassRegInfo(billmark);
		if (billRegInfo == null || billRegInfo.isEmpty()) {
			throw new BusinessException("找不到翻译信息！");
		}
		return billRegInfo;

	}

	/**
	 * @desc 翻译code
	 * @return 无法翻译的错误信息 MapList<String,String>
	 */
	protected abstract MapList<String, String> tranDocByValue(E[] bills,
			Map<String, Map<String, BddataVO>> docMap,
			Map<String, RegInfoBill> regInfo);

	// 流水号计数器
	private static int count = 0;

	// 获取“表头表体唯一关联标识”流水号
	private static String getNumber() {
		// 流水号前缀为日期
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String leftnumber = sdf.format(date);
		// 流水号后缀为四位递增数字
		AtomicLong at = new AtomicLong();
		Long atLong = at.incrementAndGet();
		String rightnumber = String.format("%04d", atLong + (count++));
		String number = leftnumber + rightnumber;
		return number;
	}

	// json转二维数组
	public Object[][] convertJsonToArray(Map<String, String> fieldsData,
			String json) throws BusinessException {
		// 每次将聚合vo转换为二维数组时，将“流水号计数器”置为0
		count = 0;
		List<APIPubVO> voList = new ArrayList<>();
		// 将json转换成vo数组
		for (Object array : JSONArray.fromObject(json)) {
			voList.add(APIPubVO.getVOFromObject(array));
		}
		APIPubVO[] aggvo = voList.toArray(new APIPubVO[0]);
		// 存放表头表体数据
		List<Object[]> headlist = new ArrayList<Object[]>();
		List<Object[]> bodylist = new ArrayList<Object[]>();
		// 存放表头表体声明字段
		List<String> headstrlist = new ArrayList<>();
		List<String> bodystrlist = new ArrayList<>();

		String[] allFields = fieldsData.keySet().toArray(new String[0]);
		String tempKey = allFields[0].split(":")[0];
		for (String field : allFields) {
			if(tempKey.equals(field.split(":")[0])){
				System.out.println(field);
				headstrlist.add(field.split(":")[1]);
			}else{
				bodystrlist.add(field.split(":")[1]);
			}
		}
		String[] headstr = headstrlist.toArray(new String[0]);
		String[] bodystr = bodystrlist.toArray(new String[0]);

		// 遍历聚合vo
		for (APIPubVO svo : aggvo) {
			APIPubVO hvo = (APIPubVO) svo.getAttributeValue("parentvo");
			APIPubVO[] bvo = (APIPubVO[]) svo.getAttributeValue("childrenvo");
			// 表头赋值
			String num = getNumber();
			List<Object> hvolist = new ArrayList<>();
			hvolist.add("");
			hvolist.add(num);
			hvolist.add("");
			getDataForVO(headlist, headstr, hvo, hvolist);
			// 表体赋值
			for (APIPubVO bbvo : bvo) {
				List<Object> bvolist = new ArrayList<>();
				bvolist.add("");
				bvolist.add(num);
				bvolist.add("");
				getDataForVO(bodylist, bodystr, bbvo, bvolist);
			}
		}
		List<String> keyList = new ArrayList<>(fieldsData.keySet());
		Map<String, String> newFieldsData = new LinkedHashMap<>();
		for(String key : keyList){
			String value = fieldsData.get(key);
			key = key.split(":")[1];
			newFieldsData.put(key, value);
		}
		
		// 装载【】到表头声明字段
		List<String> hlist = getNewFileds(headstr, newFieldsData);
		hlist.add(0, "表头【parentvo】");
		hlist.add(1, "表头唯一标识【id】");
		hlist.add(2, "");
		headstr = hlist.toArray(new String[0]);
		// 装载【】到表体声明字段
		List<String> blist = getNewFileds(bodystr, newFieldsData);
		blist.add(0, "表体【childrenvo】");
		blist.add(1, "对应表头唯一标识【id】");
		blist.add(2, "");
		bodystr = blist.toArray(new String[0]);
		// 获取表头表体数据准备组装
		Object[][] head = headlist.toArray(new Object[0][]);
		Object[][] body = bodylist.toArray(new Object[0][]);
		int len = head.length + body.length + 3;
		Object[][] obj = new Object[len][];
		
		int rowIndex = 0;
		rowIndex = addFields(headstr, head, obj, rowIndex);
		obj[rowIndex++] = null;
		rowIndex = addFields(bodystr, body, obj, rowIndex);
		return obj;
	}
	
	// 组装表头表体数组
	private int addFields(String[] headbodystr, Object[][] fields, Object[][] obj, int rowIndex) {
		obj[rowIndex++] = headbodystr;
		for(int i = 0; i < fields.length; i++){
			obj[rowIndex++] = fields[i];
		}
		return rowIndex;
	}
	
	// 装载【】到表头表体声明字段
	private List<String> getNewFileds(String[] headbodystr, Map<String, String> newFieldsData) {
		List<String> hlist = new ArrayList<>();
		for (int j = 0; j < headbodystr.length; j++) {
			if (headbodystr[j] == null)
				continue;
			// 去掉“表头:”，“表体:”字符串
			String chinesestr = newFieldsData.get(headbodystr[j]).split(":")[1];
			if (headbodystr[j].contains(",")) {
				headbodystr[j] = headbodystr[j].split(",")[0];
			}
			// 装载【】到表头表体声明字段
			headbodystr[j] = NoteXmlHandler.BRACKET[0] + headbodystr[j]
					+ NoteXmlHandler.BRACKET[1];
			headbodystr[j] = chinesestr + headbodystr[j];
			hlist.add((String) headbodystr[j]);
		}
		return hlist;
	}

	// 表头表体赋值
	private void getDataForVO(List<Object[]> headbodylist, String[] headbodystr,
			APIPubVO pubvo, List<Object> volist) {
		for (int i = 0; i < headbodystr.length; i++) {
			String strtemp = "";
			if (headbodystr[i].contains(",")) {
				strtemp = headbodystr[i].split(",")[0];
				headbodystr[i] = headbodystr[i].split(",")[1];
			}
			volist.add(pubvo.getAttributeValue((String) headbodystr[i]));
			if (!"".equals(strtemp))
				headbodystr[i] = strtemp + "," + headbodystr[i];
		}
		Object[] hvoarray = volist.toArray(new Object[0]);
		headbodylist.add(hvoarray);
	}

}
