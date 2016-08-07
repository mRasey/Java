package instructions;
/*“goto +AA”：无条件跳转到指定偏移处，偏移量AA不能为0。
“goto/16 +AAAA”：无条件跳转到指定偏移处，偏量AAAA不能为0。
“goto/32 +AAAAAAAA”：无条件跳转到指定偏移处。*/
public class _goto extends Instruction {

    public _goto(String instrName) {
        super(instrName);
    }

    @Override
    public void analyze() {
        switch (dexCodes[0]) {
            case "goto" :
            case "goto/16" :
            case "goto/32" :
        }
    }
}
