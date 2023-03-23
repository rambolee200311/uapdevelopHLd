package u8c.vo.cusmandoc;

public class CusResponse {
	 	private String status;
	    private String custCode ;
	    private String u8cCode ;
	    private String comCode;
	    private String desc;
	    public void setStatus(String status) {
	         this.status = status;
	     }
	     public String getStatus() {
	         return status;
	     }

	    public void setCustCode (String custCode ) {
	         this.custCode  = custCode ;
	     }
	     public String getCustCode () {
	         return custCode ;
	     }

	    public void setU8cCode (String u8cCode ) {
	         this.u8cCode  = u8cCode ;
	     }
	     public String getU8cCode () {
	         return u8cCode ;
	     }

	    public void setDesc(String desc) {
	         this.desc = desc;
	     }
	     public String getDesc() {
	         return desc;
	     }
		public String getComCode() {
			return comCode;
		}
		public void setComCode(String comCode) {
			this.comCode = comCode;
		}
	     
}
