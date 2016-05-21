package oop2;

import java.io.File;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.TreeMap;

/**
 * Created by cc on 2016/4/14.
 */
public class FileScanner extends Thread {
    private String Path;
    private TreeMap<String, FileRecord> FileMap;
    private LinkedList<IF> MissionList;
    private boolean click=false;
    FileScanner(String path,TreeMap<String, FileRecord> fileMap,IF If)
    {
        MissionList = new LinkedList<>();
        Path = path;
        FileMap =fileMap;
        MissionList.add(If);
    }
    public String getPath(){
        return Path;
    }
    public void close(){click=false;}
    public void addMission(IF If){
        MissionList.add(If);
    }
    public void run(){
        File ParentDirectory = new File(Path);
        click = true;
        LinkedList<FileRecord> DeletedFileList = new LinkedList<>();
        LinkedList<FileRecord> CreatedFileList = new LinkedList<>();
        LinkedList<FileRecord> ModifiedFileList = new LinkedList<>();
        while(ParentDirectory.exists() && click) {
            TreeMap<String, FileRecord> fileMap = new TreeMap<>();
            try {
                new ResourceListener(Path, fileMap);
            }catch (NullPointerException e){

            }finally {

            }
            for(String key : fileMap.keySet()){
                if(!FileMap.containsKey(key)){
                    CreatedFileList.add(fileMap.get(key));
                }
                else{
                    if(fileMap.get(key).ModifiedTime != FileMap.get(key).ModifiedTime)
                    {
                        ModifiedFileList.add(fileMap.get(key));
                    }
                }

            }
            for(String key : FileMap.keySet()){
                if(!fileMap.containsKey(key)){
                    DeletedFileList.add(FileMap.get(key));
                }
            }
            for(IF mission : MissionList){
                if(mission.getTrigger().equals("renamed")){//for files
                    LinkedList<FileRecord> deletelist = new LinkedList<>();
                    for(FileRecord DeletedFile : DeletedFileList){
                        if((mission.getFileType().equals("File") && DeletedFile.AbsolutePath.equals(mission.Path)) || (mission.getFileType().equals("Folder") && mission.Path.equals(DeletedFile.AbsolutePath.substring(0,mission.Path.length())))){
                            for(FileRecord CreatedFile: CreatedFileList){
                                if(CreatedFile.fileType.toString().equals("File") && CreatedFile.ParentPath.equals(DeletedFile.ParentPath) && DeletedFile.Capacity == CreatedFile.Capacity) {
                                    String MissionPath = mission.Path;
                                    for (IF SameIf : MissionList) {
                                        if(SameIf.getFileType().equals("File") && SameIf.getTrigger().equals(mission.getTrigger()) && SameIf.Path.equals(MissionPath) && mission.getFileType().equals("File")) {
                                            if (SameIf.getMission().equals("recordsummary")) {
                                                InputHandler.summary.PlusRenamed();
                                                SameIf.Path = CreatedFile.AbsolutePath;
                                            } else if (SameIf.getMission().equals("recorddetail")) {
                                                InputHandler.detail.RecordRenamed(DeletedFile.AbsolutePath, CreatedFile.AbsolutePath);
                                                SameIf.Path = CreatedFile.AbsolutePath;
                                            } else if (SameIf.getMission().equals("recover")) {
                                                File CreaFile = new File(CreatedFile.AbsolutePath);
                                                File DeleFile = new File(DeletedFile.AbsolutePath);
                                                if (!DeleFile.exists()) {
                                                    CreaFile.renameTo(DeleFile);
                                                    CreaFile = new File(DeletedFile.AbsolutePath);
                                                }
                                                try {
                                                    fileMap.put(DeletedFile.AbsolutePath, new FileRecord(CreaFile));
                                                    if(fileMap.containsKey(CreatedFile.AbsolutePath)){
                                                        fileMap.remove(CreatedFile.AbsolutePath);
                                                    }
                                                } catch (Exception e) {

                                                }
                                            } else {
                                                System.out.println("Unknown trigger");
                                            }
                                        }
                                        else if(SameIf.getFileType().equals("Folder") && SameIf.getTrigger().equals(mission.getTrigger()) && SameIf.Path.equals(MissionPath.substring(0,SameIf.Path.length())) && mission.getFileType().equals("File")) {
                                            if (SameIf.getMission().equals("recordsummary")) {
                                                InputHandler.summary.PlusRenamed();
                                            } else if (SameIf.getMission().equals("recorddetail")) {
                                                InputHandler.detail.RecordRenamed(DeletedFile.AbsolutePath, CreatedFile.AbsolutePath);
                                            } else if (SameIf.getMission().equals("recover")) {
                                                File CreaFile = new File(CreatedFile.AbsolutePath);
                                                File DeleFile = new File(DeletedFile.AbsolutePath);
                                                if (!DeleFile.exists()) {
                                                    CreaFile.renameTo(DeleFile);
                                                    CreaFile = new File(DeletedFile.AbsolutePath);
                                                }
                                                try {
                                                    fileMap.put(DeletedFile.AbsolutePath, new FileRecord(CreaFile));
                                                    if(fileMap.containsKey(CreatedFile.AbsolutePath)){
                                                        fileMap.remove(CreatedFile.AbsolutePath);
                                                    }
                                                } catch (Exception e) {
                                                }
                                            } else {
                                                System.out.println("Unknown trigger");
                                            }
                                        }
                                        else if(SameIf.getFileType().equals("Folder") && SameIf.getTrigger().equals(mission.getTrigger()) && SameIf.Path.equals(MissionPath) && mission.getFileType().equals("Folder")) {
                                            if (SameIf.getMission().equals("recordsummary")) {
                                                InputHandler.summary.PlusRenamed();
                                            } else if (SameIf.getMission().equals("recorddetail")) {
                                                InputHandler.detail.RecordRenamed(DeletedFile.AbsolutePath, CreatedFile.AbsolutePath);
                                            } else if (SameIf.getMission().equals("recover")) {
                                                File CreaFile = new File(CreatedFile.AbsolutePath);
                                                File DeleFile = new File(DeletedFile.AbsolutePath);
                                                if (!DeleFile.exists()) {
                                                    CreaFile.renameTo(DeleFile);
                                                    CreaFile = new File(DeletedFile.AbsolutePath);
                                                }
                                                try {
                                                    fileMap.put(DeletedFile.AbsolutePath, new FileRecord(CreaFile));
                                                    if(fileMap.containsKey(CreatedFile.AbsolutePath)){
                                                        fileMap.remove(CreatedFile.AbsolutePath);
                                                    }
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            } else {
                                                System.out.println("Unknown trigger");
                                            }
                                        }
                                        else if(SameIf.getFileType().equals("File") && SameIf.getTrigger().equals(mission.getTrigger()) && MissionPath.equals(SameIf.Path.substring(0,MissionPath.length())) && mission.getFileType().equals("Folder")) {
                                            if(SameIf.Path.equals(DeletedFile.AbsolutePath)){
                                                if (SameIf.getMission().equals("recordsummary")) {
                                                    InputHandler.summary.PlusRenamed();
                                                    SameIf.Path=CreatedFile.AbsolutePath;
                                                } else if (SameIf.getMission().equals("recorddetail")) {
                                                    InputHandler.detail.RecordRenamed(DeletedFile.AbsolutePath, CreatedFile.AbsolutePath);
                                                    SameIf.Path=CreatedFile.AbsolutePath;
                                                } else if (SameIf.getMission().equals("recover")) {
                                                    File CreaFile = new File(CreatedFile.AbsolutePath);
                                                    File DeleFile = new File(DeletedFile.AbsolutePath);
                                                    if (!DeleFile.exists()) {
                                                        CreaFile.renameTo(DeleFile);
                                                        CreaFile = new File(DeletedFile.AbsolutePath);
                                                    }
                                                    try {
                                                        fileMap.put(DeletedFile.AbsolutePath, new FileRecord(CreaFile));
                                                        if(fileMap.containsKey(CreatedFile.AbsolutePath)){
                                                            fileMap.remove(CreatedFile.AbsolutePath);
                                                        }
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }
                                                } else {
                                                    System.out.println("Unknown trigger");
                                                }
                                            }
                                        }
                                    }
                                    deletelist.add(DeletedFile);
                                    break;
                                }
                            }
                        }
                    }
                    for(FileRecord ddd:deletelist){
                        for(FileRecord kk : DeletedFileList){
                            if(ddd.AbsolutePath.equals(kk.AbsolutePath)){
                                DeletedFileList.remove(kk);
                                break;
                            }
                        }
                    }
                }else if(mission.getTrigger().equals("modified")){
                    for(FileRecord ModifiedFile:ModifiedFileList){
                        if(ModifiedFile.AbsolutePath.equals(mission.Path) && mission.getFileType().equals("File")){
                            if(mission.getMission().equals("recordsummary")){
                                InputHandler.summary.PlusModified();
                            }else if(mission.getMission().equals("recorddetail")){
                                InputHandler.detail.RecordModified(mission.Path,FileMap.get(ModifiedFile.AbsolutePath).ModifiedTime,ModifiedFile.ModifiedTime);
                            }else{
                                System.out.println("Unknown trigger");
                            }
                        }
                        else if(mission.Path.equals(ModifiedFile.AbsolutePath.substring(0,mission.Path.length())) && mission.getFileType().equals("Folder")){
                            File file = new File(ModifiedFile.AbsolutePath);
                            if(file.exists()){
                                if(file.getParent().equals(mission.Path)){
                                    if(mission.getMission().equals("recordsummary")){
                                        InputHandler.summary.PlusModified();
                                    }else if(mission.getMission().equals("recorddetail")){
                                        InputHandler.detail.RecordModified(ModifiedFile.AbsolutePath,FileMap.get(ModifiedFile.AbsolutePath).ModifiedTime,ModifiedFile.ModifiedTime);
                                    }else{
                                        System.out.println("Unknown trigger");
                                    }
                                }
                            }
                        }
                    }
                }else if(mission.getTrigger().equals("pathchanged")){// to file
                    LinkedList<FileRecord> deletelist = new LinkedList<>();
                    for(FileRecord DeletedFile : DeletedFileList){
                        if(DeletedFile.AbsolutePath.substring(0,mission.Path.length()).equals(mission.Path)){
                            for(FileRecord CreatedFile: CreatedFileList){
                                if(CreatedFile.fileType.toString().equals("File") && CreatedFile.Name.equals(DeletedFile.Name) && DeletedFile.Capacity == CreatedFile.Capacity)
                                {
                                    String MissionPath = mission.Path;
                                    for(IF SameIf:MissionList) {
                                        if(SameIf.getTrigger().equals(mission.getTrigger())  && SameIf.getFileType().equals("File")){
                                            if(SameIf.Path.equals(DeletedFile.AbsolutePath)){
                                                if (SameIf.getMission().equals("recordsummary")) {
                                                    InputHandler.summary.PlusPathChanged();
                                                    SameIf.Path=CreatedFile.AbsolutePath;
                                                } else if (SameIf.getMission().equals("recorddetail")) {
                                                    InputHandler.detail.RecordPathChanged(DeletedFile.AbsolutePath, CreatedFile.AbsolutePath);
                                                    SameIf.Path=CreatedFile.AbsolutePath;
                                                } else if (SameIf.getMission().equals("recover")) {
                                                    File CreaFile = new File(CreatedFile.AbsolutePath);
                                                    File DeleFile = new File(DeletedFile.AbsolutePath);
                                                    if (!DeleFile.exists()) {
                                                        CreaFile.renameTo(DeleFile);
                                                        CreaFile = new File(DeletedFile.AbsolutePath);
                                                    }
                                                    try {
                                                        fileMap.put(DeletedFile.AbsolutePath, new FileRecord(CreaFile));
                                                        if(fileMap.containsKey(CreatedFile.AbsolutePath)) {
                                                            fileMap.remove(CreatedFile.AbsolutePath);
                                                        }
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }
                                                } else {
                                                    System.out.println("Unknown trigger");
                                                }
                                            }
                                        }
                                        else if(SameIf.getTrigger().equals(mission.getTrigger())  && SameIf.getFileType().equals("Folder")){
                                            if(SameIf.Path.equals(DeletedFile.AbsolutePath.substring(0,SameIf.Path.length()))){
                                                if (SameIf.getMission().equals("recordsummary")) {
                                                    InputHandler.summary.PlusPathChanged();
                                                } else if (SameIf.getMission().equals("recorddetail")) {
                                                    InputHandler.detail.RecordPathChanged(DeletedFile.AbsolutePath, CreatedFile.AbsolutePath);
                                                } else if (SameIf.getMission().equals("recover")) {
                                                    File CreaFile = new File(CreatedFile.AbsolutePath);
                                                    File DeleFile = new File(DeletedFile.AbsolutePath);
                                                    if (!DeleFile.exists()) {
                                                        CreaFile.renameTo(DeleFile);
                                                        CreaFile = new File(DeletedFile.AbsolutePath);
                                                    }
                                                    try {
                                                        fileMap.put(DeletedFile.AbsolutePath, new FileRecord(CreaFile));
                                                        if(fileMap.containsKey(CreatedFile.AbsolutePath)) {
                                                            fileMap.remove(CreatedFile.AbsolutePath);
                                                        }
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }
                                                } else {
                                                    System.out.println("Unknown trigger");
                                                }
                                            }
                                        }
                                    }
                                    deletelist.add(DeletedFile);
                                    break;
                                }
                            }
                        }
                    }
                    for(FileRecord ddd:deletelist){
                        for(FileRecord kk : DeletedFileList){
                            if(ddd.AbsolutePath.equals(kk.AbsolutePath)){
                                DeletedFileList.remove(kk);
                                break;
                            }
                        }
                    }
                }else if(mission.getTrigger().equals("sizechanged")){
                    if(mission.getFileType().equals("File")){
                        for(FileRecord CreatedFile:CreatedFileList){
                            if(mission.Path.equals(CreatedFile.AbsolutePath)){
                                if(mission.getMission().equals("recorddetail")){
                                    InputHandler.detail.RecordSizeChanged("Size changed in "+mission.Path+" By creating a file with the same name."+"The new file 's capacity is "+fileMap.get(CreatedFile.AbsolutePath).Capacity);
                                }else if(mission.getMission().equals("recordsummary")){
                                    InputHandler.summary.PlusSizeChanged();
                                }else
                                {
                                    System.out.println("Illegal mission.");
                                }
                            }
                        }
                        for(FileRecord DeletedFile:DeletedFileList){
                            if(mission.Path.equals(DeletedFile.AbsolutePath)){
                                if(mission.getMission().equals("recorddetail")){
                                    InputHandler.detail.RecordSizeChanged("Size changed in "+mission.Path+" By deleting this file."+"The original file 's capacity is "+FileMap.get(DeletedFile.AbsolutePath).Capacity);
                                }else if(mission.getMission().equals("recordsummary")){
                                    InputHandler.summary.PlusSizeChanged();
                                }else
                                {
                                    System.out.println("Illegal mission.");
                                }
                            }
                        }
                        for(FileRecord ModifiedFile:ModifiedFileList){
                            if(mission.Path.equals(ModifiedFile.AbsolutePath)){
                                if(mission.getMission().equals("recorddetail")){
                                    InputHandler.detail.RecordSizeChanged("Size changed in "+mission.Path+" By Modifying this file."+"The original file 's capacity is "+FileMap.get(ModifiedFile.AbsolutePath).Capacity+"The new file 's capacity is "+fileMap.get(ModifiedFile.AbsolutePath).Capacity);
                                }else if(mission.getMission().equals("recordsummary")){
                                    InputHandler.summary.PlusSizeChanged();
                                }else
                                {
                                    System.out.println("Illegal mission.");
                                }
                            }
                        }
                    }
                    else{
                        File direc=new File(mission.Path);
                        long Capacity=0;
                        for(File f:direc.listFiles())
                        {
                            if(f.isFile())
                                Capacity+=f.length();
                        }
                        for(FileRecord CreatedFile:CreatedFileList){
                            if(mission.Path.equals(CreatedFile.AbsolutePath.substring(0,mission.Path.length())) && !mission.Path.equals(CreatedFile.AbsolutePath) && CreatedFile.fileType.toString().equals("File")){
                                if(mission.getMission().equals("recorddetail")){

                                    InputHandler.detail.RecordSizeChanged("Size changed in "+mission.Path+" By creating a file -->"+CreatedFile.AbsolutePath+"The direct capacity of the monitored directory is "+Capacity);
                                }else if(mission.getMission().equals("recordsummary")){
                                    InputHandler.summary.PlusSizeChanged();
                                }else
                                {
                                    System.out.println("Illegal mission.");
                                }
                            }
                        }
                        for(FileRecord DeletedFile:DeletedFileList){
                            if(mission.Path.equals(DeletedFile.AbsolutePath.substring(0,mission.Path.length())) && !mission.Path.equals(DeletedFile.AbsolutePath) && DeletedFile.fileType.toString().equals("File")){
                                if(mission.getMission().equals("recorddetail")){
                                    InputHandler.detail.RecordSizeChanged("Size changed in "+mission.Path+" By deleting the file -->"+DeletedFile.AbsolutePath+"The direct capacity of the monitored directory is "+Capacity);
                                }else if(mission.getMission().equals("recordsummary")){
                                    InputHandler.summary.PlusSizeChanged();
                                }else
                                {
                                    System.out.println("Illegal mission.");
                                }
                            }
                        }
                        for(FileRecord ModifiedFile:ModifiedFileList){
                            if(mission.Path.equals(ModifiedFile.AbsolutePath.substring(0,mission.Path.length())) && !mission.Path.equals(ModifiedFile.AbsolutePath) && ModifiedFile.fileType.toString().equals("File")){
                                if(mission.getMission().equals("recorddetail")){
                                    InputHandler.detail.RecordSizeChanged("Size changed in "+mission.Path+" By Modifying this file-->"+ModifiedFile.AbsolutePath+"The direct capacity of the monitored directory is "+Capacity);
                                }else if(mission.getMission().equals("recordsummary")){
                                    InputHandler.summary.PlusSizeChanged();
                                }else
                                {
                                    System.out.println("Illegal mission.");
                                }
                            }
                        }
                    }
                }else{
                    System.out.println("Unknown mission  "+mission.getTrigger());
                }
            }
            FileMap = fileMap;
            CreatedFileList.clear();
            DeletedFileList.clear();
            ModifiedFileList.clear();

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {

            }
        }
    }
}
