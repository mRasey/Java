package oop2;

import java.io.File;
import java.io.FileWriter;

/**
 * Created by cc on 2016/4/13.
 */
public class Main {
    public static void main(String[] argv) throws Exception {
        InputHandler ih = new InputHandler();
        //文件修改区域上边界，END之后才可以走完上一句哦
        FileManipulate.WriteToFile("D:\\test2\\4.txt", "asdfcvfpk",true);
        //文件修改区域下边界，下面的shutdown去掉的话就一直都在监测啦，如果不想让监测过程立刻结束，可以注释掉那句哦
        Thread.sleep(20000);
        InputHandler.ShutDown();
    }
}
