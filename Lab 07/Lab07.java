import java.io.*;
import java.util.StringTokenizer;

public class Lab07 {
    private static InputReader in;
    private static PrintWriter out;

    static long V;

    public static void main(String[] args) {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        int N = in.nextInt(), M = in.nextInt(); V = N;

        for (int i = 1; i <= N; i++) {
            // TODO: Inisialisasi setiap benteng
        }

        for (int i = 0; i < M; i++) {
            int F = in.nextInt();
            // TODO: Tandai benteng F sebagai benteng diserang
        }

        int E = in.nextInt();
        for (int i = 0; i < E; i++) {
            int A = in.nextInt(), B = in.nextInt(), W = in.nextInt();
            // TODO: Inisialisasi jalan berarah dari benteng A ke B dengan W musuh
        }

        int Q = in.nextInt();
        while (Q-- > 0) {
            int S = in.nextInt(), K = in.nextInt();
            // TODO: Implementasi query
        }

        out.close();
    }

    static void djikstra(s) {
        long[] dist = new long[V+1];
        boolean[] visited = new boolean[V+1];
        long[] pred = new long[V+1];

        Arrays.fill(dist, Integer.MAX_VALUE); // HATI2 CEK LAGI INTEGER.MAX_VALUE VS INFINITY
        Arrays.fill(visited, false);
        Arrays.fill(pred, -1);

        MinHeap heap = new MinHeap<>();
        heap.insert(<0,s>);
        dist[s] = 0;

        while (!heap.isEmpty()) {
            // hcurDist,ui ← POP()

            if (!visited[u]) {
                visited[u] = true;
                for (v in adj(u)) {
                    if (dist[v] > dist[u] + w[u][v]) {
                        dist[v] = dist[u] + w[u][v];
                        pred[v] = u;
                        // heap.insert(hdist[v], vi);
;                    }
                }
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

class MinHeap {
    Saham[] Heap;
    int size;
    private int maxsize;
 
    // Constructor to initialize an
    // empty max heap with given maximum
    // capacity
    MinHeap(int maxsize) {
        // This keyword refers to current instance itself
        this.maxsize = maxsize;
        this.size = 0;
        Heap = new Saham[this.maxsize];
    }
 
    public boolean isEmpty() {
        return (this.size == 0);
    }

    // Returning position of parent
    private int parent(int pos) { return (pos - 1) / 2; }
 
    // Returning left children
    private int leftChild(int pos) { return (2 * pos) + 1; }

    // Returning right children
    private int rightChild(int pos) { return (2 * pos) + 2; }
 
    // Returning true of given node is leaf
    private boolean isLeaf(int pos) { return (pos >= (size / 2) && pos <= size); }
 
    // Swapping nodes
    private void swap(int fpos, int spos) {
        Saham tmp;
        tmp = Heap[fpos];
        Heap[fpos] = Heap[spos];
        Heap[spos] = tmp;
    }
 
    // Recursive function to min heapify given subtree
    private void minHeapify(int pos) {
        if (isLeaf(pos))
            return;
 
        // saat child ada yang lebih kecil dari parentnya maka lakukan penukaran sesuai child yang lebih kecil
        if (Heap[leftChild(pos)].isLessThan(Heap[pos])
            || Heap[rightChild(pos)].isLessThan(Heap[pos])) {
 
            // jika child kiri lebih kecil dari child kanan maka lakukan penukaran dengan child kiri
            if (Heap[leftChild(pos)].isLessThan(Heap[rightChild(pos)])) {
                swap(pos, leftChild(pos));
                minHeapify(leftChild(pos));
            } else { // jika child kanan lebih kecil dari child kiri maka lakukan penukaran dengan child kanan
                swap(pos, rightChild(pos));
                minHeapify(rightChild(pos));
            }
        }
    }
 
    // Inserts a new element to min heap
    public void insert(Saham element) {
        Heap[size] = element;
 
        // Traverse up and fix violated property
        int current = size;
        while (Heap[current].isLessThan(Heap[parent(current)])) {
            swap(current, parent(current));
            current = parent(current);
        }
        size++;
    }
 
    // To display heap
    public void print() {
 
        System.out.println("\nMINHEAP: ");
        for (int i = 0; i < size; i++) {
            System.out.println("["+i+"]: "+Heap[i]);
        }
        System.out.println(" ");
    }

    // Remove an element from min heap
    public Saham extractMin() {
        Saham popped = Heap[0];
        Heap[0] = Heap[--size];
        minHeapify(0);
        return popped;
    }

    public Saham getMin() {
        return Heap[0];
    }

    // insertion sort from min to max in Heap
    void sort(){
        int n = size;
        for (int i = 1; i < n; ++i) {
            Saham key = Heap[i];
            int j = i - 1;
            
            while (j >= 0 && key.isLessThan(Heap[j])) {
                Heap[j + 1] = Heap[j];
                j = j - 1;
            }
            Heap[j + 1] = key;
        }
    }
}
