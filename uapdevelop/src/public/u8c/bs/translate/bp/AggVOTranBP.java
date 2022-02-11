package u8c.bs.translate.bp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import nc.vo.bd.access.BddataVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import u8c.bs.config.RegInfoManager;
import u8c.bs.translate.QueryIDMappingUtil;
import u8c.bs.translate.TranslateUtil;
import u8c.vo.entity.refinfo.RegInfoBill;
import u8c.vo.pub.MapList;
import u8c.vo.pub.MapSet;

/**
 * @���������� AggregatedValueObject����ģ��
 * @author xuxq3
 * @date 2018-1-8 ����1:39:40
 */
public class AggVOTranBP extends AbstractTranBP<AggregatedValueObject> {

  @Override
  public void translate(AggregatedValueObject[] bills, String billMark)
      throws BusinessException {
    super.translate(bills, billMark);
    if (getExtBillMark() != null) {
      super.translate(bills, getExtBillMark());
    }
  }

  @Override
  public String translateToJson(AggregatedValueObject[] bills, String billMark)
      throws BusinessException {
    String extBillMark = getExtBillMark();
    if (extBillMark == null) {
      return super.translateToJson(bills, billMark);
    }
    // ��billmark���ҷ�����Ϣ��ʼ��
    List<String> billMarks = new ArrayList<>();
    billMarks.add(billMark);
    billMarks.add(extBillMark);
    List<Map<String, RegInfoBill>> regInfos = new ArrayList<>();
    List<Map<String, Map<String, BddataVO>>> docDatas = new ArrayList<>();
    for (String mark : billMarks) {
      Map<String, RegInfoBill> regInfo = queryRegInfo(mark);
      regInfos.add(regInfo);
      // ��ȡ�����maplist
      MapSet<String, String> docIds = this.getTranValues(bills, regInfo);
      Map<String, String[]> otherProperty = this.getOtherPropery(regInfo);
      // ��ѯ���е�����pk��Ӧ�ĵ���
      Map<String, Map<String, BddataVO>> docData =
          this.queryBDVOById(mark, docIds, bills, otherProperty);
      docDatas.add(docData);
    }
    // ����code
    return this.convertToJson(bills, docDatas, regInfos);
  }
  
