import java.util.Random;

public class split {
    public static void main(String[] args) {
        for(int i = 0; i < 4; i++) {
            Random random = new Random();
            int r = random.nextInt('z' - 'a');
            System.out.print((char) ('a' + r));
        }
        System.out.println(21);
    }
}
