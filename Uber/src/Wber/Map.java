package Wber;

public class Map implements Runnable{
    static int[][] flows = new int[6400][6400];
    static boolean[][] blocked = new boolean[6400][6400];

    /**
     * 增加道路流量
     * @param from 起点
     * @param to 终点
     */
    public static void setFlows(int from, int to){
        //Requires: 起点和终点
        //Modifies: none
        //Effects: 增加起点和终点之间的道路流量
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
        //Requires: 起点和终点
        //Modifies: none
        //Effects: 获取起点和终点之间的道路的流量
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
        //Requires: 起点，终点以及指定的道路状态
        //Modifies: none
        //Effects: 将起点终点之间的道路的开闭状态设为指定状态
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
        //Requires: 起点和终点
        //Modifies: none
        //Effects: 返回起点和终点之间的道路的开闭状态
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
