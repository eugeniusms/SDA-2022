
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.*;
import java.util.StringTokenizer;
// Java Program to Implement Dijkstra's Algorithm
// Using Priority Queue
 
// Main class DPQ
public class MultisourceDijkstra {
    private static InputReader in;
    private static PrintWriter out;
 
    // Member variables of this class
    private long dist[];
    private Set<Integer> settled;
    private PriorityQueue<Node> pq;
    // Number of vertices
    private int V;
    List<List<Node> > adj;

    static boolean[] isExist = new boolean[10069];
    static Long[] memoDist = new Long[10069]; // <index: node, value: minimum distance>

    // key: dist, value: array[10069] node
    static ArrayList<long[]> memo = new ArrayList<long[]>();

    public static void main(String arg[]) {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        // ================================= INISIASI GRAPH ===========================================
        // N = number of vertices
        // M = number of vertices that ! (attacked)
        int N = in.nextInt(), M = in.nextInt();
        int V = 1+N; // ex: 0+8 nodes = 9 nodes
        // input benteng yang diserang
        int[] attacked = new int[M];
        for(int i = 0; i < M; i++) {
            attacked[i] = in.nextInt();
        }

        // ================================= INISIASI EDGE ===========================================
        long E = in.nextInt();
        // Adjacency list untuk edge yang ada
        List<List<Node> > adj = new ArrayList<List<Node> >();
        // Initialize list for every node
        for (int i = 0; i < V; i++) {
            List<Node> item = new ArrayList<Node>();
            adj.add(item);
        }
        // Mendaftarkan edge yang ada melalui input
        for (int i = 0; i < E; i++) {
            int A = in.nextInt(); int B = in.nextInt(); long W = in.nextInt();
            adj.get(B).add(new Node(A, W));
        }

        // ================================= CALCULATE DIJKSTRA ======================================
        for (int i = 0; i < M; i++) {
            // Calculating the single source shortest path
            MultisourceDijkstra dpq = new MultisourceDijkstra(V); // RESET
            dpq.dijkstra(adj, attacked[i]); // (adj, source)

            // System.out.println(attacked[i]);
            long[] temp = new long[10069];
            for (int j = 1; j < V; j++) { // mencari distance ke benteng yang diserang
                // System.out.println("CEK attacked: "+j+ " "+dpq.dist[j]); // TEST 
                temp[j] = dpq.dist[j];
            }
            memo.add(temp);
        }


        // ================================= INPUT QUERY ============================================
        int Q = in.nextInt();
        for (int i = 0; i < Q; i++) {
            int S = in.nextInt(); 
            long K = in.nextInt();

            boolean isPossible = false;
            for (long[] dist : memo) {
                // System.out.println(dist[S]);
                if (dist[S] < K) {
                    isPossible = true;
                    break;
                }
            }
            out.println(isPossible ? "YES" : "NO");
        }
        out.close();    
    }
 
    // Constructor of this class
    public MultisourceDijkstra(int V) {
        // This keyword refers to current object itself
        this.V = V;
        dist = new long[V];
        settled = new HashSet<Integer>();
        pq = new PriorityQueue<Node>(V, new Node());
    }
 
    // Method 1
    // Dijkstra's Algorithm
    public void dijkstra(List<List<Node> > adj, int src) {
        this.adj = adj;
 
        for (int i = 0; i < V; i++)
            dist[i] = Long.MAX_VALUE;
 
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
    private void e_Neighbours(int u) {
 
        long edgeDistance = -1;
        long newDistance = -1;
 
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
    public long cost;
 
    // Constructors of this class
    // Constructor 1
    public Node() {}
    // Constructor 2
    public Node(int node, long cost) {
        // This keyword refers to current instance itself
        this.node = node;
        this.cost = cost;
    }
 
    // Method 1
    @Override public int compare(Node node1, Node node2) {
        if (node1.cost < node2.cost)
            return -1;
        if (node1.cost > node2.cost)
            return 1;
        return 0;
    }
}


// References:
// *) https://www.geeksforgeeks.org/dijkstras-shortest-path-algorithm-in-java-using-priorityqueue/

// IDEA:

// SUDAH KECOVER 0-4
// 0) INFINITE GAUSAH DIMASUKKAN KE MEMO KARENA PASTI GABISA DIJANGKAU (DONE)
// 1) SIMPEN MEMO DIST OF ATTACKED BENTENG DI NODE SEKALIAN
// 2) SIMPEN K JUGA DAN HASIL YES/NO NYA DI MEMO
// 3) JIKA DIST[i] > 1.000.000.000, MAKA GABISA DIJANGKAU KARENA K MAX = 10^9
// 4) GAUSAH DIITERASI SEMUA DIST, CUMA PERLU SIMPAN DIST YG TERKECIL UNTUK DIBANDINGKAN DENGAN K (MINIMALNYA) (MEMO DIST)

// IDE REVERSE AJA DIJKSTRANYA