package instructions;
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

    public _move(String instrName) {
        super(instrName);
    }

    @Override
    public void analyze() {
        switch (dexCodes[0]){
            case "move" :
            case "move/16" :
            case "move/from16" :
            case "move-wide" :
            case "move-wide/from16" :
            case "move-object" :
            case "move-object/16" :
            case "move-object/from16" :
            case "move-result" :
            case "move-result-wide" :
            case "move-result-object" :
            case "move-exception" :
        }
    }
}
