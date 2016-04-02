package Elevator;

import java.util.*;

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

    public long getNextAskingTime(){
        return m_askingQueue.get(m_current).getM_askingTime();
    }

    public void moveCurrentVectorOneStep(){
        m_current++;
    }

    public boolean addAskingQueue(Asking asking){
        /*System.out.println(asking.getM_entryState());
        System.out.println(asking.getM_askingFloorNumber());
        System.out.println(asking.getM_elevatorState());*/

        if(1 <= asking.getM_askingFloorNumber() && asking.getM_askingFloorNumber() <= 20) {
            if(asking.getM_entryState() == EntryState.ER)
                m_askingQueue.add(asking);
            else if(asking.getM_entryState() == EntryState.FR && asking.getM_askingFloorNumber() <= 19 && asking.getM_elevatorState() == ElevatorState.UP)
                m_askingQueue.add(asking);
            else if(asking.getM_entryState() == EntryState.FR && asking.getM_askingFloorNumber() >= 2 && asking.getM_elevatorState() == ElevatorState.DOWN)
                m_askingQueue.add(asking);
            else {
                System.out.println("输入无效");
                return false;
            }
            return true;
        }
        else {
            System.out.println("输入无效");
            return false;
        }
    }

    public Vector<Asking> getM_askingQueue(){
        return m_askingQueue;
    }

    public void removeSameAsking(){
        //去除时间相同的请求
        long currentTime = m_askingQueue.get(0).getM_askingTime();
        for(int i = 1; i < m_askingQueue.size(); i++){
            if(currentTime == m_askingQueue.get(i).getM_askingTime())
                m_askingQueue.remove(i);
            else
                currentTime = m_askingQueue.get(0).getM_askingTime();
        }
    }
}

