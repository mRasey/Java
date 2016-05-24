package elevator2;

public class request {
	private String form;
	private String U_D;
	private String elevator;
	private int floor;
	private long t;
	private statereq state = statereq.unfinish;
	private allot dis = allot.unallot;
	public request(String s1,int i1,String s2,long i2)
	{
		this.form = s1;
		this.floor = i1;
		this.U_D = s2;
		this.elevator = null;
		this.t = i2;
	}
	public request(String s1,String s2,int i1 ,long i2)
	{
		this.form = s1;
		this.elevator = s2;
		this.U_D = null;
		this.floor = i1;
		this.t = i2;
	}
	public String getform()
	{
		return this.form;
	}
	public String getU_D()
	{
		return this.U_D;
	}
	public int getfloor()
	{
		return this.floor;
	}
	public long gett()
	{
		return this.t;
	}
	public int getid()
	{
		int i = elevator.charAt(1)-'1';
		return i;
	}
	public statereq getsta()
	{
		return this.state;
	}
	public allot getallot()
	{
		return dis;
	}
	public void setsta()
	{
		state = statereq.finish;
	}
	public void setallot()
	{
		dis = allot.allot;
	}
	public String toString()
	{
		String s;
		if(this.getform().equals("FR"))
			s = "("+this.getform()+","+this.getfloor()+","+this.getU_D()+")";
		else
			s = "("+this.getform()+","+this.elevator+","+this.getfloor()+")";
		return s;		
	}
	public boolean equals(request req)
	{
		if(this.getform().equals(req.getform()))
		{
			if(this.getform().equals("FR"))
			{
				if(this.getfloor()==req.getfloor()&&this.getU_D().equals(req.getU_D())&&this.gett()==req.gett())
					return true;
			}
			else
			{
				if(this.getfloor()==req.getfloor()&&this.getid()==req.getid()&&this.gett()==req.gett())
					return true;
			}	
		}
		return false;
	}
}
