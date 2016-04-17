package detect;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Detecter implements Runnable{
    private int detecterNumber;
    private File parentFile;/*要监视的文件夹*/
    private HashMap<String, FileInfo> fileInfoMap = new HashMap<>();/*用于递归遍历的MAP*/
    private HashMap<String, FileInfo> preFileInfoMap = new HashMap<>();/*存储先前遍历的文件信息*/
    private HashMap<String, FileInfo> lastFileInfoMap = new HashMap<>();/*存储最近一次遍历的文件信息*/
    private String detectPath;/*监视文件路径*/
    private ArrayList<String> triggers = new ArrayList<>();/*触发器*/
    private ArrayList<String> missions = new ArrayList<>();/*任务*/
    private HashSet<FileInfo> deleteFiles = new HashSet<>();/*删除文件队列*/
    private HashSet<FileInfo> newFiles = new HashSet<>();/*新建文件队列*/
    private HashSet<File> modifiedFiles = new HashSet<>();/*修改内容文件队列*/
    private HashSet<File> sizeChangeFiles = new HashSet<>();/*大小改变文件队列*/
    private Summary summary;
    private Detail detail;
    private boolean isFile = false;
    private String parentPath;

    public Detecter(String input, int detecterNumber){
        try {
            this.detecterNumber = detecterNumber;
            String[] strings = input.split("\\|");
            detectPath = strings[1];
            parentPath = strings[1];
            int i;
            for (i = 2; !strings[i].equals("THEN"); i++)
                triggers.add(strings[i]);
            for (i = i + 1; i < strings.length; i++)
                missions.add(strings[i]);
            parentFile = new File(detectPath);
            if(parentFile.isFile()){
                isFile = true;
            }
            if(!parentFile.exists()){
                System.out.println("第 " + detecterNumber + " 个监视目录不存在");
                System.exit(0);
            }
            summary = new Summary(triggers, missions, detectPath, detecterNumber);
            detail = new Detail(triggers, detectPath, missions, fileInfoMap, detecterNumber);
            if(parentFile.isFile()) {/*如果监视的是文件，则监视其父文件夹*/
                detectPath = parentFile.getParent();
            }
            searchAndSetMap(detectPath);/*初始化Map*/
            preFileInfoMap.putAll(fileInfoMap);
            fileInfoMap.clear();
        }
        catch (Throwable t){
            //t.printStackTrace();
            System.out.println("输入有误");
            System.out.println("线程 " + detecterNumber + " 结束");
            System.exit(0);
        }
    }
    /*遍历目标路径所有文件夹并建立MAP*/
    public void searchAndSetMap(String detectPath){
//        System.out.println("searchAndSetMap");
        File f = new File(detectPath);
        if(f.exists()){
            if(f.isDirectory()){
                fileInfoMap.put(f.getAbsolutePath(),
                        new FileInfo(f.lastModified(), f.getAbsolutePath(), getChildFileLength(f), f.getParent()));
                File[] files = f.listFiles();
                for (File file : files) {
                    searchAndSetMap(file.getAbsolutePath());
                }
            }
            else{
                fileInfoMap.put(f.getAbsolutePath(),
                        new FileInfo(f.lastModified(), f.getAbsolutePath(), f.length(), f.getParent()));
            }
        }
    }
    /*获取被删除的文件*/
    public HashSet<FileInfo> getDeleteFiles(){
//        System.out.println("getDeleteFiles");
        for(Map.Entry<String, FileInfo> entry : preFileInfoMap.entrySet()){/*遍历先前的MAP*/
            File file = new File(preFileInfoMap.get(entry.getKey()).getLastPath());
            if(!file.exists()) {/*如果路径文件在当前MAP中不存在*/
                deleteFiles.add(entry.getValue());
            }
            else if(!file.isDirectory()){
                if(entry.getValue().getLastModified() != lastFileInfoMap.get(entry.getKey()).getLastModified()){
                    /*如果两次修改时间不同，modified*/
                    modifiedFiles.add(file);
                }
            }
        }
        return deleteFiles;
    }
    /*获取被新建的文件*/
    public HashSet<FileInfo> getNewFiles(){
//        System.out.println("getNewFiles");
        for(Map.Entry<String, FileInfo> entry : lastFileInfoMap.entrySet()){/*遍历最近的MAP*/
            if(!preFileInfoMap.containsKey(entry.getKey())) {/*如果发现新的队列里含有之前队列中不存在的key*/
                newFiles.add(entry.getValue());
            }
            else{
                FileInfo preFileInfo = preFileInfoMap.get(entry.getKey());
                FileInfo lastFileInfo = lastFileInfoMap.get(entry.getKey());
                if(preFileInfo.getLastSpace() != lastFileInfo.getLastSpace()){/*文件的大小不一样*/
                    sizeChangeFiles.add(new File(lastFileInfo.getLastPath()));
//                    System.out.println("sizechanged");
                }
            }
        }
        return newFiles;
    }
    /*比较deleteFiles和newFiles*/
    public void compareDeleteAndNew() throws IOException {
        for(FileInfo df : deleteFiles){
            for(FileInfo nf : newFiles){
                if(df.getLastSpace() == nf.getLastSpace() && df.getLastModified() == nf.getLastModified()){
//                    System.out.println("in");
                    if(isFile && missions.contains("recover")
                            && triggers.contains("renamed")
                            && df.getParentPath().equals(nf.getParentPath())
                            && df.getLastPath().equals(parentPath)) {
                        summary.setCount("renamed");
                        recover(df.getLastPath(), nf.getLastPath());
                        lastFileInfoMap.put(df.getLastPath(), df);
                    }
                    else if(isFile && missions.contains("recover")
                            && triggers.contains("path-changed")
//                            && df.getParentPath().equals(nf.getParentPath())
                            && df.getLastPath().equals(parentPath)){
                        summary.setCount("path-changed");
                        recover(df.getLastPath(), nf.getLastPath());
                        lastFileInfoMap.put(df.getLastPath(), df);
                    }
                    else if(!missions.contains("recover")){
                        if (df.getParentPath().equals(nf.getParentPath())) {/*在同一个文件夹里则为rename*/
                            if(isFile){
                                System.out.println("监视文件被改名，监视结束");
                                System.exit(0);
                            }
                            summary.setCount("renamed");
                            detail.printRenamed(preFileInfoMap.get(df.getLastPath()), lastFileInfoMap.get(nf.getLastPath()));
                        } else{/*path-change*/
                            if(isFile){
                                System.out.println("监视文件被移动，监视结束");
                                System.exit(0);
                            }
                            summary.setCount("path-change");
                            detail.printPathChanged(preFileInfoMap.get(df.getLastPath()), lastFileInfoMap.get(nf.getLastPath()));
                        }
                    }
                    else if(!isFile){
                        if((triggers.contains("renamed") && df.getParentPath().equals(nf.getParentPath()))
                                || (triggers.contains("path-changed") && !df.getParentPath().equals(nf.getParentPath()))){
                            recover(df.getLastPath(), nf.getLastPath());
                            lastFileInfoMap.put(df.getLastPath(), df);
                        }
                    }
                    deleteFiles.remove(df);
                    newFiles.remove(nf);
                }
            }
        }
    }
    /*遍历sizeChangeFiles*/
    public void traverseSizeChangeFiles() throws IOException{
//        System.out.println("traverseSizeChanged");
        for (File file : sizeChangeFiles) {
//            System.out.println(missions.contains("recover"));
            if((!isFile)
                    || (isFile && file.getAbsolutePath().equals(parentPath))){
//                    && !missions.contains("recover"))){
//                System.out.println("sizechanged");
                summary.setCount("size-changed");
//                summary.printCountToConsole();
                detail.printSizeChanged(preFileInfoMap.get(file.getAbsolutePath()), lastFileInfoMap.get(file.getAbsolutePath()));
            }
//          }
        }
    }
    /*遍历modifiedFiles*/
    public void traverseModifiedFiles() throws IOException{
        for(File file : modifiedFiles){
//            System.out.println("traverseModifiedFiles");
            if((!isFile) || (isFile && file.getAbsolutePath().equals(parentPath))) {
                summary.setCount("modified");
                detail.printModified(preFileInfoMap.get(file.getAbsolutePath()), lastFileInfoMap.get(file.getAbsolutePath()));
            }
        }
    }
    /*获得文件夹直接子文件的大小*/
    public long getChildFileLength(File file){
//        System.out.println("getChildFileLength");
        long totalSize = 0;
        File[] files = file.listFiles();
        for(File f : files){
            if(f.isFile())
                totalSize += f.length();
        }
        return totalSize;
    }
    /*恢复newPath路径为oldPath*/
    public void recover(String oldPath, String newPath){
        File newFile = new File(newPath);
        newFile.renameTo(new File(oldPath));

    }
    /*获取被真正删除的文件*/
    public void getTrueDeletedFiles() throws IOException {
        if(!lastFileInfoMap.keySet().contains(parentPath)) {
            System.out.println("监视文件被删除，线程" + detecterNumber + "退出");
            System.exit(0);
        }
        if(!(isFile && missions.contains("recover"))) {
            if (!deleteFiles.isEmpty()) {/*若删除队列不为空，说明有文件被删除*/
                for (FileInfo fileInfo : deleteFiles) {
                    summary.setCount("size-changed");
                    detail.printDelete(fileInfo);
                }
            }
        }
    }
    /*获得真正被新建的文件*/
    public void getTrueNewFiles() throws IOException{
        if(!(parentFile.isFile() && missions.contains("recover"))) {
            if (!newFiles.isEmpty()) {/*若新建队列不为空，说明有文件被新建*/
                for (FileInfo fileInfo : newFiles) {
                    if(!isFile) {
                        summary.setCount("size-changed");
                        detail.printNew(fileInfo);
                    }
                }
            }
        }
    }
    @Override
    public void run() {
        try {
            summary.timerPrint();
            while (true) {
                try {
                    searchAndSetMap(detectPath);
                    lastFileInfoMap.putAll(fileInfoMap);
                    fileInfoMap.clear();
                /*与上一次遍历的结果进行比较*/
                    getDeleteFiles();
                    getNewFiles();
                    try {
                        compareDeleteAndNew();
                        getTrueNewFiles();
                        getTrueDeletedFiles();
                        traverseSizeChangeFiles();
                        traverseModifiedFiles();
                        summary.printCountToFile();
                    } catch (IOException i) {
                        //i.printStackTrace();
                    }
                /*更新上一次MAP为当前MAP，清空当前MAP*/
                    preFileInfoMap.clear();
                    preFileInfoMap.putAll(lastFileInfoMap);
                    lastFileInfoMap.clear();
                    deleteFiles.clear();
                    newFiles.clear();
                    sizeChangeFiles.clear();
                    modifiedFiles.clear();
                    Thread.sleep(1);
                } catch (InterruptedException i) {
                    //i.printStackTrace();
                }
            }
        }
        catch (Throwable t){
            //t.printStackTrace();
            System.out.println("监测线程 " + detecterNumber + " 出现故障");
        }
        finally {
            System.out.println("监测线程 " + detecterNumber + " 监测结束");
            System.exit(0);
        }
    }
}
