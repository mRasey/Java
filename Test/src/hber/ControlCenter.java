package hber;

/**
 * Created by cc on 2016/4/18.
 */
import java.io.*;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;


public class ControlCenter {
    //OVERVIEW : This class is used to store the map and the car.
    public static Spot [][] City = new Spot [81][81];
    public static int [][] HorizontalTrafficFlow = new int[80][79];
    public static int [][] VerticalTrafficFlow = new int [80][79];
    public static LinkedList<Car> CarArray = new LinkedList<>();
    public static HashSet<Car> IdleCarSet = new HashSet<>();
    public static TrafficLight trafficLight;
    private static ExecutorService fixedThreadPool = Executors.newFixedThreadPool(200);
    public static synchronized  void addTrafficFlow(int x,int y, int Dx, int Dy)
        /*
        * Requires : None
        * Modifies : None
        * Effects : increment traffic flow.
        * */
    {
        if(Math.abs(x-Dx) == 1 && y==Dy){
            HorizontalTrafficFlow[y-1][Math.min(x,Dx)-1]++;
        }
        else if(Math.abs(y - Dy) ==1 && x==Dx){
            VerticalTrafficFlow[x-1][Math.min(y,Dy)-1]++;
        }
    }
    public static synchronized  void subTrafficFlow(int x,int y, int Dx, int Dy)
        /*
        * Requires : None
        * Modifies : None
        * Effects : decrement the traffic flow.
        * */
    {
        if(Math.abs(x-Dx) == 1 && y==Dy){
            HorizontalTrafficFlow[y-1][Math.min(x,Dx)-1]--;
        }
        else if(Math.abs(y - Dy) ==1 && x==Dx){
            VerticalTrafficFlow[x-1][Math.min(y,Dy)-1]--;
        }
    }
    public static int getTrafficFlow(int x,int y, int Dx, int Dy)
                /*
        * Requires : None
        * Modifies : None
        * Effects : return the traffic flow.
        * */
    {
        if(Math.abs(x - Dx) == 1 && y == Dy){
            return HorizontalTrafficFlow[y-1][Math.min(x,Dx)-1];
        }
        else if(Math.abs(y - Dy) ==1 && x == Dx){
            return VerticalTrafficFlow[x-1][Math.min(y,Dy)-1];
        }
        return 0;
    }
    public void shutdown()
        /*
        * Requires : None
        * Modifies : None
        * Effects : shut down the whole system.
        * */
    {
        for(Car car:CarArray)
        {
            car.close();
        }
        fixedThreadPool.shutdown();
    }
    public static synchronized void RemoveFromIdleSet(Car car)
    /*
        * Requires : IdleCarset has been initalized.
        * Effects : remove the reference from the set.
        * */
    {
        if(IdleCarSet.contains(car))
            IdleCarSet.remove(car);
    }
    public static synchronized void AddToIdleSet(Car car)
    /*
        * Requires : IdleCarset has been initalized.
        * Effects : add the reference to the set.
        * */
    {
        if(!IdleCarSet.contains(car))
            IdleCarSet.add(car);
    }
    public ControlCenter(){
        /*
        * Requires : The file "map.txt" should exist.
        * Modifies : None
        * Effects : Initialize.
        * */
        try{
        File file = new File("map.txt");
            if(!file.exists()){
                System.out.println("I can not find the \" map.txt \" ");
                System.exit(0);
            }
        for(int i=0;i<81;i++)
            for(int j=0;j<81;j++)
                City[i][j] = new Spot(i,j);
        Integer [][] Map = new Integer[81][81];
        if(file.canRead())
        {
            try {
                BufferedReader BR = new BufferedReader(new FileReader(file.getName()));
                for(int i=1;i<81;i++) {
                    String s = BR.readLine();
                    String str[] = s.split(" ");
                    int j=1;
                    for(int con=0;con<str.length;con++){
                        Map[i][j] = Integer.parseInt(str[con]);
                        if(Map[i][j]==1 && j!=80){
                            City[i][j].connect(i,j+1);
                            City[i][j+1].connect(i,j);
                        }else if(Map[i][j]==2 && i!=80){
                            City[i][j].connect(i+1,j);
                            City[i+1][j].connect(i,j);
                        }else if(Map[i][j]==3 && j!=80 && i!=80){
                            City[i][j].connect(i,j+1);
                            City[i][j+1].connect(i,j);
                            City[i][j].connect(i+1,j);
                            City[i+1][j].connect(i,j);
                        }else if(Map[i][j]==3 && i==80 && j!=80){
                            City[i][j].connect(i,j+1);
                            City[i][j+1].connect(i,j);
                        }else if(Map[i][j]==3 && j==80 && i!=80){
                            City[i][j].connect(i+1,j);
                            City[i+1][j].connect(i,j);
                        }
                        j++;
                    }
                }
                for(int i=1;i<81;i++){
                    for(int j=1;j<81;j++)
                    {
                        if(!City[i][j].left && !City[i][j].down && !City[i][j].right && !City[i][j].up)
                        {
                            System.out.println("Graph has blocked area."+i+" "+j);
                            System.exit(0);
                        }
                    }
                }
                LinkedList<Integer> queue = new LinkedList<>();
                LinkedList<Integer> ans = new LinkedList<>();
                int [][] Parent = new int[81][81];
                queue.add(80);
                while(!queue.isEmpty()){
                    int S = queue.removeFirst();
                    int Sx = S / 80;
                    int Sy = S % 80 + 1;
                    ans.add(S);
                    if(ControlCenter.City[Sx][Sy].left && Parent[Sx][Sy-1]==0){
                        queue.addLast(80*(Sx)+Sy-1-1);
                        Parent[Sx][Sy-1]=Sx*80+Sy-1;
                    }
                    if(ControlCenter.City[Sx][Sy].right && Parent[Sx][Sy+1]==0){
                        queue.addLast(80*(Sx)+Sy+1-1);
                        Parent[Sx][Sy+1]=Sx*80+Sy-1;
                    }
                    if(ControlCenter.City[Sx][Sy].up && Parent[Sx-1][Sy]==0){
                        queue.addLast(80*(Sx-1)+Sy-1);
                        Parent[Sx-1][Sy]=Sx*80+Sy-1;
                    }
                    if(ControlCenter.City[Sx][Sy].down && Parent[Sx+1][Sy]==0){
                        queue.addLast(80*(Sx+1)+Sy-1);
                        Parent[Sx+1][Sy]=Sx*80+Sy-1;
                    }
                }
                if(ans.size()!=6401)
                {
                    System.out.println("Unconnected Graph."+ans.size());
                    System.exit(0);
                }

            } catch (FileNotFoundException e) {
                System.out.println("Map File can not be found.");
                System.exit(0);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            System.out.println("Map File cannot be read.");
            System.exit(0);
        }
        file = new File("cross.txt");
        if(!file.exists()){
            System.out.println("I can not find the \" cross.txt \" ");
            System.exit(0);
        }
            if(file.canRead())
            {
                try {
                    BufferedReader BR = new BufferedReader(new FileReader(file.getName()));
                    for(int i=1;i<81;i++) {
                        String s = BR.readLine();
                        String str[] = s.split(" ");
                        int j=1;
                        for(int con=0;con<str.length;con++){
                            Map[i][j] = Integer.parseInt(str[con]);
                            if(Map[i][j]==1)
                                City[i][j].NormalCross = true;
                            j++;
                        }
                    }
                } catch (FileNotFoundException e) {
                    System.out.println("Cross File can not be found.");
                    System.exit(0);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else {
                System.out.println("Cross File cannot be read.");
                System.exit(0);
            }
            trafficLight = new TrafficLight();
            trafficLight.start();
        for(int i=0;i<1;i++){
            Car car = new Car(i+1);
            CarArray.add(car);
            fixedThreadPool.execute(car);
        }
      }catch (Throwable e ){
            System.out.println("Error happens.");
            e.printStackTrace();
            System.exit(0);
        }
    }
    public static synchronized void connect(int x,int y, int Dx, int Dy)
    /*
        * Requires : City has been initialized.
        * Effects : connect two spots which is adjacent
        * */
    {
        City[x][y].connect(Dx, Dy);
        City[Dx][Dy].connect(x, y);
    }
    public static synchronized  void disconnect(int x, int y, int Dx, int Dy)
            /*
        * Requires : City has been initialized.
        * Effects : disconnect two spots which is adjacent
        * */
    {
        City[x][y].disconnect(Dx, Dy);
        City[Dx][Dy].disconnect(x, y);
    }
    public static synchronized  boolean getConnectivity(int x, int y, int Dx, int Dy)
    /*
        * Requires : City has been initalized.
        * Effects : get the connectivity of two adjacent spots
        * */
    {
        if(x-Dx == 1)
        {
            return City[x][y].left && City[Dx][Dy].right;
        }
        else if(Dx-x == 1)
        {
            return City[x][y].right && City[Dx][Dy].left;
        }
        else if(y-Dy == 1)
        {
            return City[x][y].up && City[Dx][Dy].down;
        }
        else if(x-Dx == 1)
        {
            return City[x][y].down && City[Dx][Dy].up;
        }
        return  false;
    }
}
