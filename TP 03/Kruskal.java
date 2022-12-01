
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.*;
import java.util.StringTokenizer;


public class Kruskal {
    private static InputReader in;
    private static PrintWriter out;

    static List<List<Node> > adj;
 
    public static void main(String[] args) {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        int V = in.nextInt(); int E = in.nextInt();

        // inisiate vertex
        adj = new ArrayList<List<Node> >();
        for (int i = 0; i <= V; i++) {
            List<Node> item = new ArrayList<Node>();
            adj.add(item);
        }

        // inisiate edges
        for (int i = 0; i < E; i++) {
            int start = in.nextInt(); int end = in.nextInt(); long L = in.nextInt(); long S = in.nextInt();
            adj.get(start).add(new Node(end, L, S));
            adj.get(end).add(new Node(start, L, S));
        }   

        int counter = 0;
        for (List<Node> lst : adj) {
            out.print(counter+" : ");
            for (Node n : lst) {
                out.print(n.node+" ");
            }
            counter++;
            out.println();
        }

        out.close();
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

class Node implements Comparable<Node> {
    public int node;
    public long L;
    public long S;
    // public boolean isKurcaciExist = false;
 
    public Node() {}
    public Node(int node, long L, long S) {
        this.node = node;
        this.L = L;
        this.S = S;
    }

    @Override 
    public int compareTo(Node other) {
        if (this.L < other.L)
            return -1;
        if (this.L > other.L)
            return 1;
        return 0;
    }
}