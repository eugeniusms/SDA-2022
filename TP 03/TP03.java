
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

    // key: dist, value: array[1069] node
    // static ArrayList<long[]> memo = new ArrayList<long[]>();

    // memo shortest path by node
    static ArrayList<Integer> kurcaci = new ArrayList<Integer>();
    static Dist[] memoByNode = new Dist[1069]; // [node][Dist[i]]
    static boolean[] isMemo = new boolean[1069]; // [node]

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
            Dist dist = dijkstra(k);
            memoByNode[k] = dist; // memoize
            isMemo[k] = true; // memoized
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
                dijkstraSuper();
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
            // find min
            long[] dist = memoByNode[k].dist;
            long minTime = Long.MAX_VALUE;
            for (int g : gate) {
                if (dist[g] < minTime) {
                    minTime = dist[g];
                }
            }
            // find max
            if (minTime > maxTime) {
                maxTime = minTime;
            }
        }

        out.println(maxTime);
    }

    // QUERY 3 : SUPER

    static void SUPER() {
        int s = in.nextInt(); int t = in.nextInt(); int x = in.nextInt();
        // find D(s,v) == D(v,s)
        // cek isMemoized
        Dist S;
        if (isMemo[s]) {
            S = memoByNode[s]; 
        } else {
            S = dijkstra(s);
            memoByNode[s] = S; // memoize
            isMemo[s] = true; // memoized
        }
        // find D(t,v) == D(v,t)
        Dist T;
        if (isMemo[t]) {
            T = memoByNode[t]; 
        } else {
            T = dijkstra(t);
            memoByNode[t] = T; // memoize
            isMemo[t] = true; // memoized
        }
        // find D(x,v) == D(v,x)
        Dist X;
        if (isMemo[x]) {
            X = memoByNode[x];
        } else {
            X = dijkstra(x);
            memoByNode[x] = X; // memoize
            isMemo[x] = true; // memoized
        }

        // find minCost (s,t)
        // operate min (D(s,u) - D(w,t)) for all edges (u,w)
        long minCostST = Long.MAX_VALUE;
        for (Edge e : edges) {
            long cost = S.dist[e.start] + T.dist[e.destination]; // (D(s,u) - D(w,t))
            if (cost < minCostST) {
                minCostST = cost;
            }
        }
        // find minCost (t,x)
        // operate min (D(t,u) - D(w,x)) for all edges (u,w)
        long minCostTX = Long.MAX_VALUE;
        for (Edge e : edges) {
            long cost = T.dist[e.start] + X.dist[e.destination]; // (D(t,u) - D(w,x))
            if (cost < minCostTX) {
                minCostTX = cost;
            }
        }

        // Melakukan comparing combine
        // S -> skipped -> T -> X
        long versi1 = minCostST + T.dist[x];
        // S -> T -> skipped -> X
        long versi2 = S.dist[t] + minCostTX;
        // Mencetak yang terkecil di antara kedua versi
        if (versi1 <= versi2) {
            out.println(versi1);
        } else {
            out.println(versi2);
        }
    }

    // Method 1
    // Dijkstra's Algorithm
    static Dist dijkstra(int src) { // return Dist with isi [src[v]] to all nodes
        // inisiate
        long[] D = new long[V];
        List<Integer> green = new ArrayList<Integer>();
        MinHeap<Node> minHeap = new MinHeap<Node>();
        // dijkstra
        for (int i = 0; i < V; i++)
            D[i] = Long.MAX_VALUE;
        minHeap.insert(new Node(src, 0, 0));
        D[src] = 0;
        while (green.size() != V) {
            if (minHeap.isEmpty())
                break;
            int u = minHeap.remove().node;
            if (green.contains(u))
                continue;
            green.add(u);
            // e_neighbours
            long edgeDistance = -1;
            long newDistance = -1;
            for (int i = 0; i < adj.get(u).size(); i++) {
                Node v = adj.get(u).get(i);
                if (!green.contains(v.node)) {
                    edgeDistance = v.L;
                    newDistance = D[u] + edgeDistance;
                    if (newDistance < D[v.node])
                        D[v.node] = newDistance;
                    minHeap.insert(new Node(v.node, D[v.node], v.S));
                }
            }
        }
        Dist dist = new Dist(D);
        return dist;
    }

    static void dijkstraSuper() {
        int s = in.nextInt(); int t = in.nextInt();

        // dp[destination][state=0/1], 0 -> Take, 1 -> Skip
        long[][] dp = new long[V][2];
        for (int i = 0; i < V; i++) {
            dp[i][0] = Long.MAX_VALUE;
            dp[i][1] = Long.MAX_VALUE;
        }
        dp[s][0] = 0; // state 0 -> take
        dp[s][1] = 0; // state 1 -> skip
        // dijkstra with 1 is k-skip edge
        MinHeap<Node> minHeap = new MinHeap<Node>();
        minHeap.insert(new Node(s, 0, 0));
        while (!minHeap.isEmpty()) {
            Node start = minHeap.remove();
            // e_neighbours
            long edgeDistance = -1;
            long noSkip = -1;
            for (int i = 0; i < adj.get(start.node).size(); i++) { // untuk setiap edges di node u
                Node desti = adj.get(start.node).get(i); // ambil node tujuan
                edgeDistance = desti.L; // cost ke v
                // out.println("start: " + start.node + " desti: " + desti.node);
                if (start.skip) { // saat sudah pernah diskip
                    
                    long belumskip = dp[start.node][0]; // cost belum pernah skip tapi mencoba skip saat ini 
                    long sudahskip = dp[start.node][1] + edgeDistance; // cost sudah pernah skip + cost ke v (sudah tidak bisa diskip lagi)
                    if (Math.min(belumskip, sudahskip + edgeDistance) < dp[desti.node][1]) { // jika cost ke v lebih kecil dari cost sebelumnya
                        dp[desti.node][1] = Math.min(belumskip, sudahskip + edgeDistance); // update cost
                        minHeap.insert(new Node(desti.node, dp[desti.node][1], desti.S, true)); // masukkan ke minHeap
                    }

                } else { // belum pernah diskip
                    noSkip = dp[start.node][0] + edgeDistance;
                    // dijkstra biasa
                    // dijkstra ini biasa state[0] udah bener
                    if (noSkip < dp[desti.node][0]) {
                        dp[desti.node][0] = noSkip;
                        minHeap.insert(new Node(desti.node, dp[desti.node][0], desti.S, false));
                    }

                    // skip jika state 1 lebih kecil dari state 0
                    if (dp[desti.node][1] < noSkip) {
                        out.println("MASUK");
                        // newDistance = dp[start.node][1] + edgeDistance;
                        // if (newDistance < dp[desti.node][1]) {
                        //     dp[start.node][1] = newDistance;
                            minHeap.insert(new Node(desti.node, dp[desti.node][1], desti.S, true));
                        // }
                    }
                    
                }

            }
            out.println("DP TABLE: | NODE: " + start.node);
            for (int i = 1; i < V; i++) {
                out.println("dp[" + i + "][0] = " + dp[i][0]);
                out.println("dp[" + i + "][1] = " + dp[i][1]);
            }
        }   
                // if (!u.skip) { // Take
                //     edgeDistance = v.L;
                //     newDistance = dp[u.node][0] + edgeDistance;
                //     if (newDistance < dp[v.node][0]) {
                //         dp[v.node][0] = newDistance;
                //         minHeap.insert(new Node(v.node, dp[v.node][0], v.S, false));
                //     }
                //     newDistance = dp[u.node][0] + edgeDistance;
                //     if (newDistance < dp[v.node][1]) {
                //         dp[v.node][1] = newDistance;
                //         minHeap.insert(new Node(v.node, dp[v.node][1], v.S, true));
                //     }
                // } else { // Skip
                //     edgeDistance = v.L;
                //     newDistance = dp[u.node][1] + edgeDistance;
                //     if (newDistance < dp[v.node][1]) {
                //         dp[v.node][1] = newDistance;
                //         minHeap.insert(new Node(v.node, dp[v.node][1], v.S, true));
                //     }
                // }
            // }
        // }
        // print
        // long versi1 = dp[t][0] + dp[x][0];
        // long versi2 = dp[s][0] + dp[t][1] + dp[x][0];
        // print dp table
        // print final
        out.println("FINAL DP TABLE: ");
        for (int i = 1; i < V; i++) {
            out.println("dp[" + i + "][0] = " + dp[i][0]);
            out.println("dp[" + i + "][1] = " + dp[i][1]);
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

class MinHeap<T extends Comparable<T>> {
	ArrayList<T> data;

	public MinHeap() {
		data = new ArrayList<T>();
	}

	public MinHeap(ArrayList<T> arr) {
		data = arr;
		heapify();
	}

    public boolean isEmpty() {
        return data.isEmpty();
    }

	public T peek() {
		if (data.isEmpty())
			return null;
		return data.get(0);
	}

	public void insert(T value) {
		data.add(value);
		percolateUp(data.size() - 1);
	}

	public T remove() {
		T removedObject = peek();

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
		T node = data.get(idx);
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
		T node = data.get(idx);
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

	public T remove(int n) {
		T removedObject = peek();

		if (n > 1) {
			data.set(0, data.get(n - 1));
			percolateDown(0, n - 1);
		}

		return removedObject;
	}

	private void percolateDown(int idx, int n) {
		T node = data.get(idx);
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