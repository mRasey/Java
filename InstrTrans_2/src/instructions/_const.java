package instructions;

public class _const extends Instruction{

    public _const(String instrName) {
        super(instrName);
    }

    @Override
    public void analyze() {
        switch (dexCodes[0]){
            case "const/4" :
            case "const/16" :
            case "const" :
            case "const/high16" :
            case "const-wide/16" :
            case "const-wide/32" :
            case "const-wide" :
            case "const-wide/high16" :
            case "const-string" :
            case "const-string/jumbo" :
            case "const-class" :
            case "const-class/jumbo" :
        }
    }
}
