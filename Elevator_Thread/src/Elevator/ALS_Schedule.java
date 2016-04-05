package Elevator;

import com.sun.javafx.binding.SelectBinding;

import java.util.Vector;

public class ALS_Schedule extends Scheduler implements Runnable{
    private volatile Vector<Asking> askings = new Vector<>();
    private Asking asking = new Asking();
    private thread_Elevator[] thread_elevators = new thread_Elevator[3];

    public thread_Elevator[] getThread_elevators(){
        return thread_elevators;
    }

    public void setAsking(Asking asking){
        this.asking = asking;
    }
    //加入请求托盘
    public void setAskings(Asking asking){
        askings.add(asking);
    }
    public Asking getAsking(int i){
        return askings.get(i);
    }
    /*private int m_arriveTime; //接下来一个乘客到达目标楼层的时间

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
    }*/

    //将请求分配给三个电梯
    public boolean distribute(Asking asking, thread_Elevator[] thread_elevators){
        //System.out.println("dis");
        if(asking.getM_entryState() == EntryState.ER ){
            thread_elevators[asking.getM_askingElevator()-1].addAskingQueue(asking);//进入所选择的电梯
            //thread_elevators[asking.getM_askingElevator()-1].getM_carryRequests().add(asking);
            //System.out.println("er add in");
            return true;
        }
        else {
            int whichElevatorToAdd = 0;
            int sumFlag = thread_elevators[0].getAmountOfExercise();
            boolean carryFlag = false;
            for (int i = 0; i < thread_elevators.length; i++) {
                if (thread_elevators[i].getElevatorState() == asking.getM_elevatorState()) {
                    //如果请求方向与电梯方向相同
                    if((asking.getM_elevatorState() == ElevatorState.UP
                            && thread_elevators[i].getM_primaryFloor() >= asking.getM_askingFloorNumber())
                        || (asking.getM_elevatorState() == ElevatorState.DOWN
                            && thread_elevators[i].getM_primaryFloor() <= asking.getM_askingFloorNumber())){
                        //
                        if(!carryFlag) {
                            //System.out.println("carryFlag");
                            whichElevatorToAdd = i;
                            sumFlag = thread_elevators[i].getAmountOfExercise();
                            carryFlag = true;
                        }
                        if(sumFlag > thread_elevators[i].getAmountOfExercise()){
                            whichElevatorToAdd = i;
                            sumFlag = thread_elevators[whichElevatorToAdd].getAmountOfExercise();
                        }
                    }
                }
            }
            if(carryFlag){
                //如果可以捎带则选择运动量最小的电梯加入
                //System.out.println("fr add in");
                thread_elevators[whichElevatorToAdd].addAskingQueue(asking);
                //System.out.println("fr add in to elevator " + (whichElevatorToAdd + 1));
                return true;
            }
        }

        //从多个静止电梯中选择运动量最小的
        int whichElevatorToAdd = -1;
        int sumFlag = Integer.MAX_VALUE;
        boolean addFlag = false;
        for(int i = 0; i < thread_elevators.length; i++){
            //System.out.println("WTF");
            if(thread_elevators[i].getAmountOfExercise() < sumFlag && thread_elevators[i].getElevatorState() == ElevatorState.STABLE) {
                whichElevatorToAdd = i;
                sumFlag = thread_elevators[i].getAmountOfExercise();
                addFlag = true;
            }
        }
        //System.out.println(addFlag);
        if(addFlag){
            //System.out.println("min add in");
            thread_elevators[whichElevatorToAdd].addAskingQueue(asking);
            thread_elevators[whichElevatorToAdd].setElevatorState(asking.getM_askingFloorNumber());
            //System.out.println("move add in to elevator " + (whichElevatorToAdd+1));
            return true;
        }
        return false;
    }

    @Override
    public void run(){
        System.out.println("als is running");
        while(true) {
            //synchronized (askings) {
                for (int i = 0; i < askings.size(); i++) {
                    //System.out.println(distribute(getAsking(i), thread_elevators));
                    if (getAsking(i) != null && distribute(getAsking(i), thread_elevators)) {
                        //System.out.println(getAsking(i).toString());
                        askings.set(i, null);//分配成功从托盘删除
                    }
                }
            //}
        }
        /*if(asking != null) {
            System.out.println("data in");
            distribute(asking, thread_elevators);
            asking = null;
        }*/
    }
}
