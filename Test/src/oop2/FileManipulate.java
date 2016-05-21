package oop2;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by cc on 2016/4/15.
 */
public class FileManipulate {//这个是供给测试者您使用的类哦
    public static void Create(String Path){
        File file = new File(Path);
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                System.out.println("无法创建文件，请检查系统资源管理器。");
            }
        }
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public static void Delete(String Path){
        File file = new File(Path);
        if(file.exists())
        {
            file.delete();
        }
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public static void Rename(String Old,String New){
        File oldfile = new File(Old);
        File newfile = new File(New);
        if(oldfile.exists()){
            if(!newfile.exists()){
                oldfile.renameTo(newfile);
            }else{
                System.out.println("The new name has been used.");
            }
        }
        else{
            System.out.println("The original file didn't exist.");
        }
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public static void ChangePath(String Old,String New){
        File oldfile = new File(Old);
        File newfile = new File(New);
        if(oldfile.exists()){
            if(!newfile.exists()){
                oldfile.renameTo(newfile);
            }else{
                System.out.println("The new name has been used.");
            }
        }
        else{
            System.out.println("The original file didn't exist.");
        }
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public static void WriteToFile(String Path, String str){
        File file = new File(Path);
        if(file.exists()){
            if(file.canWrite()){
                try {
                    FileWriter fw = new FileWriter(Path);
                    fw.write(str);
                    fw.close();
                } catch (IOException e) {
                }
            }
            else {
                System.out.println("This file cannot write.");
            }
        }else{
            System.out.println("The original file didn't exist.");
        }
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public static void WriteToFile(String Path, String str,boolean Continue){
        File file = new File(Path);
        if(file.exists()){
            if(file.canWrite()){
                try {
                    FileWriter fw = new FileWriter(Path,Continue);
                    fw.write(str);
                    fw.close();
                } catch (IOException e) {
                }
            }
            else {
                System.out.println("This file cannot write.");
            }
        }else{
            System.out.println("The original file didn't exist.");
        }
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
