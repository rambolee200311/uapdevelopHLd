package u8c.vo.invoice;

public class InvoiceHead {
	private String userName;
    private String passWord;
    private String redKey;
    private String requestDate;
    private String seqNO;
    public void setUserName(String userName) {
         this.userName = userName;
     }
     public String getUserName() {
         return userName;
     }

    public void setPassWord(String passWord) {
         this.passWord = passWord;
     }
     public String getPassWord() {
         return passWord;
     }

    public void setRedKey(String redKey) {
         this.redKey = redKey;
     }
     public String getRedKey() {
         return redKey;
     }

    public void setRequestDate(String requestDate) {
         this.requestDate = requestDate;
     }
     public String getRequestDate() {
         return requestDate;
     }

    public void setSeqNO(String seqNO) {
         this.seqNO = seqNO;
     }
     public String getSeqNO() {
         return seqNO;
     }
}
