package u8c.vo.invoice;

public class InvoiceData {
	private String invoiceSeqNo;//发票流水号 U8C发票号
	private String invoiceNo;//发票号  金税发票号
	private String adviceNote;//通知书编号 开票通知书编号（唯一）
	private int adviceType;//通知书类型 业务类型 (1-保司经纪费,2-客户经纪费,3-咨询经纪费,4-转付保费到账,5-再保结算到站,6-理赔款结算,7-再保理赔,8-退保经纪费）
	private String invoiceType;//发票类型 （普通发票、增值税）
	private String currency;//币种 人民币
	private Double inclusiveMoney;//发票含税金额 金额
	private Double taxRate;//含税税率 整数（6，13）
	private Double inclusive;//含税税率 整数（6，13）
	private Double specialMoney; //增值税金额 税额
	private Double noInclusiveMoney;//不含税金额 金额
	private String payerCode;//支付方编号 u8c客商编号
	private String payerName;//支付方名称 u8c客商名称
	private String vatPayerId;//纳税人识别号 u8c税号 
	private String invoiceTime; //开票日期 发票日期
	private int isVatInvoice;//是否红票 是否红票
	private String reverseInvoiceNo; //冲销发票号 （红票的原发票号）
	private String operatorCode; //经办人 操作员
	private String comCode;//经办人归属机构 公司编码
	private String comName;//经办人归属机构 公司名称
	private String remark; //备注 发票备注	
	
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
