package Wber;

import java.util.Random;

/**
 * 位置类
 */
public class Location {
    private int x;
    private int y;

    public Location(){
        //Requires: none
        //Modifies: none
        //Effects: 构造器
        this.x = new Random().nextInt(80);
        this.y = new Random().nextInt(80);
    }

    public Location(int x, int y){
        //Requires: 横坐标，纵坐标
        //Modifies: none
        //Effects: 构造器
        this.x = x;
        this.y = y;
    }

    public int getX(){
        //Requires: none
        //Modifies: none
        //Effects: 返回横坐标
        return x;
    }

    public int getY(){
        //Requires: none
        //Modifies: none
        //Effects: 返回纵坐标
        return y;
    }

    public Location setX(int x) {
        //Requires: 指定横坐标
        //Modifies: 横坐标
        //Effects: 将横坐标改为指定横坐标
        this.x = x;
        return this;
    }

    public Location setY(int y) {
        //Requires: 指定纵坐标
        //Modifies: 纵坐标
        //Effects: 将纵坐标改为指定纵坐标
        this.y = y;
        return this;
    }

    public Location setXY(int x, int y) {
        //Requires: 指定横坐标和纵坐标
        //Modifies: 横坐标和纵坐标
        //Effects: 将横坐标和纵坐标改为指定的横坐标和纵坐标
        this.x = x;
        this.y = y;
        return this;
    }

    public int oneDimensionalLoc(){
        //Requires: none
        //Modifies: none
        //Effects: 将二维点改为一维点
        return x * 80 + y;
    }

    public Location clone() {
        //Requires: none
        //Modifies: none
        //Effects: 拷贝当前对象
        return new Location(this.x, this.y);
    }

    public boolean equals(Location location) {
        //Requires: 要比较的对象
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

}
