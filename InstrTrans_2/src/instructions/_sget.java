package instructions;

import op.Register;
import op.globalArguments;

import java.util.ArrayList;

public class _sget extends Instruction {



    @Override
    public void analyze(String[] dexCodes) {
        super.analyze(dexCodes);
        switch (dexCodes[0]) {
            case "sget" :
            case "sget/jumbo" :
            case "sget-wide" :
            case "sget-wide/jumbo" :
            case "sget-object" :
            case "sget-object/jumbo" :
            case "sget-boolean" :
            case "sget-boolean/jumbo" :
            case "sget-byte" :
            case "sget-byte/jumbo" :
            case "sget-char" :
            case "sget-char/jumbo" :
            case "sget-short" :
            case "sget-short/jumbo" :
                Register firstRegister = globalArguments.registerQueue.getByDexName(dexCodes[1]);
                String dataType = firstRegister.getType(globalArguments.dexCodeNumber).toLowerCase();
                globalArguments.finalByteCode.add("getfield" + " " + dexCodes[2]);
                if(dataType.startsWith("l")) {
                    globalArguments.finalByteCode.add("astore" + " " + firstRegister.stackNum);
                }
                else {
                    if (dataType.equals("j"))
                        dataType = "l";
                    if (dataType.equals("l") || dataType.equals("d") || dataType.equals("f")) {
                        globalArguments.finalByteCode.add(dataType + "store" + " " + firstRegister.stackNum);
                    } else {
                        globalArguments.finalByteCode.add("istore" + " " + firstRegister.stackNum);
                    }
                }
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
