package Wber;

/**
 * Point
 */
public class Point {
    private Matrix location;
    private Connect connect;

    /**
     * Constructor
     */
    public Point(Matrix location){
        this.location = location;
    }

    /**
     * Matrix getLocation()
     * @return location
     */
    public Matrix getLocation(){return location;}

    /**
     * Connect getConnect()
     * @return connect
     */
    public Connect getConnect(){return connect;}

}

class Connect{
    boolean up;
    boolean down;
    boolean left;
    boolean right;

    public Connect(){
        up = false;
        down = false;
        left = false;
        right = false;
    }
}
