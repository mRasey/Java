package JunitTest;

import java.util.Scanner;

public class Test {

    public void helpInputJudge(){
        inputJudge(new Scanner(System.in));
    }

    public void inputJudge(Scanner in) {
        String input = in.nextLine();
        if (input.equals("true"))
            System.out.println("good");
        else
            System.out.println("bad");
    }

    public void judge(boolean input) {
        if (input)
            System.out.println("true");
        else
            System.out.println("false");
    }

    public void judge1(boolean input) {
        if (input)
            System.out.println("true1");
        else
            System.out.println("false1");
    }

    public void judge2(boolean input) {
        if (input)
            System.out.println("true2");
        else
            System.out.println("false2");
    }
}
