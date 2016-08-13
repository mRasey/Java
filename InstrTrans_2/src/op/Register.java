package op;

import java.util.HashMap;

public class Register {

    String dexName;
    public int stackNum;
    public String currentType;
    HashMap<Integer, String> typeInCurrentLine = new HashMap<>();

    public Register(String dexName) {
        this.dexName = dexName;
    }

    public Register(String dexName, String type, int stackNum) {
        currentType = type;
        this.dexName = dexName;
        this.stackNum = stackNum;
    }

    public Register(String dexName, String type, int stackNum, int lineNume) {
        currentType = type;
        this.dexName = dexName;
        this.stackNum = stackNum;
        typeInCurrentLine.put(lineNume, dexName);
    }

    public void updateType(int lineNum, String type) {
        currentType = type;
        typeInCurrentLine.put(lineNum, currentType);
    }

    public String getType(int lineNum) {
        return typeInCurrentLine.get(lineNum);
    }

}
