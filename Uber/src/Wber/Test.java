package Wber;

import java.io.IOException;
import java.util.ArrayList;

public class Test {
    public static void main(String[] args) throws IOException {
        Input input = new Input("D:\\123\\456.txt");/*读入文件建立地图*/
        input.buildMap();
        Center center = new Center();
        Car car = new Car(input.getPoints(), center.getCars());
        center.addCars(car);
        ArrayList<Integer> arrayList = car.findPath(new Location(12, 33), new Location(33, 33));
        System.out.println(arrayList.size());
        for (Integer anArrayList : arrayList) {
            System.out.println(anArrayList / 80 + " " + anArrayList % 80);
        }
//        Passenger passenger = new Passenger(center, (HashSet<Car>) center.getCars());
        new Thread(center).start();
        new Thread(car).start();
//        new Thread(passenger).start();
    }
}