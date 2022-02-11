package u8c.bs.action.query;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import nc.bs.dao.BaseDAO;
import nc.bs.framework.common.NCLocator;
import nc.jdbc.framework.page.SQLBuilderFactory;
import nc.vo.bd.access.BdinfoManager;
import nc.vo.bd.access.BdinfoVO;
import nc.vo.bd.access.IBdinfoConst;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDateTime;
import net.sf.json.JSONObject;
import u8c.bs.APIConst;
import u8c.bs.exception.ConfigException;
import u8c.bs.translate.QueryIDMappingUtil;
import u8c.bs.translate.TranslateUtil;
import u8c.bs.utils.FileUtils;
import u8c.itf.oip.chg.IChgToJson;
import u8c.jdbc.processor.FieldArrayProcessor;
import u8c.jdbc.processor.QueryCountProcessor;
import u8c.pubitf.action.IAction;
import u8c.pubitf.action.IQueryForExcel;
import u8c.pubitf.config.IAPIConfigFileService;
import u8c.util.oip.APIOutSysUtil;
import u8c.vo.entity.APICondition;
import u8c.vo.entity.GlobalSetVO;
import u8c.vo.entity.InputDataVO;
import u8c.vo.entity.enumtype.BaseDoc;
import u8c.vo.entity.enumtype.QueryOperator;
import u8c.vo.oip.chg.APIDocChgVO;
import u8c.vo.oip.outsys.APIOutSysVO;

/**
 * 查询抽象父类
 * 
 * @since U8C 2.1
 * @version 2018-3-16 上午10:46:08
 * @author luojw
 */
