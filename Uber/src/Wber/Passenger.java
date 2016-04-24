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
        passengerCount.addAndGet(1);
        num = passengerCount.get();
//        if(num > 300){
//            System.out.println("生成的乘客超过300个，程序结束");
//            System.exit(0);
//        }
        startLocation = new Location(new Random().nextInt(80), new Random().nextInt(80));
        destinationLocation = new Location(new Random().nextInt(80), new Random().nextInt(80));
        this.cars = center.getCars();
        this.center = center;
    }

    /**
     * 构造器2
     */
    public Passenger(Center center, Location startLocation, Location destinationLocation) {
        passengerCount.addAndGet(1);
        num = passengerCount.get();
//        if(num > 300){
//            System.out.println("生成的乘客超过300个，程序结束");
//            System.exit(0);
//        }
        this.startLocation = startLocation;
        this.destinationLocation = destinationLocation;
        this.cars = center.getCars();
        this.center = center;
    }

    public int getNum() {
        return num;
    }

    public Location getStartLocation() {
        return startLocation;
    }

    public Location getDestinationLocation() {
        return destinationLocation;
    }

    /**
     * 遍历出租车队列寻找满足条件的出租车
     * @return 找到满足条件的出租车，返回真
     */
    public boolean traverseCars(){
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
        return (carLocation.getX() >= passengerLocation.getX() - 2
                && carLocation.getX() <= passengerLocation.getX() + 2
                && carLocation.getY() >= passengerLocation.getY() - 2
                && carLocation.getY() <= passengerLocation.getY() + 2);
    }

    @Override
    public void run(){
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
            System.out.println("乘客线程发生故障，程序退出");
            System.exit(0);
        }
    }
}