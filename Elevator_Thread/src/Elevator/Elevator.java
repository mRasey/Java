package Elevator;

import com.sun.javafx.binding.BidirectionalBinding;
import com.sun.javafx.binding.SelectBinding;

import java.nio.channels.CancelledKeyException;
import java.security.PublicKey;
import java.util.TreeMap;
import java.util.Vector;

enum ElevatorState{
    UP,//上升
    DOWN,//下降
    STABLE//稳定
}

public class Elevator implements Move{
    private int m_currentFloor = 1;
    private double m_currentTime = 0;//电梯当前时间
    private double m_time = 0;//电梯到达目标楼层时间
    private ElevatorState elevatorState = ElevatorState.STABLE;
    private boolean[] m_ifStay = new boolean[10];
    private int m_primaryFloor = 0;
    private Vector<Asking> m_carryRequests = new Vector<>();
    private int amountOfExercise = 0;
    //构造方法
    public Elevator(){
        for(int i = 0; i < 10; i++){
            m_ifStay[i] = false;
        }
    }
    //电梯每到一层就遍历整个队列，如果有在电梯运动方向上的且在时间范围内的就记录下来
    public void traverseFloors(AskQueue askQueue){
        if(elevatorState == ElevatorState.UP) {
            //for (int i = m_currentFloor-1; i < 10; i++) {
                for (int j = 0; j < askQueue.getM_askingQueue().size(); j++) {
                    Asking asking = askQueue.getM_askingQueue().get(j);
                    if (asking != null && asking.getM_askingTime() < m_time
                            && asking.getM_askingFloorNumber() >= m_currentFloor) {
                        if(asking.getM_entryState() == EntryState.ER) {
                            //如果是在电梯内部的指令就直接响应
                            m_ifStay[asking.getM_askingFloorNumber()-1] = true;
                            askQueue.getM_askingQueue().set(j, null);
                            addCarryRequests(asking);//将捎带请求加入队列
                        }
                        else{
                            //如果是电梯外部的指令
                            if(asking.getM_elevatorState() == elevatorState
                                    && asking.getM_askingFloorNumber() <= m_primaryFloor){
                                //如果是在电梯的运动方向上且楼层数不大于主请求的楼层
                                m_ifStay[asking.getM_askingFloorNumber()-1] = true;
                                askQueue.getM_askingQueue().set(j, null);
                                addCarryRequests(asking);//将捎带请求加入队列
                            }
                        }
                    }
                }
            }
        //}
        else if(elevatorState == ElevatorState.DOWN){
            //for (int i = m_currentFloor-1; i > 0; i--) {
                for (int j = 0; j < askQueue.getM_askingQueue().size(); j++) {
                    Asking asking = askQueue.getM_askingQueue().get(j);
                    if (asking != null && asking.getM_askingTime() < m_time
                            && asking.getM_askingFloorNumber() <= m_currentFloor){
                        if(asking.getM_entryState() == EntryState.ER) {
                            //如果是在电梯内部的指令就直接响应
                            m_ifStay[asking.getM_askingFloorNumber()-1] = true;
                            askQueue.getM_askingQueue().set(j, null);
                            addCarryRequests(asking);//将捎带请求加入队列
                        }
                        else{
                            //如果是电梯外部的请求
                            if(asking.getM_elevatorState() == elevatorState
                                    && asking.getM_askingFloorNumber() >= m_primaryFloor){
                                //如果是在电梯的运动方向上并且不小于主请求的楼层
                                m_ifStay[asking.getM_askingFloorNumber()-1] = true;
                                askQueue.getM_askingQueue().set(j, null);
                                addCarryRequests(asking);//将捎带请求加入队列
                            }
                        }
                    }
                }
            //}
        }
        /*else{
            for(int j = 0; j < askQueue.getM_askingQueue().size(); j++) {
                Asking asking = askQueue.getM_askingQueue().get(j);
                if(asking != null && asking.getM_askingTime() <= m_time) {
                    //m_ifStay[m_currentFloor-1] = true;
                    askQueue.getM_askingQueue().set(j, null);
                }
            }
        }*/
    }
    //遍历捎带请求队列
    public void traverseCarryFloors(Vector<Asking> m_carryRequests){
        for(int i = 0; i < m_carryRequests.size(); i++){
            if(m_carryRequests.get(i).getM_askingFloorNumber() == m_currentFloor){
                m_carryRequests.set(i, null);
            }
        }
    }
    //将捎带请求加入捎带请求队列
    public boolean addCarryRequests(Asking asking){
        for(int i = 0; i < m_carryRequests.size(); i++){
            if(asking.equals(m_carryRequests.get(i)))
                return false;
        }
        m_carryRequests.add(asking);
        return true;
    }
    public boolean setM_ifStay(int i){
        return m_ifStay[i];
    }
    //判断是否还有楼层没有响应
    public boolean ifStillHaveTrueFloor(){
        //System.out.println("233");
        for(int i = 0; i < 20; i++){
            if(m_ifStay[i])
                return true;
        }
        return false;
    }
    //电梯刚刚从静止状态开始运动,取整个队列中最近的请求
    public void starToMove(AskQueue askQueue, int i){
        //int i;
        /*for(i = 0; i < askQueue.getM_askingQueue().size(); i++){
            if(askQueue.getM_askingQueue().get(i) != null)
                break;
        }*/
        Asking asking = askQueue.getM_askingQueue().get(i);
        m_primaryFloor = asking.getM_askingFloorNumber();
        m_carryRequests.add(asking);//将主请求加入队首
        if(asking.getM_askingTime() > m_time)//当请求时电梯已经停止运行
            m_time = asking.getM_askingTime();
        m_ifStay[asking.getM_askingFloorNumber()-1] = true;
        setElevatorState(asking.getM_askingFloorNumber());//设置电梯运动方向

        if(elevatorState == ElevatorState.STABLE){
            //如果电梯处于稳定状态
            m_ifStay[asking.getM_askingFloorNumber()-1] = false;
            for(int j = 0; j < askQueue.getM_askingQueue().size(); j++) {
                asking = askQueue.getM_askingQueue().get(j);
                if(asking != null && asking.getM_askingTime() <= m_time && asking.getM_askingFloorNumber() == m_currentFloor) {
                    //m_ifStay[m_currentFloor-1] = true;
                    askQueue.getM_askingQueue().set(j, null);
                }
            }
            printElevatorState();
        }

    }
    //电梯一层一层地运动
    public void moveStepByStep(AskQueue askQueue){
        amountOfExercise++;
        if(elevatorState == ElevatorState.UP) {
            m_currentFloor++;
            m_time += 3;
            traverseFloors(askQueue);
            if (m_ifStay[m_currentFloor-1] == true) {
                printElevatorState();
                m_ifStay[m_currentFloor-1] = false;//过了该层之后变成false
            }
        }
        else if(elevatorState == ElevatorState.DOWN){
            m_currentFloor--;
            m_time += 3;
            traverseFloors(askQueue);
            if (m_ifStay[m_currentFloor - 1] == true) {
                printElevatorState();
                m_ifStay[m_currentFloor - 1] = false;//过了该层之后变成false
            }
        }
    }
    public void printElevatorState() {
        //电梯去目的地
        System.out.println(toString());
        m_time += 6;//开关门附加时间
    }
    //重载toString()
    @Override
    public String toString(){
        return "(" + m_currentFloor + "," + elevatorState + "," + m_time + ")";
    }
    //输出捎带请求队列
    public void printCarryRequests(){
        boolean flag1 = false;
        boolean flag = true;
        for(int i = 0; i < m_carryRequests.size(); i++){
            Asking asking = m_carryRequests.get(i);
            if(asking != null) {
                flag1 = true;
                if (asking.getM_entryState() == EntryState.FR) {
                    if(flag){
                        System.out.print("捎带队列：");
                        System.out.print("(" + asking.getM_entryState() + ","
                                + asking.getM_askingFloorNumber() + ","
                                + asking.getM_elevatorState() + ","
                                + asking.getM_askingTime() + ")(");
                        flag = false;
                        m_carryRequests.set(i,null);
                    }
                    else {
                        System.out.print("(" + asking.getM_entryState() + ","
                                + asking.getM_askingFloorNumber() + ","
                                + asking.getM_elevatorState() + ","
                                + asking.getM_askingTime() + ")");
                    }
                } else {
                    if(flag){
                        System.out.print("捎带队列：");
                        System.out.print("(" + asking.getM_entryState() + ","
                                + asking.getM_askingFloorNumber() + ","
                                + asking.getM_askingTime() + ")(");
                        flag = false;
                        m_carryRequests.set(i,null);
                    }
                    else {
                        System.out.print("(" + asking.getM_entryState() + ","
                                + asking.getM_askingFloorNumber() + ","
                                + asking.getM_askingTime() + ")");
                    }
                }
            }
        }
        //m_carryRequests.remove(0);//去除老的主请求
        if(flag1)
            System.out.println(")");
    }
    //重新设定捎带队列,将最近的一个未完成捎带请求变成主请求
    public boolean rebuildCarryRequesets(){
        for(int i = 0; i < m_carryRequests.size(); i++){
            Asking asking = m_carryRequests.get(i);
            if(asking != null && asking.getM_askingFloorNumber() <= m_primaryFloor){
                m_carryRequests.set(i, null);
            }
        }
        for(int i = 0; i < m_carryRequests.size(); i++) {
            if(m_carryRequests.get(i) != null) {
                m_primaryFloor = m_carryRequests.get(i).getM_askingFloorNumber();
                return true;
            }
        }
        return  false;
    }
    public double getM_time(){
        return m_time;
    }
    public int getCurrentFloor(){
        return m_currentFloor;
    }
    public double getM_currentTime(){
        return  m_currentTime;
    }
    //设置电梯运动状态
    public void setElevatorState(int nextFloor){
        if(nextFloor > m_currentFloor)
            elevatorState = ElevatorState.UP;
        else if(nextFloor < m_currentFloor)
            elevatorState = ElevatorState.DOWN;
        else
            elevatorState = ElevatorState.STABLE;
    }
    //获得主请求的楼层数
    public int getM_primaryFloor(){
        return m_primaryFloor;
    }
    public void setM_primaryFloor(int primaryFloor){
        m_primaryFloor = primaryFloor;
    }
    //获得捎带请求队列
    public Vector<Asking> getM_carryRequests(){
        return m_carryRequests;
    }
    //获得运动量
    public int getAmountOfExercise(){
        return amountOfExercise;
    }
    //获得电梯运动状态
    public ElevatorState getElevatorState(){
        return elevatorState;
    }
}

