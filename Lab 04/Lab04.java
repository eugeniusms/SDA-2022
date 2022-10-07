import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.*;

public class Lab04 {

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
            checkGedung(kompleks[i]);
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

        // checkPemain();

        // ===== MEMBERI PERINTAH =====
        int Q = in.nextInt();

        for (int i = 0; i < Q; i++) {

            String command = in.next();

            if (command.equals("GERAK")) {
                
                checkPosisi("GERAK");
                gerak();
            } else if (command.equals("HANCUR")) {
                // checkGedung(kompleks[0]);
                // checkGedung(kompleks[1]);
                checkPosisi("HANCUR");
                hancur();
            } else if (command.equals("TAMBAH")) {
                checkPosisi("TAMBAH");
                tambah();
            } else if (command.equals("PINDAH")) {
                checkPosisi("PINDAH");
                pindah();
            }
        }

        checkPosisi("TERAKHIR");
        // checkPemain();

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

    public static void checkGedung(Gedung gedung) {
        out.println("===== GEDUNG "+gedung.getNama()+" =====");
        Lantai lantai = gedung.getFirst();
        Lantai puncak = gedung.getLast();
        // saat next belum null (yg last maka diiterasi terus)
        int countLantai = 1;
        while (lantai.getNext() != null) {
            out.println("["+countLantai+ "] | ADDR: " + lantai + " | PREV: "+lantai.getPrev()+" | NEXT: "+lantai.getNext());
            lantai = lantai.getNext();
            countLantai++;
        }
        // print the last
        out.println("["+countLantai+ "] | ADDR: "+puncak+" | PREV: "+puncak.getPrev()+" | NEXT: "+puncak.getNext());
    }

    public static void checkPemain() {
        out.println("===== CEK LANTAI PEMAIN =====");
        out.println("LANTAI DENJI: "+denji.getLantaiNow());
        out.println("LANTAI IBLIS: "+iblis.getLantaiNow());
    }

    static void gerak() {
        out.println("POSISI LANTAI DENJI: "+denji.getLantaiNow()+" | IBLIS: "+iblis.getLantaiNow());
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
        // if (denji.getLantaiNow().equals(iblis.getLantaiNow())) {
        //     out.println("DENJI MENCATAT");
        //     counterPertemuan++; // DENJI MENCATAT
        // }

        if (denji.getGedungNow().equals(iblis.getGedungNow()) && counterLantaiDenji == counterLantaiIblis) {
            counterPertemuan++;
        }

        gerakIblis(); gerakIblis(); // iblis gerak dua kali

        // // catat pertemuan hanya setelah selesai bergerak
        // // apabila lantai mereka sama maka hitung jumlah pertemuan
        // if (iblis.getLantaiNow().equals(denji.getLantaiNow())) {
        //     out.println("IBLIS MENCATAT");
        //     counterPertemuan++;
        //     // IBLIS MENCATAT
        // }

        if (iblis.getGedungNow().equals(denji.getGedungNow()) && counterLantaiIblis == counterLantaiDenji) {
            counterPertemuan++;
        }

        // OUTPUT
        out.println(denji.getGedungNow().getNama()+" "+counterLantaiDenji+" "+iblis.getGedungNow().getNama()+" "+counterLantaiIblis+" "+counterPertemuan);
    }

