import java.io.*;
import java.util.StringTokenizer;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Collections;

public class Submitted14 {
    private static InputReader in;
    private static PrintWriter out;

    // Rules:
    // *) Indexing data dalam array dimulai dari 1 (bukan 0)
    // *) Isi default sebuah array of object, data field int = 0, char = 'O'

    // default arr[0] = kosong
    public static Makanan[] menu; // index => id makanan

    // Query P, L, C
    public static PriorityQueue<Koki> kokiS = new PriorityQueue<>(); // kokiS (terurut minimal melayani)
    public static PriorityQueue<Koki> kokiG = new PriorityQueue<>(); // kokiG (terurut minimal melayani)
    public static PriorityQueue<Koki> kokiA = new PriorityQueue<>(); // kokiA (terurut minimal melayani)
    public static ArrayList<Koki> kokiAll= new ArrayList<Koki>(); // koki (terurut minimal melayani)

    public static Pelanggan[] pelanggan; // index => id pelanggan

    // Query B
    public static Queue<Pesanan> pesanan = new LinkedList<>(); // menyimpan antrian pesanan

    // jumlah kursi
    public static int jumlahKursi = 0;

    // Query D
    public static int jumlahMenu;
    public static long totalHargaMenu = 0;

    public static void main(String[] args) {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        // ---------------------------- AMBIL INPUT DEFAULT -------------------------------------
        // ambil jumlah menu makanan
        jumlahMenu = in.nextInt();
        menu = new Makanan[jumlahMenu+1]; menu[0] = new Makanan(0, 'O'); // set default
        // membaca input menu makanan
        for (int i = 1; i <=jumlahMenu; i++) {
            int hargaMenu = in.nextInt();
            menu[i] = new Makanan(hargaMenu, in.nextChar());
            totalHargaMenu += hargaMenu;
        }

        // ambil jumlah koki
        int jumlahKoki = in.nextInt();

        char tipeKoki;
        // membaca input koki
        for (int i = 1; i <= jumlahKoki; i++) {
            tipeKoki = in.nextChar();
            // menambahkan koki sesuai tipenya
            if (tipeKoki == 'S') {
                Koki baru = new Koki(i, tipeKoki);
                kokiS.add(baru); kokiAll.add(baru);
            } else if (tipeKoki == 'G') {
                Koki baru = new Koki(i, tipeKoki);
                kokiG.add(baru); kokiAll.add(baru);
            } else if (tipeKoki == 'A') {
                Koki baru = new Koki(i, tipeKoki);
                kokiA.add(baru); kokiAll.add(baru);
            }
        }

        // ambil jumlah pelanggan
        int jumlahPelanggan = in.nextInt(); 
        pelanggan = new Pelanggan[jumlahPelanggan+1]; pelanggan[0] = new Pelanggan();
        // generate pelanggan default
        for (int i = 1; i <= jumlahPelanggan; i++) {
            pelanggan[i] = new Pelanggan();
        }

        // ambil jumlah kursi dan jumlah hari
        jumlahKursi = in.nextInt();
        int jumlahHari = in.nextInt();

        // menjalankan hari yang dimasukkan
        for (int i = 1; i <= jumlahHari; i++) {
            jalaniHari();
        }

        // Tutup OutputStream
        out.close();
    }

