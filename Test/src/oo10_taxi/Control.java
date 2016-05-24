package oo10_taxi;

//Overview
//allocate Req to taxi
public class Control extends Thread{
	private Taxi[] taxi;
	private ReqQueue RQ;
	private int[][] way;
	public Control(Taxi[] taxi,ReqQueue RQ,int[][] way){
		this.RQ = RQ;
		this.taxi = taxi;
		this.way = way;
	}
	public void run(){
		Req r;
		while(true){
			RQ.check();
			while(!RQ.ifEmpty()){
				r = RQ.getReq(0);
				if((System.currentTimeMillis() - r.getTime())/3000 >=1){
					if(0 == r.getNum()){
						RQ.remove(0);
						System.out.println("locate ("+r.getCondition()/80+","+r.getCondition()%80+") no response");
						continue;
					}
					int id = find(r);
					if(-1 == id){
						RQ.remove(0);
						System.out.println("locate ("+r.getCondition()/80+","+r.getCondition()%80+") no response");
						continue;
					}
					taxi[id].setReq(r.getCondition(), r.getGoalCon());
					System.out.println(id+" car get passenger");
					RQ.remove(0);
					try{
						Thread.sleep(50);
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			}
		}
	}
	//REQUIRES:the req must in the Queue
	//EFFECTS:if there are no taxi responded to the req,return -1;
	//		  else return the num of the taxi whose credit is the highest
	private int find(Req Req){
		int taxi[] = Req.gettaxi();
		int num = Req.getNum();int j;
		int distance = way[this.taxi[taxi[0]].getContion()][Req.getCondition()] /10;
		int tempId = taxi[0];
		int tempCredit = this.taxi[tempId].getCredit();
		for(j = 0;j < num;j++){
			if(this.taxi[taxi[j]].getWork())
				continue;
			distance = way[this.taxi[taxi[j]].getContion()][Req.getCondition()]/10;
			tempId = taxi[j];
			tempCredit = this.taxi[tempId].getCredit();
			break;
		}
		if(j == num)	return -1;
		for(int i = 0;i < num;i++){
			if(tempCredit < this.taxi[taxi[i]].getCredit() && !this.taxi[taxi[i]].getWork()){
				tempId = taxi[i];
				tempCredit = this.taxi[taxi[i]].getCredit();
				distance = way[this.taxi[taxi[i]].getContion()][Req.getCondition()]/10;
			}else if(tempCredit == this.taxi[taxi[i]].getCredit() && !this.taxi[taxi[i]].getWork()){
				if(distance > way[this.taxi[taxi[i]].getContion()][Req.getCondition()]/10){
					tempId = taxi[i];
					tempCredit = this.taxi[taxi[i]].getCredit();
					distance = way[this.taxi[taxi[i]].getContion()][Req.getCondition()]/10;
				}
			}
		}
		return tempId;
	}
}
