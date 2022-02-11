package u8c.bs.translate;

import java.util.List;
import java.util.Map;

import nc.vo.bd.access.AccessorManager;
import nc.vo.bd.access.BddataVO;
import nc.vo.bd.access.IBDAccessor;
import nc.vo.pub.BusinessException;
import nc.vo.pub.ValueObject;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import u8c.bs.utils.ArrayUtils;
import u8c.bs.utils.PropertyHelper;
import u8c.util.oip.APIOutSysUtil;
import u8c.vo.entity.refinfo.RegInfoBVO;
import u8c.vo.entity.refinfo.RegInfoBill;
import u8c.vo.oip.chg.APIDocChgVO;
import u8c.vo.oip.outsys.APIOutSysVO;
import u8c.vo.pub.MapList;
import u8c.vo.pub.MapSet;

/**
 * @功能描述： <ul>
 *        <li>翻译工具类
 *        </ul>
 * @author xuxq3
 * @since 2.0
 * @date 2018-1-10 下午12:30:24
 */

public class TranslateUtil {

  public static String convertVO(ValueObject vo,
      Map<String, Map<String, BddataVO>> docDatas,
      Map<String, RegInfoBill> regInfo) {
    JSONObject voJson = new JSONObject();
    // 类名称
    String clazz = vo.getClass().getName();
    // 获取注册信息
    RegInfoBill childRegInfo = regInfo.get(clazz);
    if (childRegInfo == null) {
      return null;
    }
    for (RegInfoBVO bregInfo : childRegInfo.getChildrenVO()) {
      if (bregInfo == null) {
        continue;
      }
      // 字段名称
      String fieldName = bregInfo.getPropertyName();
      // 档案类型字段
      String pk_bdinfo = bregInfo.getPk_bdinfo();
      Object value = PropertyHelper.getProperty(vo, fieldName);
      if (value instanceof ValueObject) {
        // 如果是实体vo或者实体数组，接着递归处理
        voJson.put(fieldName,
            TranslateUtil.convertVO((ValueObject) value, docDatas, regInfo));
      }
      else if (value instanceof ValueObject[]) {
        // 如果是实体vo或者实体数组，接着递归处理
        JSONArray voArray = new JSONArray();
        for (ValueObject object : (ValueObject[]) value) {
          voArray.add(TranslateUtil.convertVO(object, docDatas, regInfo));
        }
        voJson.put(fieldName, voArray);
      }
      else if (value instanceof List) {
        // 如果是实体vo或者实体数组，接着递归处理
        JSONArray voArray = new JSONArray();
        for (Object object : (List) value) {
          voArray.add(TranslateUtil.convertVO((ValueObject) object, docDatas,
              regInfo));
        }
        voJson.put(fieldName, voArray);
      }
      else if (value != null) {
        voJson.put(fieldName, convertValue(value));
        if (pk_bdinfo != null && docDatas.containsKey(pk_bdinfo)) {
          BddataVO docData = docDatas.get(pk_bdinfo).get(value);
          if (docData != null) {
            if (bregInfo.getCodeProperty() != null) {
              voJson.put(bregInfo.getCodeProperty(), docData.getCode());
            }
            if (bregInfo.getNameProperty() != null) {
              voJson.put(bregInfo.getNameProperty(), docData.getName());
            }
            String otherProperty = bregInfo.getOtherProperty();
            if (otherProperty != null) {
              for (String property : otherProperty.split(",")) {
                voJson.put(property, docData.getDefFieldValue(property));
              }
            }
          }
        }
      }
      else if (bregInfo.getIsdef().booleanValue()) {
        // 自定义项特殊处理
        String nameField = bregInfo.getNameProperty();
        if (nameField != null) {
          Object nameValue = PropertyHelper.getProperty(vo, nameField);
          if (null != nameValue) {
            voJson.put(nameField, nameValue);
          }
        }
      }
    }
    return voJson.toString();
  }

  /**
   * 递归查找所有的需要翻译的档案的code
   * 
   * @param object
   * @param codes
   */
  public static void findDocCodes(Object object,
      MapSet<String, String> docCodes, Map<String, RegInfoBill> regInfo) {
    if (object instanceof ValueObject[]) {
      ValueObject[] items = (ValueObject[]) object;
      for (ValueObject item : items) {
        TranslateUtil.findDocCodes(item, docCodes, regInfo);
      }
    }
    else if (object instanceof List) {
      List items = (List) object;
      for (Object item : items) {
        TranslateUtil.findDocCodes(item, docCodes, regInfo);
      }
    }
    else if (object instanceof ValueObject) {
      // 类名称
      String clazz = object.getClass().getName();
      // 获取注册信息
      RegInfoBill childRegInfo = regInfo.get(clazz);
      if (null != childRegInfo) {
        RegInfoBVO[] bregInfos = childRegInfo.getChildrenVO();
        if (ArrayUtils.isNotEmpty(bregInfos)) {
          for (RegInfoBVO bregInfo : bregInfos) {
            if (null == bregInfo) {
              continue;
            }
            // 字段名称
            String filedcode = bregInfo.getPropertyName();
            // 档案类型字段
            String pk_bdinfo = bregInfo.getPk_bdinfo();
            Object value = PropertyHelper.getProperty(object, filedcode);
            if (value instanceof ValueObject || value instanceof ValueObject[]
                || value instanceof List) {
              // 如果是实体vo或者实体数组，接着递归处理
              TranslateUtil.findDocCodes(value, docCodes, regInfo);
            }
            else if (value != null && !value.equals("") && null != pk_bdinfo) {
              // 档案属性，将code和档案类型放到doccodes
              docCodes.put(pk_bdinfo, (String) value);
            }
          }
        }
      }
    }
  }

