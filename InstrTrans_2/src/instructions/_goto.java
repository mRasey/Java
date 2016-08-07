package instructions;

public class _goto extends Instruction {

    public _goto(String instrName) {
        super(instrName);
    }

    @Override
    public void analyze() {
        switch (dexCodes[0]) {
            case "goto" :
            case "goto/16" :
            case "goto/32" :
        }
    }
}
