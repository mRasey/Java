package instructions;

import op.Register;
import op.globalArguments;

import java.util.ArrayList;

import static op.globalArguments.dexCodeNumber;
import static op.globalArguments.registerQueue;

/*数据操作指令
数据操作指令为move。move指令的原型为“move destination,source”，move指令根据字节码的大小与类型不同，后面会跟上不同的后缀。
“move vA, vB”：将vB寄存器的值赋给vA寄存器，源寄存器与目的寄存器都为4位。
“move/from16 vAA, vBBBB”：将vBBBB寄存器的值赋给vAA寄存器，源寄存器为16位，目的寄存器为8位。
“move/16 vAAAA, vBBBB”：将vBBBB寄存器的值赋给vAAAA寄存器，源寄存器与目的寄存器都为16位。
“move-wide vA, vB”：为4位的寄存器对赋值。源寄存器与目的寄存器都为4位。
“move-wide/from16 vAA, vBBBB”与“move-wide/16 vAAAA, vBBBB”实现与“move-wide”相同。
“move-object vA, vB”：为对象赋值。源寄存器与目的寄存器都为4位。
“move-object/from16 vAA, vBBBB”：为对象赋值。源寄存器为16位，目的寄存器为8位。
“move-object/16 vAA, vBBBB”：为对象赋值。源寄存器与目的寄存器都为16位。
“move-result vAA”：将上一个invoke类型指令操作的单字非对象结果赋给vAA寄存器。
“move-result-wide vAA”：将上一个invoke类型指令操作的双字非对象结果赋给vAA寄存器。
“move-result-object vAA"：将上一个invoke类型指令操作的对象结果赋给vAA寄存器。
“move-exception vAA”：保存一个运行时发生的异常到vAA寄存器，这条指令必须是异常发生时的异常处理器的一条指令。否则的话，指令无效。*/
public class _move extends Instruction {

