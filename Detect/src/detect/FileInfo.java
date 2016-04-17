package detect;

public class FileInfo {
    private long lastModified;
    private String lastPath;
    private long lastSpace;
    private String parentPath;

    public FileInfo(long lastModified, String absolutePath, long lastSpace, String parentPath){
        this.lastModified = lastModified;
        lastPath = absolutePath;
        this.lastSpace = lastSpace;
        this.parentPath = parentPath;
    }
    /*获取属性*/
    public long getLastModified(){return lastModified;}
    public String getLastPath(){return lastPath;}
    public long getLastSpace() {return lastSpace;}
    public String getParentPath(){return parentPath;}
    /*设置属性*/
    public void setLastModified(long lastModified){this.lastModified = lastModified;}
    public void setLastPath(String lastPath){this.lastPath = lastPath;}
    public void setLastSpace(long lastSpace){this.lastSpace = lastSpace;}
    public void setParentPath(String parentPath){this.parentPath = parentPath;}
}

