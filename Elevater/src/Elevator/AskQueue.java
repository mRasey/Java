package Elevator;

import java.util.*;

/**
 * Created by billy on 16-3-7.
 */
class AskQueueCompare implements Comparator{ //实现Comparator，定义自己的比较方法

    public int compare(Object o1, Object o2) {
        Asking a1 = (Asking)o1;
        Asking a2 = (Asking)o2;

        if(a1.getM_askingTime() > a2.getM_askingTime()){ //这样比较是升序,如果把1改成-1就是降序.
            return 1;
        }
        else if(a1.getM_askingTime() < a2.getM_askingTime()){
            return -1;
        }
        else{
            return 0;
        }
    }
}

public class AskQueue{
    private int m_current = 0;
    private Vector<Asking> m_askingQueue = new Vector<Asking>();
    private boolean firstInput = true;//是否是第一次输入

    /*public Asking getNextAsking(){
        return m_askingQueue.get(m_current++);
    }*/

    public int getNextAskingFloor(){
        return m_askingQueue.get(m_current).getM_askingFloorNumber();
    }

    public int getNextAskingTime(){
        return m_askingQueue.get(m_current).getM_askingTime();
    }

    public void moveCurrentVectorOneStep(){
        m_current++;
    }

    public int addAskingQueue(Asking asking, int currentTime){
        if(currentTime <= asking.getM_askingTime()) {
            //输入时间必须递增
            m_askingQueue.add(asking);
            firstInput = false;
            return asking.getM_askingTime();
        }
        else {
            System.out.println("输入无效，未按照递增时间排序");
            return currentTime;
        }
    }

    public Vector<Asking> getM_askingQueue(){
        return m_askingQueue;
    }

    public void sort(){
        //Comparator cp = new AskQueueCompare();
        Collections.sort(m_askingQueue, new AskQueueCompare());
    }

    public void removeSameAsking(){
        //去除时间相同的请求
        int currentTime = m_askingQueue.get(0).getM_askingTime();
        for(int i = 1; i < m_askingQueue.size(); i++){
            if(currentTime == m_askingQueue.get(i).getM_askingTime())
                m_askingQueue.remove(i);
            else
                currentTime = m_askingQueue.get(0).getM_askingTime();
        }
    }
}

