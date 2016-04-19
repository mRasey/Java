package Wber;

import java.util.Random;

/**
 * 乘客类
 */
public class Passenger {
    private int num;
    private Point location;

    /**
     * 构造器
     */
    public Passenger() {
        location = new Point(new Random().nextInt(80), new Random().nextInt(80));
    }

    /**
     * Point getLocation()
     * @return 乘客当前坐标
     */
    public Point getLocation(){return location;}
}
