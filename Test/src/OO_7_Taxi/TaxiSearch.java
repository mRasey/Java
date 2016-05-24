package OO_7_Taxi;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

public class TaxiSearch extends Thread{
	private static final int SERVING=0;
	private static final int PICKING=1;
	private static final int WAITING=2;
	private static final int RESTING=3;
	//private HashMap<Integer,Passenger> passengerList;
	private PassengerQueue passengerList;
	private LinkedList<Taxi> taxis;
	public TaxiSearch(PassengerQueue List,LinkedList<Taxi>taxis)
	{
		this.passengerList=List;
		this.taxis=taxis;
	}
	public void run()
	{
		while(true)
		{
			try{sleep(1);}
			catch(InterruptedException e){}
			int size=passengerList.getSize();
			//LinkedList<Integer> removekeys=new LinkedList<Integer>();
			for(int key=0;key<size;key++)
			{
				Passenger p=passengerList.get(key);
				int x=p.getCallPoint().getX();
				int y=p.getCallPoint().getY();
				for(Taxi t:taxis)
				{
					if((Math.abs(t.getPosition().getX()-x)<=2)&&
					(Math.abs(t.getPosition().getY()-y)<=2)&&
					(t.getMode()==WAITING))
					{
						p.addTaxi(t);
					}
				}
				if(System.currentTimeMillis()-p.getTime()>=3000)
				{
					passengerList.remove(key);
					size--;
					p.getTaxi();
				}
			}
			
		}
	}
}
