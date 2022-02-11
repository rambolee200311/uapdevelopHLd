package u8c.vo.reject.invoiceApply;
import java.util.List;

import u8c.vo.arrival.ArrivalData;
public class InvoiceDataRoot {
	private String invoiceBusinType;//发票业务类型 (1-保司经纪费,2-客户经纪费,3-咨询经纪费,4-转付保费到账,5-再保结算到站,6-理赔款结算,7-再保理赔,8-退保经纪费)*删除*
    private int totalNum;
    private List<InvoiceData> invoiceData;
	public String getInvoiceBusinType() {
		return invoiceBusinType;
	}
	public void setInvoiceBusinType(String invoiceBusinType) {
		this.invoiceBusinType = invoiceBusinType;
	}
	public int getTotalNum() {
		return totalNum;
	}
	public void setTotalNum(int totalNum) {
		this.totalNum = totalNum;
	}
	public List<InvoiceData> getInvoiceData() {
		return invoiceData;
	}
	public void setInvoiceData(List<InvoiceData> invoiceData) {
		this.invoiceData = invoiceData;
	}
    
}