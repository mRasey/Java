package OO_7_Taxi;

import java.util.LinkedList;
import java.util.Random;

public class Taxi extends Thread{
	private Point position;
	private Point start;
	private Point end;
	private int no;
	private int mode;
	private int credit;
	private Center center;
	private LinkedList<Point> picking_path;
	private LinkedList<Point> serving_path;
	private static final int SIZE=80;
	private static final int SERVING=0;
	private static final int PICKING=1;
	private static final int WAITING=2;
	private static final int RESTING=3;
	private int waiting_count;
	public Taxi (int number,Center center)
	{
		this.no=number;
		this.mode=WAITING;
		this.credit=0;
		this.center=center;
		Random rand=new Random ();
		position=new Point(rand.nextInt(SIZE),rand.nextInt(SIZE));
	}
	public void run()
	{
		while(true)
		{
			try{sleep(100);}
			catch(InterruptedException e){}
			if(mode==SERVING)
			{
				if(!serving_path.isEmpty())
				{
					this.position=serving_path.removeFirst();
					update();
					if(this.position.equals(end))
					{
						this.mode=RESTING;
						update();
						try{sleep(1000);}
						catch(InterruptedException e){}
						System.out.println(this.getNum()+"号出租车到达目的地"+this.end);
						this.mode=WAITING;
						this.credit+=3;
						update();
					}
				}
			}
			else if(mode==PICKING)
			{
				if(!picking_path.isEmpty())
				{
					this.position=picking_path.removeFirst();
					update();
					if(this.position.equals(start))
					{
						this.mode=RESTING;
						update();
						try{sleep(1000);}
						catch(InterruptedException e){}
						this.mode=SERVING;
						update();
					}
				}
					
			}
			else if(mode==WAITING)
			{
				this.position=center.getNext(position);
				update();
				waiting_count++;
				if(waiting_count==20)
				{
					try{
						this.mode=RESTING;
						update();
						sleep(1000);
						waiting_count=0;
						this.mode=WAITING;
						update();
					}
					catch(InterruptedException e){}
				}
			}
		}
	}
	public void update()
	{
		center.updateCredit(no, credit);
		center.updateMode(no, mode);
		center.updatePosition(no, position);
	}
	public Point getPosition()
	{
		return this.position;
	}
	public int getNum()
	{
		return this.no;
	}
	public int getCredit()
	{
		return this.credit;
	}
	public int getMode()
	{
		return this.mode;
	}
	public void setPickingPath(LinkedList<Point> p)
	{
		this.picking_path=p;
	}
	public void setServingPath(LinkedList<Point> p)
	{
		this.serving_path=p;
	}
	public void setMode(int mode)
	{
		this.mode=mode;
	}
	public void setStart(Point p)
	{
		this.start=p;
	}
	public void setEnd(Point p)
	{
		this.end=p;
	}
	public void addCredit(int c)
	{
		this.credit+=c;
	}
}
