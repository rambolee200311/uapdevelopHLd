package u8c.vo.reject.paymentApply;
import java.util.List;
public class TransferDataRoot {
	private String transferBusinType;//ת���������� (1-ת������,2-�ٱ�ת������,3-����ת������,4-�ٱ�����ת������)
    private int totalNum;
    private List<TransferData> transferData;
	public String getTransferBusinType() {
		return transferBusinType;
	}
	public void setTransferBusinType(String transferBusinType) {
		this.transferBusinType = transferBusinType;
	}
	public int getTotalNum() {
		return totalNum;
	}
	public void setTotalNum(int totalNum) {
		this.totalNum = totalNum;
	}
	public List<TransferData> getTransferData() {
		return transferData;
	}
	public void setTransferData(List<TransferData> transferData) {
		this.transferData = transferData;
	}
}