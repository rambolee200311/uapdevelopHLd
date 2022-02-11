package u8c.vo.respmsg;

import java.util.Date;

public class Response {

    private String senderDateTime;
    private String seqNO;
    public void setSenderDateTime(String senderDateTime) {
         this.senderDateTime = senderDateTime;
     }
     public String getSenderDateTime() {
         return senderDateTime;
     }

    public void setSeqNO(String seqNO) {
         this.seqNO = seqNO;
     }
     public String getSeqNO() {
         return seqNO;
     }

}