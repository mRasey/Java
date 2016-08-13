package exec;

import java.io.*;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class OpApk {

    //解压提取classes.dex文件
    public void unzipDex(String apkPath, String apkName) throws IOException {
        final int BUFFER = 2048;
        ZipFile zipFile = new ZipFile(apkPath + apkName);
        Enumeration enumeration = zipFile.entries();
        while (enumeration.hasMoreElements()) {
            ZipEntry zipEntry = (ZipEntry) enumeration.nextElement();
            if(zipEntry.getName().equals("classes.dex")){
                BufferedInputStream bis = new BufferedInputStream(zipFile.getInputStream(zipEntry));
                FileOutputStream fos = new FileOutputStream(apkPath + zipEntry.getName());
                BufferedOutputStream bos = new BufferedOutputStream(fos, BUFFER);
                byte[] data = new byte[BUFFER];
                int count;
                while((count = bis.read(data, 0, BUFFER)) != -1) {
                    bos.write(data, 0, count);
                }
                bos.flush();
                bos.close();
                bis.close();
            }
        }
    }

    //调用baksmali.jar获得smali文件
    public void getSmali(String dexFilePath) throws IOException, InterruptedException {
        String baksmaliPath = dexFilePath + "baksmali-2.0.3.jar";
        String outPath = dexFilePath + "classout/";
        Process process = Runtime.getRuntime().exec(
                "java -jar " + baksmaliPath + " -o " + outPath + " " + dexFilePath + "classes.dex");//调用baksmali.jar
        String readLine;
        BufferedReader bf;
        bf = new BufferedReader(new InputStreamReader(process.getInputStream()));
        while((readLine = bf.readLine()) != null) {
            System.out.println(readLine);
        }
        bf = new BufferedReader(new InputStreamReader(process.getErrorStream()));
        while((readLine = bf.readLine()) != null) {
            System.out.println(readLine);
        }
        process.waitFor();
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        String apkPath = "C:/Users/Billy/Desktop/test/";
        String apkName = "app.apk";
        OpApk opApk = new OpApk();
        opApk.unzipDex(apkPath, apkName);//解压dex文件
        opApk.getSmali(apkPath);//获得smali文件

    }
}
