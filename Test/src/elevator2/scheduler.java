package elevator2;

public class scheduler extends Thread {
	private static long zerotime;
	private queue que;
	private Elevator[] ele = new Elevator[3];
	private boolean[] judge = new boolean[3];
	public scheduler(queue q)
	{
		que = q;
	}
	public static long gettime()
	{
		return zerotime;
	}
	public static void settime(long t)
	{
		zerotime = t;
	}
	public void run()
	{
	    ele[0] = new Elevator(1);
		ele[1] = new Elevator(2);
	    ele[2] = new Elevator(3);
	    judge[0] = false;
	    judge[1] = false;
	    judge[2] = false;
		ele[0].start();
        ele[1].start();
        ele[2].start();
		while(true)
		{
			//System.out.println("scheduler is running"+que.getrear());
			try {
				sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			for(int i = 0;i<que.getrear();i++)
			{
				if(/*que.fetch(i)!= null &&*/ que.fetch(i).getallot() == allot.unallot)
				{
					for(int j = 0;j < 3;j++)
						{
						judge[j] = judgetake(que.fetch(i),ele[j],j);
						//System.out.println(judge[j]+"'''"+j);
						}
					if(que.fetch(i).getform().equals("FR"))
						judgeele(judge,que.fetch(i));
					else
					{
						if(judge[que.fetch(i).getid()] == true)
							eleaddreq(que.fetch(i).getid(),que.fetch(i));
						else if(ele[que.fetch(i).getid()].getstate() == state.SERVING)
							{
							//judgeER(i);
							eleaddreq(que.fetch(i).getid(),que.fetch(i));
							}
					}
				}
			}
		}
	}
	public int seek(int i ,int j)
	{
		if(ele[i].getamount()<=ele[j].getamount())
			return i;
		else 
			return j;
	}
	public int seek()
	{
		int i = 0;
		if(ele[0].getamount()<=ele[1].getamount()&&ele[0].getamount()<=ele[2].getamount())
			i = 0;
		else if(ele[1].getamount()<=ele[0].getamount()&&ele[1].getamount()<=ele[2].getamount())
			i = 1;
		else if(ele[2].getamount()<=ele[0].getamount()&&ele[2].getamount()<=ele[1].getamount())
			i = 2;
		return i;
	}
	public boolean judgetake(request req,Elevator ele,int id)
	{
		if(ele.getstate()==state.SERVING)
			return false;
		else
		{
			if(ele.getdirection()==direction.UP)
			{
				if(req.getform().equals("FR"))
				{
					if(req.getU_D().equals("UP"))
					{
						if(req.getfloor()<=ele.getgoal()&&req.getfloor()>ele.getfloor()&&(req.gett()+zerotime)<ele.gettime())
							{
							//System.out.println("add1");
							return true;
							}
					}
				}
				else if (req.getform().equals("ER"))
				{
					if(req.getfloor()>ele.getfloor()&&(req.gett()+zerotime)<ele.gettime()&&req.getid() == id)
						return true;
				}
			}
			else if(ele.getdirection()==direction.DOWN)
			{
				if(req.getform().equals("FR"))
				{
					if(req.getU_D().equals("DOWN"))
					{
						if(req.getfloor()>=ele.getgoal()&&req.getfloor()<ele.getfloor()&&(req.gett()+zerotime)<ele.gettime())
							{
							//System.out.println("add2");
							return true;
							}
					}
				}
				else if (req.getform().equals("ER"))
				{
					if(req.getfloor()<ele.getfloor()&&(req.gett()+zerotime)<ele.gettime()&&req.getid() == id)
						return true;
				}
			}
		}
		return false;
	}
	public void judgeserve(request req)
	{
		if(ele[0].getstate() == state.SERVING&&ele[1].getstate() == state.SERVING&&ele[2].getstate() == state.SERVING)
			{
			//judgeFR(req);
			eleaddreq(seek(),req);
			}
		else if(ele[0].getstate() == state.RUNNING&&ele[1].getstate() == state.SERVING&&ele[2].getstate() == state.SERVING)
			{
			//judgeFR(1,2,req);
			eleaddreq(seek(1,2),req);
			}
		else if(ele[0].getstate() == state.SERVING&&ele[1].getstate() == state.RUNNING&&ele[2].getstate() == state.SERVING)
			{
			//judgeFR(0,2,req);
			eleaddreq(seek(0,2),req);			
			}
		else if(ele[0].getstate() == state.SERVING&&ele[1].getstate() == state.SERVING&&ele[2].getstate() == state.RUNNING)
			{
			//judgeFR(0,1,req);
			eleaddreq(seek(0,1),req);
			}
		else if(ele[0].getstate() == state.RUNNING&&ele[1].getstate() == state.RUNNING&&ele[2].getstate() == state.SERVING)
			{
			//judgeFR(2,req);
			eleaddreq(2,req);
			}
		else if(ele[0].getstate() == state.RUNNING&&ele[1].getstate() == state.SERVING&&ele[2].getstate() == state.RUNNING)
			{
			//judgeFR(1,req);
			eleaddreq(1,req);			
			}
		else if(ele[0].getstate() == state.SERVING&&ele[1].getstate() == state.RUNNING&&ele[2].getstate() == state.RUNNING)
			{
			//judgeFR(0,req);
			eleaddreq(0,req);
			}
	}
	public void judgeele(boolean[] judge,request req)
	{
		if(judge[0] == false&&judge[1] == false&&judge[2] == false)
			judgeserve(req);
		else if(judge[0] == true&&judge[1] == false&&judge[2] == false)
			eleaddreq(0,req);
		else if(judge[0] == false&&judge[1] == true&&judge[2] == false)
			eleaddreq(1,req);
		else if(judge[0] == false&&judge[1] == false&&judge[2] == true)
			eleaddreq(2,req);
		else if(judge[0] == true&&judge[1] == true&&judge[2] == false)
			eleaddreq(seek(0,1),req);
		else if(judge[0] == true&&judge[1] == false&&judge[2] == true)
			eleaddreq(seek(0,2),req);
		else if(judge[0] == false&&judge[1] == true&&judge[2] == true)
			eleaddreq(seek(1,2),req);
		else
			eleaddreq(seek(),req);
			
	}
	private void eleaddreq(int i,request req)
	{
		ele[i].addreq(req);
		ele[i].setsta();
		req.setallot();
		try {
			sleep(2);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//System.out.println(i+req.getform()+req.getU_D()+ele[i].getdirection()+ele[i].getstate());
	}
	private void judgeER(int i)
	{
		ele[que.fetch(i).getid()].addreq(que.fetch(i));
		que.fetch(i).setallot();
		if(ele[que.fetch(i).getid()].getfloor() == que.fetch(i).getfloor())
		for(int j = 0;j<que.getrear();j++)
		{
			if(que.fetch(j).getallot() == allot.unallot&&que.fetch(j).equals(que.fetch(i)))
			{
				ele[que.fetch(j).getid()].addreq(que.fetch(j));
				que.fetch(j).setallot();
			}
		}
		ele[que.fetch(i).getid()].setsta();
	}
	private void judgeFR(int i,int j,request req)
	{
		ele[seek(i,j)].addreq(req);
		req.setallot();
		if(ele[seek(i,j)].getfloor()==req.getfloor())
			for(int k = 0;k<que.getrear();k++)
			{
				if(que.fetch(k).getallot() == allot.unallot)
				{
					//System.out.print(que.fetch(k).equals(req)+"'''");
					//System.out.println(k);
					if(que.fetch(k).equals(req))
					{
						ele[seek(i,j)].addreq(que.fetch(k));
						que.fetch(k).setallot();
					}
				}
			}
		ele[seek(i,j)].setsta();
		try {
			sleep(2);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void judgeFR(int i,request req)
	{
		ele[i].addreq(req);
		req.setallot();
		if(ele[i].getfloor()==req.getfloor())
			for(int k = 0;k<que.getrear();k++)
			{
				if(que.fetch(k).getallot() == allot.unallot)
				{
					//System.out.print(que.fetch(k).equals(req)+"'''");
					//System.out.println(k);
					if(que.fetch(k).equals(req))
					{
						ele[i].addreq(que.fetch(k));
						que.fetch(k).setallot();
					}
				}
			}
		ele[i].setsta();
		try {
			sleep(2);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void judgeFR(request req)
	{
		ele[seek()].addreq(req);
		req.setallot();
		if(ele[seek()].getfloor()==req.getfloor())
			for(int k = 0;k<que.getrear();k++)
			{
				if(que.fetch(k).getallot() == allot.unallot)
				{
					//System.out.print(que.fetch(k).equals(req)+"'''");
					//System.out.println(k);
					if(que.fetch(k).equals(req))
					{
						ele[seek()].addreq(que.fetch(k));
						que.fetch(k).setallot();
					}
				}
			}
		ele[seek()].setsta();
		try {
			sleep(2);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
