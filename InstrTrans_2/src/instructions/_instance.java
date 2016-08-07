package instructions;
/*实例操作指令
与实例相关的操作包括实例的类型转换，检查及新建等：
“instance-of vA, vB, type@CCCC”：判断vB寄存器中的对象引用是否可以转换成指定的类型，如果可以vA寄存器赋值为1，否则vA寄存器赋值为0。
“new-instance vAA, type@BBBB”：构造一个指定类型对象的新实例，并将对象引用赋值给vAA寄存器，类型符type指定的类型不能是数组类。
“instance-of/jumbo vAAAA, vBBBB, type@CCCCCCCC”：指令功能与“instance-of vA, vB, type@CCCC”相同，只是寄存器值与指令的索引取值范围更大（Android4.0中新增的指令）。
“new-instance/jumbo vAAAA, type@BBBBBBBB”：指令功能与“new-instance vAA, type@BBBB”相同，只是寄存器值与指令的索引取值范围更大（Android4.0中新增的指令）。*/
public class _instance extends Instruction {

    public _instance(String instrName) {
        super(instrName);
    }

    @Override
    public void analyze() {
        switch (dexCodes[0]) {
            case "instance-of" :
            case "new-instance" :
            case "instance-of/jumbo" :
            case "new-instance/jumbo" :
        }
    }
}
