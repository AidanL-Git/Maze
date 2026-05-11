import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.Stack;

public class Maze {
	// Stores the Graph to represent the Maze
	private Graph mazeGraph;
	// Stores the start & end node ID's
	private int startID;
	private int endID;
	// Stores the amount of coins provided to solve
	private int totalCoinCount;
	// Stores the stack used in Maze traversals
	private Stack<GraphNode> traversalStack;
	
	// Initializes this Maze instance
	public Maze(String inputFile) throws MazeException {
		try {
			// Reads the maze data from inputFile
			readInput(new BufferedReader(new FileReader(inputFile)));
		} catch (Exception e) {
			// Throw a MazeException if readInput() fails
			throw new MazeException("Maze: constructor failed.");
		}
		// Initialize the stack
		traversalStack = new Stack<GraphNode>();
	}
	
	// Return a reference to the maze graph
	public Graph getGraph() {
		return mazeGraph;
	}
	
	// Find a solution to the maze
	public Iterator<GraphNode> solve() {
		try {
			// Uses a DFS algorithm to return a pathway through the maze
			Iterator<GraphNode> solution = DFS(totalCoinCount, mazeGraph.getNode(startID));
			return solution;
		} catch (GraphException e) {
			// Print the error if the DFS fails
			e.printStackTrace();
		}
		return null;
	}
	
	// DFS algorithm that returns an iterator of nodes that is a valid
	// path throught the maze
	private Iterator<GraphNode> DFS(int k, GraphNode go) throws GraphException {
		// Add the current node to the traversal stack
		traversalStack.push(go);
		
		// If this node is the end of the maze, update the 
		// amount of coins used & return an iterator of the stack
		if (go.getName() == endID) {
			totalCoinCount = k;
			return traversalStack.iterator();
		}
		
		// Mark this node for future calls
		go.mark(true);
		
		try {
			// Find all edges incident to the current node
			Iterator<GraphEdge> incidentEdges = mazeGraph.incidentEdges(go);
			
			// Iterate over each edge
			while (incidentEdges.hasNext()) {
				GraphEdge nextEdge = incidentEdges.next();
				
				// Set the next endpoint to whichever the current node isn't
				GraphNode endpoint;
				if (nextEdge.secondEndpoint() == go)
					endpoint = nextEdge.firstEndpoint();
				else 
					endpoint = nextEdge.secondEndpoint();
				
				// If the path is affordable & hasn't been visited
				if (nextEdge.getType() <= k && !endpoint.isMarked()) {
					// Visit the other endpoint & remove coins if necessary
					Iterator<GraphNode> next = DFS(k - nextEdge.getType(), endpoint);
					
					// If a solution is found, return it
					if (next != null) 
						return next;	
				}
			}
		} catch (GraphException e) {
			// Throw a GraphException if incidentEdges() fails
			throw new GraphException("Maze.DFS: incident edges not found.");
		}
		
		// pop the current node off the stack & return null
		traversalStack.pop();
		return null;
	}
	
	// Attempts to initialize the maze using a file's contents
	private void readInput(BufferedReader inputReader) throws IOException, GraphException {
		try {
			// Skip the line corresponding to display
			inputReader.readLine();
			
			// Initialize the maze's dimensions & coin allowance
			int mazeWidth = Integer.parseInt(inputReader.readLine());
			int mazeLength = Integer.parseInt(inputReader.readLine());
			totalCoinCount = Integer.parseInt(inputReader.readLine());
			
			// Initialize the graph with the new values
			mazeGraph = new Graph(mazeWidth * mazeLength);
			
			// Iterate over the rest of the file
			for (int i = 0; i < 2 * mazeLength - 1; i++) {
				String fileData = inputReader.readLine();
				
				// Iterate over each character in the data
				for (int j = 0; j < 2 * mazeWidth - 1; j++) {
					char mazeTileChar = fileData.charAt(j);
					
					// If the current character is along an even diagonal,
					// it will represent a node, so check whether it 
					// corresponds to the entrance/exit
					if (i % 2 == 0 && j % 2 == 0) {
						// Initialize the startID if the character matches
						if (mazeTileChar == 's') 
							startID = mazeWidth * (i / 2) + (j / 2);
						
						// Initialize the endID if the character matches
						else if (mazeTileChar == 'x') 
							endID = mazeWidth * (i / 2) + (j / 2);
					}
					
					// If not along the even diagonal, check for doors/corridors
					else { 
						try {
							// If the character corresponds to a corridor & is in an even row, 
							// insert a corridor with horizontal connections
							if (mazeTileChar == 'c' && i % 2 == 0) { 
								insertEdge(mazeWidth * (i/2) + ((j - 1)/ 2), mazeWidth * (i/2) + ((j + 1)/ 2), 0, "corridor");
							}
							
							// If the character corresponds to a door & is in an even row, 
							// insert a door with horizontal connections
							else if (Character.isDigit(mazeTileChar) && i % 2 == 0) { 
								insertEdge(mazeWidth * (i/2) + ((j - 1)/ 2), mazeWidth * (i/2) + ((j + 1)/ 2), 
										Character.getNumericValue(mazeTileChar), "door");
							}
							
							// If the character corresponds to a corridor, insert with vertical connections
							else if (mazeTileChar == 'c') { 
								insertEdge(mazeWidth * ((i - 1)/ 2) + (j/2), mazeWidth * ((i + 1)/2) + (j/2), 0, "corridor");
							}
							
							// If the character corresponds to a door, insert with vertical connections
							else if (Character.isDigit(mazeTileChar)) { 
								insertEdge(mazeWidth * ((i - 1)/ 2) + (j/2), mazeWidth * ((i + 1)/2) + (j/2), 
										Character.getNumericValue(mazeTileChar), "door");
							}
						} catch (GraphException e) {
							// Throw a GraphException if the edge fails to insert
							throw new GraphException("Maze.readInput: unable to insert edge.");
						}
					}
				}
			}
			// Close the file reader
			inputReader.close();
			
		} catch (IOException e) {
			// Throw an IOException if the file fails to open
			throw new IOException("Maze.readInput: failed to read input file.");
		}
	}
	
	// Attempts to insert a new edge into the maze's graph
	private void insertEdge(int node1, int node2, int linkType, String label) throws GraphException {
		try {
			// Call the graph's insertEdge() method
			mazeGraph.insertEdge(mazeGraph.getNode(node1), mazeGraph.getNode(node2), linkType, label);
		} catch (GraphException e) {
			// Throws a GraphException if the insertion fails
			throw new GraphException("Maze.insertEdge: unable to insert edge.");
		}
	}
}