package Elevator;

public class ALS_Schedule extends Scheduler implements Runnable{
    private Asking asking = new Asking();
    private thread_Elevator[] thread_elevators = new thread_Elevator[3];

    public thread_Elevator[] getThread_elevators(){
        return thread_elevators;
    }

    public void setAsking(Asking asking){
        this.asking = asking;
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
    public void distribute(Asking asking, thread_Elevator[] thread_elevators){
        boolean addFlag = false;
        if(asking.getM_entryState() == EntryState.ER){
            thread_elevators[asking.getM_askingElevator()-1].addAskingQueue(asking);//进入所选择的电梯
            System.out.println("er add in");
            addFlag = true;
        }
        else {
            for (int i = 0; i < thread_elevators.length; i++) {
                if (thread_elevators[i].getElevatorState() == asking.getM_elevatorState()) {
                    if((asking.getM_elevatorState() == ElevatorState.UP
                            && thread_elevators[i].getCurrentFloor() <= asking.getM_askingFloorNumber())
                        || (asking.getM_elevatorState() == ElevatorState.DOWN
                            && thread_elevators[i].getCurrentFloor() >= asking.getM_askingFloorNumber())){
                        //如果可以加入
                        thread_elevators[i].addAskingQueue(asking);
                        System.out.println("fr add in to elevator " + (i + 1));
                        addFlag = true;
                        break;
                    }
                }
            }
        }
        if(!addFlag){
            //如果捎带加入失败，选择运动量最少的加入
            int whichElevatorToAdd = 0;
            int sumFlag = thread_elevators[0].getAmountOfExercise();
            for(int i = 0; i < thread_elevators.length; i++){
                if(thread_elevators[i].getAmountOfExercise() < sumFlag) {
                    whichElevatorToAdd = i;
                    sumFlag = thread_elevators[i].getAmountOfExercise();
                }
            }
            thread_elevators[whichElevatorToAdd].addAskingQueue(asking);
            System.out.println("move add in to elevator " + (whichElevatorToAdd+1));
        }
    }

    @Override
    public void run(){
        System.out.println("als is running");
        //while(true){
        if(asking != null) {
            System.out.println("data in");
            distribute(asking, thread_elevators);
            asking = null;
        }
        //}
    }
}
