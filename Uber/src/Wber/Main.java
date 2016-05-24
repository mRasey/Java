package Wber;

import java.io.IOException;
import java.util.HashSet;
import java.util.ListIterator;

/**
 * 主方法
 */
public class Main {
    /*Overview:
    这个类是用来跑main方法的类
    */

    public boolean repOK(){
        //Effects: returns true if the rep variant hols for this, otherwise returns false
        return true;
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        try {
            HashSet<Integer> normalCars = new HashSet<>();
            HashSet<Integer> tracingCars = new HashSet<>();
            Input input = new Input("D:\\123\\456.txt", "D:\\123\\789.txt");/*读入文件建立地图*/
            input.buildMap();
            input.buildTrafficLightMap();
            new Thread(new TrafficLight()).start();
            new Thread(new Map()).start();
            Center center = new Center(tracingCars, normalCars);
            new Thread(center).start();
            try {
                /***************在这里初始化车辆****************/
                /*************生成一个普通车示例************/
                /*随机位置的普通车*/
//                Car randomNormalCar = new Car(input.getPoints());
//                center.addCars(randomNormalCar);
//                new Thread(randomNormalCar).start();
//                normalCars.add(randomNormalCar.getNum());
                /*****************/
                /*指定位置为[55,55]的普通车*/
//                Car assignedNormalCar = new Car(input.getPoints(), 55, 55);
//                center.addCars(assignedNormalCar);
//                new Thread(assignedNormalCar).start();
//                normalCars.add(assignedNormalCar.getNum());
                /*************************/
                /*****************************************/
                /************生成一个可追踪车示例***********/
                /*随机位置的可追踪车*/
//                TracingCar normalTracingCar = new TracingCar(input.getPoints());
//                center.addCars(normalTracingCar);
//                new Thread(normalTracingCar).start();
//                tracingCars.add(normalTracingCar.getNum());
                /*******************/
                /*指定位置为[55,55]的可追踪车*/
//                TracingCar assignedTracingCar = new TracingCar(input.getPoints(), 55, 55);
//                center.addCars(assignedTracingCar);
//                new Thread(assignedTracingCar).start();
//                tracingCars.add(assignedTracingCar.getNum());
                /***************************/
                /*****************************************/
                for(int i = 0; i < 70; i++){
                    Car car = new Car(input.getPoints());
                    center.addCars(car);
                    new Thread(car).start();
                    normalCars.add(car.getNum());
                }
                for(int i = 0; i < 30; i++){
                    TracingCar tracingCar = new TracingCar(input.getPoints());
                    center.addCars(tracingCar);
                    new Thread(tracingCar).start();
                    tracingCars.add(tracingCar.getNum());
                }
                /**********************************************/


                /******************检测代码，勿删***************/
                if(tracingCars.size() != 30 && normalCars.size() != 70){
                    System.out.println("出租车数量不正确，程序退出");
                    System.exit(0);
                }
                /**********************************************/


//                /***************在这里添加测试代码**************/
//                /*****************示例*****************/
//                /*在随机位置添加一个乘客，并且随机目的地*/
//                new Thread(new Passenger(center)).start();
//                /*在指定位置（12，34）添加一个乘客， 指定目的地（56，78）（合法范围是（0，0）到（79，79））*/
//                new Thread(new Passenger(center, new Location(12, 34), new Location(56, 78))).start();
//                /*getNormalCar获取普通车，getTracingCar获取可追踪车*/
//                center.getNormalCar(12);
//                center.getTracingCar(77);
//                center.getNormalCar(12).print();/*获取车号为12的出租车的状态*/
//                /*获取指定出租车的服务过的乘客的信息队列，信息包括乘客编号，乘客起点和乘客终点，合法范围为[0,size()-1]*/
//                center.getNormalCar(12).servedPassengerQueue.get(0).getNum();
//                center.getNormalCar(12).servedPassengerQueue.get(0).getStartLocation();
//                center.getNormalCar(12).servedPassengerQueue.get(0).getDestinationLocation();
//                center.getTracingCar(77).getServeCount();/*获取指定可追踪车的服务次数*/
//                /*获取77号可追踪车辆的第5次服务路径的迭代器（车辆合法号码为71到100，listIterator迭代器参数合法范围为0到服务次数的大小减一）*/
//                ListIterator listIterator = center.getTracingCar(77).listIterator(4);
//                listIterator.hasNext();/*是否有下一个元素*/
//                listIterator.next();/*指向下一个元素,可以调用toString打印*/
//                listIterator.hasPrevious();/*是否有前一个元素*/
//                listIterator.previous();/*指向前一个元素，可以调用toString打印*/
//                /*将（1，2）到（1，3）的道路关闭，需为相邻的两个点之间，否则无效，true表示设置道路断开*/
//                Map.setBlocked(new Location(1, 2).oneDimensionalLoc(), new Location(1, 3).oneDimensionalLoc(), true);
//                /*将（1，2）到（1，3）的道路打开，需为相邻的两个点之间，否则无效，false表示设置道路连接*/
//                Map.setBlocked(new Location(1, 2).oneDimensionalLoc(), new Location(1, 3).oneDimensionalLoc(), false);
//                /*获取（1，2）到（1，3）的道路的流量，需为相邻的两个点之间否则返回Integer.MAX_VALUE*/
//                int flow = Map.getFlows(new Location(1, 2).oneDimensionalLoc(), new Location(1, 3).oneDimensionalLoc());
//                /**************************************/
//                /**********************************************/
            }
            catch (Throwable t){
//                t.printStackTrace();
                System.out.println("测试模块发生故障，程序退出");
                System.exit(0);
            }
        } catch (Throwable t) {
            System.out.println("主线程运行时出现了问题，程序退出");
//            t.printStackTrace();
            System.exit(0);
        }
    }

}