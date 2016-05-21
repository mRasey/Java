package hber;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Created by cc on 2016/4/20.
 */
public class PassengerQueue extends Thread {
    private BlockingDeque<Passenger>  PassengerQueue = new LinkedBlockingDeque<>(300);
    private static ExecutorService fixedThreadPool = Executors.newFixedThreadPool(300);
    private boolean click = false;
    public  void enQueue(Passenger passenger)
        /*
        * Requires : City has been initalized.
        * Modified : None
        * Effects : get the connectivity of two adjacent spots
        * */
    {
        try {
            PassengerQueue.put(passenger);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void close()
    /*
        * Requires : None
        * Modified : None
        * Effects : get the connectivity of two adjacent spots
     * */
    {
        click =false;
    }
    public void run(){
        click = true;
        while(click){
            if(!PassengerQueue.isEmpty()){
                try {
                    fixedThreadPool.execute(PassengerQueue.take());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }else{
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
