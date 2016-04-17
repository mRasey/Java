package Elevator;

import java.util.ArrayList;
import java.util.Vector;

public class thread_Elevator extends Elevator implements Runnable{
    private ArrayList<Asking> elevatorAskingQueue = new ArrayList<>();
    //AskQueue finishAskQueue = new AskQueue();
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

    public Vector<Asking> removeNull(Vector<Asking> askings){
        for (int i = 0; i < askings.size(); i++) {
            Asking asking = askings.get(i);
            /*if(asking != null){
                System.out.println("askQ : " + asking.toString());
            }*/
            if (asking == null) {
                askQueue.getM_askingQueue().remove(asking);
                i--;
            }
        }
        return askings;
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

    public void setElevatorState(ElevatorState elevatorState){
        this.elevatorState = elevatorState;
    }

    @Override
    public void run() {
        try {
            boolean deleteFlag = false;
            //System.out.println("elevator " + getElevatorNumber() + " is running");
            long last = System.currentTimeMillis();
            long now = System.currentTimeMillis();
            while (true) {
                //try{Thread.sleep(1);}catch (InterruptedException i){}
                //synchronized (getM_carryRequests()) {
                    //System.out.println("true in");
                    if (!askQueue.getM_askingQueue().isEmpty()) {
                        //System.out.println("thread data in");
                        for (int i = 0; i < askQueue.getM_askingQueue().size(); i++) {
                            Asking asking = askQueue.getM_askingQueue().get(i);
                        /*if(asking != null){
                            System.out.println("askQ : " + asking.toString());
                        }*/
                            if (asking == null) {
                                askQueue.getM_askingQueue().remove(asking);
                                i--;
                            }
                        }
                        if(askQueue.getM_askingQueue().size() != 0) {
                            starToMove(askQueue, 0);
                            //System.out.println("start");
                            do {
                                //System.out.println("into do while");
                                //System.out.println("primary floor : " + getM_primaryFloor());
                                //System.out.println("current floor : " + getCurrentFloor());
                                while (getCurrentFloor() != getM_primaryFloor()) {
                                    //还没有到主请求楼层，则继续移动
                                    //traverseFloors(askQueue);
                                    moveStepByStep(askQueue);
                                    for (int i = 0; i < askQueue.getM_askingQueue().size(); i++) {
                                        if (askQueue.getM_askingQueue().get(i) != null
                                                && askQueue.getM_askingQueue().get(i).getM_askingFloorNumber() == m_currentFloor)
                                            askQueue.getM_askingQueue().set(i, null);
                                    }
                                }
                                //elevator.rebuildCarryRequesets();
                            } while (rebuildCarryRequesets() && ifStillHaveTrueFloor());//当重建捎带序列成功,继续循环
                            //System.out.println("输出：" + toString());
                            //printElevatorState();
                            //askQueue.getM_askingQueue().removeAllElements();
                            finishAskings.removeAllElements();
                            getM_carryRequests().removeAllElements();
                        }
                    } else {
                        initElevatorState();//将电梯运动状态初始化
                    }
               // }
            }
        }
        catch (Throwable t){
            System.out.println("电梯 " + elevatorNumber + " 发生故障");
            t.printStackTrace();
        }
        finally {
            System.out.println("电梯 " + elevatorNumber + " 停止运行");
            System.out.println("程序结束");
            System.exit(0);
        }
    }


}
