package u8c.vo.applyInvoice;

public class ApplyInvoiceDetail {
	private int rowNo;//行号，1开始自增
	private String insurTypeCode;//险种编码
	private String insurTypeName;//险种名称
	private String currency;//币种：人民币-CNY,美元-USD…
	private Double inclusiveMoney;//含税金额：原币含税金额
	private Double curRate;//汇率：人民币-1，外币：当天汇率，建议业务员和客户确定汇率
	private Double inclusiveRMB;//含税金额：人民币含税金额
	private Double taxRate;//税率
	private Double taxRMB;//税额：人民币税额
	private String zyx1;//扩展字段1 
	private String zyx2;//扩展字段2
	private String zyx3;//扩展字段3
	private String zyx4;//扩展字段4
	private String zyx5;//扩展字段5
	public int getRowNo() {
		return rowNo;
	}
	public void setRowNo(int rowNo) {
		this.rowNo = rowNo;
	}
	public String getInsurTypeCode() {
		return insurTypeCode;
	}
	public void setInsurTypeCode(String insurTypeCode) {
		this.insurTypeCode = insurTypeCode;
	}
	public String getInsurTypeName() {
		return insurTypeName;
	}
	public void setInsurTypeName(String insurTypeName) {
		this.insurTypeName = insurTypeName;
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
	public Double getCurRate() {
		return curRate;
	}
	public void setCurRate(Double curRate) {
		this.curRate = curRate;
	}
	public Double getInclusiveRMB() {
		return inclusiveRMB;
	}
	public void setInclusiveRMB(Double inclusiveRMB) {
		this.inclusiveRMB = inclusiveRMB;
	}
	public Double getTaxRate() {
		return taxRate;
	}
	public void setTaxRate(Double taxRate) {
		this.taxRate = taxRate;
	}
	public Double getTaxRMB() {
		return taxRMB;
	}
	public void setTaxRMB(Double taxRMB) {
		this.taxRMB = taxRMB;
	}
	public String getZyx1() {
		return zyx1;
	}
	public void setZyx1(String zyx1) {
		this.zyx1 = zyx1;
	}
	public String getZyx2() {
		return zyx2;
	}
	public void setZyx2(String zyx2) {
		this.zyx2 = zyx2;
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
