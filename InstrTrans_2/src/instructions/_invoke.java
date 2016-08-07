package instructions;
/*方法调用指令
方法调用指令负责调用类实例的方法。它的基础指令为 invoke，方法调用指令有“invoke-kind {vC, vD, vE, vF, vG},meth@BBBB”
与“invoke-kind/range {vCCCC  .. vNNNN},meth@BBBB”两类，两类指令在作用上并无不同，只是后者在设置参数寄存器时使用了range来指定寄存器的范围。
根据方法类型的不同，共有如下五条方法调用指令：
“invoke-virtual” 或 “invoke-virtual/range”调用实例的虚方法。
“invoke-super”或"invoke-super/range"调用实例的父类方法。
“invoke-direct”或“invoke-direct/range”调用实例的直接方法。
“invoke-static”或“invoke-static/range”调用实例的静态方法。
“invoke-interface”或“invoke-interface/range”调用实例的接口方法。
在Android4.0系统中，Dalvik指令集中增加了“invoke-kind/jumbo {vCCCC  .. vNNNN},meth@BBBBBBBB”这类指令，
它与上面介绍的两类指令作用相同，只是在指令中增加了jumbo字节码后缀，且寄存器值与指令的索引取值范围更大。*/
public class _invoke extends Instruction {

    public _invoke(String instrName) {
        super(instrName);
    }

    @Override
    public void analyze() {
        switch (dexCodes[0]) {
            case "invoke-virtual" :
            case "invoke-virtual/jumbo" :
            case "invoke-virtual/range" :
            case "invoke-super" :
            case "invoke-super/jumbo" :
            case "invoke-super/range" :
            case "invoke-direct" :
            case "invoke-direct/jumbo" :
            case "invoke-direct/range" :
            case "invoke-static" :
            case "invoke-static/jumbo" :
            case "invoke-static/range" :
            case "invoke-interface" :
            case "invoke-interface/jumbo" :
            case "invoke-interface/range" :
        }
    }
}
