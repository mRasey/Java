package instructions;
/*“if-test vA, vB, +CCCC”：条件跳转指令。比较vA寄存器与vB寄存器的值，如果比较结果满足就跳转到CCCC指定的偏移处。偏移量CCCC不能为0。
if-test类型的指令有以下几条：
“if-eq”：如果vA等于vB则跳转。Java语法表示为“if(vA == vB)”
"if-ne"：如果vA不等于vB则跳转。Java语法表示为“if(vA != vB)”
“if-lt”：如果vA小于vB则跳转。Java语法表示为“if(vA < vB)”
“if-ge”：如果vA大于等于vB则跳转。Java语法表示为“if(vA >= vB)”
“if-gt”：如果vA大于vB则跳转。Java语法表示为“if(vA > vB)”
“if-le”：如果vA小于等于vB则跳转。Java语法表示为“if(vA <= vB)”
“if-testz vAA, +BBBB”：条件跳转指令。拿vAA寄存器与0比较，如果比较结果满足或值为0时就跳转到BBBB指定的偏移处。偏移量BBBB不能为0。
if-testz类型的指令有以下几条：
“if-eqz”：如果vAA为0则跳转。Java语法表示为“if(vAA == 0)”
"if-nez"：如果vAA不为0则跳转。Java语法表示为“if(vAA != 0)”
"if-ltz"：如果vAA小于0则跳转。Java语法表示为“if(vAA < 0)”
“if-gez”：如果vAA大于等于0则跳转。Java语法表示为“if(vAA >= 0)”
“if-gtz”：如果vAA大于0则跳转。Java语法表示为“if(vAA > 0)”
“if-lez”：如果vAA小于等于0则跳转。Java语法表示为“if(vAA <= 0)”*/
public class _if extends Instruction{

    public _if(String instrName) {
        super(instrName);
    }

    @Override
    public void analyze() {
        switch (dexCodes[0]) {
            case "if-eq" :
            case "if-ne" :
            case "if-lt" :
            case "if-ge" :
            case "if-gt" :
            case "if-le" :
            case "if-eqz" :
            case "if-nez" :
            case "if-ltz" :
            case "if-gez" :
            case "if-gtz" :
            case "if-lez" :
        }
    }
}
