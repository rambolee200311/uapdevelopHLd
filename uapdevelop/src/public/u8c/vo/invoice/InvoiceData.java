package u8c.vo.invoice;

public class InvoiceData {
	private String invoiceSeqNo;//��Ʊ��ˮ�� U8C��Ʊ��
	private String invoiceNo;//��Ʊ��  ��˰��Ʊ��
	private String adviceNote;//֪ͨ���� ��Ʊ֪ͨ���ţ�Ψһ��
	private int adviceType;//֪ͨ������ ҵ������ (1-��˾���ͷ�,2-�ͻ����ͷ�,3-��ѯ���ͷ�,4-ת�����ѵ���,5-�ٱ����㵽վ,6-��������,7-�ٱ�����,8-�˱����ͷѣ�
	private String invoiceType;//��Ʊ���� ����ͨ��Ʊ����ֵ˰��
	private String currency;//���� �����
	private Double inclusiveMoney;//��Ʊ��˰��� ���
	private Double taxRate;//��˰˰�� ������6��13��
	private Double inclusive;//��˰˰�� ������6��13��
	private Double specialMoney; //��ֵ˰��� ˰��
	private Double noInclusiveMoney;//����˰��� ���
	private String payerCode;//֧������� u8c���̱��
	private String payerName;//֧�������� u8c��������
	private String vatPayerId;//��˰��ʶ��� u8c˰�� 
	private String invoiceTime; //��Ʊ���� ��Ʊ����
	private int isVatInvoice;//�Ƿ��Ʊ �Ƿ��Ʊ
	private String reverseInvoiceNo; //������Ʊ�� ����Ʊ��ԭ��Ʊ�ţ�
	private String operatorCode; //������ ����Ա
	private String comCode;//�����˹������� ��˾����
	private String comName;//�����˹������� ��˾����
	private String remark; //��ע ��Ʊ��ע	
	
	public Double getInclusive() {
		return inclusive;
	}
	public void setInclusive(Double inclusive) {
		this.inclusive = inclusive;
	}
	public String getComName() {
		return comName;
	}
	public void setComName(String comName) {
		this.comName = comName;
	}
	public String getInvoiceSeqNo() {
		return invoiceSeqNo;
	}
	public void setInvoiceSeqNo(String invoiceSeqNo) {
		this.invoiceSeqNo = invoiceSeqNo;
	}
	public String getInvoiceNo() {
		return invoiceNo;
	}
	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}
	public String getAdviceNote() {
		return adviceNote;
	}
	public void setAdviceNote(String adviceNote) {
		this.adviceNote = adviceNote;
	}
	public int getAdviceType() {
		return adviceType;
	}
	public void setAdviceType(int adviceType) {
		this.adviceType = adviceType;
	}
	public String getInvoiceType() {
		return invoiceType;
	}
	public void setInvoiceType(String invoiceType) {
		this.invoiceType = invoiceType;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public Double getInclusiveMoney() {
		return inclusiveMoney;
	}
	public void setInclusiveMoney(Double inclusiveMoney) {
		this.inclusiveMoney = inclusiveMoney;
	}
	public Double getTaxRate() {
		return taxRate;
	}
	public void setTaxRate(Double taxRate) {
		this.taxRate = taxRate;
	}
	public Double getSpecialMoney() {
		return specialMoney;
	}
	public void setSpecialMoney(Double specialMoney) {
		this.specialMoney = specialMoney;
	}
	public Double getNoInclusiveMoney() {
		return noInclusiveMoney;
	}
	public void setNoInclusiveMoney(Double noInclusiveMoney) {
		this.noInclusiveMoney = noInclusiveMoney;
	}
	public String getPayerCode() {
		return payerCode;
	}
	public void setPayerCode(String payerCode) {
		this.payerCode = payerCode;
	}
	public String getPayerName() {
		return payerName;
	}
	public void setPayerName(String payerName) {
		this.payerName = payerName;
	}
	public String getVatPayerId() {
		return vatPayerId;
	}
	public void setVatPayerId(String vatPayerId) {
		this.vatPayerId = vatPayerId;
	}
	public String getInvoiceTime() {
		return invoiceTime;
	}
	public void setInvoiceTime(String invoiceTime) {
		this.invoiceTime = invoiceTime;
	}
	public int getIsVatInvoice() {
		return isVatInvoice;
	}
	public void setIsVatInvoice(int isVatInvoice) {
		this.isVatInvoice = isVatInvoice;
	}
	public String getReverseInvoiceNo() {
		return reverseInvoiceNo;
	}
	public void setReverseInvoiceNo(String reverseInvoiceNo) {
		this.reverseInvoiceNo = reverseInvoiceNo;
	}
	public String getOperatorCode() {
		return operatorCode;
	}
	public void setOperatorCode(String operatorCode) {
		this.operatorCode = operatorCode;
	}
	public String getComCode() {
		return comCode;
	}
	public void setComCode(String comCode) {
		this.comCode = comCode;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
}
