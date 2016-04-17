package detect;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Detail {
    private ArrayList<String> records = new ArrayList<>();
    private ArrayList<String> triggers = new ArrayList<>();
    private ArrayList<String> missions = new ArrayList<>();
    private HashMap<String, FileInfo> fileInfoMap = new HashMap<>();
    private String detailPath;
    private int detectNumber;
    private File file;// = new File("D:\\thread_" + detectNumber + "_record-detail.txt");

    public Detail(ArrayList<String> triggers, String detailPath, ArrayList<String> missions,
                  HashMap<String, FileInfo> fileInfoMap, int detectNumber){
        this.triggers = triggers;
        this.detailPath = detailPath;
        this.missions = missions;
        this.fileInfoMap = fileInfoMap;
        this.detectNumber = detectNumber;
        file = new File("D:\\record-detail.txt");
    }
    /*输出所有信息*/
    public synchronized void printAll(FileInfo preFileInfo, FileInfo lastFileInfo) throws IOException{
        if(missions.contains("record-detail")){
            printModified(preFileInfo, lastFileInfo);
            printRenamed(preFileInfo, lastFileInfo);
            printSizeChanged(preFileInfo, lastFileInfo);
            printPathChanged(preFileInfo, lastFileInfo);
        }
    }
    /*输出modified*/
    public synchronized void printModified(FileInfo preFileInfo, FileInfo lastFileInfo) throws IOException{
        FileWriter fileWriter = new FileWriter(file, true);
        if(triggers.contains("modified")){
            /*输出到控制台*/
            System.out.println(preFileInfo.getLastPath() + " 发生了修改 "
                    + "上次修改时间 ：" + new Date(preFileInfo.getLastModified())
                    + "   最近一次修改时间 : " + new Date(lastFileInfo.getLastModified()));
            /*输出到文件*/
            fileWriter.write(preFileInfo.getLastPath() + " 发生了修改 "
                    + "上次修改时间 ：" + new Date(preFileInfo.getLastModified())
                    + "   最近一次修改时间 : " + new Date(lastFileInfo.getLastModified()) + "\n");
            fileWriter.close();
        }
    }
    /*输出renamed*/
    public synchronized void printRenamed(FileInfo preFileInfo, FileInfo lastFileInfo) throws IOException{
        FileWriter fileWriter = new FileWriter(file, true);
        if(triggers.contains("renamed")){
            /*输出到控制台*/
            System.out.println(new File(preFileInfo.getLastPath()).getParent() + " 里发生了重命名 "
                    + "之前名称 ：" + new File(preFileInfo.getLastPath()).getName()
                    + "   现在名称 : " + new File(lastFileInfo.getLastPath()).getName());
            /*输出到文件*/
            fileWriter.write(preFileInfo.getLastPath() + " 发生了修改 "
                    + "之前名称 ：" + preFileInfo.getLastPath()
                    + "   现在名称 : " + lastFileInfo.getLastPath() + "\n");
            fileWriter.close();
        }
    }
    /*输出path-changed*/
    public synchronized void printPathChanged(FileInfo preFileInfo, FileInfo lastFileInfo) throws IOException{
        FileWriter fileWriter = new FileWriter(file, true);
        if(triggers.contains("path-changed")){
            /*输出到控制台*/
            System.out.println("发生了路径修改 "
                    + "之前路径 ：" + preFileInfo.getLastPath()
                    + "   现在路径 : " + lastFileInfo.getLastPath());
            /*输出到文件*/
            fileWriter.write("发生了路径修改 "
                    + "之前路径 ：" + preFileInfo.getLastPath()
                    + "   现在路径 : " + lastFileInfo.getLastPath() + "\n");
            fileWriter.close();
        }
    }
    /*输出size-changed*/
    public synchronized void printSizeChanged(FileInfo preFileInfo, FileInfo lastFileInfo) throws IOException{
        FileWriter fileWriter = new FileWriter(file, true);
        if(triggers.contains("size-changed")){
            /*输出到控制台*/
            System.out.println(preFileInfo.getLastPath() + " 发生了大小变化 "
                    + "之前大小 ：" + preFileInfo.getLastSpace()
                    + "   现在大小 : " + lastFileInfo.getLastSpace());
            /*输出到文件*/
            fileWriter.write(preFileInfo.getParentPath() + " 里发生了大小变化 "
                    + "之前大小 ：" + preFileInfo.getLastSpace()
                    + "   现在大小 : " + lastFileInfo.getLastSpace() + "\n");
            fileWriter.close();
        }
    }
    /*输出新建或删除文件*/
    public synchronized void printDeleteOrNew(FileInfo preFileInfo, FileInfo lastFileInfo) throws IOException{
        FileWriter fileWriter = new FileWriter(file, true);
        if(triggers.contains("size-changed")){
            /*输出到控制台*/
            System.out.println(preFileInfo.getLastPath() + " 发生了大小变化 "
                    + "之前大小 ：" + preFileInfo.getLastSpace()
                    + "   现在大小 : " + lastFileInfo.getLastSpace());
            /*输出到文件*/
            fileWriter.write(preFileInfo.getLastPath() + " 发生了大小变化 "
                    + "之前大小 ：" + preFileInfo.getLastSpace()
                    + "   现在大小 : " + lastFileInfo.getLastSpace() + "\n");
            fileWriter.close();
        }
    }
    /*输出删除文件*/
    public synchronized void printDelete(FileInfo preFileInfo) throws IOException{
        FileWriter fileWriter = new FileWriter(file, true);
        if(triggers.contains("size-changed")){
            /*输出到控制台*/
            System.out.println(preFileInfo.getLastPath() + " 发生了删除文件的大小变化 "
                    + "之前大小 ：" + preFileInfo.getLastSpace()
                    + "   现在大小 : " + 0);
            /*输出到文件*/
            fileWriter.write(preFileInfo.getLastPath() + " 发生了删除文件大小变化 "
                    + "之前大小 ：" + preFileInfo.getLastSpace()
                    + "   现在大小 : " + 0 + "\n");
            fileWriter.close();
        }
    }
    /*输出新建文件*/
    public synchronized void printNew(FileInfo lastFileInfo) throws IOException{
        FileWriter fileWriter = new FileWriter(file, true);
        if(triggers.contains("size-changed")){
            /*输出到控制台*/
            System.out.println(lastFileInfo.getLastPath() + " 发生了新建文件大小变化 "
                    + "之前大小 ：" + 0
                    + "   现在大小 : " + lastFileInfo.getLastSpace());
            /*输出到文件*/
            fileWriter.write(lastFileInfo.getLastPath() + " 发生了新建文件大小变化 "
                    + "之前大小 ：" + 0
                    + "   现在大小 : " + lastFileInfo.getLastSpace() + "\n");
            fileWriter.close();
        }
    }
    /*追加输出到文件*/
    public synchronized void printRecordToFile() throws IOException {
        File file = new File(detailPath + "_record-detail.txt");
        FileWriter fileWriter = new FileWriter(file, true);
        for(String record : records){
            fileWriter.write(record);
        }
        fileWriter.close();
        records.clear();
    }
    /*定时输出到文件*/
    public void timerPrint(){
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                try {
                    printRecordToFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        timer.schedule(timerTask, 10000);/*每十秒输出到文件一次*/
    }
}
