import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.*;

// javac Submitted06ClearInisiasi.java


public class Submitted07FixRTE {

    private static InputReader in;
    static PrintWriter out;

    // [(gedung A),(gedung B),...]
    public static Gedung[] kompleks;
    // ex: (gedung A => menyimpan first and last)
    // first: lantai 1, .next: lantai 2 dst
    // public static Karakter[] pemain = new Karakter[2]; // [0]: Denji, [1]: Iblis
    public static Karakter denji;
    public static Karakter iblis;

    // counter untuk menghitung secara matematis letak lantai
    // menghindari O(n) by linkedlist search menjadi math O(1)
    // DENJI (GERAK) => +1 / -1
    // DENJI (HANCUR) => -1
    // IBLIS (GERAK) => +2 / -2
    // IBLIS (HANCUR) => TETAP (TAPI DENJI NAIK KALAU DENJI SEGEDUNG DENGAN IBLIS)
    public static int counterLantaiDenji;
    public static int counterLantaiIblis;
    public static int counterPertemuan = 0;


    public static void main(String[] args) {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        // ===== INISIASI KOMPLEKS GEDUNG =====
        int jumlahGedung = in.nextInt(); kompleks = new Gedung[jumlahGedung];
        for (int i = 0; i < jumlahGedung; i++) {
            String namaGedung = in.next();
            int jumlahLantai = in.nextInt();
            inisiasiGedung(i, namaGedung, jumlahLantai);
        }

        // ===== MENCARI GEDUNG [LANTAI] DENJI =====
        String gedungDenji = in.next();
        int lantaiDenji = in.nextInt(); counterLantaiDenji = lantaiDenji;
        Gedung gedungNow = null;
        for (Gedung g: kompleks) {
            if (g.getNama().equals(gedungDenji)) {
                gedungNow = g;
            }
        }
        // mencari address lantai Denji berdasarkan gedung
        Lantai lantai = gedungNow.getFirst();
        int counterLantai = 1; // counter lantai
        while (counterLantai != lantaiDenji) {
            lantai = lantai.getNext();
            counterLantai++;
        }   
        // pada awalnya Denji bergerak naik
        denji = new Karakter(gedungNow, lantai, true);

        // ===== MENCARI GEDUNG [LANTAI] IBLIS =====
        String gedungIblis = in.next();
        int lantaiIblis = in.nextInt(); counterLantaiIblis = lantaiIblis;
        // mencari gedung Iblis
        for (Gedung g: kompleks) {
            if (g.getNama().equals(gedungIblis)) {
                gedungNow = g;
            }
        }
        // mencari address lantai Iblis berdasarkan gedung
        lantai = gedungNow.getFirst(); // set ke first lagi
        counterLantai = 1; // set ke 1 lagi
        while (counterLantai != lantaiIblis) {
            lantai = lantai.getNext();
            counterLantai++;
        }   
        // pada awalnya Iblis bergerak turun (false)
        iblis = new Karakter(gedungNow, lantai, false);

        // ===== MEMBERI PERINTAH =====
        int Q = in.nextInt();

        for (int i = 0; i < Q; i++) {

            String command = in.next();

            if (command.equals("GERAK")) {
                gerak();
            } else if (command.equals("HANCUR")) {
                hancur();
            } else if (command.equals("TAMBAH")) {
                tambah();
            } else if (command.equals("PINDAH")) {
                pindah();
            }
        }

        out.close();
    }

    // method untuk menginisiasikan gedung
    public static void inisiasiGedung(int gedungKe, String namaGedung, int jumlahLantai) {
        Lantai dasar = new Lantai(null, null); 
        Lantai puncak = new Lantai(null, null);
        
        // jika jumlah lantai cuma satu
        if (jumlahLantai == 1) {
            // simpan lantai-lantai di atas masuk ke dalam gedung
            Gedung gedung = new Gedung(gedungKe, namaGedung, dasar, dasar, jumlahLantai);
            // simpan gedung ke dalam kompleks
            kompleks[gedungKe] = gedung;

        // jika jumlah lantai == 2
        } else if (jumlahLantai == 2) {
            dasar.setNext(puncak);
            puncak.setPrev(dasar);
            // simpan lantai-lantai di atas masuk ke dalam gedung
            Gedung gedung = new Gedung(gedungKe, namaGedung, dasar, puncak, jumlahLantai);
            // simpan gedung ke dalam kompleks
            kompleks[gedungKe] = gedung;

        // jika jumlah lantai lebih dari 2    
        } else { 
            // mengisi prev dan next pada setiap node lantai
            // variabel menyimpan lantai sebelum iterasi saat ini
            Lantai savePrev = dasar;
            for (int j = 2; j < jumlahLantai; j++) { // inisiasi lantai 2 sampai puncak-1
                // set prev adalah savePrev
                Lantai lantai = new Lantai(savePrev, null);  
                savePrev.setNext(lantai); // set next nya prev lantai saat ini
                savePrev = lantai; // menyimpan lantai sebelumnya
            }
            // set puncak & n-1
            savePrev.setNext(puncak); // mengeset lantai n-1 nextnya ke puncak
            puncak.setPrev(savePrev); // mengeset prev puncak adalah lantai n-1

            // simpan lantai-lantai di atas masuk ke dalam gedung
            Gedung gedung = new Gedung(gedungKe, namaGedung, dasar, puncak, jumlahLantai);
            // simpan gedung ke dalam kompleks
            kompleks[gedungKe] = gedung;
        }
    }

