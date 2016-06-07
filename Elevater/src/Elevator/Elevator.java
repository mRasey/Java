package Elevator;

import java.util.Vector;

enum ElevatorState{
    UP,//上升
    DOWN,//下降
    STABLE//稳定
}

public class Elevator implements Move {
    /**
     * OVERVIEW:
     * This class is about a elevator, it has some methods to run, currentFloor means the floor which
     * the elevator was right now, currentTime means the current time now, time means when the elevator
     * arrived, elevatorState means the current state the elevator is, ifStay records if one floor has
     * askings now, primaryFloor means the floor which was the current main request asked. and carryRequests
     * records the askings which were carried by a main request.
     * <p>
     * 表示对象:
     * currentFloor, currentTime, time, elevatorState, ifStay[], primaryFloor, carryRequests
     * <p>
     * 抽象函数:
     * AF(c) = (currentFloor, currentTime, time, elevatorState, ifStay[], primaryFloor, carryRequests)
     *         where currentFloor = c.currentFloor, currentTime = c.currentTime, time = c.time,
     *         elevatorState = c.elevatorState, ifStay = c.ifStay, primaryFloor = c.primaryFloor,
     *         carryRequests = c.carryRequests
     * <P>
     * 不变式;
     * 1 <= currentFloor <= 10 && time >= 0 && 1 <= primaryFloor <= 10
     */

    int currentFloor = 1;
    private double currentTime = 0;//电梯当前时间
    double time = 0;//电梯到达目标楼层时间
    private ElevatorState elevatorState = ElevatorState.STABLE;
    boolean[] ifStay = new boolean[10];
    int primaryFloor = 0;
    Vector<Asking> carryRequests = new Vector<>();

    public boolean repOK(){
        if(currentFloor <= 0 || currentFloor > 10)
            return false;
        if(time < 0)
            return false;
        if(primaryFloor <= 0)
            return false;
        return true;
    }
    //构造方法
    public Elevator() {
        //Requires: none
        //Modified: none
        //Effects: constructor
        for (int i = 0; i < 10; i++) {
            ifStay[i] = false;
        }
    }

    public double getTime() {
        //Requires: none
        //Modified: none
        //Effects: return time
        return time;
    }

    public int getCurrentFloor() {
        //Requires: none
        //Modified: none
        //Effects: return currentFloor
        return currentFloor;
    }

    public double getCurrentTime() {
        //Requires: none
        //Modified: none
        //Effects: return currentTime
        return currentTime;
    }

    //获得主请求的楼层数
    public int getPrimaryFloor() {
        //Requires: none
        //Modified: none
        //Effects: return primaryFloor
        return primaryFloor;
    }

    //获得捎带请求队列
    public Vector<Asking> getCarryRequests() {
        //Requires: none
        //Modified: none
        //Effects: return carryRequests
        return carryRequests;
    }

    public ElevatorState getElevatorState() {
        //Requires: none
        //Modified: none
        //Effects: return elevatorState
        return elevatorState;
    }

    public boolean[] getIfStay() {
        //Requires: none
        //Modified: none
        //Effects: return ifStay[]
        return ifStay;
    }

    //设置电梯运动状态
    public void setElevatorState(int nextFloor) {
        //Requires: none
        //Modified: elevatorState
        //Effects: compare the value of nextFloor with currentFloor
        // if nextFloor > currentFloor, set ElevatorState with UP
        // if nextFloor < currentFloor, set ElevatorState with DOWN
        // if nextFloor = currentFloor, set ElevatorState with STABLE
        if (nextFloor > currentFloor)
            elevatorState = ElevatorState.UP;
        else if (nextFloor < currentFloor)
            elevatorState = ElevatorState.DOWN;
        else
            elevatorState = ElevatorState.STABLE;
    }

    /*public boolean getIfStay(int i) {
        //Requires: i is within the bond of ifStay
        //Modified: ifStay[]
        //Effects: return ifStay[i]
        return ifStay[i];
    }*/

