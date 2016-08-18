package instructions;

import op.Register;
import op.globalArguments;

import java.util.ArrayList;
/*方法调用指令
方法调用指令负责调用类实例的方法。它的基础指令为 invoke，方法调用指令有“invoke-kind {vC, vD, vE, vF, vG},meth@BBBB”
与“invoke-kind/range {vCCCC  .. vNNNN},meth@BBBB”两类，两类指令在作用上并无不同，只是后者在设置参数寄存器时使用了range来指定寄存器的范围。
根据方法类型的不同，共有如下五条方法调用指令：
“invoke-virtual” 或 “invoke-virtual/range”调用实例的虚方法。
“invoke-super”或"invoke-super/range"调用实例的父类方法。
“invoke-direct”或“invoke-direct/range”调用实例的直接方法。
“invoke-static”或“invoke-static/range”调用实例的静态方法。
“invoke-interface”或“invoke-interface/range”调用实例的接口方法。
在Android4.0系统中，Dalvik指令集中增加了“invoke-kind/jumbo {vCCCC  .. vNNNN},meth@BBBBBBBB”这类指令，
它与上面介绍的两类指令作用相同，只是在指令中增加了jumbo字节码后缀，且寄存器值与指令的索引取值范围更大。*/
public class _invoke extends Instruction {

    @Override
    public void analyze(String[] dexCodes) {
    	super.analyze(dexCodes);
    	//先提取数据到操作数栈
    	int i = 1;
    	for(i=1; i<dexCodes.length-1;i++){
    		Register register = globalArguments.registerQueue.getByDexName(dexCodes[i]);
    		String dataType = register.getType(globalArguments.dexCodeNumber).toLowerCase();
    		if(dataType.equals("j")) {
    			dataType = "l";
            }
    		if(dataType.equals("d") || dataType.equals("f") || dataType.equals("l")) {
                globalArguments.finalByteCode.add(dataType + "load" + " " + register.stackNum);
            }
            else if(dataType.equals("i") || dataType.equals("b") || dataType.equals("s") || dataType.equals("c")){
                globalArguments.finalByteCode.add("iload" + " " + register.stackNum);
            }
            else{
            	globalArguments.finalByteCode.add("aload" + " " + register.stackNum);
            }
    		globalArguments.finalByteCodePC++;
    	}
    	
    	//翻译成invoke指令
    	if(dexCodes[0].contains("virtual")){
    		globalArguments.finalByteCode.add("invokevirtual"+" "+globalArguments.className.replace(";", ".")+globalArguments.methodName.replace("(", ":("));
    	}
    	else if(dexCodes[0].contains("static")){
    		globalArguments.finalByteCode.add("invokestatic"+" "+globalArguments.className.replace(";", ".")+globalArguments.methodName.replace("(", ":("));
    	}
    	else if(dexCodes[0].contains("interface")){
    		globalArguments.finalByteCode.add("invokeinterface"+" "+globalArguments.className.replace(";", ".")+globalArguments.methodName.replace("(", ":("));
    	}
    	else{
    		globalArguments.finalByteCode.add("invokespecial"+" "+globalArguments.className.replace(";", ".")+globalArguments.methodName.replace("(", ":("));
    	}
    	globalArguments.finalByteCodePC++;
    }

    @Override
    public boolean ifUpgrade(ArrayList<String> dexCode, int lineNum) {
        //先求出所有参数的类型，保存在regTypes中，最后再依次赋值
        ArrayList<String> regTypes = new ArrayList<>();
        regTypes = getRegType(dexCode);
        //为寄存器分配类型
        int i = 1;
        for (String aRegType : regTypes) {
            Register register1 = globalArguments.registerQueue.getByDexName(dexCode.get(i++));
            register1.updateType(lineNum, aRegType);
            //long double 为两个连续寄存器赋类型
            if (aRegType.equals("J") || aRegType.equals("D")) {
                Register register2 = globalArguments.registerQueue.getByDexName(dexCode.get(i++));
                register2.updateType(lineNum, aRegType);
            }
        }
        return true;
    }

    public ArrayList<String> getRegType(ArrayList<String> dexCode){
        ArrayList<String> regTypes = new ArrayList<>();
        String[] temp = dexCode.get(dexCode.size() - 1).split(";->");
        regTypes.add(temp[0]);//默认this的类型
        String types = temp[1].substring(temp[1].indexOf("(") + 1, temp[1].indexOf(")") + 1);//括号中参数的类型 example: ILstring;D)
        while (!types.equals(")")) {
            if (types.startsWith("L")) {
                regTypes.add(types.substring(0, types.indexOf(";") + 1));
                types = types.substring(types.indexOf(";"));
            } else if (types.startsWith("[")) {
                String tempType = "";
                do {
                    tempType += "[";
                    types = types.substring(1);
                }while (types.startsWith("["));
                if((types.charAt(0) + "").equals("L")) {
                    regTypes.add(tempType + types.substring(0, types.indexOf(";") + 1));
                    types += ";";
                }
                else {
                    regTypes.add(tempType + types.charAt(0));
                }
            } else {
                regTypes.add(types.charAt(0) + "");
            }
            types = types.substring(1);
        }
        return regTypes;
    }
    
}
