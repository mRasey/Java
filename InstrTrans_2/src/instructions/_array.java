package instructions;
/*数组操作指令
数组操作包括获取数组长度，新建数组，数组赋值，数组元素取值与赋值等操作。
“array-length vA, vB”：获取给定vB寄存器中数组的长度并将值赋给vA寄存器，数组长度指的是数组的条目个数。
“new-array vA, vB, type@CCCC”：构造指定类型（type@CCCC）与大小（vB）的数组，并将值赋给vA寄存器。
“filled-new-array {vC, vD, vE, vF, vG},type@BBBB”构造指定类型（type@BBBB）与大小（vA）的数组并填充数组内容。
                                            vA寄存器是隐含使用的，除了指定数组的大小外还指定了参数的个数，vC~vG是使用到的参数寄存序列。
“filled-new-array/range {vCCCC  ..vNNNN}, type@BBBB”指令功能与“filled-new-array {vC, vD, vE, vF, vG},type@BBBB”相同，
                                                        只是参数寄存器使用range字节码后缀指定了取值范围 ，vC是第一个参数寄存器，N = A +C -1。
"fill-array-data vAA, +BBBBBBBB"用指定的数据来填充数组，vAA寄存器为数组引用，引用必须为基础类型的数组，在指令后面会紧跟一个数据表。
"new-array/jumbo vAAAA, vBBBB,type@CCCCCCCC"指令功能与“new-array vA,vB,type@CCCC”相同，只是寄存器值与指令的索引取值范围更大（Android4.0中新增的指令）。
"filled-new-array/jumbo {vCCCC  ..vNNNN},type@BBBBBBBB"指令功能与“filled-new-array/range {vCCCC  ..vNNNN},type@BBBB”相同，只是索引取值范围更大（Android4.0中新增的指令）。
"arrayop vAA, vBB, vCC"对vBB寄存器指定的数组元素进入取值与赋值。vCC寄存器指定数组元素索引，
                        vAA寄存器用来存放读取的或需要设置的数组元素的值。读取元素使用aget类指令，元素赋值使用aput类指定，
                        根据数组中存储的类型指令后面会紧跟不同的指令后缀，指令列表有 aget, aget-wide, aget-object,
                        aget-boolean, aget-byte,aget-char, aget-short, aput, aput-wide, aput-object, aput-boolean,
                        aput-byte, aput-char, aput-short。*/
public class _array extends Instruction{

    public _array(String instrName) {
        super(instrName);
    }

    @Override
    public void analyze() {
        switch (dexCodes[0]) {
            case "array-length" :
            case "new-array" :
            case "filled-new-array" :
            case "filled-new-array/range" :
            case "fill-array-data" :
            case "new-array/jumbo" :
            case "filled-new-array/jumbo" :
            case "arrayop" :
        }
    }
}
