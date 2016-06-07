package Elevator;

public class Scheduler {
    /**
     * OVERVIEW:
     * This class is a schedule which used to manager the elevator.
     * nextFloor means the floor which was asked by the next asking,
     * nextTime means the time which was asked by the next asking.
     *
     * 表示对象:
     * nextFloor, nextTime
     *
     * 抽象函数:
     * AF(c) = (nextFloor, nextTime) where nextFloor = c.nextFloor, nextTime = nextTime
     *
     * 不变式:
     * 1 <= nextFloor <= 10 && nextTime >= 0
     */

    private int nextFloor;
    private int nextTime;

    public boolean repOK(){
        if (nextFloor <= 0 
        		|| nextFloor > 10) 
        	return false;
        if(nextTime < 0) return false;
        return true;
    }

    public int getNextFloor() {
        //Requires: none
        //Modified: none
        //Effects: return nextFloor
        return nextFloor;
    }

    public int getNextTime() {
        //Requires: none
        //Modified: none
        //Effects: return nextTime
        return nextTime;
    }

    public void setNextFloor(int nextFloor) {
        //Requires: nextFloor ∈ [1,10]
        //Modified: this.nextFloor
        //Effects: set this.nextFloor to nextFloor
        this.nextFloor = nextFloor;
    }

    public void setNextTime(int nextTime) {
        //Requires: nextTime >= 0
        //Modified: this.nextTime
        //Effects: set this.nextTime to nextTime
        this.nextTime = nextTime;
    }

}

