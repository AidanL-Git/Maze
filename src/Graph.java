import java.util.ArrayList;
import java.util.Iterator;

public class Graph implements GraphADT {
	// Stores a list of nodes & edges via an adjacency list
	ArrayList<GraphNode> nodes;
	ArrayList<ArrayList<GraphEdge>> edges;
	
	// Returns reference to an edge between two vertices if one exists
	private GraphEdge edgeExists(GraphNode nu, GraphNode nv) {
		int smaller;
		
		// Determines shorter list of edges between nodes to check 
		if (edges.get(nu.getName()).size() < edges.get(nv.getName()).size())
			smaller = nu.getName();
		else
			smaller = nv.getName();
		
		// Loops over each edge corresponding to the shorter list
		for (int c = 0; c < edges.get(smaller).size(); c++) {
			GraphEdge cur = edges.get(smaller).get(c);
			
			// If an edge exists bounded by both nodes in either order, return it
			if ((cur.firstEndpoint().equals(nu) && cur.secondEndpoint().equals(nv)) 
					|| (cur.firstEndpoint().equals(nv) && cur.secondEndpoint().equals(nu)))
				return cur;
		}
		
		// If no edge is found, return null
		return null;
	}
	
	// Initializes both lists for a Graph of size n
	public Graph(int n) {
		nodes = new ArrayList<GraphNode>();
		edges = new ArrayList<ArrayList<GraphEdge>>();
		
		// Initialize each element within both lists
		for (int i = 0; i < n; i++) {
			nodes.add(new GraphNode(i));
			edges.add(new ArrayList<GraphEdge>());
		}
	}
	
	// Attempts to insert an edge between two nodes
	public void insertEdge(GraphNode nodeu, GraphNode nodev, int type, String label) throws GraphException {
		// If either of the argument nodes don't exist, throw a GraphException
		if (!nodes.contains(nodeu) || !nodes.contains(nodev)) 
			throw new GraphException("Graph.insertEdge: unable to insert edge.");
		
		// Create a new edge 
		GraphEdge newEdge = new GraphEdge(nodeu, nodev, type, label);
		
		// If the edge exists already, throw a GraphException
		if (edgeExists(nodeu, nodev) != null)
			throw new GraphException("Graph.insertEdge: edge between nodes already exists.");
		
		// Otherwise, insert the edge into the lists of both nodes
		edges.get(nodeu.getName()).add(newEdge);
		edges.get(nodev.getName()).add(newEdge);
	}
	
	// Attempts to return a reference to a node at index u; 
	public GraphNode getNode(int u) throws GraphException {
		GraphNode returnNode;
		
		// If retrieving the node goes out of bounds or returns null,
		// throw a GraphException
		try {
			returnNode = nodes.get(u);
		} catch (IndexOutOfBoundsException e) {
			throw new GraphException("Graph.getNode: node doesn't exist.");
		}
		if (returnNode == null)
			throw new GraphException("Graph.getNode: node doesn't exist.");
		
		// Otherwise, return the node
		return returnNode;
	}
	
	// Attempts to return an iterator of the edges incident to node u
	public Iterator<GraphEdge> incidentEdges(GraphNode u) throws GraphException {
		// throws a GraphException if the node doesn't exist
		if (!nodes.contains(u))
			throw new GraphException("Graph.incidentEdges: node doesn't exist");
		
		// returns null if the node is isolated
		if (edges.get(u.getName()).isEmpty()) 
			return null;
		
		return edges.get(u.getName()).iterator();
	}

	// Attempts to return a reference to the edge between nodes u & v
	public GraphEdge getEdge(GraphNode u, GraphNode v) throws GraphException {
		// Throw a GraphException if either node doesn't exist
		if (!nodes.contains(u) || !nodes.contains(v)) 
			throw new GraphException("Graph.getEdge: node doesn't exist.");
		
		// Throw a GraphException if both edge lists are empty
		if (edges.get(u.getName()).isEmpty() || edges.get(v.getName()).isEmpty())
			throw new GraphException("Graph.getEdge: edge doesn't exist.");
		
		// Retrieves a reference to the edge
		GraphEdge returnEdge = edgeExists(u, v);
		
		// Throw a GraphExeption if the retrieval failed
		if (returnEdge == null)
			throw new GraphException("Graph.getEdge: edge doesn't exist.");

		// Otherwise, return the reference
		return returnEdge;
	}
	
	// Indicates whether nodes u & v share an edge
	public boolean areAdjacent(GraphNode u, GraphNode v) throws GraphException {
		// Throw a GraphException if either node doesn't exist
		if (!nodes.contains(u) || !nodes.contains(v)) 
			throw new GraphException("Graph.getEdge: node doesn't exist.");
		
		try {
			// Returns true when getEdge() returns a valid value
			getEdge(u, v);
			return true;
		} catch (GraphException e) {
			// Returns false if there is no edge
			return false;
		}
	}
}