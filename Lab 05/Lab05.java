import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.StringTokenizer;
import java.util.HashMap;
import java.util.Stack;

public class Lab05 {

    private static InputReader in;
    static PrintWriter out;
    static AVLTree tree = new AVLTree();

    // IDE : AVL node menyimpan key menuju key dalam map yg berisi value-value

    // Map penyimpan node power level sama [ PowerLevel : <Stack of Nama Peserta> ]
    static HashMap<Integer, Stack<String>> map = new HashMap<Integer, Stack<String>>();

    // Jumlah Before K (new node)
    static int beforeK = 0;

    public static void main(String[] args) {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        int numOfInitialPlayers = in.nextInt();
        for (int i = 0; i < numOfInitialPlayers; i++) {
            String name = in.next();
            int powerLevel = in.nextInt();

            // INSERT KEY [POWER LEVEL] KE AVL
            tree.root = tree.insertNode(tree.root, powerLevel); 

            // INSERT NAMA KE MAP
            if (map.containsKey(powerLevel)) {
                map.get(powerLevel).push(name);
            } else {
                Stack<String> stack = new Stack<String>();
                stack.push(name); 
                map.put(powerLevel, stack); 
            }
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
        String name = in.next();
        int powerLevel = in.nextInt();

        // INSERT KEY [POWER LEVEL] KE AVL
        tree.root = tree.insertNode(tree.root, powerLevel); 

        // INSERT NAMA KE MAP
        if (map.containsKey(powerLevel)) {
            map.get(powerLevel).push(name);
        } else {
            Stack<String> stack = new Stack<String>();
            stack.push(name); 
            map.put(powerLevel, stack); 
        }

        // PRINT JUMLAH NODE SEBELUM NODE SAAT INI
        // out.println(tree.countNodes(tree.root, powerLevel));
        inOrderTraversal(tree.root, powerLevel);
        out.println(beforeK);
        beforeK = 0; // reset
    }

    static void inOrderTraversal(Node root, int powerLevel) {
        if (root != null) {
            inOrderTraversal(root.left, powerLevel);
            if (root.key < powerLevel) {
                beforeK += map.get(root.key).size();
            }
            inOrderTraversal(root.right, powerLevel);
        }
    }

    static void handleQueryDuo() {
        // TODO
        int l = in.nextInt();
        int r = in.nextInt();

        // CARI SUCCESSOR OF L & R
        Node predecessorL = tree.findPredecessor(tree.root, null, l);
        Node predecessorR = tree.findPredecessor(tree.root, null, r);
        Node successorL = tree.findSuccessor(tree.root, null, l);
        Node successorR = tree.findSuccessor(tree.root, null, r);
        out.println("Predecessor of " + l + " is " + predecessorL);
        out.println("Successor of "+l+" is "+successorL);

        out.println("Predecessor of " + r + " is " + predecessorR);
        out.println("Successor of "+r+" is "+successorR);
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

// Node menyimpan power level saja (key dalam map) refer ke map
class Node {
    int key, height; // key => sama dengan key map (power level)
    Node left, right;

    Node(int key) {
        this.key = key;
        this.height = 1;
    }

    // Overriding toString() method of String class
    @Override
    public String toString() {
        return this.key + "";
    }
}


class AVLTree {

    Node root;

    // Implement right rotate
    Node rightRotate(Node node) {
        Node leftChild = node.left;
        Node rightChildOfLeftChild = leftChild.right;

        // Perform rotation
        leftChild.right = node;
        node.left = rightChildOfLeftChild;

        // Update height
        node.height = Math.max(getHeight(node.left), getHeight(node.right)) + 1;
        leftChild.height = Math.max(getHeight(leftChild.left), getHeight(leftChild.right)) + 1;

        // Return new root
        return leftChild;
    }

    // Implement left rotate
    Node leftRotate(Node node) {
        Node rightChild = node.right;
        Node leftChildOfRightChild = rightChild.left;

        // Perform rotation
        rightChild.left = node;
        node.right = leftChildOfRightChild;

        // Update height
        node.height = Math.max(getHeight(node.left), getHeight(node.right)) + 1;
        rightChild.height = Math.max(getHeight(rightChild.left), getHeight(rightChild.right)) + 1;

        // Return new root
        return rightChild;
    }

    // Implement insert node to AVL Tree
    Node insertNode(Node node, int key) {
        if (node == null) {
            return (new Node(key));
        }

        if (key < node.key) {
            node.left = insertNode(node.left, key);
        } else if (key > node.key) {
            node.right = insertNode(node.right, key);
        } else {
            return node;
        }

        // Update height
        node.height = 1 + Math.max(getHeight(node.left), getHeight(node.right));

        // Get balance factor
        int balance = getBalance(node);

        // If this node becomes unbalanced, then there are 4 cases

        // Left Left Case
        if (balance > 1 && key < node.left.key) {
            return rightRotate(node);
        }

        // Right Right Case
        if (balance < -1 && key > node.right.key) {
            return leftRotate(node);
        }

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

        return node;
    }

    // // Implement delete node from AVL Tree
    // Node deleteNode(Node node, int key) {
    //     // TODO: implement delete node
    //     return null;
    // }

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

    // Utility function to get number of nodes before this node
    int countNodes(Node node, int key) {
        if (node == null) {
            return 0;
        }

        if (key < node.key) {
            return countNodes(node.left, key);
        } else if (key > node.key) {
            return 1 + getHeight(node.left) + countNodes(node.right, key);
        } else {
            return getHeight(node.left);
        }
    }

    // successor case
    // 1) saat node x memiliki right subtree > cari lowerbound dari subtree kanan
    // 2) saat node x tidak memiliki right subtree dan x merupakan right child dari parentnya > the last left is the answer
    // 3) saat node x tidak memiliki right subtree dan x merupakan left child dari parentnya
    // Recursive function to find an inorder successor for the given key in the BST
    Node findSuccessor(Node root, Node succ, int key) { // return null when gaada successor
        // base case
        if (root == null) {
            return succ;
        }

        // if a node with the desired value is found, the successor is the minimum
        // value node in its right subtree (if any)
        if (root.key == key)
        {
            if (root.right != null) {
                return lowerBound(root.right);
            }
        }

        // if the given key is less than the root node, recur for the left subtree
        else if (key < root.key)
        {
            // update successor to the current node before recursing in the
            // left subtree
            succ = root;
            return findSuccessor(root.left, succ, key);
        }

        // if the given key is more than the root node, recur for the right subtree
        else {
            return findSuccessor(root.right, succ, key);
        }

        return succ;
    }

    // predecessor case
    // 1) saat node x memiliki left subtree
    // 2) saat node x tidak memiliki left subtree dan x merupakan left child dari parentnya
    // 3) saat node x tidak memiliki left subtree dan x merupakan right child dari parentnya
    // Recursive function to find inorder predecessor for a given key in the BST
    Node findPredecessor(Node root, Node prec, int key) { // return null when gaada successor
        // base case
        if (root == null) {
            return prec;
        }
 
        // if a node with the desired value is found, the predecessor is the maximum
        // value node in its left subtree (if any)
        if (root.key == key)
        {
            if (root.left != null) {
                return upperBound(root.left);
            }
        }
 
        // if the given key is less than the root node, recur for the left subtree
        else if (key < root.key) {
            return findPredecessor(root.left, prec, key);
        }
 
        // if the given key is more than the root node, recur for the right subtree
        else {
            // update predecessor to the current node before recursing
            // in the right subtree
            prec = root;
            return findPredecessor(root.right, prec, key);
        }
        return prec;
    }
}

// References:
// 1) https://www.geeksforgeeks.org/deletion-in-an-avl-tree
// 2) https://www.geeksforgeeks.org/stack-class-in-java/
// 3) https://docs.oracle.com/javase/8/docs/api/java/util/HashMap.html

// AVL Methods:
// 1) https://www.youtube.com/watch?v=psFKTGahpCs
// 2) https://www.youtube.com/watch?v=JdmAYw5h3G8
// 3 https://www.techiedelight.com/find-inorder-successor-given-key-bst/)
// 4) https://www.techiedelight.com/find-inorder-predecessor-given-key-bst/