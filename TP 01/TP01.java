import java.io.*;
import java.util.StringTokenizer;

public class TP01 {
    private static InputReader in;
    private static PrintWriter out;
    
    // Rules:
    // *) Indexing data dalam array dimulai dari 1 (bukan 0)
    // *) Isi default sebuah array adalah ??? <BELUM ADA>

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
    // jumlah pelanggan dalam suatu hari 1 <= pelanggan harian <= 100.000
    public static int jumlahPelangganHarian;
    // total status pelanggan
    public static int[] I = new int[100069]; // id : 1 <= I <= 100.000
    public static char[] K = new char[100069]; // status kesehatan : {‘+’, ‘-’, ‘?’}
    public static int[] U = new int[100069]; // jumlah uang : 1 <= U <= 100.000
    public static int[] R = new int[100069]; // jumlah range advance screening : 1 <= R < j
    // single status pelanggan
    public static int i; // id : 1 <= I <= 100.000
    public static char k; // status kesehatan : {‘+’, ‘-’, ‘?’}
    public static int u; // jumlah uang : 1 <= U <= 100.000
    public static int r; // jumlah range advance screening : 1 <= R < j

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
        for (int i = 1; i <= jumlahMenu; i++) {
            makananHarga[i] = in.nextInt();
            makananTipe[i] = in.nextChar();
        }

        jumlahKoki = in.nextInt(); // mengambil jumlah koki

        // Membaca input [tipe] koki
        char inputTipe;
        // Pointer digunakan untuk mendapatkan iterasi koki
        // Hasil akhir pointer berupa jumlah koki pada tipe tersebut
        int pointerS = 1; int pointerG = 1; int pointerA = 1;
        for (int i = 1; i <= jumlahKoki; i++) {
            inputTipe = in.nextChar();
            if (inputTipe == 'S') {
                kokiS[pointerS] = i;
                pointerS++;
            } else if (inputTipe == 'G') {
                kokiG[pointerG] = i;
                pointerG++;
            } else {
                kokiA[pointerA] = i;
                pointerA++;
            }
        }
        // Di sini pointer = jumlah koki pada tipe tersebut
        pointerS--; pointerG--; pointerA--;

        jumlahPelanggan = in.nextInt(); // jumlah pelanggan total
        jumlahKursi = in.nextInt(); // jumlah kursi pada toko

        // ----------------------- AMBIL INPUT PELANGGAN HARIAN --------------------------------
        jumlahHari = in.nextInt(); // jumlah hari restoran beroperasi

        // Melakukan iterasi harian
        for (int i = 1; i <= jumlahHari; i++) { // hari ke-i
            jumlahPelangganHarian = in.nextInt(); // jumlah pelanggan hari ke-i
            for (int j = 1; j <= jumlahPelangganHarian; j++) { // pelanggan ke-j
                // sebelah kiri mengambil data satuan [id] [status] [uang]
                // sebelah kanan menyimpan pelanggan ke j di array I, K, U, R total
                i = in.nextInt(); I[j] = i;
                k = in.nextChar(); K[j] = k;
                u = in.nextInt(); U[j] = u;
                // if k == ? then add [range] => [id] [status] [uang] [range]
                if (k == '?') {
                    r = in.nextChar(); R[j] = r;
                }
            }
        }

        // --------------------------- AMBIL INPUT PELAYANAN -----------------------------------

        // Run Solusi
        // int solution = 1;
        // out.print(solution);

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
            return next().charAt(0);
        }
    }
}