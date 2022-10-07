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

    public static void main(String[] args) {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        int jumlahGedung = in.nextInt();
        for (int i = 0; i < jumlahGedung; i++) {
            String namaGedung = in.next();
            int jumlahLantai = in.nextInt();

            // TODO: Inisiasi gedung pada kondisi awal
            inisiasiGedung(namaGedung, jumlahLantai);
                


        }

        String gedungDenji = in.next();
        int lantaiDenji = in.nextInt();
        // TODO: Tetapkan kondisi awal Denji

        String gedungIblis = in.next();
        int lantaiIblis = in.nextInt();
        // TODO: Tetapkan kondisi awal Iblis

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
    public static void inisiasiGedung(String namaGedung, int jumlahLantai) {
        Lantai dasar = new Lantai(false, false, null, null);
        Lantai puncak = new Lantai(false, false, null, null);
        // mengisi prev pada setiap node lantai
        // variabel menyimpan lantai sebelum iterasi saat ini
        Lantai savePrev = dasar;
        for (int i = 2; i < jumlahLantai; i++) { // inisiasi lantai 2 sampai puncak-1
            Lantai lantai = new Lantai(false, false, savePrev, null);  
            savePrev = lantai; // menyimpan lantai sebelumnya
        } 
        // menyusun next pada setiap node lantai
        Lantai saveNext = puncak;
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
    boolean isDenjiExist; 
    boolean isIblisExist;
    Lantai prev; // refer ke index lantai sebelumnya
    Lantai next; // refer ke index lantai selanjutnya 

    Lantai (boolean isDenjiExist, boolean isIblisExist, Lantai prev, Lantai next) {
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

class Gedung {
    String nama; // nama gedung
    Lantai first; // lantai dasar
    Lantai last; // lantai teratas

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