    static void gerak() {
        // OUTPUT:
        // - Nama gedung tempat Denji berada
        // - Ketinggian lantai tempat Denji berada
        // - Nama gedung tempat Iblis berada
        // - Ketinggian lantai tempat Iblis berada
        // - Jumlah pertemuan keduanya

        // catat pertemuan hanya setelah selesai bergerak
        // apabila lantai mereka sama maka hitung jumlah pertemuan
        gerakDenji();

        // catat pertemuan hanya setelah selesai bergerak
        // apabila lantai mereka sama maka hitung jumlah pertemuan
        if (denji.getGedungNow().equals(iblis.getGedungNow()) && counterLantaiDenji == counterLantaiIblis) {
            counterPertemuan++;
        }

        gerakIblis(); gerakIblis(); // iblis gerak dua kali

        // // catat pertemuan hanya setelah selesai bergerak
        // // apabila lantai mereka sama maka hitung jumlah pertemuan
        if (iblis.getGedungNow().equals(denji.getGedungNow()) && counterLantaiIblis == counterLantaiDenji) {
            counterPertemuan++;
        }

        // OUTPUT
        out.println(denji.getGedungNow().getNama()+" "+counterLantaiDenji+" "+iblis.getGedungNow().getNama()+" "+counterLantaiIblis+" "+counterPertemuan);
    }

    static void gerakDenji() {   
        // ATAS
        if (denji.getIsNaik()) {
            // jika lantai atasnya null
            if (denji.getLantaiNow().getNext() == null) {
                pindahGedung(denji, denji.getGedungNow().getId());
            } else {
                denji.setLantaiNow(denji.getLantaiNow().getNext());
                counterLantaiDenji++;
            }
        } else {
            // BAWAH
            // jika lantai bawahnya null
            if (denji.getLantaiNow().getPrev() == null) {
                pindahGedung(denji, denji.getGedungNow().getId());
            } else {
                denji.setLantaiNow(denji.getLantaiNow().getPrev());
                counterLantaiDenji--;
            }
        }
    }

    static void gerakIblis() {
        // ATAS
        if (iblis.getIsNaik()) {
            // jika lantai atasnya null
            if (iblis.getLantaiNow().getNext() == null) {
                pindahGedung(iblis, iblis.getGedungNow().getId());
            } else {
                iblis.setLantaiNow(iblis.getLantaiNow().getNext());
                counterLantaiIblis++;
            }
        } else {
            // BAWAH
            // jika lantai bawahnya null
            if (iblis.getLantaiNow().getPrev() == null) {
                pindahGedung(iblis, iblis.getGedungNow().getId());
            } else {
                iblis.setLantaiNow(iblis.getLantaiNow().getPrev());
                counterLantaiIblis--;
            }
        }
    }

    static void pindahGedung(Karakter ka, int idGedung) {
        Gedung baru = null;
        // jika gedung terakhir
        if (idGedung == kompleks.length-1) {
            // pindah ke gedung pertama
            baru = kompleks[0];
        } else {
            // pindah ke gedung selanjutnya
            baru = kompleks[idGedung+1];
        }

        // pindah gedung
        ka.setGedungNow(baru);
        // pindah lantai
        if (ka.getIsNaik()) {
            ka.setLantaiNow(baru.getLast());
            if (ka == denji) {
                counterLantaiDenji = baru.getJumlahLantai();
            } else {
                counterLantaiIblis = baru.getJumlahLantai();
            }
            ka.setIsNaik(false);
        } else {
            ka.setLantaiNow(baru.getFirst());
            if (ka == denji) {
                counterLantaiDenji = 1;
            } else {
                counterLantaiIblis = 1;
            }
            ka.setIsNaik(true);
        }
    }

