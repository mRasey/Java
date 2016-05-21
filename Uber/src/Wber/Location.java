package Wber;

import java.util.Random;

/**
 * 位置类
 */
public class Location {
    /*Overview
    这个类是记录点的横坐标和纵坐标的类，x表示横坐标，y表示纵坐标
    */

    private int x;
    private int y;

    public Location(){
        //Requires: none
        //Effects: 构造器
        this.x = new Random().nextInt(80);
        this.y = new Random().nextInt(80);
    }

    public Location(int x, int y){
        //Requires: x y属于[0,79]
        //Effects: 构造器
        this.x = x;
        this.y = y;
    }

    public boolean repOK(){
        //Effects: returns true if the rep variant holds for this, otherwise returns false
        if(x < -1 || x > 80) return false;
        if(y < -1 || y > 80) return false;
        return true;
    }

    public int getX(){
        //Requires: none
        //Modifies: none
        //Effects: 返回x属性
        return x;
    }

    public int getY(){
        //Requires: none
        //Modifies: none
        //Effects: 返回y属性
        return y;
    }

    public Location setX(int x) {
        //Requires: x属于[0,79]
        //Modifies: x属性
        //Effects: 将x属性改为x
        this.x = x;
        return this;
    }

    public Location setY(int y) {
        //Requires: y属于[0,79]
        //Modifies: y属性
        //Effects: 将y属性改为y
        this.y = y;
        return this;
    }

    public Location setXY(int x, int y) {
        //Requires: x y属于[0,79]
        //Modifies: x y 属性
        //Effects: 将x y属性改为x y
        this.x = x;
        this.y = y;
        return this;
    }

    public int oneDimensionalLoc(){
        //Requires: none
        //Modifies: none
        //Effects: 将二维坐标转换为一维坐标
        return x * 80 + y;
    }

    public Location clone() {
        //Requires: none
        //Modifies: none
        //Effects: 拷贝当前对象
        return new Location(this.x, this.y);
    }

    public boolean equals(Location location) {
        //Requires: location不为NULL
        //Modifies: none
        //Effects: 当且仅当两个对象的横纵坐标都相同的时候返回真
        return this.x == location.getX() && this.y == location.getY();
    }

    public Location upLocation(){
        //Requires: none
        //Modifies: none
        //Effects: 返回当前位置的上方位置
        return new Location(this.x - 1, this.y);
    }

    public Location downLocation(){
        //Requires: none
        //Modifies: none
        //Effects: 返回当前位置的下方位置
        return new Location(this.x + 1, this.y);
    }

    public Location leftLocation(){
        //Requires: none
        //Modifies: none
        //Effects: 返回当前位置的左方位置
        return new Location(this.x, this.y - 1);
    }

    public Location rightLocation(){
        //Requires: none
        //Modifies: none
        //Effects: 返回当前位置的右方位置
        return new Location(this.x, this.y + 1);
    }

    @Override
    public String toString() {
        //Requires: none
        //Modifies: none
        //Effects: 返回Location的字符串表示
        return "Location [" + x + ", " + y + ']';
    }
}
