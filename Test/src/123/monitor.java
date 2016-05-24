import java.io.File;
import java.util.ArrayList;

public class monitor extends Thread{
	private String path;
	private String goalpath;
	private String goalname;
	private boolean isfi;
	private trigs trig;
	private tasks task;
	private summary summary;
	private detail detail;
	private ArrayList<document> list1 = new ArrayList<document>();
	private ArrayList<document> list2 = new ArrayList<document>();
	public monitor(String path,trigs trig,tasks task , summary summary,detail detail,boolean isfi)
	{
		this.path = path;
		this.trig = trig;
		this.task = task;
		this.summary = summary;
		this.detail = detail;
		this.isfi = isfi;
	}
	public monitor(String path,trigs trig,tasks task , summary summary,detail detail,boolean isfi,String goalname)
	{
		this.path = path;
		this.trig = trig;
		this.task = task;
		this.summary = summary;
		this.detail = detail;
		this.isfi = isfi;
		this.goalname = goalname;
	}
	public void run(){
		File file =new File(path);
		if(!isfi)
		{
			
			if(!(file.exists()))
			{
				System.out.println(file);
				System.out.println("根目录不存在");
			}
			else
			{	
				if(file.isFile())
					System.out.println("请正确输入");
				else
				{
					Traversal(file,list1);
					while(true)
					{
						try {
							sleep(3000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						Traversal(file,list2);
						if(task == tasks.summary)
						{
							if(trig == trigs.renamed)	
								checkrenamed(list1,list2,task);
							else if(trig == trigs.modified)
								checkmodified(list1,list2,task);
							else if(trig == trigs.pathchange)
								checkpathchange(list1,list2,task);
							else
								checksizechange(list1,list2,task);
						}
						else if(task == tasks.detail)
						{
							if(trig == trigs.renamed)	
								checkrenamed(list1,list2,task);
							else if(trig == trigs.modified)
								checkmodified(list1,list2,task);
							else if(trig == trigs.pathchange)
								checkpathchange(list1,list2,task);
							else
								checksizechange(list1,list2,task);
						}
						else if(task == tasks.recover)
						{
							if(trig == trigs.renamed)	
								checkrenamed(list1,list2,task);
							else if(trig == trigs.pathchange)
								checkpathchange(list1,list2,task);
						}
						list1.clear();
						list1 = new ArrayList<document>();
						for(int i =0;i<list2.size();i++)
							list1.add(list2.get(i));
						list2.clear();
					}
				}
			}
		}
		else
		{
			if(!(file.exists()))
			{
				System.out.println("根目录不存在");
			}
			else{			
				if(file.isFile())
					System.out.println("error");
				else
				{
					goalpath = path;
					File f2 = new File(goalpath+goalname);
					if(f2.exists()&&f2.isFile())
					{
						Traversal(file,list1);
						while(true){
							f2 = new File(goalpath+goalname);
							String ssss = f2.getParent()+File.separator;
							document docu1 = new document(ssss,f2.lastModified(),f2.length(),f2.getName(),true,0);
							Traversal(file,list2);
							if(task == tasks.summary)
							{
								if(trig == trigs.renamed)	
									checkrenamed(list1,list2,task,docu1);
								else if(trig == trigs.modified)
									checkmodified(list1,list2,task,docu1);
								else if(trig == trigs.pathchange)
									checkpathchange(list1,list2,task,docu1);
								else
									checksizechange(list1,list2,task,docu1);
							}
							else if(task == tasks.detail)
							{
								if(trig == trigs.renamed)	
									checkrenamed(list1,list2,task,docu1);
								else if(trig == trigs.modified)
									checkmodified(list1,list2,task,docu1);
								else if(trig == trigs.pathchange)
									checkpathchange(list1,list2,task,docu1);
								else
									checksizechange(list1,list2,task,docu1);
							}
							else if(task == tasks.recover)
							{
								if(trig == trigs.renamed)	
									checkrenamed(list1,list2,task,docu1);
								else if(trig == trigs.pathchange)
									checkpathchange(list1,list2,task,docu1);
							}
							list1.clear();
							list1 = new ArrayList<document>();
							for(int i =0;i<list2.size();i++)
								list1.add(list2.get(i));
							list2.clear();
						}
					}
					else
						System.out.println("error");
				}
			}
		}
	}
	public void Traversal(File file,ArrayList<document> list)
	{
		File[] files =file.listFiles();		
		long size = 0;
        for(File f:files){
            if(f.isDirectory())
            {
            	Traversal(f,list);
            }
            else{
            	size += f.length();
            	String ssss = f.getParent()+File.separator;
            	document docu = new document(ssss,f.lastModified(),f.length(),f.getName(),true,0);
            	list.add(docu);
            	//System.out.println(docu.tostring());
            }
        }
        String ssss = file.getParent()+File.separator;
        document  docu = new document(ssss,file.lastModified(),size,file.getName(),false,files.length);
        list.add(docu);
	}
	private void checkrenamed(ArrayList<document> list1,ArrayList<document> list2,tasks task,document docu)
	{
		boolean exist = false;
		int n;
		for(int i = 0 ;i< list2.size();i++)
		{
			document docu2 = list2.get(i);
			for(n = 0;n < list1.size();n++)
			{
				document docu1 = list1.get(n);
				if(docu1.getpath().equals(docu2.getpath())&&docu1.getname().equals(docu2.getname())&&docu1.isfile()==docu2.isfile()&&docu1.gettime()==docu2.gettime()&&docu1.getsize()==docu2.getsize())
					break;
			}
			if(n == list1.size())
			{
				docu2.setisnew(true);
			}
		}
			document docu1 = docu;
			for(int j = 0;j < list2.size();j++)
			{
				document docu2 = list2.get(j);
				if(docu1.getpath().equals(docu2.getpath())&&docu1.getname().equals(docu2.getname())&&docu1.isfile()==docu2.isfile()&&docu1.gettime()==docu2.gettime()&&docu1.getsize()==docu2.getsize())
				{
					exist = true;
					break;
				}
			}
			//System.out.println(exist);
			if(exist == false)
			{
				for(int j = 0;j < list2.size();j++)
				{
					document docu2 = list2.get(j);
					if(docu1.getpath().equals(docu2.getpath())&&docu1.isfile()==docu2.isfile()&&docu1.gettime()==docu2.gettime()&&docu1.getsize()==docu2.getsize()&&docu2.getisnew()==true)
					{
						if(task == tasks.summary)
							summary.renamedadd();
						else if(task == tasks.detail)
						{
							String s1 = docu1.getpath()+"***"+docu1.getname()+"***"+docu1.getsize()+"***"+docu1.gettime();
							String s2 = docu2.getpath()+"***"+docu2.getname()+"***"+docu2.getsize()+"***"+docu2.gettime();
							detail.addchange(s1, s2);
						}
						else if(task == tasks.recover)
						{
							summary.renamedadd();
							File old = new File(docu1.getpath()+docu1.getname());
							File newfile = new File(docu2.getpath()+docu2.getname());
							if(newfile.isFile())
								newfile.renameTo(old);
							list2.remove(j);
							list2.add(docu1);
						}
						this.goalname = docu2.getname();
						break;
					}
				}
			}
			exist = false;
		for(int j = 0;j < list2.size();j++)
		{
			list2.get(j).setisnew(false);
		}
	}
	private void checkrenamed(ArrayList<document> list1,ArrayList<document> list2,tasks task)
	{
		boolean exist = false;
		int n;
		for(int i = 0 ;i< list2.size();i++)
		{
			document docu2 = list2.get(i);
			for(n = 0;n < list1.size();n++)
			{
				document docu1 = list1.get(n);
				if(docu1.getpath().equals(docu2.getpath())&&docu1.getname().equals(docu2.getname())&&docu1.isfile()==docu2.isfile()&&docu1.gettime()==docu2.gettime()&&docu1.getsize()==docu2.getsize())
					break;
			}
			if(n == list1.size())
			{
				docu2.setisnew(true);
			}
		}
		for(int i = 0 ;i< list1.size();i++)
		{
			document docu1 = list1.get(i);
			for(int j = 0;j < list2.size();j++)
			{
				document docu2 = list2.get(j);
				if(docu1.getpath().equals(docu2.getpath())&&docu1.getname().equals(docu2.getname())&&docu1.isfile()==docu2.isfile()&&docu1.gettime()==docu2.gettime()&&docu1.getsize()==docu2.getsize())
				{
					exist = true;
					break;
				}
			}
			//System.out.println(exist);
			if(exist == false)
			{
				for(int j = 0;j < list2.size();j++)
				{
					document docu2 = list2.get(j);
					if(docu1.getpath().equals(docu2.getpath())&&docu1.isfile()==docu2.isfile()&&docu1.gettime()==docu2.gettime()&&docu1.getsize()==docu2.getsize()&&docu2.getisnew()==true)
					{
						if(task == tasks.summary)
							summary.renamedadd();
						else if(task == tasks.detail)
						{
							String s1 = docu1.getpath()+"***"+docu1.getname()+"***"+docu1.getsize()+"***"+docu1.gettime();
							String s2 = docu2.getpath()+"***"+docu2.getname()+"***"+docu2.getsize()+"***"+docu2.gettime();
							detail.addchange(s1, s2);
						}
						else if(task == tasks.recover)
						{
							File old = new File(docu1.getpath()+docu1.getname());
							File newfile = new File(docu2.getpath()+docu2.getname());
							if(newfile.isFile())
								newfile.renameTo(old);
							list2.remove(j);
							list2.add(docu1);
						}
						break;
					}
				}
			}
			exist = false;
		}
		for(int j = 0;j < list2.size();j++)
		{
			list2.get(j).setisnew(false);
		}
	}
	private void checkmodified(ArrayList<document> list1,ArrayList<document> list2,tasks task,document docu1)
	{
			for(int j = 0;j < list2.size();j++)
			{
				document docu2 = list2.get(j);
				if(docu1.getpath() == docu2.getpath()&&docu1.getname() == docu2.getname()&&docu1.isfile()==docu2.isfile()&&docu1.gettime()!=docu2.gettime())
				{
					if(task == tasks.summary)
						summary.modifiedadd();
					if(task == tasks.detail)
					{
						String s1 = docu1.getpath()+"***"+docu1.getname()+"***"+docu1.getsize()+"***"+docu1.gettime();
						String s2 = docu2.getpath()+"***"+docu2.getname()+"***"+docu2.getsize()+"***"+docu2.gettime();
						detail.addchange(s1, s2);
					}
				}
			}
	}
	private void checkmodified(ArrayList<document> list1,ArrayList<document> list2,tasks task)
	{
		for(int i = 0 ;i< list1.size();i++)
		{
			document docu1 = list1.get(i);
			for(int j = 0;j < list2.size();j++)
			{
				document docu2 = list2.get(j);
				if(docu1.getpath() == docu2.getpath()&&docu1.getname() == docu2.getname()&&docu1.isfile()==docu2.isfile()&&docu1.gettime()!=docu2.gettime())
				{
					if(task == tasks.summary)
						summary.modifiedadd();
					if(task == tasks.detail)
					{
						String s1 = docu1.getpath()+"***"+docu1.getname()+"***"+docu1.getsize()+"***"+docu1.gettime();
						String s2 = docu2.getpath()+"***"+docu2.getname()+"***"+docu2.getsize()+"***"+docu2.gettime();
						detail.addchange(s1, s2);
					}
				}
			}
		}
	}
	private void checksizechange(ArrayList<document> list1,ArrayList<document> list2,tasks task,document docu1)
	{
			for(int j = 0;j < list2.size();j++)
			{
				document docu2 = list2.get(j);
				if(docu1.getpath() == docu2.getpath()&&docu1.getname() == docu2.getname()&&docu1.isfile()==docu2.isfile()&&docu1.getsize()!=docu2.getsize())
				{
					if(task == tasks.summary)
						summary.sizechangeadd();
					if(task == tasks.detail)
					{
						String s1 = docu1.getpath()+"***"+docu1.getname()+"***"+docu1.getsize()+"***"+docu1.gettime();
						String s2 = docu2.getpath()+"***"+docu2.getname()+"***"+docu2.getsize()+"***"+docu2.gettime();
						detail.addchange(s1, s2);
					}
				}
			}
	}
	private void checksizechange(ArrayList<document> list1,ArrayList<document> list2,tasks task)
	{
		for(int i = 0 ;i< list1.size();i++)
		{
			document docu1 = list1.get(i);
			for(int j = 0;j < list2.size();j++)
			{
				document docu2 = list2.get(j);
				if(docu1.getpath() == docu2.getpath()&&docu1.getname() == docu2.getname()&&docu1.isfile()==docu2.isfile()&&docu1.getsize()!=docu2.getsize())
				{
					if(task == tasks.summary)
						summary.sizechangeadd();
					if(task == tasks.detail)
					{
						String s1 = docu1.getpath()+"***"+docu1.getname()+"***"+docu1.getsize()+"***"+docu1.gettime();
						String s2 = docu2.getpath()+"***"+docu2.getname()+"***"+docu2.getsize()+"***"+docu2.gettime();
						detail.addchange(s1, s2);
					}
				}
			}
		}
		int n;
		for(int i = 0 ;i< list1.size();i++)
		{
			document docu1 = list1.get(i);
			for(n = 0;n < list1.size();n++)
			{
				document docu2 = list2.get(n);
				if(docu1.getpath().equals(docu2.getpath())&&docu1.getname().equals(docu2.getname())&&docu1.isfile()==docu2.isfile()&&docu1.gettime()==docu2.gettime()&&docu1.getsize()==docu2.getsize()&&docu1.getcount()!=docu2.getcount())
				{
					if(task == tasks.summary)
						summary.sizechangeadd();
					if(task == tasks.detail)
					{
						String s1 = docu1.getpath()+"***"+docu1.getname()+"***"+docu1.getsize()+"***"+docu1.gettime();
						String s2 = docu2.getpath()+"***"+docu2.getname()+"***"+docu2.getsize()+"***"+docu2.gettime();
						detail.addchange(s1, s2);
					}
				}
			}
		}
	}
	private void checkpathchange(ArrayList<document> list1,ArrayList<document> list2,tasks task,document docu)
	{
		boolean exist = false;
		int n;
		for(int i = 0 ;i< list2.size();i++)
		{
			document docu2 = list2.get(i);
			for(n = 0;n < list1.size();n++)
			{
				document docu1 = list1.get(n);
				if(docu1.getpath().equals(docu2.getpath())&&docu1.getname().equals(docu2.getname())&&docu1.isfile()==docu2.isfile()&&docu1.gettime()==docu2.gettime()&&docu1.getsize()==docu2.getsize())
					break;
			}
			if(n == list1.size())
			{
				docu2.setisnew(true);
			}
		}
			document docu1 = docu;
			for(int j = 0;j < list2.size();j++)
			{
				document docu2 = list2.get(j);
				if(docu1.getpath().equals(docu2.getpath())&&docu1.getname().equals(docu2.getname())&&docu1.isfile()==docu2.isfile()&&docu1.gettime()==docu2.gettime()&&docu1.getsize()==docu2.getsize())
				{
					exist = true;
					break;
				}
			}
			//System.out.println(exist);
			if(exist == false)
			{
				for(int j = 0;j < list2.size();j++)
				{
					document docu2 = list2.get(j);
					if(docu1.getname().equals(docu2.getname())&&docu1.isfile()==docu2.isfile()&&docu1.gettime()==docu2.gettime()&&docu1.getsize()==docu2.getsize()&&docu2.getisnew()==true)
					{
						if(task == tasks.summary)
							summary.pathchangeadd();
						else if(task == tasks.detail)
						{
							String s1 = docu1.getpath()+"***"+docu1.getname()+"***"+docu1.getsize()+"***"+docu1.gettime();
							String s2 = docu2.getpath()+"***"+docu2.getname()+"***"+docu2.getsize()+"***"+docu2.gettime();
							detail.addchange(s1, s2);
						}
						else if(task == tasks.recover)
						{
							summary.pathchangeadd();
							File old = new File(docu1.getpath()+docu1.getname());
							File newfile = new File(docu2.getpath()+docu2.getname());
							if(newfile.isFile())
								newfile.renameTo(old);
							list2.remove(j);
							list2.add(docu1);
						}
						this.goalpath = docu2.getpath();
						break;
					}
				}
			}
			exist = false;
		for(int j = 0;j < list2.size();j++)
		{
			list2.get(j).setisnew(false);
		}
	}
	private void checkpathchange(ArrayList<document> list1,ArrayList<document> list2,tasks task)
	{
		boolean exist = false;
		int n;
		for(int i = 0 ;i< list2.size();i++)
		{
			document docu2 = list2.get(i);
			for(n = 0;n < list1.size();n++)
			{
				document docu1 = list1.get(n);
				if(docu1.getpath().equals(docu2.getpath())&&docu1.getname().equals(docu2.getname())&&docu1.isfile()==docu2.isfile()&&docu1.gettime()==docu2.gettime()&&docu1.getsize()==docu2.getsize())
					break;
			}
			if(n == list1.size())
			{
				docu2.setisnew(true);
			}
		}
		for(int i = 0 ;i< list1.size();i++)
		{
			document docu1 = list1.get(i);
			for(int j = 0;j < list2.size();j++)
			{
				document docu2 = list2.get(j);
				if(docu1.getpath().equals(docu2.getpath())&&docu1.getname().equals(docu2.getname())&&docu1.isfile()==docu2.isfile()&&docu1.gettime()==docu2.gettime()&&docu1.getsize()==docu2.getsize())
				{
					exist = true;
					break;
				}
			}
			//System.out.println(exist);
			if(exist == false)
			{
				for(int j = 0;j < list2.size();j++)
				{
					document docu2 = list2.get(j);
					//System.out.println(docu1.getpath()+"'''"+docu1.getname()+"''''"+docu1.isfile()+"'''"+docu1.gettime()+"'''"+docu1.getsize());
					//System.out.println(docu2.getpath()+"'''"+docu2.getname()+"''''"+docu2.isfile()+"'''"+docu2.gettime()+"'''"+docu2.getsize()+"'''"+docu2.getisnew());
					if(docu1.getname().equals(docu2.getname())&&docu1.isfile()==docu2.isfile()&&docu1.gettime()==docu2.gettime()&&docu1.getsize()==docu2.getsize()&&docu2.getisnew()==true)
					{
						if(task == tasks.summary)
							summary.pathchangeadd();
						else if(task == tasks.detail)
						{
							//System.out.println("here");
							String s1 = docu1.getpath()+"***"+docu1.getname()+"***"+docu1.getsize()+"***"+docu1.gettime();
							String s2 = docu2.getpath()+"***"+docu2.getname()+"***"+docu2.getsize()+"***"+docu2.gettime();
							detail.addchange(s1, s2);
						}
						else if(task == tasks.recover)
						{
							File old = new File(docu1.getpath()+docu1.getname());
							File newfile = new File(docu2.getpath()+docu2.getname());
							if(newfile.isFile())
								newfile.renameTo(old);
							list2.remove(j);
							list2.add(docu1);
						}
						break;
					}
				}
			}
			exist = false;
		}
		for(int j = 0;j < list2.size();j++)
		{
			list2.get(j).setisnew(false);
		}
	}
}
