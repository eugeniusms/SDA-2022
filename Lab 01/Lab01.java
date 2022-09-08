import java.io.*;
import java.util.StringTokenizer;

public class Lab01 {
    private static InputReader in;
    private static PrintWriter out;

    static int getTotalDeletedLetters(int N, char[] x) {
        // TODO: implement method getTotalDeletedLetter(int, char[]) to get the answer
        int jumlahHurufS, jumlahHurufO, jumlahHurufF, jumlahHurufI, jumlahHurufT, jumlahHurufA;
        jumlahHurufS = jumlahHurufO = jumlahHurufF = jumlahHurufI = jumlahHurufT = jumlahHurufA = 0;
        // Langkah 1 : Lakukan pendataan jumlah huruf masing-masing S,O,F,I,T,A dalam x[]
        for (int i = 0; i < x.length; i++) {
            if (x[i] == 'S') {
                jumlahHurufS += 1;
            } else if (x[i] == 'O') {
                jumlahHurufO += 1;
            } else if (x[i] == 'F') {
                jumlahHurufF += 1;
            } else if (x[i] == 'I') {
                jumlahHurufI += 1;
            } else if (x[i] == 'T') {
                jumlahHurufT += 1;
            } else if (x[i] == 'A') {
                jumlahHurufA += 1;
            }
        }
        // Optimasi : Saat huruf tak lengkap maka langsung kembalikan length dari x
        if (jumlahHurufS == 0 | jumlahHurufO == 0 | jumlahHurufF == 0 | jumlahHurufI == 0 | jumlahHurufT == 0 | jumlahHurufA == 0) {
            return x.length;
        }

        // Langkah 2 : Lakukan pendataan index dari setiap huruf S,O,F,I,T,A
        // Inisiasi array yang digunakan untuk pendataan
        int[] indexHurufS = new int[jumlahHurufS];
        int[] indexHurufO = new int[jumlahHurufO];
        int[] indexHurufF = new int[jumlahHurufF];
        int[] indexHurufI = new int[jumlahHurufI];
        int[] indexHurufT = new int[jumlahHurufT];
        int[] indexHurufA = new int[jumlahHurufA];
        // Pointer pengisi data
        int pointerS, pointerO, pointerF, pointerI, pointerT, pointerA;
        pointerS = pointerO = pointerF = pointerI = pointerT = pointerA = 0;
        // Data semua index dari setiap huruf
        for (int i = 0; i < x.length; i++) {
            if (x[i] == 'S') {
                // Index disimpan ke daftar index huruf
                indexHurufS[pointerS] = i;
                // Pointer ditambahkan
                pointerS++;
            } else if (x[i] == 'O') {
                indexHurufO[pointerO] = i;
                pointerO++;
            } else if (x[i] == 'F') {
                indexHurufF[pointerF] = i;
                pointerF++;
            } else if (x[i] == 'I') {
                indexHurufI[pointerI] = i;
                pointerI++;
            } else if (x[i] == 'T') {
                indexHurufT[pointerT] = i;
                pointerT++;
            } else if (x[i] == 'A') {
                indexHurufA[pointerA] = i;
                pointerA++;
            }
        }

        // Test : Index huruf dalam array sesuai dengan yang sesungguhnya
        out.println("Huruf S");
        for (int ind : indexHurufS) {
            out.println(ind);
        }
        out.println("Huruf O");
        for (int ind : indexHurufO) {
            out.println(ind);
        }
        out.println("Huruf F");
        for (int ind : indexHurufF) {
            out.println(ind);
        }
        out.println("Huruf I");
        for (int ind : indexHurufI) {
            out.println(ind);
        }
        out.println("Huruf T");
        for (int ind : indexHurufT) {
            out.println(ind);
        }
        out.println("Huruf A");
        for (int ind : indexHurufA) {
            out.println(ind);
        }

        // Langkah 3 : Set pointer tiap huruf ke 0 (index pertama array lagi) - persiapan kalkulasi maksimal substring
        pointerS = pointerO = pointerF = pointerI = pointerT = pointerA = 0;

        // Langkah 4 : Memulai kalkulasi pointer sampai tidak bisa didapati substring
        // jumlahHuruf >> Kondisi index lokasi stop iterasi (pointer == jumlahHuruf (artinya sudah null datanya))
        while (pointerS < jumlahHurufS 
            && pointerO < jumlahHurufO
            && pointerF < jumlahHurufF
            && pointerI < jumlahHurufI
            && pointerT < jumlahHurufT
            && pointerA < jumlahHurufA) {
                // Sudah pasti urutannya harus S < O < F < I < T < A
                if (indexHurufO[pointerO] > indexHurufS[pointerS]) {
                    // Jika benar maka cek huruf selanjutnya
                    if (indexHurufF[pointerF] > indexHurufO[pointerO]) {

                    } else {
                        pointerF++;
                    }
                } else {
                    // Saat ternyata tidak lebih besar O !< S maka tambahkan pointer dan ulang operasi
                    pointerO++;
                }
        }

        return 0;
    }

    // Fungsi utama berjalannya program
    public static void main(String[] args) throws IOException {
        // Melakukan inisiasi input dan output
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        // Membaca nilai dari N (total input yang ingin dimasukkan)
        int N = in.nextInt();

        // Read value of x
        char[] x = new char[N];
        for (int i = 0; i < N; ++i) {
            x[i] = in.next().charAt(0);
        }

        int ans = getTotalDeletedLetters(N, x);
        // out.println(ans);

        // Tutup out
        out.close();
    }

    // taken from https://codeforces.com/submissions/Petr
    // together with PrintWriter, these input-output (IO) is much faster than the usual Scanner(System.in) and System.out
    // please use these classes to avoid your fast algorithm gets Time Limit Exceeded caused by slow input-output (IO)
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