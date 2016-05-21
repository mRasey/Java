package Wber;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 出租车类
 */
public class Car implements Runnable {
    /*Overview:
    这个类是出租车类，用于产生出租车，含有出租车正常运行所需的方法，count用于计算车的编号，num表示车的编号，
    location表示车当前的坐标，credit表示车的信用度，carState表示车的运行状态，points表示地图，startLocation
    表示车接受的乘客所在的位置，destinationLocation表示接受的乘客的目的地，hasPassenger表示车当前是否分配有
    未完成的乘客请求，direction表示车的运行方向，servedPassengerQueue保存了每一次请求的乘客。
    */

    static AtomicInteger count = new AtomicInteger();
    int num;
    Location location;/*坐标*/
    int credit = 0;/*信用度*/
    CarState carState;/*运行状态*/
    Point[][] points = new Point[80][80];/*地图*/
    Location startLocation;/*乘客起点*/
    Location destinationLocation;/*乘客终点*/
    boolean hasPassenger = false;
    Direction direction = Direction.UP;
    ArrayList<Passenger> servedPassengerQueue = new ArrayList<>();


    /**
     * 构造器
     */
    public Car(Point[][] points) {
        //Requires: 80X80的points[][]数组
        //Effects: 构造器
        carState = CarState.Waiting;
        this.points = points;
        location = new Location();
        num = count.addAndGet(1);
        if(num > 100){
            System.out.println("生成了过多的出租车，程序退出");
            System.exit(0);
        }
    }

    /**
     * 构造器2
     */
    public Car(Point[][] points, int x, int y){
        //Requires: 80X80的points[][]数组，x范围[0,79]，y范围[0,79]
        //Effects: 构造器，初始化变量
        carState = CarState.Waiting;
        this.points = points;
        location = new Location(x, y);
        num = count.addAndGet(1);
        if(num > 100){
            System.out.println("生成了过多的出租车，程序退出");
            System.exit(0);
        }
    }

    public boolean repOK(){
        //Effects: returns true if the rep variant holds for this, otherwise returns false
        if(count.intValue() < 0 || count.intValue() > 100) return false;
        if(num < 0 || num >100) return false;
        if(location.getX() < 0 || location.getX() >= 80
                || location.getY() < 0 || location.getY() >= 80) return false;
        if(credit < 0) return false;
        if(servedPassengerQueue.size() < 0) return false;
//        if(startLocation.getX() < 0 || startLocation.getX() >= 80
//                || startLocation.getY() < 0 || startLocation.getY() >= 80) return false;
//        if(destinationLocation.getX() < 0 || destinationLocation.getX() >= 80
//                || destinationLocation.getY() < 0 || destinationLocation.getY() >= 80) return false;
//        if(carState != CarState.Waiting && carState != CarState.Serving
//                && carState != CarState.Stopping && carState != CarState.WaitServing) return false;
//        if(direction != Direction.UP && direction != Direction.DOWN
//                && direction != Direction.LEFT && direction != Direction.RIGHT) return false;
//        if(!hasPassenger && hasPassenger) return false;
//        for(int i = 0; i < 80; i++){
//            for(int j = 0; j < 80; j++){
//                if(!(points[i][j] instanceof Point))
//                    return false;
//            }
//        }
        return true;
    }

