package trans;

import java.util.ArrayList;
import java.util.IllegalFormatCodePointException;

public class Test {
    public static void main(String[] args) throws IllegalAccessError{
        ArrayList<String> regTypes = new ArrayList<>();
        String temp = "println(I[[[BLcom/billy/dexcode/MainActivity;DD)V";
        String types = temp.substring(temp.indexOf("(") + 1, temp.indexOf(")") + 1);//括号中参数的类型 example: ILstring;D)
        while (!types.equals(")")) {
            if (types.startsWith("L")) {
                regTypes.add(types.substring(0, types.indexOf(";") + 1));
                types = types.substring(types.indexOf(";"));
            } else if (types.startsWith("[")) {
                String tempType = "";
                do {
                    tempType += "[";
                    types = types.substring(1);
                }while (types.startsWith("["));
                regTypes.add(tempType + types.charAt(0));
            } else {
                regTypes.add(types.charAt(0) + "");
            }
            types = types.substring(1);
        }
        for(String s : regTypes) {
            System.out.println(s);
        }
    }
}
