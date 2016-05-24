package oo10_taxi;

//Overview
//update the traffic of road
public class Buffer extends Thread {
	private int[][] road_X;
	private int[][] road_Y;
	private int[][] Yroad;
	private int[][] Xroad;
	private int size = 80;

	public Buffer(int[][] road_X,int[][] road_Y){
		this.road_X = road_X;
		this.road_Y = road_Y;
		this.Xroad = new int[size][size];
		this.Yroad = new int[size][size];
	}
	public void run(){
		copy(Xroad,road_X);
		copy(Yroad,road_Y);
		while(true){
			try{
				Thread.sleep(60);
			}catch(Exception e){
				e.printStackTrace();
			}
			copy(road_X,Xroad);
			copy(road_Y,Yroad);
			clean(Xroad);
			clean(Yroad);
		}
	}
	public int[][] getXroad(){
		return this.road_X;
	}
	public int[][] getYroad(){
		return this.road_Y;
	}
	//MODIFIES: clean the traffic of the road
	//EFFECTS: the traffic of a was clean
	private void clean(int[][] a){
		for(int i = 0;i < size;i++){
			for(int j = 0;j < size;j++){
				if(a[i][j]%10 == 1){
					a[i][j] = 1;
				}else{
					a[i][j] = 0;
				}
			}
		}
	}
	//REQUIRES:the size of a and b must be size*size
	//MODIFIED:the traffic of b was copy to a
	//EFFECTS:a was update
	private synchronized void copy(int[][] a,int[][] b){
		for(int i = 0;i < size;i++){
			for(int j = 0;j < size;j++){
				a[i][j] = b[i][j]/10*10+a[i][j]%10;
			}
		}
	}
	//REQUIRES:p must less than 6400 and greater than -1
	//MODIFIED: the traffic of road p was pushed one
	//EFFECTS: if the road p existed traffic of p was pushed one ,else print error message;
	public synchronized void trafficXroadAdd(int p){
		if(this.road_X[p/80][p%80]%10 == 0){
			System.out.println("the Xway was closed!");
		}else{
			this.Xroad[p/80][p%80]+=10;
		}
	}
	public synchronized void trafficYroadAdd(int p){
		if(this.road_Y[p/80][p%80]%10 == 0){
			System.out.println("the Yway was closed!");
		}else{
			this.Yroad[p/80][p%80]+=10;
		}
	}
	
	public boolean repOK(){
		if(this.size != 80){
			return false;
		}
		for(int i = 0;i < size;i++){
			for(int j = 0;j < size;j++){
				if(this.Xroad[i][j] %10 != 1 && this.Xroad[i][j] %10 != 0)
					return false;
				if(this.Yroad[i][j] %10 != 1 && this.Yroad[i][j] %10 != 0)
					return false;
			}
		}
		return true;
	}
}
