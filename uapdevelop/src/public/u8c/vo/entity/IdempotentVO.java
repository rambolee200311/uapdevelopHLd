package u8c.vo.entity;

import nc.vo.pub.SuperVO;

public class IdempotentVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String idempotentid = null;
	
	public String getIdempotentid() {
		return idempotentid;
	}

	public void setIdempotentid(String idempotentid) {
		this.idempotentid = idempotentid;
	}

	public String getUniquekey() {
		return uniquekey;
	}

	public void setUniquekey(String uniquekey) {
		this.uniquekey = uniquekey;
	}

	public String getBilltype() {
		return billtype;
	}

	public void setBilltype(String billtype) {
		this.billtype = billtype;
	}

	private String uniquekey = null;
	
	private String billtype = null;
	

	@Override
	public String getParentPKFieldName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPKFieldName() {
		// TODO Auto-generated method stub
		return "idempotentid";
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return "api_idempotent";
	}

}
