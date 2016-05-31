package Elevator;

import java.util.Scanner;

public class Floor {
    private AskQueue askQueue = new AskQueue();//每层楼有一个请求队列

    public void setAskQueue(Asking asking){
        askQueue.getAskingQueue().add(asking);
    }

    public AskQueue getAskQueue(){
        return askQueue;
    }

    public static void main(String[] args){
        try {
            int currentTime = 0;
            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();
            AskQueue askQueue = new AskQueue();
            Elevator elevator = new Elevator();
            ALS_Schedule scheduler = new ALS_Schedule();
            boolean ifFirstInput = true;

            //EntryState entryState;
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
                    //do{
                    elevator.starToMove(askQueue, i);
                    do{
                        while (elevator.getCurrentFloor() != elevator.getPrimaryFloor()) {
                            //还没有到主请求楼层，则继续移动
                            elevator.moveStepByStep(askQueue);
                        }
                        elevator.printCarryRequests();//输出捎带序列
                        //elevator.rebuildCarryRequests();
                    }while(elevator.rebuildCarryRequests() && elevator.ifStillHaveTrueFloor());//当重建捎带序列成功,继续循环
                    elevator.getCarryRequests().removeAllElements();
                }
            }

            /*for (int i = 0; i < askQueue.getM_askingQueue().size(); i++) {
                scheduler.setM_nextFloor(askQueue.getNextAskingFloor());
                scheduler.setM_nextTime(askQueue.getNextAskingTime());
                askQueue.moveCurrentVectorOneStep();
                //elevator.moveToNextFloor(scheduler.getM_nextFloor(), scheduler.getM_nextTime());
                //System.out.println(scheduler.getNextFloor());
                elevator.printElevatorState();
            }*/
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

