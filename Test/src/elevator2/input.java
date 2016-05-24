package elevator2;

import java.util.Scanner;

public class input extends Thread {
	private String s = null;
	private long time;
	queue que;
	String pattern1 = "^\\(FR,[0-9][0-9]?,(UP|DOWN)\\)$";
	String pattern2 = "^\\(ER,\\#[1-3],[0-9][0-9]?\\)$";
	public input(queue q)
	{
		que = q;
	}
	public void run() {
		Scanner in = new Scanner(System.in);
		scheduler.settime((System.currentTimeMillis()+50)/100*100);
		while(true)
		{
			try
			{
				s = in.nextLine();
				time = System.currentTimeMillis();
				s = s.replace(" ","");
				if(s.matches(pattern1)||s.matches(pattern2))
				{
					time = (time+50-scheduler.gettime())/100;
					que.append(s,time);
				}
				else if(s.equals("exit"))
				{
					System.out.print("程序结束");
					in.close();
					System.exit(0);
				}
				else
				{
					System.out.println("输入错误，请重新输入:");
				}
			}
			catch(Exception e)
			{
				System.out.print("程序结束");
				in.close();
				System.exit(0);
			}
			finally
			{
				
			}
		}
	}
}
