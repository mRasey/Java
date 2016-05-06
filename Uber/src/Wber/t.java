package Wber;

import java.io.IOException;

public class t {

    public static void main(String[] args) throws IOException, InterruptedException {
        Input input = new Input("D:\\123\\test.txt");/*读入文件建立地图*/
        input.buildMap();
        Center center = new Center();
        new Thread(center).start();
        new Thread(new Map()).start();
        Car car2 = new Car(input.getPoints(), 1, 1);
        center.addCars(car2);
        new Thread(car2).start();
//        new Thread(new Passenger(center, new Location(1, 1), new Location(1, 79))).start();
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
        while(true){
//            System.out.println("test " + Map.getFlows(new Location(1,10).oneDimensionalLoc(), new Location(1,11).oneDimensionalLoc()));
            System.out.println(car2.getLocation().getX() + " " + car2.getLocation().getY() + " " + car2.getCarState());
            Thread.sleep(100);
        }
    }
}
