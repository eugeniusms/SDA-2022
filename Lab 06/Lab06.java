import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.*;

public class Lab06 {

    private static InputReader in;
    private static PrintWriter out;

    static Saham sahamMedian;
    // inisiasi maxheap untuk data ke 1 - median
    static MaxHeap maxHeap = new MaxHeap(200069);
    // inisiasi minheap untuk data ke median - N
    static MinHeap minHeap = new MinHeap(200069);

    static Saham[] sahamById = new Saham[200069]; // save saham by id

    public static void main(String[] args) {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        int N = in.nextInt();

        // inisiasi saham ke dalam array untuk disort dahulu
        if (N > 0) {
            Saham[] initialSaham = new Saham[N]; // initial saham
            for (int i = 1; i <= N; i++) {
                int harga = in.nextInt();
                Saham saham = new Saham(i,harga);
                initialSaham[i-1] = saham;
                sahamById[i] = saham;
            }

            // sort saham
            Arrays.sort(initialSaham);

            // cek isi array
            // for(int i = 0; i < N; i++) {
            //     System.out.println("[HARGA: "+initialSaham[i].harga + " ID: " + initialSaham[i].id+"]"); // DEBUG
            // }

            // get median of initial saham
            int median = getIndexMedian(N);
            sahamMedian = initialSaham[median];
            // System.out.println("MEDIAN: [HARGA: "+sahamMedian.harga + " ID: " + sahamMedian.id+"]"); // DEBUG

            // insert node ke dalam heap
            for (int i = median-1; i >= 0; i--) { // berjalan dari kanan ke kiri (biar max duluan)
                maxHeap.insert(initialSaham[i]);
            }
            for (int i = median; i < N; i++) { // berjalan dari kiri ke kanan (biar min duluan)
                minHeap.insert(initialSaham[i]);
            }
        } else {
            // inisiasi sahamMedian saat N == 0
            sahamMedian = new Saham(0,0);
        }
        
        // input query
        int Q = in.nextInt();
        for (int i = 0; i < Q; i++) {
            String q = in.next();

            if (q.equals("TAMBAH")) {
                N += 1;
                int harga = in.nextInt();
                TAMBAH(N, harga);
            } else if (q.equals("UBAH")) {
                int nomorSeri = in.nextInt();
                int harga = in.nextInt();
                UBAH(nomorSeri, harga);
            }
        }

        // VIEW(); // DEBUG

        out.flush();
    }

    static int getIndexMedian (int length) {
        if (length % 2 == 0) {
            return length/2;
        } else {
            return (length-1)/2;
        }
    }

    static void TAMBAH(int id, int harga) {
        Saham sahamBaru = new Saham(id, harga);
        sahamById[id] = sahamBaru;
        // cek apakah harga saham baru lebih besar dari median
        if (sahamMedian.isLessThan(sahamBaru)) { // jika lebih besar maka masuk ke minHeap
            minHeap.insert(sahamBaru);
        } else { 
            maxHeap.insert(sahamBaru);
        }

        // transfer node dari heap yang lebih banyak ke heap yang kurang banyak
        if (minHeap.size - maxHeap.size == 2) { // saat selisih size heap = 2
            maxHeap.insert(minHeap.extractMin());
        } else if (maxHeap.size - minHeap.size == 2) { // saat selisih size heap = 2
            minHeap.insert(maxHeap.extractMax());
        } else if (maxHeap.size - minHeap.size == 1) { // saat selisih size heap = 1
            minHeap.insert(maxHeap.extractMax());
        } else {
            if (minHeap.size - maxHeap.size > 1) { // saat selisih size banyak
                while (minHeap.size - maxHeap.size > 1) {
                    maxHeap.insert(minHeap.extractMin());
                }
            }
            if (maxHeap.size - minHeap.size > 0) { // saat selisih size banyak
                while (maxHeap.size - minHeap.size > 0) {
                    minHeap.insert(maxHeap.extractMax());
                }
            }
        }

        // update median
        sahamMedian = minHeap.getMin();
        out.println(sahamMedian.id); // RESULT
    }

