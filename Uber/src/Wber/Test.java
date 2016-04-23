package Wber;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

public class Test implements Runnable{
    @Override
    public void run() {
        Input input = new Input("D:\\123\\456.txt");/*读入文件建立地图*/
        try {
            input.buildMap();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Center center = new Center();
        Car car = new Car(input.getPoints());
        center.addCars(car);
        ArrayList<Integer> cars = car.findPath(new Location(2, 2), new Location(2, 4));
        for (Integer anArrayList : cars) {
            System.out.println(anArrayList / 80 + " " + anArrayList % 80);
        }
        Passenger passenger = new Passenger(center);
        new Thread(center).start();
        new Thread(car).start();
        new Thread(passenger).start();
    }
}