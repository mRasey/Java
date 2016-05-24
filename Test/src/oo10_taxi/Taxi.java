package oo10_taxi;
import java.util.*;
//Overview
//record the message of taxi with ID condition and credit so that to simulate the movement of taxi
public class Taxi extends Thread {
	private int credit;
	private int condition;
	private int ID;
	private int state;
	private int[] goal;
	private long time;
	private ReqQueue RQ;
	private Buffer buf;
	private Map Map;
	private int[][] map;
	private int[][] way;
	private boolean work = false;
	private boolean getPerson = false;
	private Random rd = new Random();
	private int direction;
	private short[][] light_NS;
	private short[][] light_EW;
	private Vector<ArrayList<String>> record;
	
	public Taxi(int ID,int condition,ReqQueue RQ,int[][] map,int[][] way,Buffer buf,Map Map,short[][] light_NS,short[][] light_EW){
		this.ID = ID;
		this.buf = buf;
		this.condition = condition;
		this.state = 0;
		this.credit  = 0;
		this.goal = new int[2];
		this.RQ = RQ;
		this.Map = Map;
		this.map = map;
		this.way = way;
		this.direction = rd.nextInt(4);
		this.time = System.currentTimeMillis();
		this.light_EW = light_EW;
		this.light_NS = light_NS;
		this.record = new Vector<ArrayList<String>>();
	}
	public int getCredit(){
		return this.credit;
	}
	public ReqQueue getRQ(){
		return this.RQ;
	}
	public int[] getGoal(){
		return this.goal;
	}
	public int getContion(){
		return this.condition;
	}
	public String getLocation(){
		return "("+this.condition/80+","+this.condition%80+")";
	}
	public long getTime(){
		return (System.currentTimeMillis()-this.time)/100;
	}
	public int getID(){
		return this.ID;
	}
	public int getDirection(){
		return this.direction;
	}
	public boolean getPerson(){
		return this.getPerson;
	}
	public Vector<ArrayList<String>> getRecord(){
		return this.record;
	}
	//EFFECTS:the credit of taxi was increased n
	public void improveCredit(int n){
		this.credit+=n;
	}
	//REQUIRES:NONE
	//MODIFIED:NONE
	//EFFECTS:return the iterator of all record which includes all the path message for the passengers
	public ListIterator<ArrayList<String>> getIterator(){
		synchronized(this.record){
			ListIterator <ArrayList<String>>iter = this.record.listIterator();
			return iter;
		}
	}
	public int getNumOfSer(){
		return this.record.size();
	}
	//REQUIRES:NONE
	//MODIFIED:NONE
	//EFFECTS:if n > the number of record or n <= 0,return empty iterator;else return the iterator of No.n-1 record
	public ListIterator<String> getIteratorByNum(int n){
		if(n > this.record.size()||n <= 0){
			Vector<String> tmp = new Vector<String>();
			ListIterator<String> iter = tmp.listIterator();
			return iter;
		}
		synchronized(this.record.get(n-1)){
			ListIterator<String> iter = this.record.get(n-1).listIterator();
			return iter;
		}
	}
	//REQUIRES:n should be less than the size of number and bigger than 0;
	//MODIFIED:NONE
	//EFFECTS:if n > the number of record or n <= 0,return;else print the message of record[n]
	public void IterToString(int n){
//		if(iter == null){
////			System.out.println("Illegal Iterator!");
//			return;
//		}
//		System.out.print(ID+":");
//		while(iter.hasNext()){
//			System.out.print(iter.next().toString()+" ");
//		}
//		return;
		if(n > this.record.size()||n <= 0){
			return;
		}
		synchronized(this.record.get(n-1)){
			Iterator<String> iter = this.record.get(n-1).iterator();
			System.out.print(ID+"号车:");
			while(iter.hasNext()){
				System.out.print(iter.next().toString()+" ");
			}
			System.out.println("");
		}
		return;
	}
	//REQUIRES:the record stores the information of road which the taxi has driven;
	//MODIFIED:this.record
	//EFFECTS:add one record of road message
	public void addRecord(ArrayList<String> record){
		this.record.addElement(record);
	}
	public void scan(ReqQueue RQ){
		RQ.scan(this);
	}
	//REQUIRES:the taxi was in state 0
	//EFFECTS:find a way randomly and drive to it
	private void randomDrive(){
		int nextPosition;
		do{
			nextPosition = rd.nextInt(6400);
		}while(nextPosition == this.condition);
		Drive(nextPosition);
	}
	//REQUIRES:the goal should be in the map
	//MODIFIED:the condition of taxi was changed
	//EFFECTS:the taxi drive through a side
	private void Drive(int nextPosition){
		int i = way[this.condition][nextPosition]%10;
		if(i == 3){
			this.condition+=1;
		}else if(4 == i){
			this.condition+=80;
		}else if(5 == i){
			this.condition+=-1;
		}else if(2 == i){
			this.condition+=-80;
		}
	}
	//REQUIRES:the taxi was in state 0
	//EFFECTS:find a way randomly and drive to it
	public void random(){
		int[] flag = ifOpen();
		int[] temp = findWay(flag);
		int i = 0;
		do{
			i = rd.nextInt(4);
		}while(temp[i] == 0);
		drive(i);
	}
	//REQUIRES:the taxi should in state 1 or state 2 and the point q should be in the map
	//MODIFIED:finished one drive and changed the position of taxi and increase the traffic
	//EFFECTS:find the best way from current position to q and changed taxi's position and increase traffic
	public void driveTo(int q){
		int[] flag = ifShortest(q);
		int[] temp = findWay(flag);
		int i = 0;
		do{
			i = rd.nextInt(4);
		}while(temp[i] == 0);
		drive(i);
	}
	//REQUIRES:the point q should be in the map and the taxi could find a way to drive to point q
	//EFFECTS:judge the impossible direction which could drive to destination with shortest path and return it by array
	private int[] ifShortest(int q){
		int[] flag = ifOpen();
		int[] distance = new int[4];
		int[] flag1 = new int[4];
		if(1 == flag[0]){
			distance[0] = Map.bfs(this.condition-1, q,this.map)+1;
		}
		if(1 == flag[1]){
			distance[1] = Map.bfs(this.condition+80, q,this.map)+1;
		}
		if(1 == flag[2]){
			distance[2] = Map.bfs(this.condition+1, q,this.map)+1;
		}
		if(1 == flag[3]){
			distance[3] = Map.bfs(this.condition-80, q,this.map)+1;
		}
		int temp = 80*80*2;
		for(int i = 0;i < 4;i++){
			if(distance[i] == 0)
				continue;
			else{
				temp = (distance[i] <= temp)?distance[i]:temp;
			}
		}
		for(int i = 0;i < 4;i++){
			if(distance[i] == temp){
				flag1[i] =1;
			}
		}
		return flag1;
	}
	//REQUIRES:i was in 0-3
	//MODIFIED:if the taxi wants to go straight or turn left,it needs to watch the traffic light;when the light turns green,return true;
	//			else wait for the light turning green,return true;
	private boolean ifCanRun(int i){
		if(this.light_EW[this.condition/80][this.condition%80] == 0){
			return true;
		}
		if(this.direction == 0 && 3 == i){
			return true;
		}else if(this.direction == 1 && 0 == i){
			return true;
		}else if(this.direction == 2 && 1 == i){
			return true;
		}else if(this.direction == 3 && 2 == i){
			return true;
		}else if(Math.abs(this.direction-i) == 2){
			return true;
		}
		while(true){
			if((this.direction == 2 || this.direction == 0)&&this.light_EW[this.condition/80][this.condition%80] == 1)
				break;
			if((this.direction == 1 || this.direction == 3)&&this.light_NS[this.condition/80][this.condition%80] == 1)
				break;
			try{
				sleep(20);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return true;
		
	}
	//REQUIRES:the value of flag should be 1 or 0(1 stands for the direction is workable and 0 is opposite)
	//EFFECTS:find the direction with minimum traffic among the workable direction and return it by array
 	private int[] findWay(int[] flag ){
		int traffic = 10000;
		int[] flag1= new int[4];
		int[] temp = {-1,-1,-1,-1};
		int x = this.condition/80;
		int y = this.condition%80;
		if(flag[0] == 1){
			temp[0] = buf.getYroad()[x][y-1];
			if(buf.getYroad()[x][y-1] <= traffic){
				traffic = buf.getYroad()[x][y-1];
			}
		}
		if(flag[1] == 1){
			temp[1] = buf.getXroad()[x][y];
			if(buf.getXroad()[x][y] <= traffic){
				traffic = buf.getXroad()[x][y];
			}
		}
		if(flag[2] == 1){
			temp[2] = buf.getYroad()[x][y];
			if(buf.getYroad()[x][y] <= traffic){
				traffic = buf.getYroad()[x][y];
			}
		}
		if(flag[3]==1){
			temp[3] = buf.getXroad()[x-1][y];
			if(buf.getXroad()[x-1][y] <= traffic){
				traffic = buf.getXroad()[x-1][y];
			}
		}
		for(int i = 0;i < 4;i++){
			if(temp[i] == traffic){
				flag1[i] = 1;
			}
		}
		return flag1;
	}
 	//REQUIRES:the direction should be reasonable
 	//MODIFIED:the condition of taxi was changed and increase the traffic of the road
 	//EFFECTS:the taxi drive through a side
	private void drive(int i){
		ifCanRun(i);
		if(0 == i){
			this.condition+=-1;
			buf.trafficYroadAdd(this.condition);
		}else if(1 == i){
			buf.trafficXroadAdd(this.condition);
			this.condition+=80;
		}else if(2 == i){
			buf.trafficYroadAdd(this.condition);
			this.condition+=1;
		}else if(3 == i){
			this.condition+=-80;
			buf.trafficXroadAdd(this.condition);
		}
		this.direction = i;
	}
	//REQUIRES:the map was installed
	//EFFECTS:judge the impossible direction which could drive to destination with shortest path and return it by array
	private int[] ifOpen(){
		int x = this.condition/80;
		int y = this.condition%80;
		int[] flag = new int[4];
		if(0 == x){
			if(map[x][y] == 2 || map[x][y] == 3){
				flag[1] = 1;
			}
		}else if(79 == x){
			if(map[x-1][y] == 2 || map[x-1][y] == 3){
				flag[3] = 1;
			}
		}else{
			if(map[x][y] == 2 || map[x][y] == 3){
				flag[1] = 1;
			}
			if(map[x-1][y] == 2 || map[x-1][y] == 3){
				flag[3] = 1;
			}
		}
		if(0 == y){
			if(map[x][y] == 1 || map[x][y] == 3){
				flag[2] = 1;
			}
		}else if(79 == y){
			if(map[x][y-1] == 1 || map[x][y-1] == 3){
				flag[0] = 1;
			}
		}else{
			if(map[x][y] == 1 || map[x][y] == 3){
				flag[2] = 1;
			}
			if(map[x][y-1] == 1 || map[x][y-1] == 3){
				flag[0] = 1;
			}
		}
		return flag;
	}
	//REQUIRES:the point goal1 and point goal2 are in the map
	//MODIFIED:chang the work state and get the points of Req
	//EFFECTS:get one Req and ready for work
	public void setReq(int goal1,int goal2){
		work = true;
		this.goal[0] = goal1;
		this.goal[1] = goal2;
	}
	public boolean getWork(){
		return this.work;
	}
	public void setWork(boolean workState){
		this.work = workState;
	}
	public void setIfPerson(boolean ifPerson){
		this.getPerson = ifPerson;
	}
	public int getstate(){
		return this.state;
	}
	public void setState(int state){
		this.state = state;
	}
	public void run(){
		int num = 0;
			while(true){
				switch(state){
					case 0 :
						scan(RQ);random();
						try{
							sleep(100);num++;
							if(200 == num)	state = 3;
							if(work) 	state = 1;
						}catch(Exception e){
							e.printStackTrace();
						}break;
					case 1 :
						try{
							driveTo(goal[0]);
							sleep(100);
							if(this.condition == goal[0]){
								this.getPerson = true;
								System.out.println(ID+"号车到达乘客位置");
								state = 3;
							}break;
						}catch(Exception e){
							e.printStackTrace();
						}
					case 2:
						try{
							driveTo(goal[1]);
							sleep(100);
							if(this.condition == goal[1]){
								this.getPerson = false;
								this.credit+=3;
								this.work = false;
								System.out.println(ID+"号车到达目的地");
								state = 3;
							}break;
						}catch(Exception e){
							e.printStackTrace();
						}
					case 3:
						try{
							sleep(1000);
							if(!getPerson){
								num = 0;state = 0;
								break;
							}else{
								num = 0;state = 2;
								break;
							}
						}catch(Exception e){
							e.printStackTrace();
						}
				}
			}	
	}
	
	public boolean repOK(){
		if(this.ID < 0 || this.ID > 99)
			return false;
		if(this.condition < 0 || this.condition > 6399)
			return false;
		if(this.credit < 0)
			return false;
		if(this.direction < 0 || this.direction > 3)
			return false;
		return true;
	}
}
