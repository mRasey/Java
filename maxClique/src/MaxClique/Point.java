package MaxClique;


public class Point implements Comparable{
    int id;
    Set Neighbour;
    double weight;
    public Point(int id){
        Neighbour = new Set();
        this.id = id;
        weight = 0;
    }

    public void addToNeighbour(int t){
        //初始化的时候使用，用来向邻居节点加值
        Neighbour.add(t);
    }

    public int getDegree(){
        return Neighbour.size();
    }

    public double updateweight(Set clique)
    {
        Set nowclique = clique.clone();
        nowclique = Set.Intersection(Neighbour,nowclique);
        weight = nowclique.size()*0.8+(InputHandler.PointsNum-nowclique.size())*0.2;
        return weight;
    }
    public String toString()
    {
        String s = "id : "+ id + "\n" +" Neighbour : ";
        s+=Neighbour;
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
