package instructions;

public class _sput extends Instruction {

    public _sput(String instrName) {
        super(instrName);
    }

    @Override
    public void analyze() {
        switch (dexCodes[0]) {
            case "sput" :
            case "sput-wide" :
            case "sput-object" :
            case "sput-boolean" :
            case "sput-byte" :
            case "sput-char" :
            case "sput-short" :
            //todo /jumbo
        }
    }
}
