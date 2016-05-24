package MaxClique;

import java.io.*;
import java.util.Arrays;

public class InputHandler {
    static public int PointsNum;
    static public int EdgeNum;
    static public Point[] PointArray;
    static public Point[] SortedArray;
    public InputHandler() throws IOException {//输入处理
        File file = new File("D:\\frb100-40.clq");
        if(!file.exists()){
            throw new FileNotFoundException();
        }
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        String s = br.readLine();
        String [] strings = s.split(" ");
        String [] argu = new String[4];
        int j=0;
        for(String str:strings){
            if(!str.equals("")){
                argu[j++] = str;
            }
        }
        PointsNum = Integer.parseInt(argu[2]);
        EdgeNum = Integer.parseInt(argu[3]);//读第一行内容 得知点数和边数
        PointArray = new Point[PointsNum+1];
        for(int i = 0; i < PointArray.length; i++){
            PointArray[i]= new Point(i);
        }//初始化所有点和0点（0点没用，1开始）
        for(int i=0;i<EdgeNum;i++){
            String tmp = br.readLine();
            String [] tmpstrings  = tmp.split(" ");
            String [] tmpargu = new String[3];
            j=0;
            for(String str:tmpstrings){
                if(!str.equals("")){
                    tmpargu[j++] = str;
                }
            }
            PointArray[Integer.parseInt(tmpargu[1])].addToNeighbour(Integer.parseInt(tmpargu[2]));
            PointArray[Integer.parseInt(tmpargu[2])].addToNeighbour(Integer.parseInt(tmpargu[1]));
        }
        SortedArray = PointArray.clone();//这个排序数组用来决定由哪个先开始搜
        Arrays.sort(SortedArray);
    }

}
