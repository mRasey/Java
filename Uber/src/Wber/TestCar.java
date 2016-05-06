package Wber;

import java.io.IOException;

public class TestCar {

    public static void main(String[] args) throws IOException, InterruptedException {
        Input input = new Input("D:\\123\\test.txt");/*读入文件建立地图*/
        input.buildMap();
        Center center = new Center();
        new Thread(center).start();
        new Thread(new Map()).start();
//        Car car2 = new Car(input.getPoints(), 29, 29);
//        center.addCars(car2);
        for (int i = 0; i < 100; i++) {
            Car car = new Car(input.getPoints(), 29, 29);
            center.addCars(car);
            new Thread(car).start();
        }
        for(int i = 0; i < 80; i++){
            for(int j = 0; j < 80; j++){
                for(int m = 0; m < 80; m++){
                    for(int n = 0; n < 80; n++) {
                        if(center.getCar(1).findPath(new Location(i, j), new Location(m, n)).size()
                                != center.getCar(1).singleFindPathByFlow(new Location(i, j), new Location(m, n)).size()){
                            System.out.println("error");
                        }
//                        System.out.println("n = " + n);
                    }
//                    System.out.println("m = " + m);
                }
//                System.out.println("j = " + j);
            }
            System.out.println("i = " + i);
        }
        System.exit(0);
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
//        while(true){
//            System.out.println("test " + Map.getFlows(new Location(1,10).oneDimensionalLoc(), new Location(1,11).oneDimensionalLoc()));
//            System.out.println(car.getLocation().getX() + " " + car.getLocation().getY() + " " + car.getCarState());
//            Thread.sleep(100);
//        }
    }
}
