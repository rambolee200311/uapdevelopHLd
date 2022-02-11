package u8c.vo.applyPay;

public class ApplyPayBody {
	private String transferApplyNo;//转付申请号，唯一
	private String transferApplyDate;//转付申请日期
	private int transferBusinType;//1-转付保费,2-再保转付保费,3-理赔转付保费,4-再保理赔转付保费
	private String insuranceCode;//对方单位编码
	private String insuranceName;//对方单位名称
	private String currency;//币种：人民币-CNY,美元-USD…
	private Double transferAmount;//转付金额：原币金额
	private Double curRate;//汇率：人民币-1，外币：当天汇率
	private Double transferRMB;//转付金额：人民币金额
	private String operatorCode;
	private String comCode;//公司编码
	private String comName;//公司名称
	private String zyx1;//扩展字段1，付款申请单备注
	private String zyx2;
	private String zyx3;
	private String zyx4;
	private String zyx5;
	private String zyx6;
	private String dpName;//部门名称
	private String smName;//业务员名称
	private String pmName;
	private String acCode;//银行账号
	private String bkName;//开户行名
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
	public int getTransferBusinType() {
		return transferBusinType;
	}
	public void setTransferBusinType(int transferBusinType) {
		this.transferBusinType = transferBusinType;
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
	public Double getCurRate() {
		return curRate;
	}
	public void setCurRate(Double curRate) {
		this.curRate = curRate;
	}
	public Double getTransferRMB() {
		return transferRMB;
	}
	public void setTransferRMB(Double transferRMB) {
		this.transferRMB = transferRMB;
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
	public String getComName() {
		return comName;
	}
	public void setComName(String comName) {
		this.comName = comName;
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
	public String getZyx6() {
		return zyx6;
	}
	public void setZyx6(String zyx6) {
		this.zyx6 = zyx6;
	}
	public String getDpName() {
		return dpName;
	}
	public void setDpName(String dpName) {
		this.dpName = dpName;
	}
	public String getSmName() {
		return smName;
	}
	public void setSmName(String smName) {
		this.smName = smName;
	}
	public String getPmName() {
		return pmName;
	}
	public void setPmName(String pmName) {
		this.pmName = pmName;
	}
	public String getAcCode() {
		return acCode;
	}
	public void setAcCode(String acCode) {
		this.acCode = acCode;
	}
	public String getBkName() {
		return bkName;
	}
	public void setBkName(String bkName) {
		this.bkName = bkName;
	}
	
}
