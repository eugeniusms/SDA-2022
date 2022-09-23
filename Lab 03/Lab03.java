import java.io.*;
import java.util.StringTokenizer;

public class Lab03 {
    private static InputReader in;
    private static PrintWriter out;

    public static char[] A;
    public static int N;

    public static void main(String[] args) {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        // Inisialisasi Array Input
        N = in.nextInt();
        A = new char[N];

        // Membaca File Input
        for (int i = 0; i < N; i++) {
            A[i] = in.nextChar();
        }

        // Run Solusi
        int solution = getMaxRedVotes(0, N, 0);
        out.print(solution);

        // Tutup OutputStream
        out.close();
    }

    public static int getMaxRedVotes(int start, int end, int redSave) {
        // Jika start == end maka sudahi rekursi
        if (start == end) {
            return 0;
        }

        int red = 0;
        int blue = 0;
        int redVotes = 0;
        int redSaveNew = 0; // redSaveNew digunakan untuk menyimpan nilai total vote red terbaru
        for (int i = start; i < end; i++) {
            if (A[i] == 'R') {
                red++;
            } else {
                blue++;
            }

            // Mendapatkan redvotes di setiap perulangan dalam satu kolom
            redVotes = getVotes(red, blue);
            out.println("redVotes: " + redVotes);

            // Menyimpan nilai total redVote ditambah redSave sebelumnya
            // Example: (R R B)(B) => redSave = 3 (redVote sebelum saat ini) + 0 (redVote saat ini)
            redSaveNew = redSave + redVotes; 

            // Pecah lagi pada setiap perulangan menuju baris sequence masing-masing (sisa sequence)
            // Berdasarkan data setelah start terakhir = i + 1 sampai end
            getMaxRedVotes(i+1, end, redSaveNew);
        }

        out.println("======================");
        // redSaveNew adalah jumlah total vote red dalam satu subsequence 
        out.println("redsavenew: " + redSaveNew);
        out.println();

        return redSaveNew;
    }

    // Fungsi digunakan untuk mengembalikan jumlah vote berdasarkan (red, blue) 
    public static int getVotes(int red, int blue) {
        if (red > blue) {
            return red + blue;
        } else {
            return 0;
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
            return next().equals("R") ? 'R' : 'B';
        }
    }
}