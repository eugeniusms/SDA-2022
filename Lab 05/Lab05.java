import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.StringTokenizer;
import java.util.TreeSet;
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

    // Menyimpan semua nilai power level yang unique
    static TreeSet<Integer> set = new TreeSet<Integer>();

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
                map.get(powerLevel).push(name); tree.searchNodePush(tree.root, powerLevel).people += 1; // add people after push
            } else {
                Stack<String> stack = new Stack<String>();
                stack.push(name); 
                map.put(powerLevel, stack); 
            }

            // INSERT POWER LEVEL KE SET
            set.add(powerLevel);
        }

        int numOfQueries = in.nextInt();
        for (int i = 0; i < numOfQueries; i++) {
            String cmd = in.next();
            // CHECK TREE
            // out.println("\nCEK "+i+" BEFORE : ");
            // inOrder(tree.root);
            // out.println();
            if (cmd.equals("MASUK")) {
                handleQueryMasuk();
            } else {
                out.println(handleQueryDuo());
            }  
            // out.println("CEK "+i+" AFTER : ");
            // inOrder(tree.root);
            // out.println("\n");
        }

        // out.println("\nCEK 1: "); 
        // tree.root = tree.deleteNode(tree.root, 7); 
        // inOrder(tree.root);

        // out.println("\nCEK 2: "); 
        // tree.root = tree.deleteNode(tree.root, 8); 
        // inOrder(tree.root);

        // out.println("\nCEK 3: "); 
        // tree.root = tree.deleteNode(tree.root, 5); 
        // inOrder(tree.root);

        // out.println("\nCEK 4: "); 
        // tree.root = tree.deleteNode(tree.root, 6); 
        // inOrder(tree.root);

        // out.println("\nCEK 5: ");
        // tree.root = tree.deleteNode(tree.root, 9);
        // inOrder(tree.root);

        out.close();
    }

    static void handleQueryMasuk() {
        String name = in.next();
        int powerLevel = in.nextInt();


        boolean yow = map.containsKey(powerLevel);
        if (!yow) {
            out.println(findTotalBefore(tree.root, powerLevel));
        }

        // INSERT KEY [POWER LEVEL] KE AVL
        tree.root = tree.insertNode(tree.root, powerLevel); 

        // INSERT NAMA KE MAP
        if (map.containsKey(powerLevel)) {
            map.get(powerLevel).push(name); tree.searchNodePush(tree.root, powerLevel).people += 1; // add people after push
        } else {
            Stack<String> stack = new Stack<String>();
            stack.push(name);  
            map.put(powerLevel, stack); 
        }

        // // INSERT POWER LEVEL KE SET
        // set.add(powerLevel);

        // // ITERATE SET
        // for (Integer power : set) {
        //     if (power < powerLevel) {
        //         beforeK += map.get(power).size();
        //     } else {
        //         break;
        //     }
        // }

        if (yow) {
            out.println(findTotalBefore(tree.root, powerLevel));
        }
          
        // PRINT JUMLAH NODE SEBELUM NODE SAAT INI
        // out.println(tree.countNodes(tree.root, powerLevel));
        // inOrderTraversal(tree.root, powerLevel);
        // out.println(beforeK);
       
        // beforeK = 0; // reset
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

    static String handleQueryDuo() {
        // TODO
        int l = in.nextInt();
        int r = in.nextInt();

        // CHECK : Jika L == R maka dicek
        if (l == r) {
            boolean isExist = map.containsKey(l);
            if (isExist) { // jika exist & sizenya >= 2
                if (map.get(l).size() >= 2) {
                    String popFirst = map.get(l).pop(); 
                    String popSecond = map.get(l).pop();
                    tree.searchNodePop(tree.root, l).people -= 2; // add people after pop 2x
                    // jika stack empty maka hapus saja key dari map & node avlnya
                    if (map.get(l).isEmpty()) {
                        map.remove(l);
                        tree.root = tree.deleteNode(tree.root, l);
                        set.remove(l); // hapus dari set
                    }
                    if (popFirst.compareTo(popSecond) < 0) {
                        return popFirst + " " + popSecond;
                    } else {
                        return popSecond + " " + popFirst;
                    }
                } else {
                    return "-1 -1";
                }
            } else {
                return "-1 -1";
            }
        }

        // CHECK: JIKA L & R BERBEDA MAKA LANJUT

        // CARI SUCCESSOR OF L & R
        // Node predecessorL = tree.findPredecessor(tree.root, null, l);
        // Node predecessorR = tree.findPredecessor(tree.root, null, r);
        // Node successorL = tree.findSuccessor(tree.root, null, l);
        // Node successorR = tree.findSuccessor(tree.root, null, r);
        // out.println("Predecessor of " + l + " is " + predecessorL);
        // out.println("Successor of "+l+" is "+successorL);

        // out.println("Predecessor of " + r + " is " + predecessorR);
        // out.println("Successor of "+r+" is "+successorR);

        // CHECK 1: Cek tinggi tree, jika cuma tersisa root maka tidak bisa dihapus
        int height = tree.getHeight(tree.root);
        if (height == 0) {
            // out.println("MASUK A");
            return "-1 -1";
        }

        // Step 1: Cek apakah map memiliki node dengan key l atau r
        boolean isLExist = map.containsKey(l);
        boolean isRExist = map.containsKey(r);

        // Step 2: Dapatkan node yang ingin dihapus, jika exist dalam map maka langsung aja, jika tidak kalau l cari successor, kalau r cari predecessor
        int keyLDihapus = 0, keyRDihapus = 0; 
        Node lDihapus = null, rDihapus = null; 
        // l check
        if (isLExist) {
            keyLDihapus = l;
        } else {
            lDihapus = tree.findSuccessor(tree.root, null, l);
            if (lDihapus == null) { // jika null nodenya maka langsung return -1 -1
                // out.println("MASUK B");
                return "-1 -1";
            }
            keyLDihapus = lDihapus.key;
            if (keyLDihapus > r || keyLDihapus < l) { // jika key successor l > r maka langsung return -1 -1
                // out.println("MASUK C");
                return "-1 -1";
            }
        }
        // r check
        if (isRExist) {
            keyRDihapus = r;
        } else {
            rDihapus = tree.findPredecessor(tree.root, null, r);
            if (rDihapus == null) {
                // out.println("MASUK D");
                return "-1 -1";
            }
            keyRDihapus = rDihapus.key;
            if (keyRDihapus < l || keyRDihapus > r) {
                // out.println("MASUK E");
                return "-1 -1";
            }
        }

        // CEK KEKNYA MASALAHNYA SISA DI SINI
        // CHECK 3: Cek apakah lDihapus dan rDihapus sama, jika ya maka tidak bisa dihapus
        if (isLExist && isRExist) {
            // do nothing if l & r exist
        } else {
            if (lDihapus == rDihapus && map.get(keyLDihapus).size() <= 1) { // SAMA2 NODE BERISI 7
                // out.println("MASUK ");
                return "-1 -1";
            }
        }

        // CHECK : Jika keyLDihapus == keyRDihapus maka dicek
        if (keyLDihapus == keyRDihapus) {
            boolean isExist = map.containsKey(keyLDihapus);
            if (isExist) { // jika exist & sizenya >= 2
                if (map.get(keyLDihapus).size() >= 2) {
                    String popFirst = map.get(keyLDihapus).pop();
                    String popSecond = map.get(keyLDihapus).pop();
                    tree.searchNodePop(tree.root, keyLDihapus).people -= 2; // add people after pop 2x
                    // jika stack empty maka hapus saja key dari map & node avlnya
                    if (map.get(keyLDihapus).isEmpty()) {
                        map.remove(keyLDihapus);
                        tree.root = tree.deleteNode(tree.root, keyLDihapus);
                        set.remove(keyLDihapus); // hapus dari set
                    }
                    if (popFirst.compareTo(popSecond) < 0) {
                        return popFirst + " " + popSecond;
                    } else {
                        return popSecond + " " + popFirst;
                    }
                } else {
                    return "-1 -1";
                }
            } else {
                return "-1 -1";
            }
        }

        // Jika sudah lewat sini berarti pasti ada yg bakal dihapus 

        // Step 3: Dapatkan stack dari key yang ingin dihapus
        Stack<String> stackLDihapus = map.get(keyLDihapus);
        Stack<String> stackRDihapus = map.get(keyRDihapus);

        // Step 4: Dapatkan nama dari stack yang ingin dihapus
        String namaLDihapus = stackLDihapus.pop();  tree.searchNodePop(tree.root, keyLDihapus).people -= 1; // add people after pop 1x
        String namaRDihapus = stackRDihapus.pop(); tree.searchNodePop(tree.root, keyRDihapus).people -= 1; // add people after pop 1x

        // Step 5: Cek apakah stack kosong, jika kosong hapus key dari map
        if (stackLDihapus.isEmpty()) {
            map.remove(keyLDihapus);
            // Step 6: Hapus node AVL karena stack kosong
            tree.root = tree.deleteNode(tree.root, keyLDihapus);
            set.remove(keyLDihapus); // hapus dari set
        }
        if (stackRDihapus.isEmpty()) {
            map.remove(keyRDihapus);
            // Step 6: Hapus node AVL karena stack kosong
            tree.root = tree.deleteNode(tree.root, keyRDihapus);
            set.remove(keyRDihapus); // hapus dari set
        }

        // Step 7: Return nama yang dihapus
        if (namaLDihapus.compareTo(namaRDihapus) > 0) {
            return namaRDihapus + " " + namaLDihapus;
        } else {
            return namaLDihapus + " " + namaRDihapus;
        }
    }

    static void inOrder(Node node) {
        if (node == null)
          return;
        // traverse the left child
        inOrder(node.left);
        // traverse the root node
        out.print(node + "[" + node.people + "] ->");
        // traverse the right child
        inOrder(node.right);
    }

    static int findTotalBefore(Node root, int key) {
        if (root == null) {
            return 0;
        }
        if (root.key == key) {
            if (root.left == null) {
                return 0;
            }
            return root.left.people;
        }
        if (root.key > key) { // ke kiri
            return findTotalBefore(root.left, key);
        } 
        // ke kanan
        if (root.left == null) {
            return 0;
        }
        return root.left.people + map.get(root.key).size() + findTotalBefore(root.right, key);
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
    int key, height, people; // key => sama dengan key map (power level)
    Node left, right;

    Node(int key) {
        this.key = key;
        this.people = 1;
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
    Node rightRotate(Node y) {
        Node x = y.left; 
        Node T2 = x.right; 
  
        // Perform rotation 
        x.right = y; 
        y.left = T2; 
  
        // Update heights 
        y.height = max(getHeight(y.left), getHeight(y.right)) + 1; 
        y.people = getPeople(y.left) + getPeople(y.right) + 1;
        x.height = max(getHeight(x.left), getHeight(x.right)) + 1; 
        x.people = getPeople(x.left) + getPeople(x.right) + 1;

        // Return new root 
        return x; 
    }

    // Implement left rotate
    Node leftRotate(Node y) {
        Node x = y.right; 
        Node T2 = x.left; 
  
        // Perform rotation 
        x.left = y; 
        y.right = T2;   
  
        // Update heights 
        y.height = max(getHeight(y.left), getHeight(y.right)) + 1; 
        // y.people = getPeople(y.left) + getPeople(y.right) + 1;
        x.height = max(getHeight(x.left), getHeight(x.right)) + 1; 
        // x.people = getPeople(x.left) + getPeople(x.right) + 1;
  
        // Return new root 
        return x; 
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
        node.height = 1 + max(getHeight(node.left), getHeight(node.right));
        node.people = 1 + getPeople(node.left) + getPeople(node.right);

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

    // Delete a node
    Node deleteNode(Node root, int key) {
        // Find the node to be deleted and remove it
        if (root == null)
        return root;
        if (key < root.key)
        root.left = deleteNode(root.left, key);
        else if (key > root.key)
        root.right = deleteNode(root.right, key);
        else {
        if ((root.left == null) || (root.right == null)) {
            Node temp = null;
            if (temp == root.left)
            temp = root.right;
            else
            temp = root.left;
            if (temp == null) {
            temp = root;
            root = null;
            } else
            root = temp;
        } else {
            Node temp = lowerBound(root.right);
            root.key = temp.key;
            root.right = deleteNode(root.right, temp.key);
        }
        }
        if (root == null)
        return root;

        // Update the balance factor of each node and balance the tree
        root.height = max(getHeight(root.left), getHeight(root.right)) + 1;
        root.people = getPeople(root.left) + getPeople(root.right) + 1;

        int balanceFactor = getBalance(root);
        if (balanceFactor > 1) {
        if (getBalance(root.left) >= 0) {
            return rightRotate(root);
        } else {
            root.left = leftRotate(root.left);
            return rightRotate(root);
        }
        }
        if (balanceFactor < -1) {
        if (getBalance(root.right) <= 0) {
            return leftRotate(root);
        } else {
            root.right = rightRotate(root.right);
            return leftRotate(root);
        }
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

    // Utility function to get num of peoples
    int getPeople(Node node) {
        if (node == null) {
            return 0;
        }
        return node.people;
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
    
    int max(int a, int b) {
        return (a > b) ? a : b;
    }

    Node searchNodePush(Node root, int key) {
        if (root == null || root.key == key) {
            return root;
        }
        root.people+=1;
        if (root.key > key) { 
            return searchNodePush(root.left, key);
        }
        return searchNodePush(root.right, key);
    }

    Node searchNodePop(Node root, int key) {
        if (root == null || root.key == key) {
            return root;
        }
        root.people-=1;
        if (root.key > key) { 
            return searchNodePop(root.left, key);
        }
        return searchNodePop(root.right, key);
    }
}

// References:
// 1) https://www.geeksforgeeks.org/deletion-in-an-avl-tree
// 2) https://www.geeksforgeeks.org/stack-class-in-java/
// 3) https://docs.oracle.com/javase/8/docs/api/java/util/HashMap.html
// 4) https://docs.oracle.com/javase/7/docs/api/java/util/TreeSet.html

// AVL Methods:
// 1) https://www.youtube.com/watch?v=psFKTGahpCs
// 2) https://www.youtube.com/watch?v=JdmAYw5h3G8
// 3 https://www.techiedelight.com/find-inorder-successor-given-key-bst/)
// 4) https://www.techiedelight.com/find-inorder-predecessor-given-key-bst/
// 5) https://www.geeksforgeeks.org/deletion-in-binary-search-tree/
// 6) https://www.geeksforgeeks.org/deletion-in-an-avl-tree/