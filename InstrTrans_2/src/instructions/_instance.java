package instructions;

import op.Register;
import op.globalArguments;

import java.util.ArrayList;

/*实例操作指令
与实例相关的操作包括实例的类型转换，检查及新建等：
“instance-of vA, vB, type@CCCC”：判断vB寄存器中的对象引用是否可以转换成指定的类型，如果可以vA寄存器赋值为1，否则vA寄存器赋值为0。
“new-instance vAA, type@BBBB”：构造一个指定类型对象的新实例，并将对象引用赋值给vAA寄存器，类型符type指定的类型不能是数组类。
“instance-of/jumbo vAAAA, vBBBB, type@CCCCCCCC”：指令功能与“instance-of vA, vB, type@CCCC”相同，只是寄存器值与指令的索引取值范围更大（Android4.0中新增的指令）。
“new-instance/jumbo vAAAA, type@BBBBBBBB”：指令功能与“new-instance vAA, type@BBBB”相同，只是寄存器值与指令的索引取值范围更大（Android4.0中新增的指令）。*/
public class _instance extends Instruction {

    @Override
    public void analyze(String[] dexCodes) {
        switch (dexCodes[0]) {
            case "instance-of" :
            case "instance-of/jumbo" :
            	Register register1 = globalArguments.registerQueue.getByDexName(dexCodes[1]);
            	Register register2 = globalArguments.registerQueue.getByDexName(dexCodes[2]);
                globalArguments.finalByteCode.add("aload" + " " + register2.stackNum);
            	globalArguments.finalByteCode.add("instanceof" + " " + dexCodes[3]);
            	globalArguments.finalByteCode.add("istore" + " " + register1.stackNum);
                globalArguments.finalByteCodePC += 3;

            case "new-instance" :
            case "new-instance/jumbo" :
            	Register register = globalArguments.registerQueue.getByDexName(dexCodes[1]);
            	globalArguments.finalByteCode.add("new" + " " + dexCodes[2]);
            	globalArguments.finalByteCode.add("astore" + " " +register.stackNum);
                globalArguments.finalByteCodePC += 2;

        }
    }

    @Override
    public boolean ifUpgrade(ArrayList<String> dexCode, int lineNum) {
        if(dexCode.get(1).equals(dexCode.get(2))) {
            globalArguments.registerQueue.getByDexName(dexCode.get(1)).currentType = "Z";
        }
        else {
            globalArguments.registerQueue.getByDexName(dexCode.get(1)).updateType(lineNum, "Z");
        }
        return super.ifUpgrade(dexCode, lineNum);
    }
}
