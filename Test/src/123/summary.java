import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class summary extends Thread{
	private int renamed;
	private int modified;
	private int pathchange;
	private int sizechange;
	public synchronized void renamedadd()
	{
		renamed++;
	}
	public synchronized void modifiedadd()
	{
		modified++;
	}
	public synchronized void pathchangeadd()
	{
		pathchange++;
	}
	public synchronized void sizechangeadd()
	{
		sizechange++;
	}
	public void run()
	{
		File wfile = new File("summary.txt");
		if(!wfile.exists()){
			try {
				wfile.createNewFile();
			} catch (IOException e) {
				System.out.println("error");
			}
		}
		while(true)
		{
			try {			
				sleep(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				System.out.println("error");
			}
				FileWriter writer = null;  
		        try {
		        	writer = new FileWriter(wfile.getPath());
		        	synchronized(this)
		        	{  
			            writer.write("renamed"+renamed+"\r\n");
			            writer.write("modified"+modified+"\r\n"); 
			            writer.write("pathchange"+pathchange+"\r\n"); 
			            writer.write("sizechange"+sizechange+"\r\n");
		        	}
		        } catch (IOException e) {     
		        	System.out.println("error");     
		        } finally {     
		            try {     
		                if(writer != null){  
		                    writer.close();     
		                }  
		            } catch (IOException e) {     
		            	System.out.println("error");   
		            }     
		     
			}
		}
	}
}
