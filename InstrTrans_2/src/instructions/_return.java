package instructions;

import instructions.Instruction;

public class _return extends Instruction {

    public _return(String instrName) {
        super(instrName);
    }

    @Override
    public void analyze() {
        switch (dexCodes[0]){
            case "return" :
            case "return-void" :
            case "return-wide" :
            case "return-object" :
        }
    }
}
