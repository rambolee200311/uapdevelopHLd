package u8c.vo.entity;
import nc.vo.pub.SuperVO;
public class BanktypeVO extends SuperVO {
	private static final long serialVersionUID = 1L;
	private String pk_banktype=null;
	private String banktypecode;
	private String banktypename;
	public String getPk_banktype() {
		return pk_banktype;
	}
	public void setPk_banktype(String pk_banktype) {
		this.pk_banktype = pk_banktype;
	}
	public String getBanktypecode() {
		return banktypecode;
	}
	public void setBanktypecode(String banktypecode) {
		this.banktypecode = banktypecode;
	}
	public String getBanktypename() {
		return banktypename;
	}
	public void setBanktypename(String banktypename) {
		this.banktypename = banktypename;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	@Override
	public String getPKFieldName() {
		// TODO Auto-generated method stub
		return "pk_banktype";
	}
	@Override
	public String getParentPKFieldName() {
		// TODO Auto-generated method stub
		return null;
	}	
	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return "bd_banktype";
	}
}
