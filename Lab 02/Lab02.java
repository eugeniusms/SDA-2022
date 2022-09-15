import java.io.*;
import java.util.StringTokenizer;
import java.util.*;

public class Lab02 {
    // TODO : Silahkan menambahkan struktur data yang diperlukan
    private static InputReader in;
    private static PrintWriter out;

    // Menginisiasikan conveyorQueue untuk mengantrikan daftar toples (Queue)
    // yang mana toples adalah Stack<Integer>
    public static Queue<Stack<Integer>> conveyorQueue = new LinkedList<> ();
    // Inisiasi pointer untuk indexing queue (bukan menggeser queue)
    public static int pointer = 0;

    // Mengambil data statis masukan
    public static int banyakToples;
    public static int banyakKuePerToples;
    public static int banyakQuery;

    static int geserKanan() {
        // Konsep geser pointer: saat conveyor geserKanan maka sama saja pointer geser kiri

        // // Jika pointer sudah dipojok kiri maka geser kekiri didapati pointer dengan index
        // // terbesar dalam conveyorQueue
        // if (pointer == 0) {
        //     pointer = banyakToples-1;
        // } else {
        //     // Selain kasus pemindahan pada batas di atas maka lakukan pengurangan saja (geser kiri)
        //     pointer -= 1;
        // }

        // Lakukan pengecekan kue teratas pada toples

        // Queue method: https://www.codegrepper.com/code-examples/java/pop+in+queue+java

        // Meremove head of queue dan mengambil datanya untuk dimasukkan ke tail of queue lagi
        Stack<Integer> siToplesPalingKanan = conveyorQueue.remove(); 
        // Memasukkan data terkanan ke dalam tail of queue
        conveyorQueue.add(siToplesPalingKanan);
        return -1;
    }

    static int beliRasa(int rasa) {
        // TODO : Implementasi fitur beli rasa, manfaatkan fitur geser kanan
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

            // Toples diqueue ke dalam conveyor belt (add)
            conveyorQueue.add(toples);
        }

        for (int i = 0; i < banyakQuery; i++) {
            String perintah = in.next();
            if (perintah.equals("GESER_KANAN")) {
                out.println(geserKanan());
            } else if (perintah.equals("BELI_RASA")) {
                int namaRasa = in.nextInt();
                out.println(beliRasa(namaRasa));
            }
        }
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