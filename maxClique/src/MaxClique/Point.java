package MaxClique;

import java.util.HashSet;

public class Point implements Comparable{
    int id;
    HashSet<Integer> Neighbour;

    public Point(int id){
        Neighbour = new HashSet<>();
        this.id = id;
    }

    public void addToNeighbour(int t){
        //初始化的时候使用，用来向邻居节点加值
        Neighbour.add(t);
    }

    public int getDegree(){
        return Neighbour.size();
    }

    public String toString()
    {
        String s = "id : "+ id + "\n" +" Neighbour : ";
        for(Integer i : Neighbour){
            s += i +" ";
        }
        return  s;
    }

    @Override
    public int compareTo(Object o) {
        if(o instanceof Point){
            Point point = (Point)o;
            return point.getDegree()-getDegree();
        }
        return 0;
    }
}
