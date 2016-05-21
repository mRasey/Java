package oop2;

import java.io.File;

/**
 * Created by cc on 2016/4/13.
 */
public class IF {
    enum IFType{renamed, modified, pathchanged, sizechanged};
    public enum FileType{File,Folder};
    enum mission{recover,recordsummary,recorddetail};
    private mission Mission;
    private IFType IfType;
    private FileType fileType;
    public String Path;
    private String Name;
    public long Capacity;
    public String getMission(){
        return Mission.toString();
    }
    public String getFileType(){
        return fileType.toString();
    }
    public String getTrigger(){return IfType.toString();}
    public String getName(){return Name;}
    public IF(String iftype, String Filetype, String m,String path)throws Exception{
        this.Path = path;
        File name=new File(path);
        Name = name.getName();
        if(Filetype.equals("File")) {
            fileType = FileType.File;
        }
        else{
            fileType = FileType.Folder;
        }
        if(iftype.equals("renamed")){
            IfType = IFType.renamed;
        }else if(iftype.equals("modified")){
            IfType = IFType.modified;
        }else if(iftype.equals("path-changed")){
            IfType = IFType.pathchanged;
        }else if(iftype.equals("size-changed")){
            IfType = IFType.sizechanged;
        }else {
            throw new Exception();
        }
        if(m.equals("recover"))
        {
            Mission = mission.recover;
        }else if(m.equals("record-summary")) {
            Mission = mission.recordsummary;
        }else if(m.equals("record-detail")){
            Mission = mission.recorddetail;
        }else{
            throw new Exception();
        }
        if((IfType.equals(IFType.sizechanged) || IfType.equals(IFType.modified)) && Mission.equals(mission.recover))
        {
            System.out.println("The trigger \""+IfType.toString()+"\" can not be matched with the mission \"recover\"");
            throw new Exception();
        }
        /*if(fileType.equals(FileType.Folder) && (IfType.equals(IFType.pathchanged) || IfType.equals(IFType.renamed))){
            System.out.println("Folder can not be detected this way.");
            throw new Exception();
        }*/
    }
    public String toString(){
        return "IF"+Path+IfType.toString()+"THEN"+Mission.toString();
    }
}
