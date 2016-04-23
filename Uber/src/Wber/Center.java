package Wber;

import java.util.HashSet;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 指挥中心类
 */
public class Center implements Runnable{
    private HashSet<Car> cars = new HashSet<>();/*出租车队列*/
    private LinkedBlockingQueue<AskedCar> chosenCars = new LinkedBlockingQueue<>(300);
    /**
     * 构造器
     */
    public Center() {}

    public HashSet<Car> getCars() {
        return (HashSet<Car>) cars.clone();
    }

    public void addCars(Car car){
        cars.add(car);
    }

    public void setChosenCars(AskedCar askedCar) {
        chosenCars.add(askedCar);
    }

    /**
     * 根据编号获取指定车辆
     * @param carNum 车编号
     * @return 找到指定编号的车，返回车，找不到则返回null
     */
    public Car getCar(int carNum) {
        for (Car car : cars) {
            if (car.getNum() == carNum)
                return car;
        }
        return null;
    }

    @Override
    public void run(){
        try {
            while(true) {
//                Thread.sleep(1);
                AskedCar askedCar = chosenCars.take();
                askedCar.getCar().setStartLocation(askedCar.getPassenger().getStartLocation());
                askedCar.getCar().setDestinationLocation(askedCar.getPassenger().getDestinationLocation());
                askedCar.getCar().setCarState(CarState.WaitServing);/*使车进入等待服务状态*/
            }
        }
        catch (InterruptedException e) {
            e.printStackTrace();
            System.out.println("调度中心发生故障，程序退出");
            System.exit(0);
        }
    }
}