package instructions;

import op.globalArguments;

public class _nop extends Instruction {

    @Override
    public void analyze(String[] dexCodes){
        super.analyze(dexCodes);
        switch (dexCodes[0]) {
            case "nop" :
            	globalArguments.finalByteCode.add("nop");
                break;
        }
    }

}
