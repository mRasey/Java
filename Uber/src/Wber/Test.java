package Wber;

import java.io.IOException;
import java.util.ArrayList;

public class Test{
    public static void main(String[] args) {
        Input input = new Input("D:\\123\\test.txt");/*读入文件建立地图*/
        try {
            input.buildMap();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Center center = new Center();
        Car car = new Car(input.getPoints());
        center.addCars(car);
        new Thread(center).start();
//        ArrayList<Integer> cars = car.findPath(new Location(2, 26), new Location(1, 1));
        ArrayList<Integer> cars = car.singleFindPathByFlow(new Location(1, 1), new Location(79, 79));
        for (Integer anArrayList : cars) {
            System.out.println(anArrayList / 80 + " " + anArrayList % 80);
        }
//        for(int i = 0; i < cars.size() - 1; i++){
//            System.out.println(Map.getFlows(cars.get(i), cars.get(i+1)));
//        }
        new Thread(new Passenger(center, new Location(2, 2), new Location(79, 79))).start();
        new Thread(car).start();
    }
}