package instructions;

import op.Register;
import op.globalArguments;
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
                Register firstRegister = globalArguments.registerQueue.getByDexName(dexCodes[0]);
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
}
