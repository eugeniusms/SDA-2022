
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
    static int V;
    static List<List<Node> > adj;
    static List<Edge> edges;

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

        // Inisiate Dijkstra First Time
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
                KABURI();
            } else if (query.equals("SIMULASI")) {
                SIMULASI();
            } else {
                SUPER();
            }
        }
        out.close();    
    }

    static void KABURI() {
        int source = in.nextInt(); int destination = in.nextInt();
        // inisiate
        long[] D = new long[V];
        List<Integer> green = new ArrayList<Integer>();
        MaxHeap<Node> maxHeap = new MaxHeap<Node>();
        // dijkstra
        for (int i = 0; i < V; i++)
            D[i] = Long.MIN_VALUE;

        // input tetangga dari source ke dalam maxheap
        // for (Node node : adj.get(source)) {
        //     maxHeap.insert(node);
        // }
        maxHeap.insert(new Node(source, 0, Long.MAX_VALUE));
        D[source] = 0;
        while (green.size() != V) {
            if (maxHeap.isEmpty())
                break;
            int u = maxHeap.remove().node;
            if (green.contains(u))
                continue;
            green.add(u);
            out.println("MENGGREEN: " + u + " dengan D[" + u + "] = " + D[u]);
            // e_neighbours
            long edgeDistance = -1;
            long newDistance = -1;
            for (int i = 0; i < adj.get(u).size(); i++) {
                Node v = adj.get(u).get(i);
                if (!green.contains(v.node)) {
                    out.println(v.node + " | " + v.S + " | " + D[u]);
                    // newDistance = Math.min(D[u], v.S);

                    // if (newDistance <= 0) {
                    //     newDistance = v.S;
                    // }
                    // if (D[v.node] < newDistance) {
                    //     D[v.node] = newDistance;
                    // }
                    // // out.println("newDistance: " + newDistance);
                    // maxHeap.insert(new Node(v.node, v.L , newDistance));

                    // print D
                    // for (int j = 0; j < V; j++) {
                    //     out.println(j+" : ["+D[j]+"]");
                    edgeDistance = v.S;
                    newDistance = Math.min(D[u],edgeDistance);
                    if (newDistance == 0) {
                        newDistance = edgeDistance;
                    }
                    if (newDistance > D[v.node])
                        D[v.node] = newDistance;
                    maxHeap.insert(new Node(v.node, v.L , D[v.node]));
                    }
                // }

            // print D
            out.println("ITERASI");
            for (int j = 1; j < V; j++) {
                out.println(j+" : ["+D[j]+"]");
            }
            }
        }
        out.println(D[destination]);
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
            // System.out.println(stack.get(i)+" "+stack.get(i+1));
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
        List<Integer> sett = new ArrayList<Integer>();
        MinHeap<Node> minHeap = new MinHeap<Node>();
        // dijkstra
        for (int i = 0; i < V; i++)
            D[i] = Long.MAX_VALUE;
        minHeap.insert(new Node(src, 0, 0));
        D[src] = 0;
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
            for (int i = 0; i < adj.get(u).size(); i++) {
                Node v = adj.get(u).get(i);
                if (!sett.contains(v.node)) {
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
    // public boolean isKurcaciExist = false;
 
    public Node() {}
    public Node(int node, long L, long S) {
        this.node = node;
        this.L = L;
        this.S = S;
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

class MaxHeap<T extends Comparable<T>> {
	ArrayList<T> data;

	public MaxHeap() {
		data = new ArrayList<T>();
	}

	public MaxHeap(ArrayList<T> arr) {
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
				if (rightIdx < heapSize && data.get(rightIdx).compareTo(data.get(leftIdx)) > 0)
					minChildIdx = rightIdx;

				if (node.compareTo(data.get(minChildIdx)) < 0) {
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
		while (idx > 0 && node.compareTo(data.get(parentIdx)) > 0) {
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
				if (rightIdx < heapSize && data.get(rightIdx).compareTo(data.get(leftIdx)) > 0)
					minChildIdx = rightIdx;

				if (node.compareTo(data.get(minChildIdx)) < 0) {
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