package restapi.viewmodel;

public class HClusterNode {
	private String nodeName;
	private String parentName;
	private double height;
	private HClusterNode[] children; 

	public HClusterNode(String nodeName, HClusterNode[] hClusterNodes, double height) {
		this.nodeName = nodeName;
		this.children = hClusterNodes;
		this.height = height;
	}

	public HClusterNode(String nodeName, String parentName, HClusterNode[] hClusterNodes, double height) {
		this.nodeName = nodeName;
		this.parentName = parentName;
		this.children = hClusterNodes;
		this.height = height;
		
	}

	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
	}

	public HClusterNode[] getChildren() {
		return children;
	}

	public void setChildren(HClusterNode[] children) {
		this.children = children;
	}

	
	public void setParent(String parentName) {
		this.parentName = parentName;
		
	}

}
