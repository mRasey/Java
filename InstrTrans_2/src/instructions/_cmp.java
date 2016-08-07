package instructions;

public class _cmp extends Instruction {

    public _cmp(String instrName) {
        super(instrName);
    }

    @Override
    public void analyze() {
        switch (dexCodes[0]) {
            case "cmpl-float" :
            case "cmpg-float" :
            case "cmpl-double" :
            case "cmpg-double" :
            case "cmp-long" :
        }
    }
}
