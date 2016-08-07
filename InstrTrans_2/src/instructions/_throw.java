package instructions;

public class _throw extends Instruction {

    public _throw(String instrName) {
        super(instrName);
    }

    @Override
    public void analyze() {
        switch (dexCodes[0]) {
            case "throw" :
        }
    }
}