    public static void jalaniHari() {
        // ---------------------------- AMBIL INPUT HARIAN -------------------------------------
        // RESET ALL VALUE EVERYDAY
        int jumlahPelangganHarian = in.nextInt();
        int jumlahKursiKosong = jumlahKursi; // reset ke jumlah kursi toko
        pesanan.clear(); // reset pesanan

        // MEMOIZATION sumOfPOs
        // K[id_pelanggan] = K[id_pelanggan-1] - K[id_pelanggan-r]
        int sumPos = 0;
        int[] sumOfPos = new int[jumlahPelangganHarian+1];
        sumOfPos[0] = 0; // default batas

        // STEP 1: INISIASI PELANGGAN
        for (int i = 1; i <= jumlahPelangganHarian; i++) {

            // ambil i, k, u, r (opsional)
            int id = in.nextInt();
            char k = in.nextChar();
            int u = in.nextInt();  pelanggan[id].U = u;

            // mendapatkan K untuk pelanggan CLEAR: O(1)
            // jika ? maka cek dulu + atau -
            if (k == '?') {
                // GET: k
                int r = in.nextInt(); 
                // hitung banyak positif dan negatif dalam range
                int sumPositifinRange = sumOfPos[i-1] - sumOfPos[i-r-1];
                int sumNegatifinRange = r - sumPositifinRange;
                // tambah keterangan yang sesuai
                if (sumNegatifinRange < sumPositifinRange) { // saat jumlah (+) > jumlah (-)
                    sumPos++; 
                }
            } else {
                // jika positif maka tambah sumPos dan masukkan ke index yang baru
                if (k == '+') {
                    sumPos++; 
                }
            }
            // sesuaikan dengan jumlah sumPos saat ini
            sumOfPos[i] = sumPos;

            // STEP 2: CETAK STATUS PELANGGAN
            // lakukan penyelesaian A: status pelanggan harian (0-1-2-3)
            if (pelanggan[id].blacklist) {
                out.print("3 "); // blacklisted
            } else {
                if (sumOfPos[i] > sumOfPos[i-1]) { // terdapat perubahan -> ada tambah positif 1
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
        }
        out.println();

        // STEP 3: JALANKAN QUERY
        int jumlahQuery = in.nextInt();
        // membaca kueri
        for (int i = 1; i <= jumlahQuery; i++) { // kueri ke-i
            char kueri = in.nextChar(); 

            // jalankan fungsi kueri
            if (kueri == 'P') {
                runP(in.nextInt(), in.nextInt());
            } else if (kueri == 'L') {
                runL();
            } else if (kueri == 'B') {
                runB(in.nextInt());
            } else if (kueri == 'C') {
                runC(in.nextInt());
                out.println(); // jan dihapus, mencetak batas untuk bawahnya agar bisa masuk tc new line
            } else { // kueri == 'D'
                runD(in.nextInt(), in.nextInt(), in.nextInt());
            }
        }
    }

    // Fungsi-fungsi kueri
    public static void runP(int idPelanggan, int idMenu) {
        // mencari id koki pelayanan
        char tipeMakanan = menu[idMenu].tipe;// tipe makanan dicari
        // PENTING: ambil koki terdepan = koki paling sedikit melayani & pastinya urut id
        Koki kokiPelayan = getKokiMinimum(tipeMakanan); // O(1)
        // menambahkan pesanan baru ke pesanan
        pesanan.add(new Pesanan(idPelanggan, idMenu, kokiPelayan));
        out.println(kokiPelayan.id); // OUTPUT
    }

    // method mengembalikan koki pelayan
    public static Koki getKokiMinimum(char tipe) {
        // mencari koki minimum
        if (tipe == 'S') {
            return kokiS.peek();
        } else if (tipe == 'G') {
            return kokiG.peek();
        } else { // tipe == 'A'
            return kokiA.peek();
        }
    }
    
    public static void runL() {
        // melakukan penyelesaian pesanan terdepan
        Pesanan pesananSelesai = pesanan.remove();
        int hargaMenu = menu[pesananSelesai.idMakanan].harga;

        Koki kokiPelayan = pesananSelesai.kokiPelayan; 
        kokiPelayan.jumlahPelayanan += 1; // menambah jumlah pelayanan kokiPelayan

        // fix drawback priority queue java (remove dulu lalu add kembali for prioritizing)
        if (menu[pesananSelesai.idMakanan].tipe == 'S') {
            kokiS.remove(kokiPelayan); // hapus kokiPelayan dari kokiS
            kokiS.add(kokiPelayan);
        } else if (menu[pesananSelesai.idMakanan].tipe == 'G') {
            kokiG.remove(kokiPelayan); // hapus kokiPelayan dari kokiG
            kokiG.add(kokiPelayan);
        } else { // menu[pesananSelesai.idMakanan].tipe == 'A'
            kokiA.remove(kokiPelayan); // hapus kokiPelayan dari kokiA
            kokiA.add(kokiPelayan);
        }
        
        // uang pelanggan dikurangi
        pelanggan[pesananSelesai.idPelanggan].U -= hargaMenu;

        out.println(pesananSelesai.idPelanggan);
    }

    public static void runB(int idPelanggan) {
        // cek uang pelanggan, jika minus maka blacklist pelanggan
        // lalu cetak pembayaran (0 jika tidak mampu bayar, 1 jika mampu bayar)
        if (pelanggan[idPelanggan].U < 0) {
            pelanggan[idPelanggan].blacklist = true;
            out.println("0"); // OUTPUT
        } else {
            out.println("1"); // OUTPUT
        }
    }

    public static void runC(int Q) {
        // keluarkan by jumlahPelayanan
        Collections.sort(kokiAll);
        for (Koki k: kokiAll) {
            if (Q == 0) {
                break;
            }
            out.print(k.id+" "); // OUTPUT
            Q--;
        }
    }

    public static void runD(int costA, int costG, int costS) {
        // out.println(totalHargaMenu);
        // out.println(costA*jumlahMenu); // harga jika A...A dengan A paling kecil
        out.println(costG*jumlahMenu); // harga jika G...G dengan G paling kecil
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

// class inisiator makanan
class Makanan {
    int harga; char tipe;

    // construct makanan
    Makanan(int harga, char tipe) {
        this.harga = harga;
        this.tipe = tipe;
    }
}

// class inisiator koki
class Koki implements Comparable<Koki> {
    int id; int jumlahPelayanan; char tipe;

    // construct koki
    Koki(int id, char tipe) {
        this.id = id;
        this.jumlahPelayanan = 0; // default = 0
        this.tipe = tipe;
    }

    // compareTo
    @Override
    public int compareTo(Koki other) {
        // jika jumlah pelayanan sama maka urutkan by tipe
        if (this.jumlahPelayanan == other.jumlahPelayanan) {
            // jika tipe sama maka urutkan by id
            if (this.tipe == other.tipe) {
                return this.id - other.id;
            }
            // jika tipe beda maka urutkan by tipe (index S > G > A dalam alphabet)
            // jadi dibalik biar urut S < G < A
            return other.tipe - this.tipe;
        }
        // jika beda urutkan by jumlah pelayanan
        return this.jumlahPelayanan - other.jumlahPelayanan;
    }
}

// class inisiator pelanggan
class Pelanggan {
    long U; boolean blacklist;

    // construct pelanggan default
    Pelanggan() {
        this.U = 0; // default 0
        this.blacklist = false; // default = false
    }
}

// class inisiator antrean
class Pesanan {
    int idPelanggan; int idMakanan; Koki kokiPelayan;

    // constructor pesanan
    Pesanan(int idPelanggan, int idMakanan, Koki kokiPelayan) {
        this.idPelanggan = idPelanggan;
        this.idMakanan = idMakanan;
        this.kokiPelayan = kokiPelayan;
    }
}