    static void UBAH(int nomorSeri, int harga) {
        // get saham by id
        Saham sahamDipilih = sahamById[nomorSeri];
        // cek apakah harga saham baru lebih besar dari median
        if (sahamDipilih.isLessThan(sahamMedian)) { // jika lebih kecil maka cek ke maxheap

            // System.out.println("SANA");
            // VIEW(); // DEBUG
            Queue<Saham> temp = new LinkedList<Saham>(); // queue penyimpan elemen diremove sementara
            while (maxHeap.size > 0 && !maxHeap.getMax().equals(sahamDipilih)) {
                temp.add(maxHeap.extractMax());
            }
            // remove sahamDipilih
            maxHeap.extractMax();
            // masukkan kembali ke maxHeap
            while (!temp.isEmpty()) {
                maxHeap.insert(temp.remove());
            }

        } else { // jika lebih besar maka cek ke inheap
            
            // System.out.println("SINI");
            Queue<Saham> temp = new LinkedList<Saham>(); // queue penyimpan elemen diremove sementara
            while (minHeap.size > 0 && !minHeap.getMin().equals(sahamDipilih)) {
                temp.add(minHeap.extractMin());
            }
            // remove sahamDipilih
            minHeap.extractMin();
            // masukkan kembali ke minHeap
            while (!temp.isEmpty()) {
                minHeap.insert(temp.remove());
            }
        }

        // TAMBAH NODE YANG AKAN DITIMPA
        TAMBAH(nomorSeri, harga);
    }

    static void VIEW() {
        System.out.println("MEDIAN: [HARGA: "+sahamMedian.harga + " ID: " + sahamMedian.id+"]"); // DEBUG
        maxHeap.print(); // DEBUG
        minHeap.print(); // DEBUG
        System.out.println(maxHeap.Heap[0]);
        System.out.println(minHeap.Heap[0]);
    }

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

        public long nextLong() {
            return Long.parseLong(next());
        }
    }
}

class Saham implements Comparable<Saham> {
    public int id; // nomor seri
    public int harga;

    public Saham(int id, int harga) {
        this.id = id; 
        this.harga = harga;
    }

    @Override
    public String toString() {
        return "[HARGA: "+this.harga + " ID: " + this.id+"]";
    }

    @Override
    public int compareTo(Saham other) {
        if (this.harga == other.harga) {
            return this.id - other.id; // id terkecil hingga terbesar
        }
        return this.harga - other.harga; // harga termurah hingga termahal
    }

    boolean isLessThan(Saham other) {
        return this.compareTo(other) < 0;
    }
}

class MaxHeap {
    Saham[] Heap;
    int size;
    private int maxsize;
 
    // Constructor to initialize an
    // empty max heap with given maximum
    // capacity
    MaxHeap(int maxsize) {
        // This keyword refers to current instance itself
        this.maxsize = maxsize;
        this.size = 0;
        Heap = new Saham[this.maxsize];
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
 
    // Recursive function to max heapify given subtree
    private void maxHeapify(int pos) {
        if (isLeaf(pos))
            return;
 
        if (Heap[pos].isLessThan(Heap[leftChild(pos)])
            || Heap[pos].isLessThan(Heap[rightChild(pos)])) {
 
            if (Heap[rightChild(pos)].isLessThan(Heap[leftChild(pos)])) {
                swap(pos, leftChild(pos));
                maxHeapify(leftChild(pos));
            }
            else {
                swap(pos, rightChild(pos));
                maxHeapify(rightChild(pos));
            }
        }
    }
 
    // Inserts a new element to max heap
    public void insert(Saham element) {
        Heap[size] = element;
 
        // Traverse up and fix violated property
        int current = size;
        while (Heap[parent(current)].isLessThan(Heap[current])) {
            swap(current, parent(current));
            current = parent(current);
        }
        size++;
    }
 
    // To display heap
    public void print() {
 
        System.out.println("\nMAXHEAP: ");
        for (int i = 0; i < size; i++) {
            System.out.println("["+i+"]: "+Heap[i]);
        }
        System.out.println(" ");
    }

    // Remove an element from max heap
    public Saham extractMax() {
        Saham popped = Heap[0];
        Heap[0] = Heap[--size];
        maxHeapify(0);
        return popped;
    }

    public Saham getMax() {
        return Heap[0];
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

    // Recursive function to max heapify given subtree
    private void maxHeapify(int pos) {
        if (isLeaf(pos))
            return;
 
        if (Heap[pos].isLessThan(Heap[leftChild(pos)])
            || Heap[pos].isLessThan(Heap[rightChild(pos)])) {
 
            if (Heap[rightChild(pos)].isLessThan(Heap[leftChild(pos)])) {
                swap(pos, leftChild(pos));
                maxHeapify(leftChild(pos));
            }
            else {
                swap(pos, rightChild(pos));
                maxHeapify(rightChild(pos));
            }
        }
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
}


// MARIO'S REFERENCES:
// 1) https://stackoverflow.com/questions/15319561/how-to-implement-a-median-heap/15319593#15319593
// 2) https://www.geeksforgeeks.org/max-heap-in-java/
// 3) https://www.geeksforgeeks.org/min-heap-in-java/#:~:text=A%20Min%2DHeap%20is%20a,child%20at%20index%202k%20%2B%202.
// 4) https://stackoverflow.com/questions/13051568/making-your-own-class-comparable
// 5) https://www.geeksforgeeks.org/merge-sort/
// 6) https://www.javatpoint.com/understanding-toString()-method