package u8c.vo.cusmandoc;
//业务系统客商银行请求
public class CusBankRequest {
	 	private String bkName;
	    private String bkCode;
	    private String acCode;
	    public void setBkName(String bkName) {
	         this.bkName = bkName;
	     }
	     public String getBkName() {
	         return bkName;
	     }

	    public void setBkCode(String bkCode) {
	         this.bkCode = bkCode;
	     }
	     public String getBkCode() {
	         return bkCode;
	     }

	    public void setAcCode(String acCode) {
	         this.acCode = acCode;
	     }
	     public String getAcCode() {
	         return acCode;
	     }
}
