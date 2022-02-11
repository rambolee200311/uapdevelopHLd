/**
 * 
 */
package u8c.bs.translate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import nc.vo.bd.access.AccessorManager;
import nc.vo.bd.access.BddataVO;
import nc.vo.bd.access.BdinfoManagerServer;
import nc.vo.bd.access.BdinfoVO;
import nc.vo.bd.access.IBDAccessor;
import nc.vo.bd.access.IBdinfoConst;
import nc.vo.pub.BusinessException;
import nc.vo.sm.nodepower.OrgnizeTypeVO;
import u8c.bs.APIConst;
import u8c.util.oip.APIOutSysUtil;
import u8c.vo.oip.chg.APIDocChgVO;
import u8c.vo.oip.outsys.APIOutSysVO;
import u8c.vo.pub.MapList;

/**
 * @���������� ��ȡ����map������
 * @author xuxq3
 * @date 2017-12-20 ����10:35:12
 */
public class QueryIDMappingUtil {

  /**
   * @param otherProperty
   * @desc ��ѯ��������
   * @return Map<String,String>
   * @throws BusinessException
   */
  public static Map<String, BddataVO> queryIDMapping(String pk_bdinfo,
      String pk_corp, String pk_glorgbook, String[] ids,
      Map<String, String[]> otherProperty) throws BusinessException {
    Map<String, BddataVO> docMap = new HashMap<String, BddataVO>();
    // ���ҷ�����
    IBDAccessor tmpAcc = null;
    // ���ҷ�����
    if (QueryIDMappingUtil.isNeedAccountBook(pk_bdinfo)) {
      tmpAcc =
          AccessorManager.getAccessor(pk_bdinfo, OrgnizeTypeVO.ZHUZHANG_TYPE,
              pk_glorgbook);
    }
    else {
      // ���ż�����
      if (pk_corp == null || QueryIDMappingUtil.isGroupData(pk_bdinfo)) {
        tmpAcc = AccessorManager.getAccessor(pk_bdinfo);
      }
      else {
        tmpAcc =
            AccessorManager.getAccessor(pk_bdinfo, OrgnizeTypeVO.COMPANY_TYPE,
                pk_corp);
      }
    }
    String[] fieldnames = otherProperty.get(pk_bdinfo);
    if (fieldnames != null) {
      BdinfoVO info =
          BdinfoManagerServer.getBdInfoVO(APIConst.DEFALUTDB, pk_bdinfo);
      info.setDefFieldnames(fieldnames);
      tmpAcc.setInfoVO(info);
    }
    // ��ȡ��ϵͳ��������
    APIDocChgVO docchgvo = APIOutSysUtil.getDocChgVOByBdinfo(pk_bdinfo);
    // ƴ�ӷ���map
    for (String id : ids) {
      BddataVO doc = tmpAcc.getDocByPk(id);
      if (doc == null) {
        continue;
      }
      BddataVO castdoc = (BddataVO) doc.clone();
      if (docchgvo != null) {
        Integer trantype = docchgvo.getHeadVO().getTrantype();
        // ��֯��������˾���˲�����
        String orgid = pk_corp;
        if (QueryIDMappingUtil.isNeedAccountBook(pk_bdinfo)) {
          orgid = pk_glorgbook;
        }
        if (trantype.intValue() == APIOutSysVO.TRANTYPE_CODE.intValue()) {
          String code = castdoc.getCode();
          code = docchgvo.getSrcValueByDestValue(code, orgid);
          if (code != null) {
            castdoc.setCode(code);
          }
        }
        else if (trantype.intValue() == APIOutSysVO.TRANTYPE_NAME.intValue()) {
          String name = castdoc.getCode();
          name = docchgvo.getSrcValueByDestValue(name, orgid);
          if (name != null) {
            castdoc.setName(name);
          }
        }
      }
      docMap.put(id, castdoc);
    }

    return docMap;
  }

