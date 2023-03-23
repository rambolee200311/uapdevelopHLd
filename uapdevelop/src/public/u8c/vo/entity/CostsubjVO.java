package u8c.vo.entity;
import nc.vo.pub.SuperVO;
public class CostsubjVO  extends SuperVO {
	private static final long serialVersionUID = 1L;
	private String pk_costsubj=null;
	private String costcode;
	private String costname;
	public String getPk_costsubj() {
		return pk_costsubj;
	}
	public void setPk_costsubj(String pk_costsubj) {
		this.pk_costsubj = pk_costsubj;
	}
	public String getCostcode() {
		return costcode;
	}
	public void setCostcode(String costcode) {
		this.costcode = costcode;
	}
	public String getCostname() {
		return costname;
	}
	public void setCostname(String costname) {
		this.costname = costname;
	}
	@Override
	public String getPKFieldName() {
		// TODO Auto-generated method stub
		return "pk_costsubj";
	}
	@Override
	public String getParentPKFieldName() {
		// TODO Auto-generated method stub
		return null;
	}	
	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return "bd_costsubj";
	}
}
