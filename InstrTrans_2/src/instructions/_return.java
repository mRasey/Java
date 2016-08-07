package instructions;
/* 返回指令
返回指令指的是函数结尾时运行的最后一条指令。它的基础字节码为teturn，共有以下四条返回指令：
 "return-void"：表示函数从一个void方法返回。
“return vAA”：表示函数返回一个32位非对象类型的值，返回值寄存器为8位的寄存器vAA。
“return-wide vAA”：表示函数返回一个64位非对象类型的值，返回值为8位的寄存器对vAA。
“return-object vAA”：表示函数返回一个对象类型的值。返回值为8位的寄存器vAA。*/
public class _return extends Instruction {

    public _return(String instrName) {
        super(instrName);
    }

    @Override
    public void analyze() {
        switch (dexCodes[0]){
            case "return" :
            case "return-void" :
            case "return-wide" :
            case "return-object" :
        }
    }
}
