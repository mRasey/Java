package Elevator;

import java.util.Scanner;

public class Floor {
    static long startTime;

    public static void main(String[] args){
        //Floor floor = new Floor();
        Floor.startTime = System.currentTimeMillis();
        AskQueue askQueue = new AskQueue();
        //thread_Elevator[] thread_elevators = new thread_Elevator[3];
        ALS_Schedule als_schedule = new ALS_Schedule();
        Thread alsThread = new Thread(als_schedule);
        //boolean ifFirstInput = true;
        for(int i = 0; i < als_schedule.getThread_elevators().length; i++){
            als_schedule.getThread_elevators()[i] = new thread_Elevator(i+1);
            new Thread(als_schedule.getThread_elevators()[i]).start();
        }

        Scanner scanner = new Scanner(System.in);
        String input;
        while(true){
            if(scanner.hasNextLine()) {
                input = scanner.nextLine();
                input = input.replaceAll(" ", "");
                if (input.matches("^\\(FR,[0-9]{1,2},UP\\)$")
                        || input.matches("^\\(FR,[0-9]{1,2},DOWN\\)$")
                        || input.matches("^\\(ER,#[1-3],[0-9]{1,2}\\)$")) {
                    //成功匹配
                    Asking asking = new Asking(input);
                    if (askQueue.addAskingQueue(asking)) {
                        als_schedule.setAsking(asking);
                        alsThread.run();
                        //als_schedule.distribute(asking, als_schedule.getThread_elevators());//分配请求
                    }
                } else {
                    //未成功匹配
                    System.out.println("输入格式有误");
                }
            }
        }
            //电梯开始运动
            /*for(int i = 0; i < askQueue.getM_askingQueue().size(); i++){
                if(askQueue.getM_askingQueue().get(i) != null){
                    //do{
                    elevator.starToMove(askQueue, i);
                    do{
                        while (elevator.getCurrentFloor() != elevator.getM_primaryFloor()) {
                            //还没有到主请求楼层，则继续移动
                            elevator.moveStepByStep(askQueue);
                        }
                        elevator.printCarryRequests();//输出捎带序列
                        //elevator.rebuildCarryRequesets();
                    }while(elevator.rebuildCarryRequesets() && elevator.ifStillHaveTrueFloor());//当重建捎带序列成功,继续循环
                    elevator.getM_carryRequests().removeAllElements();
                }
            }*/

    }
}

