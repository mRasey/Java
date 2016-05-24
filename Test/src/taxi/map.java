package taxi;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class map extends Thread{//地图类
	private int[][][] map = new int[80][80][5];//0为地图，1为flag，2为交叉信息记录，3为红绿灯记录，红绿灯一个路口只有上下的红绿灯，左右的灯与其相反，0为无灯，1为绿灯，2为红灯,4为特殊出租车地图
	private int[][] right = new int[79][80];
	private int[][] down = new int[80][79];
	private ArrayList<road> temp = new ArrayList<road>();
	private ArrayList<road> way = new ArrayList<road>();
	public map(String s)
	{
		//Requires：一个不为空的字符串代表地图文件的绝对位置
		//Modifies：map,right,down
		//Effects：根据输入的字符串打开地图文件并保存其到数组map中，并初始化另外两个数组
		//若输入文件无法处理则抛出异常，若为正确的地图文件则将其中的数据依次保存到数组map中并初始化另外两个数组
		//若地图不连通，则程序结束
		File f = new File(s);
		if(f.exists()&&f.isFile())
		{
			int j = 0;
			try {
				FileReader fileReader = new FileReader(f);
				BufferedReader bufferedReader = new BufferedReader(fileReader);
				try {
					while(j<80)
					{	
						String line = bufferedReader.readLine();
						if(line.length()>=80)
						{
							line = line.replace(" ", "");
							for(int i = 0;i < 80 ;i++)
							{
								if(line.charAt(i)>='0'&&line.charAt(i)<='3')
									{
									map[i][j][4] = line.charAt(i)-'0';
									map[i][j][0] = line.charAt(i)-'0';
									}
								else
								{
									System.out.println("文件内容有误");
									System.exit(0);
								}
							}
							j++;
							//System.out.println(line+j);
						}
					}
					fileReader.close();
					bufferedReader.close();
					for(int i = 0;i<80;i++)
						for(int k = 0;k<79;k++)
						{
							right[k][i] = 0;
							down[i][k] = 0;
						}
					if(checkmap()==false)
					{
						System.out.println("地图不正确");
						System.exit(0);
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.out.println("error1");
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("error2");
			}
		}
		else
		{
			System.out.println("error3");
		}
	}
	public boolean reqOK()
	{//Requires：
		//Modifies：
		//Effects：检测属性是否符合要求，符合返回true，否则返回false
		for(int i =0;i<80;i++)
			for(int j = 0;j>80;j++)
				if(map[i][j][0]<0||map[i][j][0]>3||map[i][j][1]!=0||map[i][j][1]!=1||map[i][j][2]!=0||map[i][j][2]!=1||
				map[i][j][3]<0||map[i][j][3]>2||map[i][j][4]<0||map[i][j][4]>3)
					return false;
		if(temp!=null)return false;
		if(way !=null)return false;
		for(int i =0;i<80;i++)
			for(int j = 0;j>80;j++)
				if(right[i][j]<0||right[i][j]>100)
					return false;
		for(int i =0;i<80;i++)
			for(int j = 0;j>80;j++)
				if(down[i][j]<0||down[i][j]>100)
					return false;
		return true;		
	}
	public void mapjiaocha(String s)
	{
		//Requires：一个不为空的字符串代表文件的绝对位置
		//Modifies：map
		//Effects：根据输入的字符串打开地图交叉文件并保存其到数组map中
		//若输入文件无法处理则抛出异常，若为正确的文件则将其中的数据依次保存到数组map中并初始化红绿灯
		File f = new File(s);
		if(f.exists()&&f.isFile())
		{
			int j = 0;
			try {
				FileReader fileReader = new FileReader(f);
				BufferedReader bufferedReader = new BufferedReader(fileReader);
				try {
					while(j<80)
					{	
						String line = bufferedReader.readLine();
						//System.out.println(line);
						if(line.length()>=80)
						{
							line = line.replace(" ", "");
							for(int i = 0;i < 80 ;i++)
							{
								if(line.charAt(i)=='1'||line.charAt(i)=='0')
									map[i][j][2] = line.charAt(i)-'0';
								else
								{
									System.out.println("文件内容有误");
									System.exit(0);
								}
							}
							j++;
						}
					}
					fileReader.close();
					bufferedReader.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.out.println("error4");
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("error5");
			}
		}
		else
		{
			System.out.println("error6");
		}
		initlight();
	}
	public void initlight()
	{
		//Requires：
		//Modifies：map
		//Effects：根据地图文件和交叉文件在符合条件的路口初始化红绿灯
		int k = 0;
		for(int i = 0;i<80;i++)
			for(int j = 0;j<80;j++)
			{
				k = 0;
				if(map[i][j][0]==1||map[i][j][0] == 2)
					k++;
				else if(map[i][j][0] == 3)
					k = k+2;
				if(i>0&&(map[i-1][j][0] == 1||map[i-1][j][0]==3))
					k++;
				if(j>0&&(map[i][j-1][0]==2||map[i][j-1][0]==3))
					k++;
				if((k == 3||k==4)&&map[i][j][2]==1)
					map[i][j][3] = 1;
				else
					map[i][j][3] = 0;
			}
	}
	public void run()
	{
		while(true)
		{
			try {
				sleep(50);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			synchronized(this){
				for(int i = 0;i<80;i++)
					for(int k = 0;k<79;k++)
					{
						right[k][i] = 0;
						down[i][k] = 0;
					}
			}
		}
	}
	public ArrayList<road> findtheway(int x1,int y1,int x2,int y2)//遍历并寻找最短路径
	{
		//Requires：四个int类型的数字，都必须满足0<=x1,y1,x2,y2<=79
		//Modifies：way,temp,map
		//Effects：获得两个点(x1,y1)和(x2,y2)的最短路径的函数，返回两点间的最短路径
		temp.clear();
		if(x1 == x2&&y1 == y2)
		{
			//System.out.println("2222");
			way.clear();
			road theroad = new road(x1,y1,x2,y2);
			way.add(theroad);
			//System.out.println(way.size());
			return way;
		}
		else
		{
			///System.out.println("3333");
			way.clear();
			road theroad = new road(-1,-1,x1,y1);//-1,-1表示为起始节点
			int x,y,i = 0;
			initflag(0);
			temp.add(theroad);
			x = x1;
			y = y1;
			map[x][y][1] = 1;
			while(true)
			{
					//System.out.println(i+temp.get(i).tostring()+temp.size());
				
				if(y > 0 && (map[x][y-1][0] == 2||map[x][y-1][0] == 3)&&map[x][y-1][1] == 0)
				{
					theroad = new road(x,y,x,y-1);
					temp.add(theroad);
					map[x][y-1][1] = 1;
					if(x == x2 && y-1 == y2)
					{
						//System.out.println("find success1");
						gettheway(x2,y2);
						temp.clear();
						//System.out.println("find success2");
						return way;
					}
				}
				if(x > 0 && (map[x-1][y][0] == 1||map[x-1][y][0] == 3)&&map[x-1][y][1] == 0)
				{
					theroad = new road(x,y,x-1,y);
					temp.add(theroad);
					map[x-1][y][1] = 1;
					if(x-1 == x2 && y == y2)
					{
						//System.out.println("find success1");
						gettheway(x2,y2);
						temp.clear();
						//System.out.println("find success2");
						return way;
					}
				}
				if((map[x][y][0] == 2||map[x][y][0] == 3)&&map[x][y+1][1]==0)
				{
					theroad = new road(x,y,x,y+1);
					temp.add(theroad);
					map[x][y+1][1] = 1;
					if(x == x2 && y+1 == y2)
					{
						//System.out.println("find success1");
						gettheway(x2,y2);
						temp.clear();
						//System.out.println("find success2");
						return way;
					}
				}
				if((map[x][y][0] == 1||map[x][y][0] == 3)&&map[x+1][y][1]==0)
				{
					theroad = new road(x,y,x+1,y);
					temp.add(theroad);
					map[x+1][y][1] = 1;
					if(x+1 == x2 && y == y2)
					{
						//System.out.println("find success1");
						gettheway(x2,y2);
						temp.clear();
						//System.out.println("find success2");
						return way;
					}
				}
				x = temp.get(++i).x2();
				y = temp.get(i).y2();
				//System.out.println(x+",,,"+y+",,,"+i+","+x2+","+y2+","+temp.size());
			}
		}
	}
	public void initflag(int n)
	{
		//Requires：int类型的数n，只能为0
		//Modifies：map
		//Effects：初始化地图标记的flag
		for(int i = 0;i < 80;i++)
			for(int j = 0 ;j<80;j++)
			{
				map[i][j][1] = n;
			}
	}
	public void gettheway(int x,int y)//获得最短路径
	{
		//Requires：两个int类型的数字，都必须满足0<=x,y<=79
		//Modifies：way
		//Effects：获得最短路径的方法，(x,y)为终点坐标，根据findtheway遍历的结果获得最短路径并保存
		road theroad = new road(x,y,-1,-1);//-1,-1表示为终止节点
		way.add(theroad);
		int xt=x,yt=y;
		while(true)
		{
			for(int i = temp.size()-1;i >= 0;i--)
			{
				if(temp.get(i).x2() == xt&&temp.get(i).y2() == yt)
				{
					theroad = new road(temp.get(i).x1(),temp.get(i).y1(),temp.get(i).x2(),temp.get(i).y2());
					way.add(theroad);
					xt = temp.get(i).x1();
					yt = temp.get(i).y1();
					break;
				}
			}
			if(way.get(way.size()-1).x1()==-1)
				break;
		}
	}
	public int getmappoint(int x,int y)
	{
		//Requires：两个int类型的数字，都必须满足0<=x,y<=79
		//Modifies：
		//Effects：得到地图中点的信息的方法，返回地图中一个点的信息，该数字仅为0,1,2,3中的一个
		return map[x][y][0];
	}
	public int getmappointnew(int x,int y)
	{
		//Requires：两个int类型的数字，都必须满足0<=x,y<=79
		//Modifies：
		//Effects：得到初始地图中点的信息的方法，返回初始地图中一个点的信息，该数字仅为0,1,2,3中的一个
		return map[x][y][4];
	}
	public int waylength(ArrayList<road> way)
	{	//Requires：一条路径way，且way!=null
		//Modifies：
		//Effects：获得该路径长度的方法，返回该路径的长度
		return way.size()-2;//减去起点和终点
	}
	public void setline(int x1,int y1,int x2,int y2)
	{
		//Requires：四个int类型的数字，都必须满足0<=x1,y1,x2,y2<=79，且x1与x2,y1与y2 间有且仅有一对不同，不同的两个数字之间相差1
		//Modifies：
		//Effects：增加边的流量的方法
		if(x1!=x2)
		{
			right[min(x1,x2)][y1] += 1;
		}
		else
		{
			down[x1][min(y1,y2)] +=1;
		}
	}
	public int getline(int x1,int y1,int x2,int y2)
	{
		//Requires：四个int类型的数字，都必须满足0<=x1,y1,x2,y2<=79，且x1与x2,y1与y2 间有且仅有一对不同，不同的两个数字之间相差1
		//Modifies：
		//Effects：获得边的流量的方法
		if(x1!=x2)
		{
			//System.out.println("("+x1+","+y1+","+x2+","+y2+")");
			return right[min(x1,x2)][y1];
		}
		else
		{
			//System.out.println("("+x1+","+y1+","+x2+","+y2+")");
			return down[x1][min(y1,y2)];
		}
	}
	private int min(int x,int y)
	{
		//Requires：两个int类型的数字
		//Modifies：
		//Effects：返回两个数字之间的较小值
		if(x<=y)
			return x;
		else
			return y;
	}
	public boolean checkmap(){
		//Requires：
		//Modifies：
		//Effects：检查地图是否为联通图的方法，若为联通图则返回true,否则返回false
		road theroad = new road(-1,-1,0,0);//-1,-1表示为起始节点
		int x,y,i = 0;
		int[][] mapflag = new int[80][80];
		ArrayList<road> temp = new ArrayList<road>();
		temp.clear();
		for(int j = 0;j<80;j++)
			for(int k = 0;k<80;k++)
				mapflag[j][k] = 0;
		temp.add(theroad);
		x = 0;
		y = 0;
		mapflag[x][y] = 1;
		while(true)
		{
				//System.out.println(i+temp.get(i).tostring()+temp.size());
			for(int j = 0;j<80;j++)
				if(map[79][j][0] == 1||map[79][j][0] == 3||map[j][79][0]==2||map[j][79][0]==3)
					return false;
			if(y > 0 && (map[x][y-1][0] == 2||map[x][y-1][0] == 3)&&mapflag[x][y-1] == 0)
			{
				theroad = new road(x,y,x,y-1);
				temp.add(theroad);
				mapflag[x][y-1] = 1;
			}
			if(x > 0 && (map[x-1][y][0] == 1||map[x-1][y][0] == 3)&&mapflag[x-1][y] == 0)
			{
				theroad = new road(x,y,x-1,y);
				temp.add(theroad);
				mapflag[x-1][y] = 1;
			}
			if((map[x][y][0] == 2||map[x][y][0] == 3)&&	mapflag[x][y+1]==0)
			{
				theroad = new road(x,y,x,y+1);
				temp.add(theroad);
				mapflag[x][y+1] = 1;
			}
			if((map[x][y][0] == 1||map[x][y][0] == 3)&&mapflag[x+1][y]==0)
			{
				theroad = new road(x,y,x+1,y);
				temp.add(theroad);
				mapflag[x+1][y] = 1;
			}
			if(i+1 == temp.size()&&i+1<6400)
				return false;
			else if(i+1 == 6400)
			{
				return true;
			}
			x = temp.get(++i).x2();
			y = temp.get(i).y2();	
		}
	}
	public void changemap(int x,int y,int s)//地图更改接口
	{	//Requires：三个int类型的数字，必须满足0<=x，y<=79，s为0,1,2,3中的一个
		//Modifies：map
		//Effects：更改地图的方法，将地图中点(x,y)的值改为s,若不满足要求则结束程序，若更改后地图不正确则结束程序
		if(s !=0 &&s != 1&&s!=2&&s!=3)
		{
			System.exit(0);
		}
		if(x<0||x>=80||y<0||y>=80)
		{
			System.exit(0);
		}
		synchronized(this){
			if(s == 0)
			{
				if(map[x][y][0]==0)
				{
					System.out.println("该点处值已为0");
				}
				else
					map[x][y][0] = s;
			}
			else if(s == 1)
			{
				if(map[x][y][4]==0||map[x][y][4]==2)
					System.out.println("初始地图中该点并不存在向右的边，无法更改");
				else if(map[x][y][0]==1)
					System.out.println("该点处值已为1");
				else
					map[x][y][0] = s;
			}
			else if(s ==2)
			{
				if(map[x][y][4]==0||map[x][y][4]==1)
					System.out.println("初始地图中该点并不存在向下的边，无法更改");
				else if(map[x][y][0]==2)
					System.out.println("该点处值已为2");
				else
					map[x][y][0] = s;
			}
			else
			{
				if(map[x][y][4]==0||map[x][y][4]==1||map[x][y][4]==2)
					System.out.println("初始地图中该点并不同时存在向右及向下的边，无法更改");
				else if(map[x][y][0]==3)
					System.out.println("该点处值已为3");
				else
					map[x][y][0] = s;
			}
			if(checkmap()==false)
			{
				System.out.println("地图不正确");
				System.exit(0);
			}
		}
	}
	public int getlight(int i ,int j)
	{	//Requires：两个int类型的数字，必须满足0<=i,j<=79
		//Modifies：
		//Effects：获取红绿灯的方法，返回当前节点的红绿灯信息
		return map[i][j][3];
	}
	public void changelight()
	{	//Requires：
		//Modifies：
		//Effects：更改红绿灯的方法，更改当前节点的红绿灯信息
		for(int i = 0;i<80;i++)
			for(int j = 0 ;j<80;j++)
			{
				if(map[i][j][3] == 1)
					map[i][j][3] = 2;
				else if(map[i][j][3]==2)
					map[i][j][3] = 1;
			}
	}
}
