package optimize;

public class add {
    static int add(int a, int b) {
        return a + b;
    }

    static int sub(int a, int b) {
        return a - b;
    }

    public static void main(String[] args) {
        int a = 1;
        int b = 2;
        int c = 3;
        int d = 4;
        int e = (a + b) - (c + d);
        int f = sub(add(a, b), add(c, d));
    }
}
