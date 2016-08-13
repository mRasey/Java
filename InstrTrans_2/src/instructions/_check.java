package instructions;

import op.globalArguments;
/*实例操作指令
与实例相关的操作包括实例的类型转换，检查及新建等：
“check-cast vAA, type@BBBB”：将vAA寄存器中的对象引用转换成指定的类型，如果失败会抛出ClassCastException异常。如果类型B指定的是基本类型，对于非基本类型的A来说，运行时始终会失败。
“check-cast/jumbo vAAAA, type@BBBBBBBB”：指令功能与“check-cast vAA, type@BBBB”相同，只是寄存器值与指令的索引取值范围更大（Android4.0中新增的指令）。*/
public class _check extends Instruction {



    @Override
    public void analyze(String[] dexCodes) {
        super.analyze(dexCodes);
        switch (dexCodes[0]){
            case "check-cast" :
            case "check-cast/jumbo" :
                globalArguments.finalByteCode.add("checkcast" + " " + dexCodes[1] + " " + dexCodes[2]);
                globalArguments.finalByteCodePC++;
                break;
        }
    }
}
