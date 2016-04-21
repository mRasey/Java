package Wber;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 出租车类
 */
public class Car implements Runnable {
    private static AtomicInteger count = new AtomicInteger();
    private int num;
    private Location location;/*坐标*/
    private int credit = 0;/*信用度*/
    private CarState carState;/*运行状态*/
    private Point[][] points;/*地图*/
    private Location startLocation;/*乘客起点*/
    private Location destinationLocation;/*乘客终点*/
    private Set<Car> cars;/*出租车队列*/

    /**
     * 构造器
     */
    public Car(Point[][] points, Set<Car> cars) {
        carState = CarState.Waiting;
        this.points = points;
        location = new Location();
        this.cars = cars;
        cars.add(this);
        count.addAndGet(1);
        num = count.get();
//        System.out.println(num + " created");
    }

    public void setStartLocation(Location startLocation) {
        this.startLocation = startLocation;
    }

    public void setDestinationLocation(Location destinationLocation) {
        this.destinationLocation = destinationLocation;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(int x, int y) {
        this.location.setXY(x, y);
    }

    public void addCredit(int i) {
        credit += i;
    }

    public int getCredit() {
        return credit;
    }

    public int getNum() {
        return num;
    }

    public void setCarState(CarState carState) {
        this.carState = carState;
    }

    public CarState getCarState() {
        return carState;
    }

    /**
     * 初始化地图
     */
    public void initPoints() {
        for (int i = 0; i < 80; i++) {
            for (int j = 0; j < 80; j++) {
                points[i][j].ifPreUsed = false;
                points[i][j].ifLastUsed = false;
            }
        }
    }

    /**
     * 通过双向广搜获得最短路径
     *
     * @param startLocation       初始坐标
     * @param destinationLocation 目的地
     * @return 最短路径
     */
    public synchronized ArrayList<Integer> findPath(Location startLocation, Location destinationLocation) {
        initPoints();
        ArrayList<Integer> path = new ArrayList<>();
        Queue<Location> preQueue = new ArrayDeque<>();/*前序队列*/
        Queue<Location> lastQueue = new ArrayDeque<>();/*后序队列*/
        int[] records = new int[6400];
        int preSonLoc = destinationLocation.oneDimensionalLoc();
        int lastSonLoc = startLocation.oneDimensionalLoc();

//        System.out.println(startLocation.getX() + " " + destinationLocation.getX());
//        System.out.println(startLocation.oneDimensionalLoc() + " " + destinationLocation.oneDimensionalLoc());

        points[startLocation.getX()][startLocation.getY()].ifPreUsed = true;
        records[startLocation.oneDimensionalLoc()] = startLocation.oneDimensionalLoc();
        preQueue.add(startLocation);
        points[destinationLocation.getX()][destinationLocation.getY()].ifLastUsed = true;
        records[destinationLocation.oneDimensionalLoc()] = destinationLocation.oneDimensionalLoc();
        lastQueue.add(destinationLocation);

        if (startLocation.oneDimensionalLoc() == destinationLocation.oneDimensionalLoc()) {/*起点与终点相同，直接返回*/
            return path;
        }

        while (true) {
            if (!preQueue.isEmpty()) {
                Location preLocation = preQueue.poll();
                Point point = points[preLocation.getX()][preLocation.getY()];
                if (point.up && preLocation.getX() -1 >= 0) {
                    Location nextLocation = new Location(preLocation.getX() - 1, preLocation.getY());
                    Point nextPoint = points[nextLocation.getX()][nextLocation.getY()];
                    if (nextPoint.ifLastUsed) {
                        preSonLoc = preLocation.oneDimensionalLoc();
                        lastSonLoc = nextLocation.oneDimensionalLoc();
                        break;
                    }
                    if (!nextPoint.ifPreUsed) {/*未被前序走过*/
                        records[nextLocation.oneDimensionalLoc()] = preLocation.oneDimensionalLoc();
                        preQueue.add(nextLocation);
                        nextPoint.ifPreUsed = true;
//                        preSonLoc = nextLocation.oneDimensionalLoc();
                    }
                }
                if (point.down && preLocation.getX() + 1 < 80) {
                    Location nextLocation = new Location(preLocation.getX() + 1, preLocation.getY());
                    Point nextPoint = points[nextLocation.getX()][nextLocation.getY()];
                    if (nextPoint.ifLastUsed) {
                        preSonLoc = preLocation.oneDimensionalLoc();
                        lastSonLoc = nextLocation.oneDimensionalLoc();
                        break;
                    }
                    if (!nextPoint.ifPreUsed) {/*未被前序走过*/
                        records[nextLocation.oneDimensionalLoc()] = preLocation.oneDimensionalLoc();
                        preQueue.add(nextLocation);
                        nextPoint.ifPreUsed = true;
//                        preSonLoc = nextLocation.oneDimensionalLoc();
                    }
                }
                if (point.left && preLocation.getY() - 1 >= 0) {
                    Location nextLocation = new Location(preLocation.getX(), preLocation.getY() - 1);
                    Point nextPoint = points[nextLocation.getX()][nextLocation.getY()];
                    if (nextPoint.ifLastUsed) {
                        preSonLoc = preLocation.oneDimensionalLoc();
                        lastSonLoc = nextLocation.oneDimensionalLoc();
                        break;
                    }
                    if (!nextPoint.ifPreUsed) {/*未被前序走过*/
                        records[nextLocation.oneDimensionalLoc()] = preLocation.oneDimensionalLoc();
                        preQueue.add(nextLocation);
                        nextPoint.ifPreUsed = true;
//                        preSonLoc = nextLocation.oneDimensionalLoc();
                    }
                }
                if (point.right && preLocation.getY() + 1 < 80) {
                    Location nextLocation = new Location(preLocation.getX(), preLocation.getY() + 1);
                    Point nextPoint = points[nextLocation.getX()][nextLocation.getY()];
                    if (nextPoint.ifLastUsed) {
                        preSonLoc = preLocation.oneDimensionalLoc();
                        lastSonLoc = nextLocation.oneDimensionalLoc();
                        break;
                    }
                    if (!nextPoint.ifPreUsed) {/*未被前序走过*/
                        records[nextLocation.oneDimensionalLoc()] = preLocation.oneDimensionalLoc();
                        preQueue.add(nextLocation);
                        nextPoint.ifPreUsed = true;
//                        preSonLoc = nextLocation.oneDimensionalLoc();
                    }
                }
            }

            if (!lastQueue.isEmpty()) {
                Location preLocation = lastQueue.poll();
                Point point = points[preLocation.getX()][preLocation.getY()];
                if (point.up && preLocation.getX() - 1 >= 0) {
                    Location nextLocation = new Location(preLocation.getX() - 1, preLocation.getY());
                    Point nextPoint = points[nextLocation.getX()][nextLocation.getY()];
                    if (nextPoint.ifPreUsed) {
                        lastSonLoc = preLocation.oneDimensionalLoc();
                        preSonLoc = nextLocation.oneDimensionalLoc();
                        break;
                    }
                    if (!nextPoint.ifLastUsed) {/*未被后序走过*/
                        records[nextLocation.oneDimensionalLoc()] = preLocation.oneDimensionalLoc();
                        lastQueue.add(nextLocation);
                        nextPoint.ifLastUsed = true;
//                        lastSonLoc = nextLocation.oneDimensionalLoc();
                    }
                }
                if (point.down && preLocation.getX() + 1 < 80) {
                    Location nextLocation = new Location(preLocation.getX() + 1, preLocation.getY());
                    Point nextPoint = points[nextLocation.getX()][nextLocation.getY()];
                    if (nextPoint.ifPreUsed) {
                        lastSonLoc = preLocation.oneDimensionalLoc();
                        preSonLoc = nextLocation.oneDimensionalLoc();
                        break;
                    }
                    if (!nextPoint.ifLastUsed) {/*未被后序走过*/
                        records[nextLocation.oneDimensionalLoc()] = preLocation.oneDimensionalLoc();
                        lastQueue.add(nextLocation);
                        nextPoint.ifLastUsed = true;
//                        lastSonLoc = nextLocation.oneDimensionalLoc();
                    }
                }
                if (point.left && preLocation.getY() - 1 >= 0) {
                    Location nextLocation = new Location(preLocation.getX(), preLocation.getY() - 1);
                    Point nextPoint = points[nextLocation.getX()][nextLocation.getY()];
                    if (nextPoint.ifPreUsed) {
                        lastSonLoc = preLocation.oneDimensionalLoc();
                        preSonLoc = nextLocation.oneDimensionalLoc();
                        break;
                    }
                    if (!nextPoint.ifLastUsed) {/*未被后序走过*/
                        records[nextLocation.oneDimensionalLoc()] = preLocation.oneDimensionalLoc();
                        lastQueue.add(nextLocation);
                        nextPoint.ifLastUsed = true;
//                        lastSonLoc = nextLocation.oneDimensionalLoc();
                    }
                }
                if (point.right && preLocation.getY() + 1 < 80) {
                    Location nextLocation = new Location(preLocation.getX(), preLocation.getY() + 1);
                    Point nextPoint = points[nextLocation.getX()][nextLocation.getY()];
                    if (nextPoint.ifPreUsed) {
                        lastSonLoc = preLocation.oneDimensionalLoc();
                        preSonLoc = nextLocation.oneDimensionalLoc();
                        break;
                    }
                    if (!nextPoint.ifLastUsed) {/*未被后序走过*/
                        records[nextLocation.oneDimensionalLoc()] = preLocation.oneDimensionalLoc();
                        lastQueue.add(nextLocation);
                        nextPoint.ifLastUsed = true;
//                        lastSonLoc = nextLocation.oneDimensionalLoc();
                    }
                }
            }

            if (preQueue.isEmpty() && lastQueue.isEmpty()) {
                break;
            }
        }

        Stack<Integer> backPath = new Stack<>();
        int parentLoc = preSonLoc;
//        backPath.push(parentLoc);
        int childLoc;
        /*将前序队列加入路径*/
        do {
            backPath.push(parentLoc);
            childLoc = parentLoc;
            parentLoc = records[childLoc];
        } while (parentLoc != childLoc);
        while (backPath.size() > 0) {
            path.add(backPath.pop());
        }
        /*将后序队列加入路径*/
        parentLoc = lastSonLoc;
        do {
            path.add(parentLoc);
            childLoc = parentLoc;
            parentLoc = records[childLoc];
        } while (parentLoc != childLoc);

        path.remove(0);/*去除第一个出租车所在的点*/
        return path;
    }

    /**
     * 出租车按照固定路线驶向指定目标
     *
     * @param path 指定路线
     * @throws InterruptedException
     */
    public void moveToDestination(ArrayList<Integer> path) throws InterruptedException {
        for (int i = 0; i < path.size(); i++) {
            Thread.sleep(100);
            location.setXY(path.get(i) / 80, path.get(i) % 80);
        }
    }

    /**
     * 在等待状态下随机移动
     */
    public void waitingMove() throws InterruptedException {
        Random random = new Random();
        long startTime = System.currentTimeMillis();
        while (carState == CarState.Waiting) {
            if(location.getX() < 0 || location.getY() < 0)
                System.out.println("x " + location.getX() + " y " + location.getY());
            Point point = points[location.getX()][location.getY()];
            long endTime = System.currentTimeMillis();
            if (random.nextInt(3) == 0 && point.up) {
                moveUp();
//                System.out.println("moveUp");
            } else if (random.nextInt(3) == 1 && point.down) {
                moveDown();
//                System.out.println("moveDown");
            } else if (random.nextInt(3) == 2 && point.left) {
                moveLeft();
//                System.out.println("moveLeft");
            } else if (random.nextInt(3) == 3 && point.right) {
                moveRight();
//                System.out.println("moveRight");
            }
//            System.out.println(num + " " + location.getX() + " " + location.getY());
            if (endTime - startTime > 20000) {/*运行时间到达20秒，停止1秒*/
                Thread.sleep(1000);
                startTime = endTime;
            }
        }
    }

    public void moveUp() throws InterruptedException {
        if(location.getX() -  1 < 0)
            return;
        location.setY(location.getX() - 1);
        Thread.sleep(100);
    }

    public void moveDown() throws InterruptedException {
        if(location.getX() + 1 >= 80)
            return;
        location.setY(location.getX() + 1);
        Thread.sleep(100);
    }

    public void moveLeft() throws InterruptedException {
        if(location.getY() - 1 < 0)
            return;
        location.setX(location.getY() - 1);
        Thread.sleep(100);
    }

    public void moveRight() throws InterruptedException {
        if (location.getY() + 1 >= 80)
            return;
        location.setX(location.getY() + 1);
        Thread.sleep(100);
    }

    @Override
    public void run() {
        try {
            while (true) {
                Thread.sleep(1);
//                System.out.println(num + " waiting move");
                if (carState == CarState.Waiting) {
                    waitingMove();
                } else if (carState == CarState.Stopping) {

                } else if (carState == CarState.Serving) {
//                    System.out.println(num + " serving");
                    moveToDestination(findPath(startLocation, destinationLocation));
//                    System.out.println(num + " finish");
                    addCredit(3);/*完成订单，信用度加三*/
                    cars.add(this);/*将当前车加入等待队列*/
                    carState = CarState.Stopping;
//                    System.out.println(num + " stopping");
                    Thread.sleep(1000);
                    carState = CarState.Waiting;
//                    System.out.println(num + " waiting");
                } else if (carState == CarState.WaitServing) {
//                    System.out.println(num + " waitServing");
                    addCredit(1);/*抢单成功，信用度加一*/
                    cars.remove(this);/*将当前车从等待队列中移除*/
                    moveToDestination(findPath(location, startLocation));
                    carState = CarState.Stopping;
                    Thread.sleep(1000);
                    carState = CarState.Serving;
                }

            }
        } catch (Throwable t) {
            t.printStackTrace();
        } finally {
            System.out.println(num + " 号出租车停止运行");
            System.exit(0);
        }
    }
}

enum CarState{
    Waiting,/*等待状态*/
    Serving,/*服务状态*/
    Stopping,/*停止状态*/
    WaitServing/*等待服务状态*/
}