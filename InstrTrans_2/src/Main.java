import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/*
 * 在翻译指令时：
 * 1.遇到特殊指令，能确定某个寄存器类型时要及时修改registerType
 * */


public class Main {
	
	static int LineNumber = 0;   //编号
	static int constNumber = 1;
	//各个小字符串对应的编号
	//static Map <String,Integer> constants = new HashMap<String,Integer>();
	//完整字符串对应的编号
	static Map <String,Integer> constants = new HashMap<String,Integer>();
	static Map <String,String> constantsType = new HashMap<String,String>();
	
	static String className = "";
	static String methodName = "";
	
	//记录跳转指令编号和他跳转的标签
	static Map <Integer,String> jumpAndtable = new HashMap<Integer,String>();
	//记录标签和他下条指令的编号
	static Map <String,Integer> tableAndnumber = new HashMap<String,Integer>();
	//记录跳转指令编号与他跳转到目的指令的编号
	static Map <Integer,Integer> jumpToaim = new HashMap<Integer,Integer>();
	//记录每条dex指令的编号和翻译成class指令后对应的编号
	static Map <Integer,Integer> dexToclass = new HashMap<Integer,Integer>();
	
	//linenember -> dex code number
	static Map <Integer,Integer> lineTonumber = new HashMap<Integer,Integer>();
	
	
	//遇到method清零     
	static int registerNumber = 0;
	static Map <String,Integer> register = new HashMap<String,Integer>();
	//默认为int
	static Map <String,String> registerType = new HashMap<String,String>();

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		String file = "G:\\实验室任务\\baksmail\\out\\com\\example\\test\\MainActivity.smali";
		ArrayList<String> instruction;
		
		String regex1 = "[p,v]\\d+";
		String regex2 = ";->";
		
		ReadFile rf = new ReadFile(file);
		translation tr = new translation();
		
		//每个方法的开始序号
		int method_begin_number = 0;
		
		int i = 0;
		while(rf.readLine()){
			
			if(rf.ifNull()){
				continue;
			}
			
			instruction = rf.getInstruction();
			if(rf.ifNewMethod()){
				registerNumber = 0;
				register.clear();
				registerType.clear();
				registerNumber = 1;
				register.put("p0",0);
				registerType.put("p0", "this");
				
				method_begin_number = LineNumber;
				methodName = instruction.get(instruction.size()-1);
			}
			else if(instruction.get(0).equals(".class")){
				className = instruction.get(instruction.size()-1);
			}
			else if(instruction.get(0).equals(".local")){
				String []temp1 = new String[2];
				temp1 = instruction.get(2).split(":");
				registerType.put(instruction.get(1), temp1[1]);
			}
			else if(instruction.get(0).equals(".param")){
				if(!register.containsKey(instruction.get(1))){
					register.put(instruction.get(1), registerNumber);
					registerType.put(instruction.get(1), instruction.get(4));
					registerNumber++;
				}
			}
			//处理标签:
			else if(instruction.get(0).startsWith(":")){
				tableAndnumber.put(instruction.get(0), LineNumber+1);
			}
			else if(instruction.get(0).equals(".line")){
				int ln = Integer.parseInt(instruction.get(1));
				lineTonumber.put(ln, LineNumber+1);
			}
			else if(rf.ifAnInstruction(instruction.get(0))){
				i=1;
				//分配栈空间
				if(i<instruction.size()){
					while(i<instruction.size() && instruction.get(i).matches(regex1)){
						if(register.containsKey(instruction.get(i))){
							i++;
						}
						else{
							register.put(instruction.get(i), registerNumber);
							registerType.put(instruction.get(i), "I");
							//System.out.println(registerNumber+": "+instruction.get(i));
							registerNumber++;
							i++;
						}
					}
					//记录跳转指令信息
					if(i<instruction.size() && instruction.get(i).startsWith(":")){
						jumpAndtable.put(LineNumber, instruction.get(i));
					}
				}
			}
			//方法结束时再统一处理指令
			else if(instruction.get(0).equals(".end")){
				while(method_begin_number < LineNumber){
					instruction = rf.getInstruction(method_begin_number);
					if(rf.ifAnInstruction(instruction.get(0))){
						//翻译指令，把指令编号也传进去
						tr.getInformation(instruction, LineNumber, className, methodName, register, registerType);
						tr.translateIns();
					}
					method_begin_number++;
				}
			}
			
			LineNumber++;
		}
	}
}
