package max;

import java.util.TimerTask;

public class Print extends TimerTask{
    MaxClique maxClique;

    public Print(MaxClique maxClique) {
        this.maxClique = maxClique;
    }

    @Override
    public void run(){
        try {
            if(maxClique.flag) {
                System.out.println("the answer is " + maxClique.answer);
                System.exit(0);
            }
            else{
                System.out.print("calculating ");
                Thread.sleep(1000);
                for(int i = 0; i < 100; i++) {
                    System.out.print("|");
                    Thread.sleep(200);
                }
                System.out.println();
            }
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
