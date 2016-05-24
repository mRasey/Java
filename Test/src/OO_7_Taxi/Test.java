package OO_7_Taxi;

import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Test extends Thread{
	private static final int SLEEP_INTERVAL=500;
	private static final int SIZE=80;
	//private HashMap<Integer,Passenger> passengerList;
	private PassengerQueue passengerList;
	private Center center;
	public Test(PassengerQueue passengerList,Center center)
	{
		this.passengerList=passengerList;
		this.center=center;
	}
	public void run()
	{
		int passenger_count=0;
		while(true)
		{
			Random rand = new Random();
			try {
				TimeUnit.MILLISECONDS.sleep(rand.nextInt(SLEEP_INTERVAL));
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Point c=new Point(rand.nextInt(SIZE),rand.nextInt(SIZE));
			Point d=new Point(rand.nextInt(SIZE),rand.nextInt(SIZE));
			Passenger p=new Passenger(passenger_count,c,d,center);
			this.passengerList.add(p);
			passenger_count++;
			//System.out.println("乘客"+passenger_count);
			if(passenger_count==100)
				break;
		}
		
	}

}
