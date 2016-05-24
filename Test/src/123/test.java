import java.util.Scanner;

public class test {
	@SuppressWarnings("resource")
	public static void main(String[] args)
	{
		Scanner in = new Scanner(System.in);
		monitor[] m = new monitor[8];
		summary sum = new summary();
		detail det = new detail();
		trigs trig = null;
		tasks task = null;
		String path;
		String filename;
		int i = 0;
		String pattern1 = "^IF .+ (renamed|modified|path-changed|size-changed) THEN (record-summary|record-detail|recover)$";
		String pattern2 = "^IF .+@.+ (renamed|modified|path-changed|size-changed) THEN (record-summary|record-detail|recover)$";
		for(i = 0;i < 8;){
			if(in.hasNextLine()){
				  String s = in.nextLine();
				 
				 if(s.equals("end") && i >=5)
					 break;
				 if(s.matches(pattern1)){
					 String[] ss = s.split(" ");
					 path = ss[1];
					 if(ss[2].equals("renamed"))
						 trig = trigs.renamed;
					 else if(ss[2].equals("modified"))
						 trig = trigs.modified;
					 else if(ss[2].equals("path-changed"))
						 trig = trigs.pathchange;
					 else if(ss[2].equals("size-changed"))
						 trig = trigs.sizechanged;
					 else
						 {
						 System.out.println("输入有误程序结束");
						 System.exit(0);
						 }
					 if(ss[4].equals("record-summary"))
						 task = tasks.summary;
					 else if(ss[4].equals("record-detail"))
						 task = tasks.detail;
					 else if(ss[4].equals("recover"))
						 task = tasks.recover;
					 else
					 {
					 System.out.println("输入有误程序结束");
					 System.exit(0);
					 }
					 m[i] = new monitor(ss[1],trig,task,sum,det,false);
					 i++;
				 }
				 else if(s.matches(pattern2)){
					 String[] ss = s.split(" ");
					 String[] sss = ss[1].split("@");
					 path = sss[0];
					 filename = sss[1];
					 if(ss[2].equals("renamed"))
						 trig = trigs.renamed;
					 else if(ss[2].equals("modified"))
						 trig = trigs.modified;
					 else if(ss[2].equals("path-changed"))
						 trig = trigs.pathchange;
					 else if(ss[2].equals("size-changed"))
						 trig = trigs.sizechanged;
					 else
					 {
					 System.out.println("输入有误程序结束");
					 System.exit(0);
					 }
					 if(ss[4].equals("record-summary"))
						 task = tasks.summary;
					 else if(ss[4].equals("record-detail"))
						 task = tasks.summary;
					 else if(ss[4].equals("recover"))
						 task = tasks.recover;
					 else
					 {
					 System.out.println("输入有误程序结束");
					 System.exit(0);
					 }
					 m[i] = new monitor(path,trig,task,sum,det,true,filename);
					 i++;
				 }
				 else{
					 System.out.println("输入有误");
				 }				 
			}
		}
		for(int j = 0;j<i;j++)
			{
				m[j].start();
			}
		sum.start();
		det.start();
		}
		/////线程插入位置/////////////
}