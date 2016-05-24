package taxi;

import java.util.ArrayList;

public class request {//乘客请求
	private int x1,y1,x2 ,y2;
	long time = 0;
	private ArrayList<taxi> taxiqueue = new ArrayList<taxi>();
	public boolean reqOK()
	{//Requires：
		//Modifies：
		//Effects：检测属性是否符合要求，符合返回true，否则返回false
		if(x1<0||x1>=80)return false;
		if(x2<0||x2>=80)return false;
		if(y1<0||y2>=80)return false;
		if(y2<0||y2>=80)return false;
		if(time<0)return false;
		if(taxiqueue==null)return false;
		return true;
	}
	public request(int x1,int y1,int x2,int y2)
	{
		//Requires：四个int类型的数字，都必须满足0<=x1,y1,x2,y2<=79
		//Modifies：x1,y1,x2,y2,time
		//Effects：初始化乘客请求的方法，(x1,y1)为乘客当前位置坐标,(x2,y2)为乘客目标位置坐标，根据乘客的位置及目标初始化该请求
			this.x1 = x1;
			this.y1 = y1;
			this.x2 = x2;
			this.y2 = y2;
			this.time = System.currentTimeMillis();
	}
	public int getx1(){
		//Requires：
		//Modifies：
		//Effects：返回请求当前位置的横坐标
		return x1;
	}
	public int gety1()
	{	//Requires：
		//Modifies：
		//Effects：返回请求当前位置的纵坐标
		return y1;
	}
	public int getx2()
	{	//Requires：
		//Modifies：
		//Effects：返回请求目标位置的横坐标
		
		return x2;
	}
	public int gety2()
	{
		//Requires：
		//Modifies：
		//Effects：返回请求目标位置的纵坐标
		return y2;
	}
	public long gettime()
	{	//Requires：
		//Modifies：
		//Effects：返回请求产生的时间
		return time;
	}
	public ArrayList<taxi> gettaxiqueue()
	{	//Requires：
		//Modifies：
		//Effects：返回可以响应该请求的出租车队列		
		return taxiqueue;
	}
}
