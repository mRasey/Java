package taxi;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
class NoIteratorException extends Exception
{
	public NoIteratorException()
	{
		//Requires：
		//Modifies：
		//Effects：构造一个NoIteratorException的对象
	}
}
public class newtaxi extends taxi{
	private int servecount=0;
	private ArrayList<road> way = new ArrayList<road>();
	private ArrayList<ArrayList<road>> allway = new ArrayList<ArrayList<road>>();
	public newtaxi(int x, int y, map m) {
		//Requires：出租车起始位置(x,y),两个int类型的数字，都必须满足0<=x,y<=79，一个类型为map的对象代表地图,map不为null
		//Modifies：x,y,m,id
		//Effects：初始化该出租车，初始化其编号及位置，并使其可以访问地图
		super(x, y, m);
	}
	public boolean repOk()
	{	//Requires：
		//Modifies：
		//Effects：检测属性是否符合要求，符合返回true，否则返回false
		if(!super.reqOK())return false;
		if(servecount<0)return false;
		if(way==null)return false;
		if(allway==null) return false;
		return true;
	}
	public boolean randomway(Random rand)
	{	
		//Requires：一个随机数种子rand
		//Modifies：dirflag,map,x,y,lastx,lasty
		//Effects：出租车在等待服务下在初始地图上随机选择方向行走的方法，出租车总是会选择流量较少的边，若有多条流量最少的边，则随机选择一条行走
		for(int i = 0;i<4;i++)
		{
			dirflag[i] = false;
		}
		int min = 101;
		//synchronized(map){
			if(y >=1 &&y < 80&& (map.getmappointnew(x, y-1) == 2||map.getmappointnew(x, y-1) == 3))
			{
				min = map.getline(x,y-1,x,y);
			}
			if(x >= 1 &&x < 80&& (map.getmappointnew(x-1, y) == 1||map.getmappointnew(x-1, y) == 3))
			{
				if(min > map.getline(x-1,y,x,y))
					min = map.getline(x-1,y,x,y);
			}
			if(map.getmappointnew(x, y) == 2||map.getmappointnew(x, y) == 3)
			{
				if(min > map.getline(x,y,x,y+1))
					min = map.getline(x,y,x,y+1);
			}
			if(map.getmappointnew(x, y) == 1||map.getmappointnew(x, y) == 3)
			{
				if(min > map.getline(x,y,x+1,y))
					min = map.getline(x,y,x+1,y);
			}
			int i = 0;
			if(y >=1 &&y < 80&& (map.getmappointnew(x, y-1) == 2||map.getmappointnew(x, y-1) == 3)&&min == map.getline(x,y-1,x,y))
			{
				i++;
				dirflag[0] = true;
			}
			if(x >= 1 &&x < 80&& (map.getmappointnew(x-1, y) == 1||map.getmappointnew(x-1, y) == 3)&&min == map.getline(x-1,y,x,y))
			{
				i++;
				dirflag[1] = true;
			}
			if(map.getmappointnew(x, y) == 2||map.getmappointnew(x, y) == 3&&min == map.getline(x,y,x,y+1))
			{
				i++;
				dirflag[2] = true;
			}
			if(map.getmappointnew(x, y) == 1||map.getmappointnew(x, y) == 3&&min == map.getline(x,y,x+1,y))
			{
				i++;
				dirflag[3] = true;
			}
			i = rand.nextInt(i) + 1;
			if(y > 0 && (map.getmappointnew(x, y-1) == 2||map.getmappointnew(x, y-1) == 3)&&dirflag[0] == true)
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
					if(map.getmappointnew(x,y-1)==2||map.getmappointnew(x, y-1) == 3)
					{
						if(map.getmappoint(x,y-1)==2||map.getmappoint(x, y-1) == 3)
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
			if(x > 0 && (map.getmappointnew(x-1, y) == 1||map.getmappointnew(x-1, y) == 3)&&dirflag[1] == true)
			{
				i--;
				if(i == 0)
				{
					while(map.getlight(x, y) == 1&&(lastx == x+1||lastx==x-1||lasty==y+1))
					{
						try {
							sleep(1);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					synchronized(map){
						if(map.getmappointnew(x-1, y) == 1||map.getmappointnew(x-1, y) == 3)
						{
							if(map.getmappoint(x-1, y) == 1||map.getmappoint(x-1, y) == 3)
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
			if(map.getmappointnew(x, y) == 2||map.getmappointnew(x, y) == 3&&dirflag[2] == true)
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
						if(map.getmappointnew(x, y) == 2||map.getmappointnew(x, y) == 3)
						{
							if(map.getmappoint(x, y) == 2||map.getmappoint(x, y) == 3)
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
			if(map.getmappointnew(x, y) == 1||map.getmappointnew(x, y) == 3&&dirflag[3] == true)
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
						if(map.getmappointnew(x, y) == 1||map.getmappointnew(x, y) == 3)
						{
							if(map.getmappoint(x, y) == 1||map.getmappoint(x, y) == 3)
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
	public int waylength(int x1,int y1,int x2,int y2)
	{	//Requires：四个int类型的数字，都必须满足0<=x1,y1,x2,y2<=79
		//Modifies：waytemp,temp,mapflag
		//Effects：在初始地图上获得两个点(x1,y1)和(x2,y2)的最短距离的函数，返回两点间最短路径的长度
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
			if(y > 0 && (map.getmappointnew(x, y-1) == 2||map.getmappointnew(x, y-1) == 3)&&mapflag[x][y-1] == 0)
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
			if(x > 0 && (map.getmappointnew(x-1, y) == 1||map.getmappointnew(x-1, y) == 3)&&mapflag[x-1][y] == 0)
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
			if((map.getmappointnew(x, y) == 2||map.getmappointnew(x, y) == 3)&&mapflag[x][y+1]==0)
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
			if((map.getmappointnew(x, y) == 1||map.getmappointnew(x, y) == 3)&&mapflag[x+1][y]==0)
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
	public boolean go(int x2,int y2,Random rand)
	{	//Requires：两个int类型的数字，都必须满足0<=x,y<=79，一个随机数种子rand ,rand!=null
		//Modifies：x,y,dirflag,map,lastx,lasty,way,allway
		//Effects：出租车收到请求后的寻路算法，出租车总是寻找原始地图中的最短路径行走，若有多条最短路径，则总是寻找当前流量小的边行走，若有多条最短路径且对应的边流量均最小则随机选一条路行走
		int lengthmin = 10000,flowmin = 200;
		if(x == x2&&y == y2)
			;
		else{
			for(int i = 0;i<4;i++)
			{
				dirflag[i] = false;
			}
			if(y > 0 && (map.getmappointnew(x, y-1) == 2||map.getmappointnew(x, y-1) == 3))
			{
				if(lengthmin > waylength(x,y-1,x2,y2)&&flowmin > map.getline(x,y-1,x,y))
				{
					lengthmin = waylength(x,y-1,x2,y2);
					dirflag[0] = true;
				}
			}
			if(x > 0 && (map.getmappointnew(x-1, y) == 1||map.getmappointnew(x-1, y) == 3))
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
			if((map.getmappointnew(x, y) == 2||map.getmappointnew(x, y) == 3))
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
			if((map.getmappointnew(x, y) == 1||map.getmappointnew(x, y) == 3))
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
		if(y > 0 && (map.getmappointnew(x, y-1) == 2||map.getmappointnew(x, y-1) == 3)&&dirflag[0] == true)
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
					if(map.getmappointnew(x,y-1)==2||map.getmappointnew(x, y-1) == 3)
					{
						if(map.getmappoint(x,y-1)==2||map.getmappoint(x, y-1) == 3)	
							map.setline(x, y-1, x, y);
						way.add(new road(x,y,x,y-1));
						if(allway.size()>=1)
							allway.remove(allway.size()-1);
						allway.add(way);
						lasty = this.y;
						this.y = y-1;
						return true;
					}
					else
						return false;
					}
			}
		}
		if(x > 0 && (map.getmappointnew(x-1, y) == 1||map.getmappointnew(x-1, y) == 3)&&dirflag[1] == true)
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
					if(map.getmappointnew(x-1, y) == 1||map.getmappointnew(x-1, y) == 3)
					{
						if(map.getmappoint(x-1, y) == 1||map.getmappoint(x-1, y) == 3)	
							map.setline(x-1, y, x, y);
						way.add(new road(x,y,x-1,y));
						if(allway.size()>=1)
							allway.remove(allway.size()-1);
						allway.add(way);
						lastx = this.x;
						this.x = x-1;
						return true;
					}
					else
						return false;
				}
			}
		}
		if(map.getmappointnew(x, y) == 2||map.getmappointnew(x, y) == 3&&dirflag[2] == true)
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
					if(map.getmappointnew(x, y) == 2||map.getmappointnew(x, y) == 3)
					{
						if(map.getmappoint(x, y) == 2||map.getmappoint(x, y) == 3)
							map.setline(x, y, x, y+1);
						way.add(new road(x,y,x,y+1));
						if(allway.size()>=1)
							allway.remove(allway.size()-1);
						allway.add(way);
						lasty = this.y;
						this.y = y+1;
						return true;
					}
					else
						return false;
				}
			}
		}
		if(map.getmappointnew(x, y) == 1||map.getmappointnew(x, y) == 3&&dirflag[3] == true)
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
					if(map.getmappointnew(x, y) == 1||map.getmappointnew(x, y) == 3)
					{
						if(map.getmappoint(x, y) == 1||map.getmappoint(x, y) == 3)
							map.setline(x, y, x+1, y);
						way.add(new road(x,y,x+1,y));
						if(allway.size()>=1)
							allway.remove(allway.size()-1);
						allway.add(way);
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
	public void setstate(int x1,int y1,int x2,int y2)
	{	//Requires：四个int类型的数字，都必须满足0<=x1,y1,x2,y2<=79
		//Modifies：cx,cy,goalx,goaly,goal,credit,state,servecount,way,allway
		//Effects：给出租车请求并使其改变出租车状态的方法，(x1,y1)为乘客当前位置坐标,(x2,y2)为乘客目标位置坐标
		super.setstate(x1, y1, x2, y2);
		this.servecount++;
		way = new ArrayList<road>();
		allway.add(way);
	}
	public int getservecount()
	{	//Requires：
		//Modifies：
		//Effects：返回可追踪出租车的服务次数的方法，返回可追踪出租车的服务次数
		return servecount;
	}
	private static class serveway implements Iterator{
		private ArrayList<road> serveway;
        private int index = -1;
        public serveway(ArrayList<road> way){
        	//Requires：一条路径way!=null
    		//Modifies：serveway
    		//Effects：构造方法，根据传入的路径way初始化serveway;
        	if(way==null)
        	{
        			System.out.println("serveway custruct error");
        			System.exit(0);
        	}
        	else
            	this.serveway = way;
        }

        public boolean hasNext() {
        	//Requires：
    		//Modifies：
    		//Effects：返回该路径当前节点后面是否有节点，若有则返回true，否则返回false
            return index < serveway.size()-1;
        }
        public void remove() {
        	//Requires：
    		//Modifies：
    		//Effects：删除该路径的当前节点
            serveway.remove(index);
        }
        public road previous(){
        	//Requires：
    		//Modifies：
    		//Effects：若该路径当前节点前面有节点，返回该路径当前节点前面的第一个节点，否则提示没有下一个了，并返回road(-2,-2,-2,-2)
            if(hasPrevious())
                return serveway.get(--index);
            else
                System.out.println("没有上一个了");
            return new road(-2,-2,-2,-2);
        }
        public road next(){
        	//Requires：
    		//Modifies：
    		//Effects：若该路径当前节点后面有节点，返回该路径当前节点后面的第一个节点，否则提示没有下一个了，并返回road(-2,-2,-2,-2)
            if(hasNext())
                return serveway.get(++index);
            else
                System.out.println("没有下一个了");
            return new road(-2,-2,-2,-2);
        }
        public boolean hasPrevious(){
        	//Requires：
    		//Modifies：
    		//Effects：返回该路径当前节点前面是否有节点，若有则返回true，否则返回false
            return index > 0;
        }
        public boolean repOk(){
        	//Requires：
    		//Modifies：
    		//Effects：检测属性是否符合要求，符合返回true，否则返回false
        	if(serveway==null)return false;
        	if(index<-1)return false;
        	return true;
        }
    }
	public Iterator diedai(int index) throws Exception{
		//Requires：一个int类型的服务次数index，index>0
		//Modifies：
		//Effects：返回该可追踪出租车的第index次服务路径的迭代器
		//若该可追踪出租车没有服务过或输入的index大于其服务次数，则抛出异常
		//否则，返回该出租车的第index次服务路径的迭代器
		if(index>0&&index<=allway.size())
		{
			serveway s = new serveway(allway.get(index-1));
			return s;
		}
		else{
			throw new NoIteratorException();
		}
	}
}
