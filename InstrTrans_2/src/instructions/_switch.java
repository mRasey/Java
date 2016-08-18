package instructions;

import op.Register;
import op.SwitchData;
import op.globalArguments;

import java.util.ArrayList;

/*“packed-switch vAA, +BBBBBBBB”：分支跳转指令。vAA寄存器为switch分支中需要判断的值，BBBBBBBB指向一个packed-switch-payload格式的偏移表，表中的值是有规律递增的。
“sparse-switch vAA, +BBBBBBBB”：分支跳转指令。vAA寄存器为switch分支中需要判断的值，BBBBBBBB指向一个sparse-switch-payload格式的偏移表，表中的值是无规律的偏移量。*/
public class _switch extends Instruction {


    @Override
    public void analyze(String[] dexCodes) {
        super.analyze(dexCodes);
        ArrayList<SwitchData> switchDataList = globalArguments.switchData.get(dexCodes[2]);
        
        
        Register register = globalArguments.registerQueue.getByDexName(dexCodes[1]);
        String dataType = register.getType(globalArguments.dexCodeNumber);
        ArrayList<ArrayList<String>> instructions = globalArguments.rf.instructions;
        String defaultTab = null;
        for(int i = globalArguments.dexCodeNumber + 2; i < instructions.size(); i++) {
            ArrayList<String> nextInstr = instructions.get(i);
            if(nextInstr.get(0).startsWith("."))
                continue;
            else if(nextInstr.get(0).startsWith(":")) {
                //是一个跳转标签
                defaultTab = nextInstr.get(0);
                break;
            }
            else {
                //是一条指令
                defaultTab = ":default_" + globalArguments.switchDefaultIndex;
                globalArguments.switchDefaultIndex++;
            }
        }
        switch (dexCodes[0]) {
            case "packed-switch" :
                //tableswitch按照编译器推断出的顺序排列，可能跟源代码的代码顺序不同，且按照第一个case的值往下递增
                if(dataType.equals("C") || dataType.equals("S") || dataType.equals("B") || dataType.equals("I"))
                    dataType = "i";
                else
                    dataType = "a";
                globalArguments.finalByteCode.add(dataType + "load" + register.stackNum);
                globalArguments.finalByteCode.add("tableswitch");
                for(SwitchData data : switchDataList) {
                    globalArguments.finalByteCode.add(data.hashValue + " " + data.tabName);
                }
                globalArguments.finalByteCode.add("default" + " " + defaultTab);
                globalArguments.finalByteCodePC += switchDataList.size() + 1;
                break;

            case "sparse-switch" :
                //lookupswitch不受具体值影响，编号从1开始按照代码顺序
                if(dataType.equals("C") || dataType.equals("S") || dataType.equals("B") || dataType.equals("I")) {
                    dataType = "i";
                }
                else {
                    dataType = "a";
                }
                globalArguments.finalByteCode.add(dataType + "load" + register.stackNum);
                globalArguments.finalByteCode.add("lookupswitch");
                for(SwitchData data : switchDataList) {
                    globalArguments.finalByteCode.add(data.hashValue + " " + data.tabName);
                }
                globalArguments.finalByteCode.add("default" + " " + defaultTab);
                globalArguments.finalByteCodePC += switchDataList.size() + 1;
                break;
        }
    }
    
    
    public boolean ifUpgrade(ArrayList<String> dexCode, int lineNum){
    	Register register = globalArguments.registerQueue.getByDexName(dexCode.get(1));
    	register.updateType(lineNum, register.currentType);
    	return true;
    }
}
