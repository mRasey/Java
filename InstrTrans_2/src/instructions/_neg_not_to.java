package instructions;

import op.Register;
import op.globalArguments;

import java.util.ArrayList;

import static op.globalArguments.registerQueue;


/*数据转换指令
数据转换指令用于将一种类型的数值转换成另一种类型。它的格式为“unop vA, vB”，vB寄存器或vB寄存器对存放需要转换的数据，
转换后的结果保存在vA寄存器或vA寄存器对中。
“neg-int”：对整型数求补。
“neg-long”：对长整型数求补。
“neg-float”：对单精度浮点型数求补。
“neg-double”：对双精度浮点型数求补。
“not-int”：对整型数求反。
“not-long”：对长整型数求反。
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

    @Override
    public void analyze(String[] dexCodes) {
        super.analyze(dexCodes);
        Register firstRegister = registerQueue.getByDexName(dexCodes[1]);
        
        if(dexCodes[0].contains("neg")) {
            String dataType = dexCodes[0].substring(dexCodes[0].indexOf("-") + 1, dexCodes[0].indexOf("-") + 2);
            globalArguments.finalByteCode.add(dataType + "load" + " " + firstRegister.stackNum);
            globalArguments.finalByteCode.add("iconst_m1");
            globalArguments.finalByteCode.add(dataType + "xor");
            globalArguments.finalByteCode.add(dataType + "const_1");
            globalArguments.finalByteCode.add(dataType + "add");
            globalArguments.finalByteCode.add(dataType + "store" + " " + firstRegister.stackNum);
            dataType = dataType.toUpperCase();//变为大写
//            if(dataType.equals("L"))
//                dataType = "J";
            globalArguments.finalByteCodePC += 6;
        }
        else if(dexCodes[0].contains("not")) {
            String dataType = dexCodes[0].substring(dexCodes[0].indexOf("-") + 1, dexCodes[0].indexOf("-") + 2);
            globalArguments.finalByteCode.add(dataType + "load" + " " + firstRegister.stackNum);
            globalArguments.finalByteCode.add("iconst_m1");//将-1推至栈顶
            globalArguments.finalByteCode.add(dataType + "xor");
            globalArguments.finalByteCode.add(dataType + "store" + " " + firstRegister.stackNum);
            dataType = dataType.toUpperCase();//变为大写
//            if(dataType.equals("L"))
//                dataType = "J";
            globalArguments.finalByteCodePC += 4;
        }
        else if(dexCodes[0].contains("to")) {
            String oldType = dexCodes[0].charAt(0) + "";
            String newType = dexCodes[0].substring(dexCodes[0].lastIndexOf("-") + 1, dexCodes[0].lastIndexOf("-") + 2);
            globalArguments.finalByteCode.add(oldType + "load" + " " + firstRegister.stackNum);
            globalArguments.finalByteCode.add(oldType + "2" + newType);
            globalArguments.finalByteCode.add(oldType + "store" + firstRegister.stackNum);
            newType = newType.toUpperCase();
            if(newType.equals("L"))
                newType = "J";
            firstRegister.updateType(globalArguments.dexCodeNumber, newType);//更新类型
            globalArguments.finalByteCodePC += 3;
        }

        /*switch (dexCodes[0]) {
            case "neg-int" :
            case "neg-long" :
            case "neg-float" :
            case "neg-double" :
            case "not-int" :
            case "not-long" :
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
        }*/
    }

    @Override
    public boolean ifUpgrade(ArrayList<String> dexCode, int lineNum) {
        if(dexCode.get(0).contains("to")){
            String firstDataType = (dexCode.get(0).charAt(0)+"").toUpperCase();
            String secondDataType = (dexCode.get(0).charAt(0)+"").toUpperCase();
            if(firstDataType.equals("L")){
                firstDataType = "J";
            }
            if(secondDataType.equals("L")){
                secondDataType = "J";
            }
            Register firstRegister = globalArguments.registerQueue.getByDexName(dexCode.get(1));
            Register secondRegister = globalArguments.registerQueue.getByDexName(dexCode.get(2));
            firstRegister.updateType(lineNum, secondDataType);
            secondRegister.updateType(lineNum, firstDataType);
        }
        else{
            String dataType = dexCode.get(0).substring(dexCode.get(0).indexOf("-")+1, dexCode.get(0).indexOf("-")+2).toUpperCase();
            if(dataType.equals("L")){
                dataType = "J";
            }
            Register register = globalArguments.registerQueue.getByDexName(dexCode.get(1));
            register.updateType(lineNum, dataType);
        }
        return true;
    }
}
