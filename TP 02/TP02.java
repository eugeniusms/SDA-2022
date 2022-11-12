import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.StringTokenizer;

public class TP02 {

    private static InputReader in;
    static PrintWriter out;

    static CircularDoublyLL<Mesin> daftarMesin = new CircularDoublyLL<Mesin>(); // include budi di dalamnya
    static AVLTree tree = new AVLTree();

    public static void main(String[] args) {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        // input

        // TESTCircularDoublyLL();
        // TESTAVLTree();

        // INISIALISASI INPUT
        int N = in.nextInt(); // banyak mesin
        Mesin terpopuler = null;
        int maxi = -999999;
        for(int i = 1; i <= N; i++) {
            int M = in.nextInt(); // banyak score
            AVLTree scoreTree = new AVLTree();
            for(int s = 1; s <= M; s++) {
                int S = in.nextInt(); // daftar score
                scoreTree.root = scoreTree.insert(scoreTree.root, S);
            }
            Mesin mesin = new Mesin(i, scoreTree);
            daftarMesin.addLast(mesin);
            if (M > maxi) {
                maxi = M;
                terpopuler = mesin;
            }
        }

        // INISIALISASI BUDI
        daftarMesin.setBudiNow(terpopuler);
        // out.println(daftarMesin.budiNow.id); // TEST

        // QUERY
        int Q = in.nextInt();
        for(int i = 1; i <= Q; i++) {
            String query = in.next();
            if (query.equals("MAIN")) {
                MAIN();
            } else if (query.equals("GERAK")) {
                GERAK();
            }
        }

        out.close();
    }

    // method

    static void MAIN() { // TODO: BELUM INCLUDE STACK/QUEUE DI DALAMNYA
        int insertedKey = in.nextInt();
        // insert score to tree
        AVLTree budiTree = daftarMesin.budiNow.scoreTree;
        budiTree.root = budiTree.insert(budiTree.root, insertedKey);
        // get result
        int sumOfCount = budiTree.root.count;
        int sumOfBefore = budiTree.countBefore(budiTree.root, insertedKey);
        out.println(sumOfCount - sumOfBefore);
    }

    static void GERAK() {
        String arah = in.next();
        if (arah.equals("KIRI")) {
            out.println(daftarMesin.gerakKiri().id);
        } else {
            out.println(daftarMesin.gerakKanan().id);
        }
    }

    static void TESTCircularDoublyLL() {
        // add last 1
        Mesin mesinBaru = new Mesin(1, tree);
        daftarMesin.addLast(mesinBaru);
        daftarMesin.print();
        // add last 2
        mesinBaru = new Mesin(2, tree);
        daftarMesin.addLast(mesinBaru);
        daftarMesin.print();
        // remove last 1
        daftarMesin.removeLast();
        daftarMesin.print();
        // remove last 2
        daftarMesin.removeLast();
        daftarMesin.print();
        // remove last 3 => error
        // daftarMesin.removeLast();
        // daftarMesin.print();

        // add first 1
        mesinBaru = new Mesin(3, tree);
        daftarMesin.addFirst(mesinBaru);
        daftarMesin.print();
        // add first 2
        mesinBaru = new Mesin(4, tree);
        daftarMesin.addFirst(mesinBaru);
        daftarMesin.print();
        // remove first 1
        daftarMesin.removeFirst();
        daftarMesin.print();
        // remove first 2
        daftarMesin.removeFirst();
        daftarMesin.print();
        // remove first 3 => error
        // daftarMesin.removeFirst();
        // daftarMesin.print();

        // add first 1
        Mesin mesinBaru5 = new Mesin(5, tree);
        daftarMesin.addFirst(mesinBaru5);
        daftarMesin.print();
        // add last 1
        Mesin mesinBaru6 = new Mesin(6, tree);
        daftarMesin.addLast(mesinBaru6);
        daftarMesin.print();
        // add first 2
        Mesin mesinBaru7 = new Mesin(7, tree);
        daftarMesin.addFirst(mesinBaru7);
        daftarMesin.print();
        // add last 2
        Mesin mesinBaru8 = new Mesin(8, tree);
        daftarMesin.addLast(mesinBaru8);
        daftarMesin.print();

        // swap 5 <=> 7
        daftarMesin.swap(mesinBaru5, mesinBaru7);
        daftarMesin.print();
        // swap 5 <=> 8
        daftarMesin.swap(mesinBaru5, mesinBaru8);
        daftarMesin.print();   

        // remove 7
        daftarMesin.remove(mesinBaru7);
        daftarMesin.print();
        // add first 7
        daftarMesin.addFirst(mesinBaru7);
        daftarMesin.print();
        // pindah mesin 6 ke pojok kanan, budi ke 5
        Mesin ditempatiBudi = daftarMesin.pindahMesin(mesinBaru6);
        daftarMesin.print();
        System.out.println("Mesin Ditempati Budi: "+ditempatiBudi.id + "\n");
        // mesin 6 tetep di pojok kanan, budi ke depan
        ditempatiBudi = daftarMesin.pindahMesin(mesinBaru6);
        daftarMesin.print();
        System.out.println("Mesin Ditempati Budi: "+ditempatiBudi.id + "\n");
    }

