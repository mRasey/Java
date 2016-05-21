package max;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Timer;

public class Main {
    public static void main(String[] args) throws IOException {
        int[][] map = new int[450][450];
        BufferedReader bufferedReader = new BufferedReader(new FileReader(new File("D:\\frb30-15-1.clq")));
        bufferedReader.readLine();
        String s = bufferedReader.readLine();
        while(s != null){
            String[] strings = s.split(" ");
            map[Integer.parseInt(strings[1]) - 1][Integer.parseInt(strings[2]) - 1] = 1;
            s = bufferedReader.readLine();
        }
        System.out.println("finish build map");
        MaxClique maxClique = new MaxClique(450, map);
        Print print = new Print(maxClique);
        new Thread(maxClique).start();
        new Timer().schedule(print, 1000, 1000);
    }
}
