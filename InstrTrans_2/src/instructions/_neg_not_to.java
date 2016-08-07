package instructions;

public class _neg_not_to extends Instruction {

    public _neg_not_to(String instrName) {
        super(instrName);
    }

    @Override
    public void analyze() {
        switch (dexCodes[0]) {
            case "neg-int" :
            case "not-int" :
            case "neg-long" :
            case "not-long" :
            case "neg-float" :
            case "neg-double" :
            case "int-to-long" :
            case "int-to-float" :
            case "int-to-double" :
            case "long-to-int" :
            case "long-to-float" :
            case "long-to-double" :
            case "float-to-int" :
            case "float-to-long" :
            case "float-to-double" :
            case "double-to-int" :
            case "double-to-long" :
            case "double-to-float" :
            case "int-to-byte" :
            case "int-to-char" :
            case "int-to-short" :
        }
    }
}
