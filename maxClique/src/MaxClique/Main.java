package MaxClique;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.LinkedList;

public class Main {

    public static void main(String [] args){
        try {
            InputHandler.Initial();
        } catch (FileNotFoundException e) {
            System.out.println("Graph file doesn't exist.");
            System.exit(0);
        } catch (IOException e) {
            System.out.println("I/O is fucked.");
            System.exit(0);
        }
        Operation.PointArray = InputHandler.PointArray.clone();
        long startTime = System.currentTimeMillis();      
        
        Set clique = new Set();
        
        clique = Trygetmore.getMore5();
        System.out.println(Operation.correctTest(clique));
        System.out.println("answer:"+clique.size());
        //Trygetmore.getMore4();
        System.out.println("time:"+(System.currentTimeMillis()-startTime)+"ms");

        File output = new File("output.txt");
        try {
			FileWriter fWriter = new FileWriter(output);
			LinkedList<Integer> list = clique.getAllElements();
			for(Integer i:list){
				fWriter.write(i+" ");
			}
			fWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
        
    }

}
