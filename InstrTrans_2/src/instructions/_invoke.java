package instructions;

public class _invoke extends Instruction {

    public _invoke(String instrName) {
        super(instrName);
    }

    @Override
    public void analyze() {
        switch (dexCodes[0]) {
            case "invoke-virtual" :
            case "invoke-virtual/jumbo" :
            case "invoke-virtual/range" :
            case "invoke-super" :
            case "invoke-super/jumbo" :
            case "invoke-super/range" :
            case "invoke-direct" :
            case "invoke-direct/jumbo" :
            case "invoke-direct/range" :
            case "invoke-static" :
            case "invoke-static/jumbo" :
            case "invoke-static/range" :
            case "invoke-interface" :
            case "invoke-interface/jumbo" :
            case "invoke-interface/range" :
        }
    }
}
