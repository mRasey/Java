package instructions;

public class _sget extends Instruction {

    public _sget(String instrName) {
        super(instrName);
    }

    @Override
    public void analyze() {
        switch (dexCodes[0]) {
            case "sget" :
            case "sget-wide" :
            case "sget-object" :
            case "sget-boolean" :
            case "sget-byte" :
            case "sget-char" :
            case "sget-short" :
            //todo /jumbo
        }
    }
}
