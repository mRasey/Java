package detect;

import java.io.IOException;

public class test implements Runnable{
    public test(){

    }
    @Override
    public void run() {
        try {
            Operate.writeFile("D:\\oo7\\hello.txt", "asd");
        } catch (IOException e1) {
// TODO Auto-generated catch block
            e1.printStackTrace();
        }
        Operate.deleteFile("D:\\oo7\\hello.txt");
    }
}
