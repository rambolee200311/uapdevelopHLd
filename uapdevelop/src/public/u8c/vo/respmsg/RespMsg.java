package u8c.vo.respmsg;

public class RespMsg {
		private Message message;
	    private Response response;
	    public void setMessage(Message message) {
	         this.message = message;
	     }
	     public Message getMessage() {
	         return message;
	     }
	
	    public void setResponse(Response response) {
	         this.response = response;
	     }
	     public Response getResponse() {
	         return response;
	     }
}
