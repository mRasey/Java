package instructions;

import op.Main;
import op.Register;
import op.globalArguments;

import java.util.ArrayList;

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



    @Override
    public void analyze(String[] dexCodes) {
        super.analyze(dexCodes);
        Register firstRegister = globalArguments.registerQueue.getByDexName(dexCodes[1]);
        Register secondRegister = null;
        if(!dexCodes[0].contains("z"))
            secondRegister = globalArguments.registerQueue.getByDexName(dexCodes[2]);
        switch (dexCodes[0]) {
            case "if-eq" :
            case "if-ne" :
            case "if-lt" :
            case "if-ge" :
            case "if-gt" :
            case "if-le" :
                String dataType = "i";
                if(!firstRegister.getType(globalArguments.dexCodeNumber).equals("I")) {
                    dataType = "a";
                }
                String op = dexCodes[0].substring(dexCodes[0].indexOf("-") + 1, dexCodes[0].length());
                globalArguments.finalByteCode.add(dataType + "load" + " " + firstRegister.stackNum);
                globalArguments.finalByteCode.add(dataType + "load" + " " + secondRegister.stackNum);
                globalArguments.finalByteCode.add(dataType + "f_icmple" + op + " " + dexCodes[3]);
                globalArguments.finalByteCodePC += 3;
                break;
            case "if-eqz" :
            case "if-nez" :
            case "if-ltz" :
            case "if-gez" :
            case "if-gtz" :
            case "if-lez" :
                String opz = dexCodes[0].substring(dexCodes[0].indexOf("-") + 1, dexCodes[0].indexOf("-") + 3);
                globalArguments.finalByteCode.add("iload" + " " + firstRegister.stackNum);
                globalArguments.finalByteCode.add("if" + opz + " " + dexCodes[2]);
                globalArguments.finalByteCodePC += 3;
                break;
        }
    }

    @Override
    public boolean ifUpgrade(ArrayList<String> dexCode, int lineNum) {
        if(dexCode.get(0).contains("z")){
            Register firstRegister = globalArguments.registerQueue.getByDexName(dexCode.get(1));
            firstRegister.updateType(lineNum, firstRegister.currentType);
        }
        else{
            Register firstRegister = globalArguments.registerQueue.getByDexName(dexCode.get(1));
            firstRegister.updateType(lineNum, firstRegister.currentType);

            Register secondRegister = globalArguments.registerQueue.getByDexName(dexCode.get(2));
            secondRegister.updateType(lineNum, secondRegister.currentType);
        }

        return true;
    }
}
