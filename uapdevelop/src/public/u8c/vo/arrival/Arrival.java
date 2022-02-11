package u8c.vo.arrival;

public class Arrival {
	 /// <summary>
    /// 
    /// </summary>
    public ArrivalHeader header;
    /// <summary>
    /// 
    /// </summary>
    public ArrivalMessage message;
    
	public ArrivalHeader getHeader() {
		return header;
	}
	public void setHeader(ArrivalHeader header) {
		this.header=header;
	}
	public ArrivalMessage getMessage() {
		return message;
	}
	public void setMessage(ArrivalMessage message) {
		this.message = message;
	}
    
}
