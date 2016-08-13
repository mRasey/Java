public class testSwitch {
    public static void main(String[] args) {
        Character a = '1';
        switch (a) {
            case '4' :
                System.out.println(1);
                break;
            case '2' :
                System.out.println(2);
                break;
        }

        E e = E.A;
        switch (e) {
            case D :
                System.out.println(1);
                break;
            case B :
                System.out.println(2);
                break;
        }
    }
}

enum E {
    A,
    B,
    C,
    D
}
