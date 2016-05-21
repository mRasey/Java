package Wber;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

public class TestCar{

    public static void main(String[] args) throws IOException {
        HashSet<Integer> normalCars = new HashSet<>();
        HashSet<Integer> tracingCars = new HashSet<>();
        Input input = new Input("D:\\123\\456.txt", "D:\\123\\123.txt");/*读入文件建立地图*/
        input.buildMap();
        input.buildTrafficLightMap();
        new Thread(new TrafficLight()).start();
        new Thread(new Map()).start();
        Center center = new Center(tracingCars, normalCars);
        new Thread(center).start();
//        for(int i = 0; i < 100; i++){
//            Car randomNormalCar = new Car(input.getPoints());
//            center.addCars(randomNormalCar);
//            new Thread(randomNormalCar).start();
//            normalCars.add(randomNormalCar.getNum());
//        }
        Car randomNormalCar = new Car(input.getPoints(), 0, 0);
        center.addCars(randomNormalCar);
        new Thread(randomNormalCar).start();
        normalCars.add(randomNormalCar.getNum());
        new Thread(new Passenger(center, new Location(0, 0), new Location(79, 79))).start();
    }
}
