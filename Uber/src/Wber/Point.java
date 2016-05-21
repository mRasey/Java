package Wber;

/**
 * 坐标点类
 */
public class Point {
    /* Overview:
    这个类用来保存地图入上的点，up，down，left，right分别表示能够向上下左右行驶，
    isPreUsed表示双向广搜寻找最短路径时候有没有被前序搜索队列遍历过，ifLastUsed表
    示双向广搜寻找最短路径时候有没有被后序搜索队列遍历过，sumFlow表示起点到当前点
    的总流量，hasTrafficLight表示当前路口有没有红绿灯。
    */

    boolean up = false;
    boolean down = false;
    boolean left = false;
    boolean right = false;
    boolean ifPreUsed = false;
    boolean ifLastUsed = false;
    int sumFlow = 0;/*从起点到该点的流量*/
    boolean hasTrafficLight = false;


    public Point() {
        //Requires: none
        //Effects: 空构造器
    }

    public Point(boolean up, boolean down, boolean left, boolean right, boolean ifPreUsed, boolean ifLastUsed, int sumFlow, boolean hasTrafficLight) {
        //Requires: 能否上下左右，是否被遍历过，总流量等均不为NULL
        //Effects: 构造器
        this.up = up;
        this.down = down;
        this.left = left;
        this.right = right;
        this.ifPreUsed = ifPreUsed;
        this.ifLastUsed = ifLastUsed;
        this.sumFlow = sumFlow;
        this.hasTrafficLight = hasTrafficLight;
    }

    public boolean repOK(){
        //Effects: returns true if the rep variant holds for this, otherwise returns false
//        if(!up && up) return false;
//        if(!down && down) return false;
//        if(!left && left) return false;
//        if(!right && right) return false;
//        if(!ifPreUsed && ifPreUsed) return false;
//        if(!ifLastUsed && ifLastUsed) return false;
        if(sumFlow < 0) return false;
//        if(!hasTrafficLight && hasTrafficLight) return false;
        return true;
    }

    public Point clone(){
        //Requires: none
        //Modifies: none
        //Effects: 返回当前对象的一个拷贝
        return new Point(up, down, left, right, ifPreUsed, ifLastUsed, sumFlow, hasTrafficLight);
    }
}
