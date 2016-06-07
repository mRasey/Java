package Elevator;

import java.util.Scanner;

public class ALS_Schedule extends Scheduler implements Runnable {
    /**
     * OVERVIEW:
     * This class is a little smart schedule, this class manager the elevator.
     * arriveTime means the time when the next passenger arrived.
     * <p>
     * 表示对象:
     * arriveTime
     * <p>
     * 抽象函数:
     * AF(c) = (arriveTime) where arriveTime = c.arriveTime
     * <p>
     * 不变式:
     * always true
     */
    private double arriveTime; //接下来一个乘客到达目标楼层的时间

    public boolean repOK(){
        return true;
    }

    public double getArriveTime() {
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
        //Modified: none
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

    @Override
    public void run() {
        try {
            int currentTime = 0;
            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();
            AskQueue askQueue = new AskQueue();
            Elevator elevator = new Elevator();
            boolean ifFirstInput = true;

            while(!input.equals("run")){
                input = input.replaceAll(" ", "");
                if(input.matches("^\\(FR,[1-9],UP,[0-9]{1,9}\\)$")
                        || input.matches("^\\(FR,(([2-9])|([1][0])),DOWN,[0-9]{1,9}\\)$")
                        || input.matches("^\\(ER,([1-9]|([1][0])),[0-9]{1,9}\\)$")){
                    //成功匹配
                    Asking asking = new Asking(input);
                    if(ifFirstInput) {
                        //第一次输入
                        if (asking.getAskingTime() != 0)
                            System.out.println("起始时间不为0，请重新输入");
                        else {
                            currentTime = askQueue.addAskingQueue(asking, currentTime);
                            ifFirstInput = false;
                        }
                    }
                    else
                        //不是第一次输入
                        currentTime = askQueue.addAskingQueue(asking, currentTime);
                }
                else{
                    //未成功匹配
                    System.out.println("输入有误");
                }
                input = scanner.nextLine();
            }
            //电梯开始运动
            for(int i = 0; i < askQueue.getAskingQueue().size(); i++){
                if(askQueue.getAskingQueue().get(i) != null){
                    elevator.starToMove(askQueue, i);
                    do{
                        while (elevator.getCurrentFloor() != elevator.getPrimaryFloor()) {
                            //还没有到主请求楼层，则继续移动
                            elevator.moveStepByStep(askQueue);
                        }
                        elevator.printCarryRequests();//输出捎带序列
                    }while(elevator.rebuildCarryRequests() && elevator.ifStillHaveTrueFloor());//当重建捎带序列成功,继续循环
                    elevator.getCarryRequests().removeAllElements();
                }
            }
        }
        catch (Throwable t){
            System.out.println(t);
        }
        finally {
            System.out.println("程序结束");
            System.exit(0);
        }
    }
}
