package Wber;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 乘客类
 */
public class Passenger implements Runnable{
    private static AtomicInteger passengerCount = new AtomicInteger();
    private int num;/*乘客编号*/
    private Location startLocation;/*初始坐标*/
    private Location destinationLocation;/*目的地坐标*/
    private HashSet<Car> cars = new HashSet<>();/*出租车队列*/
    private HashSet<Car> fitCars = new HashSet<>();/*满足条件的出租车队列*/
    private boolean accept = false;/*是否接受*/
    private Center center;

    /**
     * 构造器1
     */
    public Passenger(Center center) {
        //Requires: Center
        //Modifies: none
        //Effects: 构造器
        passengerCount.addAndGet(1);
        num = passengerCount.get();
        startLocation = new Location(new Random().nextInt(80), new Random().nextInt(80));
        destinationLocation = new Location(new Random().nextInt(80), new Random().nextInt(80));
        this.cars = center.getCars();
        this.center = center;
    }

    /**
     * 构造器2
     */
    public Passenger(Center center, Location startLocation, Location destinationLocation) {
        //Requires: Center，起点位置，终点位置
        //Modifies: none
        //Effects: 构造器
        passengerCount.addAndGet(1);
        num = passengerCount.get();
        this.startLocation = startLocation;
        this.destinationLocation = destinationLocation;
        this.cars = center.getCars();
        this.center = center;
    }

    public int getNum() {
        //Requires: none
        //Modifies: none
        //Effects: 返回乘客编号
        return num;
    }

    public Location getStartLocation() {
        //Requires: none
        //Modifies: none
        //Effects: 返回乘客位置
        return startLocation;
    }

    public Location getDestinationLocation() {
        //Requires: none
        //Modifies: none
        //Effects: 返回乘客的终点位置
        return destinationLocation;
    }

    /**
     * 遍历出租车队列寻找满足条件的出租车
     * @return 找到满足条件的出租车，返回真
     */
    public boolean traverseCars(){
        //Requires: none
        //Modifies: none
        //Effects: 遍历寻找合适的出租车
        Iterator<Car> carIterator = cars.iterator();
        while (carIterator.hasNext()) {
            Car car = carIterator.next();
            if (inPassengerRange(startLocation, car.getLocation())
                    && !car.isHasPassenger()) {
                fitCars.add(car);
            }
        }
        return !fitCars.isEmpty();
    }

    /**
     * 根据信用度和距离寻找最合适的出租车
     * @return 最终选择的出租车
     */
    public Car findBestFitCar(){
        //Requires: none
        //Modifies: none
        //Effects: 寻找最终选择的出租车
        Iterator<Car> carIterator = fitCars.iterator();
        Car chosenCar = carIterator.next();
        int chosenCarPathSize = chosenCar.findPath(chosenCar.getLocation(), startLocation).size();
        while (carIterator.hasNext()) {
            Car car = carIterator.next();
            int carPathSize = car.findPath(car.getLocation(), startLocation).size();
            if(car.getCredit() > chosenCar.getCredit() && !car.isHasPassenger()){
                chosenCar = car;
                chosenCarPathSize = carPathSize;
            }
            else if(car.getCredit() == chosenCar.getCredit()
                    && carPathSize <= chosenCarPathSize
                    && !car.isHasPassenger()){
                chosenCar = car;
                chosenCarPathSize = carPathSize;
            }
        }
        if(chosenCar.isHasPassenger())
            return null;
        return chosenCar;
    }

    /**
     * 判断出租车是否在乘客请求范围内
     * @param passengerLocation 乘客坐标
     * @param carLocation 出租车坐标
     * @return 如果在请求范围内，返回true
     */
    public boolean inPassengerRange(Location passengerLocation, Location carLocation) {
        //Requires: 出租车的位置和乘客的位置
        //Modifies: none
        //Effects: 如果出租车的位置在乘客的呼叫范围内，返回真
        return (carLocation.getX() >= passengerLocation.getX() - 2
                && carLocation.getX() <= passengerLocation.getX() + 2
                && carLocation.getY() >= passengerLocation.getY() - 2
                && carLocation.getY() <= passengerLocation.getY() + 2);
    }

    @Override
    public void run(){
        if (startLocation.getX() < 0 || startLocation.getX() >= 80
                || startLocation.getY() < 0 || startLocation.getY() >= 80
                || destinationLocation.getX() < 0 || destinationLocation.getX() >= 80
                || destinationLocation.getY() < 0 || destinationLocation.getY() >= 80) {
            System.out.println("输入的 " + num + " 号乘客位置超出合法范围");
            Thread.currentThread().interrupt();
        }
        long startTime = System.currentTimeMillis();
        try {
            while (true) {
                long endTime = System.currentTimeMillis();
                if (endTime - startTime > 3000)
                    break;/*时间到达三秒，线程结束*/
                else {
                    traverseCars();
                    Thread.sleep(50);
                }
            }
            synchronized (this.getClass()) {
                if (fitCars.isEmpty()) {
                    System.out.println(num + " 号乘客无可用车");
                } else {
                    Car car = findBestFitCar();
                    if (car != null) {
                        center.setChosenCars(new AskedCar(this, car));
                        car.setHasPassenger(true);
                        System.out.println(num + " 号乘客上了 " + car.getNum() + " 号车");
                    } else {
                        System.out.println(num + " 号乘客无可用车");
                    }
                }
            }
        } catch (InterruptedException i) {
//            i.printStackTrace();
//            System.out.println("乘客线程发生故障，程序退出");
            Thread.currentThread().interrupt();
        }
    }
}