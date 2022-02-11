package u8c.vo.reject.paymentApply;

public class Transfer {
	private TransferHeader header;
	private TransferMessage message;
	public TransferHeader getHeader() {
		return header;
	}
	public void setHeader(TransferHeader header) {
		this.header = header;
	}
	public TransferMessage getMessage() {
		return message;
	}
	public void setMessage(TransferMessage message) {
		this.message = message;
	}
	
}