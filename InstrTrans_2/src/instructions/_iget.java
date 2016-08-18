package instructions;

import op.Register;
import op.globalArguments;

import java.util.ArrayList;

public class _iget extends Instruction {


    @Override
    public void analyze(String[] dexCodes) {
        super.analyze(dexCodes);
        switch (dexCodes[0]) {
            case "iget" :
            case "iget/jumbo" :
            case "iget-wide" :
            case "iget-wide/jumbo" :
            case "iget-object" :
            case "iget-object/jumbo" :
            case "iget-boolean" :
            case "iget-boolean/jumbo" :
            case "iget-byte" :
            case "iget-byte/jumbo" :
            case "iget-char" :
            case "iget-char/jumbo" :
            case "iget-short" :
            case "iget-short/jumbo" :
                Register firstRegister = globalArguments.registerQueue.getByDexName(dexCodes[1]);
                String dataType = firstRegister.getType(globalArguments.dexCodeNumber).toLowerCase();
                globalArguments.finalByteCode.add("aload 0");
                globalArguments.finalByteCode.add("getfield" + " " + dexCodes[3]);
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
