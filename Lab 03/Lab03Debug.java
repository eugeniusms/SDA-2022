import java.io.*;
import java.util.StringTokenizer;

public class Lab03Debug {
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
        int solution = getMaxRedVotes(0, N);
        out.print(solution);

        // Tutup OutputStream
        out.close();
    }

    public static int getMaxRedVotes(int start, int end) {
        out.println("start: " + start + " | end: " + end); // TEST
        // Jika start == end maka sudahi rekursi
        if (start == end) {
            return 0;
        }

        // Inisiasi variabel
        int maxVotes = 0;
        int red = 0;
        int blue = 0;
        int redVotes = 0;
        // Untuk setiap pecahan nilai iterasi from start -> i sampai end
        for (int i = start; i < end; i++) {
            // Menghitung jumlah R, B pada setiap iterasi panjang A
            if (A[i] == 'R') {
                red += 1;
            } else {
                blue += 1;
            }

            // Mengambil data redVotes 
            redVotes = getVotes(red, blue);
            // Mencatat nilai max dari vote yang ada
            if (redVotes > maxVotes) {
                maxVotes = redVotes;
                // Mencatat nilai start letak perpotongan array pada nilai maxnya
                start = i;
            }
        }

        out.println("max: " + maxVotes); // TEST

        // Terdapat 2 kasus lanjutan:
        // 1) Semua nilai berisikan 0, artinya start = posisi terdepan potongan array
        // 2) Ada satu nilai max, artinya start = posisi nilai maxnya
        // Namun keduanya memiliki pendekatan next yang sama
        // Sama-sama next pada start + 1 (sisa potongan array)
        return maxVotes + getMaxRedVotes(start + 1, end);
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