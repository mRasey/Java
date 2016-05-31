package Elevator;

public class Scheduler {
    private int nextFloor;
    private int nextTime;

    public int getNextFloor(){
        return nextFloor;
    }

    public void setNextFloor(int nextFloor){
        this.nextFloor = nextFloor;
    }

    public void setNextTime(int nextTime){
        this.nextTime = nextTime;
    }

    public int getNextTime(){
        return nextTime;
    }
}

