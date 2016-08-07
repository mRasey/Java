package instructions;
/*“add-type”：vBB寄存器与vCC寄存器值进行加法运算（vBB + vCC）
"sub-type"：vBB寄存器与vCC寄存器值进行减法运算（vBB - vCC）
"mul-type"：vBB寄存器与vCC寄存器值进行乘法运算（vBB * vCC）
"div-type"：vBB寄存器与vCC寄存器值进行除法运算（vBB / vCC）
"rem-type"：vBB寄存器与vCC寄存器值进行模运算（vBB % vCC）
"and-type"：vBB寄存器与vCC寄存器值进行与运算（vBB & vCC）
"or-type"：vBB寄存器与vCC寄存器值进行或运算（vBB | vCC）
"xor-type"：vBB寄存器与vCC寄存器值进行异或运算（vBB ^ vCC）
"shl-type"：vBB寄存器值（有符号数）左移vCC位（vBB << vCC ）
"shr-type"：vBB寄存器值（有符号）右移vCC位（vBB >> vCC）
"ushr-type"：vBB寄存器值（无符号数）右移vCC位（vBB >>> vCC）
其中基础字节码后面的-type可以是-int，-long， -float，-double。后面3类指令与之类似。
至此，Dalvik虚拟机支持的所有指令就介绍完了。在android4.0系统以前，每个指令的字节码只占用一个字节，范围是0x0~0x0ff。
在android4.0系统中，又扩充了一部分指令，这些指令被称为扩展指令，主要是在指令助记符后添加了jumbo后缀，增加了寄存器与常量的取值范围。*/
public class _op extends Instruction {

    public _op(String instrName) {
        super(instrName);
    }

    @Override
    public void analyze() {
        switch (dexCodes[0]) {
            case "add-int" :
            case "add-long" :
            case "add-float" :
            case "add-double" :

            case "sub-int" :
            case "sub-long" :
            case "sub-float" :
            case "sub-double" :

            case "mul-int" :
            case "mul-long" :
            case "mul-float" :
            case "mul-double" :

            case "div-int" :
            case "div-long" :
            case "div-float" :
            case "div-double" :

            case "rem-int" :
            case "rem-long" :
            case "rem-float" :
            case "rem-double" :

            case "and-int" :
            case "and-long" :
            case "and-float" :
            case "and-double" :

            case "or-int" :
            case "or-long" :
            case "or-float" :
            case "or-double" :

            case "xor-int" :
            case "xor-long" :
            case "xor-float" :
            case "xor-double" :

            case "shl-int" :
            case "shl-long" :
            case "shl-float" :
            case "shl-double" :

            case "shr-int" :
            case "shr-long" :
            case "shr-float" :
            case "shr-double" :

            case "ushr-int" :
            case "ushr-long" :
            case "ushr-float" :
            case "ushr-double" :
        }
    }
}
