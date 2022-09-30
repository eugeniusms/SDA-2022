import java.io.*;
import java.util.StringTokenizer;

public class TP01 {
    private static InputReader in;
    private static PrintWriter out;

    // jumlah menu 1 <= M <= 50.000
    public static int jumlahMenu; 

    // array digunakan untuk menyimpan semua menu makanan [harga] dan [tipe] 
    // ukuran diinisiasikan 50.069 sesuai batas worstcase
    public static int[] makananHarga = new int[50069]; // harga makanan yang ada 
    public static char[] makananTipe = new char[50069]; // tipe makanan yang ada (S -> G -> A)

    // jumlah koki 1 <= V <= 1.000.000
    public static int jumlahKoki;
    // array digunakan untuk menyimpan index semua koki sesuai tipenya
    // ukuran diinisiasikan 1.000.069 sesuai batas worstcase
    public static int[] kokiS = new int[1000069];
    public static int[] kokiG = new int[1000069];
    public static int[] kokiA = new int[1000069];
    

    public static char[] A;
    public static int N;
    // Memo yang digunakan untuk menyimpan redSaveNew dari kombinasi yang pernah ada
    public static int[] memo = new int[1069];

    public static void main(String[] args) {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        jumlahMenu = in.nextInt();



        // Inisialisasi Array Input
        N = in.nextInt();
        A = new char[N];

        // Membaca File Input
        for (int i = 0; i < N; i++) {
            A[i] = in.nextChar();
        }

        // Run Solusi
        int solution = 1;
        out.print(solution);

        // Tutup OutputStream
        out.close();
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