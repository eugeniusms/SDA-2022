import java.io.*;
import java.util.StringTokenizer;
import java.util.*;

public class Lab02Debug {
    private static InputReader in;
    private static PrintWriter out;

    // Menginisiasikan conveyorQueue untuk mengantrikan daftar toples (Deque untuk pakai dua arah conveyor)
    // https://docs.oracle.com/javase/7/docs/api/java/util/Deque.html
    // yang mana toples adalah Stack<Integer>
    // MY SIGN:
    // GESER_KANAN: QUEUE HEAD - First (addLast, removeFirst, peekFirst) + -
    // GESER_KIRI: QUEUE TAIL - Last (addFirst, removeLast, peekLast) - + 
    public static Deque<Stack<Integer>> conveyorQueue = new LinkedList<> ();
    
    // Menginisiasi variabel data statis masukan
    public static int banyakToples;
    public static int banyakKuePerToples;
    public static int banyakQuery;

    // Method yang dipanggil saat Sofita menggeser conveyor ke kanan
    static int geserKanan() {
        // Queue method: https://www.codegrepper.com/code-examples/java/pop+in+queue+java

        // STEP 1: Meremove head of queue dan mengambil datanya untuk dimasukkan ke tail of queue lagi (removeFirst) - head
        Stack<Integer> siToplesPalingKanan = conveyorQueue.removeFirst(); 
        // Memasukkan data terkanan ke dalam tail of queue (addLast) - tail
        conveyorQueue.addLast(siToplesPalingKanan);

        // STEP 2: Mengecek kue teratas di siToplesPalingKanan
        // stack.empty(): https://www.geeksforgeeks.org/stack-empty-method-in-java/
        // Jika toples kosong maka return -1
        if (siToplesPalingKanan.empty()) {
            return -1;
        } else {
            // Jika masih ada isinya maka return kode kue paling atas dalam toples untuk dicetak
            return siToplesPalingKanan.peek();
        }
    }

    // Method yang dipanggil saat Sofita ingin membeli rasa tertentu
    static int beliRasa(int rasa) {
        // STEP 1: Mengecek kue teratas di setiap toples yang ada dalam conveyor queue (maksimal sebanyak banyakToples)
        for(int i = 0; i < banyakToples; i++) {
            // Mengecek setiap toples yang ada lalu geser ke arah sebalik conveyor jalan (geser kiri) untuk
            // mendapati toples terdekat dari Dek Sofita

            // Mengambil toples paling kiri
            Stack<Integer> siToplesPalingKiri = conveyorQueue.removeLast(); 

            // CHECK 1: Cek apakah toples kosong, jika kosong langsung continue ke pengecekan toples selanjutnya
            if (siToplesPalingKiri.empty()) {
                // Geser toples palingKiri ke head (addFirst)
                conveyorQueue.addFirst(siToplesPalingKiri);
                continue;
            }

            // CHECK 2: Cek top of stack (kue teratas) dari toples paling kiri
            if (siToplesPalingKiri.peek() == rasa) {
                // Jika kue teratas sama dengan rasa diminta maka 
                // Kembalikan siToplesPalingKiri ke asalnya (di tail) (dengan stack toples yang sudah dipop)
                siToplesPalingKiri.pop();
                conveyorQueue.addLast(siToplesPalingKiri);
                // Kembalikan i (jumlah pergeseran/pengecekan toples ke berapa) untuk dicetak
                return i;

            } else {
                // Jika kue teratas tidak sama dengan rasa yang diminta maka geser kekiri lagi
                conveyorQueue.addFirst(siToplesPalingKiri);
                continue;
            }         
        }
        // STEP 2: Saat melebihi banyak perulangan toples maka berarti tidak ada kue teratas yang sesuai rasa diinginkan
        return -1;
    }

    public static void main(String[] args) {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        // Mengambil data statis masukan
        banyakToples = in.nextInt();
        banyakKuePerToples = in.nextInt();
        banyakQuery = in.nextInt();

        // Mendata setiap toples beserta isi kue di dalamnya
        for (int i = 0; i < banyakToples; ++i) {
            // Menginsiasikan toples untuk diisi kue-kue (Stack), kue berkodekan angka integer
            Stack<Integer> toples = new Stack<Integer>();

            // Mengisi kue di dalam toples
            for (int j = 0; j < banyakKuePerToples; j++) {
                // Mengambil kode rasa kue
                int rasaKeJ = in.nextInt();
                // Menambahkan kue sesuai rasa ke dalam toples (push)
                toples.push(rasaKeJ);
            }

            // Toples diqueue ke dalam conveyor belt (addLast) - tail
            conveyorQueue.addLast(toples);
        }

        long startTime = System.nanoTime(); // Test : Timing

        // Mengambil banyak query
        for (int i = 0; i < banyakQuery; i++) {
            // Mengambil query
            String perintah = in.next();
            
            // Sesuaikan query dengan pemanggilan method yang dibutuhkan
            if (perintah.equals("GESER_KANAN")) {
                out.println(geserKanan());
            } else if (perintah.equals("BELI_RASA")) {
                int namaRasa = in.nextInt();
                out.println(beliRasa(namaRasa));
            }
        }

        long endTime   = System.nanoTime(); // Test : Timing
        long totalTime = endTime - startTime; // Test : Timing
        out.println("TOTAL WAKTU : " + totalTime + " ns | " + totalTime/1000000000 + " s"); // Test : Timing (ns)

        out.close();
    }
    // taken from https://codeforces.com/submissions/Petr
    // together with PrintWriter, these input-output (IO) is much faster than the usual Scanner(System.in) and System.out
    // please use these classes to avoid your fast algorithm gets Time Limit Exceeded caused by slow input-output (IO)
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