    static void hancur() { 
        // ambil nama gedung Denji saat ini (gedung yang lantainya akan dihancurkan)
        Gedung gedungDihancurkan = denji.getGedungNow();

        if (gedungDihancurkan.getJumlahLantai() == 1) {
            out.println(gedungDihancurkan.getNama()+" -1");
        } else {

        // GAGAL HANCURKAN
        // cek apakah lantai paling bawah
        if (denji.getLantaiNow().getPrev() == null) { // saat tidak ada lantai di bawahnya (dasar)
            // OUTPUT
            out.println(gedungDihancurkan.getNama()+" -1");
        } else if (denji.getLantaiNow().getPrev().equals(iblis.getLantaiNow())) { // ada iblis di lantainya
            // OUTPUT
            out.println(gedungDihancurkan.getNama()+" -1");
        
        // SUCCESS HANCURKAN
        } else if (counterLantaiDenji == 2 || denji.getLantaiNow().getPrev().getPrev() == null) { // jika lantai yang dihancurkan denji adalah lantai 1 maka hapus prevnya denji aja
            denji.getLantaiNow().setPrev(null);

            // set counter lantai karakter
            // iblis 
            if (denji.getGedungNow().equals(iblis.getGedungNow()) && counterLantaiDenji <= counterLantaiIblis) { 
                // jika gedung iblis sama dengan gedung dihancurkan denji 
                // dan lantai denji dibawah atau sama dengan lantai iblis maka iblis ikut turun 
                counterLantaiIblis--; 
            }
            // denji
            counterLantaiDenji = 1; 

            // kurang jumlah lantai gedung
            gedungDihancurkan.setJumlahLantai(gedungDihancurkan.getJumlahLantai()-1);

            // OUTPUT
            out.println(gedungDihancurkan.getNama()+" "+counterLantaiDenji); 
        } else if (counterLantaiDenji >= 3 && denji.getGedungNow().getJumlahLantai() >= 3) { // saat bisa dihancurkan dan bukan lantai 1 yg dihancurkan
            // lantai di bawahnya dihancurkan
            // IMPORTANT (HATI-HATI!)

             // jika lantai di bawahnya adalah lantai dasar
            denji.getLantaiNow().getPrev().getPrev().setNext(denji.getLantaiNow());
            denji.getLantaiNow().setPrev(denji.getLantaiNow().getPrev().getPrev());
            
            // set counter lantai karakter  
            // iblis 
            if (denji.getGedungNow().equals(iblis.getGedungNow()) && counterLantaiDenji <= counterLantaiIblis) { 
                // jika gedung iblis sama dengan gedung dihancurkan denji 
                // dan lantai denji dibawah atau sama dengan lantai iblis maka iblis ikut turun
                counterLantaiIblis--; 
            }
            // denji
            counterLantaiDenji--; 

            // kurang jumlah lantai gedung
            gedungDihancurkan.setJumlahLantai(gedungDihancurkan.getJumlahLantai()-1);

            // OUTPUT
            out.println(gedungDihancurkan.getNama()+" "+counterLantaiDenji); // lantai denji saat ini == lantai gedung dihancurkan barusan
            // TODO: HANDLE GA? JIKA DENJI SELANTAI DENGAN IBLIS => GIMANA COUNTER PERTEMUANNYA?
        } else {
            // OUTPUT
            out.println(gedungDihancurkan.getNama()+" -1");
        }

        }
    } 
    

    static void tambah() {
        // ambil nama gedung iblis saat ini (gedung yang lantainya akan ditambahkan)
        Gedung gedungIblisNow = iblis.getGedungNow();
        Lantai posisiIblisNow = iblis.getLantaiNow();
        
        if (posisiIblisNow.getPrev() == null || counterLantaiIblis == 1) { // saat tidak ada lantai di bawahnya (lantai dasar)
            // inisiasi lantai dasar
            Lantai baru = new Lantai(null, posisiIblisNow);
            posisiIblisNow.setPrev(baru);

            if (denji.getGedungNow().equals(gedungIblisNow)) {
                counterLantaiDenji++;
            }
            counterLantaiIblis++;

        } else if (counterLantaiIblis == 2) { // saat berada di lantai dua
            Lantai baru = new Lantai(posisiIblisNow.getPrev(), posisiIblisNow);
            posisiIblisNow.getPrev().setNext(baru);
            posisiIblisNow.setPrev(baru);

            if (denji.getGedungNow().equals(gedungIblisNow) && counterLantaiIblis <= counterLantaiDenji) {
                counterLantaiDenji++;
            }
            counterLantaiIblis++;

        } else {
            // inisiasi lantai baru
            Lantai baru = new Lantai(posisiIblisNow.getPrev(), posisiIblisNow);
            posisiIblisNow.setPrev(baru);
            posisiIblisNow.getPrev().getPrev().setNext(baru);

            if (denji.getGedungNow().equals(gedungIblisNow) && counterLantaiIblis <= counterLantaiDenji) {
                counterLantaiDenji++;
            }
            counterLantaiIblis++;
        }

        gedungIblisNow.setJumlahLantai(gedungIblisNow.getJumlahLantai()+1);

        // OUTPUT
        out.println(gedungIblisNow.getNama()+" "+(counterLantaiIblis-1));
    }

