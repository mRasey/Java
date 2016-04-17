package detect;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

public class Operate {
    private static int sleepTime = 1000;
    /*移动文件*/
    public synchronized static boolean moveFile(String oldPath, String newPath){
        File file = new File(oldPath);
        if(file.exists() && file.isFile()) {
            file.renameTo(new File(newPath));
            try {Thread.sleep(sleepTime);} catch (InterruptedException e) {/*e.printStackTrace();*/}
            return true;
        }
        try {Thread.sleep(sleepTime);} catch (InterruptedException e) {/*e.printStackTrace();*/}
        return false;
    }
    /*删除文件或文件夹*/
    public synchronized static boolean deleteFile(String filePath){
        File file = new File(filePath);
        if(file.exists()){
            file.delete();
            try {Thread.sleep(sleepTime);} catch (InterruptedException e) {/*e.printStackTrace();*/}
            return true;
        }
        try {Thread.sleep(sleepTime);} catch (InterruptedException e) {/*e.printStackTrace();*/}
        return false;
    }
    /*重命名文件*/
    public synchronized static boolean renameFile(String oldName, String newName){
        File file = new File(oldName);
        if(file.exists() && file.isFile()){
            file.renameTo(new File(newName));
            try {Thread.sleep(sleepTime);} catch (InterruptedException e) {/*e.printStackTrace();*/}
            return true;
        }
        try {Thread.sleep(sleepTime);} catch (InterruptedException e) {/*e.printStackTrace();*/}
        return false;
    }
    /*修改文件*/
    public synchronized static boolean writeFile(String filePath, String string) throws IOException{
        File file = new File(filePath);
        FileWriter fileWriter = new FileWriter(file, true);
        if(file.exists() && file.isFile()){
            fileWriter.write(string);
            fileWriter.close();
            try {Thread.sleep(sleepTime);} catch (InterruptedException e) {/*e.printStackTrace();*/}
            return true;
        }
        try {Thread.sleep(sleepTime);} catch (InterruptedException e) {/*e.printStackTrace();*/}
        return false;
    }
    /*新建文件夹*/
    public synchronized static void makeNewFileFolder(String filePath){
        File file = new File(filePath);
        file.mkdirs();
        try {Thread.sleep(sleepTime);} catch (InterruptedException e) {/*e.printStackTrace();*/}
    }
    /*新建文件*/
    public synchronized static void makeNewFile(String filePath) throws IOException {
        File file = new File(filePath);
        file.createNewFile();
        try {Thread.sleep(sleepTime);} catch (InterruptedException e) {/*e.printStackTrace();*/}
    }
    /*获取文件大小*/
    public synchronized static long getLength(String filePath){
        File file = new File(filePath);
        if(file.exists() && file.isFile())
            return file.length();
        return -1;
    }
    /*获取文件修改时间*/
    public synchronized static Date getModifiedTime(String filePath){
        File file = new File(filePath);
        if(file.exists() && file.isFile())
            return new Date(file.lastModified());
        return null;
    }
    /*获取文件名称*/
    public synchronized static String getFileName(String filePath){
        File file = new File(filePath);
        if(file.exists() && file.isFile())
            return file.getName();
        return null;
    }
}