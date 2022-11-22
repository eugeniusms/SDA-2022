
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.StringTokenizer;
import java.util.*;

public class Game {

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
        adjMatrix = new boolean[N][N]; 

        // int E = in.nextInt(); // jumlah edge (u,v)
        // for (int i = 0; i < E; i++) {
        //     int u = in.nextInt();
        //     int v = in.nextInt();
        //     adjMatrix = 
        // }

        // for(int i = 0; i < N; i++) {
        //     for(int j = 0;  j < N; j++) {
        //         adjMatrix[i][j] = in.nextInt();
        //     }
        // }
        printAdjMatrix(N);

        out.close();
    }

    // method
    static void printAdjMatrix(int size) {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                out.print(adjMatrix[i][j]+" ");
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

// class
class Node {
    String label;

    Node(String label) {
        this.label = label;
    }
}

class Edge {
    String label;
    int weight;
}

class Graph {
    List<Node> nodeList;
    Edge[][] adjacencyadjMatrix;
}

// references

// Graph - <Key>,<Value1>...<ValueN>