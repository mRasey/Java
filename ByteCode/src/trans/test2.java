package trans;

/**
 * Created by Billy on 2016/8/10.
 */
public class test2 {
    int a = 233;
    int b = a;
    static int c = 666;
    static int d = c;

    public void foo() {
        int m = a + b;
        int n = c + d;
    }
    public static void main(String[] args) {
        int[] a = new int[]{1, 2, 3};
        float[] b = new float[5];
        b[3] = 233;
        b[4] = 666;
        float c = b[3];
        int d = 0;
        int x = d;

        String s = "123";
        String j = s;
    }
}

class C {
    int a = 233;
    int b = 666;

    public void foo() {
        int c = a + b;
    }
}

class D extends C{

}
