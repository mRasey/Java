package instructions;

import op.Register;
import op.globalArguments;

import static op.globalArguments.dexCodeNumber;

/*数据定义指令
数据定义指令用来定义程序中用到的常量，字符串，类等数据。它的基础字节码为const。
“const/4 vA, #+B”：将数值符号扩展为32位后赋给寄存器vA。
“const/16 vAA, #+BBBB”：将数据符号扩展为32位后赋给寄存器vAA。
“const vAA, #+BBBBBBBB”：将数值赋给寄存器vAA。
“const/high16 vAA, #+BBBB0000“：将数值右边零扩展为32位后赋给寄存器vAA。
“const-wide/16 vAA, #+BBBB”：将数值符号扩展为64位后赋给寄存器对vAA。
“const-wide/32 vAA, #+BBBBBBBB”：将数值符号扩展为64位后赋给寄存器对vAA。
“const-wide vAA, #+BBBBBBBBBBBBBBBB”：将数值赋给寄存器对vAA。
“const-wide/high16 vAA, #+BBBB000000000000”：将数值右边零扩展为64位后赋给寄存器对vAA。
“const-string vAA, string@BBBB”：通过字符串索引构造一个字符串并赋给寄存器vAA。
“const-string/jumbo vAA, string@BBBBBBBB”：通过字符串索引（较大）构造一个字符串并赋给寄存器vAA。
“const-class vAA, type@BBBB”：通过类型索引获取一个类引用并赋给寄存器vAA。
“const-class/jumbo vAAAA, type@BBBBBBBB”：通过给定的类型索引获取一个类引用并赋给寄存器vAAAA。
                                            这条指令占用两个字节，值为0xooff（Android4.0中新增的指令）。*/
public class _const extends Instruction{

    @Override
    public void analyze(String[] dexCodes) {
        super.analyze(dexCodes);
    	String newInstr;
    	//保存dex->class
        Register firstRegister = globalArguments.registerQueue.getByDexName(dexCodes[1]);
    	
        switch (dexCodes[0]){
        	//char 应该放在哪里？？？
            case "const/4" :
            case "const/16" :
            case "const" :
            case "const/high16" :
            	switch(firstRegister.getType(dexCodeNumber)){
            		case "I":
            			globalArguments.finalByteCode.add("ldc"+" "+ dexCodes[2]);
            			globalArguments.finalByteCode.add("istore" + " " + firstRegister.stackNum);
            			globalArguments.finalByteCodePC += 2;
            			break;
            		case "F":
            			globalArguments.finalByteCode.add("ldc"+" "+dexCodes[2]);
            			globalArguments.finalByteCode.add("fstore" + " " + firstRegister.stackNum);
            			globalArguments.finalByteCodePC += 2;
            			break;
            		case "S":
            			globalArguments.finalByteCode.add("sipush"+" "+dexCodes[2]);
            			globalArguments.finalByteCode.add("istore" + " " + firstRegister.stackNum);
            			globalArguments.finalByteCodePC += 2;
            			break;
            		case "B":
            		case "C":
            			globalArguments.finalByteCode.add("bipush"+" "+dexCodes[2]);
            			globalArguments.finalByteCode.add("istore" + " " + firstRegister.stackNum);
            			globalArguments.finalByteCodePC += 2;
            			break;
            		default:
            			System.err.println("error:");
            			for(int i=0; i<dexCodes.length;i++){
            				System.err.print(dexCodes[i]+" ");
            			}
            			break;
            	}
            	break;
            case "const-wide/16" :
            case "const-wide/32" :
            case "const-wide" :
            case "const-wide/high16" :
            	switch(firstRegister.getType(dexCodeNumber)){
            		case "J":
            			globalArguments.finalByteCode.add("ldc2_w"+" "+dexCodes[2]);
            			globalArguments.finalByteCode.add("lstore" + " " + firstRegister.stackNum);
            			globalArguments.finalByteCodePC += 2;
            			break;
            		case "D":
            			globalArguments.finalByteCode.add("ldc2_w"+" "+dexCodes[2]);
            			globalArguments.finalByteCode.add("dstore" + " " + firstRegister.stackNum);
            			globalArguments.finalByteCodePC += 2;
            			break;
            		default:
            			System.err.println("error:");
            			for(int i=0; i<dexCodes.length;i++){
            				System.err.print(dexCodes[i]+" ");
            			}
            			break;
            	}
            	
            case "const-string" :
            case "const-string/jumbo" :
                globalArguments.finalByteCode.add("ldc" + " " + dexCodes[2]);
    			globalArguments.finalByteCode.add("astore" + " " + firstRegister.stackNum);
    			globalArguments.finalByteCodePC += 2;
            	break;
            	
            //类的引用不确定怎么翻译？
            case "const-class" :
            case "const-class/jumbo" :
                globalArguments.finalByteCode.add("ldc" + " " + dexCodes[2]);
    			globalArguments.finalByteCode.add("astore" + " " + firstRegister.stackNum);
    			globalArguments.finalByteCodePC += 2;
            	break;
        }
    }
}
