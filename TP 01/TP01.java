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
    public static int jumlahKokiS;
    public static int jumlahKokiG;
    public static int jumlahKokiA;
    // array digunakan untuk menyimpan index semua koki sesuai tipenya
    // ukuran diinisiasikan disesuaikan jumlah koki pada saat input koki 
    // starting with index 1
    // menyimpan id koki SGA, default = 0 (tidak ada koki lagi back to depan)
    public static int[] idKokiS = new int[1000069];
    public static int[] idKokiG = new int[1000069];
    public static int[] idKokiA = new int[1000069];
    // pelayanan saat ini pada koki (all day)
    public static int[] pelayananKokiS;
    public static int[] pelayananKokiG;
    public static int[] pelayananKokiA;

    // PidForL digunakan untuk menyimpan ID Pelanggan sesuai urutan pelanggan memesan makanan
    public static int[] PidForL = new int[100069]; // dari index 0 -> ...
    // PtipeMakananForL digunakan untuk menyimpan tipe makanan pelanggan sesuai urutan memesan makanan
    public static char[] PtipeMakananForL = new char[100069]; // dari index 0 -> ...
    // PhargaMakananForL digunakan untuk menyimpan harga makanan pelanggan sesuai urutan memesan makanan
    public static int[] PhargaMakananForL = new int[100069]; // dari index 0 -> ...

    // Pointer kokiS, starting with index 1
    public static int pointerKokiS = 1;
    public static int pointerKokiG = 1;
    public static int pointerKokiA = 1;
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
    public static int[] U = new int[100069]; // berdasarkan antrian (one day), jumlah uang : 1 <= U <= 100.000, default: 0
    public static int[] BONL = new int[100069]; // berdasarkan antrian (one day), jumlah uang membayar yang sudah dilayani, default: 0
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
        jumlahKokiS = pointerS; jumlahKokiG = pointerG; jumlahKokiA = pointerA;

        // ------------- INISIASI BESAR ARRAY KOKI --------------
        // isi tidak perlu direset karena tercatat sampai besok
        pelayananKokiS = new int[jumlahKokiS];
        pelayananKokiG = new int[jumlahKokiG];
        pelayananKokiA = new int[jumlahKokiA];
        // ------------------------------------------------------

        jumlahPelanggan = in.nextInt(); // jumlah pelanggan total
        jumlahKursi = in.nextInt(); // jumlah kursi pada toko

        // -------------------------- AMBIL INPUT HARIAN -----------------------------------
        jumlahHari = in.nextInt(); // jumlah hari restoran beroperasi

        // melakukan iterasi harian
        for (int i = 1; i <= jumlahHari; i++) { // hari ke-i
            // 4eset antrean ke default = 0
            Arrays.fill(IbyQueue, 0);
            Arrays.fill(KbyQueue, 0); 
            Arrays.fill(UbyQueue, 0);
            // reset U dan BONL pelanggan
            Arrays.fill(U, 0);
            Arrays.fill(BONL, 0);

            int jumlahKursiKosong = jumlahKursi; // reset ke jumlah kursi toko
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
            // inisiasi variabel query L
            int pointerPidForL = 0;
            int pointerPtipeMakananForL = 0;
            int pointerPhargaMakananForL = 0;
            int sumOfL = 0;
            char jenisMakananDilayani;

            // inisiasi variabel pointer koki yang mengambil pesanan
            char jenisMakanan;

            for (int k = 1; k <= jumlahPelayananHarian; k++) { // pelayanan ke-k
                kode = in.nextChar(); // mengambil kode pelayanan

                // QUERY P (CLEAR)
                if (kode == 'P') {
                    arg1 = in.nextInt(); // [ID_PELANGGAN]
                    arg2 = in.nextInt(); // [INDEX_MAKANAN]

                    PidForL[pointerPidForL] = arg1; pointerPidForL++; // simpan urutan id pelanggan yang pesan
                    PtipeMakananForL[pointerPtipeMakananForL] = makananTipe[arg2]; pointerPtipeMakananForL++; // simpan jenis makanan pelanggan
                    PhargaMakananForL[pointerPhargaMakananForL] = makananHarga[arg2]; pointerPhargaMakananForL++;

                    // operasi disesuaikan jenis makanan
                    jenisMakanan = makananTipe[arg2];
                    if (jenisMakanan == 'S') {
                        // jika id koki tersebut == 0 maka reset kembali ke index pertama idKoki
                        if (idKokiS[pointerKokiS] == 0) {
                            pointerKokiS = 1;
                        }
                        out.println("P: "+idKokiS[pointerKokiS]); // OUTPUT P
                    } else if (jenisMakanan == 'G') {
                        // jika id koki tersebut == 0 maka reset kembali ke index pertama idKoki
                        if (idKokiG[pointerKokiG] == 0) {
                            pointerKokiG = 1;
                        }
                        out.println("P: "+idKokiG[pointerKokiG]); // OUTPUT P
                    } else { // jenisMakanan == 'A'
                        // jika id koki tersebut == 0 maka reset kembali ke index pertama idKoki
                        if (idKokiA[pointerKokiA] == 0) {
                            pointerKokiA = 1;
                        }
                        out.println("P: "+idKokiA[pointerKokiA]); // OUTPUT P
                    }
                    
                // QUERY L (CLEAR)
                } else if (kode == 'L') {
                    // ambil tipe makanan pada L yang sesuai (sumOfL)
                    jenisMakananDilayani = PtipeMakananForL[sumOfL];
                    BONL[PidForL[sumOfL]] += PhargaMakananForL[sumOfL];
                    
                    // kurangi pending dan tambah pelayanan pada koki sesuai jenisMakananDilayani
                    // lalu letakan pointer pada koki dengan pelayanan terkecil dari index terdepan 
                    if (jenisMakananDilayani == 'S') {
                        pelayananKokiS[pointerKokiS]++;
                        // searching minimum pelayanan koki s
                        int minim = 999999;
                        for (int sm = 1; sm < jumlahKokiS; sm++) {
                            if (pelayananKokiS[sm] < minim) {
                                minim = pelayananKokiS[sm];
                                pointerKokiS = sm; // set pointer koki s ke sm (koki dengan id minim && pelayanan paling minimum)
                            }
                        }
                    } else if (jenisMakananDilayani == 'G') {
                        pelayananKokiG[pointerKokiG]++;
                        // searching minimum pelayanan koki g
                        int minim = 999999;
                        for (int sm = 1; sm < jumlahKokiG; sm++) {
                            if (pelayananKokiG[sm] < minim) {
                                minim = pelayananKokiG[sm];
                                pointerKokiG = sm; // set pointer koki g ke sm (koki dengan id minim && pelayanan paling minimum)
                            }
                        }
                    } else {
                        pelayananKokiA[pointerKokiA]++;
                        // searching minimum pelayanan koki a
                        int minim = 999999;
                        for (int sm = 1; sm < jumlahKokiA; sm++) {
                            if (pelayananKokiA[sm] < minim) {
                                minim = pelayananKokiA[sm];
                                pointerKokiA = sm; // set pointer koki a ke sm (koki dengan id minim && pelayanan paling minimum)
                            }
                        }
                    }

                    out.println("L : "+PidForL[sumOfL]); // print output id pelanggan // OUTPUT L
                    sumOfL++; // menambah jumlah L dipanggil
            
                // QUERY B
                } else if (kode == 'B') {
                    arg1 = in.nextInt(); // [ID_PELANGGAN]

                    // jika uang pelanggan minus maka blacklist (bonnya > uangnya)
                    U[arg1] -= BONL[arg1];
                    if (U[arg1] < 0) {
                        isBlacklist[arg1] = true;
                        out.println("B: 0"); // uang pelanggan tidak mencukupi
                    } else {
                        out.println("B: 1"); // uang pelanggan mencukupi
                    }
                
                // QUERY C
                } else if (kode == 'C') {
                    arg1 = in.nextInt(); // [Q]
                
                    out.print("C: ");
                    // search Q koki dengan pelayanan paling sedikit lalu S > G > A lalu index
                    int searchPoin = 0;
                    int indexS = 1; int indexG = 1; int indexA = 1;
                    // memulai pencarian dengan koki poin 0
                    while (arg1 > 0) {
                        boolean gotIt = false; // var untuk mengecek apakah sudah didapati nilai score

                        // cek S terkecil dahulu
                        while (indexS < jumlahKokiS) {
                            if (pelayananKokiS[indexS] == searchPoin) {
                                out.print(idKokiS[indexS] + " ");
                                indexS++; gotIt = true;
                                break;
                            }
                            indexS++;
                        }

                        // lanjutkan while saat didapati nilai poin yang sesuai
                        if (gotIt) {
                            arg1--;
                            continue;
                        }

                        // cek G 
                        while (indexG < jumlahKokiG) {
                            if (pelayananKokiG[indexG] == searchPoin) {
                                out.print(idKokiG[indexG] + " ");
                                indexG++; gotIt = true;
                                break;
                            }
                            indexG++;
                        }

                        // lanjutkan while saat didapati nilai poin yang sesuai
                        if (gotIt) {
                            arg1--;
                            continue;
                        }

                        // cek A
                        while (indexA < jumlahKokiA) {
                            if (pelayananKokiA[indexA] == searchPoin) {
                                out.print(idKokiA[indexA] + " ");
                                indexA++; gotIt = true;
                                break;
                            }
                            indexA++;
                        }

                        // lanjutkan while saat didapati nilai poin yang sesuai
                        if (gotIt) {
                            arg1--;
                            continue;
                        }

                        // Mereset index ke depan kembali                        
                        if (indexS == jumlahKokiS ) {
                            indexS = 1;
                        } 
                        if (indexG == jumlahKokiG) {
                            indexG = 1;
                        }
                        if (indexA == jumlahKokiA) {
                            indexA = 1;
                        }

                        // Melanjutkan cari poin 1,2,3.. 
                        searchPoin++; // saat tidak didapati satupun maka lanjut cari poin 1,2,3...
                    }


                // QUERY D
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

            printCheck();
        }

        // printCheck();

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
        for (int i = 1; i < jumlahKokiS; i++) {
            out.println(idKokiS[i]+") Pelayanan: " + pelayananKokiS[i]);
        }
        out.println("Koki G: ");
        for (int i = 1; i < jumlahKokiG; i++) {
            out.println(idKokiG[i]+") Pelayanan: " + pelayananKokiG[i]);
        }
        out.println("Koki A: ");
        for (int i = 1; i < jumlahKokiA; i++) {
            out.println(idKokiA[i]+") Pelayanan: " + pelayananKokiA[i]);
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