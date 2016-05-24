
public class document {
	private String path;
	private long time;
	private long size;
	private String name;
	private boolean isfile;
	private boolean isnew;
	private int count;
	public document(String path,long time,long size ,String name,boolean isfile,int count)
	{
		this.path = path;
		this.time = time;
		this.size = size;
		this.name = name;
		this.isfile = isfile;
		this.isnew = false;
		this.count = count;
	}
	public String getpath()
	{
		return path;
	}
	public long gettime()
	{
		return time;
	}
	public long getsize()
	{
		return size;
	}
	public String getname()
	{
		return name;
	}
	public boolean isfile()
	{
		return isfile;
	}
	public boolean getisnew()
	{
		return isnew;
	}
	public void setisnew(boolean bool)
	{
		isnew = bool;
	}
	public String tostring()
	{
		String s;
		s = this.getpath()+"***"+this.getname()+"***"+this.getsize()+"***"+this.gettime();
		return s;
	}
	public int getcount()
	{
		return count;
	}
	public void setcount(int count)
	{
		this.count = count;
	}
}
