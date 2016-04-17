package detect;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Summary {
    private static int countRenamed;
    private static int countModified;
    private static int countPathChanged;
    private static int countSizeChanged;
    private ArrayList<String> triggers;
    private ArrayList<String> missions;
    private String summaryPath;
    private int detecterNumber;

    public Summary(ArrayList<String> trigger, ArrayList<String> missions, String summaryPath, int detecterNumber){
        this.triggers = trigger;
        this.summaryPath = summaryPath;
        this.missions = missions;
        this.detecterNumber = detecterNumber;
    }
    /*设置次数*/
    public void setCount(String trigger){
        if(missions.contains("record-summary")) {
            if (trigger.equals("renamed"))
                countRenamed++;
            if (trigger.equals("modified"))
                countModified++;
            if (trigger.equals("path-changed"))
                countPathChanged++;
            if (trigger.equals("size-changed"))
                countSizeChanged++;
        }
    }
    /*输出次数信息到控制台*/
    public void printCountToConsole(){
        if(triggers.contains("renamed"))
            System.out.println("renamed : " + countRenamed + " 次\n");
        if (triggers.contains("modified"))
            System.out.println("modified : " + countModified + " 次\n");
        if(triggers.contains("path-changed"))
            System.out.println("path-changed : " + countPathChanged + " 次\n");
        if(triggers.contains("size-changed"))
            System.out.println("size-changed : " + countSizeChanged + " 次\n");
    }
    /*输出次数信息到文件*/
    public synchronized void printCountToFile() throws IOException {
        if(missions.contains("record-summary")) {
            File file = new File("D:\\record-summary.txt");/*记录到record-summary文件*/
            FileWriter fileWriter = new FileWriter(file);
            if (triggers.contains("renamed"))
                fileWriter.write("renamed : " + countRenamed + " 次\n");
            if (triggers.contains("modified"))
                fileWriter.write("modified : " + countModified + " 次\n");
            if (triggers.contains("path-changed"))
                fileWriter.write("path-changed : " + countPathChanged + " 次\n");
            if (triggers.contains("size-changed"))
                fileWriter.write("size-changed : " + countSizeChanged + " 次\n");
            fileWriter.close();
        }
    }
    /*定时输出程序*/
    public void timerPrint(){
        final Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                try {
                    printCountToFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        timer.schedule(timerTask, 1000);/*每秒钟输出到文件一次*/
    }
}
