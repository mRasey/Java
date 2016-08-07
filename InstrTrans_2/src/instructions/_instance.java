package instructions;

public class _instance extends Instruction {

    public _instance(String instrName) {
        super(instrName);
    }

    @Override
    public void analyze() {
        switch (dexCodes[0]) {
            case "instance-of" :
            case "new-instance" :
            case "instance-of/jumbo" :
            case "new-instance/jumbo" :
        }
    }
}
