package u8c.vo.cusmandoc;
//业务系统客商档案请求
import java.util.List;

public class CusRequest {
		private String comCode;
	    private String comName;
	    private String custCode;
	    private String custName;
	    private List<CusBankRequest > bank ;
	    private String conAddr;
	    private String phone1;
	    private String phone2;
	    private String phone3;
	    private String zyx1;
	    private String zyx2;
	    private String zyx3;
	    private String zyx4;
	    private String zyx5;
	    public void setComCode(String comCode) {
	         this.comCode = comCode;
	     }
	     public String getComCode() {
	         return comCode;
	     }
	
	    public void setComName(String comName) {
	         this.comName = comName;
	     }
	     public String getComName() {
	         return comName;
	     }
	
	    public void setCustCode(String custCode) {
	         this.custCode = custCode;
	     }
	     public String getCustCode() {
	         return custCode;
	     }
	
	    public void setCustName(String custName) {
	         this.custName = custName;
	     }
	     public String getCustName() {
	         return custName;
	     }
	
	    public void setBank (List<CusBankRequest > bank ) {
	         this.bank  = bank ;
	     }
	     public List<CusBankRequest > getBank () {
	         return bank ;
	     }
	
	    public void setConAddr(String conAddr) {
	         this.conAddr = conAddr;
	     }
	     public String getConAddr() {
	         return conAddr;
	     }
	
	    public void setPhone1(String phone1) {
	         this.phone1 = phone1;
	     }
	     public String getPhone1() {
	         return phone1;
	     }
	
	    public String getPhone2() {
			return phone2;
		}
		public void setPhone2(String phone2) {
			this.phone2 = phone2;
		}		
		public String getPhone3() {
			return phone3;
		}
		public void setPhone3(String phone3) {
			this.phone3 = phone3;
		}
		public void setZyx1(String zyx1) {
	         this.zyx1 = zyx1;
	     }
	     public String getZyx1() {
	         return zyx1;
	     }
	
	    public void setZyx2(String zyx2) {
	         this.zyx2 = zyx2;
	     }
	     public String getZyx2() {
	         return zyx2;
	     }
		public String getZyx3() {
			return zyx3;
		}
		public void setZyx3(String zyx3) {
			this.zyx3 = zyx3;
		}
		public String getZyx4() {
			return zyx4;
		}
		public void setZyx4(String zyx4) {
			this.zyx4 = zyx4;
		}
		public String getZyx5() {
			return zyx5;
		}
		public void setZyx5(String zyx5) {
			this.zyx5 = zyx5;
		}
     
}
