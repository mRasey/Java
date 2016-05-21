package Wber;

import java.util.*;

public class TracingCar extends Car implements Runnable{
    /*Overview:
    这个类继承了Car类，是可追踪出租车出租车类，servePath里的每一项都保存了当次服务的路径
    */
    private ArrayList<ArrayList<Integer>> servePath = new ArrayList<>();

    public TracingCar(Point[][] points) {
        //Requires: 80X80的points[][]数组
        //Effects: 构造器
        super(points);
    }

    public TracingCar(Point[][] points, int x, int y) {
        //Requires: 80X80的points[][]数组，x范围[0,79]，y范围[0,79]
        //Effects: 构造器，初始化变量
        super(points, x, y);
    }

    public boolean repOK(){
        //Effects: returns true if the rep variant holds for this, otherwise returns false
        if(!super.repOK())
            return false;
        for (ArrayList<Integer> aServePath : servePath) {
            if (aServePath.size() < 0)
                return false;
        }
        return true;
    }

    public int getServeCount(){
        //Requires: none
        //Modifies: none
        //Effects: 返回已经服务的乘客的次数
        return servePath.size();
    }

    public Passenger getServedPassenger(int i){
        //Requires: none
        //Modifies: none
        //Effects: 返回第i次服务的乘客的编号，如果没有则返回null
        i--;
        if(0 <= i && i < servePath.size())
            return servedPassengerQueue.get(i);
        return null;
    }

    /**
     * 通过单向广搜获得最短路径
     * @param startLocation       初始坐标
     * @param destinationLocation 目的地
     * @return 最短路径
     */
    @Override
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
            Location preLocation = queue.poll();
            Point prePoint = pointMap[preLocation.getX()][preLocation.getY()];
            if (prePoint.up) {
                Location nextLocation = new Location(preLocation.getX() - 1, preLocation.getY());
                Point nextPoint = pointMap[nextLocation.getX()][nextLocation.getY()];
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
                }
                pointMap[nextLocation.getX()][nextLocation.getY()].down = false;
            }
            if (prePoint.down) {
                Location nextLocation = new Location(preLocation.getX() + 1, preLocation.getY());
                Point nextPoint = pointMap[nextLocation.getX()][nextLocation.getY()];
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
                }
                pointMap[nextLocation.getX()][nextLocation.getY()].up = false;
            }
            if (prePoint.left) {
                Location nextLocation = new Location(preLocation.getX(), preLocation.getY() - 1);
                Point nextPoint = pointMap[nextLocation.getX()][nextLocation.getY()];
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
                }
                pointMap[nextLocation.getX()][nextLocation.getY()].right = false;
            }
            if (prePoint.right) {
                Location nextLocation = new Location(preLocation.getX(), preLocation.getY() + 1);
                Point nextPoint = pointMap[nextLocation.getX()][nextLocation.getY()];
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
                }
                pointMap[nextLocation.getX()][nextLocation.getY()].left = false;
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

    @Override
    public void moveToDestination(ArrayList<Integer> path, Location destinationLocation) throws InterruptedException {
        //Requires: destinationLocation不为NULL
        //Modifies: location和carState属性，经过道路的流量
        //Effects: 将car按照path移动到指定的destinationLocation
        Location preLocation = location.clone();
        Location nextLocation = new Location();
        for (int i = 0; i < path.size(); i++) {
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
            if(!Map.getBlocked(preLocation.oneDimensionalLoc(), location.oneDimensionalLoc()))/*关闭的道路不计算流量*/
                Map.setFlows(preLocation.oneDimensionalLoc(), location.oneDimensionalLoc());
            preLocation = location.clone();
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
                Thread.sleep(1000);
                startTime = System.currentTimeMillis();
            }
        }
    }

    public ListIterator<Location> listIterator(int count){
        //Requires: none
        //Modifies: none
        //Effects: 返回count指定服务次数时的服务路径的迭代器，越界返回null
        if(count < 0 || count >= getServeCount()){
            System.out.println("请求越界");
            return null;
        }
        return new ListIterator<Location>() {

            ArrayList<Integer> arrayList = servePath.get(count);
            int cursor = 0;

            @Override
            public boolean hasNext() {
                //Requires: none
                //Modifies: none
                //Effects: 当且仅当有下一个元素返回true，否则返回false
                return cursor != arrayList.size();
            }

            @SuppressWarnings("unchecked")
            public Location next() {
                //Requires: none
                //Modifies: none
                //Effects: 如果有后一个元素则返回后一个元素，游标加一，否则抛出异常
                int i = cursor;
                if (i >= arrayList.size())
                    throw new NoSuchElementException();
                cursor = i + 1;
                return new Location(arrayList.get(i) % 80, arrayList.get(i) / 80);
            }

            @Override
            public boolean hasPrevious() {
                //Requires: none
                //Modifies: none
                //Effects: 当且仅当有前一个元素返回true，否则返回false
                return cursor != 0;
            }

            @SuppressWarnings("unchecked")
            public Location previous() {
                //Requires: none
                //Modifies: none
                //Effects: 如果有前一个元素则返回前一个元素，游标减一，否则抛出异常
                int i = cursor - 1;
                if (i < 0)
                    throw new NoSuchElementException();
                cursor = i;
                return new Location(arrayList.get(i) % 80, arrayList.get(i) / 80);
            }

            @Override
            public int nextIndex() {
                //Requires: none
                //Modifies: none
                //Effects: 返回cursor
                return cursor;
            }

            @Override
            public int previousIndex() {
                //Requires: none
                //Modifies: none
                //Effects: 返回cursor减一
                return cursor - 1;
            }

            @Override
            public void remove() {
                //Requires: none
                //Modifies: none
                //Effects: 移除游标所指向的元素
                arrayList.remove(cursor);
            }

            @Override
            public void set(Location location) {
                //Requires: none
                //Modifies: none
                //Effects: none
            }

            @Override
            public void add(Location location) {
                //Requires: none
                //Modifies: none
                //Effects: none
            }
        };
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
                    /*记录服务路径*/
                    ArrayList<Integer> recordPath = new ArrayList<>();
                    recordPath.add(location.oneDimensionalLoc());
                    recordPath.addAll(path);
                    servePath.add(recordPath);
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
