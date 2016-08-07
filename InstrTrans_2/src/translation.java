import java.util.ArrayList;
import java.util.Map;

/*
 * 特别注意跳转指令。
 * 每条指令都记录下来他翻译后的起始编号，跳转指令先记录下对应的原指令号，最后全翻译完之后再根据原指令号求出跳转位置
 * 
 *方法调用时都会先使用aload_0将this参数放入操作数栈
 * 
 * */
public class translation {
	//存放指令的信息
	ArrayList<String> instruction; 
	int insNumber = 0; //dex指令编号
	String className = "";
	String methodName = "";
	//寄存器 -> 栈编号
	Map <String,Integer> register;
	//寄存器类型
	Map <String,String> registerType;
	
	//java字节码指令编号
	int classCodeNumber = 0;
	
	//-1?
	String []i6 = new String[]{"0x0","0x1","0x2","0x3","0x4","0x5"};
	
	//获取信息
	public void getInformation(ArrayList<String> i, int in,String cn, String mn, Map <String,Integer> r, Map <String,String> rt){
		instruction = i;
		insNumber = in;
		className = cn;
		methodName = mn;
		register = r;
		registerType = rt;
	}
	
	//翻译
	public void translateIns(){
		switch(instruction.get(0)){
			case "nop":
				Main.dexToclass.put(insNumber, classCodeNumber);
				System.out.println(classCodeNumber+": "+"nop");
				classCodeNumber++;
				break;
			//const 应该只有int型            /16 扩展在smail文件里处理?
			case "const/4":
			case "const/16":
			case "const/high16":
			case "const":
				Main.dexToclass.put(insNumber, classCodeNumber);
				char []tempchar = instruction.get(2).toCharArray();
				int tempint = 0;
				for(int i=2;i<tempchar.length;i++){
					tempint = tempint*10 + (tempchar[i] - '0');
				}
				if(tempint < 6){
					System.out.println(classCodeNumber+": "+"iconst_"+tempint);
					classCodeNumber++;
				}
				else{
					System.out.println(classCodeNumber+": "+"ldc            "+instruction.get(2));
					classCodeNumber++;
				}
				if(register.get(instruction.get(1)) < 4){
					System.out.println(classCodeNumber+": "+"istore_"+register.get(instruction.get(1)));
					classCodeNumber++;
				}
				else{
					System.out.println(classCodeNumber+": "+"istore         "+register.get(instruction.get(1)));
					classCodeNumber++;
				}
				break;
				
			case "const-wide/4":
			case "const-wide/16":
			case "const-wide/32":
			case "const-wide/high16":
			case "const-wide":
				System.out.println(classCodeNumber+": "+"ldc2_w         "+instruction.get(2));
				classCodeNumber++;
				if(register.get(instruction.get(1)) < 4){
					System.out.println(classCodeNumber+": "+"istore_"+register.get(instruction.get(1)));
					classCodeNumber++;
				}
				else{
					System.out.println(classCodeNumber+": "+"istore         "+register.get(instruction.get(1)));
					classCodeNumber++;
				}
				break;
			
			case "const-string":
				System.out.println(classCodeNumber+": "+"ldc            "+instruction.get(2));
				classCodeNumber++;
				if(register.get(instruction.get(1)) < 4){
					System.out.println(classCodeNumber+": "+"istore_"+register.get(instruction.get(1)));
					classCodeNumber++;
				}
				else{
					System.out.println(classCodeNumber+": "+"istore         "+register.get(instruction.get(1)));
					classCodeNumber++;
				}
				break;
			//宽索引？？？
			case "const-string/jumbo":
				System.out.println(classCodeNumber+": "+"ldc_w          "+instruction.get(2));
				classCodeNumber++;
				if(register.get(instruction.get(1)) < 4){
					System.out.println(classCodeNumber+": "+"istore_"+register.get(instruction.get(1)));
					classCodeNumber++;
				}
				else{
					System.out.println(classCodeNumber+": "+"istore         "+register.get(instruction.get(1)));
					classCodeNumber++;
				}
				break;
				
			case "const-class":
				break;
			case "const-class/jumbo":
				break;
			
			default:
				break;
		}
	}
	
}
