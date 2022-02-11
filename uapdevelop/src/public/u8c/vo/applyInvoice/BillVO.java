package u8c.vo.applyInvoice;
import java.util.List;
public class BillVO {
	private ParentVO parentvo;
	private List<ChildrenVO> children;
	public ParentVO getParentvo() {
		return parentvo;
	}
	public void setParentvo(ParentVO parentvo) {
		this.parentvo = parentvo;
	}
	public List<ChildrenVO> getChildren() {
		return children;
	}
	public void setChildren(List<ChildrenVO> children) {
		this.children = children;
	}
	 
}
