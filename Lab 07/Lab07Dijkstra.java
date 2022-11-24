
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.*;
import java.util.*;
import java.util.StringTokenizer;
// Java Program to Implement Dijkstra's Algorithm
// Using Priority Queue
 
// Main class DPQ
public class Lab07Dijkstra {
    private static InputReader in;
    private static PrintWriter out;
 
    // Member variables of this class
    private int dist[];
    private Set<Integer> settled;
    private PriorityQueue<Node> pq;
    // Number of vertices
    private int V;
    List<List<Node> > adj;

    public static void main(String arg[]) {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        // N = number of vertices
        // M = number of vertices that ! (attacked)
        int N = in.nextInt(), M = in.nextInt();
        int V = 1+N; // ex: 0+8 nodes = 9 nodes

        // input benteng yang diserang
        int[] attacked = new int[M];
        for(int i = 0; i < M; i++) {
            attacked[i] = in.nextInt();
        }

        // Adjacency list representation of the
        // connected edges by declaring List class object
        // Declaring object of type List<Node>
        List<List<Node> > adj
            = new ArrayList<List<Node> >();
 
        // Initialize list for every node
        for (int i = 0; i < V; i++) {
            List<Node> item = new ArrayList<Node>();
            adj.add(item);
        }
 

        // 1 3 2
        // 1 4 1
        // 1 2 7
        // 3 4 2
        // 1 5 1
        // 5 6 2
        // 6 7 10
        // 7 5 3
        // 7 8 4
        // 8 6 5
        // Inputs for the GFG(dpq) graph
        adj.get(1).add(new Node(3, 2));
        adj.get(1).add(new Node(4, 1));
        adj.get(1).add(new Node(2, 7));
        adj.get(3).add(new Node(4, 2));
        adj.get(1).add(new Node(5, 1));
        adj.get(5).add(new Node(6, 2));
        adj.get(6).add(new Node(7, 10));
        adj.get(7).add(new Node(5, 3));
        adj.get(7).add(new Node(8, 4));
        adj.get(8).add(new Node(6, 5));

        
        // Calculating the single source shortest path
        Lab07Dijkstra dpq = new Lab07Dijkstra(V);

        // input query
        int Q = in.nextInt();
        for (int i = 0; i < Q; i++) {
            int S = in.nextInt(); 
            long K = in.nextInt();

            dpq.dijkstra(adj, S); // (adj, source)
 
            // Printing the shortest path to all the nodes
            // from the source node
            System.out.println("CEK QUERY: "+S);

            // // TESTING AJA NIE
            // System.out.println("The shorted path from node :");
    
            // for (int j = 0; j < dpq.dist.length; j++)
            //     System.out.println(S + " to " + j + " is "
            //                     + dpq.dist[j]);

            // JAWAB
            boolean isPossible = false;
            for (int j = 0; j < attacked.length; j++) { // mencari distance ke benteng yang diserang
                System.out.println("CEK attacked: "+attacked[j]+ " "+dpq.dist[attacked[j]]); // TEST
                if (dpq.dist[attacked[j]] < K) {
                    isPossible = true;
                    break;
                } 
            }
            System.out.println(isPossible ? "YES" : "NO");
        }
            
    }
 
    // Constructor of this class
    public Lab07Dijkstra(int V)
    {
 
        // This keyword refers to current object itself
        this.V = V;
        dist = new int[V];
        settled = new HashSet<Integer>();
        pq = new PriorityQueue<Node>(V, new Node());
    }
 
    // Method 1
    // Dijkstra's Algorithm
    public void dijkstra(List<List<Node> > adj, int src)
    {
        this.adj = adj;
 
        for (int i = 0; i < V; i++)
            dist[i] = Integer.MAX_VALUE;
 
        // Add source node to the priority queue
        pq.add(new Node(src, 0));
 
        // Distance to the source is 0
        dist[src] = 0;
 
        while (settled.size() != V) {
 
            // Terminating condition check when
            // the priority queue is empty, return
            if (pq.isEmpty())
                return;
 
            // Removing the minimum distance node
            // from the priority queue
            int u = pq.remove().node;
 
            // Adding the node whose distance is
            // finalized
            if (settled.contains(u))
 
                // Continue keyword skips execution for
                // following check
                continue;
 
            // We don't have to call e_Neighbors(u)
            // if u is already present in the settled set.
            settled.add(u);
 
            e_Neighbours(u);
        }
    }
 
    // Method 2
    // To process all the neighbours
    // of the passed node
    private void e_Neighbours(int u)
    {
 
        int edgeDistance = -1;
        int newDistance = -1;
 
        // All the neighbors of v
        for (int i = 0; i < adj.get(u).size(); i++) {
            Node v = adj.get(u).get(i);
 
            // If current node hasn't already been processed
            if (!settled.contains(v.node)) {
                edgeDistance = v.cost;
                newDistance = dist[u] + edgeDistance;
 
                // If new distance is cheaper in cost
                if (newDistance < dist[v.node])
                    dist[v.node] = newDistance;
 
                // Add the current node to the queue
                pq.add(new Node(v.node, dist[v.node]));
            }
        }
    }

    // taken from https://codeforces.com/submissions/Petr
    // together with PrintWriter, these input-output (IO) is much faster than the
    // usual Scanner(System.in) and System.out
    // please use these classes to avoid your fast algorithm gets Time Limit
    // Exceeded caused by slow input-output (IO)
    static class InputReader {
        public BufferedReader reader;
        public StringTokenizer tokenizer;

        public InputReader(InputStream stream) {
            reader = new BufferedReader(new InputStreamReader(stream), 32768);
            tokenizer = null;
        }

        public String next() {
            while (tokenizer == null || !tokenizer.hasMoreTokens()) {
                try {
                    tokenizer = new StringTokenizer(reader.readLine());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            return tokenizer.nextToken();
        }

        public int nextInt() {
            return Integer.parseInt(next());
        }

    }
}
 
// Class 2
// Helper class implementing Comparator interface
// Representing a node in the graph
class Node implements Comparator<Node> {
 
    // Member variables of this class
    public int node;
    public int cost;
 
    // Constructors of this class
 
    // Constructor 1
    public Node() {}
 
    // Constructor 2
    public Node(int node, int cost)
    {
 
        // This keyword refers to current instance itself
        this.node = node;
        this.cost = cost;
    }
 
    // Method 1
    @Override public int compare(Node node1, Node node2)
    {
 
        if (node1.cost < node2.cost)
            return -1;
 
        if (node1.cost > node2.cost)
            return 1;
 
        return 0;
    }
}

// References:
// *) https://www.geeksforgeeks.org/dijkstras-shortest-path-algorithm-in-java-using-priorityqueue/