package Elevator;

import java.util.ArrayList;

public class thread_Elevator extends Elevator implements Runnable{
    ArrayList<Asking> elevatorAskingQueue = new ArrayList<>();
    AskQueue askQueue = new AskQueue();
    private int elevatorNumber;

    public thread_Elevator(int elevatorNumber){
        this.elevatorNumber = elevatorNumber;
    }
    public void addAskingQueue(Asking asking){
        askQueue.addAskingQueue(asking);
    }

    public int getElevatorNumber(){
        return elevatorNumber;
    }

    @Override
    public String toString(){
        return "(#" + elevatorNumber + ",#" + getCurrentFloor() + ","
                + getElevatorState() + "," + getAmountOfExercise() + ","
                + getM_time() + ")";
    }
    //@Override
    /*public boolean rebuildCarryRequesets(){
        for(int i = 0; i < getM_carryRequests().size(); i++){
            Asking asking = getM_carryRequests().get(i);
            if(asking != null && asking.getM_askingFloorNumber() <= getM_primaryFloor()){
                getM_carryRequests().set(i, null);
            }
        }
        for(int i = 0; i < getM_carryRequests().size(); i++) {
            if(getM_carryRequests().get(i) != null) {
                setM_primaryFloor(getM_carryRequests().get(i).getM_askingFloorNumber());
                return true;
            }
        }
        return  false;
    }*/

    @Override
    public void run() {
        boolean deleteFlag = false;
        System.out.println("elevator " + getElevatorNumber() + " is running");
        long last = System.currentTimeMillis();
        long now = System.currentTimeMillis();
        while(true) {
            //System.out.println("true in");
            if (!askQueue.getM_askingQueue().isEmpty()) {
                //System.out.println("data in");
                do{
                    for (Asking asking : askQueue.getM_askingQueue()) {
                        if (asking == null) {
                            askQueue.getM_askingQueue().remove(asking);
                            deleteFlag = true;
                            break;
                        }
                        else
                            deleteFlag = false;
                    }
                }while(deleteFlag);
                starToMove(askQueue, 0);
                do {
                    while (getCurrentFloor() != getM_primaryFloor()) {
                        //还没有到主请求楼层，则继续移动
                        moveStepByStep(askQueue);
                    }
                    //printCarryRequests();//输出捎带序列
                    //elevator.rebuildCarryRequesets();
                } while (rebuildCarryRequesets() && ifStillHaveTrueFloor());//当重建捎带序列成功,继续循环
                //System.out.println("输出：" + toString());
                //printElevatorState();
                askQueue.getM_askingQueue().removeAllElements();
                //break;
            }
            /*else {
                now = System.currentTimeMillis();
                if(now - last > 5000) {
                    System.out.println("data out");
                    last = now;
                }
            }*/
        }
    }


}
