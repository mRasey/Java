
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

//import javax.swing.text.AttributeSet.CharacterAttribute;

public class inputtest extends Thread{
	//获取文件信息
	public void run(){
		//////////////////从这里开始///////////
	}
	public void getinfo(String path){//获得文件信息方法
		File file = new File(path);
		if(file.exists()){
			System.out.println(file.getPath()+" "+file.getName()+" "+file.length()+" "+file.lastModified());
		}
		else{
			System.out.println("文件不存在");
		}
		try {
			sleep(10000);
		} catch (InterruptedException e) {
			System.out.println("error");
		}
	}
	
	public void creatfile(String path){//新建文件方法
		File file = new File(path);
		if(file.exists()){
			System.out.println("文件已存在");
		}
		else{
			try {
				file.createNewFile();
			} catch (IOException e) {
				System.out.println("error");
			}
		}
		try {
			sleep(10000);
		} catch (InterruptedException e) {
			System.out.println("error");
		}
	}
	
	public void deletefile(String path){//删除文件方法
		File file = new File(path);
		if(file.exists()){
			file.delete();
		}
		else{
			System.out.println("文件不存在");
		}
		try {
			sleep(10000);
		} catch (InterruptedException e) {
			System.out.println("error");
		}
	}

	public void renamefile(String path, String newpath){//重命名文件方法
		File file = new File(path);
		if(file.exists()){
			if(file.isFile()){
				file.renameTo(new File(newpath));
			}
			else{
				System.out.println("文件夹不能重命名");
			}
		}
		else {
			System.out.println("文件不存在");
		}
		try {
			sleep(10000);
		} catch (InterruptedException e) {
			System.out.println("error");
		}
	}	
	public void movefile(String oldpath, String newpath){//移动文件方法
		File file = new File(oldpath);
		if(file.exists()){
			if(file.isFile())
				file.renameTo(new File(newpath));
			else
				System.out.println("文件夹不能移动");
		}
		else{
			System.out.println("文件不存在");
		}
		try {
			sleep(10000);
		} catch (InterruptedException e) {
			System.out.println("error");
		}
	}
	
	public  void writefile(String path, String content){//文件写入方法
		File file = new File(path);
		if(file.exists() && file.isFile()){
			FileWriter writer = null;  
	        try {     
	            writer = new FileWriter(path, true);     
	            writer.write(content);       
	        } catch (IOException e) {     
	            e.printStackTrace();     
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
		else{
			System.out.println("error");
		}
		try {
			sleep(10000);
		} catch (InterruptedException e) {
			System.out.println("error");
		}
	}
	public void creatdir(String path){//新建文件夹方法
		File file = new File(path);
		if(file.exists() && file.isDirectory()){
			System.out.println("文件夹已存在");
		}
		else{
			file.mkdirs();
		}
		try {
			sleep(10000);
		} catch (InterruptedException e) {
			System.out.println("error");
		}
		
	}
}