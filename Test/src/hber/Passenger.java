package hber;

import java.util.HashSet;
import java.util.concurrent.TimeUnit;

/**
 * Created by cc on 2016/4/20.
 */
public class Passenger extends Thread{
    public int x,y;
    public int Dx,Dy;
    public Passenger(int X, int Y,int DX, int DY) throws Exception {
        /*
        * Requires : Nome
        * Modifies : None
        * Effects : Initialize.
        * */
        x = X;
        y = Y;
        Dx = DX;
        Dy = DY;
        if(x<=0 || x>=81 ||y<=0 || y>=81 ||Dy<=0 || Dy>=81 ||Dx<=0 || Dx>=81 )
            throw new Exception();
    }
    public void getCar(HashSet<Car> Carset){
        /*
        * Requires : Carset should have been initialized.
        * Modifies : None
        * Effects : If there is a car in the qualified area, those cars will be added into the Carset.
        * */
        for(int i=Integer.max(x-2,1);i<=x+2 && i<=80;i++)
        {
            for(int j = Integer.max(y-2,1);j<=y+2 && j<=80;j++){
                ControlCenter.City[i][j].getBestCar(Carset);
            }
        }
        return ;
    }
    public void run(){
        int Count=0;
        boolean found = false;
        HashSet<Car> Carset = new HashSet<>();
        System.out.println("Passenger in ("+x+","+y+") Des is in ("+Dx+","+Dy+")");
        while(Count < 60){
            getCar(Carset);
            try {
                TimeUnit.MILLISECONDS.sleep(50);
                Count++;
            } catch (InterruptedException e) {
            }
        }
        Car maxcar = getMaxCar(Carset);
        while(maxcar!=null && !maxcar.stepAhead(this))
        {
            Carset.remove(maxcar);
            maxcar=getMaxCar(Carset);
        }
        if(maxcar==null){
            System.out.println("The passenger in the spot("+x+","+y+") can not found a taxi, so he suicides.");
        }
    }
    public Car getMaxCar(HashSet<Car> Carset)
    /*
        * Requires : Nome
        * Modifies : None
        * Effects : return the most appropriate car
        * */
    {
        Car maxcar = null;
        int Max =  Integer.MIN_VALUE;
        boolean[][] tmp = new boolean[81][81];
        for(Car car:Carset){
            if(car.getCredibility()>Max && car.getStatus().equals("Idle")){
                maxcar = car;
                Max = car.getCredibility();
            }
            else if(car.getCredibility()==Max && car.getStatus().equals("Idle") && car.bfs(car.x*80+car.y-1,x*80+y-1,tmp)<car.bfs(maxcar.x*80+maxcar.y-1,x*80+y-1,tmp)){
                maxcar = car;
                Max = car.getCredibility();
            }
        }
        return maxcar;
    }

}
