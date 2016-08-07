package instructions;

import java.util.ArrayList;

public class Instruction {
    String instrName = "";
    ArrayList<String> byteInstr = new ArrayList<>();

    public Instruction(String instrName) {
        this.instrName = instrName;
    }

    public ArrayList<String> getByteInstr() {
        return byteInstr;
    }

}
