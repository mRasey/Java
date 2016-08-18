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
    	//保存dex->class
        Register firstRegister = registerQueue.getByDexName(dexCodes[1]);
        Register secondRegister = null;
        String firstDataType = firstRegister.getType(dexCodeNumber).toLowerCase();
        String secondDataType = null;
        if(dexCodes.length == 3){
        	secondRegister = registerQueue.getByDexName(dexCodes[2]);
        	secondDataType = secondRegister.getType(dexCodeNumber).toLowerCase();
        }
            
        
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
                case "z":
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
    }

    @Override
    public boolean ifUpgrade(ArrayList<String> firstDexCode, ArrayList<String> secondDexCode, int lineNum) {
        if(secondDexCode.get(0).contains("result")){
            Register firstRegister = globalArguments.registerQueue.getByDexName(secondDexCode.get(1));

            if(firstDexCode.get(0).contains("invoke")){
                String methodInf = firstDexCode.get(firstDexCode.size()-1);
                String dataType = methodInf.substring(methodInf.indexOf(")")+1);
                firstRegister.updateType(lineNum, dataType);
            }
            else if(firstDexCode.get(0).contains("filled-new-array")){
                String arrayType = firstDexCode.get(firstDexCode.size()-1);
                String dataType = arrayType.substring(arrayType.indexOf("[") + 1);
                firstRegister.updateType(lineNum, dataType);
            }

        }
        //异常类型不确定？？？
        else if(secondDexCode.get(0).contains("exception")){
            Register firstRegister = globalArguments.registerQueue.getByDexName(secondDexCode.get(1));
        }
        else{
            Register firstRegister = globalArguments.registerQueue.getByDexName(secondDexCode.get(1));
            Register secondRegister = globalArguments.registerQueue.getByDexName(secondDexCode.get(2));

            firstRegister.updateType(lineNum, secondRegister.currentType);
            secondRegister.updateType(lineNum, secondRegister.currentType);
        }
        return true;
    }
}
