package oop2;

import java.io.File;

/**
 * Created by cc on 2016/4/13.
 */
public class FileRecord {
    public String AbsolutePath;
    public String ParentPath;
    public String Path;
    public long ModifiedTime;
    public long Capacity;
    public String Name;
    IF.FileType fileType;
    public FileRecord(File file) throws Exception {
        AbsolutePath = file.getAbsolutePath();
        ParentPath = file.getParent();
        Path = file.getPath();
        Capacity = file.length();
        Name = file.getName();
        ModifiedTime = file.lastModified();
        if(file.isFile())
            fileType = IF.FileType.File;
        else if(file.isDirectory())
            fileType = IF.FileType.Folder;
        else
            throw new Exception();
    }
    public String toString()
    {
        return AbsolutePath+" "+fileType;
    }
}