    // GERAK PADA DENJI
    static void gerakDenji() {
        // menentukan lantai yang dipakai untuk pindah denji
        // jika sebelumnya lantai puncak maka pindah ke lantai puncak juga (1)
        // jika sebelumnya lantai dasar maka pindah ke lantai dasar juga (2)

        // (1) SAAT LANGKAH MENAIK
        if (denji.getIsNaik()) { // jika naik == null (di puncak)
            // cek apakah lantai selanjutnya null (puncak) atau tidak 
            if (denji.getLantaiNow().getNext() != null) {
                // ===== SET STATUS =====
                denji.setLantaiNow(denji.getLantaiNow().getNext()); // naik
                counterLantaiDenji++; // denji naik satu lantai

            } else { // pindah gedung
                // menentukkan gedung yang dipakai untuk pindah denji

                // jika id == kompleks.length-1 maka balik ke gedung 0 lagi
                // jika tidak maka lanjut ke gedung selanjutnya
                int idGedung = denji.getGedungNow().getId();
                int moveIdGedung = (idGedung == kompleks.length-1) ? 0 : idGedung+1;

                // ===== SET STATUS =====
                // berhubung saat denji naik next == null artinya dia berada di puncak
                // oleh karena itu pindah denji ke puncak gedung selanjutnya
                denji.setLantaiNow(kompleks[moveIdGedung].getLast()); // pindah ke puncak
                denji.setGedungNow(kompleks[moveIdGedung]); // pindah gedung
                denji.setIsNaik(false);; // balik arah (jadi turun)
                counterLantaiDenji = kompleks[moveIdGedung].getJumlahLantai(); // pindah counter jadi ke lantai puncak = sesuai jumlah lantai
            }
        }
        // (2) SAAT LANGKAH MENURUN
        else {
            // cek apakah lantai sebelumnya null (dasar) atau tidak
            if (denji.getLantaiNow().getPrev() != null) {
                // ===== SET STATUS =====
                denji.setLantaiNow(denji.getLantaiNow().getPrev()); // turun
                counterLantaiDenji--; // denji turun satu lantai

            } else { // pindah gedung
                // menentukkan gedung yang dipakai untuk pindah denji

                // jika id == kompleks.length-1 maka balik ke gedung 0 lagi
                // jika tidak maka lanjut ke gedung selanjutnya
                int idGedung = denji.getGedungNow().getId();
                int moveIdGedung = (idGedung == kompleks.length-1) ? 0 : idGedung+1;

                // ===== SET STATUS =====
                // berhubung saat denji turun prev == null artinya dia berada di dasar
                // oleh karena itu pindah denji ke dasar gedung selanjutnya
                denji.setLantaiNow(kompleks[moveIdGedung].getFirst()); // pindah ke lantai dasar    
                denji.setGedungNow(kompleks[moveIdGedung]); // pindah gedung
                denji.setIsNaik(true);; // balik arah (jadi naik)
                counterLantaiDenji = 1; // pindah counter jadi ke lantai 1 lagi
            }
        }
    }

    // GERAK PADA iblis
    static void gerakIblis() {
        // menentukan lantai yang dipakai untuk pindah iblis
        // jika sebelumnya lantai puncak maka pindah ke lantai puncak juga (1)
        // jika sebelumnya lantai dasar maka pindah ke lantai dasar juga (2)
  
        // (1) SAAT LANGKAH MENAIK
        if (iblis.getIsNaik()) { // jika naik == null (di puncak)
            // cek apakah lantai selanjutnya null (puncak) atau tidak 
            if (iblis.getLantaiNow().getNext() != null) {
                // ===== SET STATUS =====
                iblis.setLantaiNow(iblis.getLantaiNow().getNext()); // naik
                counterLantaiIblis++; // iblis naik dua lantai

            } else { // pindah gedung
                // menentukkan gedung yang dipakai untuk pindah iblis

                // jika id == kompleks.length-1 maka balik ke gedung 0 lagi
                // jika tidak maka lanjut ke gedung selanjutnya
                int idGedung = iblis.getGedungNow().getId();
                int moveIdGedung = (idGedung == kompleks.length-1) ? 0 : idGedung+1;

                // ===== SET STATUS =====
                // berhubung saat iblis naik next == null artinya dia berada di puncak
                // oleh karena itu pindah iblis ke puncak gedung selanjutnya
                iblis.setLantaiNow(kompleks[moveIdGedung].getLast()); // pindah ke puncak
                iblis.setGedungNow(kompleks[moveIdGedung]); // pindah gedung
                iblis.setIsNaik(false);; // balik arah (jadi turun)
                counterLantaiIblis = kompleks[moveIdGedung].getJumlahLantai(); // pindah counter jadi ke lantai puncak = sesuai jumlah lantai
            }
        }
        // (2) SAAT LANGKAH MENURUN
        else {
            // cek apakah lantai sebelumnya null (dasar) atau tidak
            if (iblis.getLantaiNow().getPrev() != null) {
                // ===== SET STATUS =====
                iblis.setLantaiNow(iblis.getLantaiNow().getPrev()); // turun
                counterLantaiIblis--; // iblis turun satu lantai

            } else { // pindah gedung
                // menentukkan gedung yang dipakai untuk pindah iblis

                // jika id == kompleks.length-1 maka balik ke gedung 0 lagi
                // jika tidak maka lanjut ke gedung selanjutnya
                int idGedung = iblis.getGedungNow().getId();
                int moveIdGedung = (idGedung == kompleks.length-1) ? 0 : idGedung+1;

                // ===== SET STATUS =====
                // berhubung saat iblis turun prev == null artinya dia berada di dasar
                // oleh karena itu pindah iblis ke dasar gedung selanjutnya
                iblis.setLantaiNow(kompleks[moveIdGedung].getFirst()); // pindah ke lantai dasar    
                iblis.setGedungNow(kompleks[moveIdGedung]); // pindah gedung
                iblis.setIsNaik(true);; // balik arah (jadi naik)
                counterLantaiIblis = 1; // pindah counter jadi ke lantai 1 lagi
            }
        }
    }

