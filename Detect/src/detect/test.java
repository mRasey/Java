package detect;

import java.io.IOException;

public class test implements Runnable{
    public test(){

    }
    @Override
    public void run() {
        Operate.makeNewFileFolder("D:\\789\\123");
        for (int i = 0; i < 200; i++) {
            try {
                Operate.makeNewFile("D:\\789\\" + i + ".txt");
            } catch (IOException e) {
                //e.printStackTrace();
            }
        }
        try {
            Operate.writeFile("D:\\789\\2.txt", "hello world");
        } catch (IOException e) {
            //e.printStackTrace();
        }
        Operate.renameFile("D:\\789\\2.txt", "D:\\789\\11.txt");
        Operate.deleteFile("D:\\789\\5.txt");
        Operate.moveFile("D:\\789\\4.txt", "D:\\789\\123\\4.txt");
        System.out.println(Operate.getFileName("D:\\789\\123\\4.txt"));
        System.out.println(Operate.getLength("D:\\789\\1.txt"));
        System.out.println(Operate.getModifiedTime("D:\\789\\2.txt"));
    }
}
