import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.StringTokenizer;

public class Lab05 {

    private static InputReader in;
    static PrintWriter out;
    static AVLTree tree = new AVLTree();

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


class Node {
    int key, height;
    String playerName;
    Node left, right;

    Node(int key, String playerName) {
        this.key = key;
        this.playerName = playerName;
        this.height = 1;
    }
}


class AVLTree {

    Node root;

    Node rightRotate(Node y) {
        Node x = y.left;
        Node T2 = x.right;
  
        // Rotasikan
        x.right = y;
        y.left = T2;
  
        // Update tingginya
        y.height = max(height(y.left), height(y.right)) + 1;
        x.height = max(height(x.left), height(x.right)) + 1;
  
        // Return root baru
        return x;
    }

    Node leftRotate(Node x) {
        Node y = x.right;
        Node T2 = y.left;
  
        // Rotasikan
        y.left = x;
        x.right = T2;
  
        // Update tingginya
        x.height = max(height(x.left), height(x.right)) + 1;
        y.height = max(height(y.left), height(y.right)) + 1;
  
        // Return root baru
        return y;
    }

    Node insert(Node node, int key, String playerName) {
        /* 1.  Perform the normal BST insertion */
        if (node == null)
            return (new Node(key, playerName));
  
        if (key < node.key)
            node.left = insert(node.left, key, playerName);
        else if (key > node.key)
            node.right = insert(node.right, key, playerName);
        else // Duplicate keys not allowed
            return node;
  
        /* 2. Update height of this ancestor node */
        node.height = 1 + max(height(node.left),
                              height(node.right));
  
        /* 3. Get the balance factor of this ancestor
              node to check whether this node became
              unbalanced */
        int balance = getBalance(node);
  
        // If this node becomes unbalanced, then there
        // are 4 cases Left Left Case
        if (balance > 1 && key < node.left.key)
            return rightRotate(node);
  
        // Right Right Case
        if (balance < -1 && key > node.right.key)
            return leftRotate(node);
  
        // Left Right Case
        if (balance > 1 && key > node.left.key) {
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }
  
        // Right Left Case
        if (balance < -1 && key < node.right.key) {
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }
  
        /* return the (unchanged) node pointer */
        return node;
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
    int height(Node node) {
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
        return height(node.left) - height(node.right);
    }

    // Utility function to get maximum of two integers
    int max(int a, int b) {
        return (a > b) ? a : b;
    }
}

// REFERENCES:
// 1) https://www.geeksforgeeks.org/insertion-in-an-avl-tree/