package u8c.vo.applyInvoice;
import java.util.List;
public class ApplyInvoiceBody {
	private String adviceNote;//��Ʊ֪ͨ��ID��Ψһ
	private String adviceDate;//��Ʊ֪ͨ������
	private String payerCode;//��Ʊ��λ����
	private String payerName;//��Ʊ��λ����
	private String operatorCode;//����Ա
	private String comCode;//��˾����
	private String comName;//��˾����
	private int busiType;//ҵ������:1-���ͷ� 2-�ٱ����ͷ� 3-��ѯ�� 99-����
	private String dpName;//��������
	private String smName;//ҵ��Ա����
	private String pmName;//��Ŀ����
	private String zyx1;//��չ�ֶ�1����Ʊ���뵥��ע
	private String zyx2;//��չ�ֶ�2
	private String zyx3;//��չ�ֶ�3
	private String zyx4;//��չ�ֶ�4
	private String zyx5;//��չ�ֶ�5
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
