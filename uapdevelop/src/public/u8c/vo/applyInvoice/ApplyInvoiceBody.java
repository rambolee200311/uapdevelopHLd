package u8c.vo.applyInvoice;
import java.util.List;
public class ApplyInvoiceBody {
	private String adviceNote;//发票通知书ID，唯一
	private String adviceDate;//发票通知书日期
	private String payerCode;//收票单位编码
	private String payerName;//收票单位名称
	private String operatorCode;//操作员
	private String comCode;//公司编码
	private String comName;//公司名称
	private int busiType;//业务类型:1-经纪费 2-再保经纪费 3-咨询费 99-其他
	private String dpName;//部门名称
	private String smName;//业务员名称
	private String pmName;//项目名称
	private String zyx1;//扩展字段1，开票申请单备注
	private String zyx2;//扩展字段2
	private String zyx3;//扩展字段3
	private String zyx4;//扩展字段4
	private String zyx5;//扩展字段5
	private List<ApplyInvoiceDetail> detail;
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
	public int getBusiType() {
		return busiType;
	}
	public void setBusiType(int busiType) {
		this.busiType = busiType;
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
	public List<ApplyInvoiceDetail> getDetail() {
		return detail;
	}
	public void setDetail(List<ApplyInvoiceDetail> detail) {
		this.detail = detail;
	}
	
}
