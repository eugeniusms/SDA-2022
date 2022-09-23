import java.io.*;
import java.util.StringTokenizer;
import java.util.Arrays;

public class Lab03 {
    private static InputReader in;
    private static PrintWriter out;

    public static char[] A;
    public static int N;
    // Memo yang digunakan untuk menyimpan redSaveNew dari kombinasi yang pernah ada
    public static int[] memo = new int[1069];

    public static void main(String[] args) {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        // Inisiasi nilai memo
        // Fill each index with -1
        Arrays.fill(memo, -1);

        // Inisialisasi Array Input
        N = in.nextInt();
        A = new char[N];

        // Membaca File Input
        for (int i = 0; i < N; i++) {
            A[i] = in.nextChar();
        }

        // Run Solusi
        int solution = getMaxRedVotes(0, N);
        out.print(solution);

        // Tutup OutputStream
        out.close();
    }

    // Fungsi untuk mendapatkan nilai maksimal red votes
    public static int getMaxRedVotes(int start, int end) {
        // Melakukan cek ke dalam memo, jika sudah pernah ada hasil maka langsung return memo
        if (memo[start] != -1) {
            return memo[start];
        }

        // Saat start == end maka return 0
        if (start >= end) {
            return 0;
        } else {
            // Inisiasi variabel
            int red = 0;
            int blue = 0;
            int redVotes = 0;
            int maxRedVotes = 0;

            // Pada setiap cabang sequence
            for (int i = start; i < end; i++) {
                // Lakukan pengambilan data 'R' atau 'B'
                if (A[i] == 'R') {
                    red++;
                } else {
                    blue++;
                }

                // Mendapat nilai vote ke dalamnya 
                // Kemudian cari redVote paling maksimal
                redVotes = getVotes(red, blue) + getMaxRedVotes(i+1, end);
                if (redVotes > maxRedVotes) {
                    maxRedVotes = redVotes;
                }
            }

            // Menyimpan nilai maksimal red votes ke memo
            memo[start] = maxRedVotes;

            // Kembalikan nilai maksimal red votes yang ada
            return maxRedVotes;
        }
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