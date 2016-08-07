package instructions;
/*“packed-switch vAA, +BBBBBBBB”：分支跳转指令。vAA寄存器为switch分支中需要判断的值，BBBBBBBB指向一个packed-switch-payload格式的偏移表，表中的值是有规律递增的。
“sparse-switch vAA, +BBBBBBBB”：分支跳转指令。vAA寄存器为switch分支中需要判断的值，BBBBBBBB指向一个sparse-switch-payload格式的偏移表，表中的值是无规律的偏移量。*/
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
