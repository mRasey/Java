package hber;

import java.util.LinkedList;
import java.util.Random;
import java.util.Stack;
import java.util.Vector;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by cc on 2016/4/18.
 */
public class Car extends Thread{
    enum State{Dead,Idle,Picking,Serving};
    private State Status;
    private int id;
    private int Credibility;
    private boolean [][] path;
    private enum Direction{up,down,left,right};
    private Direction CurrentDirection;
    public int x;
    public int y;
    public long runningtime;
    private boolean click;
    private Passenger passenger;
    public int getID()
        /*
        * Requires : Nome
        * Modifies : None
        * Effects : return ID
        * */
    {
        return id;
    }

    public int getCredibility()
    /*
        * Requires : Nome
        * Modifies : None
        * Effects : return credibility
        * */
    {
        return Credibility;
    }

    public synchronized void increCredibility()
    /*
        * Requires : Nome
        * Modifies : None
        * Effects : add 1 to the credibility
        * */
    {
        Credibility++;
    }
    public Car(int ID)
        /*
        * Requires : Nome
        * Modifies : None
        * Effects : initialize
        * */
    {
        path = new boolean [81][81];
        id = ID;
        runningtime = 0;
        Credibility = 0;
        Status = State.Idle;
        click = false;
        Random random = new Random();
//        x = random.nextInt(80)+1;
//        y = random.nextInt(80)+1;
        x = 20;
        y = 20;
        ControlCenter.City[x][y].CarIn(this);
        CurrentDirection = Direction.down;
        ControlCenter.AddToIdleSet(this);
    }
    public Car(int ID,int X,int Y)
    /*
        * Requires : Nome
        * Modifies : None
        * Effects : Initialize.
        * */
    {
        path = new boolean [81][81];
        id = ID;
        runningtime = 0;
        Credibility = 0;
        Status = State.Idle;
        click = false;
        Random random = new Random();
        x = X;
        y = Y;
        ControlCenter.City[x][y].CarIn(this);
        ControlCenter.AddToIdleSet(this);
    }
    public String getStatus()
            /*
        * Requires : Nome
        * Modifies : None
        * Effects : return the car status
        * */
    {
        return Status.toString();
    }
    public synchronized boolean stepAhead(Passenger passenger)
        /*
        * Requires : the car should have get the passenger signal
        * Modifies : None
        * Effects : if the car can pick the passenger, the car will return true and pick him.
        * */
    {
        while(Status.equals(State.Dead)){
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if(Status.equals(State.Idle)){
            System.out.println("The taxi"+id+" in the spot("+x+","+y+") receive the request from the passenger in the spot("+passenger.x+","+passenger.y+")");
            Credibility +=3;
            ControlCenter.RemoveFromIdleSet(this);
            this.passenger = passenger;
            Status = State.Picking;
            FindPath(x*80+y-1,passenger.x*80+passenger.y-1);
            return true;
        }
        return false;
    }
    public void FindPath(int Source,int Destination)
        /*
        * Requires : Nome
        * Modifies : None
        * Effects : bfs and let the car boot
        * */
    {
        for(boolean a[]:path)
        {
            for(boolean b:a)
            {
                b=false;
            }
        }
        boolean [][] map = new boolean[81][81];
        int SourceX = Source / 80;
        int SourceY = Source % 80 +1;
        int min_pathlength = Integer.MAX_VALUE;
        int length;
        Direction direction = null;
        int min_traffic = Integer.MAX_VALUE;
        if(Source == Destination){
            path[SourceX][SourceY]=true;
            return ;
        }
        if(ControlCenter.City[SourceX][SourceY].left){
            length = bfs(Source-1,Destination,map);
            if(length<min_pathlength){
                direction = Direction.left;
                map = new boolean[81][81];
                min_pathlength = length;
                min_traffic = ControlCenter.getTrafficFlow(SourceX,SourceY,SourceX,SourceY-1);
            }
        }
        map = new boolean[81][81];
        if(ControlCenter.City[SourceX][SourceY].right){
            length = bfs(Source+1,Destination,map);
            if(length <min_pathlength || length == min_pathlength && min_traffic > ControlCenter.getTrafficFlow(SourceX,SourceY,SourceX,SourceY+1)){
                direction = Direction.right;
                map = new boolean[81][81];
                min_pathlength = length;
                min_traffic = ControlCenter.getTrafficFlow(SourceX,SourceY,SourceX,SourceY+1);
            }
        }
        map = new boolean[81][81];
        if(ControlCenter.City[SourceX][SourceY].up){
            length = bfs(Source-80,Destination,map);
            if(length<min_pathlength || length==min_pathlength && min_traffic > ControlCenter.getTrafficFlow(SourceX,SourceY,SourceX-1,SourceY)){
                direction = Direction.up;
                map = new boolean[81][81];
                min_pathlength = bfs(Source-80,Destination,map);
                min_traffic = ControlCenter.getTrafficFlow(SourceX,SourceY,SourceX-1,SourceY);
            }
        }map = new boolean[81][81];
        if(ControlCenter.City[SourceX][SourceY].down ){
            length = bfs(Source+80,Destination,map);
            if(length<min_pathlength || length == min_pathlength && min_traffic >= ControlCenter.getTrafficFlow(SourceX,SourceY,SourceX+1,SourceY)){
                direction = Direction.down;
            }
        }
        if(direction == null){
            System.out.println("Path can not be found.");
            System.exit(0);
        }
        else if(direction.equals(Direction.up)){
            path[SourceX][SourceY]=true;
            path[SourceX-1][SourceY] = true;
        }
        else if(direction.equals(Direction.down)){
            path[SourceX][SourceY]=true;
            path[SourceX+1][SourceY] = true;
        }
        else if(direction.equals(Direction.left)){
            path[SourceX][SourceY]=true;
            path[SourceX][SourceY-1] = true;
        }
        else if(direction.equals(Direction.right)){
            path[SourceX][SourceY]=true;
            path[SourceX][SourceY+1] = true;
        }

//        bfs(Source,Destination,path);
    }
    public int bfs(int Source, int Destination, boolean map[][])
        /*
        * Effects : find the shortest path with the smallest traffic flow and return the length of the path
        * */
    {
        int [][] Parent = new int[81][81];
        int [][] trafficFlow = new int[81][81];
        int [][] PathLength = new int [81][81];
        LinkedList<Integer> queue = new LinkedList<>();
        int SourceX = Source / 80;
        int SourceY = Source % 80 +1;
        int DestinationX = Destination / 80;
        int DestinationY = Destination % 80+1;
        if(SourceX==DestinationX && SourceY==DestinationY)
        {
            map[SourceX][SourceY]=true;
            return 0;
        }
        PathLength[SourceX][SourceY] = 1;
        queue.addLast(Source);
        while(!queue.isEmpty()){
            int S = queue.removeFirst();
            int Sx = S / 80;
            int Sy = S % 80 + 1;
            try{
                if(Destination == (80*(Sx)+Sy-1)){
                    break;
                }
                if(ControlCenter.City[Sx][Sy].left && (Parent[Sx][Sy-1]==0 || (Parent[Sx][Sy-1]!=0 && trafficFlow[Sx][Sy-1] > trafficFlow[Sx][Sy] + ControlCenter.getTrafficFlow(Sx,Sy,Sx,Sy-1) && PathLength[Sx][Sy]+1<=PathLength[Sx][Sy-1]))){
                    queue.addLast(80*(Sx)+Sy-1-1);
                    Parent[Sx][Sy-1]=Sx*80+Sy-1;
                    trafficFlow[Sx][Sy-1] = trafficFlow[Sx][Sy] + ControlCenter.getTrafficFlow(Sx,Sy,Sx,Sy-1);
                    PathLength[Sx][Sy-1] = PathLength[Sx][Sy] + 1;
                }
                if(ControlCenter.City[Sx][Sy].right && (Parent[Sx][Sy+1]==0 || (Parent[Sx][Sy+1]!=0 && trafficFlow[Sx][Sy+1] > trafficFlow[Sx][Sy] + ControlCenter.getTrafficFlow(Sx,Sy,Sx,Sy+1) && PathLength[Sx][Sy]+1<=PathLength[Sx][Sy+1]))){
                    queue.addLast(80*(Sx)+Sy+1-1);
                    Parent[Sx][Sy+1]=Sx*80+Sy-1;
                    trafficFlow[Sx][Sy+1] = trafficFlow[Sx][Sy] + ControlCenter.getTrafficFlow(Sx,Sy,Sx,Sy+1);
                    PathLength[Sx][Sy+1] = PathLength[Sx][Sy] + 1;
                }
                if(ControlCenter.City[Sx][Sy].up && (Parent[Sx-1][Sy]==0 || (Parent[Sx-1][Sy]!=0 && trafficFlow[Sx-1][Sy] > trafficFlow[Sx][Sy] + ControlCenter.getTrafficFlow(Sx,Sy,Sx-1,Sy) && PathLength[Sx][Sy]+1<=PathLength[Sx-1][Sy]))){
                    queue.addLast(80*(Sx-1)+Sy-1);
                    Parent[Sx-1][Sy]=Sx*80+Sy-1;
                    trafficFlow[Sx-1][Sy] = trafficFlow[Sx][Sy] + ControlCenter.getTrafficFlow(Sx-1,Sy,Sx,Sy);
                    PathLength[Sx-1][Sy] = PathLength[Sx][Sy] + 1;
                }
                if(ControlCenter.City[Sx][Sy].down && (Parent[Sx+1][Sy]==0 || (Parent[Sx+1][Sy]!=0 && trafficFlow[Sx+1][Sy] > trafficFlow[Sx][Sy] + ControlCenter.getTrafficFlow(Sx,Sy,Sx+1,Sy) && PathLength[Sx][Sy]+1<=PathLength[Sx+1][Sy]))){
                    queue.addLast(80*(Sx+1)+Sy-1);
                    Parent[Sx+1][Sy]=Sx*80+Sy-1;
                    trafficFlow[Sx+1][Sy] = trafficFlow[Sx][Sy] + ControlCenter.getTrafficFlow(Sx+1,Sy,Sx,Sy);
                    PathLength[Sx+1][Sy] = PathLength[Sx][Sy] + 1;
                }
            }catch (ArrayIndexOutOfBoundsException e ){
                System.out.println("Out of bounce : "+Sx+" "+Sy);
                System.out.println();
            }
        }
        int pathlength=0;
        if(Parent[DestinationX][DestinationY] == 0){
            System.out.println("Path cannot be found from "+SourceX+" "+SourceY+" to "+DestinationX+" "+DestinationY);
            System.exit(0);
        }
        else{
            map[DestinationX][DestinationY]=true;
            while(!map[SourceX][SourceY]){
                pathlength++;
                int parent = Parent[DestinationX][DestinationY];
                DestinationX = parent / 80;
                DestinationY = parent % 80+1;
                map[DestinationX][DestinationY] = true;
                if(SourceX==DestinationX && SourceY==DestinationY){
                    break;
                }
            }
        }
        return pathlength;
    }
    public void run(){
        try{
        click = true;
        while(click){
            if(path[x][y]){
                path[x][y] = false;
                if(x>1 && path[x-1][y] && ControlCenter.City[x][y].up){
                    try {
                        if(ControlCenter.City[x][y].NormalCross && ControlCenter.trafficLight.getStatus("Vertical").equals("Red") && !CurrentDirection.equals(Direction.left))
                        {
                            while(ControlCenter.trafficLight.getStatus("Vertical").equals("Red"))
                            {
                                Thread.sleep(10);
                            }
                        }
//                        System.out.println(x+" "+y+" "+CurrentDirection+" "+System.currentTimeMillis()+ControlCenter.trafficLight.getStatus("Vertical"));
                        ControlCenter.City[x][y].CarOut(this);
                        Thread.sleep(20);
                        ControlCenter.addTrafficFlow(x,y,x-1,y);
                        Thread.sleep(60);
                        ControlCenter.subTrafficFlow(x,y,x-1,y);
                        Thread.sleep(20);
                        x=x-1;
                        CurrentDirection = Direction.up;
                        ControlCenter.City[x][y].CarIn(this);
                        if(Status.equals(State.Picking)){
                        FindPath(x * 80 + y - 1, passenger.x * 80 + passenger.y - 1);
                        }
                        else if(Status.equals(State.Serving)){
                            FindPath(x * 80 + y - 1, passenger.Dx * 80 + passenger.Dy - 1);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }else if(x<80 && path[x+1][y] && ControlCenter.City[x][y].down){
                    try {
                        if(ControlCenter.City[x][y].NormalCross && ControlCenter.trafficLight.getStatus("Vertical").equals("Red") && !CurrentDirection.equals(Direction.right))
                        {
                            while(ControlCenter.trafficLight.getStatus("Vertical").equals("Red"))
                            {
                                Thread.sleep(10);
                            }
                        }
//                        System.out.println(x+" "+y+" "+CurrentDirection+" "+System.currentTimeMillis()+ControlCenter.trafficLight.getStatus("Vertical"));
                        ControlCenter.City[x][y].CarOut(this);
                        Thread.sleep(20);
                        ControlCenter.addTrafficFlow(x,y,x+1,y);
                        Thread.sleep(60);
                        ControlCenter.subTrafficFlow(x,y,x+1,y);
                        Thread.sleep(20);
                        x=x+1;
                        CurrentDirection = Direction.down;
                        ControlCenter.City[x][y].CarIn(this);
                        if(Status.equals(State.Picking)){
                            FindPath(x * 80 + y - 1, passenger.x * 80 + passenger.y - 1);
                        }
                        else if(Status.equals(State.Serving)){
                            FindPath(x * 80 + y - 1, passenger.Dx * 80 + passenger.Dy - 1);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }else if(y>1 && path[x][y-1] && ControlCenter.City[x][y].left){
                    try {
                        if(ControlCenter.City[x][y].NormalCross && ControlCenter.trafficLight.getStatus("Horizontal").equals("Red") && !CurrentDirection.equals(Direction.down))
                        {
                            while(ControlCenter.trafficLight.getStatus("Horizontal").equals("Red"))
                            {
                                Thread.sleep(10);
                            }
                        }
//                        System.out.println(x+" "+y+" "+CurrentDirection+" "+System.currentTimeMillis()+ControlCenter.trafficLight.getStatus("Vertical"));
                        ControlCenter.City[x][y].CarOut(this);
                        Thread.sleep(20);
                        ControlCenter.addTrafficFlow(x,y,x,y-1);
                        Thread.sleep(60);
                        ControlCenter.subTrafficFlow(x,y,x,y-1);
                        Thread.sleep(20);
                        y=y-1;
                        CurrentDirection = Direction.left;
                        ControlCenter.City[x][y].CarIn(this);
                        if(Status.equals(State.Picking)){
                            FindPath(x * 80 + y - 1, passenger.x * 80 + passenger.y - 1);
                        }
                        else if(Status.equals(State.Serving)){
                            FindPath(x * 80 + y - 1, passenger.Dx * 80 + passenger.Dy - 1);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }else if(y<80 && path[x][y+1] && ControlCenter.City[x][y].right){
                    try {
                        if(ControlCenter.City[x][y].NormalCross && ControlCenter.trafficLight.getStatus("Horizontal").equals("Red") && !CurrentDirection.equals(Direction.up))
                        {
                            while(ControlCenter.trafficLight.getStatus("Horizontal").equals("Red"))
                            {
                                Thread.sleep(10);
                            }
                        }
//                        System.out.println(x+" "+y+" "+CurrentDirection+" "+System.currentTimeMillis()+ControlCenter.trafficLight.getStatus("Vertical"));
                        ControlCenter.City[x][y].CarOut(this);
                        Thread.sleep(20);
                        ControlCenter.addTrafficFlow(x,y,x,y+1);
                        Thread.sleep(60);
                        ControlCenter.subTrafficFlow(x,y,x,y+1);
                        Thread.sleep(20);
                        y=y+1;
                        CurrentDirection = Direction.right;
                        ControlCenter.City[x][y].CarIn(this);
                        if(Status.equals(State.Picking)){
                            FindPath(x * 80 + y - 1, passenger.x * 80 + passenger.y - 1);
                        }
                        else if(Status.equals(State.Serving)){
                            FindPath(x * 80 + y - 1, passenger.Dx * 80 + passenger.Dy - 1);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                else if(Status.equals(State.Serving) && (x!=passenger.Dx || y!=passenger.Dy))
                {
                    FindPath(x * 80 + y - 1, passenger.Dx * 80 + passenger.Dy - 1);
                }
                else if(Status.equals(State.Picking) && (x!=passenger.x || y!=passenger.y))
                {
                    FindPath(x * 80 + y - 1, passenger.x * 80 + passenger.y - 1);
                }
                if(!path[x][y]){
                    try {
                        Thread.sleep(1000);
                        runningtime = 0;
                        if(Status.equals(State.Picking)){
                            FindPath(x * 80 + y - 1, passenger.Dx * 80 + passenger.Dy - 1);
                            System.out.println("Passenger is picked");
                            Status = State.Serving;
                        }
                        else if(Status.equals(State.Serving)){
                            System.out.println("The taxi"+id+" finishes the job in the spot("+x+","+y+")");
                            passenger = null;
                            Status = State.Idle;
                            ControlCenter.AddToIdleSet(this);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            else {
                int min = Integer.MAX_VALUE;
                Random random = new Random();
                Direction direction=Direction.up;
                if(Status.equals(State.Idle) && ControlCenter.City[x][y].up)
                {
                    if(min > ControlCenter.getTrafficFlow(x,y,x-1,y))
                    {
                        min = ControlCenter.getTrafficFlow(x,y,x-1,y);
                        direction = Direction.up;
                    }
                }
                if(Status.equals(State.Idle) && ControlCenter.City[x][y].down)
                {
                    if(min > ControlCenter.getTrafficFlow(x,y,x+1,y))
                    {
                        min = ControlCenter.getTrafficFlow(x,y,x+1,y);
                        direction = Direction.down;
                    }
                    else  if(min == ControlCenter.getTrafficFlow(x,y,x+1,y))
                    {
                        if(random.nextInt(2)==1)
                        {
                            direction = Direction.down;
                        }
                    }
                }
                if(Status.equals(State.Idle) && ControlCenter.City[x][y].left)
                {
                    if(min > ControlCenter.getTrafficFlow(x,y-1,x,y))
                    {
                        min = ControlCenter.getTrafficFlow(x,y-1,x,y);
                        direction = Direction.left;
                    }
                    else  if(min == ControlCenter.getTrafficFlow(x,y-1,x,y))
                    {
                        if(random.nextInt(3)==1)
                        {
                            direction = Direction.left;
                        }
                    }
                }
                if(Status.equals(State.Idle) && ControlCenter.City[x][y].right)
                {
                    if(min > ControlCenter.getTrafficFlow(x,y+1,x,y))
                    {
                        min = ControlCenter.getTrafficFlow(x,y+1,x,y);
                        direction = Direction.right;
                    }
                    else  if(min == ControlCenter.getTrafficFlow(x,y+1,x,y))
                    {
                        if(random.nextInt(4)==1)
                        {
                            direction = Direction.right;
                        }
                    }
                }
                if(false && Status.equals(State.Idle) && direction.equals(Direction.up)&& ControlCenter.City[x][y].up){
//                  System.out.println("The taxi"+id+"is in the spot("+x+","+y+")");
                    try {

                        if(ControlCenter.City[x][y].NormalCross && ControlCenter.trafficLight.getStatus("Vertical").equals("Red") && !CurrentDirection.equals(Direction.left))
                        {
                            while(ControlCenter.trafficLight.getStatus("Vertical").equals("Red"))
                            {
                                Thread.sleep(10);
                            }
                        }
                        ControlCenter.City[x][y].CarOut(this);
                        x=x-1;
                        ControlCenter.City[x][y].CarIn(this);
                        Thread.sleep(20);
                        ControlCenter.addTrafficFlow(x,y,x+1,y);
                        Thread.sleep(60);
                        ControlCenter.subTrafficFlow(x,y,x+1,y);
                        Thread.sleep(20);
                        CurrentDirection  = Direction.up;
//                    System.out.println("The taxi"+id+"is in the spot("+x+","+y+")");
                        runningtime += 100;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }else if(false && Status.equals(State.Idle) && direction.equals(Direction.down)&& ControlCenter.City[x][y].down){
//                    System.out.println("The taxi"+id+"is in the spot("+x+","+y+")");
                    try {
                        if(ControlCenter.City[x][y].NormalCross && ControlCenter.trafficLight.getStatus("Vertical").equals("Red") && !CurrentDirection.equals(Direction.right))
                        {
                            while(ControlCenter.trafficLight.getStatus("Vertical").equals("Red"))
                            {
                                Thread.sleep(10);
                            }
                         }
                        ControlCenter.City[x][y].CarOut(this);
                        x=x+1;
                        ControlCenter.City[x][y].CarIn(this);
                        Thread.sleep(20);
                        ControlCenter.addTrafficFlow(x,y,x-1,y);
                        Thread.sleep(60);
                        ControlCenter.subTrafficFlow(x,y,x-1,y);
                        Thread.sleep(20);
                        CurrentDirection  = Direction.down;

//                    System.out.println("The taxi"+id+"is in the spot("+x+","+y+")");
                        runningtime += 100;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }else if(false && Status.equals(State.Idle) && direction.equals(Direction.left)&& ControlCenter.City[x][y].left){
//                    System.out.println("The taxi"+id+"is in the spot("+x+","+y+")");
                    try {
                        if(ControlCenter.City[x][y].NormalCross && ControlCenter.trafficLight.getStatus("Vertical").equals("Red") && !CurrentDirection.equals(Direction.down))
                        {
                            while(ControlCenter.trafficLight.getStatus("Horizontal").equals("Red"))
                            {
                                Thread.sleep(10);
                            }
                        }
                        ControlCenter.City[x][y].CarOut(this);
                        y=y-1;
                        ControlCenter.City[x][y].CarIn(this);
                        Thread.sleep(20);
                        ControlCenter.addTrafficFlow(x,y,x,y+1);
                        Thread.sleep(60);
                        ControlCenter.subTrafficFlow(x,y,x,y+1);
                        Thread.sleep(20);
                        CurrentDirection  = Direction.left;

//                    System.out.println("The taxi"+id+"is in the spot("+x+","+y+")");
                        runningtime += 100;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }else if(false && Status.equals(State.Idle) && direction.equals(Direction.right)&& ControlCenter.City[x][y].right){
//                    System.out.println("The taxi"+id+"is in the spot("+x+","+y+")");
                    try {
                        if(ControlCenter.City[x][y].NormalCross && ControlCenter.trafficLight.getStatus("Vertical").equals("Red") && !CurrentDirection.equals(Direction.up))
                        {
                            while(ControlCenter.trafficLight.getStatus("Horizontal").equals("Red"))
                            {
                                Thread.sleep(10);
                            }
                        }
                        ControlCenter.City[x][y].CarOut(this);
                        y=y+1;
                        ControlCenter.City[x][y].CarIn(this);
                        Thread.sleep(20);
                        ControlCenter.addTrafficFlow(x,y,x,y-1);
                        Thread.sleep(60);
                        ControlCenter.subTrafficFlow(x,y,x,y-1);
                        Thread.sleep(20);
                        CurrentDirection  = Direction.right;

//                    System.out.println("The taxi"+id+"is in the spot("+x+","+y+")");
                        runningtime += 100;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if(runningtime>=20000)
                {
//                   System.out.println("Driver is sleeping");
                    runningtime = 0;
                    try {
                        Status = State.Dead;
                        Thread.sleep(1000);
                        Status = State.Idle;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
//                if(Status.equals(State.Idle))
//                {
//                    System.out.println(x+","+y);
//                }
            }
        }
        }catch(Throwable a ){
            System.out.println("Error happens.");
            a.printStackTrace();
            System.exit(0);
        }
    }

    public String toString()
    /*
        * Requires : Nome
        * Modifies : None
        * Effects : return the car status
    * */
    {
        return "The taxi "+id+" is in the spot("+x+","+y+")   --> State : " + Status + "Credibility : "+Credibility;
    }
    public void close()
    /*
        * Requires : Nome
        * Modifies : None
        * Effects : end the car thread
     * */
    {
        click = true;
    }
}
