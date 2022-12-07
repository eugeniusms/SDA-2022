
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
 
    // Number of vertices
    static int V = 1; // default = 1
    static List<List<Node> > adj;
    static Edge[] edges; 

    // memo shortest path by node
    static ArrayList<Integer> kurcaci = new ArrayList<Integer>();
    static Dist[] memoByNode = new Dist[1069]; // [node][Dist[i]]
    static boolean[] isMemo = new boolean[1069]; // [node]

    // SIMULASI 
    static long[] memoSim_K = new long[1069]; // [node][Simulasi-K[i]]
    static boolean[] isMemoSim_K = new boolean[1069]; // [node]

    // SUPER
    static Dist[] memoSkip = new Dist[1069]; // [node][skip1Dist[i]]
    static boolean[] isMemoSkip = new boolean[1069]; // [node]

    public static void main(String arg[]) {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        // ================================= INISIASI GRAPH ===========================================
        // N = number of vertices
        // M = number of vertices that ! (attacked)
        V += in.nextInt();
        
        // ================================= INISIASI EDGE ===========================================
        int E = in.nextInt();
        edges = new Edge[E*2]; // kali 2 karena undirected
        // Adjacency list untuk edge yang ada
        adj = new ArrayList<List<Node> >();
        // Initialize list for every node
        for (int i = 0; i < V; i++) {
            adj.add(new ArrayList<Node>());
        }
        // Mendaftarkan edge yang ada melalui input
        for (int i = 0; i < E; i++) {
            int A = in.nextInt(); int B = in.nextInt(); long W = in.nextInt(); long S = in.nextInt();
            // karena dijkstra undirected berlaku dua arah
            adj.get(A).add(new Node(B, W, S));
            adj.get(B).add(new Node(A, W, S));
        }

        // ================================= INISIASI MAX SPANNING TREE ===============================
        findMaximumSpanningTree(V);

        // ================================= INISIASI NODE DENGAN KURCACI ===========================
        int P = in.nextInt();
        for (int i = 0; i < P; i++) {
            kurcaci.add(in.nextInt());
        }

        // Inisiate Dijkstra First Time (Pos Kurcaci)
        for (int k : kurcaci) {
            palingDijkstra(k); // sekalian memo di dalam fungsi
        }

        // ================================= INPUT QUERY ============================================
        int Q = in.nextInt();
        for (int i = 0; i < Q; i++) {
            String query = in.next();
            if (query.equals("KABUR")) {
                KABUR();
            } else if (query.equals("SIMULASI")) {
                SIMULASI();
            } else {
                // SUPER();
                SUPER();
            }
        }
        out.close();    
    }

    // QUERY 1 : KABUR
    static void KABUR() {
        int source = in.nextInt(); int destination = in.nextInt();

        // Memanfaatkan Kruskal's Maximum Spanning Tree (MST))
        // Gunakan DFS untuk mencari path dari source ke destination
        visited = new boolean[V]; // reset
        DFS(source, destination, new Stack<Integer>());
    }

    static void getMinL(Vector<Integer> stack) {
        long minL = Long.MAX_VALUE;
        for(int i = 0; i < stack.size() - 1; i++) {
            long l = spanningTreeEdges[stack.get(i)][stack.get(i+1)];
            if (l < minL) {
                minL = l;
            }
        }
        out.println(minL);
    }

    static boolean[] visited;
    static void DFS(int x, int y, Vector<Integer> stack) {
        stack.add(x);
        if (x == y) {
            // print the path and return on
            // reaching the destination node
            getMinL(stack);
            return;
        }
        visited[x] = true;
        // if backtracking is taking place     
        List<Node> spanningTreeX = spanningTree.get(x);
        if (spanningTreeX.size() > 0) {
            for(int j = 0; j < spanningTreeX.size(); j++) {
                // if the node is not visited
                int node = spanningTreeX.get(j).node;
                if (visited[node] == false) {
                    DFS(node, y, stack);
                }
            }
        }
        stack.remove(stack.size() - 1);
    }

    static List<List<Node>> spanningTree;
    static long[][] spanningTreeEdges = new long[1069][1069]; // <source, [destination]=L>
    static void findMaximumSpanningTree(int v) { // v : jumlah nodes (include 0)
        spanningTree = new ArrayList<List<Node> >();
        // Initialize list for every node
        for (int i = 0; i < v; i++) {
            spanningTree.add(new ArrayList<Node>());
        }
        // Melakukan pencarian max spanning tree
        // print adj (adj adalah graf penyimpan edge)        
        // Kruskal's Algorithms
        // get all edges

        int counter = 0;
        for (int i = 0; i < adj.size(); i++) {
            for (int j = 0; j < adj.get(i).size(); j++) {
                edges[counter] = new Edge(i, adj.get(i).get(j).node, (int) adj.get(i).get(j).S);
                counter++;
            }
        }
        // STEP 1 Kruskal's : Sorting edges
        // Sorting Edges with Merge Sort
        sort(edges, 0, edges.length-1);

        // STEP 2 Kruskal's : Check cycle
        // Check cycle with Union Find
        UnionFind uf = new UnionFind(v);
        for (Edge e : edges) {
            if (!uf.isSameSet(e.start, e.destination)) {
                uf.unionSet(e.start, e.destination);
                spanningTree.get(e.start).add(new Node(e.destination, e.cost, 0));
                spanningTree.get(e.destination).add(new Node(e.start, e.cost, 0));
            }
        }

        // input spanningTreeEdges
        for (int i = 0; i < spanningTree.size(); i++) {
            for (Node n : spanningTree.get(i)) {
                spanningTreeEdges[i][n.node] = n.L;
            }
        }
    }

    // REFERENCE : https://www.geeksforgeeks.org/merge-sort/
    static void merge(Edge arr[], int l, int m, int r) {
        // Find sizes of two subarrays to be merged
        int n1 = m - l + 1;
        int n2 = r - m;
 
        /* Create temp arrays */
        Edge L[] = new Edge[n1];
        Edge R[] = new Edge[n2];
 
        /*Copy data to temp arrays*/
        for (int i = 0; i < n1; ++i)
            L[i] = arr[l + i];
        for (int j = 0; j < n2; ++j)
            R[j] = arr[m + 1 + j];
 
        /* Merge the temp arrays */
 
        // Initial indexes of first and second subarrays
        int i = 0, j = 0;
 
        // Initial index of merged subarray array
        int k = l;
        while (i < n1 && j < n2) {
            if (L[i].compareTo(R[j]) > 0) {
                arr[k] = L[i];
                i++;
            }
            else {
                arr[k] = R[j];
                j++;
            }
            k++;
        }
 
        /* Copy remaining elements of L[] if any */
        while (i < n1) {
            arr[k] = L[i];
            i++;
            k++;
        }
 
        /* Copy remaining elements of R[] if any */
        while (j < n2) {
            arr[k] = R[j];
            j++;
            k++;
        }
    }
 
    // Main function that sorts arr[l..r] using
    // merge()
    static void sort(Edge arr[], int l, int r) {
        if (l < r) {
            // Find the middle point
            int m = l + (r - l) / 2;
 
            // Sort first and second halves
            sort(arr, l, m);
            sort(arr, m + 1, r);
 
            // Merge the sorted halves
            merge(arr, l, m, r);
        }
    }
 
    // QUERY 2 : SIMULASI
    static void SIMULASI() {
        int K = in.nextInt();
        // input pintu keluar
        int[] gate = new int[K];
        for (int i = 0; i < K; i++) {
            gate[i] = in.nextInt();
        }
        long maxTime = 0;
        for (int k : kurcaci) {
            long minTime = Long.MAX_VALUE;
            // find min
            if (isMemoSim_K[k]) {
                minTime = memoSim_K[k];
            } else {
                long[] dist = memoByNode[k].dist;
                for (int g : gate) {
                    if (dist[g] < minTime) {
                        minTime = dist[g];
                    }
                }
                // memo minTime for K
                memoSim_K[k] = minTime; isMemoSim_K[k] = true;
            }
            // find max
            if (minTime > maxTime) {
                maxTime = minTime;
            }
        }

        out.println(maxTime);
    }
    
    // QUERY 3 : SUPER
    static void SUPER() { // manfaatin dijkstra dp
        int s = in.nextInt(); int t = in.nextInt(); int x = in.nextInt();

        // CEK SUDAH PERNAH DIMEMO DI SUPER BELUM
        long[][] dp; MinHeap minHeap;
        long minCostST;
        long skipCostST;
        long minCostTX;
        long skipCostTX;
        // ============================= DIJKSTRA KEDUA DARI T KE SEMUA PASTI DIDAPATI S & X ===============================
        if (isMemoSkip[t]) {
            minCostST = memoByNode[t].dist[s];
            skipCostST = memoSkip[t].dist[s];
            minCostTX = memoByNode[t].dist[x];
            skipCostTX = memoSkip[t].dist[x];
        } else { // GENERATE DP SKIP
            // dp[destination][state=0/1], 0 -> Take, 1 -> Skip
            dp = new long[2][V];
            for (int i = 0; i < V; i++) {
                dp[0][i] = Long.MAX_VALUE;
                dp[1][i] = Long.MAX_VALUE;
            }
            dp[0][t] = 0; // state 0 -> take
            dp[1][t] = 0; // state 1 -> skip
            // dijkstra with 1 is k-skip edge 
            minHeap = new MinHeap();
            minHeap.insert(new Node(t, 0, 0));
            while (!minHeap.isEmpty()) {
                Node start = minHeap.remove();
                // e_neighbours
                long edgeDistance = -1;
                long noSkip = -1;
                for (int i = 0; i < adj.get(start.node).size(); i++) { // untuk setiap edges di node u
                    Node desti = adj.get(start.node).get(i); // ambil node tujuan
                    edgeDistance = desti.L; // cost ke v

                    if (start.skip) { // saat sudah pernah diskip                    
                        long belumskip = dp[0][start.node]; // cost belum pernah skip tapi mencoba skip saat ini  // dijkstra biasa
                        long sudahskip = dp[1][start.node] + edgeDistance; // cost sudah pernah skip + cost ke v (sudah tidak bisa diskip lagi)
                        if (Math.min(belumskip, sudahskip) < dp[1][desti.node]) { // jika cost ke v lebih kecil dari cost sebelumnya
                            dp[1][desti.node] = Math.min(belumskip, sudahskip); // update cost
                            minHeap.insert(new Node(desti.node, dp[1][desti.node], desti.S, true)); // masukkan ke minHeap
                        }

                    } else { // belum pernah diskip
                        noSkip = dp[0][start.node] + edgeDistance;
                        // dijkstra biasa
                        // dijkstra ini biasa state[0] udah bener
                        if (noSkip < dp[0][desti.node]) {
                            dp[0][desti.node] = noSkip;
                            minHeap.insert(new Node(desti.node, dp[0][desti.node], desti.S, false));
                        }

                        // skip jika state 1 lebih kecil dari state 0
                        if (dp[1][desti.node] < noSkip) {
                            minHeap.insert(new Node(desti.node, dp[1][desti.node], desti.S, true));
                        }
                        
                    }

                }
            }   
            minCostST = dp[0][s]; // cost dari s ke t tanpa skip
            skipCostST = dp[1][s]; // cost dari s ke t dengan skip
            minCostTX = dp[0][x]; // cost dari t ke x tanpa skip
            skipCostTX = dp[1][x]; // cost dari t ke x dengan skip
            // memoize
            memoByNode[t] = new Dist(dp[0]); isMemo[t] = true;
            memoSkip[t] = new Dist(dp[1]); isMemoSkip[t] = true;
        }

        // Cari versi terpendek between
        // S -> T -> Skip -> X && S -> Skip -> T -> X
        long result = Math.min(minCostST + skipCostTX, skipCostST + minCostTX);
        out.println(result);
    }

    // DIJKSTRA CAMPUR SUPER & SIMULASI
    static void palingDijkstra(int src) {
        // dp[destination][state=0/1], 0 -> Take, 1 -> Skip
        long[][] dp; MinHeap minHeap;
        dp = new long[2][V];
        for (int i = 0; i < V; i++) {
            dp[0][i] = Long.MAX_VALUE;
            dp[1][i] = Long.MAX_VALUE;
        }
        dp[0][src] = 0; // state 0 -> take
        dp[1][src] = 0; // state 1 -> skip
        // dijkstra with 1 is k-skip edge 
        minHeap = new MinHeap();
        minHeap.insert(new Node(src, 0, 0));
        while (!minHeap.isEmpty()) {
            Node start = minHeap.remove();
            // e_neighbours
            long edgeDistance = -1;
            long noSkip = -1;
            for (int i = 0; i < adj.get(start.node).size(); i++) { // untuk setiap edges di node u
                Node desti = adj.get(start.node).get(i); // ambil node tujuan
                edgeDistance = desti.L; // cost ke v

                if (start.skip) { // saat sudah pernah diskip                    
                    long belumskip = dp[0][start.node]; // cost belum pernah skip tapi mencoba skip saat ini  // dijkstra biasa
                    long sudahskip = dp[1][start.node] + edgeDistance; // cost sudah pernah skip + cost ke v (sudah tidak bisa diskip lagi)
                    if (Math.min(belumskip, sudahskip) < dp[1][desti.node]) { // jika cost ke v lebih kecil dari cost sebelumnya
                        dp[1][desti.node] = Math.min(belumskip, sudahskip); // update cost
                        minHeap.insert(new Node(desti.node, dp[1][desti.node], desti.S, true)); // masukkan ke minHeap
                    }

                } else { // belum pernah diskip
                    noSkip = dp[0][start.node] + edgeDistance;
                    // dijkstra biasa
                    // dijkstra ini biasa state[0] udah bener
                    if (noSkip < dp[0][desti.node]) {
                        dp[0][desti.node] = noSkip;
                        minHeap.insert(new Node(desti.node, dp[0][desti.node], desti.S, false));
                    }

                    // skip jika state 1 lebih kecil dari state 0
                    if (dp[1][desti.node] < noSkip) {
                        minHeap.insert(new Node(desti.node, dp[1][desti.node], desti.S, true));
                    }
                    
                }

            }
        }   
        // memoize
        memoByNode[src] = new Dist(dp[0]); isMemo[src] = true;
        memoSkip[src] = new Dist(dp[1]); isMemoSkip[src] = true;
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

class Node implements Comparable<Node> {
    public int node;
    public long L;
    public long S;
    public boolean skip;
 
    // 3 parameter constructor
    public Node() {}
    public Node(int node, long L, long S) {
        this.node = node;
        this.L = L;
        this.S = S;
        this.skip = false;
    }
    public Node(int node, long L, long S, boolean skip) {
        this.node = node;
        this.L = L;
        this.S = S;
        this.skip = skip;
    }

    @Override 
    public int compareTo(Node other) {
        if (this.L < other.L)
            return -1;
        if (this.L > other.L)
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
                return other.destination - this.destination;
            }
            return  other.start - this.start; 
        }
        return this.cost - other.cost;
    }
}

