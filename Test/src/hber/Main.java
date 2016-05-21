package hber;


import java.util.Random;

/**
 * Created by cc on 2016/4/18.
 */
public class Main {
    public static void main(String [] args){
        ControlCenter controlCenter = new ControlCenter();
        PassengerQueue passengerQueue = new PassengerQueue();
        Random random = new Random();
        passengerQueue.start();
//        ControlCenter.connect(79,80,80,80);
//        ControlCenter.disconnect(2,2,3,2);
        for(int i=0;i<1;i++){
            try {
                if(!controlCenter.IdleCarSet.isEmpty()){
                    passengerQueue.enQueue(new Passenger(22,22,35,35));
                }
            } catch (Exception e) {
            }
        }
    }
}
