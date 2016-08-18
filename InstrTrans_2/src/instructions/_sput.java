package instructions;

import op.Register;
import op.globalArguments;

import java.util.ArrayList;

public class _sput extends Instruction {


    @Override
    public void analyze(String[] dexCodes) {
        super.analyze(dexCodes);
        switch (dexCodes[0]) {
            case "sput" :
            case "sput/jumbo" :
            case "sput-wide" :
            case "sput-wide/jumbo" :
            case "sput-object" :
            case "sput-object/jumbo" :
            case "sput-boolean" :
            case "sput-boolean/jumbo" :
            case "sput-byte" :
            case "sput-byte/jumbo" :
            case "sput-char" :
            case "sput-char/jumbo" :
            case "sput-short" :
            case "sput-short/jumbo" :
                Register firstRegister = globalArguments.registerQueue.getByDexName(dexCodes[1]);
                String dataType = firstRegister.getType(globalArguments.dexCodeNumber).toLowerCase();
                if(dataType.startsWith("l")) {
                    globalArguments.finalByteCode.add("aload" + " " + firstRegister.stackNum);
                }
                else {
                    if (dataType.equals("j"))
                        dataType = "l";
                    if (dataType.equals("l") || dataType.equals("d") || dataType.equals("f")) {
                        globalArguments.finalByteCode.add(dataType + "load" + " " + firstRegister.stackNum);
                    } else {
                        globalArguments.finalByteCode.add("iload" + " " + firstRegister.stackNum);
                    }
                }
                globalArguments.finalByteCode.add("putfield" + " " + dexCodes[2]);
                globalArguments.finalByteCodePC += 2;
                break;
        }
    }

    @Override
    public boolean ifUpgrade(ArrayList<String> dexCode, int lineNum) {
        Register firstRegister = globalArguments.registerQueue.getByDexName(dexCode.get(1));
        firstRegister.updateType(lineNum, dexCode.get(2).substring(dexCode.get(2).lastIndexOf(":")+1));

        return true;
    }
}
