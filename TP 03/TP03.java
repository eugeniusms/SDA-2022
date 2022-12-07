
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

    // memo kabur
    static Dist[] memoKabur = new Dist[1069];
    static boolean[] isMemoKabur = new boolean[1069];

    // memo shortest path by node
    static ArrayList<Integer> kurcaci = new ArrayList<Integer>();
    static Dist[] memoByNode = new Dist[1069]; // [node][Dist[i]]
    static boolean[] isMemo = new boolean[1069]; // [node]

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
                SUPER();
            }
        }
        out.close();    
    }

    // QUERY 1 : KABUR
    static void KABUR() {
        int source = in.nextInt(); int destination = in.nextInt();
        if (isMemoKabur[source]) {
            out.println(memoKabur[source].dist[destination]);
        } else {
            // saat belum ada di dalam memo
            // inisiate
            long[] D = new long[V];
            List<Integer> green = new ArrayList<Integer>();
            MaxHeap maxHeap = new MaxHeap();
            // dijkstra
            for (int i = 0; i < V; i++)
                D[i] = Long.MIN_VALUE;

            maxHeap.insert(new Node(source, 0, Long.MAX_VALUE));
            D[source] = 0;
            while (green.size() != V) {
                if (maxHeap.isEmpty())
                    break;
                int u = maxHeap.remove().node;
                if (green.contains(u))
                    continue;
                green.add(u);
                // e_neighbours
                long edgeDistance = -1;
                long newDistance = -1;
                for (int i = 0; i < adj.get(u).size(); i++) {
                    Node v = adj.get(u).get(i);
                    if (!green.contains(v.node)) {
                        edgeDistance = v.S;
                        newDistance = Math.min(D[u],edgeDistance);
                        if (newDistance == 0) {
                            newDistance = edgeDistance;
                        }
                        if (newDistance > D[v.node])
                            D[v.node] = newDistance;
                        maxHeap.insert(new Node(v.node, v.L , D[v.node]));
                        }
                }
            }
            out.println(D[destination]);
            // memo kabur
            Dist dist = new Dist(D);
            memoKabur[source] = dist;
            isMemoKabur[source] = true;
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

    // QUERY 3 : SUPER
    static void SUPER() { // manfaatin dijkstra dp
        int s = in.nextInt(); int t = in.nextInt(); int x = in.nextInt();

        // CEK SUDAH PERNAH DIMEMO DI SUPER BELUM
        long[][] dp; MinHeap<Node> minHeap;
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
            minHeap = new MinHeap<Node>();
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

    public int compareToS(Node other) {
        if (this.S < other.S)
            return -1;
        if (this.S > other.S)
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

class MaxHeap {
	ArrayList<Node> data;

	public MaxHeap() {
		data = new ArrayList<Node>();
	}

	public MaxHeap(ArrayList<Node> arr) {
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
				if (rightIdx < heapSize && data.get(rightIdx).compareToS(data.get(leftIdx)) > 0)
					minChildIdx = rightIdx;

				if (node.compareToS(data.get(minChildIdx)) < 0) {
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
		while (idx > 0 && node.compareToS(data.get(parentIdx)) > 0) {
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
				if (rightIdx < heapSize && data.get(rightIdx).compareToS(data.get(leftIdx)) > 0)
					minChildIdx = rightIdx;

				if (node.compareToS(data.get(minChildIdx)) < 0) {
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