package instructions;
/*锁指令
锁指令多用在多线程程序中对同一对象的操作。Dalvik指令集中有两条锁指令：
"monitor-enter vAA"：为指定的对象获取锁。
“monitor-exit vAA”：释放指定的对象的锁。*/
public class _monitor extends Instruction {

    public _monitor(String instrName) {
        super(instrName);
    }

    @Override
    public void analyze() {
        switch (dexCodes[0]){
            case "monitor-enter" :
            case "monitor-exit" :
        }
    }
}
