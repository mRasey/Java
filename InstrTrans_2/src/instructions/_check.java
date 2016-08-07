package instructions;

public class _check extends Instruction {

    public _check(String instrName) {
        super(instrName);
    }

    @Override
    public void analyze() {
        switch (dexCodes[0]){
            case "check-cast" :
            case "check-cast/jumbo" :
        }
    }
}
