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

    // Calculating attributes
    static int counter = 0;

    public static void main(String[] args) {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        int numOfInitialPlayers = in.nextInt();
        for (int i = 0; i < numOfInitialPlayers; i++) {
            String playerName = in.next();
            int powerLevel = in.nextInt();
            Node node = new Node(powerLevel, playerName, i);
            tree.root = tree.insertNode(tree.root, node);
        }

        // menambahkan i ke MASUK LAGI

        int numOfQueries = in.nextInt();
        int counterPlusPlayer = 0;
        for (int i = 0; i < numOfQueries; i++) {
            String cmd = in.next();
            if (cmd.equals("MASUK")) {
                handleQueryMasuk(numOfInitialPlayers+counterPlusPlayer);
                counterPlusPlayer++;
            } else {
                handleQueryDuo();
            }
        }

        // tree.preOrder(tree.root);
        // tree.inOrder(tree.root);

        out.close();
    }

    static void handleQueryMasuk(int urutanDaftar) {
        // Get input and insert node
        String playerName = in.next();
        int powerLevel = in.nextInt();
        Node node = new Node(powerLevel, playerName, urutanDaftar);
        tree.root = tree.insertNode(tree.root, node);

        countBefore(node);        
    }

    static void countBefore(Node node) {
        counter = 0; // reset again
        count(tree.root, node);
    }

    // Count node before with inOrder Concept
    // MINUS: GABISA NGESTOP PAS KETEMU NODE :) PIKIRIN CARA LAGI NANTI PAKAI HEIGHT(?)
    static void count(Node rootNode, Node node) {
        // Base Case
        if (rootNode == node) {
            out.println(counter-1); // print index of node
        }

        // Recursive Case
        if (rootNode != null) { 
            count(rootNode.left, node); 
            counter++;
            count(rootNode.right, node);
        }
    } 

    static void handleQueryDuo() {
        int powerLevel1 = in.nextInt();
        int powerLevel2 = in.nextInt();
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


class Node implements Comparable<Node> {
    int powerLevel, height, urutanDaftar;
    String playerName;
    Node left, right;

    Node(int powerLevel, String playerName, int urutanDaftar) {
        this.powerLevel = powerLevel;
        this.playerName = playerName;
        this.urutanDaftar = urutanDaftar;
        this.height = 1;
    }

    // compareTo (ngikutin tandanya aja)
    // this.compareTo(other) > 0 = this > other
    // this.compareTo(other) < 0 = this < other
    @Override
    public int compareTo(Node other) { // default this < other
        // jika power level sama
        if (this.powerLevel == other.powerLevel) {
            // bandingkan by urutan daftar
            return this.urutanDaftar - other.urutanDaftar;
        }
        // bandingkan berdasarkan power level
        return this.powerLevel - other.powerLevel;
    }
}

// Insert by
// 1) key
// 2) if key same, then urutanDaftar
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

    Node insertNode(Node rootNode, Node node) {
        /* 1.  Perform the normal BST insertion */
        if (rootNode == null)
            return node;
  
        // review compareTo 
        // this.compareTo(other) > 0 = this > other
        // this.compareTo(other) < 0 = this < other
        if (node.compareTo(rootNode) < 0)
            rootNode.left = insertNode(rootNode.left, node);
        else if (node.compareTo(rootNode) > 0)
            rootNode.right = insertNode(rootNode.right, node);
        else // Duplicate keys not allowed
            return node;
  
        /* 2. Update height of this ancestor node */
        rootNode.height = 1 + max(getHeight(rootNode.left),
                              getHeight(rootNode.right));
  
        /* 3. Get the balance factor of this ancestor
              node to check whether this node became
              unbalanced */
        int balance = getBalance(rootNode);
  
        // If this node becomes unbalanced, then there
        // are 4 cases Left Left Case
        if (balance > 1 && node.compareTo(rootNode.left) < 0)
            return rightRotate(rootNode);
  
        // Right Right Case
        if (balance < -1 && node.compareTo(rootNode.right) > 0)
            return leftRotate(rootNode);
  
        // Left Right Case
        if (balance > 1 && node.compareTo(rootNode.left) > 0) {
            rootNode.left = leftRotate(rootNode.left);
            return rightRotate(rootNode);
        }
  
        // Right Left Case
        if (balance < -1 && node.compareTo(rootNode.right) < 0) {
            rootNode.right = rightRotate(rootNode.right);
            return leftRotate(rootNode);
        }
  
        /* return the (unchanged) node pointer */
        return rootNode;
    }

    Node deleteNode(Node root, Node node) {
        // STEP 1: PERFORM STANDARD BST DELETE 
        if (root == null) 
            return root; 
  
        // If the key to be deleted is smaller than 
        // the root's key, then it lies in left subtree 
        if (node.compareTo(root) < 0)
            root.left = deleteNode(root.left, node); 
  
        // If the key to be deleted is greater than the 
        // root's key, then it lies in right subtree 
        else if (node.compareTo(root) > 0) 
            root.right = deleteNode(root.right, node); 
  
        // Jika key sama tetapi urutan daftar beda / masih ada right child node dengan nilai key sama
        else if (node.compareTo(root) == 0 && node.compareTo(root.right) == 0)
            root.right = deleteNode(root.right, node);

        // if key is same as root's key && urutan daftar sama (tidak ada right child node
        // dengan key yg sama), then this is the node to be deleted 
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
                root.playerName = temp.playerName;
                root.urutanDaftar = temp.urutanDaftar;
                root.powerLevel = temp.powerLevel;
  
                // Delete the inorder successor 
                root.right = deleteNode(root.right, temp);
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
            System.out.print(node.powerLevel + " "); 
            preOrder(node.left); 
            preOrder(node.right); 
        } 
    } 

    void inOrder(Node node) { 
        if (node != null) { 
            inOrder(node.left); 
            System.out.println("ORDER: " + node.playerName + "[" + node.powerLevel + "|" + node.urutanDaftar + "]" + " "); 
            inOrder(node.right); 
        } 
    } 

    // A utility function to search a given key in BST
    Node search(Node root, int pl) {
        // Base Cases: root is null or key is present at root
        if (root==null || root.powerLevel==pl && root.right.powerLevel!=pl) // cek juga right sama ngga (pilih terakhir soalnya)
            return root;
    
        // Key is greater than root's key
        if (root.powerLevel < pl)
            return search(root.right, pl);
    
        // Key is smaller than root's key
        return search(root.left, pl);
    }
}

// REFERENCES:
// 1) https://www.geeksforgeeks.org/insertion-in-an-avl-tree/
// 2) https://www.geeksforgeeks.org/deletion-in-an-avl-tree/
// 3) https://www.techiedelight.com/find-inorder-predecessor-given-key-bst/
// 4) https://www.geeksforgeeks.org/tree-traversals-inorder-preorder-and-postorder/
// 5) https://www.geeksforgeeks.org/binary-search-tree-set-1-search-and-insertion/
// 6) https://www.geeksforgeeks.org/inorder-predecessor-successor-given-key-bst/
// 7) https://www.geeksforgeeks.org/comparable-vs-comparator-in-java/