    /*获取当前出租车是否有乘客*/
    public void setHasPassenger(boolean hasPassenger) {
        //Requires: boolean型的hasPassenger
        //Modifies: hasPassenger属性
        //Effects: 设置hasPassenger属性为hasPassenger
        this.hasPassenger = hasPassenger;
    }
    /*设置是否有乘客*/
    public boolean isHasPassenger() {
        //Requires: none
        //Modifies: none
        //Effects: 返回hasPassenger属性
        return hasPassenger;
    }
    /*设置起始的位置*/
    public void setStartLocation(Location startLocation) {
        //Requires: startLocation不为NULL
        //Modifies: startLocation属性
        //Effects: 设置startLocation属性为startLocation
        this.startLocation = startLocation;
    }
    /*设置目的地*/
    public void setDestinationLocation(Location destinationLocation) {
        //Requires: destinationLocation不为NULL
        //Modifies: destinationLocation属性
        //Effects: 改变destinationLocation属性为destinationLocation
        this.destinationLocation = destinationLocation;
    }
    /*获取出租车当前位置*/
    public Location getLocation() {
        //Requires: none
        //Modifies: none
        //Effects: 返回location属性
        return location.clone();
    }
    /*增加出租车指定大小信用度*/
    public void addCredit(int i) {
        //Requires: i为1或3
        //Modifies: credit属性
        //Effects: credit属性增加i
        credit += i;
    }
    /*获取出租车信用度*/
    public int getCredit() {
        //Requires: none
        //Modifies: none
        //Effects: 返回credit属性
        return credit;
    }
    /*获取出租车编号*/
    public int getNum() {
        //Requires: none
        //Modifies: none
        //Effects: 返回num属性
        return num;
    }
    /*设置出租车运行状态*/
    public void setCarState(CarState carState) {
        //Requires: carState不为NULL
        //Modifies: carState属性
        //Effects: 将carState属性设置为carState
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
        //Effects: 输出出租车的location属性和当前系统时间
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
        //Requires: startLocation和destinationLocation不为NULL
        //Modifies: none
        //Effects: 返回startLocation与destinationLocation之间的最短路径
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
        //Requires: startLocation和destinationLocation不为NULL
        //Modifies: none
        //Effects: 返回startLocation与destinationLocation之间的最短路径
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
//            System.out.println("hahahhhahahahaha");
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
        //Requires: none
        //Modifies: none
        //Effects: 如果num在0和79之间，返回true，否则返回false
        return 0 <= num && num <= 79;
    }

    /**
     * 出租车按照固定路线驶向指定目标
     * @param path 指定路线
     * @throws InterruptedException
     */
    public void moveToDestination(ArrayList<Integer> path, Location destinationLocation) throws InterruptedException {
        //Requires: destinationLocation不为NULL
        //Modifies: location和carState属性，经过道路的流量
        //Effects: 将car按照path移动到指定的destinationLocation
        Location preLocation = location.clone();
        Location nextLocation = new Location();
        for (int i = 0; i < path.size(); i++) {
            if(Map.getBlocked(preLocation.oneDimensionalLoc(), path.get(i))){
                moveToDestination(singleFindPathByFlow(preLocation, destinationLocation), destinationLocation);
                break;
            }
            nextLocation.setXY(path.get(i) / 80, path.get(i) % 80);
            while(points[preLocation.getX()][preLocation.getY()].hasTrafficLight && nextLocation.equals(preLocation.upLocation())
                    && direction != Direction.LEFT && TrafficLight.verticalLight == LightColor.RED){
                Thread.sleep(1);
                direction = Direction.UP;
//                System.out.println("向上红灯");
            }
            while(points[preLocation.getX()][preLocation.getY()].hasTrafficLight && nextLocation.equals(preLocation.downLocation())
                    && direction != Direction.RIGHT && TrafficLight.verticalLight == LightColor.RED){
                Thread.sleep(1);
                direction = Direction.DOWN;
//                System.out.println("向下红灯");
            }
            while(points[preLocation.getX()][preLocation.getY()].hasTrafficLight && nextLocation.equals(preLocation.leftLocation())
                    && direction != Direction.DOWN && TrafficLight.crossLight == LightColor.RED){
                Thread.sleep(1);
                direction = Direction.LEFT;
//                System.out.println("向左红灯");
            }
            while(points[preLocation.getX()][preLocation.getY()].hasTrafficLight && nextLocation.equals(preLocation.rightLocation())
                    && direction != Direction.UP && TrafficLight.crossLight == LightColor.RED){
                Thread.sleep(1);
                direction = Direction.RIGHT;
//                System.out.println("向右红灯");
            }
            Thread.sleep(100);
            location.setXY(path.get(i) / 80, path.get(i) % 80);
            Map.setFlows(preLocation.oneDimensionalLoc(), location.oneDimensionalLoc());
            preLocation = location.clone();
        }
    }

