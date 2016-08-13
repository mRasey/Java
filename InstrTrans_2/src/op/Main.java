package op;

import instructions.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import op.globalArguments;
/*
* 在翻译指令时：
* 1.遇到特殊指令，能确定某个寄存器类型时要及时修改registerType
* */


public class Main {

   
//	public static Map <String,Integer> register = new HashMap<>();
	//默认为int
//	public static Map <String,String> registerType = new HashMap<>();

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		
        String byteCodeSavePath = "C:\\Users\\Billy\\Documents\\result.txt";

		ArrayList<String> instruction;
		
		String regex1 = "[p,v]\\d+";
		String regex2 = ";->";
		
		
//		translation tr = new translation();
		
		//每个方法的开始序号
		int method_begin_number = 0;
		
		int i = 0;
		while(globalArguments.rf.readLine()){
			
			if(globalArguments.rf.ifNull()){
				continue;
			}
			
			instruction = globalArguments.rf.getInstruction();
			
			for(int j=0;j<instruction.size();j++){
				System.out.print(instruction.get(j)+" ");
			}
			System.out.println(" ");
			
			
			if(globalArguments.rf.ifNewMethod()){
				globalArguments.stackNumber = 0;
				globalArguments.arrayData.clear();
				globalArguments.registerQueue.clear();
				globalArguments.registerQueue.addNewRegister(new Register("p0", "this", globalArguments.stackNumber++));

				method_begin_number = globalArguments.LineNumber;
				globalArguments.methodName = instruction.get(instruction.size()-1);
			}
			else if(instruction.get(0).equals(".class")){
				globalArguments.className = instruction.get(instruction.size()-1);
			}
			/*else if(instruction.get(0).equals(".local")){
				String []temp1 = new String[2];
				temp1 = instruction.get(2).split(":");
				registerType.put(instruction.get(1), temp1[1]);
			}*/
			else if(instruction.get(0).equals(".param")){
				if(globalArguments.registerQueue.getByDexName(instruction.get(1)) == null){
					globalArguments.registerQueue.addNewRegister(new Register(instruction.get(1), instruction.get(4), globalArguments.stackNumber++, globalArguments.LineNumber));
				}
			}
			else if(globalArguments.rf.ifAnInstruction(instruction.get(0))){
				i=1;
				//分配栈空间
				if(i<instruction.size()){
					while(i<instruction.size() && instruction.get(i).matches(regex1)){
						if(globalArguments.registerQueue.getByDexName(instruction.get(i)) != null){
							i++;
						}
						else{
							globalArguments.registerQueue.addNewRegister(new Register(instruction.get(1), null, globalArguments.stackNumber++));
							i++;
						}
					}
					//记录跳转指令信息
					if(i<instruction.size() && instruction.get(i).startsWith(":")){
						globalArguments.jumpAndTab.put(globalArguments.LineNumber, instruction.get(i));
					}
				}
			}
			//方法结束时再统一处理指令
			else if(instruction.get(0).equals(".end") && instruction.get(1).equals("method")){
                int temp = method_begin_number;
				while(method_begin_number < globalArguments.LineNumber){
					instruction = globalArguments.rf.getInstruction(method_begin_number);
					if(globalArguments.rf.ifAnInstruction(instruction.get(0))){
                        if(instruction.get(0).contains("invoke"))
                            new _invoke().ifUpgrade(instruction,method_begin_number);
                        else if(instruction.get(0).contains("move"))
                            new _move().ifUpgrade(globalArguments.rf.getInstruction(method_begin_number-1),instruction,method_begin_number);
                        else if(instruction.get(0).contains("return"))
                            new _return().ifUpgrade(instruction,method_begin_number);
                        else if(instruction.get(0).contains("to"))
                            new _neg_not_to().ifUpgrade(instruction,method_begin_number);
                        else if(instruction.get(0).contains("instance-of"))
                            new _instance().ifUpgrade(instruction, method_begin_number);
                        else {
                            ArrayList<String> nextInstr = new ArrayList<>();
                            nextInstr = globalArguments.rf.getInstruction(method_begin_number + 2);
                            if(nextInstr.get(0).equals(".local")) {
                                Register register = globalArguments.registerQueue.getByDexName(nextInstr.get(1));
                                register.updateType(method_begin_number,
                                        nextInstr.get(2).substring(nextInstr.get(2).lastIndexOf(":") + 1));
                            }
                            else {
                                int j = 0;
                                while(j<instruction.size() && instruction.get(j).matches(regex1)){
                                    Register register = globalArguments.registerQueue.getByDexName(instruction.get(j));
                                    register.updateType(method_begin_number, register.currentType);
                                }
                            }
                        }
					}
					//处理标签: 和获取数组数据
					else if(instruction.get(0).startsWith(":")){
						ArrayList<String> nextDexCode = globalArguments.rf.getInstruction(method_begin_number+1);
						//处理数组数据
						if(nextDexCode.get(0).equals(".array-data")){
							//标签
							String tab = instruction.get(0);
							ArrayList<String> ad = new ArrayList<>();
							method_begin_number += 2;
							nextDexCode = globalArguments.rf.getInstruction(method_begin_number);
							while(!nextDexCode.get(0).equals(".end")){
								ad.add(nextDexCode.get(0));
								method_begin_number++;
								nextDexCode = globalArguments.rf.getInstruction(method_begin_number);
							}
							globalArguments.arrayData.put(tab, ad);
							
						}
                        //处理switch数据
                        else if(nextDexCode.get(0).equals(".packed-switch")
                                || nextDexCode.get(0).equals(".sparse-switch")) {
                            String tab = instruction.get(0);
                            String type = nextDexCode.get(0);//跳转类型
                            ArrayList<String> data = new ArrayList<>();
                            method_begin_number += 2;
                            nextDexCode = globalArguments.rf.getInstruction(method_begin_number);
                            while(!nextDexCode.get(0).equals(".end")){
                                if(type.contains("packed"))
                                    data.add(nextDexCode.get(0));
                                else
                                    data.add(nextDexCode.get(2));
                                method_begin_number++;
                                nextDexCode = globalArguments.rf.getInstruction(method_begin_number);
                            }
                            globalArguments.switchData.put(tab, data);
                        }
						else{
                            globalArguments.tabAndNextInstr.put(instruction.get(0), globalArguments.LineNumber + 1);
                        }

					}
					//处理.line
					else if(instruction.get(0).equals(".line")){
						int ln = Integer.parseInt(instruction.get(1));
						//下条是指令
						if(globalArguments.rf.ifAnInstruction(globalArguments.rf.getInstruction(method_begin_number+1).get(0))){
							globalArguments.lineToNumber.put(ln, globalArguments.LineNumber+1);
						}
						//下条不是指令
						else{
							globalArguments.lineToNumber.put(ln, globalArguments.LineNumber+2);
						}
					}
					method_begin_number++;
				}
                method_begin_number = temp;
                while(method_begin_number < globalArguments.LineNumber){
                    instruction = globalArguments.rf.getInstruction(method_begin_number);
                    if(globalArguments.rf.ifAnInstruction(instruction.get(0))){
                        new translation(instruction, method_begin_number).translateIns();//翻译指令，把指令编号也传进去
                    }
                    method_begin_number++;
                }
			}
			
			globalArguments.LineNumber++;
		}

        System.out.println("\n\n\nfinalByteCode.size()  " +  globalArguments.finalByteCode.size());
        File byteCodeFile = new File(byteCodeSavePath);
        FileWriter fileWriter = new FileWriter(byteCodeFile);
        for(String s : globalArguments.finalByteCode) {
            fileWriter.write(s + "\n");
            System.out.println(s);
        }
	}
}
