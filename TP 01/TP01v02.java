import java.io.*;
import java.util.StringTokenizer;
import java.util.Arrays;
import java.util.Queue;
import java.util.LinkedList;
import java.util.Comparator;

public class TP01v02 {
    private static InputReader in;
    private static PrintWriter out;

    // Rules:
    // *) Indexing data dalam array dimulai dari 1 (bukan 0)
    // *) Isi default sebuah array of object, data field int = 0, char = 'O'

    // default arr[0] = kosong
    public static Makanan[] menu; // index => id makanan

    // Query P & L
    public static LinkedList<Koki> kokiS = new LinkedList<>(); // kokiS (terurut minimal melayani)
    public static LinkedList<Koki> kokiG = new LinkedList<>();  // kokiG (terurut minimal melayani)
    public static LinkedList<Koki> kokiA = new LinkedList<>();  // kokiA (terurut minimal melayani)
    // Query C
    public static Koki[] semuaKoki;

    public static Pelanggan[] pelanggan; // index => id pelanggan

    public static Queue<Pesanan> pesanan = new LinkedList<>(); // menyimpan antrian pesanan

    // jumlah kursi
    public static int jumlahKursi;

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
        int jumlahKoki = in.nextInt(); semuaKoki = new Koki[jumlahKoki];
        char tipeKoki;
        // membaca input koki
        for (int i = 1; i <= jumlahKoki; i++) {
            tipeKoki = in.nextChar();
            // menambahkan koki sesuai tipenya
            if (tipeKoki == 'S') {
                Koki kokiBaru = new Koki(i, tipeKoki);
                kokiS.add(kokiBaru); semuaKoki[i-1] = kokiBaru;
            } else if (tipeKoki == 'G') {
                Koki kokiBaru = new Koki(i, tipeKoki);
                kokiG.add(kokiBaru); semuaKoki[i-1] = kokiBaru;
            } else { // tipeKoki == 'A'
                Koki kokiBaru = new Koki(i, tipeKoki);
                kokiA.add(kokiBaru); semuaKoki[i-1] = kokiBaru;
                // CEK ADDRESS OF OBJECT (OBJECT REFERENCE YANG DIMASUKKAN KE DALAM ARRAY)
                // out.println("CEK ADDRESS 1: "+kokiBaru);
                // out.println("CEK ADDRESS 2: "+kokiA.get(kokiA.size()-1));
                // out.println("CEK ADDRESS 3: "+semuaKoki[i-1]);
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

        // check1();

        // menjalankan hari yang dimasukkan
        for (int i = 1; i <= jumlahHari; i++) {
            jalaniHari();
            // check2(i);
        }

        // check1();

        // Tutup OutputStream
        out.close();
    }

    public static void jalaniHari() {
        // ---------------------------- AMBIL INPUT HARIAN -------------------------------------
        // RESET ALL VALUE EVERYDAY
        int jumlahPelangganHarian = in.nextInt();
        int jumlahKursiKosong = jumlahKursi; // reset ke jumlah kursi toko
        pesanan.clear(); // reset pesanan

        // STEP 1: INISIASI PELANGGAN
        // membaca input pelanggan harian
        for (int i = 1; i <= jumlahPelangganHarian; i++) {

            // ambil i, k, u, r (opsional)
            int id = in.nextInt();
            char k = in.nextChar(); int ket;
            int u = in.nextInt();  pelanggan[id].setU(u);

            // mendapatkan K untuk pelanggan
            if (k == '?') {
                int r = in.nextInt(); 
                ket = getKFromQueue(i, r);       
            } else {
                ket =  (k == '+' ? 1 : -1);  // jika + => 1, jika - => -1
                KbyQueue[i] = ket;
            }
            pelanggan[id].setK(ket);

            // STEP 2: CETAK STATUS PELANGGAN
            // lakukan penyelesaian A: status pelanggan harian (0-1-2-3)
            if (pelanggan[id].isBlacklist()) {
                out.print("3 "); // blacklisted
            } else {
                if (ket == 1) {
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
                // checkP();
            } else if (kueri == 'L') {
                runL();
                // checkC();
                // checkListKoki();
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
    // menampilkan id koki pelayan
    public static void runP(int idPelanggan, int idMenu) { // CLEAR
        char tipeMakanan = menu[idMenu].getTipe(); // tipe makanan dicari
        // mendapatkan koki pelayan
        Koki kokiPelayan = new Koki(0, 'S'); // default (tidak ada koki index 0 jadi aman)
        if (tipeMakanan == 'S') { kokiPelayan = kokiS.getFirst(); }
        else if (tipeMakanan == 'G') { kokiPelayan = kokiG.getFirst(); }
        else { kokiPelayan = kokiA.getFirst(); } // tipeMakanan = 'A'
        // menambahkan pesanan baru ke pesanan
        pesanan.add(new Pesanan(idPelanggan, idMenu, kokiPelayan));
        out.println("P: "+kokiPelayan.getId()); // OUTPUT
    }

    public static void checkP() {
        out.println("\n-------------------------------------");
        out.println("CHECK P");
        out.println("-------------------------------------");
        // CHECK
        for (int i = 0; i < kokiS.size(); i++) {
            out.print("S["+kokiS.get(i).getId()+"]: "+kokiS.get(i).getJumlahPelayanan()+" ");
        }
        for (int i = 0; i < kokiG.size(); i++) {
            out.print("G["+kokiG.get(i).getId()+"]: "+kokiG.get(i).getJumlahPelayanan()+" ");
        }
        for (int i = 0; i < kokiA.size(); i++) {
            out.print("A["+kokiA.get(i).getId()+"]: "+kokiA.get(i).getJumlahPelayanan()+" ");
        }
    }
    

    // menampilkan id koki yang melayani
    // references:
    // linkedlist: https://www.w3schools.com/java/java_linkedlist.asp
    // insert element in the middle of array: https://www.geeksforgeeks.org/how-to-insert-an-element-at-a-specific-position-in-an-array-in-java/
    public static void runL() {
        // melakukan penyelesaian pesanan terdepan
        Pesanan pesananSelesai = pesanan.remove();
        int hargaMenu = menu[pesananSelesai.getIdMakanan()].getHarga();
        char tipeMenu = menu[pesananSelesai.getIdMakanan()].getTipe();

        Koki kokiPelayan = pesananSelesai.getkokiPelayan(); 
        kokiPelayan.tambahJumlahPelayanan(); // menambah jumlah pelayanan kokiPelayan

        // binary search insertion untuk memindah kokiPelayanan sesuai urutan dalam koki (linkedlist)
        moveKoki(kokiPelayan, tipeMenu);

        // uang pelanggan dikurangi
        pelanggan[pesananSelesai.getIdPelanggan()].kurangiU(hargaMenu);   
        
        out.println("L: "+pesananSelesai.getIdPelanggan());
    }

    // method move koki algo binary search insertion with sorted linkedlist
    public static void moveKoki(Koki kokiPelayan, char tipeKoki) {
        if (tipeKoki == 'S') {
            // menghapus elemen pertama dari linkedlist (koki pelayan)
            kokiS.removeFirst();
            // binary search digunakan untuk mencari index yang tepat untuk memasukkan kokiPelayan
            int indeks = findInsertIndex(kokiS, kokiS.size(), kokiPelayan);
            // memindahkan kokiPelayan ke indeks yang tepat
            kokiS.add(indeks, kokiPelayan);
        } else if (tipeKoki == 'G') {
            kokiG.removeFirst();
            int indeks = findInsertIndex(kokiG, kokiG.size(), kokiPelayan);
            kokiG.add(indeks, kokiPelayan);
        } else { // tipeKoki == 'A'
            kokiA.removeFirst();
            int indeks = findInsertIndex(kokiA, kokiA.size(), kokiPelayan);
            kokiA.add(indeks, kokiPelayan);
        }
    }

    // function to find insert position of kokiPelayan
    public static int findInsertIndex(LinkedList<Koki> koki, int n, Koki kokiPelayan) {
        // Base cases
        if (n == 0)
            return 0;
        if (kokiPelayan.getJumlahPelayanan() >= koki.get(n - 1).getJumlahPelayanan())
            return n;
     
        // Binary search
        int low = 0, high = n - 1;
        while (low <= high)
        {
            int mid = (low + high) / 2;
            if (koki.get(mid).getJumlahPelayanan() > kokiPelayan.getJumlahPelayanan())
                high = mid - 1;
            else
                low = mid + 1;
        }
     
        return low;
    }

    public static void checkL() {
        out.println("\n-------------------------------------");
        out.println("CHECK L");
        out.println("-------------------------------------");
        // CHECK
        for (int i = 0; i < kokiS.size(); i++) {
            out.print("S["+kokiS.get(i).getId()+"]: "+kokiS.get(i).getJumlahPelayanan()+" ");
        }
        for (int i = 0; i < kokiG.size(); i++) {
            out.print("G["+kokiG.get(i).getId()+"]: "+kokiG.get(i).getJumlahPelayanan()+" ");
        }
        for (int i = 0; i < kokiA.size(); i++) {
            out.print("A["+kokiA.get(i).getId()+"]: "+kokiA.get(i).getJumlahPelayanan()+" ");
        }
    }

    public static void runB(int idPelanggan) {
        // cek uang pelanggan, jika minus maka blacklist pelanggan
        // lalu cetak pembayaran (0 jika tidak mampu bayar, 1 jika mampu bayar)
        if (pelanggan[idPelanggan].getU() < 0) {
            pelanggan[idPelanggan].setBlacklist();
            out.println("B: 0"); // OUTPUT
        } else {
            out.println("B: 1"); // OUTPUT
        }
    }

    public static void runC(int Q) {
        // menampilkan Q data koki paling kecil (prioritas S > G > A)
        out.println("C: ");
        
        // lakukan sorting untuk mendapati Q data terbawah
        Arrays.sort(semuaKoki, new SortbyPelayananNtipeNId());

        int counter = 0;
        while (counter < Q) {
            out.println(semuaKoki[counter].getId() + " | " + semuaKoki[counter].getJumlahPelayanan());
            counter++;
        }

        checkC();
    }

    public static void checkC() {
        out.println("CHECK QUERY C SORTED: ");
        int counter = 0;
        while (counter < semuaKoki.length) {
            out.println("ID: "+semuaKoki[counter].getId() + " | Pelayanan: " + semuaKoki[counter].getJumlahPelayanan());
            counter++;
        }
    }

    public static void runD(int costA, int costG, int costS) {
        
    }


    public static void check1() {
        int counter = 0;
        // cek makanan
        out.println("-------------------------------------");
        out.println("CEK MENU: [harga] [tipe]");
        for (Makanan m: menu) {
            out.println(counter + ") " + m.getHarga() + " | " + m.getTipe());
            counter++;
        }
        // cek koki
        counter = 0;
        out.println("-------------------------------------");
        out.println("CEK KOKI S: [id] [jumlah pelayanan]");
        for (Koki k: kokiS) {
            out.println(counter + ") " + k.getId() + " | " + k.getJumlahPelayanan());
            counter++;
        }
        counter = 0;
        out.println("-------------------------------------");
        out.println("CEK KOKI G: [id] [jumlah pelayanan]");
        for (Koki k: kokiG) {
            out.println(counter + ") " + k.getId() + " | " + k.getJumlahPelayanan());
            counter++;
        }
        counter = 0;
        out.println("-------------------------------------");
        out.println("CEK KOKI A: [id] [jumlah pelayanan]");
        for (Koki k: kokiA) {
            out.println(counter + ") " + k.getId() + " | " + k.getJumlahPelayanan());
            counter++;
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
            out.println(counter + ") " + p.getK() + " | " + p.getU() + " | " + p.isBlacklist());
            counter++;
        }
    }

    public static void checkListKoki() {
        // out.println("CEK KOKI S:" + kokiS.size());
        // // menampilkan semua kokiS
        // for (int i = 0; i < kokiS.size(); i++) {
        //     out.print("S["+kokiS.get(i).getId()+"]: "+kokiS.get(i).getJumlahPelayanan()+" ");
        // }
        // out.print("CEK KOKI G:");
        // for (Koki k: kokiG) {
        //     out.println(k.getId()+" | "+k.getJumlahPelayanan());
        // }
        out.println("CEK KOKI A:");
        for (Koki k: kokiA) {
            out.println(k.getId()+" | "+k.getJumlahPelayanan());
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
    private int id;
    private int jumlahPelayanan;
    private char tipe;

    // construct koki
    Koki(int id, char tipe) {
        this.id = id;
        this.jumlahPelayanan = 0; // default = 0
        this.tipe = tipe;
    }

    // getter id
    public int getId() {
        return this.id;
    }

    // getter jumlahPelayanan
    public int getJumlahPelayanan() {
        return this.jumlahPelayanan;
    }

    // getter tipe
    public char getTipe() {
        return this.tipe;
    }

    // setter penambah jumlahPelayanan
    public void tambahJumlahPelayanan() {
        this.jumlahPelayanan += 1;
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

    // setter kurangi U : Uang
    public void kurangiU(int harga) {
        this.U -= harga;
    }

    // setter blacklist => mengubah pelanggan jadi blacklisted
    public void setBlacklist() {
        this.blacklist = true;
    }
}

// class inisiator antrean
class Pesanan {
    private int idPelanggan; 
    private int idMakanan;
    private Koki kokiPelayan;

    // constructor pesanan
    Pesanan(int idPelanggan, int idMakanan, Koki kokiPelayan) {
        this.idPelanggan = idPelanggan;
        this.idMakanan = idMakanan;
        this.kokiPelayan = kokiPelayan;
    }

    // getter idPelanggan
    public int getIdPelanggan() {
        return idPelanggan;
    }

    // getter idMakanan
    public int getIdMakanan() {
        return idMakanan;
    }

    // getter idKokiPelayan
    public Koki getkokiPelayan() {
        return this.kokiPelayan;
    }
}

class SortbyPelayananNtipeNId implements Comparator<Koki>
{
    // Used for sorting in ascending order of
    // roll number
    public int compare(Koki a, Koki b)
    {
        // CHECK COMPARE +/- NYA
        // jika jumlah pelayanan sama maka sort by tipe
        if (a.getJumlahPelayanan() == b.getJumlahPelayanan()) {
            // jika tipe sama maka sort by id
            if (a.getTipe() == b.getTipe()) {
                return a.getId() - b.getId();
            }
            // jika jumlah pelayanan sama tapi tipe tidak sama maka sort by S > G > A 
            // kuncinya adalah index S lebih besar dari index G dan index G lebih besar dari index A dalam ASCII
            return b.getTipe() - a.getTipe(); // a musti kurang dari b
        }
        // jika jumlah pelayanan tidak sama maka sort langsung by jumlah pelayanan
        return a.getJumlahPelayanan() - b.getJumlahPelayanan();
    }
}
 