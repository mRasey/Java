package instructions;

public class _op extends Instruction {

    public _op(String instrName) {
        super(instrName);
    }

    @Override
    public void analyze() {
        switch (dexCodes[0]) {
            case "add-int" :
            case "add-long" :
            case "add-float" :
            case "add-double" :

            case "sub-int" :
            case "sub-long" :
            case "sub-float" :
            case "sub-double" :

            case "mul-int" :
            case "mul-long" :
            case "mul-float" :
            case "mul-double" :

            case "div-int" :
            case "div-long" :
            case "div-float" :
            case "div-double" :

            case "rem-int" :
            case "rem-long" :
            case "rem-float" :
            case "rem-double" :

            case "and-int" :
            case "and-long" :
            case "and-float" :
            case "and-double" :

            case "or-int" :
            case "or-long" :
            case "or-float" :
            case "or-double" :

            case "xor-int" :
            case "xor-long" :
            case "xor-float" :
            case "xor-double" :

            case "shl-int" :
            case "shl-long" :
            case "shl-float" :
            case "shl-double" :

            case "shr-int" :
            case "shr-long" :
            case "shr-float" :
            case "shr-double" :

            case "ushr-int" :
            case "ushr-long" :
            case "ushr-float" :
            case "ushr-double" :
        }
    }
}
