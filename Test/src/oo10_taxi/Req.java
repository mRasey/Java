package oo10_taxi;
//Overview
//pack the message into Req
public class Req {
	private int condition;
	private int goalCon;
	private long time;
	private int[] taxiId;
	private int Max;
	private int[] taxiID;
	private int num=0;
	public Req(int condition,int goalCon){
		this.condition = condition;
		this.goalCon = goalCon;
		this.Max = 100;
		this.taxiId = new int[Max];
		this.taxiID = new int[Max];
		this.time = System.currentTimeMillis();
	}
	public long getTime(){
		return this.time;
	}
	public int getCondition(){
		return this.condition;
	}
	public int getGoalCon(){
		return this.goalCon;
	}
	public int gettaxiId(int ID){
		return this.taxiId[ID];
	}
	public int[] gettaxi(){
		return this.taxiID;
	}
	public void setTaxiId(int ID){
		this.taxiId[ID] = 1;
		this.taxiID[num++] = ID;
	}
	public int getNum(){
		return this.num;
	}
	
	public boolean repOK(){
		if(this.condition < 0 || this.condition > 6399)
			return false;
		if(this.goalCon < 0 || this.goalCon > 6399)
			return false;
		return true;
	}
}