    @Override
    public void analyze(String[] dexCodes) {
		super.analyze(dexCodes);
//    	String newInstr;
    	//保存dex->class
        Register firstRegister = registerQueue.getByDexName(dexCodes[1]);
        Register secondRegister = null;
        if(dexCodes.length == 3)
            secondRegister = registerQueue.getByDexName(dexCodes[2]);
        String firstDataType = firstRegister.getType(dexCodeNumber).toLowerCase();
        String secondDataType = secondRegister.getType(dexCodeNumber).toLowerCase();
        if(firstDataType.equals("j")) {
            firstDataType = "l";
        }

        if(dexCodes[0].contains("exception") || dexCodes[0].contains("move-result-object")) {
        	globalArguments.finalByteCode.add("astore" + " " + firstRegister.stackNum);
        	globalArguments.finalByteCodePC++;
        }
        else if(dexCodes[0].contains("object")) {
        	globalArguments.finalByteCode.add("aload" + " " + secondRegister.stackNum);
        	globalArguments.finalByteCode.add("astore" + " " +firstRegister.stackNum);
        	globalArguments.finalByteCodePC += 2;
        }
        else if(dexCodes[0].contains("result")) {
            if(firstDataType.equals("d") || firstDataType.equals("f") || firstDataType.equals("l"))
            	globalArguments.finalByteCode.add(firstDataType + "store" + " " + firstRegister.stackNum);
            else
            	globalArguments.finalByteCode.add("istore" + " " + firstRegister.stackNum);
            globalArguments.finalByteCodePC++;
        }
        else {
            switch (firstDataType) {
                case "d":
                case "f":
                case "l":
                	globalArguments.finalByteCode.add(firstDataType + "load" + " " + secondRegister.stackNum);
                	globalArguments.finalByteCode.add(firstDataType + "store" + " " + firstRegister.stackNum);
                    break;
                case "b":
                case "s":
                case "c":
                case "i":
                	globalArguments.finalByteCode.add("iload" + " " + secondRegister.stackNum);
                	globalArguments.finalByteCode.add("istore" + " " + firstRegister.stackNum);
                    break;
                default:
                	globalArguments.finalByteCode.add("aload" + " " + secondRegister.stackNum);
                	globalArguments.finalByteCode.add("astore" + " " + firstRegister.stackNum);
                    break;
            }
            globalArguments.finalByteCodePC += 2;
        }

        /*switch (dexCodes[0]){
            case "move" :
            case "move/16" :
            case "move/from16" :
            	switch(firstRegister.getType(dexCodeNumber)){
            		//这几个都用的是i
            		case "B":
            		case "S":
            		case "C":
            		case "I":
                        newInstr = "iload" + " " + dexCodes[2];
                        globalArguments.finalByteCode.add(newInstr);
            			globalArguments.finalByteCodePC++;
        			
            			newInstr = "istore" + " " + firstRegister.stackNum;
            			globalArguments.finalByteCode.add(newInstr);
            			globalArguments.finalByteCodePC++;
            			break;
            		case "F":
            			newInstr = "fload"+" "+ dexCodes[2];
            			globalArguments.finalByteCode.add(newInstr);
            			globalArguments.finalByteCodePC++;
        			
            			newInstr = "fstore" + " " + firstRegister.stackNum;
            			globalArguments.finalByteCode.add(newInstr);
            			globalArguments.finalByteCodePC++;
            			break;
            		default:
            			System.err.println("error:");
            			for(int i=0; i<dexCodes.length;i++){
            				System.err.print(dexCodes[i]+" ");
            			}
            			break;
            	}
            	break;
            	
            case "move-wide" :
            case "move-wide/from16" :
            	switch(firstRegister.getType(dexCodeNumber)){
        			case "J":
                        newInstr = "lload" + " " + dexCodes[2];
                        globalArguments.finalByteCode.add(newInstr);
        				globalArguments.finalByteCodePC++;
        			
        				newInstr = "lstore" + " " + firstRegister.stackNum;
        				globalArguments.finalByteCode.add(newInstr);
        				globalArguments.finalByteCodePC++;
        				break;
        			case "D":
                        newInstr = "dload" + " " + dexCodes[2];
                        globalArguments.finalByteCode.add(newInstr);
        				globalArguments.finalByteCodePC++;
        			
        				newInstr = "dstore" + " " + firstRegister.stackNum;
        				globalArguments.finalByteCode.add(newInstr);
        				globalArguments.finalByteCodePC++;
        				break;
        			default:
        				System.err.println("error:");
        				for(int i=0; i<dexCodes.length;i++){
        					System.err.print(dexCodes[i]+" ");
        				}
        				break;
            		}
            	break;
            
            //这几个用的是引用
            case "move-object" :
            case "move-object/16" :
            case "move-object/from16" :
            	newInstr = "aload"+" "+dexCodes[2];
    			globalArguments.finalByteCode.add(newInstr);
    			globalArguments.finalByteCodePC++;
    			
    			newInstr = "astore" + " " + firstRegister.stackNum;
    			globalArguments.finalByteCode.add(newInstr);
    			globalArguments.finalByteCodePC++;
    			break;
            	
            case "move-result" :
            	switch(firstRegister.getType(dexCodeNumber)){
            		case "B":
            		case "S":
            		case "C":
            		case "I":
            			newInstr = "istore" + " " + firstRegister.stackNum;
            			globalArguments.finalByteCode.add(newInstr);
            			globalArguments.finalByteCodePC++;
            			break;
            		case "F":
            			newInstr = "fstore" + " " + firstRegister.stackNum;
            			globalArguments.finalByteCode.add(newInstr);
            			globalArguments.finalByteCodePC++;
            			break;
            		default:
            			System.err.println("error:");
            			for(int i=0; i<dexCodes.length;i++){
            				System.err.print(dexCodes[i]+" ");
            			}
            			break;
            	}
            	break;
            	
            case "move-result-wide" :
            	switch(firstRegister.getType(dexCodeNumber)){
        		case "J":
        			newInstr = "lstore" + " " + firstRegister.stackNum;
        			globalArguments.finalByteCode.add(newInstr);
        			globalArguments.finalByteCodePC++;
        			break;
        		case "D":
        			newInstr = "dstore" + " " + firstRegister.stackNum;
        			globalArguments.finalByteCode.add(newInstr);
        			globalArguments.finalByteCodePC++;
        			break;
        		default:
        			System.err.println("error:");
        			for(int i=0; i<dexCodes.length;i++){
        				System.err.print(dexCodes[i]+" ");
        			}
        			break;
            	}
            	break;
        			
            case "move-result-object" :
            case "move-exception" :
            	newInstr = "astore" + " " + firstRegister.stackNum;
    			globalArguments.finalByteCode.add(newInstr);
    			globalArguments.finalByteCodePC++;
    			break;
        }*/
    }

    @Override
    public boolean ifUpgrade(ArrayList<String> firstDexCode, ArrayList<String> secondDexCode, int lineNum) {
        String methodInf = firstDexCode.get(firstDexCode.size()-1);
    	String dataType = methodInf.substring(methodInf.indexOf(")"));
    	
    	Register register = globalArguments.registerQueue.getByDexName(secondDexCode.get(1));
    	register.updateType(lineNum, dataType);
    	
    	return true;
    }
}
