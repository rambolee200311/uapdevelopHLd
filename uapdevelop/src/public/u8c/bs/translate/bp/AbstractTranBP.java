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
 * @��������������Ļ���ģ�� <ul>
 *               <li>1����billmark���ҷ�����Ϣ
 *               <li>2���Ȳ�ѯ������֯��pk
 *               <li>3����ʼ��maplist<String,String>key:pk_bdinfo(���뵵������pk)��value:
 *               ��Ҫ����ĵ���code
 *               <li>4����ѯ��ÿ��������map
 *               <li>5������ÿ��code������������Ϊpk
 *               <li>6���׳�������Ϣ
 *               </ul>
 * @author xuxq3
 * @date 2018-1-9 ����12:44:26
 * @param <E>
 */

public abstract class AbstractTranBP<E> {

	/**
	 * @param bills
	 * @param billmark
	 * @throws BusinessException
	 */
	public void translate(E[] bills, String billMark) throws BusinessException {

		// ��billmark���ҷ�����Ϣ��ʼ��
		Map<String, RegInfoBill> regInfo = this.queryRegInfo(billMark);

		// ��ʼ������֯
		String[] orgValues = this.queryOrgs(billMark, bills[0]);
		// ��ȡ������maplist
		MapSet<String, String> docValues = this.getTranValues(bills, regInfo);
		//��¼���뷽ʽ
		Map<String,Integer> trantypes = new HashMap<>();// ��ѯ���е�����ֵ��Ӧ��pk
		// ��ѯ���е�����ֵ��Ӧ��pk
		Map<String, Map<String, BddataVO>> docMap = this.queryDocByValue(
				docValues, orgValues,trantypes);
		//���������0001������
		String bdinfo_corp = "00010000000000000031";
		if(docValues.containsKey(bdinfo_corp)){
			Map<String, BddataVO> docMapValue = docMap.get(bdinfo_corp)!=null?
					docMap.get(bdinfo_corp):new HashMap<String, BddataVO>();
			BddataVO bddata = new BddataVO();
			bddata.setPk("0001");
			bddata.setName("����");
			docMapValue.put("0001", bddata);
			docMap.put(bdinfo_corp, docMapValue);
		}
		// ����code
		MapList<String, String> errorCodes = this.tranDocByValue(bills, docMap,
				regInfo);
		// �׳����󵵰���Ϣ
		this.buildErrMessage(errorCodes,trantypes);

	}

	/**
	 * @param vos
	 * @param billmark
	 * @throws BusinessException
	 */
	public String translateToJson(E[] vos, String billMark)
			throws BusinessException {
		// ��billmark���ҷ�����Ϣ��ʼ��
		Map<String, RegInfoBill> regInfo = this.queryRegInfo(billMark);
		// ��ʼ������֯
		String[] orgValues = this.getOrgs(billMark, vos[0]);
		// ��ȡ�����maplist
		MapSet<String, String> docIds = this.getTranValues(vos, regInfo);
		Map<String, String[]> otherProperty = this.getOtherPropery(regInfo);
		// ��ѯ���е�����pk��Ӧ�ĵ���
		Map<String, Map<String, BddataVO>> docDatas = this.queryBDVOById(
				docIds, orgValues, otherProperty);
		// ����code
		return this.convertToJson(vos, docDatas, regInfo);
	}