    // TODO: Implemen perintah PINDAH
    // memindahkan denji ke gedung selanjutnya
    // jika denji bergerak ke atas pada gedung sebelumnya (isNaik == true)
        // maka denji akan bergerak dari lantai dasar dengan isNaik yg sama
    // jika sebaliknya
        // maka denji akan bergerak dari lantai paling atas dengan isNaik yg sama
    // jika ada pertemuan dengan iblis maka tambahkan counterPertemuan
    static void pindah() {
        // ambil gedung denji saat ini
        Gedung gedungDenjiNow = denji.getGedungNow();
        Gedung baru = null;
        
        // cek apakah gedung denji saat ini adalah gedung terakhir
        if (gedungDenjiNow.getId() == kompleks.length-1) { // jika gedung terakhir
            baru = kompleks[0]; // maka gedung selanjutnya adalah gedung pertama
        } else {
            baru = kompleks[gedungDenjiNow.getId()+1]; // jika bukan gedung terakhir maka gedung selanjutnya adalah gedung berikutnya
        }

        if (denji.getIsNaik()) {
            // jika denji bergerak ke atas pada gedung sebelumnya
            // maka denji akan bergerak dari lantai dasar dengan isNaik yg sama
            denji.setGedungNow(baru);
            denji.setLantaiNow(baru.getFirst());
            counterLantaiDenji = 1;
        } else {
            // jika sebaliknya
            // maka denji akan bergerak dari lantai paling atas dengan isNaik yg sama
            denji.setGedungNow(baru);
            denji.setLantaiNow(baru.getLast());
            counterLantaiDenji = baru.getJumlahLantai();
        }

        if (denji.getLantaiNow().equals(iblis.getLantaiNow())) {
            counterPertemuan++;
        }

        // OUTPUT
        out.println(baru.getNama()+" "+(counterLantaiDenji));
    }

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
    }
}

// lantai adalah sebuah node dalam doubly linkedlist gedung
class Lantai {
    private Lantai prev; // refer ke index lantai sebelumnya
    private Lantai next; // refer ke index lantai selanjutnya 

    Lantai (Lantai prev, Lantai next) {
        this.prev = prev;
        this.next = next;
    }

    // getter prev
    Lantai getPrev() {
        return this.prev;
    }

    // getter next
    Lantai getNext() {
        return this.next;
    }

    // setter prev
    void setPrev(Lantai prev) {
        this.prev = prev;
    }

    // setter next
    void setNext(Lantai next) {
        this.next = next;
    }
}

// gedung untuk mengidentifikasikan gedung
class Gedung {
    private int id; // menandai id gedung berdasarkan kompleks array
    private String nama; // nama gedung
    private Lantai first; // lantai dasar
    private Lantai last; // lantai teratas
    private int jumlahLantai;

    Gedung (int id, String nama, Lantai first, Lantai last, int jumlahLantai) {
        this.id = id;
        this.nama = nama;
        this.first = first;
        this.last = last;
        this.jumlahLantai = jumlahLantai;
    }

    // getter id
    int getId() {
        return this.id;
    }

    // getter nama
    String getNama() {
        return this.nama;
    }

    // getter first
    Lantai getFirst() {
        return this.first;
    }

    // getter last
    Lantai getLast() {
        return this.last;
    }

    int getJumlahLantai() {
        return this.jumlahLantai;
    }

    void setJumlahLantai(int jumlahLantai) {
        this.jumlahLantai = jumlahLantai;
    }
}

// karakter untuk mengidentifikasikan karakter
class Karakter {
    private Gedung gedungNow;
    private Lantai lantaiNow;
    // isNaik ada saat Karakter bergerak naik
    private boolean isNaik; 

    Karakter (Gedung gedungNow, Lantai lantaiNow, boolean isNaik) {
        this.gedungNow = gedungNow;
        this.lantaiNow = lantaiNow;
        this.isNaik= isNaik;
    }

    Gedung getGedungNow() {
        return this.gedungNow;
    } 

    Lantai getLantaiNow() {
        return this.lantaiNow;
    }

    boolean getIsNaik() {
        return this.isNaik;
    }

    void setGedungNow(Gedung gedungNow) {
        this.gedungNow = gedungNow;
    }

    void setLantaiNow(Lantai lantaiNow) {
        this.lantaiNow = lantaiNow;
    }

    void setIsNaik(boolean isNaik) {
        this.isNaik = isNaik;
    }
}