package Elevator;

/**
 * Created by Billy on 2016/3/14.
 */
public class ALS_Schedule extends Scheduler{
    private int m_arriveTime; //接下来一个乘客到达目标楼层的时间

    public int getM_arriveTime(){
        return m_arriveTime;
    }

    public void setM_arriveTime(Elevator elevator){
        elevator.getM_time();
    }

    public boolean ifCanCarry(Elevator elevator, Asking nextAsking){
        if(nextAsking.getM_askingFloorNumber() >= elevator.getCurrentFloor()) {
            double arriveCarryFloorTime = (nextAsking.getM_askingFloorNumber() - elevator.getCurrentFloor()) * 0.5;
            if((elevator.getM_currentTime() + arriveCarryFloorTime) >= nextAsking.getM_askingTime()){
                //当前时间加上到达下一个乘客所在楼层的时间不小于该乘客的请求时间，则可以捎带
                return true;
            }
        }
        return false;
    }
}
