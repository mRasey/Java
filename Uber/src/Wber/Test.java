package Wber;

import java.io.IOException;
import java.util.HashSet;

public class Test{
    public static void main(String[] args) throws IOException {
        HashSet<Integer> normalCars = new HashSet<>();
        HashSet<Integer> tracingCars = new HashSet<>();
        Input input = new Input("D:\\123\\456.txt", "D:\\123\\789.txt");/*读入文件建立地图*/
        input.buildMap();
        input.buildTrafficLightMap();
        Center center = new Center(tracingCars, normalCars);
        new Thread(new TrafficLight()).start();
        new Thread(center).start();
        new Thread(new Map()).start();
        for (int i = 0; i < 70; i++) {/*随机生成70辆普通出租车*/
            Car car = new Car(input.getPoints(), 50, 50);
            center.addCars(car);
            new Thread(car).start();
        }
        for(int i = 0; i < 30; i++){/*随机生成30辆可追踪出租车*/
            TracingCar car = new TracingCar(input.getPoints(), 50, 50);
            center.addCars(car);
            new Thread(car).start();
        }
        for(int i = 0; i < 100; i++){
            new Thread(new Passenger(center, new Location(50, 50), new Location(50, 50))).start();
        }
    }
}