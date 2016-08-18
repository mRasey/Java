package op;

import instructions.*;
import java.util.ArrayList;

/*
 * 特别注意跳转指令。
 * 每条指令都记录下来他翻译后的起始编号，跳转指令先记录下对应的原指令号，最后全翻译完之后再根据原指令号求出跳转位置
 * 
 *每个方法调用指令翻译时都得先用aload_0将this放入操作栈中
 * 
 * */
public class translation {
	//存放指令的信息
	ArrayList<String> instruction; 
	
    String[] dexCodes;
	//java字节码指令编号
	int classCodeNumber = 0;

    public translation(ArrayList<String> instruction, int dexCodeNumber) {
        this.instruction = instruction;
        globalArguments.dexCodeNumber = dexCodeNumber;
        dexCodes = instruction.toArray(new String[instruction.size()]);
    }
	
	//翻译
	public void translateIns(){
		if(dexCodes[0].contains("array") || dexCodes[0].contains("aget") || dexCodes[0].contains("aput"))
            new _array().analyze(dexCodes);
        else if(dexCodes[0].contains("check"))
            new _check().analyze(dexCodes);
        else if(dexCodes[0].contains("cmp"))
            new _cmp().analyze(dexCodes);
        else if(dexCodes[0].contains("const"))
            new _const().analyze(dexCodes);
        else if(dexCodes[0].contains("goto"))
            new _goto().analyze(dexCodes);
        else if(dexCodes[0].contains("if"))
            new _if().analyze(dexCodes);
        else if(dexCodes[0].contains("iget"))
            new _iget().analyze(dexCodes);
        else if(dexCodes[0].contains("instance"))
            new _instance().analyze(dexCodes);
        else if(dexCodes[0].contains("invoke"))
            new _invoke().analyze(dexCodes);
        else if(dexCodes[0].contains("iput"))
            new _iput().analyze(dexCodes);
        else if(dexCodes[0].contains("monitor"))
            new _monitor().analyze(dexCodes);
        else if(dexCodes[0].contains("move"))
            new _move().analyze(dexCodes);
        else if(dexCodes[0].contains("neg") || dexCodes[0].contains("not") || dexCodes[0].contains("to"))
            new _neg_not_to().analyze(dexCodes);
        else if(dexCodes[0].contains("nop"))
            new _nop().analyze(dexCodes);
        else if(  dexCodes[0].contains("add-")
                || dexCodes[0].contains("sub-")
                || dexCodes[0].contains("mul-")
                || dexCodes[0].contains("div-")
                || dexCodes[0].contains("rem-")
                || dexCodes[0].contains("and-")
                || dexCodes[0].contains("or-")
                || dexCodes[0].contains("xor-")
                || dexCodes[0].contains("shl-")
                || dexCodes[0].contains("shr-")
                || dexCodes[0].contains("ushr-")) {
            new _op().analyze(dexCodes);
        }
        else if(dexCodes[0].contains("return"))
            new _return().analyze(dexCodes);
        else if(dexCodes[0].contains("sget"))
            new _sget().analyze(dexCodes);
        else if(dexCodes[0].contains("sput"))
            new _sput().analyze(dexCodes);
        else if(dexCodes[0].contains("switch"))
            new _switch().analyze(dexCodes);
        else if(dexCodes[0].contains("throw"))
            new _throw().analyze(dexCodes);
        else {
        	System.out.println(dexCodes[0]);
            System.out.println("error instruction");
        }
	}
	
}
