package Wber;

import java.io.IOException;
import java.util.HashSet;
import java.util.ListIterator;

public class t {

    public static void main(String[] args) throws IOException, InterruptedException {
        HashSet<Integer> normalCars = new HashSet<>();
        HashSet<Integer> tracingCars = new HashSet<>();
        Input input = new Input("D:\\123\\456.txt", "D:\\123\\123.txt");/*读入文件建立地图*/
        input.buildMap();
        input.buildTrafficLightMap();
        Center center = new Center(tracingCars, normalCars);
        new Thread(new TrafficLight()).start();
        new Thread(center).start();
        new Thread(new Map()).start();
//        Car car2 = new Car(input.getPoints(), 1, 1);
//        center.addCars(car2);
//        new Thread(car2).start();
        for (int i = 0; i < 70; i++) {/*随机生成70辆普通出租车*/
            Car car = new Car(input.getPoints(), 55, 55);
            center.addCars(car);
            new Thread(car).start();
            normalCars.add(car.getNum());
        }
        for(int i = 0; i < 30; i++){/*随机生成30辆可追踪出租车*/
            TracingCar car = new TracingCar(input.getPoints(), 55, 55);
            center.addCars(car);
            new Thread(car).start();
            tracingCars.add(car.getNum());
        }
        for(int i = 0; i < 200; i++){
            new Thread(new Passenger(center, new Location(55, 55), new Location(66, 66))).start();
            Thread.sleep(100);
        }
        Thread.sleep(30000);
        ListIterator<Location> iterator = center.getTracingCar(77).listIterator(0);
        while(iterator.hasNext()){
            System.out.println(iterator.next().toString());
        }
        while (iterator.hasPrevious()) {
            System.out.println(iterator.previous().toString());
        }
//        new Thread(new TestCar(center.getCars())).start();
//        new Thread(new Passenger(center, new Location(1, 1), new Location(79, 1))).start();
//        Car car = new Car(input.getPoints(), 1, 1);
//        center.addCars(car);
//        new Thread(car).start();
//        Map.setBlocked(new Location(1, 5).oneDimensionalLoc(), new Location(1, 6).oneDimensionalLoc(), false);
//        new Thread(new Passenger(center, new Location(1, 1), new Location(1, 79))).start();
//        for(int i = 0; i < 1; i++) {
//            new Thread(new Passenger(center)).start();
//            new Thread(new Passenger(center, new Location(1, 1), new Location(79, 79))).start();
//            new Thread(new Passenger(center, new Location(29, 29), new Location(79, 79))).start();
//            new Thread(new Passenger(center, new Location(29, 29), new Location(79, 79))).start();
//        }
    }
}
