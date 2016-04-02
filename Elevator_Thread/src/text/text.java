package text;

import java.util.Scanner;
import java.util.Vector;

/**
 * Created by Billy on 2016/4/2.
 */
public class text {
    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        Vector<Integer> in = new Vector<>();
        //while(true){
        in.add(123);
        in.add(456);
        in.removeAllElements();
        if(in.isEmpty())
            System.out.println("is empty");
        //}
    }
}
