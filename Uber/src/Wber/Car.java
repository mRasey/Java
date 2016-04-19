package Wber;

import java.util.Random;

/**
 * 出租车类
 */
public class Car {
    private Point location;

    /**
     * 构造器
     */
    public Car(){
        location = new Point(new Random().nextInt(80), new Random().nextInt(80));/*随机出租车的初始位置*/

    }

    /**
     * Point getLocation()
     * @return 出租车当前坐标
     */
    public Point getLocation(){return location;}

}
