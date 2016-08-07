package instructions;

public class _binop extends Instruction {

    public _binop(String instrName) {
        super(instrName);
    }

    @Override
    public void analyze() {
        switch (dexCodes[0]) {
            case "binop" :
            case "binop/2addr" :
            case "binop/lit16" :
            case "binop/lit8" :
        }
    }
}
