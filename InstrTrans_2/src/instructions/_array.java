package instructions;

public class _array extends Instruction{

    public _array(String instrName) {
        super(instrName);
    }

    @Override
    public void analyze() {
        switch (dexCodes[0]) {
            case "array-length" :
            case "new-array" :
            case "filled-new-array" :
            case "filled-new-array/range" :
            case "fill-array-data" :
            case "new-array/jumbo" :
            case "filled-new-array/jumbo" :
            case "arrayop" :
        }
    }
}
