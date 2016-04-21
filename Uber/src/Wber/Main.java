package Wber;

import java.io.IOException;
import java.util.HashSet;

/**
 * 主方法
 */
public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {
        Input input = new Input("D:\\123\\456.txt");/*读入文件建立地图*/
        input.buildMap();
        Center center = new Center();
        new Thread(center).start();
        for(int i = 0; i < 10; i++){
//            System.out.println("new car");
            Car car = new Car(input.getPoints(), center.getCars());
            center.addCars(car);
            car.setCarState(CarState.Waiting);
//            System.out.println(car.getNum() + " " + car.getLocation().getX() + " " + car.getLocation().getY());
            new Thread(car).start();
        }
        while (true) {
            Passenger passenger = new Passenger(center, center.getCars());
//            System.out.println("passenger " + passenger.getStartLocation().getX() + " " + passenger.getStartLocation().getY());
            new Thread(passenger).start();
            Thread.sleep(1000);
        }
    }

}