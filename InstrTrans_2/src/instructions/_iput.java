package instructions;

import op.Register;
import op.globalArguments;

import java.util.ArrayList;

public class _iput extends Instruction {



    @Override
    public void analyze(String[] dexCodes) {
        super.analyze(dexCodes);
        switch (dexCodes[0]) {
            case "iput" :
            case "iput/jumbo" :
            case "iput-wide" :
            case "iput-wide/jumbo" :
            case "iput-object" :
            case "iput-object/jumbo" :
            case "iput-boolean" :
            case "iput-boolean/jumbo" :
            case "iput-byte" :
            case "iput-byte/jumbo" :
            case "iput-char" :
            case "iput-char/jumbo" :
            case "iput-short" :
            case "iput-short/jumbo" :
                Register firstRegister = globalArguments.registerQueue.getByDexName(dexCodes[1]);
                String dataType = firstRegister.getType(globalArguments.dexCodeNumber).toLowerCase();
                globalArguments.finalByteCode.add("aload 0");
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
                globalArguments.finalByteCode.add("putfield" + " " + dexCodes[3]);
                globalArguments.finalByteCodePC += 3;
                break;
        }
    }

    @Override
    public boolean ifUpgrade(ArrayList<String> dexCode, int lineNum) {
        Register firstRegister = globalArguments.registerQueue.getByDexName(dexCode.get(1));
        Register secondRegister = globalArguments.registerQueue.getByDexName(dexCode.get(2));
        firstRegister.updateType(lineNum, dexCode.get(3).substring(dexCode.get(3).lastIndexOf(":")+1));
        secondRegister.updateType(lineNum, secondRegister.currentType);

        return true;
    }
}
