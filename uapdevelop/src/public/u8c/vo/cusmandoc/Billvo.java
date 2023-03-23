package u8c.vo.cusmandoc;

import java.util.List;

public class Billvo {

    private Parentvo parentvo;
    private List<Childrenvo> childrenvo;
    private List<AddressVO> addrs;
    private List<CustBankVO> custBanks;
    public void setParentvo(Parentvo parentvo) {
         this.parentvo = parentvo;
     }
     public Parentvo getParentvo() {
         return parentvo;
     }

    public void setChildrenvo(List<Childrenvo> childrenvo) {
         this.childrenvo = childrenvo;
     }
     public List<Childrenvo> getChildrenvo() {
         return childrenvo;
     }
	public List<AddressVO> getAddrs() {
		return addrs;
	}
	public void setAddrs(List<AddressVO> addrs) {
		this.addrs = addrs;
	}
	public List<CustBankVO> getCustBanks() {
		return custBanks;
	}
	public void setCustBanks(List<CustBankVO> custBanks) {
		this.custBanks = custBanks;
	}

}