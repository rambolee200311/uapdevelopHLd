package u8c.vo.reject.invoiceApply;
public class InvoiceData {
	
	private String adviceNote;//通知书编号 开票通知书编号（唯一）
	private String adviceDate;//发票通知书驳回日期
	private String currency;//币种 人民币
	private double totleAmount;//开票申请金额（原币）
	private String operatorCode;//操作员
	private String busiType;//通知书类型 业务类型 (业务类型:1-经纪费 2-再保经纪费 3-咨询费 99-其他）
	private String value1;
	private String value2;
	private String value3;
	private String value4;
	private String value5;
	public String getAdviceNote() {
		return adviceNote;
	}
	public void setAdviceNote(String adviceNote) {
		this.adviceNote = adviceNote;
	}
	public String getAdviceDate() {
		return adviceDate;
	}
	public void setAdviceDate(String adviceDate) {
		this.adviceDate = adviceDate;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public double getTotleAmount() {
		return totleAmount;
	}
	public void setTotleAmount(double totleAmount) {
		this.totleAmount = totleAmount;
	}
	public String getOperatorCode() {
		return operatorCode;
	}
	public void setOperatorCode(String operatorCode) {
		this.operatorCode = operatorCode;
	}
	public String getBusiType() {
		return busiType;
	}
	public void setBusiType(String busiType) {
		this.busiType = busiType;
	}
	public String getValue1() {
		return value1;
	}
	public void setValue1(String value1) {
		this.value1 = value1;
	}
	public String getValue2() {
		return value2;
	}
	public void setValue2(String value2) {
		this.value2 = value2;
	}
	public String getValue3() {
		return value3;
	}
	public void setValue3(String value3) {
		this.value3 = value3;
	}
	public String getValue4() {
		return value4;
	}
	public void setValue4(String value4) {
		this.value4 = value4;
	}
	public String getValue5() {
		return value5;
	}
	public void setValue5(String value5) {
		this.value5 = value5;
	}
	
}