package u8c.vo.comfirmArrival;

public class ConfirmArrivalBody {
	private String billID;//到账确认单ID，唯一
    private String billDate;//到账确认日期
    private String arrivalRegiCode;//U8C系统银行流水号
    private String payerCode;//支付方编码
    private String payerName;//支付方名称
    private String operatorCode;//操作员
    private String comCode;//公司编码
    private String comName;//公司名称
    private String zyx1;//扩展字段1，到款确认备注
    private String zyx2;
    private String zyx3;
    private String zyx4;
    private String zyx5;
    private String zyx6;
    private String zyx7;
    private String zyx8;
    private String zyx9;
    private String zyx10;
    private int busiType;//确认类型    1-经纪费，    2-再保经纪费    3-保费    4-再保款    5-理赔款    99-其他
    private String apCode;//应付方编码
    private String apName;//应付方名称
    private String currency;//币种：人民币-CNY,美元-USD
    private Double arrivalAmount;//应付金额：原币金额
    private Double curRate;//汇率：人民币-1
    private Double arrivalRMB;//应付金额：人民币金额
    private String dpName;//部门名称
    private String smName;//业务员名称
    private String pmName;//项目名称
	public String getBillID() {
		return billID;
	}
	public void setBillID(String billID) {
		this.billID = billID;
	}
	public String getBillDate() {
		return billDate;
	}
	public void setBillDate(String billDate) {
		this.billDate = billDate;
	}
	public String getArrivalRegiCode() {
		return arrivalRegiCode;
	}
	public void setArrivalRegiCode(String arrivalRegiCode) {
		this.arrivalRegiCode = arrivalRegiCode;
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
	public String getZyx7() {
		return zyx7;
	}
	public void setZyx7(String zyx7) {
		this.zyx7 = zyx7;
	}
	public String getZyx8() {
		return zyx8;
	}
	public void setZyx8(String zyx8) {
		this.zyx8 = zyx8;
	}
	public String getZyx9() {
		return zyx9;
	}
	public void setZyx9(String zyx9) {
		this.zyx9 = zyx9;
	}
	public String getZyx10() {
		return zyx10;
	}
	public void setZyx10(String zyx10) {
		this.zyx10 = zyx10;
	}
	public int getBusiType() {
		return busiType;
	}
	public void setBusiType(int busiType) {
		this.busiType = busiType;
	}
	public String getApCode() {
		return apCode;
	}
	public void setApCode(String apCode) {
		this.apCode = apCode;
	}
	public String getApName() {
		return apName;
	}
	public void setApName(String apName) {
		this.apName = apName;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public Double getArrivalAmount() {
		return arrivalAmount;
	}
	public void setArrivalAmount(Double arrivalAmount) {
		this.arrivalAmount = arrivalAmount;
	}
	public Double getCurRate() {
		return curRate;
	}
	public void setCurRate(Double curRate) {
		this.curRate = curRate;
	}
	public Double getArrivalRMB() {
		return arrivalRMB;
	}
	public void setArrivalRMB(Double arrivalRMB) {
		this.arrivalRMB = arrivalRMB;
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
    
}
