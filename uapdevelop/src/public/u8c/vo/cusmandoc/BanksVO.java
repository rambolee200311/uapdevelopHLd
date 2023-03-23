package u8c.vo.cusmandoc;

public class BanksVO {
	private String pk_accbank;
    private String pk_bankdoc;
    private String pk_banktype;
    private String account;
    private String accountname;
    private String defflag;
    public void setPk_accbank(String pk_accbank) {
         this.pk_accbank = pk_accbank;
     }
     public String getPk_accbank() {
         return pk_accbank;
     }

    public void setPk_bankdoc(String pk_bankdoc) {
         this.pk_bankdoc = pk_bankdoc;
     }
     public String getPk_bankdoc() {
         return pk_bankdoc;
     }

    public void setPk_banktype(String pk_banktype) {
         this.pk_banktype = pk_banktype;
     }
     public String getPk_banktype() {
         return pk_banktype;
     }

    public void setAccount(String account) {
         this.account = account;
     }
     public String getAccount() {
         return account;
     }

    public void setAccountname(String accountname) {
         this.accountname = accountname;
     }
     public String getAccountname() {
         return accountname;
     }

    public void setDefflag(String defflag) {
         this.defflag = defflag;
     }
     public String getDefflag() {
         return defflag;
     }
}
