import java.util.*;

// Creation of Adjacency List
// The adjacency List consist of an ArrayList within an
// ArrayList. The inner ArrayList holds the HashMap of
// (vertices,weight)
public class WeightedGraph {
	int v;
	ArrayList<ArrayList<HashMap<Integer, Integer> > > adj;
	WeightedGraph(int v)
	{
		this.v = v;
		this.adj = new ArrayList<>();

		for (int i = 0; i < v; i++) {
			this.adj.add(new ArrayList<>());
		}
	}
	// Function to add an Edge
	void addEdge(int u, int v, int weight)
	{
		this.adj.get(u).add(new HashMap<>());
		this.adj.get(u)
			.get(this.adj.get(u).size() - 1)
			.put(v, weight);

		this.adj.get(v).add(new HashMap<>());
		this.adj.get(v)
			.get(this.adj.get(v).size() - 1)
			.put(u, weight);
	}

	// Function for printing the whole graph
	// Stream API has been used
	// to easily access the HashMap elements
	// This code may not work in versions
	// prior to java 8

	void printGraph()
	{
		for (int i = 0; i < this.v; i++) {
			System.out.println("\nNode " + i
							+ " makes an edge with ");
			for (HashMap<Integer, Integer> j :
				this.adj.get(i)) {
				j.entrySet().forEach(
					e
					-> System.out.println(
						"\tNode " + e.getKey()
						+ " with edge weight "
						+ e.getValue() + " "));
			}
		}
	}
	// Main method
	public static void main(String[] args)
	{
		int v = 5;
		WeightedGraph obj = new WeightedGraph(v);
		obj.addEdge(0, 1, 10);
		obj.addEdge(0, 4, 20);
		obj.addEdge(1, 2, 30);
		obj.addEdge(1, 3, 40);
		obj.addEdge(1, 4, 50);
		obj.addEdge(2, 3, 60);
		obj.addEdge(3, 4, 70);
		obj.printGraph();
	}
}
// This code is submitted by Abhishek_Manna_HETC
