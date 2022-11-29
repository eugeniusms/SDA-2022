
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.*;
import java.util.StringTokenizer;

public class TP03 {
    private static InputReader in;
    private static PrintWriter out;
 
    // Member variables of this class
    static long dist[];
    static Set<Integer> settled;
    static PriorityQueue<Node> pq;
    // Number of vertices
    static int V;
    static List<List<Node> > adj;

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
        int VE = 1+N; // ex: 0+8 nodes = 9 nodes
        // input benteng yang diserang
        int[] attacked = new int[M];
        for(int i = 0; i < M; i++) {
            attacked[i] = in.nextInt();
        }

        // ================================= INISIASI EDGE ===========================================
        long E = in.nextInt();
        // Adjacency list untuk edge yang ada
        adj = new ArrayList<List<Node> >();
        // Initialize list for every node
        for (int i = 0; i < VE; i++) {
            List<Node> item = new ArrayList<Node>();
            adj.add(item);
        }
        // Mendaftarkan edge yang ada melalui input
        for (int i = 0; i < E; i++) {
            int A = in.nextInt(); int B = in.nextInt(); long W = in.nextInt();
            adj.get(B).add(new Node(A, W));
        }

        // Implement multisource destination
        // Menyambungkan node 0 dalam edge dengan bobot 0 terhubung ke setiap attacked (node 0 udah ada dalam 0 <- V)
        for (int i = 0; i < M; i++) {
            adj.get(0).add(new Node(attacked[i], 0));
        }
        // Call Dijkstra
        inisiateDijkstra(VE); // RESET
        dijkstra(0);
        // Memo Distance/Cost
        for (int i = 0; i < M; i++) {
            // System.out.println(attacked[i]);
            long[] temp = new long[10069];
            for (int j = 0; j < VE; j++) { // mencari distance ke benteng yang diserang
                System.out.println("CEK attacked: "+j+ " "+dist[j]); // TEST 
                temp[j] = dist[j];
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

    // =========================== DIJKSTRA ==================================  
    // inisiate Dijkstra
    static void inisiateDijkstra(int v) {
        V = v;
        dist = new long[v];
        settled = new HashSet<Integer>();
        pq = new PriorityQueue<Node>(v, new Node());
    }

    // Method 1
    // Dijkstra's Algorithm
    static void dijkstra(int src) {
        for (int i = 0; i < V; i++)
            dist[i] = Long.MAX_VALUE;
        pq.add(new Node(src, 0));
        dist[src] = 0;
        while (settled.size() != V) {
            if (pq.isEmpty())
                return;
            int u = pq.remove().node;
            if (settled.contains(u))
                continue;
            settled.add(u);
            e_Neighbours(u);
        }
    }

    // Method 2
    static void e_Neighbours(int u) {
        long edgeDistance = -1;
        long newDistance = -1;
        for (int i = 0; i < adj.get(u).size(); i++) {
            Node v = adj.get(u).get(i);
            if (!settled.contains(v.node)) {
                edgeDistance = v.cost;
                newDistance = dist[u] + edgeDistance;
                if (newDistance < dist[v.node])
                    dist[v.node] = newDistance;
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

        public char nextChar() {
            return next().equals("R") ? 'R' : 'B';
        }
    }
}

class Node implements Comparator<Node> {
    public int node;
    public long cost;
 
    public Node() {}
    public Node(int node, long cost) {
        this.node = node;
        this.cost = cost;
    }

    @Override public int compare(Node node1, Node node2) {
        if (node1.cost < node2.cost)
            return -1;
        if (node1.cost > node2.cost)
            return 1;
        return 0;
    }
}

class Edge implements Comparable<Edge>{
    int start;
    int destination;
    int cost;

    Edge(int start, int destination, int cost) {
        this.start = start;
        this.destination = destination;
        this.cost = cost;
    }

    @Override
    public int compareTo(Edge other) {
        if (this.cost == other.cost) {
            if (this.start == other.start) {
                return this.destination - other.destination;
            }
            return this.start - other.start; 
        }
        return this.cost - other.cost;
    }
}