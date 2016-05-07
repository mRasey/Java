import java.util.ArrayList;

class Request {
	private int[] position = new int[2];
	private int[] target = new int[2];
	private long startTime;
	private ArrayList<Taxi> taxis = new ArrayList<>();
	private boolean[] AlternateTaxi = new boolean[100];
	
	Request(int posX, int posY, int desX, int desY){
        //REQUIRES:none
        //MODIFIES:position，target，startTime
        //EFFECTS:创建Request实例
		position[0] = posX;
		position[1] = posY;
		target[0] = desX;
		target[1] = desY;
		startTime = 0;
	}
	
	int[] getPosition(){
        //REQUIRES:none
        //MODIFIES:none
        //EFFECTS:返回请求的起始坐标
		return position;
	}
	
	int[] getTarget(){
        //REQUIRES:none
        //MODIFIES:none
        //EFFECTS:返回请求的目的地坐标
        return target;
    }
	
	long getStartTime(){
        //REQUIRES:none
        //MODIFIES:none
        //EFFECTS:返回请求的开始时间
        return startTime;
    }
	
	void setStartTime(long time){
        //REQUIRES:none
        //MODIFIES:startTime
        //EFFECTS:修改请求的开始时间
        startTime = time;
    }
	
	void addTaxi(Taxi taxi, int n){
        //REQUIRES:none
        //MODIFIES:taxis，AlternateTaxi
        //EFFECTS:向备选车辆列表中加入出租车并修改出租车可用信息
		taxis.add(taxi);
		AlternateTaxi[n] = true;
	}
	
	ArrayList<Taxi> getTaxis(){
        //REQUIRES:none
        //MODIFIES:none
        //EFFECTS:返回备选车辆列表
        return taxis;
    }
	
	boolean Available(int n){
        //REQUIRES:none
        //MODIFIES:none
        //EFFECTS:返回记录出租车可用性的数组
        return AlternateTaxi[n];
    }
}
