import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.StringTokenizer;
import java.util.HashMap;
import java.util.Stack;

public class Lab05Stack {

    private static InputReader in;
    static PrintWriter out;
    static AVLTree tree = new AVLTree();

    // Map penyimpan node power level sama [ PowerLevel : <Stack of Nama Peserta> ]
    static HashMap<Integer, Stack<String>> map = new HashMap<Integer, Stack<String>>();

    public static void main(String[] args) {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        int numOfInitialPlayers = in.nextInt();
        for (int i = 0; i < numOfInitialPlayers; i++) {
            // TODO: process inputs
        }

        int numOfQueries = in.nextInt();
        for (int i = 0; i < numOfQueries; i++) {
            String cmd = in.next();
            if (cmd.equals("MASUK")) {
                handleQueryMasuk();
            } else {
                handleQueryDuo();
            }
        }

        out.close();
    }

    static void handleQueryMasuk() {
        // TODO
    }

    static void handleQueryDuo() {
        // TODO
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

// Node menyimpan power level saja (key dalam map)
class Node {
    int key, height; // key => sama dengan key map (power level)
    Node left, right;

    Node(int key) {
        this.key = key;
        this.height = 1;
    }
}


class AVLTree {

    Node root;

    Node rightRotate(Node node) {
        // TODO: implement right rotate
        return null;
    }

    Node leftRotate(Node node) {
        // TODO: implement left rotate
        return null;
    }

    Node insertNode(Node node, int key, String playerName) {
        // TODO: implement insert node
        return null;
    }

    Node deleteNode(Node node, int key) {
        // TODO: implement delete node
        return null;
    }

    Node lowerBound(Node node, int value) {
        // TODO: return node with the lowest key that is >= value
        return null;
    }

    Node upperBound(Node node, int value) {
        // TODO: return node with the greatest key that is <= value
        return null;
    }

    // Utility function to get height of node
    int getHeight(Node node) {
        if (node == null) {
            return 0;
        }
        return node.height;
    }

    // Utility function to get balance factor of node
    int getBalance(Node node) {
        if (node == null) {
            return 0;
        }
        return getHeight(node.left) - getHeight(node.right);
    }
}