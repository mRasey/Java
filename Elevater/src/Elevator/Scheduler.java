package Elevator;

/**
 * Created by billy on 16-3-7.
 */
public class Scheduler {
    private int m_nextFloor;
    private int m_nextTime;

    public int getM_nextFloor(){
        return m_nextFloor;
    }

    public void setM_nextFloor(int nextFloor){
        m_nextFloor = nextFloor;
    }

    public void setM_nextTime(int nextTime){
        m_nextTime = nextTime;
    }

    public int getM_nextTime(){
        return m_nextTime;
    }
}

