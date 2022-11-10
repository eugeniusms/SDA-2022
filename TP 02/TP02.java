import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.StringTokenizer;
import java.util.LinkedList;

public class TP02 {

    private static InputReader in;
    static PrintWriter out;

    static CircularDoublyLL<Mesin> Permainan = new CircularDoublyLL<Mesin>();

    public static void main(String[] args) {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        // input

        out.close();
    }

    // method

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

class Budi {
    Mesin now;

    Budi(Mesin now) {
        this.now = now;
    }
}

// ================================== LINKEDLIST THINGS ==================================

class Mesin {
    Mesin prev, next;
    AVLTree scoreTree = new AVLTree(); // penyimpan score
    int popularity = 0;
    
    Mesin() {
    }
}

class CircularDoublyLL<E> {
    int size;
    Mesin header, footer; // null node for easier add and remove
    
    // construct empty list
    CircularDoublyLL() {
        this.size = 0;
        this.header = new Mesin();
        this.footer = new Mesin();
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
            mesin.prev = footer.prev; // mesin thingy first
            mesin.prev.next = mesin;      
        } else { // is exist
            footer.prev.next = mesin;
            mesin.prev = footer.prev;
        }
        mesin.next = footer;
        footer.prev = mesin;

        this.size += 1;
    }

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
}

// ====================================== AVL THINGS ====================================

class Node { // AVL Node
    int key, height, count, sum; // key => score
    Node left, right;

    Node(int key) {
        this.key = key;
        this.height = 1;
        this.count = 1;
        this.sum = key;
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
}

// References:

// Data Structure:
// 1) https://www.w3schools.com/java/java_linkedlist.asp

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