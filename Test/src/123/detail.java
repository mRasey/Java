import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class detail extends Thread{
	private ArrayList<String> filedetail1 = new ArrayList<String>();
	private ArrayList<String> filedetail2 = new ArrayList<String>();
	public synchronized void addchange(String s1, String s2)// synchronized 
	{
		filedetail1.add(s1);
		filedetail2.add(s2);
	}
	public void run()
	{
		while(true)
		{
			try {			
				sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				System.out.println("error3");
			}
			File wfile = new File("detail.txt");
			if(!wfile.exists()){
				try {
					wfile.createNewFile();
				} catch (IOException e) {
					System.out.println("error4");
				}
			}
				FileWriter writer = null;  
		        try {     
		            writer = new FileWriter(wfile.getPath()); 
		            synchronized(this)
		            {
		            	for(int i = 0;i<filedetail1.size();i++)
		            	{
		            	writer.write(filedetail1.get(i)+"&&&&"+filedetail2.get(i)+"\r\n");
		            	}
		            }
		        } catch (IOException e) {     
		        	System.out.println("error2");     
		        } finally {     
		            try {     
		                if(writer != null){  
		                    writer.close();     
		                }  
		            } catch (IOException e) {     
		            	System.out.println("error1");   
		            }     
		     
			}
		}
	}
}