  /**
   * ͨ��code��ѯ������pk
 * @param mark 
   * @param docIds
   * @param bills
   * @param otherProperty
   * @return
 * @throws BusinessException 
   */
  protected Map<String, Map<String, BddataVO>> queryBDVOById(
		String mark, MapSet<String, String> docIds, AggregatedValueObject[] bills,
		Map<String, String[]> otherProperty) throws BusinessException {
	  	Map<String, Map<String, BddataVO>> pkMap = new HashMap<>();
		String pk_corp = null;
		String pk_glorgbook = null;
		
		String[] orgValues = null;
		for(AggregatedValueObject bill : bills) {
			// ��ʼ������֯
			orgValues = this.getOrgs(mark, bill);
			if (orgValues != null) {
				pk_corp = orgValues[0];
				pk_glorgbook = orgValues[1];
			}
			for (Entry<String, Set<String>> entry : docIds.entrySet()) {
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
		}
		return pkMap;
}

private String convertVO(CircularlyAccessibleValueObject vo,
      List<Map<String, Map<String, BddataVO>>> docDatas,
      List<Map<String, RegInfoBill>> regInfos) {
    JSONObject retJson = new JSONObject();
    for (int i = 0; i < docDatas.size(); i++) {
      Map<String, Map<String, BddataVO>> docData = docDatas.get(i);
      Map<String, RegInfoBill> regInfo = regInfos.get(i);
      String json = TranslateUtil.convertVO(vo, docData, regInfo);
      if (json != null) {
        retJson.putAll(JSONObject.fromObject(json));
      }
    }
    return retJson.toString();
  }

  protected String convertToJson(AggregatedValueObject[] bills,
      List<Map<String, Map<String, BddataVO>>> docDatas,
      List<Map<String, RegInfoBill>> regInfos) {
    JSONArray array = new JSONArray();
    for (AggregatedValueObject bill : bills) {
      JSONObject billJson = new JSONObject();
      billJson.put("parentvo",
          convertVO(bill.getParentVO(), docDatas, regInfos));
      JSONArray bodyJson = new JSONArray();
      // fuyshb ���Ӽ�У���Ӽ�Ϊ�յ���� 20190218
      if (null != bill.getChildrenVO() && bill.getChildrenVO().length > 0) {
        for (CircularlyAccessibleValueObject body : bill.getChildrenVO()) {
          bodyJson.add(convertVO(body, docDatas, regInfos));
        }
      }
      billJson.put("childrenvo", bodyJson);
      array.add(billJson);
    }
    return array.toString();
  }

  @Override
  protected String convertToJson(AggregatedValueObject[] bills,
      Map<String, Map<String, BddataVO>> docDatas,
      Map<String, RegInfoBill> regInfo) {
    JSONArray array = new JSONArray();
    for (AggregatedValueObject bill : bills) {
      JSONObject billJson = new JSONObject();
      billJson.put("parentvo",
          TranslateUtil.convertVO(bill.getParentVO(), docDatas, regInfo));
      JSONArray bodyJson = new JSONArray();
      // fuyshb ���Ӽ�У���Ӽ�Ϊ�յ���� 20190218
      if (null != bill.getChildrenVO() && bill.getChildrenVO().length > 0) {
        for (CircularlyAccessibleValueObject body : bill.getChildrenVO()) {
          bodyJson.add(TranslateUtil.convertVO(body, docDatas, regInfo));
        }
      }
      billJson.put("childrenvo", bodyJson);
      array.add(billJson);
    }
    return array.toString();
  }

  /**
   * ����ĵ��ݱ�ʶ
   * 
   * @return
   */
  protected String getExtBillMark() {
    return null;
  }

  @Override
  protected String[] getOrgs(String billMark, AggregatedValueObject bill)
      throws BusinessException {
    String[] fields = RegInfoManager.getInstance().queryOrgFields(billMark);
    if (fields == null || fields.length == 0) {
      throw new BusinessException("�Ҳ�������֯�ֶΣ�");
    }
    return super.getOrgsFromVO(fields, bill.getParentVO());
  }

  /**
   * @desc ��ȡ��maplist<String ,String>
   *       key:pk_bdinfo(���뵵������pk)��value:��Ҫ����ĵ���code
   * @return void
   */
  @Override
  protected MapSet<String, String> getTranValues(AggregatedValueObject[] bills,
      Map<String, RegInfoBill> regInfo) {
    MapSet<String, String> docCodes = new MapSet<String, String>();
    // ����ÿһ������
    for (AggregatedValueObject bill : bills) {
      // �ݹ��ҵ���ͷ�����е�Ƕ�׵�CircularlyAccessibleValueObject���͵�vo����vos
      CircularlyAccessibleValueObject headvo = bill.getParentVO();
      TranslateUtil.findDocCodes(headvo, docCodes, regInfo);
      // �ݹ��ҵ���������е�Ƕ�׵�CircularlyAccessibleValueObject���͵�vo����vos
      CircularlyAccessibleValueObject[] bodys = bill.getChildrenVO();
      TranslateUtil.findDocCodes(bodys, docCodes, regInfo);
    }
    return docCodes;
  }

  /**
   * @desc ��ѯ����֯�ֶβ���ʼ��
   * @return String[]
   */
  @Override
  protected String[] queryOrgs(String billMark, AggregatedValueObject bill)
      throws BusinessException {
    String[] fields = RegInfoManager.getInstance().queryOrgFields(billMark);
    if (null == fields || fields.length == 0) {
      throw new BusinessException("�Ҳ�������֯�ֶΣ�");
    }
    return super.queryOrgsFromVO(fields, bill.getParentVO());
  }

  /**
   * @desc ����code
   * @return �޷�����Ĵ�����Ϣ MapList<String,String>
   */
  @Override
  protected MapList<String, String> tranDocByValue(
      AggregatedValueObject[] bills, Map<String, Map<String, BddataVO>> docMap,
      Map<String, RegInfoBill> regInfo) {
    MapList<String, String> errorCodes = new MapList<String, String>();
    // ����ÿһ������
    for (AggregatedValueObject bill : bills) {
      // �ݹ��ҵ���ͷ�����е�Ƕ�׵�CircularlyAccessibleValueObject���͵�vo����vos
      CircularlyAccessibleValueObject headvo = bill.getParentVO();
      TranslateUtil.tranDoc(headvo, errorCodes, docMap, regInfo);
      // �ݹ��ҵ���������е�Ƕ�׵�CircularlyAccessibleValueObject���͵�vo����vos
      CircularlyAccessibleValueObject[] bodys = bill.getChildrenVO();
      TranslateUtil.tranDoc(bodys, errorCodes, docMap, regInfo);
    }
    return errorCodes;

  }

}
