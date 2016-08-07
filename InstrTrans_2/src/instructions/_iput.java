package instructions;

public class _iput extends Instruction {

    public _iput(String instrName) {
        super(instrName);
    }

    @Override
    public void analyze() {
        switch (dexCodes[0]) {
            case "iput" :
            case "iput-wide" :
            case "iput-object" :
            case "iput-boolean" :
            case "iput-byte" :
            case "iput-char" :
            case "iput-short" :
            //todo /jumbo
        }
    }
}
