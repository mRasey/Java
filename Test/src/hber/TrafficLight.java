package hber;

/**
 * Created by cc on 2016/5/11.
 */
public class TrafficLight extends Thread{

    private enum Status{Green,Red};
    private Status HorizontalStatus;
    private Status VerticalStatus;
    private boolean click;
    public synchronized String getStatus(String s)
    {
        if(s.equals("Horizontal"))
            return HorizontalStatus.toString();
        return VerticalStatus.toString();
    }
    public TrafficLight(){
        HorizontalStatus = Status.Green;
        VerticalStatus = Status.Red;
        click = false;
    }
    public void run()
    {
        click = true;
        while(click){
            try {
                if(HorizontalStatus.equals(Status.Green))
                {
                    HorizontalStatus = Status.Red;
                    VerticalStatus = Status.Green;
                    Thread.sleep(300);
                }
                else
                {
                    HorizontalStatus = Status.Green;
                    VerticalStatus = Status.Red;
                    Thread.sleep(300);
                }
            } catch (InterruptedException e) {
            e.printStackTrace();
        }
        }
    }
}