	/**
	 * �׳����󵵰���Ϣ
	 * 
	 * @throws BusinessException
	 */
	protected void buildErrMessage(MapList<String, String> errorCodes,Map<String,Integer> trantypes)
			throws BusinessException {
		String dataSource = InvocationInfoProxy.getInstance()
				.getUserDataSource();
		if (null != errorCodes && errorCodes.size() > 0) {
			StringBuilder message = new StringBuilder();
			message.append("����ĵ���ͨ�������������Ϣ��U8Cϵͳ���Ҳ��������飡" +
					"���ܵ�ԭ��" +
					"1����ǰ����ʹ�õĹ�˾û�����õ������ڵ�ģ�飻" +
					"2������ǡ����/��Ŀ/����/��Ա/��֧��Ŀ�������������Ƿ�û�з��䵽��ǰʹ�õĹ�˾��" +
					"3������ǡ���ƿ�Ŀ�������������Ƿ�û�з��䵽��ǰʹ�õĻ�������˲���" +
					"�Ҳ����ĵ����б�");
			int count=1;
			for (Entry<String, List<String>> entry : errorCodes.entrySet()) {
				String pk_bdinfo = entry.getKey();
				List<String> codes = entry.getValue();
				BdinfoVO bdinfovo = BdinfoManagerServer.getBdInfoVO(dataSource,
						pk_bdinfo);
				message.append(count+"��"+"�������͡�" + bdinfovo.getBdname());
				message.append("����");
				String valueType="";
				if(trantypes.get(pk_bdinfo).intValue()==APIOutSysVO.TRANTYPE_CODE.intValue()){
					valueType = "���������б�";
				}else if(trantypes.get(pk_bdinfo).intValue()==APIOutSysVO.TRANTYPE_NAME.intValue()){
					valueType = "���������б�";
				}
				message.append(valueType+"��");
				Set<String> repcodes = new HashSet<String>();
				repcodes.addAll(codes);
				for(String value : repcodes){
					message.append(value);
					message.append("��");
				}
				message.deleteCharAt(message.lastIndexOf("��"));
				message.append("����");
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
	 * @desc ��ȡ����֯�ֶ�
	 * @return String[]
	 */
	protected abstract String[] getOrgs(String billMark, E bill)
			throws BusinessException;

	/**
	 * ֱ�Ӵ�VO�л�ȡ��֯�ֶ�
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
	 * @desc ��ȡ��maplist<String ,String>
	 *       key:pk_bdinfo(���뵵������pk)��value:��Ҫ����ĵ���code
	 * @return void
	 */
	protected abstract MapSet<String, String> getTranValues(E[] bills,
			Map<String, RegInfoBill> regInfo);

	/**
	 * ͨ��code��ѯ������pk
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
	 * ͨ��code��ѯ������pk
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
			 // ��ȡ���뷽ʽ
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
	 * @desc ��ѯ����֯�ֶ�
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
	 * @desc ����ע����Ϣ
	 * @return Map<String,RegInfoBill>
	 */
	protected Map<String, RegInfoBill> queryRegInfo(String billmark)
			throws BusinessException {
		Map<String, RegInfoBill> billRegInfo = RegInfoManager.getInstance()
				.queryClassRegInfo(billmark);
		if (billRegInfo == null || billRegInfo.isEmpty()) {
			throw new BusinessException("�Ҳ���������Ϣ��");
		}
		return billRegInfo;

	}

	/**
	 * @desc ����code
	 * @return �޷�����Ĵ�����Ϣ MapList<String,String>
	 */
	protected abstract MapList<String, String> tranDocByValue(E[] bills,
			Map<String, Map<String, BddataVO>> docMap,
			Map<String, RegInfoBill> regInfo);

	// ��ˮ�ż�����
	private static int count = 0;

	// ��ȡ����ͷ����Ψһ������ʶ����ˮ��
	private static String getNumber() {
		// ��ˮ��ǰ׺Ϊ����
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String leftnumber = sdf.format(date);
		// ��ˮ�ź�׺Ϊ��λ��������
		AtomicLong at = new AtomicLong();
		Long atLong = at.incrementAndGet();
		String rightnumber = String.format("%04d", atLong + (count++));
		String number = leftnumber + rightnumber;
		return number;
	}

	// jsonת��ά����
	public Object[][] convertJsonToArray(Map<String, String> fieldsData,
			String json) throws BusinessException {
		// ÿ�ν��ۺ�voת��Ϊ��ά����ʱ��������ˮ�ż���������Ϊ0
		count = 0;
		List<APIPubVO> voList = new ArrayList<>();
		// ��jsonת����vo����
		for (Object array : JSONArray.fromObject(json)) {
			voList.add(APIPubVO.getVOFromObject(array));
		}
		APIPubVO[] aggvo = voList.toArray(new APIPubVO[0]);
		// ��ű�ͷ��������
		List<Object[]> headlist = new ArrayList<Object[]>();
		List<Object[]> bodylist = new ArrayList<Object[]>();
		// ��ű�ͷ���������ֶ�
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

		// �����ۺ�vo
		for (APIPubVO svo : aggvo) {
			APIPubVO hvo = (APIPubVO) svo.getAttributeValue("parentvo");
			APIPubVO[] bvo = (APIPubVO[]) svo.getAttributeValue("childrenvo");
			// ��ͷ��ֵ
			String num = getNumber();
			List<Object> hvolist = new ArrayList<>();
			hvolist.add("");
			hvolist.add(num);
			hvolist.add("");
			getDataForVO(headlist, headstr, hvo, hvolist);
			// ���帳ֵ
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
		
		// װ�ء�������ͷ�����ֶ�
		List<String> hlist = getNewFileds(headstr, newFieldsData);
		hlist.add(0, "��ͷ��parentvo��");
		hlist.add(1, "��ͷΨһ��ʶ��id��");
		hlist.add(2, "");
		headstr = hlist.toArray(new String[0]);
		// װ�ء��������������ֶ�
		List<String> blist = getNewFileds(bodystr, newFieldsData);
		blist.add(0, "���塾childrenvo��");
		blist.add(1, "��Ӧ��ͷΨһ��ʶ��id��");
		blist.add(2, "");
		bodystr = blist.toArray(new String[0]);
		// ��ȡ��ͷ��������׼����װ
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
	
	// ��װ��ͷ��������
	private int addFields(String[] headbodystr, Object[][] fields, Object[][] obj, int rowIndex) {
		obj[rowIndex++] = headbodystr;
		for(int i = 0; i < fields.length; i++){
			obj[rowIndex++] = fields[i];
		}
		return rowIndex;
	}
	
	// װ�ء�������ͷ���������ֶ�
	private List<String> getNewFileds(String[] headbodystr, Map<String, String> newFieldsData) {
		List<String> hlist = new ArrayList<>();
		for (int j = 0; j < headbodystr.length; j++) {
			if (headbodystr[j] == null)
				continue;
			// ȥ������ͷ:����������:���ַ���
			String chinesestr = newFieldsData.get(headbodystr[j]).split(":")[1];
			if (headbodystr[j].contains(",")) {
				headbodystr[j] = headbodystr[j].split(",")[0];
			}
			// װ�ء�������ͷ���������ֶ�
			headbodystr[j] = NoteXmlHandler.BRACKET[0] + headbodystr[j]
					+ NoteXmlHandler.BRACKET[1];
			headbodystr[j] = chinesestr + headbodystr[j];
			hlist.add((String) headbodystr[j]);
		}
		return hlist;
	}

	// ��ͷ���帳ֵ
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
