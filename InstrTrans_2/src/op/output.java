package op;

import java.io.BufferedWriter;
import java.io.File;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class output {
	String byteCodeSavePath = "E:\\result.txt";
	
	int outputTypeCodeNumber = 0;
	
	File byteCodeFile = new File(byteCodeSavePath);
    
	public void print() throws IOException{
		FileWriter fw = new FileWriter(byteCodeFile.getAbsoluteFile(),true);
		BufferedWriter bw = new BufferedWriter(fw);
        bw.write(".method "+ globalArguments.methodName +"\n");
        for(;outputTypeCodeNumber<globalArguments.finalByteCode.size();outputTypeCodeNumber++) {
        	bw.write(outputTypeCodeNumber+": " + globalArguments.finalByteCode.get(outputTypeCodeNumber) +"\n");
        }
        bw.write(".end method");
        bw.close();
	}
	

}