// Union Find Disjoint Set for Kruskal's Algorithm
class UnionFind {
    private int[] p, rank, setSize;
    private int numSets;
 
    public UnionFind(int N) {
        p = new int[numSets = N];
        rank = new int[N];
        setSize = new int[N];
        for (int i = 0; i < N; i++) {
            p[i] = i;
            setSize[i] = 1;
        }
    }
 
    public int findSet(int i) {
        return p[i] == i ? i : (p[i] = findSet(p[i]));
    }
 
    public boolean isSameSet(int i, int j) {
        return findSet(i) == findSet(j);
    }
 
    public void unionSet(int i, int j) {
        if (isSameSet(i, j))
            return;
        numSets--;
        int x = findSet(i), y = findSet(j);
        if (rank[x] > rank[y]) {
            p[y] = x;
            setSize[x] += setSize[y];
        } else {
            p[x] = y;
            setSize[y] += setSize[x];
            if (rank[x] == rank[y])
                rank[y]++;
        }
    }
 
    public int numDisjointSets() {
        return numSets;
    }
 
    public int sizeOfSet(int i) {
        return setSize[findSet(i)];
    }
}

class MinHeap {
	ArrayList<Node> data;

	public MinHeap() {
		data = new ArrayList<Node>();
	}

	public MinHeap(ArrayList<Node> arr) {
		data = arr;
		heapify();
	}

