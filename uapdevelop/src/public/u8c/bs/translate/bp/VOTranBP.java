package u8c.bs.translate.bp;

import java.util.Map;

import nc.vo.bd.access.BddataVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.ValueObject;
import net.sf.json.JSONArray;
import u8c.bs.config.RegInfoManager;
import u8c.bs.translate.TranslateUtil;
import u8c.vo.entity.refinfo.RegInfoBill;
import u8c.vo.pub.MapList;
import u8c.vo.pub.MapSet;

/**
 * @功能描述：CircularlyAccessibleValueObject翻译模板
 * @author xuxq3
 * @date 2018-1-9 下午2:00:58
 */
public class VOTranBP extends AbstractTranBP<ValueObject> {

  @Override
  protected String convertToJson(ValueObject[] bills,
      Map<String, Map<String, BddataVO>> docDatas,
      Map<String, RegInfoBill> regInfo) {
    JSONArray array = new JSONArray();
    for (ValueObject bill : bills) {
      array.add(TranslateUtil.convertVO(bill, docDatas, regInfo));
    }
    return array.toString();
  }

  @Override
  protected String[] getOrgs(String billMark, ValueObject vo)
      throws BusinessException {
    String[] fields = RegInfoManager.getInstance().queryOrgFields(billMark);
    if (fields == null || fields.length == 0) {
      return null;
    }
    return super.getOrgsFromVO(fields, vo);
  }

  /*
   * (non-Javadoc)
   * @see u8c.api.bs.translate.bp.AbstractTranBP#getTranCodes(E[])
   */
  @Override
  protected MapSet<String, String> getTranValues(ValueObject[] bills,
      Map<String, RegInfoBill> regInfo) {
    // TODO Auto-generated method stub
    MapSet<String, String> docCodes = new MapSet<String, String>();
    // 递归查找所有的需要翻译的档案编码
    TranslateUtil.findDocCodes(bills, docCodes, regInfo);
    return docCodes;
  }

  /*
   * (non-Javadoc)
   * @see u8c.api.bs.translate.bp.AbstractTranBP#initOrgs(java.lang.String,
   * java.lang.Object)
   */
  @Override
  protected String[] queryOrgs(String billMark, ValueObject vo)
      throws BusinessException {
    String[] fields = RegInfoManager.getInstance().queryOrgFields(billMark);
    if (null == fields || fields.length == 0) {
      return null;
    }
    return super.queryOrgsFromVO(fields, vo);
  }

  /*
   * (non-Javadoc)
   * @see u8c.api.bs.translate.bp.AbstractTranBP#tranPKByCode(E[],
   * java.util.Map)
   */
  @Override
  protected MapList<String, String> tranDocByValue(ValueObject[] bills,
      Map<String, Map<String, BddataVO>> docMap,
      Map<String, RegInfoBill> regInfo) {
    // TODO Auto-generated method stub
    MapList<String, String> errorCodes = new MapList<String, String>();
    // 递归翻译所有的档案
    TranslateUtil.tranDoc(bills, errorCodes, docMap, regInfo);
    return errorCodes;
  }

}