  /**
   * @desc ��ѯ��������
   * @return Map<String,String>
   * @throws BusinessException
   */
  public static Map<String, BddataVO> queryIDMappingByValues(String pk_bdinfo,
      String pk_corp, String pk_glorgbook, String[] docValues,Integer trantype)
      throws BusinessException {

    Map<String, BddataVO> docMap = new HashMap<String, BddataVO>();
    if (null == pk_bdinfo || pk_bdinfo.length() == 0 || docValues == null
        || docValues.length == 0) {
      return docMap;
    }
    IBDAccessor tmpAcc = null;
    String corp = "0001";
    // ���ҷ�����
    if (QueryIDMappingUtil.isNeedAccountBook(pk_bdinfo)) {
      tmpAcc =
          AccessorManager.getAccessor(pk_bdinfo, OrgnizeTypeVO.ZHUZHANG_TYPE,
              pk_glorgbook);
    }
    else {
      if (null != pk_corp && !isGroupData(pk_bdinfo)) {
        // ���ż�����
        corp = pk_corp;
      }
      tmpAcc =
          AccessorManager.getAccessor(pk_bdinfo, OrgnizeTypeVO.COMPANY_TYPE,
              corp);
    }

    // ��ȡ��ϵͳ��������
    APIDocChgVO docchgvo = APIOutSysUtil.getDocChgVOByBdinfo(pk_bdinfo);
    // ������Ӧ
    MapList<String, String> map = new MapList<>();
    if (docchgvo != null) {
      // ��֯��������˾���˲�����
      String orgid = pk_corp;
      if (QueryIDMappingUtil.isNeedAccountBook(pk_bdinfo)) {
        orgid = pk_glorgbook;
      }
      for (int i = 0; i < docValues.length; i++) {
        String destValue = docchgvo.getDestValueBySrcValue(docValues[i], orgid);
        if (destValue != null) {
          map.put(destValue, docValues[i]);
          docValues[i] = destValue;
        }
      }
    }
    // ��ѯ������Ϣ
    if (null != tmpAcc) {
      if (trantype.intValue() == APIOutSysVO.TRANTYPE_CODE.intValue()) {
        docMap = tmpAcc.getDocMapByCodes(docValues);
      }
      else if (trantype.intValue() == APIOutSysVO.TRANTYPE_NAME.intValue()) {
        docMap = tmpAcc.getDocMapByNames(docValues);
      }
      if (!docMap.isEmpty() && !map.isEmpty()) {
        for (Entry<String, List<String>> entry : map.entrySet()) {
          String destValue = entry.getKey();
          List<String> srcValues = entry.getValue();
          BddataVO doc = docMap.get(destValue);
          if (doc != null) {
            docMap.remove(destValue);
            for (String srcValue : srcValues) {
              docMap.put(srcValue, doc);
            }
          }
        }
      }
    }
    return docMap;
  }

  /**
   * �Ƿ��ŵ����������ݵ����ϵĹ�˾��ѯ
   * 
   * @param pk_basdoc
   * @return
   */
  private static boolean isGroupData(String pk_basdoc) {
	  if(IBdinfoConst.CUSTBASDOC.equals(pk_basdoc)) {
		  return true;
	  }
    return false;
  }

  /**
   * ���ݻ�����������ȷ���Ƿ���Ҫ�����ʲ�, ֻ�л�ƿ�Ŀ��ƾ֤���Ϳ�Ŀ������Ҫ�����ʲ���Ϣ
   * 
   * @param basdocName
   * @return
   */
  public static boolean isNeedAccountBook(String pk_basdoc) {
    if (pk_basdoc == null || pk_basdoc.length() == 0) {
      return false;
    }
    if (IBdinfoConst.ACCSUBJ.equals(pk_basdoc)
        || IBdinfoConst.VOUCHERTYPE.equals(pk_basdoc)
        || IBdinfoConst.SUBJTYPE.equals(pk_basdoc)) {
      return true;
    }
    return false;
  }
}
