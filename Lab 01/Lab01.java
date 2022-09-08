import java.io.*;
import java.util.StringTokenizer;

public class Lab01 {
    private static InputReader in;
    private static PrintWriter out;

    static int getTotalDeletedLetters(int N, char[] x) {
        // TODO: implement method getTotalDeletedLetter(int, char[]) to get the answer
        return 0;
    }

    // Fungsi utama berjalannya program
    public static void main(String[] args) throws IOException {
        // Melakukan inisiasi input dan output
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        // Membaca nilai dari N (total input yang ingin dimasukkan)
        int N = in.nextInt();

        // Read value of x
        char[] x = new char[N];
        for (int i = 0; i < N; ++i) {
            x[i] = in.next().charAt(0);
        }

        int ans = getTotalDeletedLetters(N, x);
        out.println(ans);

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