    static void checkPosisi(String comm) {
        out.println("===== COMMAND: "+comm+" =====");
        out.println("DENJI: "+denji.getGedungNow().getNama()+" | "+counterLantaiDenji);
        out.println("IBLIS: "+iblis.getGedungNow().getNama()+" | "+counterLantaiIblis);
    }

    // OPERASI DALAM SATU GEDUNG (TIDAK PERLU ADA SET GEDUNG)
    // menghancurkan lantai tepat 1 di bawah denji
    // jika lantai bawah denji: dasar/ada iblis maka return ke 2), jika aman return ke 1)
    // 1) return nama gedung dan ketinggian lantai dihancurkan
    // 2) return nama gedung dan -1
    static void hancur() { 
        // ambil nama gedung Denji saat ini (gedung yang lantainya akan dihancurkan)
        Gedung gedungDihancurkan = denji.getGedungNow();

        // GAGAL HANCURKAN
        // cek apakah lantai paling bawah
        if (denji.getLantaiNow().getPrev() == null) { // saat tidak ada lantai di bawahnya (dasar)
            // OUTPUT
            out.println(gedungDihancurkan.getNama()+" -1");
        } else if (denji.getLantaiNow().getPrev().equals(iblis.getLantaiNow())) { // ada iblis di lantainya
            // OUTPUT
            out.println(gedungDihancurkan.getNama()+" -1");
        
        // SUCCESS HANCURKAN
        } else if (counterLantaiDenji == 2) { // jika lantai yang dihancurkan denji adalah lantai 1 maka hapus prevnya denji aja
            Lantai denjiSekarang = denji.getLantaiNow();
            denjiSekarang.setPrev(null);

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

        } else { // saat bisa dihancurkan dan bukan lantai 1 yg dihancurkan
            // lantai di bawahnya dihancurkan
            // IMPORTANT (HATI-HATI!)
            Lantai dibawahDihancurkan = denji.getLantaiNow().getPrev().getPrev();
            Lantai denjiSekarang = denji.getLantaiNow();
            dibawahDihancurkan.setNext(denji.getLantaiNow()); // set next lantai di bawahnya lantai dihancurkan ke lantai denji
            denjiSekarang.setPrev(dibawahDihancurkan); // set prev lantai di saat ini ke lantai di bawahnya lantai dihancurkan
            
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
        }
    }
    