public abstract class AbstractQueryAction<E> implements IAction, IChgToJson<E>,
    IQueryForExcel {

  /** 数组最大长度 */
  public static final int MAX_ARRAY_SIZE = 10000;

  /** 当前页 */
  public static final String PAGE_NOW = "page_now";

  /** 每页大小 */
  public static final String PAGE_SIZE = "page_size";

  /** 总数量，分页查询时会赋值 */
  private int allcount = 0;

  /** 公司条件 */
  private APICondition corpCondition;

  /** 账簿条件 */
  private APICondition glbookCondition;

  /** 是否需要写中间文件 */
  private UFBoolean needMiddleFile;

  private int pageNow = 0;

  private int pageSize = 0;

  /** <注册查询条件的字段, 字段> */
  protected Map<String, String> registerField = new HashMap<>();

  /** <注册查询条件的字段, 操作符关键字> */
  protected Map<String, String> registerOperator = new HashMap<>();

  /** <字段, 对应档案的表名> */
  protected Map<String, String> tableNames = new HashMap<>();

  @Override
  public String changeToJson(E[] vos) throws BusinessException {
    return convertToJson(vos);
  }

  public abstract Object[][] convertJsonToArray(Map<String, String> fieldsData,
      String json) throws BusinessException;

  @Override
  public String doAction(String json, String tranType)
      throws BusinessException, ConfigException {
    // 根据单据的查询条件查询vos
    E[] vos = this.queryVOS(json);

    // 组装返回结果
    return this.buildeReturnInfo(vos);
  }

  /**
   * 根据sql查询
   * 
   * @param sql
   * @return
   * @throws BusinessException
   */
  public abstract E[] queryByWhere(String sql) throws BusinessException;

  @Override
  public Object[][] queryFieldsData(Map<String, String> fieldsData, String[] ids)
      throws BusinessException {
    // 根据主键查询vos
    E[] vos = this.queryByIDs(ids);
    // 将vos数组转换成json
    String json = this.changeToJson(vos);
    return convertJsonToArray(fieldsData, json);
  }

  /**
   * 根据查询条件，直接返回json
   * 
   * @param sql
   * @return
   * @throws BusinessException
   */
  public String queryJsonByCorpID(String corpID) throws BusinessException {
    String corpField = "pk_corp";
    String[][] fields = getFieldAndTableNames();
    if (fields != null) {
      for (String[] field : fields) {
        if (field[1].equals("bd_corp")) {
          corpField = field[0];
          break;
        }
      }
    }
    String sql = corpField + " = '" + corpID + "'";
    E[] vos = this.queryByWhere(sql);
    if (vos == null || vos.length == 0) {
      return null;
    }
    return this.convertToJson(vos);
  }

  /**
   * @param json
   * @return
   * @throws BusinessException
   * @throws ConfigException
   */
  public E[] queryVOS(String json) throws BusinessException, ConfigException {
    this.init();
    APICondition[] conditions = this.convertToConditions(json);
    // 转换外系统的值
    this.chgOutsysValue(conditions);
    // 根据参数存储sqlwhere条件的文件
    if (this.isNeedMiddleFile().booleanValue()) {
      String where = this.contructWhereFromConditions(conditions);
      this.writeMiddleFile(where, APIConst.QUERYSQLPATH);
    }
    E[] vos = this.query(conditions);
    return vos;
  }

  /**
   * @param vos
   * @return
   * @throws BusinessException
   * @throws ConfigException
   */
  private String buildeReturnInfo(E[] vos) throws BusinessException,
      ConfigException {
    JSONObject retJson = new JSONObject();
    // 没有数据分2种情况：1、确实没有数据；2、分页次数大导致没有数据
    if (vos == null || vos.length == 0) {
      retJson.put("allcount", this.allcount);
      retJson.put("retcount", 0);
    }
    else {
      // 总数量是0，说明不是分页查询，数量就是vos的长度
      if (this.allcount == 0) {
        this.allcount = vos.length;
      }
      // 如果长度超过100，手动分页
      // 定义分页子VO数组
      E[] subvos = vos;
      if (vos.length > AbstractQueryAction.MAX_ARRAY_SIZE) {
        subvos = Arrays.copyOfRange(vos, 0, AbstractQueryAction.MAX_ARRAY_SIZE);
      }
      retJson.put("allcount", this.allcount);
      retJson.put("retcount", vos.length);
      retJson.put(IChgToJson.DATAS, this.changeToJson(subvos));
    }
    return retJson.toString();
  }

  private void chgOutsysValue(APICondition[] conditions)
      throws BusinessException {
    APIOutSysVO outsys = APIOutSysUtil.getCurrentOutSysVO();
    if (outsys == null) {
      return;
    }
    // 公司主键
    String pk_corp = null;
    // 账簿主键
    String pk_glbook = null;
    if (corpCondition != null) {
      if (!corpCondition.getValue().equals("0001")) {
        pk_corp = getTranlateValue(corpCondition, IBdinfoConst.CORP);
      }
      else {
        pk_corp = "0001";
      }
    }
    if (glbookCondition != null) {
      pk_glbook = getTranlateValue(glbookCondition, IBdinfoConst.GLBOOK);
    }
    for (APICondition condition : conditions) {
      BaseDoc base = condition.getBaseDoc();
      if (base == null || base == BaseDoc.BD_CORP || base == BaseDoc.BD_GLBOOK) {
        continue;
      }
      BdinfoVO bdinfo =
          BdinfoManager.getBdInfoVObyDTableName(base.getTableName());
      String pk_bdinfo = bdinfo.getPk_bdinfo();
      APIDocChgVO docchgvo = APIOutSysUtil.getDocChgVOByBdinfo(pk_bdinfo);
      if (docchgvo != null) {
        // 组织主键，公司或账簿主键
        String orgid = pk_corp;
        if (QueryIDMappingUtil.isNeedAccountBook(pk_bdinfo)) {
          orgid = pk_glbook;
        }
        Object value = condition.getValue();
        Object[] values = condition.getValues();
        if (value != null) {
          String destValue =
              docchgvo.getDestValueBySrcValue((String) value, orgid);
          if (destValue != null) {
            condition.setValue(destValue);
          }
        }
        if (values != null) {
          for (int i = 0; i < values.length; i++) {
            String destValue =
                docchgvo.getDestValueBySrcValue((String) values[i], orgid);
            if (destValue != null) {
              values[i] = destValue;
            }
          }
        }
      }
    }

  }

  /**
   * 将json串转换成条件
   * 
   * @param json
   * @return
   * @throws BusinessException
   */
  @SuppressWarnings("unchecked")
  private APICondition[] convertToConditions(String json)
      throws BusinessException {
    Map<String, Object> map = JSONObject.fromObject(json);
    List<APICondition> conditions = new ArrayList<>();
    List<String> mustList =
        new ArrayList<>(Arrays.asList(this.getMustQueryFields()));
    for (Entry<String, Object> entry : map.entrySet()) {
      String key = entry.getKey();
      Object value = entry.getValue();
      if (value == null || value.toString().trim().equals("")) {
        continue;
      }
      if (AbstractQueryAction.PAGE_SIZE.equals(key)) {
        this.pageSize = this.getIntValue(value);
        continue;
      }
      else if (AbstractQueryAction.PAGE_NOW.equals(key)) {
        this.pageNow = this.getIntValue(value);
        continue;
      }
      // 特殊标识，直接过滤
      else if (InputDataVO.SYSTEM.equals(key)||InputDataVO.UNIQUEKEY.equals(key)) {
        continue;
      }
      // 找到数据库字段
      String field = this.registerField.get(key);
      if (field == null) {
        throw new BusinessException("未找到注册条件" + key + "的数据库映射字段");
      }
      APICondition condition = null;
      // 找到对应的基本档案
      String tableName = this.tableNames.get(field);
      BaseDoc baseDoc = null;
      if (tableName != null) {
        baseDoc = BaseDoc.getEnumByTableName(tableName);
      }
      if (value instanceof List) {
        List arrays = (List) value;
        if (arrays.isEmpty()) {
          continue;
        }
        if (baseDoc == BaseDoc.BD_CORP || baseDoc == BaseDoc.BD_GLBOOK) {
          throw new BusinessException(key + "字段对应的查询值类型不能传入数组，只能是单值");
        }
        condition = new APICondition(field, arrays.toArray(), baseDoc);
      }
      else {
        condition = new APICondition(field, value, baseDoc);
      }
      // 设置翻译类型
      if (tableName != null) {
        Integer tranType = APIOutSysUtil.getTranTypeByTableName(tableName);
        if (tranType != null) {
          condition.setTranType(tranType);
        }
        if (baseDoc == BaseDoc.BD_CORP) {
          corpCondition = condition;
        }
        if (baseDoc == BaseDoc.BD_GLBOOK) {
          glbookCondition = condition;
        }
      }
      String operator = this.registerOperator.get(key);
      if (operator != null) {
        QueryOperator queryOperator = QueryOperator.getEnumByOperator(operator);
        if (queryOperator == null) {
          throw new BusinessException("注册条件" + key + "的操作符不标准");
        }
        condition.setOperator(queryOperator);
      }
      condition.checkError();
      // 条件校验都没有问题，从必输字段中删除
      mustList.remove(key);
      conditions.add(condition);
    }
    if (!mustList.isEmpty()) {
      throw new BusinessException("下列查询条件必须输入：" + mustList);
    }
    return conditions.toArray(new APICondition[0]);
  }

  private GlobalSetVO getGlobalParameter() throws ConfigException {
    return NCLocator.getInstance().lookup(IAPIConfigFileService.class)
        .getGlobalParameter();
  }

  private int getIntValue(Object value) {
    if (value instanceof Integer) {
      return (Integer) value;
    }
    try {
      return Integer.parseInt((String) value);
    }
    catch (NumberFormatException e) {
      return 0;
    }
  }

  private String getTranlateValue(APICondition condition, String pk_bdinfo)
      throws BusinessException {
    String value = (String) condition.getValue();
    APIDocChgVO docchgvo = APIOutSysUtil.getDocChgVOByBdinfo(pk_bdinfo);
    if (docchgvo != null) {
      String destValue = docchgvo.getDestValueBySrcValue(value, null);
      if (destValue != null) {
        condition.setValue(destValue);
      }
    }
    return TranslateUtil.getBdPKByDocValue(pk_bdinfo, value);
  }

  private UFBoolean isNeedMiddleFile() throws ConfigException {
    if (null == this.needMiddleFile) {
      this.needMiddleFile = this.getGlobalParameter().getNeedMiddleFile();
    }
    return this.needMiddleFile;
  }

  /**
   * 分页查询
   * 
   * @param where
   * @param pageSize 每页大小
   * @param pageNow 当前页
   * @return
   * @throws BusinessException
   */
  private E[] pageQueryByWhere(String where) throws BusinessException {
    String sql = this.getQueryIDsSql(where);
    String countSql = "select count(1) from (";
    int index = sql.indexOf(" order by ");
    if (index == -1) {
      countSql += sql + ") a";
    }
    else {
      countSql += sql.substring(0, index) + ") a";
    }
    BaseDAO dao = new BaseDAO();
    this.allcount = dao.executeQuery(countSql, new QueryCountProcessor());
    if (this.allcount == 0) {
      return null;
    }
    String[] pageIDs = null;
    if (this.allcount < dao.getMaxRows()) {
      // 先查询出所有的主键
      String[] ids = dao.executeQuery(sql, new FieldArrayProcessor());
      if (this.allcount <= this.pageSize * (this.pageNow - 1)) {
        return null;
      }
      int from = this.pageSize * (this.pageNow - 1);
      int to =
          this.pageSize * this.pageNow < this.allcount ? this.pageSize
              * this.pageNow : this.allcount;
      pageIDs = Arrays.copyOfRange(ids, from, to);
    }
    else {
      int dbType = dao.getDBType();
      String pageQuerySql =
          SQLBuilderFactory.getInstance().createLimitSQLBuilder(dbType)
              .build(sql, this.pageNow, this.pageSize);
      pageIDs = dao.executeQuery(pageQuerySql, new FieldArrayProcessor());
    }
    return this.queryByIDs(pageIDs);
  }

  /**
   * @param vos
   * @throws BusinessException
   */
  private void writeMiddleFile(String data, String filePath)
      throws BusinessException {
    String[] date =
        new UFDateTime(System.currentTimeMillis()).toString().split(" ");
    String fileName =
        filePath + this.getBillMark() + "-" + date[0] + "-"
            + date[1].replaceAll(":", "-") + ".txt";
    try {
      new FileUtils().writeBytesToFile(data.getBytes("UTF-8"), fileName);
    }
    catch (IOException e) {
      throw new BusinessException(e.getMessage(), e.getCause());
    }
  }

  /**
   * 从条件参数中构造where条件
   * 
   * @param conditions
   * @return
   */
  protected String contructWhereFromConditions(APICondition[] conditions) {
    StringBuilder sql = new StringBuilder();
    for (APICondition condition : conditions) {
      sql.append(condition.getSql());
    }
    return APICondition.removeAnd(sql.toString());
  }

  /**
   * 将vo转换成json
   * 
   * @param vos
   * @return
   * @throws BusinessException
   */
  protected abstract String convertToJson(E[] vos) throws BusinessException;

  /**
   * 单据标识
   * 
   * @return String
   */
  protected abstract String getBillMark();

  /**
   * 添加数据字段及对应基本档案的表名
   * <p>
   * 入销售订单的公司，数据库字段是pk_corp，对应部门档案的表名是bd_corp
   * <p>
   * new String[][]{{"so_sale.pk_corp", "bd_corp"}}
   */
  protected abstract String[][] getFieldAndTableNames();

  /**
   * 获取必输查询字段
   * <ul>
   * <li>如销售订单的公司。
   * <li>new String[]{"corp", "date_begin", "date_end"}
   * </ul>
   * 
   * @return
   */
  protected abstract String[] getMustQueryFields();

  /**
   * 获取查询id的sql
   * 
   * @param where
   * @return
   * @throws BusinessException
   */
  protected abstract String getQueryIDsSql(String where);

  /**
   * 添加注册查询条件的字段、数据库子段及操作符
   * <ul>
   * <li>如销售订单的公司，前台注册的是corp，但是数据库字段是pk_corp，注意加单据的别名。
   * <li>加入操作符的情况，如日期。
   * <li>new String[][]{{"corp", "so_sale.pk_corp"},
   * {"date_begin","so_sale.dbilldate", ">="}};
   * </ul>
   */
  protected abstract String[][] getRegisterFieldAndFields();

  /** 初始化，加入字段 */
  protected void init() {
    for (String[] array : this.getRegisterFieldAndFields()) {
      this.registerField.put(array[0], array[1]);
      if (array.length > 2) {
        this.registerOperator.put(array[0], array[2]);
      }
    }
    for (String[] array : this.getFieldAndTableNames()) {
      this.tableNames.put(array[0], array[1]);
    }
  }

  /**
   * 通过条件查询
   * 
   * @param conditions
   * @return
   * @throws BusinessException
   */
  protected E[] query(APICondition[] conditions) throws BusinessException {
    String where = this.contructWhereFromConditions(conditions);
    if (this.pageSize > 0 && this.pageNow > 0) {
      return this.pageQueryByWhere(where);
    }
    return this.queryByWhere(where);
  }

  /**
   * 通过主键查询
   * 
   * @param IDs
   * @return
   * @throws BusinessException
   */
  protected abstract E[] queryByIDs(String[] ids) throws BusinessException;

}
