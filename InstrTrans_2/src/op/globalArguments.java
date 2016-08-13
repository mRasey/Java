package op;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class globalArguments {
    public static RegisterQueue registerQueue = new RegisterQueue();
    static String smailFilePath = "C:\\MainActivity.smali";
    public static ReadFile rf = new ReadFile(smailFilePath);
    public static int LineNumber = 0;   //编号


    /*		常量池相关变量																	*/
    public static int constNumber = 1;
    //各个小字符串对应的编号
    //static Map <String,Integer> constants = new HashMap<String,Integer>();
    //完整字符串对应的编号
    public static Map <String,Integer> constants = new HashMap<>();
    public static Map <String,String> constantsType = new HashMap<>();

    public static String className = "";
    public static String methodName = "";


    /*		跳转相关变量																	*/

    //记录跳转指令编号和他跳转的标签
    public static Map <Integer,String> jumpAndTab = new HashMap<>();
    //记录标签和他下条指令的编号
    public static Map <String,Integer> tabAndNextInstr = new HashMap<>();
    //记录跳转指令编号与他跳转到目的指令的编号
    public static Map <Integer,Integer> jumpToAim = new HashMap<>();
    //记录每条dex指令的编号和翻译成class指令后对应的编号
    public static Map <Integer,Integer> dexToClass = new HashMap<>();
    //记录最终写入文件的代码
    public static ArrayList<String> finalByteCode = new ArrayList<>();
    public static int finalByteCodePC = 0;

    //linenember -> dex code number
    public static Map <Integer,Integer> lineToNumber = new HashMap<>();

    //遇到method清零
    public static int stackNumber = 0;

    public static int dexCodeNumber = 0; //dex指令编号

    /*		数组相关变量																	*/
    //记录数组标签和他后面的数据
    public static Map <String,ArrayList<String>> arrayData = new HashMap<>();

    //记录switch标签后后面的数据
    public static HashMap<String, ArrayList<String>> switchData = new HashMap<>();
		
}
