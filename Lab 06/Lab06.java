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

    public static void main(String[] args) {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        int N = in.nextInt();

        // inisiasi saham ke dalam array untuk disort dahulu
        Saham[] initialSaham = new Saham[N]; // initial saham
        for (int i = 1; i <= N; i++) {
            int harga = in.nextInt();
            Saham saham = new Saham(i,harga);
            initialSaham[i-1] = saham;
        }

        // sort saham
        Arrays.sort(initialSaham);

        // cek isi array
        for(int i = 0; i < N; i++) {
            System.out.println("[HARGA: "+initialSaham[i].harga + " ID: " + initialSaham[i].id+"]");
        }

        // int Q = in.nextInt();

        // // TODO
        // for (int i = 0; i < Q; i++) {
        //     String q = in.next();

        //     if (q.equals("TAMBAH")) {
        //         int harga = in.nextInt();

        //     } else if (q.equals("UBAH")) {
        //         int nomorSeri = in.nextInt();
        //         int harga = in.nextInt();

        //     }
        // }
        out.flush();
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
    private int[] Heap;
    private int size;
    private int maxsize;
 
    // Constructor to initialize an
    // empty max heap with given maximum
    // capacity
    MaxHeap(int maxsize) {
        // This keyword refers to current instance itself
        this.maxsize = maxsize;
        this.size = 0;
        Heap = new int[this.maxsize];
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
        int tmp;
        tmp = Heap[fpos];
        Heap[fpos] = Heap[spos];
        Heap[spos] = tmp;
    }
 
    // Recursive function to max heapify given subtree
    private void maxHeapify(int pos) {
        if (isLeaf(pos))
            return;
 
        if (Heap[pos] < Heap[leftChild(pos)]
            || Heap[pos] < Heap[rightChild(pos)]) {
 
            if (Heap[leftChild(pos)]
                > Heap[rightChild(pos)]) {
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
    public void insert(int element) {
        Heap[size] = element;
 
        // Traverse up and fix violated property
        int current = size;
        while (Heap[current] > Heap[parent(current)]) {
            swap(current, parent(current));
            current = parent(current);
        }
        size++;
    }
 
    // To display heap
    public void print() {
 
        for (int i = 0; i < size / 2; i++) {
 
            System.out.print("Parent Node : " + Heap[i]);
 
            if (leftChild(i)
                < size) // if the child is out of the bound
                        // of the array
                System.out.print(" Left Child Node: "
                                 + Heap[leftChild(i)]);
 
            if (rightChild(i)
                < size) // if the right child index must not
                        // be out of the index of the array
                System.out.print(" Right Child Node: "
                                 + Heap[rightChild(i)]);
 
            System.out.println(); // for new line
        }
    }

    // Remove an element from max heap
    public int extractMax() {
        int popped = Heap[0];
        Heap[0] = Heap[--size];
        maxHeapify(0);
        return popped;
    }
}

class MinHeap {
 
    // Member variables of this class
    private int[] Heap;
    private int size;
    private int maxsize;
 
    // Initializing front as static with unity
    private static final int FRONT = 1;
 
    // Constructor of this class
    public MinHeap(int maxsize) {
        this.maxsize = maxsize;
        this.size = 0;
 
        Heap = new int[this.maxsize + 1];
        Heap[0] = Integer.MIN_VALUE;
    }
 
    // Returning the position of
    // the parent for the node currently
    // at pos
    private int parent(int pos) { return pos / 2; }
 
    // Returning the position of the
    // left child for the node currently at pos
    private int leftChild(int pos) { return (2 * pos); }
 
    // Returning the position of
    // the right child for the node currently
    // at pos
    private int rightChild(int pos) { return (2 * pos) + 1; }
 
    // Returning true if the passed
    // node is a leaf node
    private boolean isLeaf(int pos) { return (pos > (size / 2)); }

    // To swap two nodes of the heap
    private void swap(int fpos, int spos) {
        int tmp;
        tmp = Heap[fpos];
 
        Heap[fpos] = Heap[spos];
        Heap[spos] = tmp;
    }
 
    // To heapify the node at pos
   private void minHeapify(int pos) {     
     if(!isLeaf(pos)){
       int swapPos= pos;
       // swap with the minimum of the two children
       // to check if right child exists. Otherwise default value will be '0'
       // and that will be swapped with parent node.
       if(rightChild(pos)<=size)
          swapPos = Heap[leftChild(pos)]<Heap[rightChild(pos)]?leftChild(pos):rightChild(pos);
       else
         swapPos= Heap[leftChild(pos)];
        
       if(Heap[pos]>Heap[leftChild(pos)] || Heap[pos]> Heap[rightChild(pos)]){
         swap(pos,swapPos);
         minHeapify(swapPos);
       }
        
     }      
   }
 
    // To insert a node into the heap
    public void insert(int element) {
 
        if (size >= maxsize) {
            return;
        }
 
        Heap[++size] = element;
        int current = size;
 
        while (Heap[current] < Heap[parent(current)]) {
            swap(current, parent(current));
            current = parent(current);
        }
    }
 
    // To print the contents of the heap
    public void print() {
        for (int i = 1; i <= size / 2; i++) {
 
            // Printing the parent and both childrens
            System.out.print(
                " PARENT : " + Heap[i]
                + " LEFT CHILD : " + Heap[2 * i]
                + " RIGHT CHILD :" + Heap[2 * i + 1]);
 
            // By here new line is required
            System.out.println();
        }
    }
 
    // To remove and return the minimum
    // element from the heap
    public int extractMin() {
        int popped = Heap[FRONT];
        Heap[FRONT] = Heap[size--];
        minHeapify(FRONT);
        return popped;
    }
}


// MARIO'S REFERENCES:
// 1) https://stackoverflow.com/questions/15319561/how-to-implement-a-median-heap/15319593#15319593
// 2) https://www.geeksforgeeks.org/max-heap-in-java/
// 3) https://www.geeksforgeeks.org/min-heap-in-java/#:~:text=A%20Min%2DHeap%20is%20a,child%20at%20index%202k%20%2B%202.
// 4) https://stackoverflow.com/questions/13051568/making-your-own-class-comparable
// 5) https://www.geeksforgeeks.org/merge-sort/