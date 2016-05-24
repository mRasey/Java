package OO_7_Taxi;

import java.util.HashMap;
import java.util.LinkedList;

public class Passenger {
	private static final int SERVING=0;
	private static final int PICKING=1;
	private static final int WAITING=2;
	private static final int RESTING=3;
	private Point call_point;
	private Point des_point;
	private Center center;
	private int no;
	private long time;
	private HashMap<Integer,Taxi> availabletaxi=new HashMap<Integer,Taxi>();
	
	public Passenger(int no,Point c,Point d,Center center)
	{
		this.no=no;
		this.call_point=c;
		this.des_point=d;
		this.center=center;
		this.time=System.currentTimeMillis();
	}
	public void addTaxi(Taxi t)
	{
		if(!availabletaxi.containsKey(t.getNum()))
		{
			availabletaxi.put(t.getNum(), t);
			t.addCredit(1);
		}
	}
	public Taxi getTaxi()//处理为空的情况
	{
		int maxCredit=0;
		int minDistance=-1;
		int distance=0;
		LinkedList<Taxi> creditList=new LinkedList<Taxi>();
		Taxi choosed=null;
		for(int no:availabletaxi.keySet())
		{
			Taxi t=availabletaxi.get(no);
			if(t.getMode()==WAITING)
			{
				if(t.getCredit()>maxCredit)
				{
					maxCredit=t.getCredit();
					creditList.clear();
					creditList.add(t);
				}
				else if(t.getCredit()==maxCredit)
				{
					creditList.add(t);
				}
			}
		}
		if(creditList.isEmpty())
		{
			System.out.println("乘客"+call_point+"无车可坐");
			return null;
		}
		for(Taxi t:creditList)
		{
			distance=Math.abs(t.getPosition().getX()-call_point.getX())+
							Math.abs(t.getPosition().getY()-call_point.getY());
			if(minDistance==-1)
			{
				minDistance=distance;
				choosed=t;
			}
			if(distance<minDistance)
			{
				minDistance=distance;
				choosed=t;
			}
		}
		choosed.setPickingPath(center.getPath(choosed.getPosition(),call_point));
		choosed.setServingPath(center.getPath(call_point,des_point));
		choosed.setMode(PICKING);
		choosed.setStart(call_point);
		choosed.setEnd(des_point);
		System.out.println("乘客"+call_point+"被"+choosed.getNum()+"号出租接载");
		return choosed;
	}

	public Point getCallPoint()
	{
		return this.call_point;
	}
	public Point getDesPoint()
	{
		return this.des_point;
	}
	public long getTime()
	{
		return this.time;
	}
}
