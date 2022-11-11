package u8c.vo.entity;
import nc.vo.pub.SuperVO;
public class BankDocVO extends SuperVO{
	private static final long serialVersionUID = 1L;
    private String pk_bankdoc=null;
    private String pk_corp;
    private String unitcode;
    private String unitname;
    private String bankdoccode;
    private String bankdocname;
    private String pk_banktype;
    private boolean sealflag;
    private String shortname;
    private String pcombinenum;
    private String pcombinename;
    private boolean iscustbank;
    public void setPk_bankdoc(String pk_bankdoc) {
         this.pk_bankdoc = pk_bankdoc;
     }
     public String getPk_bankdoc() {
         return pk_bankdoc;
     }

    public void setPk_corp(String pk_corp) {
         this.pk_corp = pk_corp;
     }
     public String getPk_corp() {
         return pk_corp;
     }

    public void setUnitcode(String unitcode) {
         this.unitcode = unitcode;
     }
     public String getUnitcode() {
         return unitcode;
     }

    public void setUnitname(String unitname) {
         this.unitname = unitname;
     }
     public String getUnitname() {
         return unitname;
     }

    public void setBankdoccode(String bankdoccode) {
         this.bankdoccode = bankdoccode;
     }
     public String getBankdoccode() {
         return bankdoccode;
     }

    public void setBankdocname(String bankdocname) {
         this.bankdocname = bankdocname;
     }
     public String getBankdocname() {
         return bankdocname;
     }

    public void setPk_banktype(String pk_banktype) {
         this.pk_banktype = pk_banktype;
     }
     public String getPk_banktype() {
         return pk_banktype;
     }

    public void setSealflag(boolean sealflag) {
         this.sealflag = sealflag;
     }
     public boolean getSealflag() {
         return sealflag;
     }

    public void setShortname(String shortname) {
         this.shortname = shortname;
     }
     public String getShortname() {
         return shortname;
     }

    public void setPcombinenum(String pcombinenum) {
         this.pcombinenum = pcombinenum;
     }
     public String getPcombinenum() {
         return pcombinenum;
     }

    public void setPcombinename(String pcombinename) {
         this.pcombinename = pcombinename;
     }
     public String getPcombinename() {
         return pcombinename;
     }

    public void setIscustbank(boolean iscustbank) {
         this.iscustbank = iscustbank;
     }
     public boolean getIscustbank() {
         return iscustbank;
     }
     @Override
 	public String getPKFieldName() {
 		// TODO Auto-generated method stub
 		return "pk_bankdoc";
 	}
 	@Override
 	public String getParentPKFieldName() {
 		// TODO Auto-generated method stub
 		return null;
 	}
 	@Override
 	public String getTableName() {
 		// TODO Auto-generated method stub
 		return "bd_bankdoc";
 	}
}