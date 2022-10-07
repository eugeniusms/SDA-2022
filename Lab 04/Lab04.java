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
    public static Karakter[] pemain = new Karakter[2]; // [0]: Denji, [1]: Iblis

    public static void main(String[] args) {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        // inisiasi kompleks gedung
        int jumlahGedung = in.nextInt(); kompleks = new Gedung[jumlahGedung];

        for (int i = 0; i < jumlahGedung; i++) {
            String namaGedung = in.next();
            int jumlahLantai = in.nextInt();
            // TODO: Inisiasi gedung pada kondisi awal
            inisiasiGedung(i, namaGedung, jumlahLantai);
        }

        String gedungDenji = in.next();
        int lantaiDenji = in.nextInt();
        // TODO: Tetapkan kondisi awal Denji
        pemain[0] = new Karakter(gedungDenji, lantaiDenji);

        String gedungIblis = in.next();
        int lantaiIblis = in.nextInt();
        // TODO: Tetapkan kondisi awal Iblis
        pemain[1] = new Karakter(gedungIblis, lantaiIblis);

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

        checkGedung(kompleks[0]);

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
        Gedung gedung = new Gedung(namaGedung, dasar, puncak);
        // simpan gedung ke dalam kompleks
        kompleks[gedungKe] = gedung;
    }

    public static void checkGedung(Gedung gedung) {
        Lantai lantai = gedung.getFirst();
        Lantai puncak = gedung.getLast();
        // saat next belum null (yg last maka diiterasi terus)
        while (lantai.getNext() != null) {
            out.println("["+lantai.getNomor()+ "] | ADDR: " + lantai + " | PREV: "+lantai.getPrev()+" | NEXT: "+lantai.getNext());
            lantai = lantai.getNext();
        }
        // print the last
        out.println("["+puncak.getNomor()+ "] | ADDR: "+puncak+" | PREV: "+puncak.getPrev()+" | NEXT: "+puncak.getNext());
    }

    // TODO: Implemen perintah GERAK
    static void gerak() {

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
    private int nomor;
    private Lantai prev; // refer ke index lantai sebelumnya
    private Lantai next; // refer ke index lantai selanjutnya 

    Lantai (boolean isDenjiExist, boolean isIblisExist, int nomor, Lantai prev, Lantai next) {
        this.isDenjiExist = isDenjiExist;
        this.isIblisExist = isIblisExist;
        this.nomor = nomor;
        this.prev = prev;
        this.next = next;
    }
    
    // method mengembalikan denji iblis selantai atau tidak
    boolean isDenjiIblisSelantai() {
        return (this.isDenjiExist == true) && (this.isIblisExist == true);
    }

    // getter nomor lantai
    int getNomor() {
        return this.nomor;
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
    private String nama; // nama gedung
    private Lantai first; // lantai dasar
    private Lantai last; // lantai teratas

    Gedung (String nama, Lantai first, Lantai last) {
        this.nama = nama;
        this.first = first;
        this.last = last;
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
}

// karakter untuk mengidentifikasikan karakter
class Karakter {
    private String gedungNow;
    private int lantaiNow;

    Karakter (String gedungNow, int lantaiNow) {
        this.gedungNow = gedungNow;
        this.lantaiNow = lantaiNow;
    }

    String getGedungNow() {
        return this.gedungNow;
    } 

    int getLantaiNow() {
        return this.lantaiNow;
    }
}