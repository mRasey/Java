package instructions;

import op.globalArguments;

import java.util.ArrayList;

public class Instruction {

    public void analyze(String[] dexCodes) {
    	globalArguments.dexToClass.put(globalArguments.dexCodeNumber, globalArguments.finalByteCodePC);
    }

    public boolean ifUpgrade(ArrayList<String> dexCode, int lineNum) {
        return false;
    }

    public boolean ifUpgrade(ArrayList<String> firstDexCode, ArrayList<String> secondDexCode, int lineNum) {
        return false;
    }
}
