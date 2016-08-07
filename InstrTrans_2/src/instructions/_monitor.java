package instructions;

public class _monitor extends Instruction {

    public _monitor(String instrName) {
        super(instrName);
    }

    @Override
    public void analyze() {
        switch (dexCodes[0]){
            case "monitor-enter" :
            case "monitor-exit" :
        }
    }
}
