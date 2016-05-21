package oop2;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;

/**
 * Created by cc on 2016/4/13.
 */
public class Summary extends Thread{
    private long Renamed;
    private long Modified;
    private long PathChanged;
    private long SizeChanged;
    private boolean click=false;
    public Summary(){
        Renamed =0;
        Modified =0;
        PathChanged =0;
        SizeChanged =0;
    }
    public synchronized void PlusRenamed(){
        Renamed++;
    }
    public synchronized void PlusModified(){
        Modified++;
    }
    public synchronized void PlusPathChanged(){
        PathChanged++;
    }
    public synchronized void PlusSizeChanged(){
        SizeChanged++;
    }
    public void close(){
        click = false;
    }
    public void run(){
        click = true;
        while(click){
            try {
                File file = new File("summary.txt");
                if(!file.exists()){
                    file.createNewFile();
                }
                FileWriter fileWriter = new FileWriter("summary.txt");
                fileWriter.write("Renamed : "+ Renamed+"\n");
                fileWriter.write("Modified : "+ Modified+"\n");
                fileWriter.write("Path-changed : "+PathChanged+"\n");
                fileWriter.write("Size-changed : "+SizeChanged+"\n");
                fileWriter.close();
                Thread.sleep(500);
            } catch (IOException e) {

            } catch (InterruptedException e) {
            }
        }
    }

}

