import java.io.*;
import java.util.StringTokenizer;
import java.util.Arrays;

public class TP01v02 {
    private static InputReader in;
    private static PrintWriter out;

    // Rules:
    // *) Indexing data dalam array dimulai dari 1 (bukan 0)
    // *) Isi default sebuah array of object, data field int = 0, char = 'O'

    // default arr[0] = kosong
    public static Makanan[] menu; // index => id makanan
    public static Koki[] koki; // index => id koki
    public static Pelanggan[] pelanggan; // index => id pelanggan

    // get status by queue (optimization step)
    public static int[] KbyQueue = new int[100069];

    public static void main(String[] args) {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        // ---------------------------- AMBIL INPUT DEFAULT -------------------------------------
        // ambil jumlah menu makanan
        int jumlahMenu = in.nextInt();
        menu = new Makanan[jumlahMenu+1]; menu[0] = new Makanan(0, 'O'); // set default
        // membaca input menu makanan
        for (int i = 1; i <=jumlahMenu; i++) {
            menu[i] = new Makanan(in.nextInt(), in.nextChar());
        }

        // ambil jumlah koki
        int jumlahKoki = in.nextInt();
        koki = new Koki[jumlahKoki+1]; koki[0] = new Koki('O'); // set default
        // membaca input koki
        for (int i = 1; i <= jumlahKoki; i++) {
            koki[i] = new Koki(in.nextChar());
        }

        // ambil jumlah pelanggan
        int jumlahPelanggan = in.nextInt(); 
        pelanggan = new Pelanggan[jumlahPelanggan+1]; pelanggan[0] = new Pelanggan();
        // generate pelanggan default
        for (int i = 1; i <= jumlahPelanggan; i++) {
            pelanggan[i] = new Pelanggan();
        }

        // ambil jumlah kursi dan jumlah hari
        int jumlahKursi = in.nextInt();
        int jumlahHari = in.nextInt();

        check1();

        for (int i = 1; i <= jumlahHari; i++) {
            jalaniHari();
            check2(i);
        }

        // Tutup OutputStream
        out.close();
    }

    public static void jalaniHari() {
        // ---------------------------- AMBIL INPUT HARIAN -------------------------------------
        // STEP 1: INISIASI PELANGGAN
        int jumlahPelangganHarian = in.nextInt();
        // membaca input pelanggan harian
        for (int i = 1; i <= jumlahPelangganHarian; i++) {

            // ambil i, k, u, r (opsional)
            int id = in.nextInt();
            char k = in.nextChar(); 
            int u = in.nextInt();  pelanggan[id].setU(u);

            // memasukkan pelanggan sesuai idnya
            if (k == '?') {
                int r = in.nextInt(); 
                pelanggan[id].setK(getKFromQueue(i, r));
            } else {
                int ket =  (k == '+' ? 1 : -1);  // jika + => 1, jika - => -1
                KbyQueue[i] = ket;
                pelanggan[id].setK(ket);
            }
        }

        // STEP 2: JALANKAN QUERY
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
            } else { // kueri == 'D'
                runD(in.nextInt(), in.nextInt(), in.nextInt());
            }
        }
    }

    // Fungsi untuk memberi status pada pelanggan bertipe = "?"
    public static int getKFromQueue(int id, int jarak) {
        int sumStatus = 0;
        int indeks = id;
        while (jarak > 0) {
            indeks--; jarak--; 
            sumStatus += KbyQueue[indeks];
        }
        // Memberi status pada pelanggan
        if (sumStatus > 0) {
            KbyQueue[id] = 1; return 1; // K = positif
        } else {
            KbyQueue[id] = -1; return -1; // K = Negatif
        }
    }

    // Fungsi-fungsi kueri
    public static void runP(int idPelanggan, int idMakanan) {

    }
    
    public static void runL() {
        
    }

    public static void runB(int idPelanggan) {
        
    }

    public static void runC(int Q) {
        
    }

    public static void runD(int costA, int costG, int costS) {
        
    }


    public static void check1() {
        int counter = 0;
        // cek makanan
        out.println("-------------------------------------");
        out.println("CEK MENU: [harga] [tipe]");
        for (Makanan m: menu) {
            counter++;
            out.println(counter + ") " + m.getHarga() + " | " + m.getTipe());
        }
        // cek koki
        counter = 0;
        out.println("-------------------------------------");
        out.println("CEK KOKI: [tipe] [jumlah pelayanan]");
        for (Koki k: koki) {
            counter++;
            out.println(counter + ") " + k.getTipe() + " | " + k.getJumlahPelayanan());
        }
    }

    // cek harian
    public static void check2(int hari) {
        int counter = 0;
        out.println("-------------------------------------");
        out.println("HARI KE-" + hari);
        // cek pelanggan
        out.println("-------------------------------------");
        out.println("CEK PELANGGAN: [K] [U] [blacklist]");
        for (Pelanggan p: pelanggan) {
            counter++;
            out.println(counter + ") " + p.getK() + " | " + p.getU() + " | " + p.isBlacklist());
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

// class inisiator makanan
class Makanan {
    private int harga;
    private char tipe;

    // construct makanan
    Makanan(int harga, char tipe) {
        this.harga = harga;
        this.tipe = tipe;
    }
   
    // getter harga
    public int getHarga() {
        return this.harga;
    }

    // getter tipe
    public char getTipe() {
        return this.tipe;
    }
}

// class inisiator koki
class Koki {
    private char tipe;
    private int jumlahPelayanan;

    // construct koki
    Koki(char tipe) {
        this.tipe = tipe;
        this.jumlahPelayanan = 0; // default = 0
    }
   
    // getter tipe
    public char getTipe() {
        return this.tipe;
    }

    // getter jumlahPelayanan
    public int getJumlahPelayanan() {
        return this.jumlahPelayanan;
    }

    // setter jumlahPelayanan
    public void setJumlahPelayanan(int jumlahPelayanan) {
        this.jumlahPelayanan = jumlahPelayanan;
    }
}

// class inisiator pelanggan
class Pelanggan {
    private int K; // K > 0 (positif), K <= 0 (negatif)
    private int U;
    private boolean blacklist;

    // construct pelanggan default
    Pelanggan() {
        this.K = 0; // default 0
        this.U = 0; // default 0
        this.blacklist = false; // default = false
    }
   
    // getter K : Keterangan
    public int getK() {
        return this.K;
    }

    // getter U : Uang
    public int getU() {
        return this.U;
    }

    // getter blacklist
    public boolean isBlacklist() {
        return this.blacklist;
    }

    // setter K : Keterangan
    public void setK(int K) {
        this.K = K;
    }

    // setter U : Uang
    public void setU(int U) {
        this.U = U;
    }

    // setter blacklist => mengubah pelanggan jadi blacklisted
    public void setBlacklist() {
        this.blacklist = true;
    }
}