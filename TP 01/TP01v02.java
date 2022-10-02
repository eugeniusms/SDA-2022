import java.io.*;
import java.util.StringTokenizer;
import java.util.Arrays;

public class TP01v02 {
    private static InputReader in;
    private static PrintWriter out;

    public static Makanan[] menu; // index => id makanan
    public static Koki[] koki; // index => id koki

    public static void main(String[] args) {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        // ambil jumlah menu makanan
        int jumlahMenu = in.nextInt();
        menu = new Makanan[jumlahMenu];
        // membaca input menu makanan
        for (int i = 0; i < jumlahMenu; i++) {
            menu[i] = new Makanan(in.nextInt(), in.nextChar());
        }

        // ambil jumlah koki
        int jumlahKoki = in.nextInt();
        koki = new Koki[jumlahKoki];
        // membaca input koki
        for (int i = 0; i < jumlahKoki; i++) {
            koki[i] = new Koki(in.nextChar());
        }

        check();

        // Tutup OutputStream
        out.close();
    }

    public static void check() {
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

    // construct makanan
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