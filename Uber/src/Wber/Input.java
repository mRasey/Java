package Wber;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * 输入类
 */
public class Input {
    private String inputPath;
    private Point[][] points = new Point[80][80];

    public Input(String inputPath){
        this.inputPath = inputPath;
        for(int i = 0; i < 80; i++){
            for(int j = 0; j < 80; j++){
                points[i][j] = new Point();
            }
        }
    }

    public Point[][] getPoints(){
        return points;
    }

    /**
     * 建立地图
     * @throws IOException
     */
    public void buildMap() throws IOException {
        File file = new File(inputPath);
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        for(int i = 0; i < 80; i++){
            String line = bufferedReader.readLine();
            String[] strings = line.split(" ");
            for (int j = 0; j < strings.length; j++) {
//                System.out.println("length " + strings.length);
                int model = Integer.parseInt(strings[j]);
                setConnection(model, i, j);
            }
        }
    }

    /**
     * 设置图的连接
     * @param model 连接模式
     * @param i 行数
     * @param j 列数
     */
    public void setConnection(int model, int i, int j){
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
//        System.out.println(points[i][j].left);
    }
}
