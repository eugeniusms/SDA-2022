
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.StringTokenizer;
import java.util.*;

public class AdjList {

    private static InputReader in;
    static PrintWriter out;

    static HashMap<Integer, ArrayList<Integer>> adjList;

    public static void main(String[] args) {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        // input
        int N = in.nextInt(); // jumlah node
        adjList = new HashMap<Integer, ArrayList<Integer>>();

        int E = in.nextInt(); // jumlah edge (u,v)
        for (int i = 0; i < E; i++) {
            int u = in.nextInt();
            int v = in.nextInt();
            if (adjList.containsKey(u)) { // map contains key
                adjList.get(u).add(v);
            } else { // not yet contains key
                ArrayList<Integer> arrU = new ArrayList<Integer>();
                arrU.add(v);
                adjList.put(u, arrU);
            }
            
        }

        printAdjList(N);

        out.close();
    }

    // method
    static void printAdjList(int size) {
        for (Integer key : adjList.keySet()) {
            out.print(key+" : ");
            for (Integer val : adjList.get(key)) {
                out.print(val+",");
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