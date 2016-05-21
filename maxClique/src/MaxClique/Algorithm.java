package MaxClique;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Random;

public class Algorithm extends Thread{
    int InitPoint;
    HashSet<Integer> Clique;
    static Random random = new Random();

    public void run(){
        HashSet<Integer> Neighbor = (HashSet<Integer>) InputHandler.PointArray[InitPoint].Neighbour.clone();
        //先由第一个点来初始化邻接点集合
        int NewPoint = getHeuPoint1(Neighbor);
        while(NewPoint!=0){
            addNewPoint(NewPoint,Neighbor);
            NewPoint = getHeuPoint1(Neighbor);
        }
        System.out.println("Inip:"+InitPoint+" "+Clique.size());
        System.out.println(new Date(System.currentTimeMillis()));
    }

    public void addNewPoint(int i, HashSet<Integer> Neighbor){
        //向团中加值，并将新加入的点和原来团的邻接点取交集
        Clique.add(i);
        Neighbor.retainAll(InputHandler.PointArray[i].Neighbour);
    }

    public static int getHeuPoint1(HashSet<Integer> Neighbour){//Version 1.1
        //第一版本的启发信息是新加入的点的邻接点和团的邻接点的交集最大
        int Maxdegree = Integer.MIN_VALUE;
        int MaxP = 0;
        for(Integer i:Neighbour){
            HashSet<Integer> tmpset = new HashSet<>();
            tmpset.addAll(Neighbour);
            tmpset.retainAll(InputHandler.PointArray[i].Neighbour);
            int tmpd = tmpset.size();
            if(tmpd > Maxdegree && random.nextInt(100) > 20){
                MaxP = i;
                Maxdegree = tmpd;
            }
        }
        return MaxP;
    }

    public Algorithm(int ip){
        InitPoint = ip;
        Clique = new HashSet<>();
        Clique.add(ip);
    }
    public static int getMaxDegreeNeighbor(HashSet<Integer> Neighbour){//Version 1.0
        //第一版本的尝试算法是去邻接点中度数最大的加入
        int Maxdegree = Integer.MIN_VALUE;
        int MaxP = 0;
        for(Integer i:Neighbour){
            if(InputHandler.PointArray[i].getDegree() > Maxdegree){
                MaxP = i;
                Maxdegree = InputHandler.PointArray[i].getDegree();
            }
        }
        return MaxP;
    }
}
