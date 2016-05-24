package taxi;

import java.io.IOException;
import java.util.Iterator;
import java.util.Random;

public class customer extends Thread{//乘客类（测试使用）
	private requestqueue queue;
	private taxi[] t;
	private map m;
	public customer(requestqueue queue,taxi[] t,map m)
	{
		//Requires：类型为requestqueue的初始化过的乘客请求队列,类型为taxi[]的出租车队列t，t中每个出租车均不能为null，类型为map的城市地图m，m不能为null
		//Modifies：queue,t,m
		//Effects：初始化该类，使其可以访问乘客请求队列,出租车队列及城市地图
		if(queue ==null)
		{
			System.out.println("customer custruct error");
			System.exit(0);
		}
		for(int i = 0;i<100;i++)
			if(t[i] == null)
			{
				System.out.println("customer custruct error");
				System.exit(0);
			}
		if(m == null)
		{
			System.out.println("customer custruct error");
			System.exit(0);
		}
		this.queue = queue;
		this.t = t;
		this.m = m;
	}
	public boolean reqOK()
	{//Requires：
		//Modifies：
		//Effects：检测属性是否符合要求，符合返回true，否则返回false
		if(queue ==null)return false;
		for(int i = 0;i<100;i++)
			if(t[i] == null)
				return false;
		if(m == null)
			return false;
		return true;		
	}
	public void addrequest(int x1,int y1,int x2,int y2)
	{	//Requires：四个int类型的数字，都必须满足0<=x1,y1,x2,y2<=79
		//Modifies：queue
		//Effects：向乘客请求队列中添加请求的方法，(x1,y1)为乘客当前位置坐标,(x2,y2)为乘客目标位置坐标，根据乘客的位置及目标将其添加到请求队列中
		queue.addtoqueue(x1, y1, x2, y2);
	}
	public void run()
	{
		Random rand = new Random();///////////////我的测试代码//////////////
		for(int i = 0;i<10;i++)///////////////我的测试代码//////////////
		{						///////////////我的测试代码//////////////
			try {				///////////////我的测试代码//////////////
				sleep(rand.nextInt(1000)+100);///////////////我的测试代码//////////////
			} catch (InterruptedException e) {///////////////我的测试代码//////////////
				// TODO Auto-generated catch block///////////////我的测试代码//////////////
				e.printStackTrace();///////////////我的测试代码//////////////
			}///////////////我的测试代码//////////////*/
			int x1,x2,y1,y2;///////////////我的测试代码//////////////
			x1 = rand.nextInt(80);///////////////我的测试代码//////////////
			y1 = rand.nextInt(80);///////////////我的测试代码//////////////
			x2 = rand.nextInt(80);///////////////我的测试代码//////////////
			y2 = rand.nextInt(80);///////////////我的测试代码//////////////
			addrequest(x1,y1,x2,y2);///////////////我的测试代码//////////////
			System.out.println("("+x1+","+y1+","+x2+","+y2+")");///////////////我的测试代码//////////////
			///////////////////////////////////////////////////////////////
			/////若重新编写请删除我的测试代码后编写////////////////////
			//////////////////////////////////////////////////////////////
		}	///////////////我的测试代码////////////////////////////////////
	}
	public Iterator getIterator(int id,int index) throws NoIteratorException
	{	//Requires：一个int类型的出租车编号id,0<=id<100，一个int类型的服务次数index，index>0
		//Modifies：
		//Effects：返回第id+1辆出租车（该出租车为可追踪出租车）的第index次服务路径的迭代器
		//如果该出租车不为可追踪出租车，则提示使用新出租车获取迭代器并没有迭代器并throw NoIteratorException
		//若编号不符合要求，则提示输入正确的出租车编号并没有迭代器并throw NoIteratorException
		//若该可追踪出租车没有服务过或输入的index大于其服务次数，提示没有迭代器并throw NoIteratorException
		//否则，返回该出租车的第index次服务路径的迭代器
		try{
			if(id>=0&&id<100)
			{
				if(t[id]!=null&&t[id] instanceof newtaxi)
				{
					return ((newtaxi)t[id]).diedai(index);
				}
				else
				{
					System.out.println("请使用新出租车获取迭代器");
					throw new Exception();
				}
			}
			else
			{
				System.out.println("请输入正确的出租车编号");
				throw new Exception();
			}
		}catch (Exception e) {
			System.out.println("没有迭代器");
		}
		throw new NoIteratorException();
	}
	public void changemap(int x,int y,int s){
		//Requires：三个int类型的数字，必须满足0<=x，y<=79，s为0,1,2,3中的一个
		//Modifies：map
		//Effects：更改地图的方法，将地图中点(x,y)的值改为s,若不满足要求则结束程序，若更改后地图不正确则结束程序
		m.changemap(x,y,s);
	}
	public void gettaxistate(int id){//查询出租车状态的接口////////////////////////////////////////////////////////////////////////////////////
		//Requires：一个int类型的出租车编号id,0<=id<100
		//Modifies：
		//Effects：查看出租车当前状态的方法，打印一系列字符串输出出租车当前状态
		//若输入不符合要求则提示taxistateerror
		try{
			if(id>=0&&id<100)
			{
				t[id].tostring();
			}
		}catch (Exception e) {
			System.out.println("taxistateerror");
		}	
	}
	public int getservecount(int id)
	{	//Requires：一个int类型的出租车编号id,0<=id<100
		//Modifies：
		//Effects：获取可追踪出租车的服务次数的方法，若第id+1辆出租车为可追踪出租车，则返回它的服务次数
		//若输入不符合要求则提示getservecounterror
		//若第id+1辆出租车不为可追踪出租车，则提示请使用新出租车获取服务次数
		try{
			if(id>=0&&id<100)
			{
				if(t[id]!=null&&t[id] instanceof newtaxi)
					return ((newtaxi)t[id]).getservecount();
				else
					System.out.println("请使用新出租车获取服务次数");
			}
		}catch (Exception e) {
			System.out.println("getservecounterror");
		}
		return 0;
	}
}
