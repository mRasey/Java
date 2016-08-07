package instructions;
/*数据转换指令
数据转换指令用于将一种类型的数值转换成另一种类型。它的格式为“unop vA, vB”，vB寄存器或vB寄存器对存放需要转换的数据，
转换后的结果保存在vA寄存器或vA寄存器对中。
“neg-int”：对整型数求补。
“not-int”：对整型数求反。
“neg-long”：对长整型数求补。
“not-long”：对长整型数求反。
“neg-float”：对单精度浮点型数求补。
“neg-double”：对双精度浮点型数求补。
“int-to-long”：将整型数转换为长整型。
“int-to-float”：将整型数转换为单精度浮点型数。
“int-to-dobule”：将整型数转换为双精度浮点数。
“long-to-int”：将长整型数转换为整型。
“long-to-float”：将长整型数转换为单精度浮点型。
“long-to-double”：将长整型数转换为双精度浮点型。
“float-to-int”：将单精度浮点数转换为整型。
“float-to-long”：将单精度浮点数转换为长整型数。
“float-to-double”：将单精度浮点数转换为双精度浮点型数。
“double-to-int”：将双精度浮点数转换为整型。
“double-to-long”：将双精度浮点数转换为长整型。
“double-to-float”：将双精度浮点数转换为单精度浮点型。
“int-to-byte”：将整型转换为字节型。
“int-to-char”：将整型转换为字符型。
“int-to-short”：将整型转换为短整型。*/
public class _neg_not_to extends Instruction {

    public _neg_not_to(String instrName) {
        super(instrName);
    }

    @Override
    public void analyze() {
        switch (dexCodes[0]) {
            case "neg-int" :
            case "not-int" :
            case "neg-long" :
            case "not-long" :
            case "neg-float" :
            case "neg-double" :
            case "int-to-long" :
            case "int-to-float" :
            case "int-to-double" :
            case "long-to-int" :
            case "long-to-float" :
            case "long-to-double" :
            case "float-to-int" :
            case "float-to-long" :
            case "float-to-double" :
            case "double-to-int" :
            case "double-to-long" :
            case "double-to-float" :
            case "int-to-byte" :
            case "int-to-char" :
            case "int-to-short" :
        }
    }
}
