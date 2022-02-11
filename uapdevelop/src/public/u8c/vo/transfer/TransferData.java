package u8c.vo.transfer;

public class TransferData {
	private String transferApplyNo; //转付申请号 业务系统的申请单号
	private String transferCode;//划拨单号 u8c单据号
	private String paymentReviewNo;//支付审批单号 业务系统的支付审批单号
	private String insuranceCode;//保司编码 u8c客商编码
	private String insuranceName;//保司名称 u8c客商名称
	private String currency;//币种 币种编码
	private Double transferAmount;//转付金额  原币金额
	private String acceptDateTime;//财务受理时间 付款日期
	private String transferPersonCode;//划拨人 操作员
	private String transferComCode; //划拨机构 公司编码
	private String transferDate;//划拨时间*付款日期*
	private String remark;//备注  付款单备注
	private String transferState;//划拨状态  （成功、失败、原因不明）
	public String getTransferApplyNo() {
		return transferApplyNo;
	}
	public void setTransferApplyNo(String transferApplyNo) {
		this.transferApplyNo = transferApplyNo;
	}
	public String getTransferCode() {
		return transferCode;
	}
	public void setTransferCode(String transferCode) {
		this.transferCode = transferCode;
	}
	public String getPaymentReviewNo() {
		return paymentReviewNo;
	}
	public void setPaymentReviewNo(String paymentReviewNo) {
		this.paymentReviewNo = paymentReviewNo;
	}
	public String getInsuranceCode() {
		return insuranceCode;
	}
	public void setInsuranceCode(String insuranceCode) {
		this.insuranceCode = insuranceCode;
	}
	public String getInsuranceName() {
		return insuranceName;
	}
	public void setInsuranceName(String insuranceName) {
		this.insuranceName = insuranceName;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public Double getTransferAmount() {
		return transferAmount;
	}
	public void setTransferAmount(Double transferAmount) {
		this.transferAmount = transferAmount;
	}
	public String getAcceptDateTime() {
		return acceptDateTime;
	}
	public void setAcceptDateTime(String acceptDateTime) {
		this.acceptDateTime = acceptDateTime;
	}
	public String getTransferPersonCode() {
		return transferPersonCode;
	}
	public void setTransferPersonCode(String transferPersonCode) {
		this.transferPersonCode = transferPersonCode;
	}
	public String getTransferComCode() {
		return transferComCode;
	}
	public void setTransferComCode(String transferComCode) {
		this.transferComCode = transferComCode;
	}
	public String getTransferDate() {
		return transferDate;
	}
	public void setTransferDate(String transferDate) {
		this.transferDate = transferDate;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getTransferState() {
		return transferState;
	}
	public void setTransferState(String transferState) {
		this.transferState = transferState;
	}
	
}
