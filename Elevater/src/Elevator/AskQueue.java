package Elevator;

import java.util.Vector;

public class AskQueue {
    /**
     * OVERVIEW:
     * This class is a queue which saves the askings, all the askings was saved in askingQueue.
     *
     * 表示对象:
     * askingQueue
     *
     * 抽象函数:
     * AF(c) = (askingQueue) where askingQueue = c.askingQueue
     *
     * 不变式:
     * always true
     */

    private Vector<Asking> askingQueue = new Vector<Asking>();

    public boolean repOK(){
        return true;
    }
/*
    public Asking getNextAsking() {
        //Requires: none
        //Modified: current
        //Effects: return the next asking in askingQueue
        return askingQueue.get(current++);
    }

    public int getNextAskingFloor(){
        //Requires: none
        //Modified: none
        //Effects: return the current
        return askingQueue.get(current).getAskingFloorNumber();
    }

    public int getNextAskingTime(){
        return askingQueue.get(current).getAskingTime();
    }

    public void moveCurrentVectorOneStep(){
        current++;
    }
*/

    public int addAskingQueue(Asking asking, int currentTime) {
        //Requires: asking != null, current >= 0
        //Modified: System.out, askingQueue
        //Effects: if the time of this asking is not smaller than currentTime,
        // then add the asking to askingQueue and return the time, else
        // print the invalid tip and return currentTime
        if (currentTime <= asking.getAskingTime()) {
            //输入时间必须递增
            askingQueue.add(asking);
            return asking.getAskingTime();
        } else {
            System.out.println("输入无效，未按照递增时间排序");
            return currentTime;
        }
    }

    public Vector<Asking> getAskingQueue() {
        //Requires: none
        //Modified: none
        //Effects: return the askingQueue
        return askingQueue;
    }

}

