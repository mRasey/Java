package instructions;

public class _switch extends Instruction {

    public _switch(String instrName) {
        super(instrName);
    }

    @Override
    public void analyze() {
        switch (dexCodes[0]) {
            case "packed-switch" :
            case "sparse-switch" :
        }
    }
}
