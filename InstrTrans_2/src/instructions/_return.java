package instructions;

import op.Register;
import op.globalArguments;

import java.util.ArrayList;


/* 返回指令
返回指令指的是函数结尾时运行的最后一条指令。它的基础字节码为teturn，共有以下四条返回指令：
 "return-void"：表示函数从一个void方法返回。
“return vAA”：表示函数返回一个32位非对象类型的值，返回值寄存器为8位的寄存器vAA。
“return-wide vAA”：表示函数返回一个64位非对象类型的值，返回值为8位的寄存器对vAA。
“return-object vAA”：表示函数返回一个对象类型的值。返回值为8位的寄存器vAA。*/
public class _return extends Instruction {

    @Override
    public void analyze(String[] dexCodes) {
        super.analyze(dexCodes);
        Register firstRegister = globalArguments.registerQueue.getByDexName(dexCodes[1]);

        switch (dexCodes[0]){
            case "return" :
            case "return-wide" :
                String dataType = firstRegister.getType(globalArguments.dexCodeNumber);//获取数据类型首字母
                if(dataType.equals("j"))
                    dataType = "l";
                if(dataType.equals("l") || dataType.equals("f") || dataType.equals("d")){
                	globalArguments.finalByteCode.add(dataType + "load" + " " + firstRegister.stackNum);
                	globalArguments.finalByteCode.add(dataType + "return");
                }
                else {
                	globalArguments.finalByteCode.add("aload" + " " + firstRegister.stackNum);
                	globalArguments.finalByteCode.add("ireturn");
                }
                globalArguments.finalByteCodePC += 2;
                break;
            case "return-void" :
            	globalArguments.finalByteCode.add("return");
            	globalArguments.finalByteCodePC++;
                break;
            case "return-object" :
            	globalArguments.finalByteCode.add("aload" + " " + firstRegister.stackNum);
            	globalArguments.finalByteCode.add("areturn");
            	globalArguments.finalByteCodePC += 2;
                break;
        }
    }

    @Override
    public boolean ifUpgrade(ArrayList<String> dexCode, int lineNum) {
    	if(dexCode.size() > 1){
    		String methodInf = globalArguments.methodName;
    	    String dataType = methodInf.substring(methodInf.indexOf(")"));
    	    	
    	    Register register = globalArguments.registerQueue.getByDexName(dexCode.get(1));
    	    register.updateType(lineNum, dataType);
    	    return true;
    	}
        return false;
    }
}
