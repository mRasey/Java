package Wber;

public class TrafficLight implements Runnable{
    /*Overview：
    这个类用于产生横向道路和纵向道路的红绿灯交通信号，verticalLight
    表示纵向道路的红绿灯，crossLight表示横向道路的红绿灯
    */

    static LightColor verticalLight = LightColor.GREEN;/*纵向道路的红绿灯*/
    static LightColor crossLight = LightColor.RED;/*横向道路的红绿灯*/

    public boolean repOK(){
        //Requires: 横向和纵向红绿灯的颜色
        //Modifies: none
        //Effects: returns true if the rep variant holds for this, otherwise returns false
//        if(verticalLight != LightColor.RED && verticalLight != LightColor.GREEN) return false;
//        if(crossLight != LightColor.RED && crossLight != LightColor.GREEN) return false;
        return true;
    }

    @Override
    public void run(){
        try {
            while (true) {
                Thread.sleep(300);
                verticalLight = LightColor.RED;
                crossLight = LightColor.GREEN;
                Thread.sleep(300);
                verticalLight = LightColor.GREEN;
                crossLight = LightColor.RED;
            }
        }
        catch (InterruptedException i){
            i.printStackTrace();
        }
    }
}

enum LightColor{
    RED,
    GREEN
}