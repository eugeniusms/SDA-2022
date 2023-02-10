
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.StringTokenizer;
import java.util.*;

public class AdjMatrix {

    private static InputReader in;
    static PrintWriter out;

    static boolean[][] adjMatrix;

    public static void main(String[] args) {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        // input
        int N = in.nextInt(); // jumlah node
        adjMatrix = new boolean[N+1][N+1]; // [0] dikosongin aja 

        int E = in.nextInt(); // jumlah edge (u,v)
        for (int i = 0; i < E; i++) {
            int u = in.nextInt();
            int v = in.nextInt();
            adjMatrix[u][v] = true;
            adjMatrix[v][u] = true;
        }

        printAdjMatrix(N);

        out.close();
    }

    // method
    static void printAdjMatrix(int size) {
        for (int i = 0; i < size; i++) {
            out.print("["+i+"]");
        }
        out.println();
        for (int i = 1; i < size; i++) {
            out.print("["+i+"]");
            for (int j = 1; j < size; j++) {
                out.print("["+(adjMatrix[i][j] ? 1 : 0)+"]");
            }
            out.println();
        }
    }

    // taken from https://codeforces.com/submissions/Petr
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