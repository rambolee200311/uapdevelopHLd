package u8c.vo.reject.paymentApply;
public class TransferData {
	private String transferApplyNo; //转付申请号 业务系统的申请单号
	private String transferApplyDate;//转付申请驳回日期
	private String transferBusinType;//1-转付保费,2-再保转付保费,3-理赔转付保费,4-再保理赔转付保费
	private String currency;//币种 币种编码
	private double transferAmount;//转付金额：原币金额
	private String operatorCode;//
	private String value1;
	private String value2;
	private String value3;
	private String value4;
	private String value5;
	public String getTransferApplyNo() {
		return transferApplyNo;
	}
	public void setTransferApplyNo(String transferApplyNo) {
		this.transferApplyNo = transferApplyNo;
	}
	public String getTransferApplyDate() {
		return transferApplyDate;
	}
	public void setTransferApplyDate(String transferApplyDate) {
		this.transferApplyDate = transferApplyDate;
	}
	public String getTransferBusinType() {
		return transferBusinType;
	}
	public void setTransferBusinType(String transferBusinType) {
		this.transferBusinType = transferBusinType;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public double getTransferAmount() {
		return transferAmount;
	}
	public void setTransferAmount(double transferAmount) {
		this.transferAmount = transferAmount;
	}
	public String getOperatorCode() {
		return operatorCode;
	}
	public void setOperatorCode(String operatorCode) {
		this.operatorCode = operatorCode;
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