import java.io.*;
import java.util.StringTokenizer;
import java.util.Arrays;

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
    // id koki SGA, default = 0 (tidak ada koki lagi back to depan)
    public static int[] idKokiS = new int[1000069];
    public static int[] idKokiG = new int[1000069];
    public static int[] idKokiA = new int[1000069];
    // pelayanan 


    // daftar urutan pesanan
    public static String combinedPesanan = "";
    // PidForL digunakan untuk menyimpan ID Pelanggan sesuai urutan pelanggan memesan makanan
    public static int[] PidForL = new int[100069]; // dari index 0 -> ...
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
    // TOTAL STATUS PELANGGAN
    // digunakan cuma saat awal dalam hari mengantri
    public static int[] IbyQueue = new int[100069]; // berdasarkan antrian (one day), id : 1 <= I <= 100.000, default: 0
    public static int[] KbyQueue = new int[100069]; // berdasarkan antrian (one day), status kesehatan : {‘+’=+1, ‘-’=-1, ‘?’=ditentukan}, default: 0
    public static int[] UbyQueue = new int[100069]; // berdasarkan antrian (one day), jumlah uang : 1 <= U <= 100.000, default: 0
    // digunakan untuk melayani pelanggan (cuma perlu uang dan isBlacklist yang selalu updated)
    public static int[] U = new int[100069]; // berdasarkan id (all day), jumlah uang : 1 <= U <= 100.000, default: 0
    public static boolean[] isBlacklist = new boolean[100069]; // berdasarkan id (all day), default: false
    // SINGLE STATUS PELANGGAN
    public static int id; // id : 1 <= I <= 100.000
    public static char k; // status kesehatan : {‘+’, ‘-’, ‘?’}
    public static int u; // jumlah uang : 1 <= U <= 100.000
    public static int r; // jumlah range advance screening : 1 <= R < j

    // ------------------------------- ALL ABOUT PELAYANAN --------------------------------------
    // jumlah pelayanan dalam suatu hari
    public static int jumlahPelayananHarian;
    // variabel penyimpan kode pelayanan
    public static char kode; // kode pelayanan : {‘P’, ‘L’, ‘B’, ‘C’, ‘D’}
    // variabel temporary arg1, arg2, arg3 dalam pelayanan
    public static int arg1, arg2, arg3;

    public static void main(String[] args) {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        // ---------------------------- AMBIL INPUT DEFAULT -------------------------------------
        jumlahMenu = in.nextInt(); // mengambil jumlah menu

        out.println("OUTPUT:"); // TEST

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
                idKokiS[pointerS] = i;
                pointerS++;
            } else if (inputTipe == 'G') {
                idKokiG[pointerG] = i;
                pointerG++;
            } else { // inputTipe == 'A'
                idKokiA[pointerA] = i;
                pointerA++;
            }
        }
        // Di sini pointer = jumlah koki pada tipe tersebut
        pointerS--; pointerG--; pointerA--;

        jumlahPelanggan = in.nextInt(); // jumlah pelanggan total
        jumlahKursi = in.nextInt(); // jumlah kursi pada toko

        // -------------------------- AMBIL INPUT HARIAN -----------------------------------
        jumlahHari = in.nextInt(); // jumlah hari restoran beroperasi

        // Melakukan iterasi harian
        for (int i = 1; i <= jumlahHari; i++) { // hari ke-i
            // Reset antrean ke default = 0
            Arrays.fill(IbyQueue, 0);
            Arrays.fill(KbyQueue, 0); 
            Arrays.fill(UbyQueue, 0);
            int jumlahKursiKosong = jumlahKursi;
            // ----------------------- AMBIL INPUT PELANGGAN ------------------------------
            jumlahPelangganHarian = in.nextInt(); // jumlah pelanggan hari ke-i
            for (int j = 1; j <= jumlahPelangganHarian; j++) { // pelanggan ke-j
                // sebelah kiri mengambil data satuan [id] [status] [uang]
                // sebelah kanan menyimpan pelanggan ke j di array I, K, U, R total
                id = in.nextInt(); IbyQueue[j] = id;
                k = in.nextChar(); 
                u = in.nextInt(); UbyQueue[j] = u;

                // "+" => +1 | "-" => -1
                if (k == '+') {
                    KbyQueue[j] = 1;
                } else if (k == '-') {
                    KbyQueue[j] = -1;
                } else {
                    // if k == ? then add [range] => [id] [status] [uang] [range]
                    r = in.nextInt(); 
                    // memasang K sesuai indeks pelanggan dalam array dan range
                    getK(j, r); 
                }

                // lakukan penyelesaian A: status pelanggan harian (0-1-2-3)
                if (isBlacklist[id]) {
                    out.print("3 "); // blacklisted
                } else {
                    if (KbyQueue[j] == 1) {
                        out.print("0 "); // positive
                    } else {
                        if (jumlahKursiKosong <= 0) {
                            out.print("2 "); // ruang lapar
                        } else {
                            out.print("1 "); // tidak ada masalah
                            jumlahKursiKosong--; // kurangi karena pelanggan bisa masuk
                        }
                    }
                }

                // ------------ SIMPAN DATA PELANGGAN DALAM ARRAY BY ID --------------------
                // hanya perlu menyimpan uang data pelanggann untuk pelayanan by id, blacklist kemudian
                U[id] = u;
            } 
            out.println(); // new line output setelah penyelesaian A

            // ----------------------- AMBIL INPUT PELAYANAN ------------------------------
            jumlahPelayananHarian = in.nextInt(); // jumlah pelayanan restoran dalam suatu hari 
            int pointerPidForL = 0;
            int sumOfL = 0;

            for (int k = 1; k <= jumlahPelayananHarian; k++) { // pelayanan ke-k
                kode = in.nextChar(); // mengambil kode pelayanan
                if (kode == 'P') {
                    arg1 = in.nextInt(); // [ID_PELANGGAN]
                    arg2 = in.nextInt(); // [INDEX_MAKANAN]

                    PidForL[pointerPidForL] = arg1; pointerPidForL++; // simpan urutan id pelanggan yang pesan
                    combinedPesanan += makananTipe[arg2]; // COMBINED COMMAND
                    
                } else if (kode == 'L') {
                    out.println(PidForL[sumOfL]); sumOfL++; // print output id pelanggan
                } else if (kode == 'B') {
                    arg1 = in.nextInt(); // [ID_PELANGGAN]
                } else if (kode == 'C') {
                    arg1 = in.nextInt(); // [Q]
                } else { // kode == 'D'
                    arg1 = in.nextInt(); // [COST_A]
                    arg2 = in.nextInt(); // [COST_G]
                    arg3 = in.nextInt(); // [COST_S]
                }
            }

            // TEST
            for (int z = 0; z < 10; z++) {
                out.println("CEK ID PEL SESUAI L: "+PidForL[z]);
            }
            out.println("COMBINED PESANAN: "+combinedPesanan); // TEST
        }

        printCheck();

        // Run Solusi
        // int solution = 1;
        // out.print(solution);

        // Tutup OutputStream
        out.close();
    }

    // Fungsi untuk memberi status pada pelanggan bertipe = "?"
    public static void getK(int indeksPelanggan, int jarak) {
        int sumStatus = 0;
        int indeks = indeksPelanggan;
        while (jarak > 0) {
            indeks--; jarak--; 
            sumStatus += KbyQueue[indeks];
            // out.println("CEK: "+ indeks + " | " + jarak + " | " + sumStatus); // TEST
        }
        // Memberi status pada pelanggan
        if (sumStatus > 0) {
            KbyQueue[indeksPelanggan] = 1; // K = Positif
        } else {
            KbyQueue[indeksPelanggan] = -1; // K = Negatif
        }
    }

    // FUNGSI TESTING AJA
    public static void printCheck() {
        // NILAI
        out.println("Keterangan Pelanggan: ");
        for (int i = 1; i <= 4; i++) {
            out.println(i + ") " + KbyQueue[i]);
        }

        // KOKI 
        out.println("Koki: ");
        out.println("Koki S: ");
        for (int i = 1; i <= 5; i++) {
            out.println(idKokiS[i]);
        }
        out.println("Koki G: ");
        for (int i = 1; i <= 5; i++) {
            out.println(idKokiG[i]);
        }
        out.println("Koki A: ");
        for (int i = 1; i <= 5; i++) {
            out.println(idKokiA[i]);
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

        public char nextChar() {
            return next().charAt(0);
        }
    }
}