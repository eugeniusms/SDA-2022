
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
    static MinHeap<Node> mh;
    // Number of vertices
    static int V;
    static List<List<Node> > adj;

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
        int N = in.nextInt();
        int VE = 1+N; // ex: 0+8 nodes = 9 nodes
        
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
            int A = in.nextInt(); int B = in.nextInt(); long W = in.nextInt(); long S = in.nextInt();
            // karena dijkstra undirected berlaku dua arah
            adj.get(A).add(new Node(B, W, S));
            adj.get(B).add(new Node(A, W, S));
        }

        // ================================= INISIASI NODE DENGAN KURCACI ===========================
        int P = in.nextInt();
        int[] pos = new int[P];
        for (int i = 0; i < P; i++) {
            pos[i] = in.nextInt();
        }
        // Implement multisource destination
        // Menyambungkan node 0 dalam edge dengan bobot 0 terhubung ke setiap attacked (node 0 udah ada dalam 0 <- V)
        for (int i = 0; i < P; i++) {
            adj.get(0).add(new Node(pos[i], 0, 0));
        }

        // ================================= DEBUG ===========================
        out.println("BEFORE DIJKSTRA: ");
        int counter = 0;
        for (List<Node> l : adj) {
            out.println("FOR["+counter+"]");
            for (Node n : l) {
                out.print(n.node+"[L:"+n.L+"] ");
            }
            counter++;
            out.println("\n");
        }
        // ===================================================================

        for (int i = 0; i < P; i++) {
            System.out.println("POS: "+pos[i]);
            // Calculating the single source shortest path
            // Call Dijkstra
            inisiateDijkstra(VE); // RESET
            dijkstra(pos[i]);

            // System.out.println(attacked[i]);
            long[] temp = new long[10069];
            for (int j = 1; j < V; j++) { // mencari distance ke benteng yang diserang
                System.out.println("CEK attacked: "+j+ " "+dist[j]); // TEST 
                temp[j] = dist[j];
            }
            memo.add(temp);
        }

        // ================================= INPUT QUERY ============================================
        int Q = in.nextInt();
        for (int i = 0; i < Q; i++) {
            String query = in.next();
            if (query.equals("SIMULASI")) {
                SIMULASI();
            }
        }
        out.close();    
    }

    // =========================== DIJKSTRA ==================================  
    // inisiate Dijkstra
    static void inisiateDijkstra(int v) {
        V = v;
        dist = new long[v];
        settled = new HashSet<Integer>();
        mh = new MinHeap<Node>();
    }

    // Method 1
    // Dijkstra's Algorithm
    static void dijkstra(int src) {
        for (int i = 0; i < V; i++)
            dist[i] = Long.MAX_VALUE;
        mh.insert(new Node(src, 0, 0));
        dist[src] = 0;
        while (settled.size() != V) {
            if (mh.isEmpty())
                return;
            int u = mh.remove().node;
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
                edgeDistance = v.L;
                newDistance = dist[u] + edgeDistance;
                if (newDistance < dist[v.node])
                    dist[v.node] = newDistance;
                mh.insert(new Node(v.node, dist[v.node], v.S));
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
        for (long[] memoPos : memo) {
            // find min between memoPos
            long minTime = Long.MAX_VALUE;
            for (int g : gate) {
                if (memoPos[g] < minTime) {
                    minTime = memoPos[g];
                }
            }
            // update maxTime
            if (minTime > maxTime) {
                maxTime = minTime;
            }
        }

        out.println(maxTime);
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
                return this.destination - other.destination;
            }
            return this.start - other.start; 
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