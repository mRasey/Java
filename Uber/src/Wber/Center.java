package Wber;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 指挥中心类
 */
public class Center implements Runnable{
    private Set<Car> cars = Collections.synchronizedSet(new HashSet<Car>());/*出租车队列*/
    private LinkedBlockingQueue<AskedCar> chosenCars = new LinkedBlockingQueue<>();
//    private Car chosenCar;
    /**
     * 构造器
     */
    public Center() {}

    public Set<Car> getCars() {
        return cars;
    }

    public void addCars(Car car){
        cars.add(car);
    }

    public void setChosenCars(AskedCar askedCar) {
        chosenCars.add(askedCar);
    }

    /**
     * 判断出租车是否在乘客请求范围内
     * @param passengerLocation 乘客坐标
     * @param carLocation 出租车坐标
     * @return 如果在请求范围内，返回true
     */
    public boolean inPassengerRange(Location passengerLocation, Location carLocation) {
        if ((carLocation.getX() >= passengerLocation.getX() - 2 || carLocation.getX() <= passengerLocation.getX() + 2)
                && (carLocation.getY() >= passengerLocation.getY() - 2 || carLocation.getY() <= passengerLocation.getY() + 2)) {
            return true;
        }
        return false;
    }

    @Override
    public void run(){
        try {
            while(true) {
                AskedCar askedCar = chosenCars.take();
                askedCar.getCar().setStartLocation(askedCar.getPassenger().getStartLocation());
                askedCar.getCar().setDestinationLocation(askedCar.getPassenger().getDestinationLocation());
                askedCar.getCar().setCarState(CarState.WaitServing);/*使车进入等待服务状态*/
            }
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        finally {
            System.out.println("指挥中心停止运行");
            System.exit(0);
        }
    }
}
