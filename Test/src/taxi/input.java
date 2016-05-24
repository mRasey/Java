package taxi;

import java.io.File;
import java.util.Random;
import java.util.Scanner;

public class input {//输入处理
	public static long time;
	public boolean reqOK()
	{//Requires：
		//Modifies：
		//Effects：检测属性是否符合要求，符合返回true，否则返回false
		if(time<0)return false;
		return true;
	}
	 public static void main(String[] args){
		//Requires：
		//Modifies：time
		//Effects：主函数,完成其他对象的初始化及线程的启动
		
		@SuppressWarnings("resource")
		Scanner in = new Scanner(System.in);
		 time = System.currentTimeMillis();
		 Random rand = new Random();
		 taxi[] t = new taxi[100];
		 dispatcher dispatch;
		 requestqueue queue = new requestqueue();
		 String s = null,s2=null;
		 String pattern = "^the state of taxi\\(1?[0-9]?[0-9]\\)$";
		 String pattern1 = "^changemap\\([1-7]?[0-9],[1-7]?[0-9],[0-3]\\)$" ;
		 System.out.println("请输入地图文件所在路径:(注意：请不要输入错误的路径)");
		 while(true)
		 {
		 	 if(in.hasNextLine())	
			 {
				 s = in.nextLine();
			 }
			 else
			 {
				 System.out.println("程序结束");
				 System.exit(0);
			 }
			 File f = new File(s);
			 if(f.exists()&&f.isFile())
			 {
				 break;
			 }
			 else
			 {
				System.out.println("输入路径有误");
			 }
		 }
		 System.out.println("请输入交叉文件所在路径:(注意：请不要输入错误的路径)");
		 while(true)
		 {
		 	 if(in.hasNextLine())	
			 {
				 s2 = in.nextLine();
			 }
			 else
			 {
				 System.out.println("程序结束");
				 System.exit(0);
			 }
			 File f = new File(s2);
			 if(f.exists()&&f.isFile())
			 {
				 break;
			 }
			 else
			 {
				System.out.println("输入路径有误");
			 }
		 }
		 map m = new map(s);
		 m.mapjiaocha(s2);
		 light l = new light(m);
		 ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
		 for(int i = 0;i<100;i++)///////////////////////////////初始化出租车位置///////////////////////////////////
		 {///////////////////////////////////////////////////////初始化出租车位置///////////////////////////////////
			 t[i] = new taxi(rand.nextInt(80),rand.nextInt(80),m);//初始化出租车位置///////////////////////////////////
		 }//////////////////////////////////////////////////////////初始化出租车位置///////////////////////////////////
		 ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		 for(int i=0;i<100;i++){
			 if(t[i] == null)
			 {
				 System.out.println("出租车未初始化");
				 System.exit(0);
			 }
			 else
				 t[i].start();
		 }
		 dispatch = new dispatcher(t,queue,m);
		 customer cus= new customer(queue,t,m);
		 dispatch.start();
		 cus.start();
		 m.start();
		 l.start();
		 while(in.hasNextLine())//自带获得出租车状态及更改地图的方法，可直接从控制台输入以获取出租车状态及更改地图
		 {
			 s = in.nextLine();
			 if(s.equals("exit"))
			 {
				 System.out.println("程序结束");
				 System.exit(0);
			 }
			 else if(s.matches(pattern))
			 {
				 String[] ss = s.split("[()]");
				 int i = Integer.parseInt(ss[1]);
				 if(i>0&&i<=100)	///////////////////////////
				 	t[i-1].tostring();
				 else
					System.out.println("输入有误");
			 }
			 else if(s.matches(pattern1))
			 {
				 String[] sss = s.split("[(),]");
				 int i1,i2,i3;
				 i1 = Integer.parseInt(sss[1]);
				 i2 = Integer.parseInt(sss[2]);
				 i3 = Integer.parseInt(sss[3]);
				 m.changemap(i1,i2,i3);
			 }
			 else
				 System.out.println("输入有误");
		 }//
	 }
}
