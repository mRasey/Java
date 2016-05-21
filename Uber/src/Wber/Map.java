package Wber;

public class Map implements Runnable{
    /*Overview
    这个类是用于保存地图上的流量以及阻塞情况的类，flows记录整个地图的流量，blocked记录整个地图道路的开关状态
    */

    static int[][] flows = new int[6400][6400];
    static boolean[][] blocked = new boolean[6400][6400];

    public  static boolean repOK(){
        //Effects: returns true if the rep variant holds for this, otherwise returns false
        for(int i = 0; i < 6400; i++){
            for(int j = 0; j < 6400; j++){
                if(flows[i][j] < 0) return false;
            }
        }
//        for(int i = 0; i < 6400; i++){
//            for(int j = 0; j < 6400; j++){
//                if(!blocked[i][j] && blocked[i][j]) return false;
//            }
//        }
        return true;
    }

    /**
     * 增加道路流量
     * @param from 起点
     * @param to 终点
     */
    public static void setFlows(int from, int to){
        //Requires: from和to均为0到6399之间
        //Modifies: flows属性
        //Effects: 增加flows[from][to]的道路流量一
        if(from < to)
            flows[from][to]++;
        else if(from > to)
            flows[to][from]++;
    }

    /**
     * 获取道路流量
     * @param from 起点
     * @param to 终点
     * @return 指定道路的流量
     */
    public static int getFlows(int from, int to) {
        //Requires: from和to均为0到6399之间
        //Modifies: none
        //Effects: 获取flows[from][to]的道路的流量
        if(from < to
                && 0 <= from / 80 && from / 80 <= 79
                && 0 <= from % 80 && from % 80 <= 79
                && 0 <= to / 80 && to / 80 <= 79
                && 0 <= to % 80 && to % 80 <= 79)
            return flows[from][to];
        else if(from > to
                && 0 <= from / 80 && from / 80 <= 79
                && 0 <= from % 80 && from % 80 <= 79
                && 0 <= to / 80 && to / 80 <= 79
                && 0 <= to % 80 && to % 80 <= 79)
            return flows[to][from];
        else
            return Integer.MAX_VALUE;
    }

    /**
     * 设置道路开闭
     * @param from 起点
     * @param to 终点
     * @param blockState 道路状态
     */
    public static void setBlocked(int from, int to, boolean blockState) {
        //Requires: from和to均为0到6399之间
        //Modifies: blocked[from][to]
        //Effects: 将blocked[from][to]设为blockState状态
        if(from < to) {
            blocked[from][to] = blockState;
        }
        else if(from > to) {
            blocked[to][from] = blockState;
        }
    }

    /**
     * 获取道路状态
     * @param from 起点
     * @param to 终点
     * @return 道路状态
     */
    public static boolean getBlocked(int from, int to) {
        //Requires: from和to均为0到6399之间
        //Modifies: none
        //Effects: 返回blocked[from][to]的道路的开闭状态,当from和to相等时返回false
        if(from < to)
            return blocked[from][to];
        else if(from > to)
            return blocked[to][from];
        else
            return false;
    }

    @Override
    public void run(){
        while(true){
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            for(int i = 0; i < 6400; i++){
                for(int j = 0; j < 6400; j++){
                    flows[i][j] = 0;
                }
            }
        }
    }
}
