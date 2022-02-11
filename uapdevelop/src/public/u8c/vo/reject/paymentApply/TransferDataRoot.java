package u8c.vo.reject.paymentApply;
import java.util.List;
public class TransferDataRoot {
	private String transferBusinType;//转付保费类型 (1-转付保费,2-再保转付保费,3-理赔转付保费,4-再保理赔转付保费)
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