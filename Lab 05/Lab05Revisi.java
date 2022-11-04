import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.StringTokenizer;

public class Lab05Revisi {

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

class Node implements Comparable<Node>{
    int power, height, urutan;
    Node left, right;
    String nama;

    Node(int power, String nama, int urutan) {
        this.nama = nama;
        this.power = power;
        this.urutan = urutan;
        this.height = 1;
    }

    // compareTo (ngikutin tandanya aja)
    // this.compareTo(other) > 0 = this > other
    // this.compareTo(other) < 0 = this < other
    @Override
    public int compareTo(Node other) { // default this < other
        // jika power level sama
        if (this.power == other.power) {
            // bandingkan by urutan daftar
            return this.urutan - other.urutan;
        }
        // bandingkan berdasarkan power level
        return this.power - other.power;
    }
}


class AVLTree {

    Node root;

    Node rightRotate(Node node) {
        // Implement right rotate AVL Tree
        Node newRoot = node.left;
        node.left = newRoot.right;
        newRoot.right = node;
        node.height = Math.max(getHeight(node.left), getHeight(node.right)) + 1;
        newRoot.height = Math.max(getHeight(newRoot.left), getHeight(newRoot.right)) + 1;
        return newRoot;
    }

    Node leftRotate(Node node) {
        // Implement left rotate AVL Tree
        Node newRoot = node.right;
        node.right = newRoot.left;
        newRoot.left = node;
        node.height = Math.max(getHeight(node.left), getHeight(node.right)) + 1;
        newRoot.height = Math.max(getHeight(newRoot.left), getHeight(newRoot.right)) + 1;
        return newRoot;
    }

    Node insertNode(Node root, int node) {
        // Implement insert node to root node AVL Tree
        if (root == null) {
            return new Node(node, null, 0);
        }
        if (node < root.power) {
            root.left = insertNode(root.left, node);
        } else if (node > root.power) {
            root.right = insertNode(root.right, node);
        } else {
            return root;
        }
        root.height = 1 + Math.max(getHeight(root.left), getHeight(root.right));
        int balance = getBalance(root);
        if (balance > 1 && node < root.left.power) {
            return rightRotate(root);
        }
        if (balance < -1 && node > root.right.power) {
            return leftRotate(root);
        }
        if (balance > 1 && node > root.left.power) {
            root.left = leftRotate(root.left);
            return rightRotate(root);
        }
        if (balance < -1 && node < root.right.power) {
            root.right = rightRotate(root.right);
            return leftRotate(root);
        }
        return root;
    }

    // Implement delete node from the root of AVL Tree
    Node deleteNode(Node root, Node node) {
        if (root == null) {
            return root;
        }
        if (node.compareTo(root) < 0) {
            root.left = deleteNode(root.left, node);
        } else if (node.compareTo(root) > 0) {
            root.right = deleteNode(root.right, node);
        } else {
            if ((root.left == null) || (root.right == null)) {
                Node temp = null;
                if (temp == root.left) {
                    temp = root.right;
                } else {
                    temp = root.left;
                }
                if (temp == null) {
                    temp = root;
                    root = null;
                } else {
                    root = temp;
                }
            } else {
                Node temp = lowerBound(root.right);
                root.power = temp.power;
                root.right = deleteNode(root.right, temp);
            }
        }
        if (root == null) {
            return root;
        }
        root.height = Math.max(getHeight(root.left), getHeight(root.right)) + 1;
        int balance = getBalance(root);
        if (balance > 1 && getBalance(root.left) >= 0) {
            return rightRotate(root);
        }
        if (balance > 1 && getBalance(root.left) < 0) {
            root.left = leftRotate(root.left);
            return rightRotate(root);
        }
        if (balance < -1 && getBalance(root.right) <= 0) {
            return leftRotate(root);
        }
        if (balance < -1 && getBalance(root.right) > 0) {
            root.right = rightRotate(root.right);
            return leftRotate(root);
        }
        return root;
    }
        
        

    Node lowerBound(Node node) {
        // Return node with the lowest from this node
        Node current = node;
        while (current.left != null) {
            current = current.left;
        }
        return current;
    }

    Node upperBound(Node node) {
        // Return node with the greatest from this node
        Node current = node;
        while (current.right != null) {
            current = current.right;
        }
        return current;
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