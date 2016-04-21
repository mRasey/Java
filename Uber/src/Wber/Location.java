package Wber;

import java.util.Random;

/**
 * 位置类
 */
public class Location {
    private int x;
    private int y;

    public Location(){
        this.x = new Random().nextInt(80);
        this.y = new Random().nextInt(80);
    }

    public Location(int x, int y){
        this.x = x;
        this.y = y;
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setXY(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int oneDimensionalLoc(){
        return x * 80 + y;
    }

}