    public boolean isEmpty() {
        return data.isEmpty();
    }

	public Node peek() {
		if (data.isEmpty())
			return null;
		return data.get(0);
	}

	public void insert(Node value) {
		data.add(value);
		percolateUp(data.size() - 1);
	}

	public Node remove() {
		Node removedObject = peek();

		if (data.size() == 1)
			data.clear();
		else {
			data.set(0, data.get(data.size() - 1));
			data.remove(data.size() - 1);
			percolateDown(0);
		}

		return removedObject;
	}

	private void percolateDown(int idx) {
		Node node = data.get(idx);
		int heapSize = data.size();

		while (true) {
			int leftIdx = getLeftChildIdx(idx);
			if (leftIdx >= heapSize) {
				data.set(idx, node);
				break;
			} else {
				int minChildIdx = leftIdx;
				int rightIdx = getRightChildIdx(idx);
				if (rightIdx < heapSize && data.get(rightIdx).compareTo(data.get(leftIdx)) < 0)
					minChildIdx = rightIdx;

				if (node.compareTo(data.get(minChildIdx)) > 0) {
					data.set(idx, data.get(minChildIdx));
					idx = minChildIdx;
				} else {
					data.set(idx, node);
					break;
				}
			}
		}
	}

	private void percolateUp(int idx) {
		Node node = data.get(idx);
		int parentIdx = getParentIdx(idx);
		while (idx > 0 && node.compareTo(data.get(parentIdx)) < 0) {
			data.set(idx, data.get(parentIdx));
			idx = parentIdx;
			parentIdx = getParentIdx(idx);
		}

		data.set(idx, node);
	}

