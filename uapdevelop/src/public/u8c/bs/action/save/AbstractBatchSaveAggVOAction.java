package u8c.bs.action.save;

import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import u8c.bs.convert.bp.excel.ExcelAggVOConvertBP;
import u8c.bs.convert.bp.json.JSONAggVOConvertBP;
import u8c.bs.translate.bp.AggVOTranBP;
import u8c.bs.utils.APILock;

/**
 * @功能描述：AggregatedValueObject类型批量保存抽象类
 * @since u8c2.1
 * @version 2018-3-15 下午8:52:28
 * @author xuxq3
 * @param <E>
 */

public abstract class AbstractBatchSaveAggVOAction<E extends AggregatedValueObject>
    extends AbstractBatchSaveAction<E> {

  /** 每次保存动作的锁主键 */
  private String lockid;

  @Override
  public E[] businessProcess(E[] vos) throws BusinessException {
    try {
      return super.businessProcess(vos);
    }
    finally {
      // 如果锁主键为空，说明没有加过锁
      if (this.lockid != null) {
    	  APILock.getInstance(this.getBillMark()).release(this.lockid);		
      }
    }
  }
  
  protected String getLockID() {
    if (this.lockid == null) {
      this.lockid = APILock.getInstance(this.getBillMark()).getLockID();
    }
    return this.lockid;
  }

  @Override
  protected E[] convert(String json) throws BusinessException {
    return new JSONAggVOConvertBP<E>(this.getBillMark()).convert(json);
  }

  @Override
  protected E[] convert(String[][] datas) throws BusinessException {
    return new ExcelAggVOConvertBP<E>(this.getBillMark()).convert(datas);
  }

  @Override
  protected void translate(E[] vos) throws BusinessException {
    new AggVOTranBP().translate(vos, this.getBillMark());
  }

  @Override
  protected String getJsonStrFromVOS(E[] vos) throws BusinessException {
    return new AggVOTranBP().translateToJson(vos, this.getBillMark());
  }

}
