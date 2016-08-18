package instructions;

import op.Register;
import op.globalArguments;

import java.util.ArrayList;

/*Dalvik指令集中有一条指令用来抛出异常。
“throw vAA”抛出vAA寄存器中指定类型的异常。*/
public class _throw extends Instruction {

    @Override
    public void analyze(String[] dexCodes) {
        super.analyze(dexCodes);
        Register firstRegister = globalArguments.registerQueue.getByDexName(dexCodes[1]);
        switch (dexCodes[0]) {
            case "throw" :
            	globalArguments.finalByteCode.add("aload" + " " + firstRegister.stackNum);//将异常压入栈顶
            	globalArguments.finalByteCode.add("athrow");//抛出栈顶异常
            	globalArguments.finalByteCodePC += 2;
                break;
        }
    }

    @Override
    public boolean ifUpgrade(ArrayList<String> dexCode, int lineNum) {
        Register firstRegister = globalArguments.registerQueue.getByDexName(dexCode.get(1));
        firstRegister.updateType(lineNum, firstRegister.currentType);
        return true;
    }
}
