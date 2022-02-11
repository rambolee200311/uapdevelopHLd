package u8c.vo.entity;
import nc.vo.pub.SuperVO;
public class CorpVO extends SuperVO {
	private static final long serialVersionUID = 1L;
	private String unitcode;
	private String unitname;
	
	public String getUnitcode() {
		return unitcode;
	}
	public void setUnitcode(String unitcode) {
		this.unitcode = unitcode;
	}
	public String getUnitname() {
		return unitname;
	}
	public void setUnitname(String unitname) {
		this.unitname = unitname;
	}
	@Override
	public String getPKFieldName() {
		// TODO Auto-generated method stub
		return "pk_corp";
	}
	@Override
	public String getParentPKFieldName() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return "bd_corp";
	}
}
