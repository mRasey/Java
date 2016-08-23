package trans;

public class testSwitch {
    int a;
    public static void main(String[] args) {
        char a = '1';
        String s = "hello";
        switch (s) {
            case "hello" :
                System.out.println(1);
                break;
            case "world" :
                System.out.println(2);
                break;
        }

        Enum e = Enum.A;
        switch (e) {
            case A :
                System.out.println(3);
                break;
            case D :
                System.out.println(1);
                break;
            case B :
                System.out.println(2);
                break;
        }
    }
}

enum Enum {
    A,
    B,
    C,
    D
}
