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

        checkPemain();

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

        checkPemain();

        out.close();
    }

    // method untuk menginisiasikan gedung
    public static void inisiasiGedung(int gedungKe, String namaGedung, int jumlahLantai) {
        Lantai dasar = new Lantai(false, false, 1, null, null); 
        Lantai puncak = new Lantai(false, false, jumlahLantai, null, null);
        
        // mengisi prev dan next pada setiap node lantai
        // variabel menyimpan lantai sebelum iterasi saat ini
        Lantai savePrev = dasar;
        for (int j = 2; j < jumlahLantai; j++) { // inisiasi lantai 2 sampai puncak-1
            // set prev adalah savePrev
            Lantai lantai = new Lantai(false, false, j, savePrev, null);  
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

    // TODO: Implemen perintah GERAK
    static void gerak() {
        // OUTPUT:
        // - Nama gedung tempat Denji berada
        // - Ketinggian lantai tempat Denji berada
        // - Nama gedung tempat Iblis berada
        // - Ketinggian lantai tempat Iblis berada
        // - Jumlah pertemuan keduanya
        gerakDenji();
        gerakIblis(); gerakIblis(); // iblis gerak dua kali
        checkGerak();

        // check pertemuan hanya setelah selesai bergerak
        // apabila lantai mereka sama maka hitung jumlah pertemuan
        if (denji.getLantaiNow().equals(iblis.getLantaiNow())) {
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
                out.println("MASUK 2");

            } else { // pindah gedung
                // menentukkan gedung yang dipakai untuk pindah iblis

                // jika id == kompleks.length-1 maka balik ke gedung 0 lagi
                // jika tidak maka lanjut ke gedung selanjutnya
                int idGedung = iblis.getGedungNow().getId();
                int moveIdGedung = (idGedung == kompleks.length-1) ? 0 : idGedung+1;
                out.println("MASUK 3: "+moveIdGedung);

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

    static void checkGerak() {
        out.println("===== CEK GERAK =====");
        out.println("DENJI: "+denji.getGedungNow().getNama()+" | "+counterLantaiDenji);
        out.println("IBLIS: "+iblis.getGedungNow().getNama()+" | "+counterLantaiIblis);
    }

    // TODO: Implemen perintah HANCUR
    static void hancur() {

    }

    // TODO: Implemen perintah TAMBAH
    static void tambah() {

    }

    // TODO: Implemen perintah PINDAH
    static void pindah() {

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
    private boolean isDenjiExist; 
    private boolean isIblisExist;
    private Lantai prev; // refer ke index lantai sebelumnya
    private Lantai next; // refer ke index lantai selanjutnya 

    Lantai (boolean isDenjiExist, boolean isIblisExist, int nomor, Lantai prev, Lantai next) {
        this.isDenjiExist = isDenjiExist;
        this.isIblisExist = isIblisExist;
        this.prev = prev;
        this.next = next;
    }
    
    // method mengembalikan denji iblis selantai atau tidak
    boolean isDenjiIblisSelantai() {
        return (this.isDenjiExist == true) && (this.isIblisExist == true);
    }

    // getter prev
    Lantai getPrev() {
        return this.prev;
    }

    // getter next
    Lantai getNext() {
        return this.next;
    }

    // setter denji exist
    void setDenjiExist(boolean isDenjiExist) {
        this.isDenjiExist = isDenjiExist;
    }

    // setter iblis exist
    void setIblisExist(boolean isIblisExist) {
        this.isIblisExist = isIblisExist;
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