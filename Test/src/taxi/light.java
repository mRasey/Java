package taxi;

public class light extends Thread{
	private map m;
	public light(map map)
	{	//Requires：map不为null
		//Modifies：m
		//Effects：初始化一个红绿灯更改类，使其可以访问地图
		if(map == null)
		{
			System.out.println("light custruct error");
			System.exit(0);
		}
		else
			this.m = map;
	}
	public boolean reqOK()
	{//Requires：
		//Modifies：
		//Effects：检测属性是否符合要求，符合返回true，否则返回false
		if(m ==null)return false;	
		return true;		
	}
	public void run()
	{
		while(true)
		{
			try {
				sleep(300);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			synchronized(this.m){
				m.changelight();
			}
		}
	}
}
