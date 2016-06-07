package MaxClique;

import java.util.Date;
import java.util.Random;

public class Algorithm extends Thread{
	//启发式算法
    int InitPoint;
    public Set Clique;
    static Random random = new Random();

    public Algorithm(int ip){
        InitPoint = ip;
        Clique = new Set();
        Clique.add(ip);
    }
    
    public void run(){
        Set Neighbor = InputHandler.PointArray[InitPoint].Neighbour.clone();
        //先由第一个点来初始化邻接点集合
        int NewPoint = getHeuPoint1(Neighbor);
        while(Neighbor.size()>0){
            Neighbor = addNewPoint(NewPoint,Neighbor);
            NewPoint = getHeuPoint1(Neighbor);
        }
        MaxClique max = new MaxClique(Neighbor);
        System.out.println("Inip:"+InitPoint+" "+(Clique.size()+max.answer));
    }

    public Set addNewPoint(int i, Set Neighbor){
        //向团中加值，并将新加入的点和原来团的邻接点取交集
        Clique.add(i);
        Neighbor = Set.Intersection(Neighbor,InputHandler.PointArray[i].Neighbour);
        return Neighbor;
    }
    
    public static int getHeuPoint0(Set Neighbour){//Version 1.0
        //找度数最大 72/100
        int Maxdegree = Integer.MIN_VALUE;
        int MaxP = 0;
        for(int i =0; i <= InputHandler.PointsNum;i++){
            if(Neighbour.getConnectiviry(i)){
                if(InputHandler.PointArray[i].getDegree() > Maxdegree){
                    MaxP = i;
                    Maxdegree = InputHandler.PointArray[i].getDegree();
                }
            }
        }
        return MaxP;
    }
    
    public int getHeuPoint1(Set Neighbour){//Version 1.1
        //第一版本的启发信息是新加入的点的邻接点和团的邻接点的交集最大 83/100
        int Maxdegree = Integer.MIN_VALUE;
        int MaxP = 0;
        for(int i =0; i <= InputHandler.PointsNum;i++){
            if(Neighbour.contains(i) && !Clique.contains(i)){
                Set tmpset = new Set();
                tmpset = Neighbour.clone();
                tmpset = Set.Intersection(InputHandler.PointArray[i].Neighbour,tmpset);
                int tmpd = tmpset.size();
                if(tmpd > Maxdegree && random.nextInt(100) > 20){ //random 代表轻微扰动
                    MaxP = i;
                    Maxdegree = tmpd;
                }
            }
        }
        return MaxP;
    }
}
