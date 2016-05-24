package elevator2;

public class Elevator extends Thread {
	private volatile state status = state.SERVING;
	private int floor = 1;
	private int id;
	private int goal = 0;
	private volatile direction dire = direction.IDIE;
	private int amount = 0;
	private queue q = new queue();
	private int r;
	private long time;
	public Elevator(int i)
	{
		id = i;
	}
	private void moveup()
	{
		//System.out.println("ele5");
		while(((System.currentTimeMillis())-time)/3000 < 1);
		//while(((System.currentTimeMillis())-time)/3000 < 1);
		time += 3000;
		floor++;
		amount++;
		//System.out.println("ele");
	}
	private void movedown()
	{
		while(((System.currentTimeMillis())-time)/3000 < 1);
		//while(((System.currentTimeMillis())-time)/3000 < 1);
		time += 3000;
		floor--;
		amount++;
	}
	private void open_off()
	{
		//while((System.currentTimeMillis() - time)/6000 < 1);
		while((System.currentTimeMillis() - time)/6000 < 1);
		//System.out.println("///");
		time += 6000;
	}
	public void addreq(request req)
	{
		q.append(req);
		//System.out.println("add");
	}
	public void run()
	{
		time = scheduler.gettime();
		while(true)
		{
			//System.out.println(status+"1"+q.hasreq());
			while(!(q.hasreq()))
			{
				try {
					sleep(1);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//System.out.println("in");
				status = state.SERVING;
				//System.out.println(status+"3"+q.hasreq());
			}
			//System.out.println(status+"2"+q.hasreq());
				for(int i = 0;i < q.getrear();i++)
				{
					if(q.fetch(i).getsta()==statereq.unfinish)
					{
						status = state.RUNNING;
						goal = q.fetch(i).getfloor();
						r = i;
						if(time<scheduler.gettime() + q.fetch(i).gett()*100)
							time = scheduler.gettime() + q.fetch(i).gett()*100;
						//System.out.println("ele1");
						break;
					}
				}
				for(int i = 0;i < q.getrear();i++)
				{
					if(q.fetch(i).getsta()==statereq.unfinish)
					{
						if(dire == direction.UP&&goal<q.fetch(i).getfloor())
							goal = q.fetch(i).getfloor();
						else if(dire == direction.DOWN&&goal>q.fetch(i).getfloor())
							goal = q.fetch(i).getfloor();
						if(dire == direction.UP&&q.fetch(r).getfloor()>q.fetch(i).getfloor())
							r = i;
						else if(dire == direction.DOWN &&q.fetch(r).getfloor()<q.fetch(i).getfloor())
							r = i;
					}
					//System.out.println("ele2");
				}
			//System.out.println(q.fetch(r).getfloor()+"'''"+goal+"'''"+floor);
			if(q.fetch(r).getfloor()>floor)
			{
				status = state.RUNNING;
				dire = direction.UP;
				moveup();
				//System.out.println("ele4");
			}
			else if(q.fetch(r).getfloor() < floor)
			{
				status = state.RUNNING;
				dire = direction.DOWN;
				movedown();
			}
			else
			{
				for(int i = 0;i < q.getrear();i++)
				{
					if(q.fetch(i).getsta()==statereq.unfinish&&q.fetch(i).getfloor() == floor)
					{
						q.fetch(i).setsta();
						System.out.println(tostring() + "("+q.fetch(i).toString()+",#"+id+")");
					}
				}
				open_off();
				if(goal == floor)
				{
					status = state.SERVING;
					dire = direction.IDIE;
				}
			}
		}
	}
	public long gettime()
	{
		return time;
	}
	public int getamount()
	{
		return amount;
	}
	private String tostring()
	{
		String s1;
		if(dire == direction.UP)
			s1 = "UP";
		else if(dire == direction.DOWN)
			s1 = "DOWN";
		else
			s1 ="IDIE";
		long t = (time-scheduler.gettime())/100*100;
		return "(#"+id+",#"+floor+","+s1+","+amount+","+t+"ms)";
	}
	public direction getdirection()
	{
		return dire;
	}
	public state getstate()
	{
		return status;
	}
	public int getfloor()
	{
		return floor;
	}
	public int getgoal()
	{
		return goal;
	}
	public void setsta()
	{
		status = state.RUNNING;
		for(int i = 0;i < q.getrear();i++)
		{
			if(q.fetch(i).getsta()==statereq.unfinish)
			{
				goal = q.fetch(i).getfloor();
				r = i;
				if(time<scheduler.gettime() + q.fetch(i).gett()*100)
					time = scheduler.gettime() + q.fetch(i).gett()*100;
				//System.out.println("ele1");
				break;
			}
		}
		if(q.fetch(r).getfloor()>floor)
		{
			dire = direction.UP;
		}
		else if(q.fetch(r).getfloor() < floor)
		{
			dire = direction.DOWN;
		}
	}
}
