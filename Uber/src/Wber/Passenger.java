package Wber;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 乘客类
 */
public class Passenger implements Runnable{
    /*Overview:
    这个类是用来生成乘客的类，里面包含了呼叫出租车有关的方法，passengerCount用于生成线程安全的乘客编号，
    num表示乘客编号，startLocation表示乘客所在的位置，destinationLocation表示乘客的目的地，cars表示整
    个车队列，fitCars表示乘客选取的符合要求的出租车，center表示调度中心
    */

    private static AtomicInteger passengerCount = new AtomicInteger();
    private int num;/*乘客编号*/
    private Location startLocation;/*初始坐标*/
    private Location destinationLocation;/*目的地坐标*/
    private HashSet<Car> cars = new HashSet<>();/*出租车队列*/
    private HashSet<Car> fitCars = new HashSet<>();/*满足条件的出租车队列*/
//    private boolean accept = false;/*是否接受*/
    private Center center;

    /**
     * 构造器1
     */
    public Passenger(Center center) {
        //Requires: Center不为NULL
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
        //Requires: center,startLocation,destinationLocation不为NULL
        //Effects: 构造器
//        passengerCount.addAndGet(1);
        num = passengerCount.addAndGet(1);
        this.startLocation = startLocation;
        this.destinationLocation = destinationLocation;
        this.cars = center.getCars();
        this.center = center;
    }

    public boolean repOK(){
        //Effects: returns true if the rep variant holds for this, otherwise returns false
        if(passengerCount.intValue() < 0) return false;
        if(num <= 0) return false;
        if(startLocation.getX() < 0 || startLocation.getX() >= 80
                || startLocation.getY() < 0 || startLocation.getY() >= 80) return false;
        if(destinationLocation.getX() < 0 || destinationLocation.getX() >= 80
                || destinationLocation.getY() < 0 || destinationLocation.getY() >= 80) return false;
//        Iterator<Car> iterator = cars.iterator();
//        while(iterator.hasNext()){
//            if(iterator.next() == null) return false;
//        }
//        Iterator<Car> iterator1 = fitCars.iterator();
//        while(iterator1.hasNext()){
//            if(iterator1.next() == null) return false;
//        }
        if(center == null) return false;
        return true;
    }

    public int getNum() {
        //Requires: none
        //Modifies: none
        //Effects: 返回num属性
        return num;
    }

    public Location getStartLocation() {
        //Requires: none
        //Modifies: none
        //Effects: 返回乘客startLocation属性
        return startLocation;
    }

    public Location getDestinationLocation() {
        //Requires: none
        //Modifies: none
        //Effects: 返回destinationLocation属性
        return destinationLocation;
    }

    /**
     * 遍历出租车队列寻找满足条件的出租车
     * @return 找到满足条件的出租车，返回真
     */
    public boolean traverseCars(){
        //Requires: none
        //Modifies: fitCars属性
        //Effects: 遍历寻找合适的出租车加入fitCars队列
        Iterator<Car> carIterator = cars.iterator();
        while (carIterator.hasNext()) {
            Car car = carIterator.next();
            if (inPassengerRange(startLocation, car.getLocation())
                    && !car.isHasPassenger()) {
                fitCars.add(car);
                car.addCredit(1);
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
        //Effects: 寻找最终选择的出租车给chosenCar赋值，返回chosenCar
        Iterator<Car> carIterator = fitCars.iterator();
        Car chosenCar = carIterator.next();
        int chosenCarPathSize = chosenCar.singleFindPathByFlow(chosenCar.getLocation(), startLocation).size();
        while (carIterator.hasNext()) {
            Car car = carIterator.next();
            int carPathSize = car.singleFindPathByFlow(car.getLocation(), startLocation).size();
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
        //Requires: passengerLocation，carLocation不为NULL
        //Modifies: none
        //Effects: 如果carLocation在passengerLocation的呼叫范围内，返回true
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
//                    System.out.println(num + " 号乘客无可用车");
                } else {
                    Car car = findBestFitCar();
                    if (car != null) {
                        center.setChosenCars(new AskedCar(this, car));
                        car.setHasPassenger(true);
//                        System.out.println(num + " 号乘客上了 " + car.getNum() + " 号车");
                    } else {
//                        System.out.println(num + " 号乘客无可用车");
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