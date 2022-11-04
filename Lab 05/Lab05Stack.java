import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.StringTokenizer;
import java.util.HashMap;
import java.util.Stack;
import java.util.ArrayList;

public class Lab05Stack {

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
        int leftRange = in.nextInt();
        int rightRange = in.nextInt();

        // CEK APAKAH ADA RANGE DALAM MAP KEYS (INKLUSIF)
        // LALU PRINT NAMA PERTAMA (ALIAS TERAKHIR MASUK)
        // JIKA MAP TERSEBUT LEBIH DARI 1, POP NAMA TERAKHIR MASUK SAJA
        out.println("DUO");
        int leftKey = 0; int rightKey = 0;
        String leftDuo = ""; String rightDuo = "";

        if (map.containsKey(leftRange)) {
            leftKey = leftRange;
            leftDuo = getRemoveNodeName(leftRange);
        } else {
            // successor
            for (Integer k : map.keySet()) {
                if (k > leftRange) {
                    leftKey = k;
                    leftDuo = getRemoveNodeName(k);
                    break;
                }
            }
        }

        if (map.containsKey(rightRange)) {
            rightKey = rightRange;
            rightDuo = getRemoveNodeName(rightRange);
        } else {
            // predecessor
            ArrayList<Integer> keys = new ArrayList<Integer>(map.keySet());
            for(int i=keys.size()-1; i>=0;i--){
                int k = keys.get(i);
                if (k < rightRange) {
                    rightKey = k;
                    rightDuo = getRemoveNodeName(k);
                }
            }
        }

        // PRINT
        if (leftDuo.equals("-1") || rightDuo.equals("-1")) {
            out.println("-1 -1");
        } else {
            out.println(leftDuo + " " + rightDuo);
            removeNode(leftKey); // TODO: MASALAH LOCAL GLOBAL
            removeNode(rightKey);
        }
    }

    // remove node decision
    static String getRemoveNodeName(int key) {
        String nama = "-1"; // default
        // remove node
        if (map.get(key).size() > 1) {
            nama = map.get(key).peek();
        } else if (map.get(key).size() == 1) {
            nama = map.get(key).peek();
        } else {
            // do nothing
        }
        return nama;
    }

    static void removeNode(int key) {
        // cek apakah ada di dalam map
        // TODO
        if (map.get(key).size() > 1) {
            map.get(key).pop();
        } else if (map.get(key).size() == 1) {
            map.get(key).pop();
            map.remove(key); // delete dari map
            tree.deleteNode(tree.root, key); // delete dari avl
        } else {
            // do nothing
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

// Node menyimpan power level saja (key dalam map) refer ke map
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
    Node deleteNode(Node node, int key) {
        if (node == null) {
            return node;
        }

        if (key < node.key) {
            node.left = deleteNode(node.left, key);
        } else if (key > node.key) {
            node.right = deleteNode(node.right, key);
        } else {
            // node with only one child or no child
            if ((node.left == null) || (node.right == null)) {
                Node temp = null;
                if (temp == node.left) {
                    temp = node.right;
                } else {
                    temp = node.left;
                }

                // No child case
                if (temp == null) {
                    temp = node;
                    node = null;
                } else {
                    // One child case
                    node = temp;
                }
            } else {
                // node with two children: Get the inorder successor (smallest in the right subtree)
                Node temp = lowerBound(node.right);

                // Copy the inorder successor's content to this node
                node.key = temp.key;

                // Delete the inorder successor
                node.right = deleteNode(node.right, temp.key);
            }
        }

        // If the tree had only one node then return
        if (node == null) {
            return node;
        }

        // Update height of this ancestor node
        node.height = Math.max(getHeight(node.left), getHeight(node.right)) + 1;

        // Get the balance factor of this ancestor node to check whether this node became unbalanced
        int balance = getBalance(node);

        // If this node becomes unbalanced, then there are 4 cases

        // Left Left Case
        if (balance > 1 && getBalance(node.left) >= 0) {
            return rightRotate(node);
        }

        // Left Right Case
        if (balance > 1 && getBalance(node.left) < 0) {
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }

        // Right Right Case
        if (balance < -1 && getBalance(node.right) <= 0) {
            return leftRotate(node);
        }

        // Right Left Case
        if (balance < -1 && getBalance(node.right) > 0) {
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }

        return node;
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

    Node findPredecessor(Node root, int key) {
        Node current = root;
        Node predecessor = null;
        while (current != null) {
            if (current.key == key) {
                if (current.left != null) {
                    predecessor = upperBound(current.left);
                }
                break;
            } else if (current.key > key) {
                current = current.left;
            } else {
                predecessor = current;
                current = current.right;
            }
        }
        return predecessor;
    }

    static Node suc = null;
    Node findSuccessor(Node root, int key) {
        // Base case
        if (root == null)
            return suc;
    
        // If key is present at root
        if (root.key == key)
        {
            // The minimum value in
            // right subtree is successor
            if (root.right != null)
            {
                Node tmp = root.right;
                
                while (tmp.left != null)
                    tmp = tmp.left;
                    
                suc = tmp;
            }
            return suc;
        }
    
        // If key is smaller than
        // root's key, go to left subtree
        if (root.key > key) {
            suc = root;
            findSuccessor(root.left , key);
        } else { // Go to right subtree
            findSuccessor(root.right, key);
        }
        return suc;
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
}

// References:
// 1) https://www.geeksforgeeks.org/deletion-in-an-avl-tree
// 2) https://www.geeksforgeeks.org/stack-class-in-java/
// 3) https://docs.oracle.com/javase/8/docs/api/java/util/HashMap.html
// 4) https://www.geeksforgeeks.org/inorder-predecessor-successor-given-key-bst/