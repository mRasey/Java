package taxi;
//delete

public class dispatcher extends Thread{//调度系统类
	private volatile taxi[] t;
	private requestqueue queue;
	private map map;
	public dispatcher(taxi[] t,requestqueue queue,map m)
	//Requires:一个类型为taxi的数组代表所有出租车，一个类型为requestqueue的初始化过的请求队列，一个类型为map的对象为地图，map中包含的地图必须为全连通图,map不为null
	//Modifies：t,queue,map
	//Effects：初始化调度器，使其可以访问地图和出租车
	{
		this.t = t;
		this.queue = queue;
		this.map = m;
	}
	public boolean reqOK()
	{//Requires：
		//Modifies：
		//Effects：检测属性是否符合要求，符合返回true，否则返回false
		if(t ==null)return false;
		if(queue ==null)return false;	
		if(map ==null)return false;	
		return true;
	}
	public void run()
	{
		request r;
		while(true)
		{
			/*try {
				sleep(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
			if(queue.hasrequest())
			{
				for(int i = 0;i<queue.size();i++)
				{
					r = queue.getrequest(i);
					for(int j = 0;j<100;j++)
					{
						if(r!=null&&queue.getrequest(i)!=null&&t[j].getstate() == taxistate.waittoserve&&Math.abs(t[j].getx()-queue.getrequest(i).getx1())<=2&&Math.abs(t[j].gety()-queue.getrequest(i).gety1())<=2)
						{
							int m = 0;
							for(m = 0;m<r.gettaxiqueue().size();m++)
							{
								if(r.gettaxiqueue().get(m)==t[j])
									break;
							}
							if(m == r.gettaxiqueue().size())
								r.gettaxiqueue().add(t[j]);
						}
					}
					if(r != null&&System.currentTimeMillis()-r.gettime()>=3000)
					{
						//System.out.println("111");
						if(r.gettaxiqueue().size()>0)
						{
							for(int j = 0;j<r.gettaxiqueue().size();j++)
							{
								r.gettaxiqueue().get(j).addcredit(1);
							}
							for(int j = 0;j<r.gettaxiqueue().size();j++)
							{
								//(r.gettaxiqueue().get(j).getstate()+"ss"+r.gettaxiqueue().get(j).getid()+"a"+r.gettaxiqueue().size());
								if(r.gettaxiqueue().get(j).getstate() != taxistate.waittoserve) {
									r.gettaxiqueue().remove(j);
									j--;
								}
							}
							
							for(int j = 0;j<r.gettaxiqueue().size();j++)
							{
								//System.out.println(r.gettaxiqueue().get(j).getstate()+"###"+r.gettaxiqueue().get(j).getid()+"a"+r.gettaxiqueue().size());
							}
							if (r.gettaxiqueue().size()>0)
							{
								int maxcredit = r.gettaxiqueue().get(0).getcredit();
								for(int j = 0;j<r.gettaxiqueue().size();j++)
								{
									if(maxcredit < r.gettaxiqueue().get(j).getcredit())
										maxcredit = r.gettaxiqueue().get(j).getcredit();
								}
								for(int j = 0;j<r.gettaxiqueue().size();j++)
								{
									if(maxcredit > r.gettaxiqueue().get(j).getcredit())
										r.gettaxiqueue().remove(j);
								}
								//System.out.println("333");
								if(r.gettaxiqueue().size()>1)
								{
									int leastlength = map.waylength(map.findtheway(r.gettaxiqueue().get(0).getx(), r.gettaxiqueue().get(0).gety(), r.getx1(),r.gety1()));
									for(int j = 0;j<r.gettaxiqueue().size();j++)
									{
										if(leastlength > map.waylength(map.findtheway(r.gettaxiqueue().get(0).getx(), r.gettaxiqueue().get(0).gety(), r.getx1(),r.gety1())))
											leastlength = map.waylength(map.findtheway(r.gettaxiqueue().get(0).getx(), r.gettaxiqueue().get(0).gety(), r.getx1(),r.gety1()));
										//System.out.println("888");
									}
									//System.out.println("777");
									for(int j = 0;j<r.gettaxiqueue().size();j++)
									{
										if(leastlength < map.waylength(map.findtheway(r.gettaxiqueue().get(0).getx(), r.gettaxiqueue().get(0).gety(), r.getx1(),r.gety1())))
											r.gettaxiqueue().remove(j);
									}
									//System.out.println("666");
									r.gettaxiqueue().get(0).setstate(r.getx1(),r.gety1(),r.getx2(),r.gety2());
									queue.delete(i);
									//System.out.println("k1kkk("+r.getx1()+","+r.gety1()+","+r.getx2()+","+r.gety2()+")"+r.gettaxiqueue().get(0).getid()+"ppp"+r.gettaxiqueue().get(0).getid());
									/*try {
										sleep(1);
									} catch (InterruptedException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}*/
									//System.out.println(r.gettaxiqueue().get(0).getstate());
								}
								else
								{
									//System.out.println("444");
									r.gettaxiqueue().get(0).setstate(r.getx1(),r.gety1(),r.getx2(),r.gety2());
									queue.delete(i);
									//System.out.println("k2kkk("+r.getx1()+","+r.gety1()+","+r.getx2()+","+r.gety2()+")"+r.gettaxiqueue().get(0).getid()+"ppp"+r.gettaxiqueue().get(0).getid());
									/*try {
										sleep(1);
									} catch (InterruptedException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}*/
									//System.out.println(r.gettaxiqueue().get(0).getstate());
								}
								//System.out.println("222");
							}
						}
						else
							{
								System.out.println("位于"+"("+r.getx1()+","+r.gety1()+")"+"的乘客无车可坐");
								queue.delete(i);
							}
					}
				}
			}
		}
	}
}
