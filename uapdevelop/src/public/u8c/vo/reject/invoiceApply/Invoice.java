package u8c.vo.reject.invoiceApply;

public class Invoice {
	public InvoiceHead header;
	public InvoiceMessage  message;
	public InvoiceHead getHeader() {
		return header;
	}
	public void setHeader(InvoiceHead header) {
		this.header = header;
	}
	public InvoiceMessage getMessage() {
		return message;
	}
	public void setMessage(InvoiceMessage message) {
		this.message = message;
	}
	
}
