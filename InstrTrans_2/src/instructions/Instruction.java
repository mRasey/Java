package instructions;

import java.util.ArrayList;

public class Instruction {
    String instrName = "";
    String[] dexCodes;
    ArrayList<String> byteInstr = new ArrayList<>();

    public Instruction(String instrName) {
        this.instrName = instrName;
        dexCodes = instrName.split(" ");
    }

    public void analyze() {

    }

    public ArrayList<String> getByteInstr() {
        return byteInstr;
    }

}
