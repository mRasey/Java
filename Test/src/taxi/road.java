package taxi;

public class road {
	private int x1,y1,x2,y2;
	public road(int x1,int y1,int x2,int y2)
	{	//Requires：四个int类型的数字，都必须满足0<=x1,y1,x2,y2<=79
		//Modifies：x1,y1,x2,y2
		//Effects：初始化一个两点组，由(x1,y1),(x2,y2)组成
		this.x1 = x1;
		this.x2 = x2;
		this.y1 = y1;
		this.y2 = y2;
	}
	public boolean reqOK()
	{//Requires：
		//Modifies：
		//Effects：检测属性是否符合要求，符合返回true，否则返回false
		if(x1<0||x1>=80)return false;
		if(x2<0||x2>=80)return false;
		if(y1<0||y2>=80)return false;
		if(y2<0||y2>=80)return false;
		return true;		
	}
	public int x1()
	{	//Requires：
		//Modifies：
		//Effects：获得两点组第一个点的横坐标的方法，返回两点组的第一个点的横坐标
		return x1;
	}
	public int y1()
	{	//Requires：
		//Modifies：
		//Effects：获得两点组第一个点的纵坐标的方法，返回两点组的第一个点的纵坐标
		return y1;
	}
	public int x2()
	{	//Requires：
		//Modifies：
		//Effects：获得两点组第二个点的横坐标的方法，返回两点组的第二个点的横坐标
		return x2;
	}
	public int y2()
	{	//Requires：
		//Modifies：
		//Effects：获得两点组第二个点的纵坐标的方法，返回两点组的第二个点的纵坐标
		return y2;
	}
	public String tostring()
	{	//Requires：
		//Modifies：
		//Effects：获得两点组的方法，若该两点组不为((-2,-2),(-2,-2)),返回一个字符串包含两点组的信息
		//否则，返回一个空字符串""
		String s;
		if(x1==-2&&y1==-2&&x2==-2&&y2==-2)
			s = "";
		else
			s = "("+x1+","+y1+","+x2+","+y2+")";
		return s;
	}
}
