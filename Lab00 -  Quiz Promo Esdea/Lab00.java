import java.io.*;
import java.util.StringTokenizer;

public class Lab00 {
    private static InputReader in;
    private static PrintWriter out;

    // Fungsi utama berjalannya program
    public static void main(String[] args) throws IOException {
        // Melakukan inisiasi input dan output
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        // Membaca nilai dari N (total input yang ingin dimasukkan)
        int N = in.nextInt();

        // Memulai untuk mengambil input dan mengalikan setiap input dengan algoritma modulo
        // Inisiasi hasil kali = 1 (gunakan long untuk menerima hasil kali besar contohnya 10^6 * 10^6 (max))
        long hasilKali = 1;
        // Membaca semua input x sesuai dengan iterasi sejumlah N
        int x;
        // Kompleksitas algoritma O(N)
        for (int i = 0; i < N; ++i) {
            x = in.nextInt();
            // Lakukan perkalian setiap input yang ada (x)
            hasilKali *= x;
            // Untuk setiap perulangan hasil kali dapat dibagi dengan Mod karena 
            // (n * m) mod x == (n mod x)(m mod x)
            hasilKali %= (1e9+7);
        }
        // Cetak hasilKali
        out.println(hasilKali);

        // Tutup out
        out.close();
    }

    // taken from https://codeforces.com/submissions/Petr
    // together with PrintWriter, these input-output (IO) is much faster than the usual Scanner(System.in) and System.out
    // please use these classes to avoid your fast algorithm gets Time Limit Exceeded caused by slow input-output (IO)
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