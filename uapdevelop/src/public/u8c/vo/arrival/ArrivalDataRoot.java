package u8c.vo.arrival;

import java.util.List;

public class ArrivalDataRoot {
	private String arrivalBusinType;//����ҵ������ (1-���ͷѵ���,2-ҵ����㵽��)
    private int totalNum;
    private List<ArrivalData> arrivalData;
    public void setArrivalBusinType(String arrivalBusinType) {
         this.arrivalBusinType = arrivalBusinType;
     }
     public String getArrivalBusinType() {
         return arrivalBusinType;
     }

    public void setTotalNum(int totalNum) {
         this.totalNum = totalNum;
     }
     public int getTotalNum() {
         return totalNum;
     }

    public void setArrivalData(List<ArrivalData> arrivalData) {
         this.arrivalData = arrivalData;
     }
     public List<ArrivalData> getArrivalData() {
         return arrivalData;
     }
}
