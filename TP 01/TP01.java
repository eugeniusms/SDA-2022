import java.io.*;
import java.util.StringTokenizer;

public class TP01 {
    private static InputReader in;
    private static PrintWriter out;

    // ----------------------------------- ALL ABOUT MENU -----------------------------------------
    // jumlah menu 1 <= M <= 50.000
    public static int jumlahMenu; 
    // array digunakan untuk menyimpan semua menu makanan [harga] dan [tipe] 
    // ukuran diinisiasikan 50.069 sesuai batas worstcase
    public static int[] makananHarga = new int[50069]; // harga makanan yang ada 
    public static char[] makananTipe = new char[50069]; // tipe makanan yang ada (S -> G -> A)

    // ----------------------------------- ALL ABOUT KOKI -----------------------------------------
    // jumlah koki 1 <= V <= 1.000.000
    public static int jumlahKoki;
    // array digunakan untuk menyimpan index semua koki sesuai tipenya
    // ukuran diinisiasikan 1.000.069 sesuai batas worstcase
    public static int[] kokiS = new int[1000069];
    public static int[] kokiG = new int[1000069];
    public static int[] kokiA = new int[1000069];

    // -------------------------------- ALL ABOUT PELANGGAN ---------------------------------------
    // jumlah pelanggan 1 <= P <= 100.000
    public static int jumlahPelanggan;
    // jumlah kursi tersedia pada restoran 1 <= N <= 50.000
    public static int jumlahKursi;
    // jumlah hari beroperasi 1 <= Y <= 5
    public static int jumlahHari;

    // -------------------------------- ALL ABOUT OPERASI ----------------------------------------
    // jumlah pelanggan dalam suatu hari
    public static int jumlahPelangganHarian;
    // antrian pelanggan dalam suatu hari
    public static int I; // id : 1 <= I <= 100.000
    public static char K; // status kesehatan : {‘+’, ‘-’, ‘?’}
    public static int U; // jumlah uang : 1 <= U <= 100.000
    public static int R; // jumlah range advance screening : 1 <= R < j

    // ------------------------------- ALL ABOUT PELAYANAN --------------------------------------
    // jumlah pelayanan dalam suatu hari
    public static int jumlahPelayananHarian;
    // variabel penyimpan kode pelayanan
    public static char kode; // kode pelayanan : {‘P’, ‘L’, ‘B’, ‘C’, ‘D’}

    public static void main(String[] args) {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        // ---------------------------- AMBIL INPUT DEFAULT -------------------------------------
        jumlahMenu = in.nextInt(); // mengambil jumlah menu

        // Membaca input [harga] [tipe] menu makanan
        for (int i = 0; i < jumlahMenu; i++) {
            makananHarga[i] = in.nextInt();
            makananTipe[i] = in.nextChar();
        }

        jumlahKoki = in.nextInt(); // mengambil jumlah koki

        // Membaca input [tipe] koki
        



        // --------------------------- AMBIL INPUT PELAYANAN -----------------------------------

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