	private int getParentIdx(int i) {
		return (i - 1) / 2;
	}

	private int getLeftChildIdx(int i) {
		return 2 * i + 1;
	}

	private int getRightChildIdx(int i) {
		return 2 * i + 2;
	}

	private void heapify() {
		for (int i = data.size() / 2 - 1; i >= 0; i--)
			percolateDown(i);
	}

	public void sort() {
		int n = data.size();
		while (n > 1) {
			data.set(n - 1, remove(n));
			n--;
		}
	}

	public Node remove(int n) {
		Node removedObject = peek();

		if (n > 1) {
			data.set(0, data.get(n - 1));
			percolateDown(0, n - 1);
		}

		return removedObject;
	}

	private void percolateDown(int idx, int n) {
		Node node = data.get(idx);
		int heapSize = n;

		while (true) {
			int leftIdx = getLeftChildIdx(idx);
			if (leftIdx >= heapSize) {
				data.set(idx, node);
				break;
			} else {
				int minChildIdx = leftIdx;
				int rightIdx = getRightChildIdx(idx);
				if (rightIdx < heapSize && data.get(rightIdx).compareTo(data.get(leftIdx)) < 0)
					minChildIdx = rightIdx;

				if (node.compareTo(data.get(minChildIdx)) > 0) {
					data.set(idx, data.get(minChildIdx));
					idx = minChildIdx;
				} else {
					data.set(idx, node);
					break;
				}
			}
		}
	}
}

// Class untuk menyimpan distance
class Dist {
    long[] dist;
    Dist (long[] dist) {
        this.dist = dist;
    }
}

// References:
// 1) https://www.geeksforgeeks.org/merge-sort/
