import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.*;

public class Lab04v02 {

    private static InputReader in;
    static PrintWriter out;

    static Gedung[] gedung;

    public static Karakter denji;
    public static Karakter iblis;

    public static long pertemuan = 0;

    public static void main(String[] args) {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        int jumlahGedung = in.nextInt(); gedung = new Gedung[jumlahGedung];
        for (int i = 0; i < jumlahGedung; i++) {
            String namaGedung = in.next();
            int jumlahLantai = in.nextInt();

            // Inisiasi gedung pada kondisi awal
            LinkedList<Lantai> lantaiGedung = new LinkedList<>();
            for (int j = 0; j < jumlahLantai; j++) {
                lantaiGedung.add(new Lantai()); // lantai berisi hampa
            }
            gedung[i] = new Gedung(i, namaGedung, lantaiGedung);
        }

        String gedungDenji = in.next();
        int lantaiDenji = in.nextInt();
        // TODO: Tetapkan kondisi awal Denji
        // DAPATI GEDUNG
        Gedung gedungInput = null;
        for (int i = 0; i < gedung.length; i++) {
            if (gedung[i].nama.equals(gedungDenji)) {
                gedungInput = gedung[i];
            }
        }
        // DAPATI LANTAI
        Lantai lantaiInput = null;
        for (int i = 0; i < gedungInput.lantai.size(); i++) {
            if (i == lantaiDenji) {
                lantaiInput = gedungInput.lantai.get(i);
            }
        }
        denji = new Karakter(gedungInput, lantaiInput, lantaiDenji, true);

        String gedungIblis = in.next();
        int lantaiIblis = in.nextInt();
        // TODO: Tetapkan kondisi awal Iblis
        // DAPATI GEDUNG
        for (int i = 0; i < gedung.length; i++) {
            if (gedung[i].nama.equals(gedungIblis)) {
                gedungInput = gedung[i];
            }
        }
        // DAPATI LANTAI
        for (int i = 0; i < gedungInput.lantai.size(); i++) {
            if (i == lantaiIblis) {
                lantaiInput = gedungInput.lantai.get(i);
            }
        }
        iblis = new Karakter(gedungInput, lantaiInput, lantaiIblis, false);

        // PERINTAH
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

    static void gerak() {
        gerakKarakter(denji);
        if (denji.gedungNow.id == iblis.gedungNow.id) {
            pertemuan++;
        }

        gerakKarakter(iblis); gerakKarakter(iblis);
        if (denji.gedungNow.id == iblis.gedungNow.id) {
            pertemuan++;
        }

        // OUTPUT
        out.println(denji.gedungNow.nama + " " + denji.tingkat + " " + iblis.gedungNow.nama + " " + iblis.tingkat + " " + pertemuan);
    }

    static void gerakKarakter(Karakter ka) {
        // jika naik
        if (ka.isNaik) {
            if (ka.lantaiNow != ka.gedungNow.lantai.getLast()) { // bukan lantai terakhir
                ka.lantaiNow = ka.gedungNow.lantai.get(ka.tingkat + 1);
                ka.tingkat++;    
            } else {
                if (ka.gedungNow != gedung[gedung.length - 1]) { // bukan gedung terakhir
                    ka.gedungNow = gedung[ka.gedungNow.id + 1];
                } else {
                    ka.gedungNow = gedung[0];
                }

                // set lantai atas
                ka.lantaiNow = ka.gedungNow.lantai.getLast();
                ka.isNaik = false;
                ka.tingkat = ka.gedungNow.lantai.size();
            }
        } else {
            if (ka.lantaiNow != ka.gedungNow.lantai.getFirst()) { // bukan lantai pertama
                ka.lantaiNow = ka.gedungNow.lantai.get(ka.tingkat - 1);
                ka.tingkat--;
            } else {
                if (ka.gedungNow != gedung[gedung.length - 1]) { // bukan gedung terakhir
                    ka.gedungNow = gedung[ka.gedungNow.id + 1];
                } else {
                    ka.gedungNow = gedung[0];
                }

                // set lantai bawah
                ka.lantaiNow = ka.gedungNow.lantai.getFirst();
                ka.isNaik = true;
                ka.tingkat = 1;
            }
        }
    }

    // TODO: Implemen perintah HANCUR
    static void hancur() {
        Gedung gedungDenjiNow = denji.gedungNow;

        if (gedungDenjiNow.lantai.size() == 1) {
            // OUTPUT
            out.println(gedungDenjiNow.nama+" -1");
        } else if (gedungDenjiNow.lantai.get(denji.tingkat - 1) == iblis.lantaiNow) {
            // OUTPUT
            out.println(gedungDenjiNow.nama+" -1");
        } else {
            out.println(gedungDenjiNow.nama+" "+(gedungDenjiNow.lantai.get(denji.tingkat-1)));
            gedungDenjiNow.lantai.remove(denji.tingkat - 1); 
            denji.tingkat--;

            if (denji.gedungNow.equals(iblis.gedungNow)) {
                if (denji.tingkat <= iblis.tingkat) {
                    iblis.tingkat--;
                }
            }
            denji.tingkat--;

            // OUTPUT
            out.println(gedungDenjiNow.nama+" "+denji.tingkat);
        }
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

// lantai hampa
class Lantai { 
}

class Gedung {
    int id;
    String nama;
    LinkedList<Lantai> lantai;

    public Gedung(int id, String nama, LinkedList<Lantai> lantai) {
        this.id = id;
        this.nama = nama;
        this.lantai = lantai;
    }
}

class Karakter {
    Gedung gedungNow;
    Lantai lantaiNow;
    int tingkat;
    boolean isNaik;

    public Karakter(Gedung gedungNow, Lantai lantaiNow, int tingkat, boolean isNaik) {
        this.gedungNow = gedungNow;
        this.lantaiNow = lantaiNow;
        this.tingkat = tingkat;
        this.isNaik = isNaik;
    }
}