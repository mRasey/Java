package oo10_taxi;
import java.util.*;
//Overview
//record the message of taxi with ID condition and credit so that to simulate the movement of taxi
//this viptaxi should run in the initial map and could record the path message when it is taxing one passenger
//also it could return one iterator about one path message or all the path message
public class VipTaxi extends Taxi {
	public VipTaxi(int ID,int condition,ReqQueue RQ,int[][] map,int[][] way,Buffer buf,Map Map,short[][] light_NS,short[][] light_EW){
		super(ID,condition,RQ,map,way,buf,Map,light_NS,light_EW);
	}
	public void run(){
		int num = 0;
		ArrayList<String> record = new ArrayList<String>();
 			while(true){
				switch(getstate()){
					case 0 :
						scan(getRQ());random();
						try{
							sleep(100);num++;
							if(200 == num)	setState(3);
							if(getWork()){
								setState(1);
							}
						}catch(Exception e){
							e.printStackTrace();
						}break;
					case 1 :
						try{
							driveTo(getGoal()[0]);
							sleep(100);
							if(getContion() == getGoal()[0]){
								setIfPerson(true);
								record = new ArrayList<String>();
								addRecord(record);
								String s = "("+getContion()/80+","+getContion()%80+")";
								synchronized(record){
									record.add(s);
								}
								System.out.println(getID()+"号车到达乘客位置");
								setState(3);
							}break;
						}catch(Exception e){
							e.printStackTrace();
						}
					case 2:
						try{
							driveTo(getGoal()[1]);
							String s = "("+getContion()/80+","+getContion()%80+")";
							synchronized(record){
								record.add(s);
							}
							sleep(100);
							if(getContion() == getGoal()[1]){
								setIfPerson(false);
								improveCredit(3);
								setWork(false);
								record = null;
								System.out.println(getID()+"号车到达目的地");
								setState(3);
							}break;
						}catch(Exception e){
							e.printStackTrace();
						}
					case 3:
						try{
							sleep(1000);
							if(!getPerson()){
								num = 0;setState(0);
								break;
							}else{
								num = 0;setState(2);
								break;
							}
						}catch(Exception e){
							e.printStackTrace();
						}
				}
			}	
	}
	public boolean repOK(){
		if(getID() < 0 || getID() > 99)
			return false;
		if(getContion() < 0 || getContion() > 6399)
			return false;
		if(getCredit() < 0)
			return false;
		if(getDirection() < 0 || getDirection() > 3)
			return false;
		return true;
	}
}
