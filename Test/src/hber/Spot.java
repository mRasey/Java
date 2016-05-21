package hber;

import java.util.HashSet;

/**
 * Created by cc on 2016/4/18.
 */
public class Spot {
    public int col;
    public int row;
    public boolean up;
    public boolean down;
    public boolean left;
    public boolean right;
    public boolean enableTrafficLight;
    public boolean NormalCross;
    private HashSet<Car> CarSet;
    public Spot(int r,int c)
        /*
        * Requires : Nome
        * Modifies : this
        * Effects : Initialize.
        * */
    {
        col = c;
        row = r;
        up=down=left=right=false;
        CarSet = new HashSet<>();
        NormalCross=false;
        enableTrafficLight =false;
    }
    public synchronized void CarIn(Car car)
    /*
        * Requires : Nome
        * Modifies : this
        * Effects : let the car get into the spot.
        * */
    {
        CarSet.add(car);
    }
    public synchronized void CarOut(Car car)
        /*
        * Requires : Nome
        * Modifies : this
        * Effects : let the car get away from the spot.
        * */
    {
        CarSet.remove(car);
    }
    public synchronized void getBestCar(HashSet<Car> carSet)
                /*
        * Requires : carSet should have been initialized
        * Modifies : this
        * Effects : let the car get into the spot.
        * */
    {
//        System.out.println(col+" "+row+" "+CarSet.size());
        for(Car car:CarSet)
        {
            if(!carSet.contains(car)){
                if(car.getStatus().equals("Idle"))
                {
                    car.increCredibility();
//                    System.out.println(car.getID()+" qiangdan at"+car.x+" "+car.y);
                    carSet.add(car);
                }
            }
        }
        return ;
    }
    public String toString()
    {
        return "row : "+ row +"col : "+col;
    }
    public void connect(int r,int c)
        /*
        * Requires : None
        * Modifies : None
        * Effects : connect two spots
        * */
    {
        if(Math.abs(r-row)<=1 && Math.abs(c-col)<=1){
            if(Math.abs(r-row)==1 && Math.abs(c-col)==1){
                return;
            }
            else{
                if(Math.abs(r-row)==1){
                    if(r>row)
                    {
                        down = true;
                    }
                    else
                    {
                        up = true;
                    }
                }
                else if(Math.abs(c-col)==1){
                    if(c>col){
                        right = true;
                    }
                    else{
                        left = true;
                    }
                }
            }
        }
        int around = 0;
        if(left)
            around++;
        if(right)
            around++;
        if(up)
            around++;
        if(down)
            around++;
        if(around>=3 && NormalCross)
            enableTrafficLight = true;
        else
            enableTrafficLight = false;

    }
    public void disconnect(int r,int c)
            /*
        * Requires : None
        * Modifies : None
        * Effects : disconnect two spots
        * */
    {
        if(Math.abs(r-row)<=1 && Math.abs(c-col)<=1){
            if(Math.abs(r-row)==1 && Math.abs(c-col)==1){
                return;
            }
            else{
                if(Math.abs(r-row)==1){
                    if(r>row)
                    {
                        down = false;
                    }
                    else
                    {
                        up = false;
                    }
                }
                else if(Math.abs(c-col)==1){
                    if(c>col){
                        right = false;
                    }
                    else{
                        left = false;
                    }
                }
            }
            int around = 0;
            if(left)
                around++;
            if(right)
                around++;
            if(up)
                around++;
            if(down)
                around++;
            if(around>=3 && NormalCross)
                enableTrafficLight = true;
            else
                enableTrafficLight = false;
        }
    }
}