    /**
     * 在等待状态下随机移动
     */
    public void waitingMove() throws InterruptedException {
        //Requires: none
        //Modifies: location和direction属性
        //Effects: 随机改变location和direction属性
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
                Thread.sleep(1000);
                startTime = System.currentTimeMillis();
            }
        }
    }

    public void waitingMoveFlow() throws InterruptedException {
        //Requires: none
        //Modifies: location和direction属性
        //Effects: 随机改变location和direction属性
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

            if(upFlow < flag && point.up
                    && !Map.getBlocked(location.oneDimensionalLoc(), location.upLocation().oneDimensionalLoc())) {
                arrayList.clear();
                arrayList.add(Direction.UP);
                flag = upFlow;
            }
            else if(upFlow == flag && point.up
                    && !Map.getBlocked(location.oneDimensionalLoc(), location.upLocation().oneDimensionalLoc())) {
                arrayList.add(Direction.UP);
            }
            if(downFlow < flag && point.down
                    && !Map.getBlocked(location.oneDimensionalLoc(), location.downLocation().oneDimensionalLoc())) {
                arrayList.clear();
                arrayList.add(Direction.DOWN);
                flag = downFlow;
            }
            else if(downFlow == flag && point.down
                    && !Map.getBlocked(location.oneDimensionalLoc(), location.downLocation().oneDimensionalLoc())) {
                arrayList.add(Direction.DOWN);
            }
            if(leftFlow < flag && point.left
                    && !Map.getBlocked(location.oneDimensionalLoc(), location.leftLocation().oneDimensionalLoc())) {
                arrayList.clear();
                arrayList.add(Direction.LEFT);
                flag = leftFlow;
            }
            else if(leftFlow == flag && point.left
                    && !Map.getBlocked(location.oneDimensionalLoc(), location.leftLocation().oneDimensionalLoc())) {
                arrayList.add(Direction.LEFT);
            }
            if(rightFlow < flag && point.right
                    && !Map.getBlocked(location.oneDimensionalLoc(), location.rightLocation().oneDimensionalLoc())) {
                arrayList.clear();
                arrayList.add(Direction.RIGHT);
                flag = rightFlow;
            }
            else if(rightFlow == flag && point.right
                    && !Map.getBlocked(location.oneDimensionalLoc(), location.rightLocation().oneDimensionalLoc())) {
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
                Thread.sleep(1000);
                startTime = System.currentTimeMillis();
            }
        }
    }

    public void moveUp() throws InterruptedException {
        //Requires: none
        //Modifies: location和direction属性，移动过的道路的流量
        //Effects: location的X加一，direction属性改为UP
        while(points[location.getX()][location.getY()].hasTrafficLight
                && direction != Direction.LEFT && TrafficLight.verticalLight == LightColor.RED) {
            Thread.sleep(1);
//            System.out.println("纵向红灯");
        }
        Map.setFlows(location.oneDimensionalLoc(), location.setX(location.getX() - 1).oneDimensionalLoc());
        Thread.sleep(100);
        direction = Direction.UP;
    }

    public void moveDown() throws InterruptedException {
        //Requires: none
        //Modifies: location和direction属性，移动过的道路的流量
        //Effects: location的X减一，direction属性改为DOWN
        while(points[location.getX()][location.getY()].hasTrafficLight
                && direction != Direction.RIGHT && TrafficLight.verticalLight == LightColor.RED) {
            Thread.sleep(1);
//            System.out.println("纵向红灯");
        }
        Map.setFlows(location.oneDimensionalLoc(), location.setX(location.getX() + 1).oneDimensionalLoc());
        Thread.sleep(100);
        direction = Direction.DOWN;
    }

    public void moveLeft() throws InterruptedException {
        //Requires: none
        //Modifies: location和direction属性，移动过的道路的流量
        //Effects: location的Y减一，direction属性改为LEFT
        while(points[location.getX()][location.getY()].hasTrafficLight
                && direction != Direction.DOWN && TrafficLight.crossLight == LightColor.RED) {
            Thread.sleep(1);
//            System.out.println("横向红灯");
        }
        Map.setFlows(location.oneDimensionalLoc(), location.setY(location.getY() - 1).oneDimensionalLoc());
        Thread.sleep(100);
        direction = Direction.LEFT;
    }

    public void moveRight() throws InterruptedException {
        //Requires: none
        //Modifies: location和direction属性，移动过的道路的流量
        //Effects: location的Y加一，direction属性改为RIGHT
        while(points[location.getX()][location.getY()].hasTrafficLight
                && direction != Direction.UP && TrafficLight.crossLight == LightColor.RED) {
            Thread.sleep(1);
//            System.out.println("横向红灯");
        }
        Map.setFlows(location.oneDimensionalLoc(), location.setY(location.getY() + 1).oneDimensionalLoc());
        Thread.sleep(100);
        direction = Direction.RIGHT;
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
                    path.clear();
                    path.addAll(singleFindPathByFlow(location, startLocation));
//                    path.addAll(findPath(location, destinationLocation));
                    moveToDestination(path, startLocation);/*去乘客所在位置*/
                    System.out.println(servedPassengerQueue.get(servedPassengerQueue.size()-1).getNum()
                            +  " 号乘客上了 " + num + " 号车");
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