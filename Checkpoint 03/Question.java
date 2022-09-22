public class Question {

    static int counter = 0;

    public static void main(String[] args) {
        System.out.println(mystery(10,3));
        System.out.println(counter);
    }

    static int mystery(int n, int m) {
        counter++;
        if (n == 0)
            return 1;
        return mystery(n/m, m) * m;
    }
}