  /**
   * 翻译集团档案
   * 
   * @param pk_bdinfo
   * @param docCode
   * @return
   * @throws BusinessException
   */
  public static String getBdPKByDocValue(String pk_bdinfo, String docValue)
      throws BusinessException {
    APIDocChgVO docchgvo = APIOutSysUtil.getDocChgVOByBdinfo(pk_bdinfo);
    Integer trantype = APIOutSysVO.TRANTYPE_CODE;
    String newDocValue = docValue;
    if (docchgvo != null) {
      trantype = docchgvo.getHeadVO().getTrantype();
      String destValue = docchgvo.getDestValueBySrcValue(docValue, null);
      if (destValue != null) {
        newDocValue = destValue;
      }
    }
    else {
      APIOutSysVO outsysvo = APIOutSysUtil.getCurrentOutSysVO();
      if (outsysvo != null) {
        trantype = outsysvo.getTrantype();
      }
    }
    IBDAccessor bdAccessor = AccessorManager.getAccessor(pk_bdinfo);
    if (bdAccessor != null) {
      BddataVO bddatavo = null;
      if (trantype.intValue() == APIOutSysVO.TRANTYPE_CODE.intValue()) {
        bddatavo = bdAccessor.getDocByCode(newDocValue);
      }
      else if (trantype.intValue() == APIOutSysVO.TRANTYPE_NAME.intValue()) {
        bddatavo = bdAccessor.getDocByName(newDocValue);
      }
      if (bddatavo != null) {
        return bddatavo.getPk();
      }
    }
    return null;
  }

  /**
   * 翻译每个子实体的档案
   * 
   * @param object
   * @param errorcodes
   * @param docpks
   */
  public static void tranDoc(Object object, MapList<String, String> errorCodes,
      Map<String, Map<String, BddataVO>> docMap,
      Map<String, RegInfoBill> regInfo) {
    if (object instanceof ValueObject[]) {
      ValueObject[] items = (ValueObject[]) object;
      for (ValueObject item : items) {
        TranslateUtil.tranDoc(item, errorCodes, docMap, regInfo);
      }
    }
    else if (object instanceof List) {
      List items = (List) object;
      for (Object item : items) {
        TranslateUtil.tranDoc(item, errorCodes, docMap, regInfo);
      }
    }
    else if (object instanceof ValueObject) {
      // 类名称
      String clazz = object.getClass().getName();
      // 获取注册信息
      RegInfoBill childRegInfo = regInfo.get(clazz);
      if (null != childRegInfo) {
        RegInfoBVO[] bregInfos = childRegInfo.getChildrenVO();
        if (null == bregInfos || bregInfos.length == 0) {
          return;
        }
        for (RegInfoBVO bregInfo : bregInfos) {
          if (null == bregInfo) {
            continue;
          }
          // 字段名称
          String fieldName = bregInfo.getPropertyName();
          // 档案类型字段
          String pk_bdinfo = bregInfo.getPk_bdinfo();
          Object value = PropertyHelper.getProperty(object, fieldName);
          if (value instanceof ValueObject || value instanceof ValueObject[]
              || value instanceof List) {
            // 如果是实体vo或者实体数组，接着递归处理
            TranslateUtil.tranDoc(value, errorCodes, docMap, regInfo);
          }
          else if (value != null && !value.equals("") && null != pk_bdinfo) {
            if (docMap.containsKey(pk_bdinfo)
                && docMap.get(pk_bdinfo).containsKey(value)) {
              BddataVO doc = docMap.get(pk_bdinfo).get(value);
              // 有相应的档案，直接翻译
              PropertyHelper.setFormatProperty(object, fieldName, doc.getPk());
              if (bregInfo.getIsdef().booleanValue()
                  && bregInfo.getNameProperty() != null) {
                // 自定义项特殊处理
                PropertyHelper.setFormatProperty(object,
                    bregInfo.getNameProperty(), doc.getName());
              }
            }
            else {
              // 通过code没找到pk，记录下来
              errorCodes.put(pk_bdinfo, (String) value);
            }
          }
        }
      }
    }
  }

  /** 转换值 */
  private static Object convertValue(Object value) {
    if (value instanceof UFBoolean) {
      return ((UFBoolean) value).booleanValue();
    }
    if (value instanceof UFDouble) {
      return value;
    }
    return value.toString();
  }
}
