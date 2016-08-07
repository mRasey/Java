package instructions;
/*异常指令
Dalvik指令集中有一条指令用来抛出异常。
“throw vAA”抛出vAA寄存器中指定类型的异常。*/
public class _throw extends Instruction {

    public _throw(String instrName) {
        super(instrName);
    }

    @Override
    public void analyze() {
        switch (dexCodes[0]) {
            case "throw" :
        }
    }
}
