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

    // Menyimpan daftar mesin di dalam Circular Doubly Linkedlist
    static CircularDoublyLL<Mesin> daftarMesin = new CircularDoublyLL<Mesin>(); 

    // Method main digunakan untuk mengambil input dan memprosesnya ke dalam struktur data
    public static void main(String[] args) {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        // INISIALISASI INPUT
        int N = in.nextInt(); // banyak mesin
        for(int i = 1; i <= N; i++) {
            int M = in.nextInt(); // banyak score
            // AVL Tree digunakan untuk menyimpan score suatu mesin
            AVLTree scoreTree = new AVLTree();
            // Jumlah score diinisiasikan dengan 0 untuk pertama kali
            long sumScore = 0;
            for(int s = 1; s <= M; s++) {
                int S = in.nextInt(); // daftar score
                // Menambahkan score ke dalam AVL Tree
                scoreTree.root = scoreTree.insert(scoreTree.root, S);
                sumScore += S;
            }
            // Menambahkan mesin ke dalam Circular Doubly Linkedlist
            Mesin mesin = new Mesin(i, scoreTree, M, sumScore);
            daftarMesin.addLast(mesin);
        }

        // INISISASI BUDI KE DALAM MESIN PALING KANAN
        daftarMesin.setBudiNow(daftarMesin.header.next);        

        // MENGAMBIL QUERY
        int Q = in.nextInt();
        for(int i = 1; i <= Q; i++) {
            String query = in.next();
            if (query.equals("MAIN")) {
                MAIN();
            } else if (query.equals("GERAK")) {
                GERAK();
            } else if (query.equals("HAPUS")) {
                HAPUS();
            } else if (query.equals("LIHAT")) {
                LIHAT();
            } else if (query.equals("EVALUASI")) {
                EVALUASI();
            }
        }

        out.close();
    }

    // Method MAIN digunakan untuk mendapatkan score yang lebih besar dari insertedKey
    static void MAIN() { 
        // Mengambil insertedKey
        int insertedKey = in.nextInt();
        // Masukkan score ke dalam AVL Tree
        AVLTree budiTree = daftarMesin.budiNow.scoreTree;
        budiTree.root = budiTree.insert(budiTree.root, insertedKey);
        // Ambil banyak score pada root AVL Tree & banyak score sebelum insertedKey
        long sumOfCount = budiTree.root.count;
        long sumOfBefore = budiTree.countBefore(budiTree.root, insertedKey);

        // Dapatkan score lebih besar dari insertedKey
        out.println(sumOfCount - sumOfBefore + 1);
        // Update budiTree popularity juga
        daftarMesin.budiNow.popularity += 1;
        // Insert sumScore in mesin
        daftarMesin.budiNow.sumScore += insertedKey;
    }

    // Method GERAK digunakan untuk menggerakkan budi ke mesin sebelah kiri atau kanan
    static void GERAK() {
        String arah = in.next();
        if (arah.equals("KIRI")) {
            out.println(daftarMesin.gerakKiri().id);
        } else {
            out.println(daftarMesin.gerakKanan().id);
        }
    }

    // Method HAPUS digunakan untuk menghapus score tertentu dari AVL Tree sekaligus mengembalikan jumlahnya
    static void HAPUS() {
        int X = in.nextInt();

        // Jika popularity mesin kurang dari atau sama dengan X maka dicek dulu
        if(daftarMesin.budiNow.popularity <= X) {
            // Jika popularity mesin kurang dari 0 maka tidak ada score yang dihapus
            if (daftarMesin.budiNow.popularity <= 0) {
                out.println("0");
                daftarMesin.budiNow.popularity = 0; // Set ke 0
                daftarMesin.budiNow.sumScore = 0; // Set ke 0
                daftarMesin.pindahMesin(daftarMesin.budiNow); // Lakukan pindah mesin  
            } else {
                // Jika popularity mesin lebih dari 0
                out.println(daftarMesin.budiNow.sumScore); // print sumScore dari suatu mesin
                daftarMesin.budiNow.scoreTree = new AVLTree();; // Set reset to new AVLTree
                daftarMesin.budiNow.popularity = 0; // Set ke 0
                daftarMesin.budiNow.sumScore = 0; // Set ke 0
                // Budi pindah dulu baru mesin dipindah
                daftarMesin.pindahMesin(daftarMesin.budiNow);                
            }
        } else {
            // Update budiNow popularity
            daftarMesin.budiNow.popularity -= X;

            // Hitung jumlah node key yang akan dihapus
            long sum = 0;
            while(X > 0) {
                // Mencari nilai max dari AVL Tree
                Node maxi = daftarMesin.budiNow.scoreTree.findMax();
                // Menjumlahkan nilai keynya
                sum += maxi.key;
                // Menghapus node dari AVL Tree
                daftarMesin.budiNow.scoreTree.root = daftarMesin.budiNow.scoreTree.delete(daftarMesin.budiNow.scoreTree.root, maxi.key); // delete node
                X--;
            }
            // Update sumScore
            daftarMesin.budiNow.sumScore -= sum;
            // Print sum
            out.println(sum);
        }      
    }

    // Method LIHAT digunakan untuk melihat score tertentu dari AVL Tree dari L ke H
    static void LIHAT() {
        int lowkey = in.nextInt();
        int highkey = in.nextInt();
        AVLTree budiTree = daftarMesin.budiNow.scoreTree; // Ambil AVL Tree mesin saat ini

        // Gunakan prefix-sum untuk mencari banyak score yang ada diantara lowkey dan highkey
        long sumOfBeforeL = budiTree.countBefore(budiTree.root, lowkey-1); // hitung banyak score sebelum lowkey-1
        long sumOfBeforeH = budiTree.countBefore(budiTree.root, highkey); // hitung banyak score sebelum highkey
        // prefix-sum
        out.println(sumOfBeforeH - sumOfBeforeL);
    }

    // Method EVALUASI digunakan untuk sorting semua mesin saat ini agar terurut sesuai dengan popularity
    static void EVALUASI() {
        Mesin[] arr = daftarMesin.sort(); // Ambil sorted data
        // Insert new data sorted to daftarMesin after reset the list
        daftarMesin.clear();
        for(int i = 0; i < arr.length; i++) {
            daftarMesin.addLast(arr[i]);
        }
        // Print urutan mesin budi saat ini
        out.println(daftarMesin.getBudiMesinSortedNow());
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

// ================================== LINKEDLIST THINGS ==================================

// Class Mesin digunakan untuk instance mesin
class Mesin {
    Mesin prev, next;
    AVLTree scoreTree; // AVL Tree penyimpan score
    int popularity; // popularity dari suatu mesin
    int id;
    long sumScore; // Jumlah score dari AVL Tree
    
    Mesin(int id, AVLTree scoreTree, int popularity, long sumScore) {
        this.id = id;
        this.scoreTree = scoreTree;
        this.popularity = popularity;
        this.sumScore = sumScore;
    }
}

// Class DaftarMesin digunakan untuk menyimpan semua mesin
class CircularDoublyLL<E> {
    int size; // Jumlah mesin
    Mesin header, footer; // null node for easier add and remove
    Mesin budiNow; // untuk menyimpan lokasi budi
    
    // construct empty list
    CircularDoublyLL() {
        this.size = 0;
        this.header = new Mesin(0, null, 0, 0);
        this.footer = new Mesin(0, null, 0, 0);
    }

    // Method digunakan untuk menambahkan node baru di akhir linkedlist
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

    // Method digunakan untuk remove node mesin dari linkedlist
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

    // Method digunakan untuk set mesin depan budi saat ini
    void setBudiNow(Mesin mesin) {
        budiNow = mesin;
    }

    // Method digunakan untuk mengecek letak urutan mesin depan budi saat ini
    int getBudiMesinSortedNow() {
        Mesin check = header.next;
        int counter = 0;
        while (!check.equals(budiNow)) {
            counter++;
            check = check.next;
        }
        // return counter;
        return counter + 1;
    }

    // Menggerakan budi ke kanan
    Mesin gerakKanan() {
        if (this.size == 0) { // empty
            // do nothing
            throw new NullPointerException("LinkedList Size is 0");
        } else if (this.size == 1) { // cuma satu elemen
            // do nothing
        } else if (budiNow.next.equals(footer)) { // elemen terakhir
            budiNow = header.next;
        } else { // kasus normal
            budiNow = budiNow.next;
        }
        return budiNow;
    }

    // Menggerakan budi ke kiri
    Mesin gerakKiri() {
        if (this.size == 0) { // empty
            // do nothing
            throw new NullPointerException("LinkedList Size is 0");
        } else if (this.size == 1) { // cuma satu elemen
            // do nothing
        } else if (budiNow.prev.equals(header)) { // elemen pertama
            budiNow = footer.prev;
        } else { // kasus normal
            budiNow = budiNow.prev;
        }
        return budiNow;
    }

    // Pindah mesin sekaligus mereturn mesin untuk ditempati budi
    void pindahMesin(Mesin mesin) {
        if (this.size == 0) {
            // do nothing
        } else if (this.size == 1) { // cuma satu mesin permainan
            // do nothing
        } else if (mesin.next.equals(footer)) { // mesin berada paling kanan
            // mesin stay
            // budi pindah ke depan
            budiNow = header.next;
        } else { // sisanya
            // budi pindah ke kanannya
            budiNow = mesin.next;
            // pindah mesin ke pojok kanan
            Mesin mesinDipindah = remove(mesin);
            this.addLast(mesinDipindah);
        }
    }

    // Clear all mesin
    void clear() {
        header.next = footer;
        footer.prev = header;
        this.size = 0;
    }

    // Merges two subarrays of arr[].
    // First subarray is arr[l..m]
    // Second subarray is arr[m+1..r]
    void merge(Mesin arr[], int l, int m, int r)
    {
        // Find sizes of two subarrays to be merged
        int n1 = m - l + 1;
        int n2 = r - m;
 
        /* Create temp arrays */
        Mesin L[] = new Mesin[n1];
        Mesin R[] = new Mesin[n2];
 
        /*Copy data to temp arrays*/
        for (int i = 0; i < n1; ++i)
            L[i] = arr[l + i];
        for (int j = 0; j < n2; ++j)
            R[j] = arr[m + 1 + j];
 
        /* Merge the temp arrays */
 
        // Initial indexes of first and second subarrays
        int i = 0, j = 0;
 
        // Initial index of merged subarray array
        int k = l;
        while (i < n1 && j < n2) {
            
            if (L[i].popularity > R[j].popularity) {
                arr[k] = L[i];
                i++;
            } else if (L[i].popularity == R[j].popularity) {
                // dicek lagi identitynya (yg rendah di depan)
                if (L[i].id < R[j].id) {
                    arr[k] = L[i];
                    i++;
                } else {
                    arr[k] = R[j];
                    j++;
                }
            }

            else {
                arr[k] = R[j];
                j++;
            }

            k++;
        }
 
        /* Copy remaining elements of L[] if any */
        while (i < n1) {
            arr[k] = L[i];
            i++;
            k++;
        }
 
        /* Copy remaining elements of R[] if any */
        while (j < n2) {
            arr[k] = R[j];
            j++;
            k++;
        }
    }

    void mergesort(Mesin arr[], int l, int r) {
        // sort array pakai merge sort
        if (l < r) {
            // Find the middle point
            int m = l + (r - l) / 2;
 
            // Sort first and second halves
            mergesort(arr, l, m);
            mergesort(arr, m + 1, r);
 
            // Merge the sorted halves
            merge(arr, l, m, r);
        }
    }

    // Method yang digunakan untuk sort mesin
    Mesin[] sort() {
        // kalo ga 0 sizenya
        // buat array kosong sesuai size of LinkedList
        Mesin[] arr = new Mesin[this.size];
        Mesin masuk = header.next;
        // Masukin semua mesin ke array
        for(int i = 0; i < this.size; i++) {
            arr[i] = masuk;
            masuk = masuk.next;
        }
        // Sort array pakai merge sort
        mergesort(arr, 0, this.size - 1);
        return arr;
    }
}

// ====================================== AVL THINGS ====================================

// AVL Node digunakan untuk menyimpan Score
class Node { // AVL Node
    long key, height, count; // key => score, count => banyaknya node pada suatu subtree dengan root == node
    Node left, right;
    long jumlahSama; // jumlah isi key yg sama (duplicate)

    Node(long key) {
        this.key = key;
        this.height = 1;
        this.count = 1;
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
  
        // Update heights & count
        y.height = max(getHeight(y.left), getHeight(y.right)) + 1; 
        y.count = y.jumlahSama + getCount(y.left) + getCount(y.right);

        x.height = max(getHeight(x.left), getHeight(x.right)) + 1; 
        x.count = x.jumlahSama + getCount(x.left) + getCount(x.right);

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
  
        // Update heights & count
        y.height = max(getHeight(y.left), getHeight(y.right)) + 1; 
        y.count = y.jumlahSama + getCount(y.left) + getCount(y.right);

        x.height = max(getHeight(x.left), getHeight(x.right)) + 1; 
        x.count = x.jumlahSama + getCount(x.left) + getCount(x.right);
  
        // Return new root 
        return x; 
    }

    // Implement insert node to AVL Tree
    Node insert(Node node, long key) {
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
            node.count += 1;
            return node;
        }

        // Update height & count
        node.height = 1 + max(getHeight(node.left), getHeight(node.right));
        node.count = node.jumlahSama + getCount(node.left) + getCount(node.right);

        // Get balance factor
        long balance = getBalance(node);

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
    Node delete(Node root, long key) 
    { 
        // STEP 1: PERFORM STANDARD BST DELETE 
        if (root == null) 
            return root; 
  
        // If the key to be deleted is smaller than 
        // the root's key, then it lies in left subtree 
        if (key < root.key) 
            root.left = delete(root.left, key); 
  
        // If the key to be deleted is greater than the 
        // root's key, then it lies in right subtree 
        else if (key > root.key) 
            root.right = delete(root.right, key); 
  
        // if key is same as root's key, then this is the node 
        // to be deleted 
        else
        { 
            // if jumlah sama masih ada rootnya jangan diilangin dulu gan, duplicatenya urusin
            if (root.jumlahSama > 1) {
                root.jumlahSama -= 1;
                root.count -= 1;
            } else {
                // node with only one child or no child 
                if ((root.left == null) || (root.right == null)) { 
                    root = (root.left == null) ? root.right : root.left;
                } else {
                    // node with two children: Get the inorder 
                    // successor (smallest in the right subtree) 
                    Node temp = lowerBound(root.right); 
    
                    // Copy the inorder successor's data to this node 
                    root.key = temp.key; 
                    // fixing yg keupdate ga cuma key doang, ada count juga
                    root.jumlahSama = temp.jumlahSama;
                    root.count = temp.count;
                    // Delete the inorder successor 
                    root.right = delete(root.right, temp.key); 
                } 
            }
        } 
  
        // If the tree had only one node then return 
        if (root == null) 
            return root; 
  
        // STEP 2: UPDATE HEIGHT OF THE CURRENT NODE 
        root.height = max(getHeight(root.left), getHeight(root.right)) + 1; 
        root.count = root.jumlahSama + getCount(root.left) + getCount(root.right);
  
        // STEP 3: GET THE BALANCE FACTOR OF THIS NODE (to check whether 
        // this node became unbalanced) 
        long balance = getBalance(root); 
  
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

    // Mencari lowerBound dari suatu node
    Node lowerBound(Node node) {
        // Return node with the lowest from this node
        Node current = node;
        while (current.left != null) {
            current = current.left;
        }
        return current;
    }

    // Mencari upperBound dari suatu node
    Node upperBound(Node node) {
        // Return node with the greatest from this node
        Node current = node;
        while (current.right != null) {
            current = current.right;
        }
        return current;
    }


    // Utility function to get height of node
    long getHeight(Node node) {
        if (node == null) {
            return 0;
        }
        return node.height;
    }

    // Utility function to get num of peoples
    long getCount(Node node) {
        if (node == null) {
            return 0;
        }
        return node.count;
    }

    // Utility function to get balance factor of node
    long getBalance(Node node) {
        if (node == null) {
            return 0;
        }
        return getHeight(node.left) - getHeight(node.right);
    }
    
    // Utility function to get max of 2 longs
    long max(long a, long b) {
        return (a > b) ? a : b;
    }   

    // QUERY MAIN, LIHAT
    // Method digunakan untuk mencari jumlah score yang kurang dari inserted key
    long countBefore(Node node, long insertedKey) {
        if (node == null) { // Jika node kosong, return 0
            return 0;
        }
        // Jika sudah didapat insertedKey sama dengan key node, maka cari count di subtree kiri dulu
        if (node.key == insertedKey) { 
            return node.jumlahSama + getCount(node.left);
        }
        // Jika insertedKey lebih kecil dari key node, maka cari di subtree kiri
        if (node.key < insertedKey) {
            // Cek kiri lalu, ke kanan
            if (node.left != null) {
                // Jika ada node di subtree kiri, maka cari count di subtree kiri + duplicatenya
                return node.jumlahSama + node.left.count + countBefore(node.right, insertedKey);
            } else {
                return node.jumlahSama + countBefore(node.right, insertedKey);
            }
        }
        // Ke kiri untuk cari key yang cocok
        return countBefore(node.left, insertedKey);
    }

    // Method digunakan untuk mencari score max
    Node findMax() {
        Node temp = root;
        while (temp.right != null) {
            temp = temp.right;
        }
        return temp;
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
// 3) https://www.geeksforgeeks.org/quicksort-on-singly-linked-list/
// 4) https://www.geeksforgeeks.org/insertion-sort/