    static void TESTAVLTree() {
        // balance tree
        tree.root = tree.insert(tree.root, 8);
        tree.root = tree.insert(tree.root, 4);
        tree.root = tree.insert(tree.root, 12);
        tree.root = tree.insert(tree.root, 2);
        tree.root = tree.insert(tree.root, 6);
        tree.root = tree.insert(tree.root, 10);
        tree.root = tree.insert(tree.root, 14);
        tree.root = tree.insert(tree.root, 1);
        tree.root = tree.insert(tree.root, 3);
        tree.root = tree.insert(tree.root, 5);
        tree.root = tree.insert(tree.root, 7);
        tree.root = tree.insert(tree.root, 9);
        tree.root = tree.insert(tree.root, 11);
        tree.root = tree.insert(tree.root, 13);
        tree.root = tree.insert(tree.root, 15);
        tree.printInOrder();
        tree.printPreOrder();
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
// IDE:
// LinkedList<Mesin> (LinkedList)
// Mesin.root (AVLTree)

// ================================== LINKEDLIST THINGS ==================================

class Mesin {
    Mesin prev, next;
    AVLTree scoreTree; // penyimpan score
    int popularity = 0;
    int id;
    
    Mesin(int id, AVLTree scoreTree) {
        this.id = id;
        this.scoreTree = scoreTree;
    }
}

class CircularDoublyLL<E> {
    int size;
    Mesin header, footer; // null node for easier add and remove
    Mesin budiNow; // untuk menyimpan lokasi budi
    
    // construct empty list
    CircularDoublyLL() {
        this.size = 0;
        this.header = new Mesin(0, null);
        this.footer = new Mesin(0, null);
    }

    // sepertinya done (belum dicek)
    void addFirst(Mesin mesin) {
        if (this.size == 0) { // empty
            footer.prev = mesin;
            mesin.next = footer;
        } else { // is exist
            mesin.next = header.next;
            mesin.next.prev = mesin;
        }
        header.next = mesin;
        mesin.prev = header;

        this.size += 1;
    }

    // sepertinya done (belum dicek)
    void removeFirst() {
        if (this.size == 0) { // empty
            // do nothing
            throw new NullPointerException("LinkedList Size is 0");
        } else if (this.size == 1) { // tidak ada elemen kedua
            header.next = footer;
            footer.prev = header;
        } else { // saat ada lebih dari 1 node
            header.next = header.next.next;
            header.next.prev = header;
        }

        this.size -= 1;
    }

    // sepertinya done (belum dicek)
    void addLast(Mesin mesin) {
        if (this.size == 0) { // empty
            footer.prev = mesin;
            mesin.next = footer;
            header.next = mesin;
            mesin.prev = header;

        } else { // is exist
            footer.prev.next = mesin;
            mesin.prev = footer.prev;
            mesin.next = footer;
            footer.prev = mesin;
        }

        this.size += 1;
    }

    // sepertinya done (belum dicek)
    void removeLast() {
        if (this.size == 0) { // empty
            // do nothing
            throw new NullPointerException("LinkedList Size is 0");
        } else if (this.size == 1) { // tidak ada elemen kedua
            header.next = footer;
            footer.prev = header;
        } else { // saat ada lebih dari 1 node
            footer.prev.prev.next = footer;
            footer.prev = footer.prev.prev;
        }

        this.size -= 1;
    }

    // done
    Mesin remove(Mesin mesin) {
        if (this.size == 0) { // empty
            // do nothing
            throw new NullPointerException("LinkedList Size is 0");
        } else if (this.size == 1) { // tidak ada elemen kedua
            header.next = footer;
            footer.prev = header;
        } else { // saat ada lebih dari 1 node
            mesin.prev.next = mesin.next;
            mesin.next.prev = mesin.prev;
        }

        this.size -= 1;
        return mesin;
    }

    void setBudiNow(Mesin mesin) {
        budiNow = mesin;
    }

    // get kanan mesin untuk ditempati budi
    Mesin gerakKanan() {
        if (this.size == 0) { // empty
            // do nothing
            throw new NullPointerException("LinkedList Size is 0");
        } else if (this.size == 1) { // cuma satu elemen
            // do nothing
        } else if (budiNow.next == footer) { // elemen terakhir
            budiNow = header.next;
        } else {
            budiNow = budiNow.next;
        }
        return budiNow;
    }

    // get kiri mesin untuk ditempati budi
    Mesin gerakKiri() {
        if (this.size == 0) {
            // do nothing
            throw new NullPointerException("LinkedList Size is 0");
        } else if (this.size == 1) {
            // do nothing
        } else if (budiNow.prev == header) { // elemen pertama
            budiNow = footer.prev;
        } else {
            budiNow = budiNow.prev;
        }
        return budiNow;
    }

    // pindah mesin sekaligus mereturn mesin untuk ditempati budi
    Mesin pindahMesin(Mesin mesin) {
        if (this.size == 0) {
            // do nothing
            throw new NullPointerException("LinkedList Size is 0");
        } else if (this.size == 1) { // cuma satu mesin permainan
            return mesin;
        } else if (mesin.next == footer) { // paling kanan
            return header.next;
        } else { // sisanya
            // pindah mesin ke pojok kanan
            Mesin tempatBaruBudi = gerakKanan();
            Mesin mesinDipindah = remove(mesin);
            this.addLast(mesinDipindah);
            return tempatBaruBudi;
        }
    }

    // swap [1]<->[2] = [2]<->[1]
    void swap(Mesin satu, Mesin dua) {
        // temp variable
        Mesin satuPrev = satu.prev;
        Mesin satuNext = satu.next;
        Mesin duaPrev = dua.prev;
        Mesin duaNext = dua.next;
       
        if (satu.equals(dua)) {
            // do nothing
        } else if (satuNext.equals(dua)) { // saat bersebelahan [1]<->[2]
            satuPrev.next = dua;
            dua.prev = satuPrev;
            duaNext.prev = satu;
            satu.next = duaNext;
            satu.prev = dua;
            dua.next = satu;
            
        } else if (duaNext.equals(satu)) { // kebalikan dari case atas [2]<->[1]
            duaPrev.next = satu;
            satu.prev = duaPrev;
            satuNext.prev = dua;
            dua.next = satuNext;
            dua.prev = satu;
            satu.next = dua;

        } else {
            dua.prev = satuPrev;
            dua.next = satuNext;
            satuPrev.next = dua;
            satuNext.prev = dua;

            satu.prev = duaPrev;
            satu.next = duaNext;
            duaPrev.next = satu;
            duaNext.prev = satu;
        }
    }

    // TEST
    void print() {
        System.out.println("LinkedList Size is " + this.size);
        if (this.size == 0) {
            System.out.println("List: Kosong :D");
        } else {
            // dari depan
            Mesin mesin = header.next;
            System.out.print("List: header->");
            while (mesin != footer) {
                System.out.print("[" + mesin.id + "]->");
                mesin = mesin.next;
            }
            System.out.print("footer\n");
            
            // dari belakang
            mesin = footer.prev;
            System.out.print("List: footer->");
            while (mesin != header) {
                System.out.print("[" + mesin.id + "]->");
                mesin = mesin.prev;
            }
            System.out.print("header\n");
        }
        System.out.println();
    }
}

// ====================================== AVL THINGS ====================================

class Node { // AVL Node
    int key, height, count, sum; // key => score
    Node left, right;
    int jumlahSama; // jumlah isi key yg sama // URUSAN TERKAIT DELETE & INSERT PADA AVLTREE

    Node(int key) {
        this.key = key;
        this.height = 1;
        this.count = 1;
        this.sum = key; 
        this.jumlahSama = 1;
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
        y.count = getCount(y.left) + getCount(y.right) + 1;
        x.height = max(getHeight(x.left), getHeight(x.right)) + 1; 
        x.count = getCount(x.left) + getCount(x.right) + 1;

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
        y.count = getCount(y.left) + getCount(y.right) + 1;
        x.height = max(getHeight(x.left), getHeight(x.right)) + 1; 
        x.count = getCount(x.left) + getCount(x.right) + 1;
  
        // Return new root 
        return x; 
    }

    // Implement insert node to AVL Tree
    Node insert(Node node, int key) {
        if (node == null) {
            return (new Node(key));
        }

        if (key < node.key) {
            node.left = insert(node.left, key);
        } else if (key > node.key) {
            node.right = insert(node.right, key);
        } else {
            // no duplication
            node.jumlahSama += 1;
            return node;
        }

        // Update height
        node.height = 1 + max(getHeight(node.left), getHeight(node.right));
        node.count = 1 + getCount(node.left) + getCount(node.right);

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
    Node delete(Node root, int key) {
        // Find the node to be deleted and remove it
        if (root == null)
        return root;
        if (key < root.key)
        root.left = delete(root.left, key);
        else if (key > root.key)
        root.right = delete(root.right, key);
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
            root.right = delete(root.right, temp.key);
        }
        }
        if (root == null)
        return root;

        // Update the balance factor of each node and balance the tree
        root.height = max(getHeight(root.left), getHeight(root.right)) + 1;
        root.count = getCount(root.left) + getCount(root.right) + 1;

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
    int getCount(Node node) {
        if (node == null) {
            return 0;
        }
        return node.count;
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
        root.count+=1;
        if (root.key > key) { 
            return searchNodePush(root.left, key);
        }
        return searchNodePush(root.right, key);
    }

    Node searchNodePop(Node root, int key) {
        if (root == null || root.key == key) {
            return root;
        }
        root.count-=1;
        if (root.key > key) { 
            return searchNodePop(root.left, key);
        }
        return searchNodePop(root.right, key);
    }

    // in order traversal
    void inOrder(Node node) {
        if (node == null)
            return;
 
        /* first recur on left child */
        inOrder(node.left);
        /* then print the data of node */
        System.out.print(node.key +"["+ node.count + "]---");
        /* now recur on right child */
        inOrder(node.right);
    }
 
    // Wrappers over above recursive functions
    void printInOrder() { 
        System.out.println("IN-ORDER TREE: ");
        inOrder(root); 
        System.out.println();
    }

    // pre order traversal
    void preOrder(Node node) {
        if (node == null)
            return;

        System.out.print(node.key +"["+ node.count + "]---");
        preOrder(node.left);
        preOrder(node.right);
    }
 
    // Wrappers over above recursive functions
    void printPreOrder() { 
        System.out.println("PRE-ORDER TREE: ");
        preOrder(root); 
        System.out.println();
    }

    // QUERY MAIN, LIHAT
    int countBefore(Node node, int insertedKey) {
        if (node.key == insertedKey) {
            // cek kiri
            if (node.left != null) {
                return node.left.count;
            } else {
                return 0;
            }
        }
        if (node.key < insertedKey) {
            // cek kiri lalu, ke kanan
            if (node.left != null) {
                return 1 + node.left.count + countBefore(node.right, insertedKey);
            } else {
                return 1 + countBefore(node.right, insertedKey);
            }
        }
        // ke kiri
        return countBefore(node.left, insertedKey);
    }
}

// References:

// Data Structure:
// 1) https://www.w3schools.com/java/java_linkedlist.asp
// 2) https://www.geeksforgeeks.org/tree-traversals-inorder-preorder-and-postorder/

// HAPUS (JANGAN LUPA DIBALANCING SETELAH DIHAPUS), 
// *OPSI 0* USE node.sum FOR DELETING SEKALIGUS (MUST LEARN REBALANCING SUBTREE TO TREE) (BETTER)
// *OPSI 1* FIND GREATEST K-TH NODE THEN DELETE NODE WITH FUNCTION DELETENODE (NO REBALANCING)
// *OPSI 2* FIND MAX -> DELETE -> FIND MAX -> DELETE ... N TIMES (LIKED)
// *OPSI 3* FIND MAX -> PREDECESSOR -> DELETE -> PREDECESSOR ... N TIMES (OVERSEARCH)
// 1) https://www.geeksforgeeks.org/kth-largest-element-bst-using-constant-extra-space/
// 2) https://www.geeksforgeeks.org/deletion-in-an-avl-tree/
// 3) https://favtutor.com/blogs/avl-tree-python#:~:text=Insertion%20and%20Deletion%20time%20complexity,tree%20and%20red%2Dblack%20tree.

// EVALUASI
// 1) https://www.geeksforgeeks.org/quick-sort/
// 2) https://visualgo.net/en/sorting