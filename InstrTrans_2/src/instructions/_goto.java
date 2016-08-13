package instructions;

import op.globalArguments;
/*“goto +AA”：无条件跳转到指定偏移处，偏移量AA不能为0。
“goto/16 +AAAA”：无条件跳转到指定偏移处，偏量AAAA不能为0。
“goto/32 +AAAAAAAA”：无条件跳转到指定偏移处。*/
public class _goto extends Instruction {



    @Override
    public void analyze(String[] dexCodes) {
        super.analyze(dexCodes);
        switch (dexCodes[0]) {
            case "goto" :
            case "goto/16" :
                globalArguments.finalByteCode.add("goto" + " " + dexCodes[1]);
                globalArguments.finalByteCodePC++;
            case "goto/32" :
                globalArguments.finalByteCode.add("goto_w" + " " + dexCodes[1]);
                globalArguments.finalByteCodePC++;
        }
    }
}
