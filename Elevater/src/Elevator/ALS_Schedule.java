package Elevator;

public class ALS_Schedule extends Scheduler{
    private double arriveTime; //接下来一个乘客到达目标楼层的时间

    public double getArriveTime(){
        //Requires: none
        //Modified: none
        //Effects: return arriveTime
        return arriveTime;
    }

    public void setArriveTime(Elevator elevator) {
        //Requires: elevator != null
        //Modified: arriveTime
        //Effects: set the arriveTime to the elevator's time
        arriveTime = elevator.getTime();
    }

    public boolean ifCanCarry(Elevator elevator, Asking nextAsking) {
        //Requires: elevator != null, nextAsking != null
        //Modified: System.out, carryRequests
        //Effects: judge if the elevator can carry this asking, if can carry, return true, else return false
        if (nextAsking.getAskingFloorNumber() >= elevator.getCurrentFloor()) {
            double arriveCarryFloorTime = (nextAsking.getAskingFloorNumber() - elevator.getCurrentFloor()) * 0.5;
            if ((elevator.getCurrentTime() + arriveCarryFloorTime) >= nextAsking.getAskingTime()) {
                //当前时间加上到达下一个乘客所在楼层的时间不小于该乘客的请求时间，则可以捎带
                return true;
            }
        }
        return false;
    }
}
