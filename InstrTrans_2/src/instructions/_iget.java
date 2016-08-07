package instructions;

public class _iget extends Instruction {

    public _iget(String instrName) {
        super(instrName);
    }

    @Override
    public void analyze() {
        switch (dexCodes[0]) {
            case "iget" :
            case "iget-wide" :
            case "iget-object" :
            case "iget-boolean" :
            case "iget-byte" :
            case "iget-char" :
            case "iget-short" :
            //todo /jumbo
        }
    }
}
