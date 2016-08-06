/**
 * Created by Billy on 2016/8/5.
 */
public class Main {
    public static void main(String[] args) {
        Main main = new Main();
        int a = 1;
        String b = "hello";
        int c = main.test(a, b);
        if(c < 0) {
            System.out.println("small");
        }
        else {
            System.out.println("big");
        }
        System.out.println(c);
    }

    public int test(int a, String b) {
        return a + b.length();
    }
}
