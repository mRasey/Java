package Wber;

import java.io.IOException;

/**
 * 主方法
 */
public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {
        try {
            Input input = new Input("D:\\123\\456.txt");/*读入文件建立地图*/
            input.buildMap();
            Center center = new Center();
            new Thread(center).start();
            for (int i = 0; i < 100; i++) {/*随机生成100辆出租车*/
                Car car = new Car(input.getPoints());
                center.addCars(car);
                new Thread(car).start();
            }
            try {
                /***************在这里添加测试代码**************/
                /*在随机位置添加一个乘客，并且随机目的地*/
                new Thread(new Passenger(center)).start();
                /*在指定位置（12，34）添加一个乘客， 指定目的地（56，78）*/
                new Thread(new Passenger(center, new Location(12, 34), new Location(56, 78))).start();
                /*获取车号为12的出租车的状态，号码为1到100*/
                center.getCar(1).print();
                /**********************************************/
            }
            catch (Throwable t){
                System.out.println("测试模块发生故障，程序退出");
                System.exit(0);
            }
            for(int j = 0; j < 2; j++) {
                for (int i = 0; i < 2000; i++) {
                    new Thread(new Passenger(center)).start();
//                Thread.sleep(100);
                }
                Thread.sleep(30000);
            }
        } catch (Throwable t) {
            System.out.println("程序运行时出现了问题，程序退出");
            t.printStackTrace();
            System.exit(0);
        }
    }

}