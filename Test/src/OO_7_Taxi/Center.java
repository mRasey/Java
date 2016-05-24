package OO_7_Taxi;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Center {
	private static boolean map[][][]=new boolean [80][80][4];
	private static String[][] tempmap=new String [80][80];
	public static final int UP=0;
	public static final int RIGHT=1;
	public static final int DOWN=2;
	public static final int LEFT=3; 
	public static final int MAX_TAXI=100;
	private static final int SERVING=0;
	private static final int PICKING=1;
	private static final int WAITING=2;
	private static final int RESTING=3;
	private static int taxi_credit[]=new int [MAX_TAXI];
	private static Point taxi_position[]=new Point[MAX_TAXI];
	private static int taxi_mode[]=new int [MAX_TAXI];
	
	
	public Center()
	{
		for(int i=0;i<80;i++)
		{
			for(int j=0;j<80;j++)
			{
				map[i][j][0]=false;
				map[i][j][1]=false;
				map[i][j][2]=false;
				map[i][j][3]=false;
				tempmap[i][j]=null;
			}
		}
		for(int i=0;i<MAX_TAXI;i++)
		{
			taxi_credit[i]=0;
			taxi_position[i]=null;
			taxi_mode[i]=WAITING;
		}
	}
	public static void main(String[] args) 
	{
		//try {
			Center center=new Center();
			File file = new File("C:\\input\\map.txt");
			if(!file.exists())
			{
				System.out.println("地图文件不存在，程序已退出");
				System.exit(0);
			}	
			BufferedReader reader = null;
			try { reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            int line=0;
            while ((tempString = reader.readLine()) != null) 
            {
            	tempmap[line] = tempString.split("\\s");
            	line++;
            }
            reader.close();
            }catch(Throwable e){
    			System.out.println("溢出");
    			System.exit(0);}
			
            center.setMap();
           
           Point c=new Point(7,0);
           Point d=new Point(8,0);
           int a=center.getLen(c,d);
           
        	   System.out.print(a);
           
            
           /* 
           Taxi taxi[]=new Taxi[MAX_TAXI];
            LinkedList<Taxi> taxiqueue=new LinkedList<Taxi>();
            for(int i=0;i<MAX_TAXI;i++)
            {
            	taxi[i]=new Taxi(i,center);
            	taxi[i].start();
            	taxiqueue.add(taxi[i]);
            }
            PassengerQueue passengerList=new PassengerQueue();
            //HashMap<Integer,Passenger> passengerList=new HashMap<Integer,Passenger>();
            Test test=new Test(passengerList,center);
            test.start();
            TaxiSearch taxisearch=new TaxiSearch(passengerList,taxiqueue);
            taxisearch.start();
            
        /*}
		catch(Throwable e){
			System.out.println("溢出");
			System.exit(0);
		}*/
        
	}
	public void setMap()
	{
		for(int i=0;i<79;i++)
		{
			for(int j=0;j<80;j++)
			{
				if(j!=79)
				{
					if(tempmap[i][j].equals("0"))
					{
						map[i][j+1][LEFT]=false;
						map[i+1][j][UP]=false;
						map[i][j][RIGHT]=false;
						map[i][j][DOWN]=false;
					}
					else if(tempmap[i][j].equals("1"))
					{
						map[i][j+1][LEFT]=true;
						map[i+1][j][UP]=false;
						map[i][j][RIGHT]=true;
						map[i][j][DOWN]=false;
					}
					else if(tempmap[i][j].equals("2"))
					{
						map[i][j+1][LEFT]=false;
						map[i+1][j][UP]=true;
						map[i][j][RIGHT]=false;
						map[i][j][DOWN]=true;
					}
					else if(tempmap[i][j].equals("3"))
					{
						map[i][j+1][LEFT]=true;
						map[i+1][j][UP]=true;
						map[i][j][RIGHT]=true;
						map[i][j][DOWN]=true;
					}
					else
						System.out.println("地图文件存在错误");
				}
				else
				{
					if(tempmap[i][j].equals("0"))
					{
						map[i+1][j][UP]=false;	
						map[i][j][DOWN]=false;
					}
					else if(tempmap[i][j].equals("2"))
					{
						map[i+1][j][UP]=true;
						map[i][j][DOWN]=true;
					}
					else
						System.out.println("地图文件存在错误");
				}
			}
		}
		for(int j=0;j<79;j++)
		{
			if(tempmap[79][j].equals("0"))
			{
				map[79][j+1][LEFT]=false;	
				map[79][j][RIGHT]=false;
			}
			else if(tempmap[79][j].equals("1"))
			{
				map[79][j+1][LEFT]=true;	
				map[79][j][RIGHT]=true;
			}
			else
				System.out.println("地图文件存在错误");
		}
	}

	public LinkedList<Point> getPath(Point start,Point end)
	{
		LinkedList<Point> path=new LinkedList<Point> ();
		LinkedList<Point> queue=new LinkedList<Point>();
		ArrayList<Point> VisitedPoint=new ArrayList<Point>();
		HashMap<Point,Point> map=new HashMap<>();
		boolean flag=false;
		VisitedPoint.add(start);
		queue.add(start);
		while(!queue.isEmpty())
		{
			Point father=queue.poll();
			ArrayList<Point> toBeVisitedPoint=getChildren(father);
			for(Point children:toBeVisitedPoint)
			{
			    if(children.equals(end))
			    {
			    	map.put(children, father);
			    	queue.clear();
			    	end=children;
			    	flag=true;
			    	break;
			    }
				else if(!checkContains(VisitedPoint,children))
				{
					VisitedPoint.add(children);
					map.put(children, father);
					queue.add(children);
				}
			}
		}
		if(flag)
		{
			path.add(end);
			Point father=map.get(end);
			while(!father.equals(start))
			{
				path.addFirst(father);
				father=map.get(father);
			}
			path.addFirst(father);
			return path;
		}
		else
			return null;
	}
	public int getLen(Point start,Point end)
	{
		int count=0;
		LinkedList<Point> queue=new LinkedList<Point>();
		ArrayList<Point> VisitedPoint=new ArrayList<Point>();
		HashMap<Point,Point> map=new HashMap<>();
		boolean flag=false;
		VisitedPoint.add(start);
		queue.add(start);
		while(!queue.isEmpty())
		{
			Point father=queue.poll();
			ArrayList<Point> toBeVisitedPoint=getChildren(father);
			for(Point children:toBeVisitedPoint)
			{
			    if(children.equals(end))
			    {
			    	map.put(children, father);
			    	queue.clear();
			    	end=children;
			    	flag=true;
			    	break;
			    }
				else if(!checkContains(VisitedPoint,children))
				{
					VisitedPoint.add(children);
					map.put(children, father);
					queue.add(children);
				}
			}
		}
		if(flag)
		{
			Point father=map.get(end);
			count++;
			while(!father.equals(start))
			{
				count++;
				father=map.get(father);
			}
			return count;
		}
		else
			return 99999999;
	}
	public ArrayList<Point> getChildren(Point p)
	{
		int x=p.getX();
		int y=p.getY();
		ArrayList<Point> list = new ArrayList<Point>();
		if(map[x][y][UP])
		{
			Point p1=new Point(x-1,y);
			list.add(p1);
		}
		if(map[x][y][RIGHT])
		{
			Point p2=new Point(x,y+1);
			list.add(p2);
		}
		if(map[x][y][DOWN])
		{
			Point p3=new Point(x+1,y);
			list.add(p3);
		}
		if(map[x][y][LEFT])
		{
			Point p4=new Point(x,y-1);
			list.add(p4);
		}
		return list;
	}
	public Point getNext(Point p)
	{
		int x=p.getX();
		int y=p.getY();
		Random rand=new Random ();
		int position=rand.nextInt(4);
		int n=position;
		while(!map[x][y][position])
		{
			if(position==(n-1)%4)
				break;
			position=(position+1)%4;
		}
		if(position==UP)
		{
			Point point=new Point(x-1,y);
			return point;
		}
		else if(position==RIGHT)
		{
			Point point=new Point(x,y+1);
			return point;
		}
		else if(position==DOWN)
		{
			Point point=new Point(x+1,y);
			return point;
		}
		else if(position==LEFT)
		{
			Point point=new Point(x,y-1);
			return point;
		}
		else
		{
			System.out.println("找不到下一个位置");
			return null;
		}
	}
	public void updatePosition(int no,Point p)
	{
		taxi_position[no]=p;
	}
	public void updateCredit(int no,int credit)
	{
		taxi_credit[no]=credit;
	}
	public void updateMode(int no,int mode)
	{
		taxi_mode[no]=mode;
	}
	public boolean checkContains(ArrayList<Point> list,Point p)
	{
		for(Point key:list)
		{
			if(key.equals(p))
				return true;
		}
		return false;
	}
	public int getCredit(int no)
	{
		return taxi_credit[no];
	}
	public int getMode(int no)
	{
		return taxi_mode[no];
	}
	public Point getPosition(int no)
	{
		return taxi_position[no];
	}
}
