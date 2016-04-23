package Wber;

import java.io.IOException;

public class TestCar {

    public static void main(String[] args) throws IOException, InterruptedException {
        Input input = new Input("D:\\123\\456.txt");/*读入文件建立地图*/
        input.buildMap();
        Center center = new Center();
        new Thread(center).start();
//        Car car2 = new Car(input.getPoints(), 29, 29);
//        center.addCars(car2);
//        for (int i = 0; i < 100; i++) {
//            Car car1 = new Car(input.getPoints(), 29, 29);
//            center.addCars(car1);
//            new Thread(car1).start();
//        }
        Car car1 = new Car(input.getPoints(),29,29);
        for(int i = 0; i < 1; i++) {
            new Thread(new Passenger(center, new Location(29, 29), new Location(79, 79))).start();
        }
        Thread.sleep(1);
        new Thread(car1).start();
//        new Thread(car2).start();
        while(true){
            System.out.println(car1.getLocation().getX() + " " + car1.getLocation().getY());
            System.out.println(car1.getCarState());
            Thread.sleep(100);
//            System.out.println(car2.getLocation().getX() + " " + car2.getLocation().getY());
//            System.out.println(car2.getCarState());
//            Thread.sleep(100);
        }
    }
}
