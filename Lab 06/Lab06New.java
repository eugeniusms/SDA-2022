import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.*;

public class Lab06New {

    private static InputReader in;
    private static PrintWriter out;

    static Saham sahamMedian;

    // inisiasi maxheap untuk data ke 1 - median
    static MaxHeap<Saham> maxHeap = new MaxHeap<Saham>();
    // inisiasi minheap untuk data ke median - N
    static MinHeap<Saham> minHeap = new MinHeap<Saham>();
    
    // Map<key = nomorseri, value = saham> : menyimpan saham sesuai nomor seri
    static HashMap<Integer, Saham> map = new HashMap<Integer, Saham>();

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
                map.put(i, saham);
            }

            // sort saham
            Arrays.sort(initialSaham);

            // get median of initial saham
            int median = getIndexMedian(N);
            sahamMedian = initialSaham[median];

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

        // VIEW();

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
        map.put(id, sahamBaru);
        // cek apakah harga saham baru lebih besar dari median
        if (sahamMedian.isLessThan(sahamBaru)) { // jika lebih besar maka masuk ke minHeap
            minHeap.insert(sahamBaru);
        } else { 
            maxHeap.insert(sahamBaru);
        }

        // transfer node dari heap yang lebih banyak ke heap yang kurang banyak
        int minHeapSize = minHeap.getSize();
        int maxHeapSize = maxHeap.getSize();

        if (minHeapSize - maxHeapSize == 2) { // saat selisih size heap = 2
            maxHeap.insert(minHeap.remove());
        } else if (maxHeapSize - minHeapSize == 2) { // saat selisih size heap = 2
            minHeap.insert(maxHeap.remove());
        } else if (maxHeapSize - minHeapSize == 1) { // saat selisih size heap = 1
            minHeap.insert(maxHeap.remove());
        } else {
            if (minHeapSize - maxHeapSize > 1) { // saat selisih size banyak
                while (minHeapSize - maxHeapSize > 1) {
                    maxHeap.insert(minHeap.remove());
                }
            }
            if (maxHeapSize - minHeapSize > 0) { // saat selisih size banyak
                while (maxHeapSize - minHeapSize > 0) {
                    minHeap.insert(maxHeap.remove());
                }
            }
        }

        // update median
        sahamMedian = minHeap.getMin();
        out.println(sahamMedian.id); // RESULT
    }

    static void UBAH(int nomorSeri, int harga) {
    }

    static void VIEW() {
        System.out.println("MEDIAN: [HARGA: "+sahamMedian.harga + " ID: " + sahamMedian.id+"]"); // DEBUG
        maxHeap.print(); // DEBUG
        minHeap.print(); // DEBUG
        // System.out.println(maxHeap.Heap[0]);
        // System.out.println(minHeap.Heap[0]);
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

class MinHeap<T extends Comparable<T>> {
	ArrayList<T> data;

	public MinHeap() {
		this.data = new ArrayList<T>();
	}

	public MinHeap(ArrayList<T> arr) {
		this.data = arr;
		heapify();
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

	void heapify() {
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

    // To display heap
    public void print() {
 
        System.out.println("\nMINHEAP: ");
        int counter = 0;
        for (T s : this.data) {
            System.out.println("["+counter+"]: "+s);
            counter++;
        }
        System.out.println(" ");
    }

    int getSize() {
        return this.data.size();
    }

    T getMin() {
        return this.data.get(0);
    }
}

class MaxHeap<T extends Comparable<T>> {
	ArrayList<T> data;

	public MaxHeap() {
		this.data = new ArrayList<T>();
	}

	public MaxHeap(ArrayList<T> arr) {
		this.data = arr;
		heapify();
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
				int maxChildIdx = leftIdx;
				int rightIdx = getRightChildIdx(idx);
				if (rightIdx < heapSize && data.get(rightIdx).compareTo(data.get(leftIdx)) > 0)
					maxChildIdx = rightIdx; // ubah max menjadi rightchild

				if (node.compareTo(data.get(maxChildIdx)) < 0) { // compare node dengan maxchild
                    // jika node lebih kecil maka swap
					data.set(idx, data.get(maxChildIdx));
					idx = maxChildIdx;
				} else {
					data.set(idx, node);
					break;
				}
			}
		}
	}

	private void percolateUp(int idx) {
		T node = data.get(idx);
		int parentIdx = getParentIdx(idx); // mendapatkan id parent
		while (idx > 0 && node.compareTo(data.get(parentIdx)) > 0) { // jika node lebih besar dari parent maka swap
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

	void heapify() {
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

    // To display heap
    public void print() {
 
        System.out.println("\nMAXHEAP: ");
        int counter = 0;
        for (T s : this.data) {
            System.out.println("["+counter+"]: "+s);
            counter++;
        }
        System.out.println(" ");
    }

    int getSize() {
        return this.data.size();
    }

    T getMax() {
        return this.data.get(0);
    }
}

// MARIO'S REFERENCES:
// 1) https://stackoverflow.com/questions/15319561/how-to-implement-a-median-heap/15319593#15319593
// 2) https://www.geeksforgeeks.org/max-heap-in-java/
// 3) https://www.geeksforgeeks.org/min-heap-in-java/#:~:text=A%20Min%2DHeap%20is%20a,child%20at%20index%202k%20%2B%202.
// 4) https://stackoverflow.com/questions/13051568/making-your-own-class-comparable
// 5) https://www.geeksforgeeks.org/merge-sort/
// 6) https://www.javatpoint.com/understanding-toString()-method
// 7) Binary Heap by Bu Pudy