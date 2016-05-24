package OO_7_Taxi;

public class Point {
	private int x;
	private int y;
	public Point(int x,int y)
	{
		this.x=x;
		this.y=y;
	}
	public String toString()
	{
		String s=new String();
		s=s+"("+this.x+","+this.y+")";
		return s;
	}
	public boolean equals(Point a)
	{
		if (a.getX()==this.x&&a.getY()==this.y)
			return true;
		else
			return false;
	}
	public int getX()
	{
		return this.x;
	}
	public int getY()
	{
		return this.y;
	}

}
