package u8c.vo.entity;
import nc.vo.pub.SuperVO;
public class PcombineinfoVO extends SuperVO{
	private static final long serialVersionUID = 1L;
	private String pcombinenum;
	private String pcombinename;
	private String pk_pcombineinfo=null;
	private String pk_banktype;
	public String getPcombinenum() {
		return pcombinenum;
	}
	public void setPcombinenum(String pcombinenum) {
		this.pcombinenum = pcombinenum;
	}
	public String getPcombinename() {
		return pcombinename;
	}
	public void setPcombinename(String pcombinename) {
		this.pcombinename = pcombinename;
	}
	public String getPk_pcombineinfo() {
		return pk_pcombineinfo;
	}
	public void setPk_pcombineinfo(String pk_pcombineinfo) {
		this.pk_pcombineinfo = pk_pcombineinfo;
	}
	public String getPk_banktype() {
		return pk_banktype;
	}
	public void setPk_banktype(String pk_banktype) {
		this.pk_banktype = pk_banktype;
	}
	@Override
	public String getPKFieldName() {
		// TODO Auto-generated method stub
		return "pk_pcombineinfo";
	}
	@Override
	public String getParentPKFieldName() {
		// TODO Auto-generated method stub
		return null;
	}	
	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return "bd_pcombineinfo";
	}
}
