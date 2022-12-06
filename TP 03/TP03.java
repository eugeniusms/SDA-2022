
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
    // static long dist[];
    // static List<Integer> settled;
    // static MinHeap<Node> mh;
    // Number of vertices
    static int V; // num of nodes
    static List<List<Node> > adj;
    static List<Edge> edges;

    // key: dist, value: array[1069] node
    // static ArrayList<long[]> memo = new ArrayList<long[]>();

    // memo shortest path by node
    static int[] kurcaci;
    static DistMax[] memoByNode = new DistMax[1069]; // [node] => [array of dist, array of max]
    static boolean[] isMemo = new boolean[1069]; // [node]

    public static void main(String arg[]) {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        // ================================= INISIASI GRAPH ===========================================
        // N = number of vertices
        // M = number of vertices that ! (attacked)
        int N = in.nextInt();
        V = 1+N; // ex: 0+8 nodes = 9 nodes
        
        // ================================= INISIASI EDGE ===========================================
        int E = in.nextInt();
        // Adjacency list untuk edge yang ada
        adj = new ArrayList<List<Node> >();
        // Initialize list for every node
        for (int i = 0; i < V; i++) {
            List<Node> item = new ArrayList<Node>();
            adj.add(item);
        }
        // Mendaftarkan edge yang ada melalui input
        for (int i = 0; i < E; i++) {
            int A = in.nextInt(); int B = in.nextInt(); long W = in.nextInt(); long S = in.nextInt();
            // karena dijkstra undirected berlaku dua arah
            adj.get(A).add(new Node(B, W, S, 0));
            adj.get(B).add(new Node(A, W, S, 0));
        }

        // ================================= INISIASI MAX SPANNING TREE ===============================
        findMaximumSpanningTree(V);

        // ================================= INISIASI NODE DENGAN KURCACI ===========================
        int P = in.nextInt();
        kurcaci = new int[P];
        for (int i = 0; i < P; i++) {
            kurcaci[i] = in.nextInt();
        }
        // // Implement multisource destination
        // // Menyambungkan node 0 dalam edge dengan bobot 0 terhubung ke setiap attacked (node 0 udah ada dalam 0 <- V)
        // for (int i = 0; i < P; i++) {
        //     adj.get(0).add(new Node(kurcaci[i], 0, 0));
        // }

        // ================================= DEBUG ===========================
        // out.println("BEFORE DIJKSTRA: ");
        // int counter = 0;
        // for (List<Node> l : adj) {
        //     out.println("FOR["+counter+"]");
        //     for (Node n : l) {
        //         out.print(n.node+"[L:"+n.L+"] ");
        //     }
        //     counter++;
        //     out.println("\n");
        // }
        // ===================================================================


        dijkstra(1);

        for (int i = 0; i < P; i++) {
            // System.out.println("POS: "+pos[i]);
            // Calculating the single source shortest path
            // Call Dijkstra
            // inisiateDijkstra(VE); // RESET
            // dijkstra(pos[i]);
            // System.out.println(kurcaci[i]);
            DistMax DM = dijkstraSuper(kurcaci[i]);
            
            // print dist on DM
            // for (int j = 0; j < V; j++) {
            //     System.out.print(DM.dist[j]+" ");
            // }
            // System.out.println();
            // // print max on DM
            // for (int j = 0; j < V; j++) {
            //     System.out.print(DM.max[j]+" ");
            // }

            // System.out.println(attacked[i]);
            // long[] temp = new long[10069];
            // for (int j = 1; j < V; j++) { // mencari distance ke benteng yang diserang
            //     // System.out.println("CEK attacked: "+j+ " "+dist[j]); // TEST 
            //     temp[j] = dist[j];
            // }
            // memo.add(temp);
            memoByNode[kurcaci[i]] = DM; // memoize
            isMemo[kurcaci[i]] = true; // memoized
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
                SUPER();
            }
        }
        out.close();    
    }

    // =========================== DIJKSTRA ==================================  
    // inisiate Dijkstra
    // static void inisiateDijkstra(int v) {
    //     V = v;
    //     dist = new long[v];
    //     settled = new ArrayList<Integer>();
    //     mh = new MinHeap<Node>();
    // }

    // Method 1
    // Dijkstra's Algorithm
    // static void dijkstra(int src) {
    //     for (int i = 0; i < V; i++)
    //         dist[i] = Long.MAX_VALUE;
    //     mh.insert(new Node(src, 0, 0));
    //     dist[src] = 0;
    //     while (settled.size() != V) {
    //         if (mh.isEmpty())
    //             return;
    //         int u = mh.remove().node;
    //         if (settled.contains(u))
    //             continue;
    //         settled.add(u);
    //         e_Neighbours(u);
    //     }
    // }

    // // Method 2
    // static void e_Neighbours(int u) {
    //     long edgeDistance = -1;
    //     long newDistance = -1;
    //     for (int i = 0; i < adj.get(u).size(); i++) {
    //         Node v = adj.get(u).get(i);
    //         if (!settled.contains(v.node)) {
    //             edgeDistance = v.L;
    //             newDistance = dist[u] + edgeDistance;
    //             if (newDistance < dist[v.node])
    //                 dist[v.node] = newDistance;
    //             mh.insert(new Node(v.node, dist[v.node], v.S));
    //         }
    //     }
    // }

    // QUERY 1 : KABUR
    static void KABUR() {
        int source = in.nextInt(); int destination = in.nextInt();

        // Memanfaatkan Kruskal's Maximum Spanning Tree (MST))
        // Gunakan DFS untuk mencari path dari source ke destination
        visited = new boolean[V]; // reset
        DFS(source, destination, new Stack<Integer>());

        // TODO: CARI MINL DI DALAM PATH TERSEBUT
    }

    static void getMinL(Vector<Integer> stack) {
        long minL = Long.MAX_VALUE;
        for(int i = 0; i < stack.size() - 1; i++) {
            // System.out.println(stack.get(i)+" "+stack.get(i+1));
            long l = spanningTreeEdges[stack.get(i)][stack.get(i+1)];
            if (l < minL) {
                minL = l;
            }
        }
        out.println(minL);
    }

    // https://www.geeksforgeeks.org/print-the-path-between-any-two-nodes-of-a-tree-dfs/
    // static void printPath(Vector<Integer> stack) {
    //     for(int i = 0; i < stack.size() - 1; i++) {
    //         System.out.print(stack.get(i) + " -> ");
    //     }
    //     System.out.println(stack.get(stack.size() - 1));
    // }

    static boolean[] visited;
    static void DFS(int x, int y, Vector<Integer> stack) {
        stack.add(x);
        if (x == y) {
            // print the path and return on
            // reaching the destination node
            // printPath(stack);
            getMinL(stack);
            return;
        }
        visited[x] = true;
        // if backtracking is taking place     
        if (spanningTree.get(x).size() > 0) {
            for(int j = 0; j < spanningTree.get(x).size(); j++) {
                // if the node is not visited
                if (visited[spanningTree.get(x).get(j).node] == false) {
                    DFS(spanningTree.get(x).get(j).node, y, stack);
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
            List<Node> item = new ArrayList<Node>();
            spanningTree.add(item);
        }
        // Melakukan pencarian max spanning tree
        // print adj (adj adalah graf penyimpan edge)        
        // Kruskal's Algorithms
        // get all edges
        edges = new ArrayList<Edge>();
        for (int i = 0; i < adj.size(); i++) {
            for (int j = 0; j < adj.get(i).size(); j++) {
                // System.out.println("CEK: "+i+" "+adj.get(i).get(j).node+" "+adj.get(i).get(j).L);
                edges.add(new Edge(i, adj.get(i).get(j).node, (int) adj.get(i).get(j).S));
            }
        }
        // STEP 1 Kruskal's : Sorting edges
        // Sorting Edges with Bubble Sort
        for (int i = 0; i < edges.size(); i++) {
            for (int j = i+1; j < edges.size(); j++) {
                if (edges.get(i).compareTo(edges.get(j)) < 0) {
                    Edge temp = edges.get(i);
                    edges.set(i, edges.get(j));
                    edges.set(j, temp);
                }
            }
        }
        // System.out.println("SORTED LIST: ");
        // for (Edge e : edges) {
        //     System.out.println("CEK: "+e.start+" "+e.destination+" "+e.cost);
        // }
        // STEP 2 Kruskal's : Check cycle
        // Check cycle with Union Find
        UnionFind uf = new UnionFind(v);
        for (Edge e : edges) {
            if (!uf.isSameSet(e.start, e.destination)) {
                uf.unionSet(e.start, e.destination);
                spanningTree.get(e.start).add(new Node(e.destination, e.cost, 0, 0));
                spanningTree.get(e.destination).add(new Node(e.start, e.cost, 0, 0));
            }
        }
        // print spanningTree
        // System.out.println("SPANNING TREE: ");
        // input spanningTreeEdges
        for (int i = 0; i < spanningTree.size(); i++) {
            // System.out.print(i+" : ");
            for (Node n : spanningTree.get(i)) {
                // System.out.print(n.node+"["+n.L+"]"+" "); 
                spanningTreeEdges[i][n.node] = n.L;
            }
            // System.out.println();
        }
        // SEPERTINYA SUDAH DAPAT SPANNING TREE
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
        // by pos kurcaci
        for (int pos : kurcaci) {
            DistMax dm = memoByNode[pos];
            // find min
            long minTime = Long.MAX_VALUE;
            for (int g : gate) {
                if (dm.dist[g] < minTime) {
                    minTime = dm.dist[g];
                }
            }
            // findMax
            if (minTime > maxTime) {
                maxTime = minTime;
            }
        }
        // for (DistMax dm : memoByNode) {
        //     System.out.println(dm);
        //     // find min
        //     long minTime = Long.MAX_VALUE;
        //     for (int g : gate) {
        //         if (dm.dist[g] < minTime) {
        //             minTime = dm.dist[g];
        //         }
        //     }
        //     // find max
        //     if (minTime > maxTime) {
        //         maxTime = minTime;
        //     }
        // }

        out.println(maxTime);
    }

    // QUERY 3 : SUPER
    // IDE : DIJKSTRA 1x dengan modifikasi tambahan array maximal cost between T -> v (v = 1..V) 
    // Then, cek D[s] & M[s] dan D[t] & M[t]
    static void SUPER() {
        int s = in.nextInt(); int t = in.nextInt(); int x = in.nextInt();
        // find D(t,v) == D(v,t)
        DistMax T;
        if (isMemo[t]) { // isMemo
            T = memoByNode[t]; 
        } else {
            T = dijkstraSuper(t);
            memoByNode[t] = T; // memo [array of dist, array of max]
            isMemo[t] = true;
        }
        
        // TESTING
        // print S
        // System.out.println("D(s,u) with u is all other nodes");
        // for (int i = 1; i < S.length; i++) {
        //     System.out.print(S[i]+" ");
        // }
        // System.out.println();
        // // print T
        // System.out.println("D(t,w) with u is all other nodes");
        // for (int i = 1; i < T.length; i++) {
        //     System.out.print(T[i]+" ");
        // }
        // System.out.println();
        // // // print DV3
        // for (int i = 1; i < X.length; i++) {
        //     System.out.print(X[i]+" ");
        // }
        // System.out.println();

        // find minCost (s,t)
        // operate min (D(s,u) - D(w,t)) for all edges (u,w)
        // long minCostST = Long.MAX_VALUE;
        // for (Edge e : edges) {
        //     long cost = S[e.start] + T[e.destination]; // (D(s,u) - D(w,t))
        //     if (cost < minCostST) {
        //         minCostST = cost;
        //     }
        // }
        // find minCost (t,x)
        // operate min (D(t,u) - D(w,x)) for all edges (u,w)
        // long minCostTX = Long.MAX_VALUE;
        // for (Edge e : edges) {
        //     long cost = T[e.start] + X[e.destination]; // (D(t,u) - D(w,x))
        //     if (cost < minCostTX) {
        //         minCostTX = cost;
        //     }
        // }

        // System.out.println("Min(D(s,u) + D(w,t)) for all edges (u,w): "+minCostST);
        // System.out.println("Min(D(t,u) + D(w,x)) for all edges (u,w): "+minCostTX);

        // Melakukan comparing combine
        // S -> skipped -> T -> X
        long versi1 = T.dist[s] - T.max[s] + T.dist[x];
        // S -> T -> skipped -> X
        long versi2 = T.dist[s] + T.dist[x] - T.max[x];
        // Mencetak yang terkecil di antara kedua versi
        if (versi1 <= versi2) {
            out.println(versi1);
        } else {
            out.println(versi2);
        }
    }

    // Method 1
    // Dijkstra's Algorithm // return DistMax [array of D, array of M]
    static DistMax dijkstraSuper(int src) { // return src(v) to all nodes
        // inisiate
        long[] D = new long[V]; // array of distance from src to all nodes
        long[] M = new long[V]; // array of maximal edge .L (cost) from src to all nodes
        List<Integer> sett = new ArrayList<Integer>();
        MinHeap<Node> minHeap = new MinHeap<Node>();
        // dijkstra
        for (int i = 0; i < V; i++) {
            D[i] = Long.MAX_VALUE;
            M[i] = Long.MIN_VALUE;
        }
        minHeap.insert(new Node(src, 0, 0, 0));
        D[src] = 0;
        M[src] = 0;
        while (sett.size() != V) {
            if (minHeap.isEmpty())
                break;
            int u = minHeap.remove().node;
            if (sett.contains(u))
                continue;
            sett.add(u);
            // e_neighbours
            long edgeDistance = -1;
            long newDistance = -1;
            long newMaximal = -1;
            for (int i = 0; i < adj.get(u).size(); i++) {
                Node v = adj.get(u).get(i);
                if (!sett.contains(v.node)) {
                    edgeDistance = v.L;
                    newDistance = D[u] + edgeDistance;
                    newMaximal = Math.max(M[u], edgeDistance);
                    // if (newDistance - M[u] < D[v.node])
                    //     D[v.node] = newDistance;
                    if (newMaximal > M[v.node])
                        D[v.node] = newDistance;
                        M[v.node] = newMaximal;
                    minHeap.insert(new Node(v.node, D[v.node], v.S, 0));
                }
            }
        }

        // print D
        // System.out.println("D(v,u) with u is all other nodes");
        // for (int i = 0; i < D.length; i++) {
        //     System.out.print(D[i]+" ");
        // }
        // System.out.println();
        // // // print M
        // System.out.println("M(v,u) with u is all other nodes");
        // for (int i = 0; i < M.length; i++) {
        //     System.out.print(M[i]+" ");
        // }
        DistMax DM = new DistMax(D, M); // gabungkan D and M on DM
        System.out.println("SOURCE: "+src);
        // print dist on DM
        System.out.println("D(src) to is all other nodes");
        for (int i = 1; i < DM.dist.length; i++) {
            System.out.print(DM.dist[i]+" ");
        }
        System.out.println();
        // print max on DM
        System.out.println("M(src) to all other nodes");
        for (int i = 1; i < DM.max.length; i++) {
            System.out.print(DM.max[i]+" ");
        }
        System.out.println();
        return DM;
    }

    static long[][] dp = new long[10][2];
    static void dijkstra(int src){
        // fill dp with infinity
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 2; j++) {
                dp[i][j] = Long.MAX_VALUE;
            }
        }
        dp[src][0] = 0; // distance from src to src after using 0 coupon is 0
        MinHeap<Node> mh = new MinHeap<Node>();
        mh.insert(new Node(src, 0, 0, 0));
        
        Node tp;
        int from, to, k;
        long dist, edgew;
        while(!mh.isEmpty()){
            tp = mh.remove();
            from = tp.node; k = tp.k; dist = tp.L;
            if(dp[from][k] < dist) continue;
    
            for(Node p: adj.get(from)){
                to = p.node; edgew = p.L;
                if(dist + edgew < dp[to][k]){
                    dp[to][k] = dist + edgew;
                    mh.insert(new Node(to, k, dp[to][k], k));
                }   
    
    
                if(k+1 <= 1 && dist < dp[to][k+1]){
                    dp[to][k+1] = dist;
                    mh.insert(new Node(to, k+1, dp[to][k+1], k+1));
                }
            }
        }
        // print DP
        System.out.println("DP(v,u) with u is all other nodes");
        for (int i = 0; i < dp.length; i++) {
            for (int j = 0; j < dp[i].length; j++) {
                System.out.print(dp[i][j]+" ");
            }
            System.out.println();
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
    public int k;
    // public boolean isKurcaciExist = false;
 
    public Node() {}
    public Node(int node, long L, long S, int k) {
        this.node = node;
        this.L = L;
        this.S = S;
        this.k = k;
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

// Class untuk menyimpan array Dist & array Max pada suatu node yang telah di Dijkstra
class DistMax {
    long[] dist;
    long[] max;

    public DistMax(long[] dist, long[] max) {
        this.dist = dist;
        this.max = max;
    }
}

// KEKNYA REFERENSINYA YG INI GA SI:
// 1) https://codeforces.com/blog/entry/70589