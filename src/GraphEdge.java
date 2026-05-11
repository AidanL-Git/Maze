public class GraphEdge {
	// Stores the nodes on either end of the edge
	private GraphNode origin; 
	private GraphNode destination;
	
	// Stores the type for doors & label
	private int type;
	private String label;
	
	// Initializes all variables of this GraphEdge instance
	public GraphEdge(GraphNode u, GraphNode v, int type, String label) {
		this.origin = u;
		this.destination = v;
		this.type = type;
		this.label = label;
	}
	
	// Returns the origin of the edge
	public GraphNode firstEndpoint() {
		return origin;
	}
	
	// Returns the destination of the edge
	public GraphNode secondEndpoint() {
		return destination;
	}
	
	// Returns the type of the edge
	public int getType() {
		return type;
	}
	
	// Sets the type of the edge
	public void settype(int type) {
		this.type = type;
	}
	
	// Returns the label of the edge
	public String getLabel() {
		return label;
	}
	
	// Returns the label of the edge
	public void setLabel(String label) {
		this.label = label;
	}
}