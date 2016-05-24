package oo10_taxi;

//Overview
//change the color of light (1 to 2 or 2 to 1)
public class Light extends Thread{
	private int[][] map;
	private short[][] light_NS;
	private short[][] light_EW;
	private int size = 80;
	private long time = 300;
	
	public Light(int[][] map,short[][]light_NS,short[][] light_EW){
		this.map = map;
		this.light_NS = light_NS;
		this.light_EW = light_EW;
	}
	
	public void run(){
		while(true){
			try{
				sleep(time);
				changeColor();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	//EFFECTS:change the color of light (1 to 2 or 2 to 1)
	private void changeColor(){
		for(int i = 0;i < size;i++){
			for(int j = 0;j < size;j++){
				if(this.light_EW[i][j] == 1){
					this.light_EW[i][j] = 2;
				}else if(this.light_EW[i][j] == 2){
					this.light_EW[i][j] = 1;
				}
				if(this.light_NS[i][j] == 1){
					this.light_NS[i][j] = 2;
				}else if(this.light_NS[i][j] == 2){
					this.light_NS[i][j] = 1;
				}
			}
		}
	}
	//EFFECTS:judge the legality of the Light class
	public boolean repOK(){
		if(this.size != 80)
			return false;
		for(int i = 0;i < size;i++){
			for(int j = 0;j < size;j++){
				if((this.light_EW[i][j] > 2 || this.light_EW[i][j] < 0 )||(this.light_NS[i][j] > 2 || this.light_NS[i][j] < 0)){
					return false;
				}
			}
		}
		return true;
	}
	
}
