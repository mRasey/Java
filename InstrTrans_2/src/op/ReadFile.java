package op;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/*
 * 负责每次读取一行数据，判断是否是指令或方法，并将指令分解传出
 * */



public class ReadFile {
	
	File file;
	String str = "";
	FileInputStream fis = null;
	InputStreamReader isr = null;
	BufferedReader br = null; 
	
	//String [][]instructions = new String[10000][5];
	
	
	ArrayList<ArrayList<String>> instructions = new ArrayList<ArrayList<String>>();
	int order = 0;
	
	
	//注意只有寄存器参数的情况
	String regex1 = "[p,v]\\d+(\\,)?";
	String regex2 = "[{][p,v]\\d+[}](\\,)?";
	String regex3 = "[{][p,v]\\d+\\,";
	String regex4 = "[p,v]\\d+[}]\\,";
	
	
	public ReadFile(String f){
		file = new File(f);
		try {
			fis = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		isr = new InputStreamReader(fis);
		br = new BufferedReader(isr);
	}
	
	
	
	
	//读一行,结束返回false
	public boolean readLine(){
		try {
			if((str = br.readLine()) != null){
				return true;
				
			}
			else{
				return false;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}
	
	//空行
	public boolean ifNull(){
		if(str.equals("")){
			return true;
		}
		return false;
	}
	
	
	
	//是否是方法的开始 .method
	public boolean ifNewMethod(){
		if(str.indexOf(".method") != -1){
			return true;
		}
		return false;
	}
	
	//是否是一条指令
	public boolean ifAnInstruction(String ins){
		char []temp = ins.toCharArray();
		int i = 0;
		if(temp[0] == '.' || temp[0] == '#' || temp[0] == ':'){
			return false;
		}
		return true;
	}
	
	//分解指令
	public ArrayList<String> getInstruction(){
		int i = 0;
		ArrayList<String> tempins = new ArrayList<String>();
		String []temp = str.split(" "); 
		
		while(i<temp.length && temp[i].equals("")){
			i++;
		}
		tempins.add(temp[i]);
		i++;
		//没有参数的指令
		if(i >= temp.length){
			order++;
			instructions.add(tempins);
			return tempins;
		}
		//v0    v0,v1
		if(temp[i].matches(regex1)){
			temp[i] = temp[i].replace(",", "");
			tempins.add(temp[i]);
			while( (i+1)<temp.length && temp[i+1].matches(regex1)){
				i++;
				temp[i] = temp[i].replace(",", "");
				tempins.add(temp[i]);
			}
		}
		//{v0}
		else if(temp[i].matches(regex2)){
			temp[i] = temp[i].replace("{", "");
			temp[i] = temp[i].replace("}", "");
			temp[i] = temp[i].replace(",", "");
			tempins.add(temp[i]);
		}
		//{v0,v1}      {v0,v1,v2}
		else if(temp[i].matches(regex3)){
			temp[i] = temp[i].replace("{", "");
			temp[i] = temp[i].replace(",", "");
			tempins.add(temp[i]);
			i++;
			
			if(temp[i].matches(regex4)){
				temp[i] = temp[i].replace("}", "");
				temp[i] = temp[i].replace(",", "");
				tempins.add(temp[i]);
			}
			else{
				while(i<temp.length && temp[i].matches(regex1)){
					temp[i] = temp[i].replace(",", "");
					tempins.add(temp[i]);
					i++;
				}
				temp[i] = temp[i].replace("}", "");
				temp[i] = temp[i].replace(",", "");
				tempins.add(temp[i]);
			}
			
		}
		else{//没有寄存器参数的情况
			while(i<temp.length){
				tempins.add(temp[i]);
				i++;
			}
		}
		i++;
		if(i >= temp.length){
			order++;
			instructions.add(tempins);
			return tempins;
		}
		else{
			while(i<temp.length){
				if(!temp[i].equals("")){
					tempins.add(temp[i]);
				}
				i++;
			}
		}
		
		order++;
		instructions.add(tempins);
		return tempins;
	}
	
	public ArrayList<String> getInstruction(int order){
		return instructions.get(order);
	}
}
