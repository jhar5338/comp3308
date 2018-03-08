import java.util.ArrayList;
import java.util.List;

public class Node {
	private int id;
	private int prev;
	private String value;
	private final List<Node> children = new ArrayList<>();
	private final Node parent;

	public Node(Node parent, int id, int prev) {
		this.parent = parent;
		this.id = id;
		this.prev = prev;
	}

	public String getValue() {
		return value;
	}

	public int getId() {
		return id;
	}

	public int getPrev() {
		return prev;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public List<Node> getChildren() {
		return children;
	}

	public Node getParent() {
		return parent;
	}

	public void addChild(Node child) {
		children.add(child);
	}
	
	public boolean hasChildren() {
		if (children.size()>0) {
			return true;
		}
		return false;
	}
	public void removeChildren() {
		children.clear();
	}

}