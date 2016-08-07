package instructions;

public class _if extends Instruction{

    public _if(String instrName) {
        super(instrName);
    }

    @Override
    public void analyze() {
        switch (dexCodes[0]) {
            case "if-test" :
            case "if-eq" :
            case "if-ne" :
            case "if-lt" :
            case "if-ge" :
            case "if-gt" :
            case "if-le" :
            case "if-testz" :
            case "if-eqz" :
            case "if-nez" :
            case "if-ltz" :
            case "if-gez" :
            case "if-gtz" :
            case "if-lez" :
        }
    }
}
