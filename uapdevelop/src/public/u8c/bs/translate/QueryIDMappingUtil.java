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
 * @功能描述： 获取翻译map工具类
 * @author xuxq3
 * @date 2017-12-20 上午10:35:12
 */
public class QueryIDMappingUtil {

  /**
   * @param otherProperty
   * @desc 查询翻译数据
   * @return Map<String,String>
   * @throws BusinessException
   */
  public static Map<String, BddataVO> queryIDMapping(String pk_bdinfo,
      String pk_corp, String pk_glorgbook, String[] ids,
      Map<String, String[]> otherProperty) throws BusinessException {
    Map<String, BddataVO> docMap = new HashMap<String, BddataVO>();
    // 查找翻译器
    IBDAccessor tmpAcc = null;
    // 查找翻译器
    if (QueryIDMappingUtil.isNeedAccountBook(pk_bdinfo)) {
      tmpAcc =
          AccessorManager.getAccessor(pk_bdinfo, OrgnizeTypeVO.ZHUZHANG_TYPE,
              pk_glorgbook);
    }
    else {
      // 集团级档案
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
    // 获取外系统档案对照
    APIDocChgVO docchgvo = APIOutSysUtil.getDocChgVOByBdinfo(pk_bdinfo);
    // 拼接翻译map
    for (String id : ids) {
      BddataVO doc = tmpAcc.getDocByPk(id);
      if (doc == null) {
        continue;
      }
      BddataVO castdoc = (BddataVO) doc.clone();
      if (docchgvo != null) {
        Integer trantype = docchgvo.getHeadVO().getTrantype();
        // 组织主键，公司或账簿主键
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
   * @desc 查询翻译数据
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
    // 查找翻译器
    if (QueryIDMappingUtil.isNeedAccountBook(pk_bdinfo)) {
      tmpAcc =
          AccessorManager.getAccessor(pk_bdinfo, OrgnizeTypeVO.ZHUZHANG_TYPE,
              pk_glorgbook);
    }
    else {
      if (null != pk_corp && !isGroupData(pk_bdinfo)) {
        // 集团级档案
        corp = pk_corp;
      }
      tmpAcc =
          AccessorManager.getAccessor(pk_bdinfo, OrgnizeTypeVO.COMPANY_TYPE,
              corp);
    }

    // 获取外系统档案对照
    APIDocChgVO docchgvo = APIOutSysUtil.getDocChgVOByBdinfo(pk_bdinfo);
    // 档案对应
    MapList<String, String> map = new MapList<>();
    if (docchgvo != null) {
      // 组织主键，公司或账簿主键
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
    // 查询档案信息
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
   * 是否集团档案，不根据单据上的公司查询
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
   * 根据基本档案主键确定是否需要主体帐簿, 只有会计科目、凭证类别和科目类型需要主体帐簿信息
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
