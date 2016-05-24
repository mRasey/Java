package OO_7_Taxi;

import java.util.LinkedList;

public class PassengerQueue {
	LinkedList<Passenger> passenger_list=new LinkedList<Passenger>();
	public PassengerQueue()
	{
		
	}
	public synchronized void add(Passenger p)
	{
		this.passenger_list.add(p);
	}
	public synchronized int getSize()
	{
		return this.passenger_list.size();
	}
	public synchronized Passenger get(int n)
	{
		return this.passenger_list.get(n);
	}
	public synchronized Passenger remove(int n)
	{
		return this.passenger_list.remove(n);
	}
	public synchronized void clear()
	{
		this.passenger_list.clear();
	}
	public synchronized boolean isEmpty()
	{
		return this.passenger_list.isEmpty();
	}

}
