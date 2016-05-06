package Wber;

/**
 * 坐标点类
 */
public class Point {
    boolean up = false;
    boolean down = false;
    boolean left = false;
    boolean right = false;
    boolean ifPreUsed = false;
    boolean ifLastUsed = false;
    int sumFlow = 0;/*从起点到该点的流量*/

    public Point() {
        //Requires: none
        //Modifies: none
        //Effects: 空构造器
    }

    public Point(boolean up, boolean down, boolean left, boolean right, boolean ifPreUsed, boolean ifLastUsed, int sumFlow) {
        //Requires: 能否上下左右的属性，是否被遍历过，总流量
        //Modifies: none
        //Effects: 构造器
        this.up = up;
        this.down = down;
        this.left = left;
        this.right = right;
        this.ifPreUsed = ifPreUsed;
        this.ifLastUsed = ifLastUsed;
        this.sumFlow = sumFlow;
    }

    public Point clone(){
        //Requires: none
        //Modifies: none
        //Effects: 返回当前对象的一个拷贝
        return new Point(up, down, left, right, ifPreUsed, ifLastUsed, sumFlow);
    }
}
