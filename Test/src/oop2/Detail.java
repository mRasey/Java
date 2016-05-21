package oop2;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;

/**
 * Created by cc on 2016/4/13.
 */
public class Detail extends Thread{
    private boolean click=false;
    private LinkedList<String> SizeChanged;
    private LinkedList<String> PathChanged;
    private LinkedList<String> Modified;
    private LinkedList<String> Renamed;
    public Detail(){
        SizeChanged = new LinkedList<>();
        PathChanged = new LinkedList<>();
        Modified = new LinkedList<>();
        Renamed = new LinkedList<>();
    }
    public void close(){
        click = false;
    }
    public synchronized void RecordSizeChanged(String ...strings){
        String s="";
        for(int i=0;i<strings.length;i++){
            s+=strings[i];
        }
        SizeChanged.add(s);
    }
    public synchronized void RecordPathChanged(String oldpath,String newpath){
        PathChanged.add("Path changed: "+oldpath +" to "+newpath);
    }
    public synchronized void RecordRenamed(String oldpath,String newpath){
        Renamed.add(oldpath+" has been renamed to "+newpath);
    }
    public synchronized void RecordModified(String Path,long OldModified, long NewModified){
        Modified.add(Path+" is modified.The old modified time is "+OldModified+". The new modified time is "+NewModified);
    }
    public void run(){
        click = true;
        File Dfile = new File("detail.txt");
        if(Dfile.exists())
            Dfile.delete();
        while(click){
            try {
                File detailfile = new File("detail.txt");
                if(!detailfile.exists())
                    detailfile.createNewFile();
                FileWriter fileWriter = new FileWriter("detail.txt",true);
                while(!SizeChanged.isEmpty()){
                    String s=SizeChanged.removeFirst();
                    fileWriter.write(s+"\n");
                }
                while(!Modified.isEmpty()){
                    String s=Modified.removeFirst();
                    fileWriter.write(s+"\n");
                }
                while(!Renamed.isEmpty()){
                    String s=Renamed.removeFirst();
                    fileWriter.write(s+"\n");
                }
                while(!PathChanged.isEmpty()){
                    String s=PathChanged.removeFirst();
                    fileWriter.write(s+"\n");
                }
                fileWriter.close();
                Thread.sleep(500);
            } catch (IOException e) {
                System.out.println("detail.txt premission denied");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}
