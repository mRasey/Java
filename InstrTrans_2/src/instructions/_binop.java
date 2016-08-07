package instructions;
/*
“binop vAA, vBB, vCC”：将vBB寄存器与vCC寄存器进行运算，结果保存到vAA寄存器。
“binop/2addr vA, vB”：将vA寄存器与vB寄存器进行运算，结果保存到vA寄存器。
“binop/lit16 vA, vB, #+CCCC”：将vB寄存器与常量 CCCC进行运算，结果保存到vA寄存器。
“binop/lit8 vAA, vBB, #+CC”：将vBB寄存器与常量CC进行运算，结果保存到vAA寄存器。*/
public class _binop extends Instruction {

    public _binop(String instrName) {
        super(instrName);
    }

    @Override
    public void analyze() {
        switch (dexCodes[0]) {
            case "binop" :
            case "binop/2addr" :
            case "binop/lit16" :
            case "binop/lit8" :
        }
    }
}
