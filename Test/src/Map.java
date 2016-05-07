import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Date;

class Map extends GlobalConstant implements Runnable{
	private int[][][] map = new int[80][80][4];
    private long refreshTime = 1;
    private long StartTime;


	int getPoint(int posX, int posY, int posZ){
        //REQUIRES:map数组非空
        //MODIFIES:none
        //EFFECTS:返回map数组中指定元素
        return map[posX][posY][posZ];
    }

    synchronized void addFlow(int posX, int posY, int posZ){
        //REQUIRES:map数组非空，线程锁控制
        //MODIFIES:map数组指定元素
        //EFFECTS:将map数组中指定元素值加1，表示增加车流量
//        System.out.println(posX+","+posY+","+posZ);
        map[posX][posY][posZ] +=1;
    }

    public void run(){
        try{
            this.StartTime = new Date().getTime();
            while(true){
                RefreshMapFlow();
                sleep(1);
            }
        }catch(Exception e){System.out.print("");}
    }

    void ReadMap(String filename){
        //REQUIRES:map.txt格式正确
        //MODIFIES:map数组
        //EFFECTS:根据map.txt内容初始化map数组
        File MapFile = new File(filename);
        if(MapFile.exists()){
            try(FileReader reader = new FileReader(MapFile);
                BufferedReader bufferedReader = new BufferedReader(reader)){
                for(int row = 0; row < 80; row++) {
                    char[] values = bufferedReader.readLine().toCharArray();
                    for (int column = 0; column < 80; column++) {
                        switch (values[column]) {
                            case '0':
                                map[row][column][Right] = Disconnect;
                                map[row][column][Down] = Disconnect;
                                CheckBack(row,column);
                                break;
                            case '1':
                                map[row][column][Right] = Initconnect;
                                map[row][column][Down] = Disconnect;
                                CheckBack(row,column);
                                break;
                            case '2':
                                map[row][column][Right] = Disconnect;
                                map[row][column][Down] = Initconnect;
                                CheckBack(row,column);
                                break;
                            default:
                                map[row][column][Right] = Initconnect;
                                map[row][column][Down] = Initconnect;
                                CheckBack(row,column);
                        }
                    }
                }
            }catch (Exception exception) {System.out.println("Something wrong when reading map!");}
        }
    }

    private void CheckBack(int row,int column){
        //REQUIRES:map数组初始化正确
        //MODIFIES:map数组
        //EFFECTS:ReadMap辅助方法，根据该点左边和上边两个点的值确定该点的值
        if (column > 0 && getPoint(row, column - 1, 2) != Disconnect)
            map[row][column][Left] = Initconnect;
        else
            map[row][column][Left] = Disconnect;
        if (row > 0 && getPoint(row - 1, column, 3) != Disconnect)
            map[row][column][Up] = Initconnect;
        else
            map[row][column][Up] = Disconnect;
    }

    private synchronized void RefreshMapFlow(){
        //REQUIRES:map数组初始化正确，线程锁控制
        //MODIFIES:map数组
        //EFFECTS:以50ms为刷新周期，将地图中每条边的车流量归零
        long time = new Date().getTime() - this.StartTime;
        if(time > refreshTime*50){
            for(int i=0;i<80;i++)
                for(int j=0;j<80;j++)
                    for(int k=0;k<4;k++){
                        if(map[i][j][k] != Disconnect)
                            map[i][j][k] = Initconnect;
                    }
        }
        refreshTime = time/50;
    }

    synchronized void ChangeConnect(int posX,int posY,int direction) {
        //REQUIRES:map数组初始化正确，线程锁控制
        //MODIFIES:map数组
        //EFFECTS:指定一个点和一个方向确定一条边，改变这条边的连通性
        try {
            boolean NoConnect = (map[posX][posY][direction] == Disconnect);
            switch (direction) {
                case Left:
                    map[posX][posY - 1][Right] = NoConnect?Initconnect:Disconnect;
                    map[posX][posY][Left] = NoConnect?Initconnect:Disconnect;
                    break;
                case Up:
                    map[posX - 1][posY][Down] = NoConnect?Initconnect:Disconnect;
                    map[posX][posY][Up] = NoConnect?Initconnect:Disconnect;
                    break;
                case Right:
                    map[posX][posY + 1][Left] = NoConnect?Initconnect:Disconnect;
                    map[posX][posY][Right] = NoConnect?Initconnect:Disconnect;
                    break;
                case Down:
                    map[posX + 1][posY][Up] = NoConnect?Initconnect:Disconnect;
                    map[posX][posY][Down] = NoConnect?Initconnect:Disconnect;
                    break;
                default:
            }
            sleep(10);
        }catch(Exception e){System.out.println("change connect failed!");}
    }

}
