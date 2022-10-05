import java.io.*;
import java.util.StringTokenizer;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;
import java.util.Collections;

public class TP01v02 {
    private static InputReader in;
    private static PrintWriter out;

    // Rules:
    // *) Indexing data dalam array dimulai dari 1 (bukan 0)
    // *) Isi default sebuah array of object, data field int = 0, char = 'O'

    // default arr[0] = kosong
    public static Makanan[] menu; // index => id makanan
    public static String strMenu = "Z"; // index 0 dari strMenu tidak dipakai

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
    public static boolean runDFirst = false;
    public static int costA; public static int costG; public static int costS;
    // just first time
    public static TreeMap<Node, Character> memoAllSequence = new TreeMap<Node, Character>(); // node save start,end | string save format char saat itu
    // all time map: {key:start > key:end > value}
    public static TreeMap<Integer, TreeMap<Integer, Integer>> memoCostbySequence = new TreeMap<Integer, TreeMap<Integer, Integer>>(); // node save start,end | integer total cost
    public static TreeMap<Integer, TreeMap<Integer, Integer>> memoMinCost = new TreeMap<Integer, TreeMap<Integer, Integer>>(); // node save start,end | integer min cost on sequence start,end

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
            int harga = in.nextInt();
            char tipe = in.nextChar();
            menu[i] = new Makanan(harga, tipe);
            // ambil versi string panjang dari menu restoran
            strMenu += tipe;
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
                // out.println("===== COMMAND P =====");
                // checkC();
            } else if (kueri == 'L') {
                runL();
                // out.println("===== COMMAND L =====");
                // checkC();
            } else if (kueri == 'B') {
                runB(in.nextInt());
            } else if (kueri == 'C') {
                runC(in.nextInt());
                out.println(); // jan dihapus, mencetak batas untuk bawahnya agar bisa masuk tc new line
            } else { // kueri == 'D'
                // set value of costs
                costA = in.nextInt(); costG = in.nextInt(); costS = in.nextInt();
                runD();
                // untuk set D pernah dijalankan setidaknya sekali
                if (!runDFirst) {
                    runDFirst = true;
                }
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
        out.println("P: "+kokiPelayan.id); // OUTPUT
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

        out.println("L: "+pesananSelesai.idPelanggan);
    }

    public static void runB(int idPelanggan) {
        // cek uang pelanggan, jika minus maka blacklist pelanggan
        // lalu cetak pembayaran (0 jika tidak mampu bayar, 1 jika mampu bayar)
        if (pelanggan[idPelanggan].U < 0) {
            pelanggan[idPelanggan].blacklist = true;
            out.println("B: 0"); // OUTPUT
        } else {
            out.println("B: 1"); // OUTPUT
        }
    }

    public static void runC(int Q) {
        // checkC();
        // keluarkan by jumlahPelayanan
        // copy priority queue
        // TODO: Create New Sorted DS vs Copying Like This
        Collections.sort(kokiAll);
        for (Koki k: kokiAll) {
            if (Q == 0) {
                break;
            }
            out.print(k.id+" ");
            Q--;
        }
    }

    // A S G S G A G
    // A S G S G A G
    // A (S G S) (G A G)
    // A (S G S) G A G
    // (A S G S G A) G
    // A S (G S G) A G
    // A S (G S G A G)

    // find combination of substring with start and end same
    public static void runD() {
        if (!runDFirst) { // jika D baru pertama kali dijalankan maka findAllSequence to memo
            findAllSequence(1,1);
        }
        // cari totalCost dari sequence lalu generate ke TreeMap
        memoCostbySequence.clear(); // clear memo dulu
        computeCostbySequence();
        // cari harga paling minimum dari kombinasi sequence
        out.println("CHECK D: "+findMinimumCost(1,memoCostbySequence.size())); // CHECK APAKAH SIZE ATAU SIZE+1
    }

    // method mengumpulkan sequence(substring) dengan char start == char end
    public static int findAllSequence(int start,int end) {
        // saat sudah mencapai end tapi start == end
        if(end == strMenu.length()){
          start++; end = start;
        }
        // saat iterasi telah mencapai panjang string maka sudahi
        if(end == strMenu.length())
          return 0;

        // jika char start dan end sama maka tambahkan ke memo
        if(strMenu.charAt(start) == strMenu.charAt(end)) {
            out.println("POTONG: "+strMenu.substring(start, end+1)); // TEST
            
            // simpan ke memo
            // memo key=(start,end), val=(char format)
            memoAllSequence.put(new Node(start, end), strMenu.charAt(start));

            // cari lagi sequence dengan start dan end huruf sama
            return findAllSequence(start, end + 1);

        // jika char di index itu tidak sama maka skip dulu gan
        } else {
            return findAllSequence(start, end + 1); 
        }
    }

    public static void computeCostbySequence() {
        for (Map.Entry<Node, Character> entry : memoAllSequence.entrySet()) {
            Node key = entry.getKey();
            Character val = entry.getValue();
            int totalCost = 0;
            // cari cost dari sequence
            if (key.start == key.end) { // artinya cuma sehuruf doang gausa pake paket 
                totalCost = menu[key.start].harga; // sesuai harga menu
            } else {
                // hitung harga berdasarkan kalkulasi soal (per A, G, S) paketan
                if (val == 'A') {
                    totalCost += (key.end-key.start+1)*costA; 
                } else if (val == 'G') {
                    totalCost += (key.end-key.start+1)*costG;
                } else { // val == 'S'
                    totalCost += (key.end-key.start+1)*costS;
                }
            }

            out.println("MAP: ("+key.start+","+key.end+"): "+totalCost); // TEST
        
            // simpan ke memo key=start, val=map(key:end, val:totalCost)
            TreeMap<Integer, Integer> endVal = new TreeMap<Integer, Integer>(); // end : val (totalCost)
            endVal.put(key.end, totalCost);
            memoCostbySequence.put(key.start, endVal);
        }
    }

    // find optimal solution = minimum cost of sequence combination
    // dynamic programming find optimal solution
    public static int findMinimumCost(int start, int end) {
        // jika sudah ada dalam memo tinggal ambil aja
        if (memoMinCost.containsKey(start)) { // jika ada key start di memo
            if (memoMinCost.get(start).containsKey(end)) { // jika ada key end di memo
                // jika didapati start,end yang sudah ada maka ambil aja valuenya langsung
                return memoMinCost.get(start).get(end);
            }
        }

        // jika belum ada maka cari yg minimum
        if (start >= end) { // saat hurufnya sama
            return 0;
        } else {
            // inisiasi variabel
            int cost = 0;
            int costSequence = 0;
            int minCost = Integer.MAX_VALUE;

            // pada setiap percabangan sequence
            for (int i = start; i < end; i++) {
                // cari cost dari sequence
                if (memoCostbySequence.containsKey(start)) { // jika ada key start di memo
                    if (memoCostbySequence.get(start).containsKey(end)) { // jika ada key end di memo
                        // jika didapati start,end yang sudah ada maka ambil aja valuenya langsung
                        costSequence = memoCostbySequence.get(start).get(end);
                        cost = costSequence + findMinimumCost(i+1, end);
                    }
                } else {
                    // jika tidak ada start end di memoCostbySequence
                    cost = findMinimumCost(start, i) + findMinimumCost(i+1, end);
                }
                // jika lebih kecil maka gunakan yang itu
                if (cost < minCost) {
                    minCost = cost;
                }
            }

            // minimal cost pada suatu sequence disimpan ke memo 
            TreeMap<Integer, Integer> endVal = new TreeMap<Integer, Integer>(); // end : val (minCost)
            endVal.put(end, minCost);
            memoMinCost.put(start, endVal); // key:start, val:map(key:end, val:minCost)
            return minCost;
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

// class inisiator node
class Node implements Comparable<Node> {
    int start; int end;

    // constructor node
    Node(int start, int end) {
        this.start = start;
        this.end = end;
    }

    // compareTo
    @Override
    public int compareTo(Node other) {
        // jika start sama maka urutkan by end
        if (this.start == other.start) {
            return this.end - other.end;
        }
        // jika beda urutkan by start
        return this.start - other.start;
    }
}