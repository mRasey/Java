package optimize;

public class testTab {
    public static void main(String[] args) {
        tab:
        for(int i = 0; i < 10; i++) {
            System.out.println(i);
            if(i == 5)
                break tab;
        }
        System.out.println("hello");
    }
}
