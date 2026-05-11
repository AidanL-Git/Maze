public class GraphNode {
	// Stores the name of the node & whether a search algorithm marked it
	private int name;
	private boolean mark;
	
	// Initializes the name of this GraphNode instance
	public GraphNode(int name) {
		this.name = name;
	}

	// Sets the mark value of this node
	public void mark(boolean mark) {
		this.mark = mark;
	}
	
	// Returns the mark value of this node
	public boolean isMarked() {
		return mark;
	}
	
	// Returns the name of this node
	public int getName() {
		return name;
	}
}