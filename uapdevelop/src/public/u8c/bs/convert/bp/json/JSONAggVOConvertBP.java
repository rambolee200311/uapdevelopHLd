package u8c.bs.convert.bp.json;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.util.JSONTokener;
import u8c.bs.convert.JSONConvertor;
import u8c.bs.convert.bp.AbstractConvertBP;
import u8c.bs.utils.ArrayUtils;
import u8c.bs.utils.PropertyHelper;

/**
 * @����������
 *        <ul>
 *        <li>����JSONArray��ת������ΪAggregatedValueObject�����ת��ģ��
 *        </ul>
 * @author xuxq3
 * @since 2.0
 * @date 2018-1-18 ����11:13:35
 */

public class JSONAggVOConvertBP<E extends AggregatedValueObject> extends
    AbstractConvertBP<E, String> {

  /**
   * @param billMark
   */
  public JSONAggVOConvertBP(String billMark) {
    super(billMark);
    // TODO Auto-generated constructor stub
  }

  protected Object parseChildJSON(String bClazzName, Object childObj)
      throws ClassNotFoundException, BusinessException {

    Object retObj = null;
    Class<?> bClazz = Class.forName(bClazzName);
    // ��������ת��
    if (childObj instanceof JSONArray) {
      JSONArray childsJSON = JSONArray.fromObject(childObj);
      retObj = JSONConvertor.parseJSONArray(childsJSON, bClazz);
    }
    else if (childObj instanceof JSONObject) {
      JSONObject childJSON = JSONObject.fromObject(childObj);
      retObj = JSONConvertor.parseJSONObject(childJSON, bClazz);
    }
    return retObj;

  }

  @Override
  protected E[] parse(String json) throws BusinessException {
    try {
      JSONArray JsonArray = JSONArray.fromObject(json);
      Class<?> clazz = Class.forName(this.mainEntity);
      List<Object> bills = new ArrayList<Object>();
      for (Object jsonObj : JsonArray.toArray()) {
        if (null == jsonObj || jsonObj.toString().length() == 0) {
          continue;
        }
        Object bill = clazz.newInstance();
        this.processBillAfterNew(bill);
        // ����ÿ�ŵ��ݵ�json
        JSONObject billJSON = JSONObject.fromObject(jsonObj);
        Iterator<?> it = billJSON.keys();
        while (it.hasNext()) {
          String mark = (String) it.next();
          Object childObj = billJSON.get(mark);
          if (null == childObj || childObj.toString().length() == 0) {
            continue;
          }
          //changed by xuxq3 ���ݴ�Сд����ֹд���´������ݴ���
          if (this.entityRelation.containsKey(mark)) {
        	  
          }else if(this.entityRelation.containsKey(mark.toLowerCase())){
        	  mark = mark.toLowerCase();
          }else{
        	  continue;
          }
          // ��ʵ���json����
          Object bJSON = new JSONTokener(childObj.toString()).nextValue();
          // �õ���ʵ�����ȫ·��
          String bClazzName = this.entityRelation.get(mark).getFullclassname();
          Object bObj = this.parseChildJSON(bClazzName, bJSON);
          PropertyHelper.setFormatProperty(bill, mark, bObj);
        }
        bills.add(bill);
      }
      Object retObjs =
          ArrayUtils.convertArrayType(bills.toArray(new Object[0]), clazz);
      return (E[]) retObjs;
    }
    catch (ClassNotFoundException e) {
      throw new BusinessException(e.getMessage(), e.getCause());
    }
    catch (InstantiationException e) {
      throw new BusinessException(e.getMessage(), e.getCause());
    }
    catch (IllegalAccessException e) {
      throw new BusinessException(e.getMessage(), e.getCause());
    }
  }

	/**
	 * ��������ʵ�ֵģ����ʼ���������Щ���飬�������޸Ľӿ��У���ʼ������Ҫ�����Զ���ճ�null���������ֲ������ǳ�ʼ����ֵ�����޸ĵ�ֵ
	 * jilu
	 * 
	 * @param bill
	 */
	protected void processBillAfterNew(Object bill) {

	}
}
