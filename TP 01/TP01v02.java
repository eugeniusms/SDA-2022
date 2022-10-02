import java.io.*;
import java.util.StringTokenizer;
import java.util.Queue;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class TP01v02 {
    private static InputReader in;
    private static PrintWriter out;

    // Rules:
    // *) Indexing data dalam array dimulai dari 1 (bukan 0)
    // *) Isi default sebuah array of object, data field int = 0, char = 'O'

    // default arr[0] = kosong
    public static Makanan[] menu; // index => id makanan

    public static ArrayList<Koki> kokiS = new ArrayList<>(); // kokiS (terurut minimal melayani)
    public static ArrayList<Koki> kokiG = new ArrayList<>(); // kokiG (terurut minimal melayani)
    public static ArrayList<Koki> kokiA = new ArrayList<>(); // kokiA (terurut minimal melayani)

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
        int jumlahKoki = in.nextInt();
        char tipeKoki;
        // membaca input koki
        for (int i = 1; i <= jumlahKoki; i++) {
            tipeKoki = in.nextChar();
            // menambahkan koki sesuai tipenya
            if (tipeKoki == 'S') {
                kokiS.add(new Koki(i)); // inisiasi id koki
            } else if (tipeKoki == 'G') {
                kokiG.add(new Koki(i));
            } else { // tipeKoki == 'A'
                kokiA.add(new Koki(i));
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
                checkP();
            } else if (kueri == 'L') {
                runL();
                checkL();
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
    public static void runP(int idPelanggan, int idMenu) {
        // mencari id koki pelayanan
        char tipeMakanan = menu[idMenu].getTipe(); // tipe makanan dicari
        // PENTING: ambil koki terdepan = koki paling sedikit melayani & pastinya urut id
        Koki kokiPelayan;
        if (tipeMakanan == 'S') {
            kokiPelayan = kokiS.get(0);
        } else if (tipeMakanan == 'G') {
            kokiPelayan = kokiG.get(0);
        } else { // tipe makanan A
            kokiPelayan = kokiA.get(0);
        }
        pesanan.add(new Pesanan(idPelanggan, idMenu, kokiPelayan));
        // out.println("P: "+idKokiPelayan); // OUTPUT
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
    
    public static void runL() {
        // melakukan penyelesaian pesanan terdepan
        Pesanan pesananSelesai = pesanan.remove();
        int hargaMenu = menu[pesananSelesai.getIdMakanan()].getHarga();

        Koki kokiPelayan = pesananSelesai.getkokiPelayan(); 
        kokiPelayan.tambahJumlahPelayanan(); // menambah jumlah pelayanan kokiPelayan

        // uang pelanggan dikurangi
        pelanggan[pesananSelesai.getIdPelanggan()].kurangiU(hargaMenu);

        // sorting setiap koki pada setiap query L dijalankan
        // selectionSort(kokiS); selectionSort(kokiG); selectionSort(kokiA);
        Collections.sort(kokiS, new SortbyPelayananNId());
        Collections.sort(kokiG, new SortbyPelayananNId());
        Collections.sort(kokiA, new SortbyPelayananNId());

        // out.println("L: "+pesananSelesai.getIdPelanggan());
    }

    // selection sort digunakan untuk sorting data hampir terurut (setiap perulangan diurutkan)
    // urutkan berdasarkan jumlah pelayanan lalu id
    // public static void selectionSort(ArrayList<Koki> arr)
    // {
    //     int n = arr.size();
 
    //     // sort berdasarkan jumlah pelayanan
    //     // One by one move boundary of unsorted subarray
    //     for (int i = 0; i < n-1; i++)
    //     {
    //         // Find the minimum element in unsorted array
    //         int min_idx = i;
    //         for (int j = i+1; j < n; j++)
    //             if (arr.get(j).getJumlahPelayanan() < arr.get(min_idx).getJumlahPelayanan())
    //                 min_idx = j;
 
    //         // Swap the found minimum element with the first
    //         // element
    //         Koki temp = arr.get(min_idx);
    //         arr.set(min_idx, arr.get(i));
    //         arr.set(i, temp);
    //     }
    // }

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
            // out.println("B: 0"); // OUTPUT
        } else {
            // out.println("B: 1"); // OUTPUT
        }
    }

    public static void runC(int Q) {
        // copy queue agar tidak mengganggu jalannya program 
        // https://stackoverflow.com/questions/22982157/how-do-i-copy-or-clone-a-linkedlist-implemented-queue-in-java
        Queue<Koki> copyKokiS = new LinkedList<>(kokiS); // kokiS (terurut minimal melayani)
        Queue<Koki> copyKokiG = new LinkedList<>(kokiG); // kokiS (terurut minimal melayani)
        Queue<Koki> copyKokiA = new LinkedList<>(kokiA); // kokiS (terurut minimal melayani)

        // CHECK
        // for (Koki cs: copyKokiS) {
        //     out.print("S: "+cs.getJumlahPelayanan()+" ");
        // }
        // for (Koki cg: copyKokiG) {
        //     out.print("G: "+cg.getJumlahPelayanan()+" ");
        // }
        // for (Koki ca: copyKokiA) {
        //     out.print("A: "+ca.getJumlahPelayanan()+" ");
        // }

        // menampilkan Q data koki paling kecil (prioritas S > G > A)
        // out.print("C: ");
        while (Q > 0) {
            // susun array [Shead, Ghead, Ahead] berisi jumlah pelayanan pada head of SGA
            int[] SGAhead = {copyKokiS.peek().getJumlahPelayanan(), copyKokiG.peek().getJumlahPelayanan(), copyKokiA.peek().getJumlahPelayanan()};
            
            // TODO: selesaikan isu indexing
            // out.println("CEK: "+copyKokiS.peek().getJumlahPelayanan());

            // mencari index yang paling minimum
            int minimumPelayanan = 999999;
            int indexMinimum = 0; // [0: S, 1: G, 2: A]
            for (int i = 0; i < 3; i++) {
                if (SGAhead[i] < minimumPelayanan) {
                    minimumPelayanan = SGAhead[i];
                    indexMinimum = i;
                }
            }
            // maka S minimum
            if (indexMinimum == 0) {
                Koki head = copyKokiS.remove();
                // out.print(head.getId()+" "); // OUTPUT
                copyKokiS.add(head); // kembalikan ke belakang

            // maka G minimum
            } else if (indexMinimum == 1) {
                Koki head = copyKokiG.remove();
                // out.print(head.getId()+" "); // OUTPUT
                copyKokiG.add(head); // kembalikan ke belakang

            // maka A minimum
            } else {
                Koki head = copyKokiA.remove();
                // out.print(head.getId()+" "); // OUTPUT
                copyKokiA.add(head); // kembalikan ke belakang
            }

            Q--; // counter
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

    // construct koki
    Koki(int id) {
        this.id = id;
        this.jumlahPelayanan = 0; // default = 0
    }

    // getter id
    public int getId() {
        return this.id;
    }

    // getter jumlahPelayanan
    public int getJumlahPelayanan() {
        return this.jumlahPelayanan;
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

class SortbyPelayananNId implements Comparator<Koki>
{
    // Used for sorting in ascending order of
    // roll number
    public int compare(Koki a, Koki b)
    {
        // jika jumlah pelayanan sama maka sort by id
        if (a.getJumlahPelayanan() == b.getJumlahPelayanan()) {
            return a.getId() - b.getId();
        }
        // jika tidak maka sort langsung by jumlah pelayanan
        return a.getJumlahPelayanan() - b.getJumlahPelayanan();
    }
}
 