    //电梯每到一层就遍历整个队列，如果有在电梯运动方向上的且在时间范围内的就记录下来
    public void traverseFloors(AskQueue askQueue) {
        //Requires: AskQueue != null
        //Modified: ifStay[], askQueue
        //Effects: Traverse the askQueue everytime when the elevator arrive a floor,
        // if there are askings which are within the time and has the same
        // direction with the elevator and the elevator can carry them,
        // then add them to carryRequests, else ignore them
        if (elevatorState == ElevatorState.UP) {
            for (int j = 0; j < askQueue.getAskingQueue().size(); j++) {
                Asking asking = askQueue.getAskingQueue().get(j);
                if (asking != null 
                		&& asking.getAskingTime() < time
                        && asking.getAskingFloorNumber() >= currentFloor) {
                    if (asking.getEntryState() == EntryState.ER) {
                        //如果是在电梯内部的指令就直接响应
                        ifStay[asking.getAskingFloorNumber() - 1] = true;
                        askQueue.getAskingQueue().set(j, null);
                        addCarryRequests(asking);//将捎带请求加入队列
                    } else {
                        //如果是电梯外部的指令
                        if (asking.getElevatorState() == elevatorState
                                && asking.getAskingFloorNumber() <= primaryFloor) {
                            //如果是在电梯的运动方向上且楼层数不大于主请求的楼层
                            ifStay[asking.getAskingFloorNumber() - 1] = true;
                            askQueue.getAskingQueue().set(j, null);
                            addCarryRequests(asking);//将捎带请求加入队列
                        }
                    }
                }
            }
        } else if (elevatorState == ElevatorState.DOWN) {
            for (int j = 0; j < askQueue.getAskingQueue().size(); j++) {
                Asking asking = askQueue.getAskingQueue().get(j);
                if (asking != null 
                		&& asking.getAskingTime() < time
                        && asking.getAskingFloorNumber() <= currentFloor) {
                    if (asking.getEntryState() == EntryState.ER) {
                        //如果是在电梯内部的指令就直接响应
                        ifStay[asking.getAskingFloorNumber() - 1] = true;
                        askQueue.getAskingQueue().set(j, null);
                        addCarryRequests(asking);//将捎带请求加入队列
                    } else {
                        //如果是电梯外部的请求
                        if (asking.getElevatorState() == elevatorState
                                && asking.getAskingFloorNumber() >= primaryFloor) {
                            //如果是在电梯的运动方向上并且不小于主请求的楼层
                            ifStay[asking.getAskingFloorNumber() - 1] = true;
                            askQueue.getAskingQueue().set(j, null);
                            addCarryRequests(asking);//将捎带请求加入队列
                        }
                    }
                }
            }
        }
    }

    /*
        //遍历捎带请求队列
        public void traverseCarryFloors(Vector<Asking> carryRequests) {
            for (int i = 0; i < carryRequests.size(); i++) {
                if (carryRequests.get(i).getAskingFloorNumber() == currentFloor) {
                    carryRequests.set(i, null);
                }
            }
        }
    */
    //将捎带请求加入捎带请求队列
    public boolean addCarryRequests(Asking asking) {
        //Requires: asking != null
        //Modified: carryRequests
        //Effects: if carryRequests contains the asking, return false
        // else add the asking to carryRequests and return true
        for (int i = 0; i < carryRequests.size(); i++) {
            if (asking.equals(carryRequests.get(i)))
                return false;
        }
        carryRequests.add(asking);
        return true;
    }

    //判断是否还有楼层没有响应
    public boolean ifStillHaveTrueFloor() {
        //Requires: none
        //Modified: none
        //Effects: if one of the ifStay is false, return true,
        // else return false
        for (int i = 0; i < 10; i++) {
            if (ifStay[i])
                return true;
        }
        return false;
    }

    //电梯刚刚从静止状态开始运动,取整个队列中最近的请求
    public void starToMove(AskQueue askQueue, int i) {
        //Requires:askQueue != null,
        // i is the first element that make the askQueue.getAskingQueue().get(i) != null
        //Modified: primaryFloor, carryRequests, time, ifStay[], askQueue
        //Effects: the elevator choose the ith request as the main request to move,
        // and if the elevator is in stable state then printElevatorState
        Asking asking = askQueue.getAskingQueue().get(i);
        primaryFloor = asking.getAskingFloorNumber();
        carryRequests.add(asking);//将主请求加入队首
        if (asking.getAskingTime() > time)//当请求时电梯已经停止运行
            time = asking.getAskingTime();
        ifStay[asking.getAskingFloorNumber() - 1] = true;
        setElevatorState(asking.getAskingFloorNumber());//设置电梯运动方向

        if (elevatorState == ElevatorState.STABLE) {
            //如果电梯处于稳定状态
            ifStay[asking.getAskingFloorNumber() - 1] = false;
            for (int j = 0; j < askQueue.getAskingQueue().size(); j++) {
                asking = askQueue.getAskingQueue().get(j);
                if (asking.getAskingTime() <= time 
                		&& asking.getAskingFloorNumber() == currentFloor) {
                    askQueue.getAskingQueue().set(j, null);
                }
            }
            printElevatorState();
        }

    }

