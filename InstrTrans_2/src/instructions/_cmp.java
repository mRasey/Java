package instructions;
/*比较指令
比较指令用于对两个寄存器的值（浮点型或长整型）进行比较。它的格式为“cmpkind vAA, vBB, vCC”，
其中vBB寄存器与vCC寄存器是需要比较的两个寄存器或寄存器对，比较的结果放到vAA寄存器。Dalvik指令集中共有5条比较指令：
“cmpl-float”：比较两个单精度浮点数。如果vBB寄存器大于vCC寄存器，结果为-1，相等则结果为0，小于的话结果为1
“cmpg-float”：比较两个单精度浮点数。如果vBB寄存器大于vCC寄存器，则结果为1，相等则结果为0，小于的话结果为-1
“cmpl-double”：比较两个双精度浮点数。如果vBB寄存器对大于vCC寄存器对，则结果为-1，相等则结果为0，小于则结果为1
“cmpg-double”：比较两个双精度浮点数。如果vBB寄存器对大于vCC寄存器对，则结果为1，相等则结果为0，小于的话，则结果为-1
“cmp-long”：比较两个长整型数。如果vBB寄存器大于vCC寄存器，则结果为1，相等则结果为0，小则结果为-1*/
public class _cmp extends Instruction {

    public _cmp(String instrName) {
        super(instrName);
    }

    @Override
    public void analyze() {
        switch (dexCodes[0]) {
            case "cmpl-float" :
            case "cmpg-float" :
            case "cmpl-double" :
            case "cmpg-double" :
            case "cmp-long" :
        }
    }
}
