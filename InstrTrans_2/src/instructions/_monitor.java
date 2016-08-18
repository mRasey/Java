package instructions;

import op.Register;
import op.globalArguments;

import java.util.ArrayList;

import static op.globalArguments.dexCodeNumber;
import static op.globalArguments.registerQueue;

/*锁指令
锁指令多用在多线程程序中对同一对象的操作。Dalvik指令集中有两条锁指令：
"monitor-enter vAA"：为指定的对象获取锁。
“monitor-exit vAA”：释放指定的对象的锁。*/
public class _monitor extends Instruction {

    @Override
    public void analyze(String[] dexCodes) {
        super.analyze(dexCodes);
        Register firstRegister = registerQueue.getByDexName(dexCodes[1]);
        switch (dexCodes[0]){
            case "monitor-enter" :
            	globalArguments.finalByteCode.add("aload" + " " + firstRegister.getType(dexCodeNumber));
            	globalArguments.finalByteCode.add("monitorenter");
            	globalArguments.finalByteCodePC += 2;
                break;
            case "monitor-exit" :
            	globalArguments.finalByteCode.add("aload" + " " + firstRegister.getType(dexCodeNumber));
            	globalArguments.finalByteCode.add("monitorexit");
            	globalArguments.finalByteCodePC += 2;
                break;
        }
    }

    @Override
    public boolean ifUpgrade(ArrayList<String> dexCode, int lineNum) {
        Register firstRegister = globalArguments.registerQueue.getByDexName(dexCode.get(1));
        firstRegister.updateType(lineNum, firstRegister.currentType);
        return true;
    }
}
