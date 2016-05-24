package oo10_taxi;
import java.util.*;
//Overview
//add the Req into Queue and provide the function with creating Req
public class ReqQueue{
	private Vector <Req> Queue;
	private int count;
	public ReqQueue(){
		Queue = new Vector<Req>();
		this.count = 0;
	}
	//EFFECTS:if the taxi could answer the Req,return true,else return false
	private boolean check(Req R,Taxi taxi){
		int x = R.getCondition()/80;
		int y = R.getCondition()%80;
		int x1 = taxi.getContion()/80;
		int y1 = taxi.getContion()%80;
		if(x <= x1+2 && x >= x1-2 && y <= y1+2 && y >= y1-2)
			return true;
		return false;
	}
	//MODIFIED:find the number of taxi which could answer the Req in Queue and improve the taxi's credit
	//EFFECTS:if one taxi could answer the Req,mark it in the Rrq's mark array and improve it's credit
	public synchronized void scan(Taxi taxi){
		for(int i = 0;i < count;i++){
			Req R = Queue.get(i);
			if(R.gettaxiId(taxi.getID()) != 1 && check(R,taxi)){
				R.setTaxiId(taxi.getID());
				taxi.improveCredit(1);
			}
		}
	}
	//MODIFIED:remove one element whose order number is i in Queue and the count of Req was subtracted 1
	public synchronized void remove(int i){
		this.Queue.remove(i);
		this.count--;
	}
	public synchronized Req getReq(int i){
		return  Queue.get(i);
	}
	public synchronized int getSize(){
		return this.count;
	}
	public synchronized boolean ifEmpty(){
		return this.count == 0;
	}
	//EFFECTS: if the Queue is empty,stop the Thread ;
	//		   when the Thread was notified,notify the other Thread
	public synchronized void check(){
		synchronized(this){
			while(ifEmpty()){
				try{
					wait();
				}catch (InterruptedException e){
					e.printStackTrace();
				} 
			}notifyAll(); 
		}
	}
	//MODIFIED: add one random req in Queue
	//EFFECTS:  add one req in Queue and notify all the Thread
	public synchronized void installReqRandom(){
		Random rd = new Random();
		int x;
		int y;
		do{
			x = rd.nextInt(6400);
			y = rd.nextInt(6400);
		}while(x == y);
		Req Req = new Req(x,y);
		Queue.add(Req);
		this.count++;
		notifyAll();
	}
	//EFFECTS: judge if the point (x,y) in the map:if true return true ,else return false 
	private boolean maintain(int x,int y){
		if(x>=0&&x<=79&&y>=0&&y<=79)
			return true;
		return false;
	}
	//MODIFIED: add one req in Queue
	//EFFECTS: if point (x,y) and (x1,y1) is not in the map or the two point was same ,print error message and return
	//		   else add one req in Queue and notify all the Thread
	public synchronized void installReq(int x,int y,int x1,int y1){
		if(!maintain(x,y)){
			System.out.println("坐标("+x+","+y+")不在地图上");
			return;
		}else if(!maintain(x1,y1)){
			System.out.println("坐标("+x1+","+y1+")不在地图上");
			return;
		}
		if(x == x1 && y == y1){
			System.out.println("请求的产生地不能与目标地一致");
			return;
		}
		Req Req = new Req(x*80+y,x1*80+y1);
		Queue.add(Req);
		this.count++;
		notifyAll();
	}
	public boolean repOK(){
		if(this.count < 0)
			return false;
		return true;
	}
}
