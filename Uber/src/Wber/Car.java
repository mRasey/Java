package Wber;

import java.util.Random;

/**
 * Car
 */
public class Car {
    private Matrix location;

    /**
     * Constructor
     */
    public Car(){
        location = new Matrix(new Random().nextInt(80), new Random().nextInt(80));/*随机出租车的初始位置*/

    }

    /**
     * Matrix getLocation()
     * @return current location of the car
     */
    public Matrix getLocation(){return location;}

}
