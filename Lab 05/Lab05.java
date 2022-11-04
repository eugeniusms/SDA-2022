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
            Node node = new Node(powerLevel, playerName);
            tree.root = tree.insertNode(tree.root, node);
        }

        // tree.preOrder(tree.root);
        // tree.inOrder(tree.root);

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
        // Get input and insert node
        String playerName = in.next();
        int powerLevel = in.nextInt();
        Node node = new Node(powerLevel, playerName);
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

        out.println(findDuoFirst(tree.root, powerLevel1) + "YOW");
    }

    static String findDuoFirst(Node root, int key) {
        // cari node sesuai keynya
        Node result = tree.search(root, key);
        String playerName = "-1";
        if (result != null) {
            playerName = result.playerName;
            // TODO: Delete node di sini
            return playerName;
        } else {
            // kalau null (tidak ada node dengan key segitu) maka cari successor
            result = tree.findSuccessor(root, result, key);
            if (result != null) {
                playerName = result.playerName;
                // TODO: Delete node di sini
                return playerName;
            } else {
                return playerName;
            }
        }
    }

    

    // static String findDuoSecond(Node root, int key) {
    //     // TODO
    // }

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
  
        if (node.key < rootNode.key)
            rootNode.left = insertNode(rootNode.left, node);
        else if (node.key > rootNode.key)
            rootNode.right = insertNode(rootNode.right, node);
        else 
            // Jika nilai sama maka insert node ke right childnya karena pasti urutan daftar lebih besar
            rootNode.right = insertNode(rootNode.right, node);
  
        /* 2. Update height of this ancestor node */
        rootNode.height = 1 + max(getHeight(rootNode.left),
                              getHeight(rootNode.right));
  
        /* 3. Get the balance factor of this ancestor
              node to check whether this node became
              unbalanced */
        int balance = getBalance(rootNode);
  
        // If this node becomes unbalanced, then there
        // are 4 cases Left Left Case
        if (balance > 1 && node.key < rootNode.left.key)
            return rightRotate(rootNode);
  
        // Right Right Case
        if (balance < -1 && node.key > rootNode.right.key)
            return leftRotate(rootNode);
  
        // Left Right Case
        if (balance > 1 && node.key > rootNode.left.key) {
            rootNode.left = leftRotate(rootNode.left);
            return rightRotate(rootNode);
        }
  
        // Right Left Case
        if (balance < -1 && node.key < rootNode.right.key) {
            rootNode.right = rightRotate(rootNode.right);
            return leftRotate(rootNode);
        }
  
        /* return the (unchanged) node pointer */
        return rootNode;
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
  
        // Jika key sama tetapi urutan daftar beda / masih ada right child node dengan nilai key sama
        else if (key == root.key && key == root.right.key)
            root.right = deleteNode(root.right, key);

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
        while (current.left != null && ) 
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

    void inOrder(Node node) { 
        if (node != null) { 
            inOrder(node.left); 
            System.out.print(node.key + " "); 
            inOrder(node.right); 
        } 
    } 

    // A utility function to search a given key in BST
    Node search(Node root, int key) {
        // Base Cases: root is null or key is present at root
        if (root==null || root.key==key && root.right.key!=key) // cek juga right sama ngga (pilih terakhir soalnya)
            return root;
    
        // Key is greater than root's key
        if (root.key < key)
            return search(root.right, key);
    
        // Key is smaller than root's key
        return search(root.left, key);
    }

    // Recursive function to find inorder predecessor for a given key in the BST
    Node findPredecessor(Node root, Node prec, int key) {
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

    // Recursive function to find inorder predecessor for a given key in the BST
    Node findSuccessor(Node root, Node succ, int key) {
        // base case
        if (root == null) {
            return succ;
        }

        // jika ditemukan parent dari node yg dicari successornya
        if (root.key == key) {
            if (root.right != null) {
                return lowerBound(root.right);
            }
        }
 
        // mencari letak node yg akan dicari successornya
        else if (key > root.key) {
            return findSuccessor(root.right, succ, key);
        } else {
            succ = root;
            return findSuccessor(root.left, succ, key);
        }
        return succ;
    }
}

// REFERENCES:
// 1) https://www.geeksforgeeks.org/insertion-in-an-avl-tree/
// 2) https://www.geeksforgeeks.org/deletion-in-an-avl-tree/
// 3) https://www.techiedelight.com/find-inorder-predecessor-given-key-bst/
// 4) https://www.geeksforgeeks.org/tree-traversals-inorder-preorder-and-postorder/
// 5) https://www.geeksforgeeks.org/binary-search-tree-set-1-search-and-insertion/
// 6) https://www.geeksforgeeks.org/inorder-predecessor-successor-given-key-bst/