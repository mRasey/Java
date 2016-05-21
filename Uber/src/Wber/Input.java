package Wber;

import java.io.*;

/**
 * 输入类
 */
public class Input {
    /*Overview:
    这个类用于处理输入的地图文件并生成由二维数组表示的地图，inputPath表示输入的地图文件的路径，crossPath表示输入的交叉属性文件的位置，points表示构建的地图。
    */

    private String inputPath;
    private String crossPath;
    private Point[][] points = new Point[80][80];

    public Input(String inputPath){
        //Requires: inputPath不为NULL
        //Effects: 构造器
        this.inputPath = inputPath;
        for(int i = 0; i < 80; i++){
            for(int j = 0; j < 80; j++){
                points[i][j] = new Point();
            }
        }
    }

    public Input(String inputPath, String crossPath){
        //Requires: inputPath,crossPath不为NULL
        //Effects: 构造器
        this.inputPath = inputPath;
        this.crossPath = crossPath;
        for(int i = 0; i < 80; i++){
            for(int j = 0; j < 80; j++){
                points[i][j] = new Point();
            }
        }
    }

    public boolean repOK(){
        //Effects: returns true if the rep variant holds for this, otherwise returns false
        if(inputPath == null) return false;
        if(crossPath == null) return false;
//        for(int i = 0; i < 80; i++){
//            for(int j = 0; j < 80; j++){
//                if(points[i][j] == null) return false;
//            }
//        }
        return true;
    }

    public Point[][] getPoints(){
        //Requires: none
        //Modifies: none
        //Effects: 返回points属性
        return points;
    }

    /**
     * 建立地图
     *
     * @throws IOException
     */
    public void buildMap() throws IOException {
        //Requires: inputPath不为NULL
        //Modifies: points属性
        //Effects: 建立地图
        try {
            File file = new File(inputPath);
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            for (int i = 0; i < 80; i++) {
                String line = bufferedReader.readLine();
                String[] strings = line.split(" ");
                for (int j = 0; j < strings.length; j++) {
                    int model = Integer.parseInt(strings[j]);
                    setConnection(model, i, j);
                }
            }
        }catch (Throwable t){
//            t.printStackTrace();
            System.out.println("建立地图出现问题，程序退出");
            System.exit(0);
        }
    }

    public void buildTrafficLightMap() throws IOException {
        //Requires: none
        //Modifies: points的每一个point中hasTrafficLight属性
        //Effects: 改变points的每一个point中hasTrafficLight属性
        try {
            for (int i = 0; i < 80; i++) {
                for (int j = 0; j < 80; j++) {
                    int count = 0;
                    Point point = points[i][j];
                    if (point.up)
                        count++;
                    if (point.down)
                        count++;
                    if (point.left)
                        count++;
                    if (point.right)
                        count++;
                    if (count >= 3)
                        point.hasTrafficLight = true;
                }
            }
            File file = new File(crossPath);
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            for (int i = 0; i < 80; i++) {
                String s = bufferedReader.readLine();
                String[] strings = s.split(" ");
                for (int j = 0; j < 80; j++) {
                    if (Integer.parseInt(strings[j]) == 0) {
                        points[i][j].hasTrafficLight = false;
                    }
                }
            }
        }catch (Throwable t){
            System.out.println("建立交叉路出现问题，程序退出");
            System.exit(0);
        }
    }

    /**
     * 设置图的连接
     * @param model 连接模式
     * @param i 行数
     * @param j 列数
     */
    public void setConnection(int model, int i, int j){
        //Requires: model为0,1,2,3之一，i j属于[0,79]
        //Modifies: 点集的连接属性
        //Effects: 改变地图上点的属性
        if(model == 0){

        }
        else if(model == 1){
            points[i][j].right = true;
            if(j + 1 < 80)
                points[i][j + 1].left = true;
        }
        else if(model == 2){
            points[i][j].down = true;
            if(i + 1 < 80)
                points[i + 1][j].up = true;
        }
        else if(model == 3){
            points[i][j].right = true;
            points[i][j].down = true;
            if(j + 1 < 80)
                points[i][j + 1].left = true;
            if(i + 1 < 80)
                points[i + 1][j].up = true;
        }
    }
}
