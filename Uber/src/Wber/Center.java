package Wber;

import java.util.HashSet;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 指挥中心类
 */
public class Center implements Runnable{
    /*Overview:
    这个类是指挥中心类，用于响应乘客请求对出租车进行调度，cars表示整个出租车队列，chosenCar保存乘客请求
    */

    private HashSet<Car> cars = new HashSet<>();/*出租车队列*/
    private LinkedBlockingQueue<AskedCar> chosenCars = new LinkedBlockingQueue<>(300);
    private HashSet<Integer> tracingCars = new HashSet<>();
    private HashSet<Integer> normalCars = new HashSet<>();
    /**
     * 构造器
     */
    public Center(HashSet<Integer> tracingCars, HashSet<Integer> normalCars) {
        //Requires: tracingCars不为null，normalCars不为null
        //Effects: 构造器
        this.tracingCars = tracingCars;
        this.normalCars = normalCars;
    }

    public boolean repOK(){
        //Effects: returns true if the rep variant holds for this, otherwise returns false
        if(cars.size() < 0 || cars.size() > 100) return false;
        if(tracingCars.size() < 0 || normalCars.size() < 0) return false;
//        Iterator<Car> iterator = cars.iterator();
//        while(iterator.hasNext()){
//            if(!(iterator.next() instanceof Car)) return false;
//        }
//        Iterator<AskedCar> iterator1 = chosenCars.iterator();
//        while(iterator1.hasNext()){
//            if(!(iterator1.next() instanceof AskedCar)) return false;
//        }
        return true;
    }

    public HashSet<Car> getCars() {
        //Requires: none
        //Modifies: none
        //Effects: 返回cars属性
        return (HashSet<Car>) cars.clone();
    }

    public void addCars(Car car){
        //Requires: car不为NULL
        //Modifies: cars属性
        //Effects: 将car加入cars
        cars.add(car);
    }

    public void setChosenCars(AskedCar askedCar) {
        //Requires: askedCar不为NULL
        //Modifies: chosenCars属性
        //Effects: 将askedCar加入chosenCars
        chosenCars.add(askedCar);
    }

    /**
     * 根据编号获取指定车辆
     * @param carNum 车编号
     * @return 找到指定编号的车，返回车，找不到则返回null
     */
    public Car getCar(int carNum) {
        //Requires: none
        //Modifies: none
        //Effects: 返回编号为carNum的出租车，如果不是有效车辆返回null
        for (Car car : cars) {
            if (car.getNum() == carNum)
                return car;
        }
        return null;
    }

    public Car getNormalCar(int carNum){
        //Requires: none
        //Modifies: none
        //Effects: 返回编号为carNum的出租车,如果不是普通车，返回null
        if(normalCars.contains(carNum)){
            return getCar(carNum);
        }
        else{
            System.out.println("不是普通车辆");
            return null;
        }
    }

    public TracingCar getTracingCar(int carNum) {
        //Requires: none
        //Modifies: none
        //Effects: 返回编号为carNum的出租车,如果不是可追踪车辆，返回null
        if(tracingCars.contains(carNum)){
            return (TracingCar) getCar(carNum);
        }
        else{
            System.out.println("不是可追踪车辆");
            return null;
        }
    }
    @Override
    public void run(){
        try {
            while(true) {
                AskedCar askedCar = chosenCars.take();
                askedCar.getCar().setStartLocation(askedCar.getPassenger().getStartLocation());
                askedCar.getCar().setDestinationLocation(askedCar.getPassenger().getDestinationLocation());
                askedCar.getCar().servedPassengerQueue.add(askedCar.getPassenger());/*将乘客编号加入队列*/
                askedCar.getCar().setCarState(CarState.WaitServing);/*使车进入等待服务状态*/
//                System.out.println(askedCar.getPassenger().getNum() + " 号乘客上了 " + askedCar.getCar().getNum() + " 号车");
            }
        }
        catch (InterruptedException e) {
//            e.printStackTrace();
            System.out.println("调度中心发生故障，程序退出");
            System.exit(0);
        }
    }
}