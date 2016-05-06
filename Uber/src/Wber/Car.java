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
    private Point[][] points = new Point[80][80];/*地图*/
    private Location startLocation;/*乘客起点*/
    private Location destinationLocation;/*乘客终点*/
    private boolean hasPassenger = false;

    /**
     * 构造器
     */
    public Car(Point[][] points) {
        //Requires: Point[][]
        //Modifies: none
        //Effects: 构造器
        carState = CarState.Waiting;
        this.points = points;
        location = new Location();
        count.addAndGet(1);
        num = count.get();
    }

    /**
     * 构造器2
     */
    public Car(Point[][] points, int x, int y){
        //Requires: Point，横坐标，纵坐标
        //Modifies: none
        //Effects: 构造器
        carState = CarState.Waiting;
        this.points = points;
        location = new Location(x, y);
        num = count.addAndGet(1);
    }
    /*获取当前出租车是否有乘客*/
    public void setHasPassenger(boolean hasPassenger) {
        //Requires: 要设置的状态
        //Modifies: 出租车的是否带有乘客的状态
        //Effects: 设置出租车的是否带有乘客的状态
        this.hasPassenger = hasPassenger;
    }
    /*设置是否有乘客*/
    public boolean isHasPassenger() {
        //Requires: none
        //Modifies: none
        //Effects: 返回出租车是否带有乘客的状态
        return hasPassenger;
    }
    /*设置起始的位置*/
    public void setStartLocation(Location startLocation) {
        //Requires: 要设置的位置
        //Modifies: none
        //Effects: 出租车的位置
        this.startLocation = startLocation;
    }
    /*设置目的地*/
    public void setDestinationLocation(Location destinationLocation) {
        //Requires: 要设置的位置
        //Modifies: 出租车当前的位置
        //Effects: 改变出租车当前的位置
        this.destinationLocation = destinationLocation;
    }
    /*获取出租车当前位置*/
    public Location getLocation() {
        //Requires: none
        //Modifies: none
        //Effects: 返回车当前的位置
        return location.clone();
    }
    /*增加出租车指定大小信用度*/
    public void addCredit(int i) {
        //Requires: 增加的信用度
        //Modifies: 出租车的信用度
        //Effects: 增加指定大小的出租车信用度
        credit += i;
    }
    /*获取出租车信用度*/
    public int getCredit() {
        //Requires: none
        //Modifies: none
        //Effects: 返回出租车信用度
        return credit;
    }
    /*获取出租车编号*/
    public int getNum() {
        //Requires: none
        //Modifies: none
        //Effects: 返回出租车编号
        return num;
    }
    /*设置出租车运行状态*/
    public void setCarState(CarState carState) {
        //Requires: 要设置的出租车运行状态
        //Modifies: 出租车的运行状态
        //Effects: 将出租车的运行状态改为指定状态
        this.carState = carState;
    }
    /*获取出租车状态*/
    public CarState getCarState() {
        //Requires: none
        //Modifies: none
        //Effects: 返回出租车的状态
        return carState;
    }
    /*获取格式化时间*/
    public String getCurrentTimeDate(){
        //Requires: none
        //Modifies: none
        //Effects: 返回出租车的当前时间
        return new Date(System.currentTimeMillis()).toString();
    }
    /*获取毫秒单位时间*/
    public long getCurrentTimeLong(){
        //Requires: none
        //Modifies: none
        //Effects: 返回出租车的当前时间
        return System.currentTimeMillis();
    }
    /*输出出租车当前状态*/
    public void print() {
        //Requires: none
        //Modifies: none
        //Effects: 输出出租车的当前信息
        System.out.println(num + " 号出租车：");
        System.out.println("出租车坐标 " + location.getX() + " " + location.getY());
        System.out.println("当前时间 " + System.currentTimeMillis() + "ms  " + new Date(System.currentTimeMillis()));
    }

    /**
     * 通过单向广搜获得最短路径
     * @param startLocation       初始坐标
     * @param destinationLocation 目的地
     * @return 最短路径
     */
    public ArrayList<Integer> singleFindPathByFlow(Location startLocation, Location destinationLocation) {
        //Requires: 起始位置和终点位置
        //Modifies: none
        //Effects: 返回起点与终点之间的最短路径
        Point[][] pointMap = new Point[80][80];
        for(int i = 0; i < 80; i++){
            for(int j = 0; j < 80; j++)
            pointMap[i][j] = points[i][j].clone();
        }
        ArrayList<Integer> path = new ArrayList<>();
        Queue<Location> queue = new ArrayDeque<>();/*前序队列*/
        int[] records = new int[6400];
        boolean isArrived = false;
        pointMap[startLocation.getX()][startLocation.getY()].ifPreUsed = true;
        records[startLocation.oneDimensionalLoc()] = startLocation.oneDimensionalLoc();
        queue.add(startLocation);

        if (startLocation.oneDimensionalLoc() == destinationLocation.oneDimensionalLoc()) {/*起点与终点相同，直接返回*/
            return path;
        }

        while (!queue.isEmpty()) {
//            System.out.println("长度 " + queue.size());
            Location preLocation = queue.poll();
            Point prePoint = pointMap[preLocation.getX()][preLocation.getY()];
            if (prePoint.up) {
                Location nextLocation = new Location(preLocation.getX() - 1, preLocation.getY());
                Point nextPoint = pointMap[nextLocation.getX()][nextLocation.getY()];
                if(!Map.getBlocked(preLocation.oneDimensionalLoc(), nextLocation.oneDimensionalLoc())) {
                    if (!nextPoint.ifPreUsed && !isArrived) {/*未被前序走过并且未找到终点*/
                        nextPoint.sumFlow = prePoint.sumFlow + Map.getFlows(preLocation.oneDimensionalLoc(), nextLocation.oneDimensionalLoc());
                        records[nextLocation.oneDimensionalLoc()] = preLocation.oneDimensionalLoc();
                        queue.add(nextLocation);
                        nextPoint.ifPreUsed = true;
                    } else if (prePoint.sumFlow + Map.getFlows(preLocation.oneDimensionalLoc(), nextLocation.oneDimensionalLoc()) < nextPoint.sumFlow) {/*发现流量更小的路径，更新父节点*/
                        records[nextLocation.oneDimensionalLoc()] = preLocation.oneDimensionalLoc();
                        nextPoint.sumFlow = prePoint.sumFlow + Map.getFlows(preLocation.oneDimensionalLoc(), nextLocation.oneDimensionalLoc());
                    }
                    if (nextLocation.equals(destinationLocation)) {
                        isArrived = true;
//                        records[nextLocation.oneDimensionalLoc()] = preLocation.oneDimensionalLoc();
//                        endLoc = nextLocation.oneDimensionalLoc();
                    }
                    pointMap[nextLocation.getX()][nextLocation.getY()].down = false;
                }
            }
            if (prePoint.down) {
                Location nextLocation = new Location(preLocation.getX() + 1, preLocation.getY());
                Point nextPoint = pointMap[nextLocation.getX()][nextLocation.getY()];
                if(!Map.getBlocked(preLocation.oneDimensionalLoc(), nextLocation.oneDimensionalLoc())) {
                    if (!nextPoint.ifPreUsed && !isArrived) {/*未被前序走过*/
                        nextPoint.sumFlow = prePoint.sumFlow + Map.getFlows(preLocation.oneDimensionalLoc(), nextLocation.oneDimensionalLoc());
                        records[nextLocation.oneDimensionalLoc()] = preLocation.oneDimensionalLoc();
                        queue.add(nextLocation);
                        nextPoint.ifPreUsed = true;
                    } else if (prePoint.sumFlow + Map.getFlows(preLocation.oneDimensionalLoc(), nextLocation.oneDimensionalLoc()) < nextPoint.sumFlow) {/*发现流量更小的路径，更新父节点*/
                        records[nextLocation.oneDimensionalLoc()] = preLocation.oneDimensionalLoc();
                        nextPoint.sumFlow = prePoint.sumFlow + Map.getFlows(preLocation.oneDimensionalLoc(), nextLocation.oneDimensionalLoc());
                    }
                    if (nextLocation.equals(destinationLocation)) {
                        isArrived = true;
//                        records[nextLocation.oneDimensionalLoc()] = preLocation.oneDimensionalLoc();
//                        endLoc = nextLocation.oneDimensionalLoc();
                    }
                    pointMap[nextLocation.getX()][nextLocation.getY()].up = false;
                }
            }
            if (prePoint.left) {
                Location nextLocation = new Location(preLocation.getX(), preLocation.getY() - 1);
                Point nextPoint = pointMap[nextLocation.getX()][nextLocation.getY()];
                if(!Map.getBlocked(preLocation.oneDimensionalLoc(), nextLocation.oneDimensionalLoc())) {
                    if (!nextPoint.ifPreUsed && !isArrived) {/*未被前序走过*/
                        nextPoint.sumFlow = prePoint.sumFlow + Map.getFlows(preLocation.oneDimensionalLoc(), nextLocation.oneDimensionalLoc());
                        records[nextLocation.oneDimensionalLoc()] = preLocation.oneDimensionalLoc();
                        queue.add(nextLocation);
                        nextPoint.ifPreUsed = true;
                    } else if (prePoint.sumFlow + Map.getFlows(preLocation.oneDimensionalLoc(), nextLocation.oneDimensionalLoc()) < nextPoint.sumFlow) {/*发现流量更小的路径，更新父节点*/
                        records[nextLocation.oneDimensionalLoc()] = preLocation.oneDimensionalLoc();
                        nextPoint.sumFlow = prePoint.sumFlow + Map.getFlows(preLocation.oneDimensionalLoc(), nextLocation.oneDimensionalLoc());
                    }
                    if (nextLocation.equals(destinationLocation)) {
                        isArrived = true;
//                        records[nextLocation.oneDimensionalLoc()] = preLocation.oneDimensionalLoc();
//                        endLoc = nextLocation.oneDimensionalLoc();
                    }
                    pointMap[nextLocation.getX()][nextLocation.getY()].right = false;
                }
            }
            if (prePoint.right) {
                Location nextLocation = new Location(preLocation.getX(), preLocation.getY() + 1);
                Point nextPoint = pointMap[nextLocation.getX()][nextLocation.getY()];
                if(!Map.getBlocked(preLocation.oneDimensionalLoc(), nextLocation.oneDimensionalLoc())) {
                    if (!nextPoint.ifPreUsed && !isArrived) {/*未被前序走过*/
                        nextPoint.sumFlow = prePoint.sumFlow + Map.getFlows(preLocation.oneDimensionalLoc(), nextLocation.oneDimensionalLoc());
                        records[nextLocation.oneDimensionalLoc()] = preLocation.oneDimensionalLoc();
                        queue.add(nextLocation);
                        nextPoint.ifPreUsed = true;
                    } else if (prePoint.sumFlow + Map.getFlows(preLocation.oneDimensionalLoc(), nextLocation.oneDimensionalLoc()) < nextPoint.sumFlow) {/*发现流量更小的路径，更新父节点*/
                        records[nextLocation.oneDimensionalLoc()] = preLocation.oneDimensionalLoc();
                        nextPoint.sumFlow = prePoint.sumFlow + Map.getFlows(preLocation.oneDimensionalLoc(), nextLocation.oneDimensionalLoc());
                    }
                    if (nextLocation.equals(destinationLocation)) {
                        isArrived = true;
//                        System.out.println("arrived");
//                        records[nextLocation.oneDimensionalLoc()] = preLocation.oneDimensionalLoc();
//                        endLoc = nextLocation.oneDimensionalLoc();
                    }
                    pointMap[nextLocation.getX()][nextLocation.getY()].left = false;
                }
            }
        }

        if(!isArrived){
            System.out.println("未发现连通路径，地图非连通，此次程序运行结束，请检查地图后再次运行");
            System.exit(0);
        }

        Stack<Integer> backPath = new Stack<>();
        int parentLoc = destinationLocation.oneDimensionalLoc();
        int childLoc;
        /*将前序队列加入路径*/
        do {
            backPath.push(parentLoc);
            childLoc = parentLoc;
            parentLoc = records[childLoc];
            if(backPath.size() > 6400) {
//                System.out.println("out of stack");
//                Thread.currentThread().interrupt();
                break;
            }
        } while (parentLoc != childLoc);
        /*加入路径*/
        while (backPath.size() > 0) {
            path.add(backPath.pop());
        }
        path.remove(0);
        return path;
    }

    /**
     * 通过双向广搜获得最短路径
     * @param startLocation       初始坐标
     * @param destinationLocation 目的地
     * @return 最短路径
     */
    public ArrayList<Integer> findPath(Location startLocation, Location destinationLocation) {
        //Requires: 起始位置和终点位置
        //Modifies: none
        //Effects: 返回起点与终点之间的最短路径
        Point[][] point = new Point[80][80];
        for(int i = 0; i < 80; i++){
            for(int j = 0; j < 80; j++)
            point[i][j] = points[i][j].clone();
        }
        ArrayList<Integer> path = new ArrayList<>();
        Queue<Location> preQueue = new ArrayDeque<>();/*前序队列*/
        Queue<Location> lastQueue = new ArrayDeque<>();/*后序队列*/
        int[] records = new int[6400];
        int lastSonLoc = startLocation.oneDimensionalLoc();
        int preSonLoc = destinationLocation.oneDimensionalLoc();
        point[startLocation.getX()][startLocation.getY()].ifPreUsed = true;
        records[startLocation.oneDimensionalLoc()] = startLocation.oneDimensionalLoc();
        preQueue.add(startLocation);
        point[destinationLocation.getX()][destinationLocation.getY()].ifLastUsed = true;
        records[destinationLocation.oneDimensionalLoc()] = destinationLocation.oneDimensionalLoc();
        lastQueue.add(destinationLocation);

        if (startLocation.oneDimensionalLoc() == destinationLocation.oneDimensionalLoc()) {/*起点与终点相同，直接返回*/
            return path;
        }

        while (true) {
            if (!preQueue.isEmpty()) {
                Location preLocation = preQueue.poll();
                Point p = point[preLocation.getX()][preLocation.getY()];
                if (p.up && inRange(preLocation.getX() - 1) && inRange(preLocation.getY())
                        && !point[preLocation.getX() - 1][preLocation.getY()].ifPreUsed) {
                    Location nextLocation = new Location(preLocation.getX() - 1, preLocation.getY());
                    Point nextPoint = point[nextLocation.getX()][nextLocation.getY()];
                    if (nextPoint.ifLastUsed) {
                        preSonLoc = preLocation.oneDimensionalLoc();
                        lastSonLoc = nextLocation.oneDimensionalLoc();
                        break;
                    }
                    if (!nextPoint.ifPreUsed) {/*未被前序走过*/
                        records[nextLocation.oneDimensionalLoc()] = preLocation.oneDimensionalLoc();
                        preQueue.add(nextLocation);
                        nextPoint.ifPreUsed = true;
                    }
                }
                if (p.down && inRange(preLocation.getX() + 1) && inRange(preLocation.getY())
                        && !point[preLocation.getX() + 1][preLocation.getY()].ifPreUsed) {
                    Location nextLocation = new Location(preLocation.getX() + 1, preLocation.getY());
                    Point nextPoint = point[nextLocation.getX()][nextLocation.getY()];
                    if (nextPoint.ifLastUsed) {
                        preSonLoc = preLocation.oneDimensionalLoc();
                        lastSonLoc = nextLocation.oneDimensionalLoc();
                        break;
                    }
                    if (!nextPoint.ifPreUsed) {/*未被前序走过*/
                        records[nextLocation.oneDimensionalLoc()] = preLocation.oneDimensionalLoc();
                        preQueue.add(nextLocation);
                        nextPoint.ifPreUsed = true;
                    }
                }
                if (p.left && inRange(preLocation.getX()) && inRange(preLocation.getY() - 1)
                        && !point[preLocation.getX()][preLocation.getY() - 1].ifPreUsed) {
                    Location nextLocation = new Location(preLocation.getX(), preLocation.getY() - 1);
                    Point nextPoint = point[nextLocation.getX()][nextLocation.getY()];
                    if (nextPoint.ifLastUsed) {
                        preSonLoc = preLocation.oneDimensionalLoc();
                        lastSonLoc = nextLocation.oneDimensionalLoc();
                        break;
                    }
                    if (!nextPoint.ifPreUsed) {/*未被前序走过*/
                        records[nextLocation.oneDimensionalLoc()] = preLocation.oneDimensionalLoc();
                        preQueue.add(nextLocation);
                        nextPoint.ifPreUsed = true;
                    }
                }
                if (p.right && inRange(preLocation.getX()) && inRange(preLocation.getY() + 1)
                        && !point[preLocation.getX()][preLocation.getY() + 1].ifPreUsed) {
                    Location nextLocation = new Location(preLocation.getX(), preLocation.getY() + 1);
                    Point nextPoint = point[nextLocation.getX()][nextLocation.getY()];
                    if (nextPoint.ifLastUsed) {
                        preSonLoc = preLocation.oneDimensionalLoc();
                        lastSonLoc = nextLocation.oneDimensionalLoc();
                        break;
                    }
                    if (!nextPoint.ifPreUsed) {/*未被前序走过*/
                        records[nextLocation.oneDimensionalLoc()] = preLocation.oneDimensionalLoc();
                        preQueue.add(nextLocation);
                        nextPoint.ifPreUsed = true;
                    }
                }
            }

            if (!lastQueue.isEmpty()) {
                Location preLocation = lastQueue.poll();
                Point p = point[preLocation.getX()][preLocation.getY()];
                if (p.up && inRange(preLocation.getX() - 1) && inRange(preLocation.getY())
                        && !point[preLocation.getX() - 1][preLocation.getY()].ifLastUsed) {
                    Location nextLocation = new Location(preLocation.getX() - 1, preLocation.getY());
                    Point nextPoint = point[nextLocation.getX()][nextLocation.getY()];
                    if (nextPoint.ifPreUsed) {
                        lastSonLoc = preLocation.oneDimensionalLoc();
                        preSonLoc = nextLocation.oneDimensionalLoc();
                        break;
                    }
                    if (!nextPoint.ifLastUsed) {/*未被后序走过*/
                        records[nextLocation.oneDimensionalLoc()] = preLocation.oneDimensionalLoc();
                        lastQueue.add(nextLocation);
                        nextPoint.ifLastUsed = true;
                    }
                }
                if (p.down && inRange(preLocation.getX() + 1) && inRange(preLocation.getY())
                        && !point[preLocation.getX() + 1][preLocation.getY()].ifLastUsed) {
                    Location nextLocation = new Location(preLocation.getX() + 1, preLocation.getY());
                    Point nextPoint = point[nextLocation.getX()][nextLocation.getY()];
                    if (nextPoint.ifPreUsed) {
                        lastSonLoc = preLocation.oneDimensionalLoc();
                        preSonLoc = nextLocation.oneDimensionalLoc();
                        break;
                    }
                    if (!nextPoint.ifLastUsed) {/*未被后序走过*/
                        records[nextLocation.oneDimensionalLoc()] = preLocation.oneDimensionalLoc();
                        lastQueue.add(nextLocation);
                        nextPoint.ifLastUsed = true;
                    }
                }
                if (p.left && inRange(preLocation.getX()) && inRange(preLocation.getY() - 1)
                        && !point[preLocation.getX()][preLocation.getY() - 1].ifLastUsed) {
                    Location nextLocation = new Location(preLocation.getX(), preLocation.getY() - 1);
                    Point nextPoint = point[nextLocation.getX()][nextLocation.getY()];
                    if (nextPoint.ifPreUsed) {
                        lastSonLoc = preLocation.oneDimensionalLoc();
                        preSonLoc = nextLocation.oneDimensionalLoc();
                        break;
                    }
                    if (!nextPoint.ifLastUsed) {/*未被后序走过*/
                        records[nextLocation.oneDimensionalLoc()] = preLocation.oneDimensionalLoc();
                        lastQueue.add(nextLocation);
                        nextPoint.ifLastUsed = true;
                    }
                }
                if (p.right && inRange(preLocation.getX()) && inRange(preLocation.getY() + 1)
                        && !point[preLocation.getX()][preLocation.getY() + 1].ifLastUsed) {
                    Location nextLocation = new Location(preLocation.getX(), preLocation.getY() + 1);
                    Point nextPoint = point[nextLocation.getX()][nextLocation.getY()];
                    if (nextPoint.ifPreUsed) {
                        lastSonLoc = preLocation.oneDimensionalLoc();
                        preSonLoc = nextLocation.oneDimensionalLoc();
                        break;
                    }
                    if (!nextPoint.ifLastUsed) {/*未被后序走过*/
                        records[nextLocation.oneDimensionalLoc()] = preLocation.oneDimensionalLoc();
                        lastQueue.add(nextLocation);
                        nextPoint.ifLastUsed = true;
                    }
                }
            }

            if (preQueue.isEmpty() && lastQueue.isEmpty()) {
                break;
            }
        }

        Stack<Integer> backPath = new Stack<>();
        int parentLoc = preSonLoc;
        if(preSonLoc == -1) {
//            System.out.println(startLocation.getX() + " " + startLocation.getY());
//            System.out.println(destinationLocation.getX() + " " + destinationLocation.getY());
        }
        int childLoc;
        /*将前序队列加入路径*/
        do {
            backPath.push(parentLoc);
            childLoc = parentLoc;
            parentLoc = records[childLoc];
            if(backPath.size() > 6400) {
                break;
            }
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
            if(backPath.size() > 6401) {
                break;
            }
        } while (parentLoc != childLoc);

        path.remove(0);/*去除第一个出租车所在的点*/
        return path;
    }

    public boolean inRange(int num) {
        //Requires: 要判断的数字
        //Modifies: none
        //Effects: 返回数字是否合法
        return 0 <= num && num <= 79;
    }

    /**
     * 出租车按照固定路线驶向指定目标
     * @param path 指定路线
     * @throws InterruptedException
     */
    public void moveToDestination(ArrayList<Integer> path, Location destinationLocation) throws InterruptedException {
        //Requires: 路径, 终点
        //Modifies: none
        //Effects: 将出租车在路径上移动
//        for (Integer anArrayList : path) {
//            System.out.println(anArrayList / 80 + " " + anArrayList % 80);
//        }
        Location preLocation = location.clone();
        for (int i = 0; i < path.size(); i++) {
            Thread.sleep(100);
            if(Map.getBlocked(preLocation.oneDimensionalLoc(), path.get(i))){
                moveToDestination(singleFindPathByFlow(preLocation, destinationLocation), destinationLocation);
                break;
            }
            location.setXY(path.get(i) / 80, path.get(i) % 80);
            Map.setFlows(preLocation.oneDimensionalLoc(), location.oneDimensionalLoc());
//            System.out.println(Map.getFlows(preLocation.oneDimensionalLoc(), location.oneDimensionalLoc()));
            preLocation = location.clone();
        }
//        while(path.size() == 0){
//            Thread.sleep(100);
//            location.setXY(path.get(0) / 80, path.get(0) % 80);
//            Map.setFlows(preLocation.oneDimensionalLoc(), location.oneDimensionalLoc());
//            preLocation = location.clone();
//            path.clear();
//            path.addAll(singleFindPathByFlow(location, destinationLocation));
//        }
    }

    /**
     * 在等待状态下随机移动
     */
    public void waitingMove() throws InterruptedException {
        //Requires: none
        //Modifies: none
        //Effects: 随机改变出租车的位置
        Random random = new Random();
        long startTime = System.currentTimeMillis();
        while (carState == CarState.Waiting) {
            Point point = points[location.getX()][location.getY()];
            long endTime = System.currentTimeMillis();
            int moveDirection = random.nextInt(4);
            if (moveDirection == 0 && point.up) {
                moveUp();
            }
            if (moveDirection == 1 && point.down) {
                moveDown();
            }
            if (moveDirection == 2 && point.left) {
                moveLeft();
            }
            if (moveDirection == 3 && point.right) {
                moveRight();
            }
            if (endTime - startTime > 20000) {/*运行时间到达20秒，停止1秒*/
                startTime = endTime;
                Thread.sleep(1000);
            }
        }
    }

    public void waitingMoveFlow() throws InterruptedException {
        //Requires: none
        //Modifies: 出租车的位置
        //Effects: 随机移动出租车改变出租车的位置
        Random random = new Random();
        long startTime = System.currentTimeMillis();
        while(carState == CarState.Waiting){
            ArrayList<Direction> arrayList = new ArrayList<>();
            int upFlow = Map.getFlows(location.oneDimensionalLoc(), location.upLocation().oneDimensionalLoc());
            int downFlow = Map.getFlows(location.oneDimensionalLoc(), location.downLocation().oneDimensionalLoc());
            int leftFlow = Map.getFlows(location.oneDimensionalLoc(), location.leftLocation().oneDimensionalLoc());
            int rightFlow = Map.getFlows(location.oneDimensionalLoc(), location.rightLocation().oneDimensionalLoc());
            int flag = Integer.MAX_VALUE;
            arrayList.add(Direction.UP);
            long endTime = System.currentTimeMillis();
            Point point = points[location.getX()][location.getY()];

            if(upFlow < flag && point.up) {
                arrayList.clear();
                arrayList.add(Direction.UP);
                flag = upFlow;
            }
            else if(upFlow == flag && point.up) {
                arrayList.add(Direction.UP);
            }
            if(downFlow < flag && point.down) {
                arrayList.clear();
                arrayList.add(Direction.DOWN);
                flag = downFlow;
            }
            else if(downFlow == flag && point.down) {
                arrayList.add(Direction.DOWN);
            }
            if(leftFlow < flag && point.left) {
                arrayList.clear();
                arrayList.add(Direction.LEFT);
                flag = leftFlow;
            }
            else if(leftFlow == flag && point.left) {
                arrayList.add(Direction.LEFT);
            }
            if(rightFlow < flag && point.right) {
                arrayList.clear();
                arrayList.add(Direction.RIGHT);
                flag = rightFlow;
            }
            else if(rightFlow == flag && point.right) {
                arrayList.add(Direction.RIGHT);
            }

            int moveDirection = random.nextInt(arrayList.size());
            if(arrayList.get(moveDirection) == Direction.UP)
                moveUp();
            if(arrayList.get(moveDirection) == Direction.DOWN)
                moveDown();
            if(arrayList.get(moveDirection) == Direction.LEFT)
                moveLeft();
            if(arrayList.get(moveDirection) == Direction.RIGHT)
                moveRight();

            if (endTime - startTime > 20000) {/*运行时间到达20秒，停止1秒*/
                startTime = endTime;
                Thread.sleep(1000);
            }
        }
    }

    public void moveUp() throws InterruptedException {
        //Requires: none
        //Modifies: 出租车的位置
        //Effects: 将出租车向上移动
        Map.setFlows(location.oneDimensionalLoc(), location.setX(location.getX() - 1).oneDimensionalLoc());
//        location.setX(location.getX() - 1);
        Thread.sleep(100);
    }

    public void moveDown() throws InterruptedException {
        //Requires: none
        //Modifies: 出租车的位置
        //Effects: 将出租车向下移动
        Map.setFlows(location.oneDimensionalLoc(), location.setX(location.getX() + 1).oneDimensionalLoc());
//        location.setX(location.getX() + 1);
        Thread.sleep(100);
    }

    public void moveLeft() throws InterruptedException {
        //Requires: none
        //Modifies: 出租车的位置
        //Effects: 将出租车向左移动
        Map.setFlows(location.oneDimensionalLoc(), location.setY(location.getY() - 1).oneDimensionalLoc());
//        location.setY(location.getY() - 1);
        Thread.sleep(100);
    }

    public void moveRight() throws InterruptedException {
        //Requires: none
        //Modifies: 出租车的位置
        //Effects: 将出租车向右移动
        Map.setFlows(location.oneDimensionalLoc(), location.setY(location.getY() + 1).oneDimensionalLoc());
//        location.setY(location.getY() + 1);
        Thread.sleep(100);
    }

    @Override
    public void run() {
        try {
            ArrayList<Integer> path = new ArrayList<>();
            while (true) {
                if (carState == CarState.Waiting) {
//                    System.out.println("waiting");
                    waitingMoveFlow();
                } else if (carState == CarState.Stopping) {
//                    System.out.println("Stopping");
                } else if (carState == CarState.Serving) {
//                    System.out.println("Serving");
                    path.clear();
                    path.addAll(singleFindPathByFlow(location, destinationLocation));
//                    path.addAll(findPath(location, destinationLocation));
                    moveToDestination(path, destinationLocation);
                    System.out.println(num + " 号出租车到达目的地");
                    addCredit(3);/*完成订单，信用度加三*/
                    hasPassenger = false;
                    carState = CarState.Stopping;
                    Thread.sleep(1000);/*等待乘客下车*/
                    carState = CarState.Waiting;
                } else if (carState == CarState.WaitServing) {
                    addCredit(1);/*抢单成功，信用度加一*/
//                    System.out.println("location " + location.getX() + " " + location.getY());
//                    System.out.println("startLocation " + startLocation.getX() + " " + startLocation.getY());
                    path.clear();
                    path.addAll(singleFindPathByFlow(location, startLocation));
//                    path.addAll(findPath(location, startLocation));
                    moveToDestination(path, startLocation);/*去乘客所在位置*/
                    carState = CarState.Stopping;
                    Thread.sleep(1000);/*等待乘客上车*/
                    carState = CarState.Serving;
                }
            }
        } catch (Throwable t) {
//            t.printStackTrace();
            System.out.println(num + " 号出租车停止运行");
            Thread.currentThread().interrupt();
        }
    }
}

enum CarState{
    Waiting,/*等待状态*/
    Serving,/*服务状态*/
    Stopping,/*停止状态*/
    WaitServing/*等待服务状态*/
}

enum Direction{
    UP,
    DOWN,
    LEFT,
    RIGHT
}