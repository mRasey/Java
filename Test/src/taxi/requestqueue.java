package taxi;

import java.util.ArrayList;

public class requestqueue {//乘客请求队列
	private ArrayList<request> queue = new ArrayList<request>();
	public boolean reqOK()
	{//Requires：
		//Modifies：
		//Effects：检测属性是否符合要求，符合返回true，否则返回false
		if(queue == null)return false;
		return true;		
	}
	public synchronized void addtoqueue(int x1,int y1,int x2,int y2)
	{	//Requires：四个int类型的数字，都必须满足0<=x1,y1,x2,y2<=79
		//Modifies：queue
		//Effects：向乘客请求队列中添加请求的方法，(x1,y1)为乘客当前位置坐标,(x2,y2)为乘客目标位置坐标，根据乘客的位置及目标将其添加到请求队列中
		if(x1>=0&&x1<80&&y1>=0&&y1<80&&x2>=0&&x2<80&&y2>=0&&y2<80)
		{	
			request r = new request(x1,y1,x2,y2);
			queue.add(r);
		}
		else
			System.out.println("乘客位置有误");
	}
	public synchronized request getrequest(int i)
	{	//Requires：一个int类型的数字i，必须小于当前请求队列的长度
		//Modifies：
		//Effects：返回乘客请求队列中请求的方法，根据i值返回乘客请求队列中对应的请求
		return queue.get(i);
	}
	public synchronized void delete(int i)
	{	//Requires：一个int类型的数字i，必须小于当前请求队列的长度
		//Modifies：
		//Effects：删除乘客请求队列中请求的方法，根据i值删除乘客请求队列中对应的请求
		queue.remove(i);
	}
	public synchronized boolean hasrequest()
	{	//Requires：
		//Modifies：
		//Effects：判断乘客请求队列中是否含有请求的方法，若队列中有请求返回true，否则返回false
		if(queue.size() == 0)
			return false;
		else
			return true;
	}
	public synchronized int size()
	{	//Requires：
		//Modifies：
		//Effects：获得乘客请求队列大小的方法，返回乘客请求队列的长度
		return queue.size();
	}
}
