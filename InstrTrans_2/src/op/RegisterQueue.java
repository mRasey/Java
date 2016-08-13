package op;

import java.util.ArrayList;

public class RegisterQueue {

    ArrayList<Register> registers = new ArrayList<>();

    public void addNewRegister(Register register) {
        registers.add(register);
    }

    public Register getByDexName(String dexName) {
        for (Register register : registers) {
            if (register.dexName.equals(dexName)) {
                return register;
            }
        }
        return null;
    }

    public void clear() {
        registers.clear();
    }

}
