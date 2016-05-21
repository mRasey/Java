package oop2;

import java.io.File;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by cc on 2016/4/13.
 */
public class InputHandler {
    private Scanner in;
    private static ExecutorService fixedThreadPool = Executors.newFixedThreadPool(20);
    public static Summary summary;
    public static Detail detail;
    public static volatile ArrayList<TreeMap<String, FileRecord>> FileMapArray;
    public File pathFile;
    private static ArrayList<FileScanner> ScannerArray ;
    public static void ShutDown(){
        for(FileScanner fs:ScannerArray)
        {
            fs.close();
        }
        fixedThreadPool.shutdown();
        summary.close();
        detail.close();
    }
    public InputHandler() throws Exception {
        FileMapArray=new ArrayList<TreeMap<String, FileRecord>>();
        ScannerArray = new ArrayList<>();
        detail = new Detail();
        summary = new Summary();
        detail.start();
        summary.start();
        while(true)
        {
            try
            {
                Scanner in = new Scanner(System.in);
                if(in.hasNextLine())
                {
                    String cmd=in.nextLine();
                    if(cmd.equals("END"))
                        break;
                    Handler(cmd);
                }
            }catch (NoSuchFileException n)
            {
                System.out.println("No such file or folder in your given directory");
            }
            catch (Exception e)
            {
                System.out.println("Wrong format.");
            }
            catch (Throwable a){

            }
        }
        System.out.println("End received.");
    }
    public void Handler(String cmd) throws Exception
    {
        String [] arguments = cmd.split(" ");
        String [] tmp = new String[5];
        try{
        for(int i=0,j=0; i< arguments.length; i++)
        {
            if(!arguments[i].equals(""))
            {
                tmp[j++]=arguments[i];
            }
        }}catch (ArrayIndexOutOfBoundsException a){
            throw new Exception();
        }
        if(!tmp[0].equals("IF") || !tmp[3].equals("THEN"))
        {
            throw new Exception();
        }
        File pathFile = new File(tmp[1]);
        String Path = tmp[1];
        boolean found=false;
        if(!pathFile.exists())
        {
            throw new NoSuchFileException(pathFile.getName());
        }
        else {
            TreeMap<String, FileRecord> FileMap = new TreeMap<>();
            FileMapArray.add(FileMap);
            if(pathFile.isFile())
                new ResourceListener(pathFile.getParent(),  FileMap);
            else if(pathFile.isDirectory())
                new ResourceListener(pathFile.getAbsolutePath(),  FileMap);
            IF If = new IF(tmp[2], pathFile.isFile() ? "File" : "Folder", tmp[4], pathFile.getAbsolutePath());
            FileScanner scanner;
            for(FileScanner sc:ScannerArray){
                if(pathFile.isFile()){
                    if(sc.getPath().equals(pathFile.getParent()))
                    {
                        sc.addMission(If);
                        found=true;
                        break;
                    }
                }
                else{
                    if(sc.getPath().equals(pathFile.getAbsolutePath()))
                    {
                        sc.addMission(If);
                        found=true;
                        break;
                    }
                }
            }
            if(!found) {
                if(pathFile.isFile()){
                scanner = new FileScanner(pathFile.getParent(), FileMap, If);
                ScannerArray.add(scanner);
                fixedThreadPool.execute(scanner);
                }
                else if(pathFile.isDirectory()){
                    scanner = new FileScanner(pathFile.getAbsolutePath(), FileMap, If);
                    ScannerArray.add(scanner);
                    fixedThreadPool.execute(scanner);
                }
            }
            }
    }
}
