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
            String playerName = in.next();
            int powerLevel = in.nextInt();
            tree.root = tree.insertNode(tree.root, powerLevel, playerName);
        }

        tree.preOrder(tree.root);

        // int numOfQueries = in.nextInt();
        // for (int i = 0; i < numOfQueries; i++) {
        //     String cmd = in.next();
        //     if (cmd.equals("MASUK")) {
        //         handleQueryMasuk();
        //     } else {
        //         handleQueryDuo();
        //     }
        // }

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
        y.height = max(getHeight(y.left), getHeight(y.right)) + 1;
        x.height = max(getHeight(x.left), getHeight(x.right)) + 1;
  
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
        x.height = max(getHeight(x.left), getHeight(x.right)) + 1;
        y.height = max(getHeight(y.left), getHeight(y.right)) + 1;
  
        // Return root baru
        return y;
    }

    Node insertNode(Node node, int key, String playerName) {
        /* 1.  Perform the normal BST insertion */
        if (node == null)
            return (new Node(key, playerName));
  
        if (key < node.key)
            node.left = insertNode(node.left, key, playerName);
        else if (key > node.key)
            node.right = insertNode(node.right, key, playerName);
        else // Duplicate keys not allowed
            return node;
  
        /* 2. Update height of this ancestor node */
        node.height = 1 + max(getHeight(node.left),
                              getHeight(node.right));
  
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
        // STEP 1: PERFORM STANDARD BST DELETE 
        if (root == null) 
            return root; 
  
        // If the key to be deleted is smaller than 
        // the root's key, then it lies in left subtree 
        if (key < root.key) 
            root.left = deleteNode(root.left, key); 
  
        // If the key to be deleted is greater than the 
        // root's key, then it lies in right subtree 
        else if (key > root.key) 
            root.right = deleteNode(root.right, key); 
  
        // if key is same as root's key, then this is the node 
        // to be deleted 
        else
        { 
  
            // node with only one child or no child 
            if ((root.left == null) || (root.right == null)) 
            { 
                Node temp = null; 
                if (temp == root.left) 
                    temp = root.right; 
                else
                    temp = root.left; 
  
                // No child case 
                if (temp == null) 
                { 
                    temp = root; 
                    root = null; 
                } 
                else // One child case 
                    root = temp; // Copy the contents of 
                                // the non-empty child 
            } 
            else
            { 
  
                // node with two children: Get the inorder 
                // successor (smallest in the right subtree) 
                Node temp = lowerBound(root.right); 
  
                // Copy the inorder successor's data to this node 
                root.key = temp.key; 
  
                // Delete the inorder successor 
                root.right = deleteNode(root.right, temp.key); 
            } 
        } 
  
        // If the tree had only one node then return 
        if (root == null) 
            return root; 
  
        // STEP 2: UPDATE HEIGHT OF THE CURRENT NODE 
        root.height = max(getHeight(root.left), getHeight(root.right)) + 1; 
  
        // STEP 3: GET THE BALANCE FACTOR OF THIS NODE (to check whether 
        // this node became unbalanced) 
        int balance = getBalance(root); 
  
        // If this node becomes unbalanced, then there are 4 cases 
        // Left Left Case 
        if (balance > 1 && getBalance(root.left) >= 0) 
            return rightRotate(root); 
  
        // Left Right Case 
        if (balance > 1 && getBalance(root.left) < 0) 
        { 
            root.left = leftRotate(root.left); 
            return rightRotate(root); 
        } 
  
        // Right Right Case 
        if (balance < -1 && getBalance(root.right) <= 0) 
            return leftRotate(root); 
  
        // Right Left Case 
        if (balance < -1 && getBalance(root.right) > 0) 
        { 
            root.right = rightRotate(root.right); 
            return leftRotate(root); 
        } 
  
        return root; 
    }

    /* Given a non-empty binary search tree, return the 
    node with minimum key value found in that tree. 
    Note that the entire tree does not need to be 
    searched. */
    Node lowerBound(Node node) {
        Node current = node; 
  
        /* loop down to find the leftmost leaf */
        while (current.left != null) 
        current = current.left; 
  
        return current; 
    }

    Node upperBound(Node node) {
        Node current = node; 
  
        /* loop down to find the rightmost leaf */
        while (current.right != null) 
        current = current.right; 
  
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

    // Utility function to get maximum of two integers
    int max(int a, int b) {
        return (a > b) ? a : b;
    }

    // A utility function to print preorder traversal of 
    // the tree. The function also prints height of every 
    // node 
    void preOrder(Node node) { 
        if (node != null) { 
            System.out.print(node.key + " "); 
            preOrder(node.left); 
            preOrder(node.right); 
        } 
    } 
}

// REFERENCES:
// 1) https://www.geeksforgeeks.org/insertion-in-an-avl-tree/
// 2) https://www.geeksforgeeks.org/deletion-in-an-avl-tree/