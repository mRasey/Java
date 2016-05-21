package oop2;



import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.TreeMap;

/**
 * Created by cc on 2016/4/13.
 */
public class ResourceListener{
    private String path;
    ResourceListener(String path, TreeMap<String, FileRecord> FileMap)
    {
        try {
            this.path = path;
            File parentPath = new File(this.path);
            if(parentPath.isFile())
                parentPath=new File(parentPath.getParent());
            if(parentPath.isDirectory()) {
                File[] filelist = parentPath.listFiles();
                for (File f : filelist) {
                   try {
                       FileRecord fr = new FileRecord(f);
                       FileMap.put(f.getAbsolutePath(),fr);
                    } catch (Exception e) {
                    }

                    if (f.isDirectory() && !f.getAbsoluteFile().equals(path)) {
                        new ResourceListener(f.getAbsolutePath(),FileMap);
                    }
                }
            }
        }catch (NullPointerException a){

        }
        catch(Throwable s){
            System.out.println("Too many files and folders. Overflow");
            InputHandler.ShutDown();
            System.exit(0);
        }
    }
}