    //电梯一层一层地运动
    public void moveStepByStep(AskQueue askQueue) {
        //Requires: askQueue != null
        //Modified: currentFloor, time, ifStay[]
        //Effects: the elevator move a layer and change the state of elevator according to the direction of elevator
        if (elevatorState == ElevatorState.UP) {
            currentFloor++;
            time += 0.5;
            traverseFloors(askQueue);
            if (ifStay[currentFloor - 1]) {
                printElevatorState();
                ifStay[currentFloor - 1] = false;//过了该层之后变成false
            }
        } else if (elevatorState == ElevatorState.DOWN) {
            currentFloor--;
            time += 0.5;
            traverseFloors(askQueue);
            if (ifStay[currentFloor - 1]) {
                printElevatorState();
                ifStay[currentFloor - 1] = false;//过了该层之后变成false
            }
        }
    }

    //电梯去目的地
    public void printElevatorState() {
        //Requires: none
        //Modified: System.out
        //Effects: print the state of elevator and spend time to open and close the door
        System.out.println(toString());
        time++;//开关门附加时间
    }

    //重载toString()
    @Override
    public String toString() {
        //Requires: elevatorState != null
        //Modified: none
        //Effects: override the toString method with currentFloor, elevatorState and time
        return "(" + currentFloor + "," + elevatorState + "," + time + ")";
    }

    //输出捎带请求队列
    public void printCarryRequests() {
        //Requires: none
        //Modified: System.out, carryRequests
        //Effects: print the askings in carryRequests to console and clear the main request of carryRequests
        boolean flag1 = false;
        boolean flag = true;
        for (int i = 0; i < carryRequests.size(); i++) {
            Asking asking = carryRequests.get(i);
            if (asking != null) {
                flag1 = true;
                if (asking.getEntryState() == EntryState.FR) {
                    if (flag) {
                        System.out.print("捎带队列：");
                        System.out.print("(" + asking.getEntryState() + ","
                                + asking.getAskingFloorNumber() + ","
                                + asking.getElevatorState() + ","
                                + asking.getAskingTime() + ")(");
                        flag = false;
                        carryRequests.set(i, null);
                    } else {
                        System.out.print("(" + asking.getEntryState() + ","
                                + asking.getAskingFloorNumber() + ","
                                + asking.getElevatorState() + ","
                                + asking.getAskingTime() + ")");
                    }
                } else {
                    if (flag) {
                        System.out.print("捎带队列：");
                        System.out.print("(" + asking.getEntryState() + ","
                                + asking.getAskingFloorNumber() + ","
                                + asking.getAskingTime() + ")(");
                        flag = false;
                        carryRequests.set(i, null);
                    } else {
                        System.out.print("(" + asking.getEntryState() + ","
                                + asking.getAskingFloorNumber() + ","
                                + asking.getAskingTime() + ")");
                    }
                }
            }
        }
        if (flag1)
            System.out.println(")");
    }

    //重新设定捎带队列,将最近的一个未完成捎带请求变成主请求
    public boolean rebuildCarryRequests() {
        //Requires: none
        //Modified: carryRequests, primaryFloor
        //Effects: rebuild carryRequests and clear all the askings which askingFloorNumber
        // is not bigger than the primaryFloor, and then set the first not null
        // request as the main request and return true, if there is no asking left,
        // return false
        for (int i = 0; i < carryRequests.size(); i++) {
            Asking asking = carryRequests.get(i);
            if (asking != null 
            		&& asking.getAskingFloorNumber() <= primaryFloor) {
                carryRequests.set(i, null);
            }
        }
        for (int i = 0; i < carryRequests.size(); i++) {
            if (carryRequests.get(i) != null) {
                primaryFloor = carryRequests.get(i).getAskingFloorNumber();
                return true;
            }
        }
        return false;
    }

}

