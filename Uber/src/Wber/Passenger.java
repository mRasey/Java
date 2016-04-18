package Wber;

import java.util.Random;

/**
 *
 */
public class Passenger {
    private int num;
    private Matrix location;

    /**
     * Constructor
     */
    public Passenger() {
        location = new Matrix(new Random().nextInt(80), new Random().nextInt(80));
    }

    /**
     * Matrix getLocation()
     * @return the location of passenger
     */
    public Matrix getLocation(){return location;}
}
