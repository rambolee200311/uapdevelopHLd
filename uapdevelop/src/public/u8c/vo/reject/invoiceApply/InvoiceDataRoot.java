package u8c.vo.reject.invoiceApply;
import java.util.List;

import u8c.vo.arrival.ArrivalData;
public class InvoiceDataRoot {
	private String invoiceBusinType;//��Ʊҵ������ (1-��˾���ͷ�,2-�ͻ����ͷ�,3-��ѯ���ͷ�,4-ת�����ѵ���,5-�ٱ����㵽վ,6-��������,7-�ٱ�����,8-�˱����ͷ�)*ɾ��*
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