    // OPERASI DALAM SATU GEDUNG (TIDAK PERLU ADA SET GEDUNG)
    // menambahkan lantai di bawah lantai iblis saat ini
    // return nama gedung dan ketinggian lantai yang ditambahkan iblis
    static void tambah() {
        // ambil nama gedung Iblis saat ini (gedung yang lantainya akan ditambahkan)
        Gedung gedungDitambahkan = iblis.getGedungNow();
        // tambah jumlah lantai gedung
        gedungDitambahkan.setJumlahLantai(gedungDitambahkan.getJumlahLantai()+1);

        // cek apakah lantai paling bawah
        if (iblis.getLantaiNow().getPrev() == null) { // saat tidak ada lantai di bawahnya (dasar)
            // maka set next lantai baru ke lantai iblis
            // dan set prev lantai iblis ke lantai baru
            Lantai lantaiBaru = new Lantai(null, iblis.getLantaiNow());
            iblis.getLantaiNow().setPrev(lantaiBaru);

            // jika denji segedung dengan iblis dan denji berada di atas atau selantai dengan iblis maka dia ikut naik counternya
            if (denji.getGedungNow().equals(iblis.getGedungNow()) && counterLantaiDenji >= counterLantaiIblis) {
                counterLantaiDenji++;
            }

            counterLantaiIblis++; // tambah counter lantai iblis

        } else { // saat ada lantai di bawahnya
            // ambil lantai iblis saat ini
            Lantai lantaiIblisSekarang = iblis.getLantaiNow();
            // ambil lantai di bawahnya
            Lantai lantaiDiBawahIblis = lantaiIblisSekarang.getPrev();
            // susun lantai baru di antara lantai iblis dan bawahnya
            Lantai lantaiBaru = new Lantai(lantaiDiBawahIblis, lantaiIblisSekarang);
            // ubah .next lantai di bawahnya
            lantaiDiBawahIblis.setNext(lantaiBaru);
            // ubah .prev lantai iblis
            lantaiIblisSekarang.setPrev(lantaiBaru);

            // jika denji segedung dengan iblis dan denji berada di atas atau selantai dengan iblis maka dia ikut naik counternya
            if (denji.getGedungNow().equals(iblis.getGedungNow()) && counterLantaiDenji >= counterLantaiIblis) {
                counterLantaiDenji++;
            }

            counterLantaiIblis++; // tambah counter lantai iblis
        }

        // OUTPUT
        out.println(gedungDitambahkan.getNama()+" "+(counterLantaiIblis-1)); // lantai iblis sebelumnya == lantai gedung ditambahkan barusan
    }

    // TODO: Implemen perintah PINDAH
    // memindahkan denji ke gedung selanjutnya
    // jika denji bergerak ke atas pada gedung sebelumnya (isNaik == true)
        // maka denji akan bergerak dari lantai dasar dengan isNaik yg sama
    // jika sebaliknya
        // maka denji akan bergerak dari lantai paling atas dengan isNaik yg sama
    // jika ada pertemuan dengan iblis maka tambahkan counterPertemuan
    static void pindah() {
        // ambil gedung denji sekarang, jika gedung terakhir maka balik ke depan
        int idGedung = denji.getGedungNow().getId();
        int moveIdGedung = (idGedung == kompleks.length-1) ? 0 : idGedung+1;
        // pindahkan gedung denji
        denji.setGedungNow(kompleks[moveIdGedung]);

        if (denji.getIsNaik()) {
            // lakukan pemindahan lantai denji ke dasar gedung selanjutnya
            denji.setLantaiNow(kompleks[moveIdGedung].getFirst());
            counterLantaiDenji = 1;
        } else {
            // lakukan pemindahan lantai denji ke paling atas gedung selanjutnya
            denji.setLantaiNow(kompleks[moveIdGedung].getLast());
            counterLantaiDenji = kompleks[moveIdGedung].getJumlahLantai();
        }

        // jika bertemu dengan iblis maka counterPertemuan
        if (denji.getLantaiNow().equals(iblis.getLantaiNow())) {
            out.println("DENJI MENCATAT");
            counterPertemuan++;
        }

        // OUTPUT
        out.println(kompleks[moveIdGedung].getNama()+" "+counterLantaiDenji); // nama gedung dan lantai denji saat ini
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