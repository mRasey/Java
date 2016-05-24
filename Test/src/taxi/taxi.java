package taxi;

import java.util.ArrayList;
import java.util.Random;


public class taxi extends Thread{//出租车类
	private static int allid = 0;
	private int id;
	private int credit = 0;
	private int time = 0;
	protected map map;
	protected int[][] mapflag = new int[80][80];
	protected int x,y,cx,cy,goalx,goaly,lastx,lasty;
	private volatile taxistate state = taxistate.waittoserve;
	protected ArrayList<road> temp = new ArrayList<road>();
	protected ArrayList<road> waytemp = new ArrayList<road>();
	protected boolean[] dirflag = new boolean[4];//0 up 1 left 2 down 3 right
	public taxi(int x,int y,map m)
	{	//Requires：出租车起始位置(x,y),两个int类型的数字，都必须满足0<=x,y<=79，一个类型为map的对象代表地图,map不为Null
		//Modifies：id,x,y,m
		//Effects：初始化该出租车，初始化其编号及位置，并使其可以访问地图
		if(0<=x&&x<=79&&0<=y&&y<=79&&m!=null)
		{	
			this.id = ++allid;
			this.x = x;
			this.y = y;
			this.map = m;
		}
		else
		{
			System.out.println("error");
			System.exit(0);
		}
	}
	public boolean reqOK()
	{
		//Requires：
		//Modifies：
		//Effects：检测属性是否符合要求，符合返回true，否则返回false
		if(allid<0||allid>=100)return false;
		if(x<0||x>=80)return false;
		if(y<0||y>=80)return false;
		if(id<0||id>=100)return false;
		if(credit<0)return false;
		if(time<0)return false;
		if(map==null)return false;
		for(int i =0;i<80;i++)
			for(int j = 0;j<80;j++)
				if(mapflag[i][j]!=0&&mapflag[i][j]!=1)
					return false;
		if(cx<0||cx>=80)return false;
		if(cy<0||cy>=80)return false;
		if(goalx<0||goalx>=80)return false;
		if(goaly<0||goaly>=80)return false;
		if(lastx<0||lastx>=80)return false;
		if(lasty<0||lasty>=80)return false;
		if(state!=taxistate.waittoserve&&state!=taxistate.readytoserve&&state!=taxistate.serving&&state!=taxistate.stop)return false;
		if(temp==null)return false;
		if(waytemp==null)return false;
		for(int i = 0;i<4;i++)
			if(dirflag[i]!=true&&dirflag[i]!=false)
				return false;
		return true;
	}
	public void run()
	{
		Random rand = new Random();
		while(true)
		{
			if (state == taxistate.waittoserve) {
				try {
					sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				time++;
				//synchronized (this) {
					if (time == 200) {
						state = taxistate.stop;
						time = 0;
					}
				//}
				//synchronized(map){
					while(!randomway(rand));
				//}
			}
			else if (state == taxistate.stop) {
				try {
					sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				time++;
				if (time == 10) {
					state = taxistate.waittoserve;
					time = 0;
				}
			}
			else if (state == taxistate.readytoserve) {
				try {
					sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("出租车" + id + "在路上" + x + "," + y + "," + cx + "," + cy);
				while(true){
					if(cx==x&&cy==y)
						break;
					else
					{
						//synchronized(map){
							while(!go(cx,cy,rand));
						//}
					}
					try {
						sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					//System.out.println(x+","+y);
				}
				time = 0;
				while (true) {
					time++;
					if (time == 10) {
						state = taxistate.serving;
						time = 0;
						break;
					}
					try {
						sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			if (state == taxistate.serving) {
				try {
					sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				while(true){
					if(goalx==x&&goaly==y)
						break;
					else
					{
						//synchronized(map){
							while(!go(goalx,goaly,rand));
						//}
					}
					try {
						sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					//System.out.println(x+","+y);
				}
				
				System.out.println("出租车" + id + "到达目的地" + cx + "," + cy + "," + goalx + "," + goaly);
				time = 0;
				while (true) {
					time++;
					if (time == 10) {
						state = taxistate.waittoserve;
						time = 0;
						break;
					}
					try {
						sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}
	public void setstate(int x1,int y1,int x2,int y2)
	{	//Requires：四个int类型的数字，都必须满足0<=x1,y1,x2,y2<=79
		//Modifies：cx,cy,goalx,goaly,goal,credit,state
		//Effects：给出租车请求并使其改变出租车状态的方法，(x1,y1)为乘客当前位置坐标,(x2,y2)为乘客目标位置坐标
		this.addcredit(3);
		this.state = taxistate.readytoserve;
		this.cx = x1;
		this.cy = y1;
		this.goalx = x2;
		this.goaly = y2;
	}
	public boolean randomway(Random rand)
	{
		//Requires：一个随机数种子rand ,rand!=null
		//Modifies：dirflag,map,x,y,lastx,lasty
		//Effects：出租车在等待服务下随机选择方向行走的方法，出租车总是会选择流量较少的边，若有多条流量最少的边，则随机选择一条行走
		for(int i = 0;i<4;i++)
		{
			dirflag[i] = false;
		}
		int min = 101;
		//synchronized(map){
			if(y >=1 &&y < 80&& (map.getmappoint(x, y-1) == 2||map.getmappoint(x, y-1) == 3))
			{
				min = map.getline(x,y-1,x,y);
			}
			if(x >= 1 &&x < 80&& (map.getmappoint(x-1, y) == 1||map.getmappoint(x-1, y) == 3))
			{
				if(min > map.getline(x-1,y,x,y))
					min = map.getline(x-1,y,x,y);
			}
			if(map.getmappoint(x, y) == 2||map.getmappoint(x, y) == 3)
			{
				if(min > map.getline(x,y,x,y+1))
					min = map.getline(x,y,x,y+1);
			}
			if(map.getmappoint(x, y) == 1||map.getmappoint(x, y) == 3)
			{
				if(min > map.getline(x,y,x+1,y))
					min = map.getline(x,y,x+1,y);
			}
			int i = 0;
			if(y >=1 &&y < 80&& (map.getmappoint(x, y-1) == 2||map.getmappoint(x, y-1) == 3)&&min == map.getline(x,y-1,x,y))
			{
				i++;
				dirflag[0] = true;
			}
			if(x >= 1 &&x < 80&& (map.getmappoint(x-1, y) == 1||map.getmappoint(x-1, y) == 3)&&min == map.getline(x-1,y,x,y))
			{
				i++;
				dirflag[1] = true;
			}
			if(map.getmappoint(x, y) == 2||map.getmappoint(x, y) == 3&&min == map.getline(x,y,x,y+1))
			{
				i++;
				dirflag[2] = true;
			}
			if(map.getmappoint(x, y) == 1||map.getmappoint(x, y) == 3&&min == map.getline(x,y,x+1,y))
			{
				i++;
				dirflag[3] = true;
			}
			i = rand.nextInt(i) + 1;
			if(y > 0 && (map.getmappoint(x, y-1) == 2||map.getmappoint(x, y-1) == 3)&&dirflag[0] == true)
			{
				i--;
				if(i == 0)
				{
					while(map.getlight(x, y) == 2&&(lastx == x-1||lasty==y-1||lasty==y+1))
					{
						try {
							sleep(1);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					synchronized(map){
					if(map.getmappoint(x,y-1)==2||map.getmappoint(x, y-1) == 3)
					{
						map.setline(x, y-1, x, y);
						lasty = this.y;
						this.y = y-1;
						return true;
					}
					else
						return false;
					}
				}
			}
			if(x > 0 && (map.getmappoint(x-1, y) == 1||map.getmappoint(x-1, y) == 3)&&dirflag[1] == true)
			{
				i--;
				if(i == 0)
				{
					while(map.getlight(x, y) == 1&&(lastx == x+1||lastx==x-1||lasty==y+1))
					{
						try {
							sleep(1);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					synchronized(map){
						if(map.getmappoint(x-1, y) == 1||map.getmappoint(x-1, y) == 3)
						{
							map.setline(x-1, y, x, y);
							lastx = this.x;
							this.x = x-1;
							return true;
						}
						else
							return false;
					}
				}
			}
			if(map.getmappoint(x, y) == 2||map.getmappoint(x, y) == 3&&dirflag[2] == true)
			{
				i--;
				if(i == 0)
				{
					while(map.getlight(x, y) == 2&&(lastx == x+1||lasty==y-1||lasty==y+1))
					{
						try {
							sleep(1);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					synchronized(map){
						if(map.getmappoint(x, y) == 2||map.getmappoint(x, y) == 3)
						{
							map.setline(x, y, x, y+1);
							lasty = this.y;
							this.y = y+1;
							return true;
						}
						else
							return false;
					}
				}
			}
			if(map.getmappoint(x, y) == 1||map.getmappoint(x, y) == 3&&dirflag[3] == true)
			{
				i--;
				if(i == 0)
				{
					while(map.getlight(x, y) == 1&&(lastx == x+1||lastx==x-1||lasty==y-1))
					{
						try {
							sleep(1);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					synchronized(map){
						if(map.getmappoint(x, y) == 1||map.getmappoint(x, y) == 3)
						{
							map.setline(x, y, x+1, y);
							lastx = this.x;
							this.x = x+1;
							return true;
						}
						else
							return false;
					}
				}
			}
		//}
			return false;
	}
	public void addcredit(int i)
	{	//Requires：一个int类型的数，为1或者3
		//Modifies：credit
		//Effects：出租车增加信誉的方法，增加i的信誉
		credit += i;
	}
	public int getcredit()
	{	//Requires：
		//Modifies：
		//Effects：获得出租车信誉的方法，返回出租车当前的信誉
		return credit;
	}
	public int waylength(int x1,int y1,int x2,int y2)
	{	//Requires：四个int类型的数字，都必须满足0<=x1,y1,x2,y2<=79
		//Modifies：waytemp,temp,mapflag
		//Effects：获得两个点(x1,y1)和(x2,y2)的最短距离的函数，返回两点间最短路径的长度
		waytemp.clear();
		road theroad = new road(-1,-1,x1,y1);//-1,-1表示为起始节点
		int x,y,i = 0;
		initflag(0);
		temp.add(theroad);
		x = x1;
		y = y1;
		mapflag[x][y] = 1;
		if(x1 == x2 && y1 == y2)
			return 0;
		while(true)
		{
				//System.out.println(i+temp.get(i).tostring()+temp.size());
			if(y > 0 && (map.getmappoint(x, y-1) == 2||map.getmappoint(x, y-1) == 3)&&mapflag[x][y-1] == 0)
			{
				theroad = new road(x,y,x,y-1);
				temp.add(theroad);
				mapflag[x][y-1] = 1;
				if(x == x2 && y-1 == y2)
				{
					//System.out.println("find success1");
					gettheway(x2,y2);
					temp.clear();
					//System.out.println("find success2");
					//return way;
					return waytemp.size()-2;
				}
			}
			if(x > 0 && (map.getmappoint(x-1, y) == 1||map.getmappoint(x-1, y) == 3)&&mapflag[x-1][y] == 0)
			{
				theroad = new road(x,y,x-1,y);
				temp.add(theroad);
				mapflag[x-1][y] = 1;
				if(x-1 == x2 && y == y2)
				{
					//System.out.println("find success1");
					gettheway(x2,y2);
					temp.clear();
					//System.out.println("find success2");
					//return way;
					return waytemp.size()-2;
				}
			}
			if((map.getmappoint(x, y) == 2||map.getmappoint(x, y) == 3)&&mapflag[x][y+1]==0)
			{
				theroad = new road(x,y,x,y+1);
				temp.add(theroad);
				mapflag[x][y+1] = 1;
				if(x == x2 && y+1 == y2)
				{
					//System.out.println("find success1");
					gettheway(x2,y2);
					temp.clear();
					//System.out.println("find success2");
					//return way;
					return waytemp.size()-2;
				}
			}
			if((map.getmappoint(x, y) == 1||map.getmappoint(x, y) == 3)&&mapflag[x+1][y]==0)
			{
				theroad = new road(x,y,x+1,y);
				temp.add(theroad);
				mapflag[x+1][y] = 1;
				if(x+1 == x2 && y == y2)
				{
					//System.out.println("find success1");
					gettheway(x2,y2);
					temp.clear();
					//System.out.println("find success2");
					//return way;
					return waytemp.size()-2;
				}
			}
			x = temp.get(++i).x2();
			y = temp.get(i).y2();
		}
	}
	public void initflag(int n)
	{	//Requires：一个int数n，只能为0
		//Modifies：mapflag
		//Effects：初始化地图标记mapflag
		for(int i = 0;i < 80;i++)
			for(int j = 0 ;j<80;j++)
			{
				mapflag[i][j] = n;
			}
	}
	public void gettheway(int x,int y)//获得最短路径
	{	//Requires：两个int类型的数字，都必须满足0<=x,y<=79
		//Modifies：waytemp
		//Effects：获得最短路径的方法，(x,y)为终点坐标，根据waylength方法遍历的结果获得最短路径并保存
		road theroad = new road(x,y,-1,-1);//-1,-1表示为终止节点
		waytemp.add(theroad);
		int xt=x,yt=y;
		while(true)
		{
			for(int i = temp.size()-1;i >= 0;i--)
			{
				if(temp.get(i).x2() == xt&&temp.get(i).y2() == yt)
				{
					theroad = new road(temp.get(i).x1(),temp.get(i).y1(),temp.get(i).x2(),temp.get(i).y2());
					waytemp.add(theroad);
					xt = temp.get(i).x1();
					yt = temp.get(i).y1();
					break;
				}
			}
			if(waytemp.get(waytemp.size()-1).x1()==-1)
				break;
		}
	}
	public boolean go(int x2,int y2,Random rand)
	{	//Requires：两个int类型的数字，都必须满足0<=x,y<=79，一个随机数种子rand ,rand!=null
		//Modifies：x,y,dirflag,map,lastx,lasty
		//Effects：出租车收到请求后的寻路算法，出租车总是寻找最短路径行走，若有多条最短路径，则总是寻找当前流量小的边行走，若有多条最短路径且对应的边流量均最小则随机选一条路行走
		int lengthmin = 10000,flowmin = 200;
		if(x == x2&&y == y2)
			;
		else{
			for(int i = 0;i<4;i++)
			{
				dirflag[i] = false;
			}
			if(y > 0 && (map.getmappoint(x, y-1) == 2||map.getmappoint(x, y-1) == 3))
			{
				if(lengthmin > waylength(x,y-1,x2,y2)&&flowmin > map.getline(x,y-1,x,y))
				{
					lengthmin = waylength(x,y-1,x2,y2);
					dirflag[0] = true;
				}
			}
			if(x > 0 && (map.getmappoint(x-1, y) == 1||map.getmappoint(x-1, y) == 3))
			{
				if(lengthmin > waylength(x-1,y,x2,y2)||(lengthmin == waylength(x-1,y,x2,y2)&&flowmin > map.getline(x-1,y,x,y)))
				{
					lengthmin = waylength(x-1,y,x2,y2);
					dirflag[1] = true;
					dirflag[0] = false;
				}
				else if(lengthmin == waylength(x-1,y,x2,y2)&&flowmin == map.getline(x-1,y,x,y))
				{
					dirflag[1] = true;
				}
			}
			if((map.getmappoint(x, y) == 2||map.getmappoint(x, y) == 3))
			{
				if(lengthmin > waylength(x,y+1,x2,y2)||(lengthmin == waylength(x,y+1,x2,y2)&&flowmin > map.getline(x,y,x,y+1)))
				{
					lengthmin = waylength(x,y+1,x2,y2);
					dirflag[2] = true;
					dirflag[1] = false;
					dirflag[0] = false;
				}
				else if(lengthmin == waylength(x,y+1,x2,y2)&&flowmin == map.getline(x,y,x,y+1))
				{
					dirflag[2] = true;
				}
			}
			if((map.getmappoint(x, y) == 1||map.getmappoint(x, y) == 3))
			{
				if(lengthmin > waylength(x+1,y,x2,y2)||(lengthmin == waylength(x+1,y,x2,y2)&&flowmin > map.getline(x,y,x+1,y)))
				{
					lengthmin = waylength(x+1,y,x2,y2);
					dirflag[3] = true;
					dirflag[2] = false;
					dirflag[1] = false;
					dirflag[0] = false;
				}
				else if(lengthmin == waylength(x+1,y,x2,y2)&&flowmin == map.getline(x,y,x+1,y))
				{
					dirflag[3] = true;
				}
			}
		}
		int i = 0;
		for(int j = 0;j<4;j++)
		{
			if(dirflag[j]==true)
				i++;
		}
		i = rand.nextInt(i) + 1;
		if(y > 0 && (map.getmappoint(x, y-1) == 2||map.getmappoint(x, y-1) == 3)&&dirflag[0] == true)
		{
			i--;
			if(i == 0)
			{
				while(map.getlight(x, y) == 2&&(lastx == x-1||lasty==y-1||lasty==y+1))
				{
					try {
						sleep(1);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				synchronized(map){
					if(map.getmappoint(x,y-1)==2||map.getmappoint(x, y-1) == 3)
					{
						map.setline(x, y-1, x, y);
						lasty = this.y;
						this.y = y-1;
						return true;
					}
					else
						return false;
					}
			}
		}
		if(x > 0 && (map.getmappoint(x-1, y) == 1||map.getmappoint(x-1, y) == 3)&&dirflag[1] == true)
		{
			i--;
			if(i == 0)
			{
				while(map.getlight(x, y) == 1&&(lastx == x+1||lastx==x-1||lasty==y+1))
				{
					try {
						sleep(1);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				synchronized(map){
					if(map.getmappoint(x-1, y) == 1||map.getmappoint(x-1, y) == 3)
					{
						map.setline(x-1, y, x, y);
						lastx = this.x;
						this.x = x-1;
						return true;
					}
					else
						return false;
				}
			}
		}
		if(map.getmappoint(x, y) == 2||map.getmappoint(x, y) == 3&&dirflag[2] == true)
		{
			i--;
			if(i == 0)
			{
				while(map.getlight(x, y) == 2&&(lastx == x+1||lasty==y-1||lasty==y+1))
				{
					try {
						sleep(1);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				synchronized(map){
					if(map.getmappoint(x, y) == 2||map.getmappoint(x, y) == 3)
					{
						map.setline(x, y, x, y+1);
						lasty = this.y;
						this.y = y+1;
						return true;
					}
					else
						return false;
				}
			}
		}
		if(map.getmappoint(x, y) == 1||map.getmappoint(x, y) == 3&&dirflag[3] == true)
		{
			i--;
			if(i == 0)
			{
				while(map.getlight(x, y) == 1&&(lastx == x+1||lastx==x-1||lasty==y-1))
				{
					try {
						sleep(1);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				synchronized(map){
					if(map.getmappoint(x, y) == 1||map.getmappoint(x, y) == 3)
					{
						map.setline(x, y, x+1, y);
						lastx = this.x;
						this.x = x+1;
						return true;
					}
					else
						return false;
				}
			}
		}
		return false;
	}
	public taxistate getstate()
	{	//Requires：
		//Modifies：
		//Effects：获得出租车状态的方法，返回出租车当前状态
		return state;
	}
	public int getx()
	{	//Requires：
		//Modifies：
		//Effects：获得出租车当前横坐标的方法，返回出租车当前横坐标
		return x;
	}
	public int gety()
	{	//Requires：
		//Modifies：
		//Effects：获得出租车当前纵坐标的方法，返回出租车当前纵坐标
		return y;
	}
	public void tostring()
	{	//Requires：
		//Modifies：
		//Effects：查看出租车当前状态的方法，打印一系列字符串输出出租车当前状态
		String string;
		long timet;
		timet = (System.currentTimeMillis()-input.time)/100*100;
		string = "当前时刻 为:"+timet*100/100+"ms";
		System.out.println(string);
		string = "出租车"+id+"位置为:("+x+","+y+")";
		System.out.println(string);
		string = "出租车"+id+"状态为:"+this.state;
		System.out.println(string);
		string = "出租车"+id+"信誉为:"+this.credit;
		System.out.println(string);
	}
	public int getid()
	{	//Requires：
		//Modifies：
		//Effects：获得出租车当前编号的方法，返回出租车当前编号
		return this.id;
	}
}
