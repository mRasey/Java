package MaxClique;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.Random;

public class Main {
    public static void main(String [] args){
        try {
            InputHandler ih = new InputHandler();
        } catch (FileNotFoundException e) {
            System.out.println("Graph file doesn't exist.");
            System.exit(0);
        } catch (IOException e) {
            System.out.println("I/O is fucked.");
            System.exit(0);
        }
        System.out.println(new Date(System.currentTimeMillis()));
//        for(Point p:InputHandler.SortedArray){
//            System.out.println(p.getDegree());
//        }
//        System.out.println("-------------");
//        for(Point p:InputHandler.PointArray){
//            System.out.println(p.getDegree());
//        }
        for(int i = 0;i < 10; i++)
            new Algorithm(InputHandler.SortedArray[new Random().nextInt(400) + 400 * i].id).start();
    }
    public static int getInitPoint(){return 1;}

}
