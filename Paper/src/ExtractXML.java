import java.io.*;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class ExtractXML {

    private FileOutputStream fos;
    private ZipOutputStream zos;
    private byte[] bufs = new byte[1024 * 10];
    private String sourceFilePath;
    private File zipFile;

    public ExtractXML(String sourceFilePath, String zipFileName) throws FileNotFoundException {
        this.sourceFilePath = sourceFilePath;
        zipFile = new File(zipFileName);
        fos = new FileOutputStream(zipFile);
        zos = new ZipOutputStream(new BufferedOutputStream(fos));
    }

    public static void unzipXml(String filePath, String fileName) throws IOException {
        int BUFFER = 2048;
        ZipFile zipFile = new ZipFile(filePath + fileName);
        Enumeration enumeration = zipFile.entries();
        while(enumeration.hasMoreElements()) {
            ZipEntry zipEntry = (ZipEntry) enumeration.nextElement();
            if(zipEntry.getName().equals("word/document.xml")
                    || zipEntry.getName().equals("[Content_Types].xml")
                    || zipEntry.getName().equals("word/_rels/document.xml.rels")) {
                BufferedInputStream bis = new BufferedInputStream(zipFile.getInputStream(zipEntry));
                FileOutputStream fos = new FileOutputStream(filePath + zipEntry.getName().substring(zipEntry.getName().lastIndexOf("/") + 1));
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

    public static void unzipAllDoc(String filePath, String fileName) throws IOException {
        int BUFFER = 2048;
        System.out.println(new File(filePath + fileName.substring(0, fileName.lastIndexOf(".") + 1)).mkdirs());
        ZipFile zipFile = new ZipFile(filePath + fileName);
        Enumeration enumeration = zipFile.entries();
        while(enumeration.hasMoreElements()) {
            ZipEntry zipEntry = (ZipEntry) enumeration.nextElement();
            BufferedInputStream bis = new BufferedInputStream(zipFile.getInputStream(zipEntry));
            System.out.println(zipEntry.getName());
            String absolutePath = zipEntry.getName();
            String dir = fileName.substring(0, fileName.indexOf(".") + 1) + "/" + absolutePath.substring(0, absolutePath.lastIndexOf("/") + 1);
            String name = absolutePath.substring(absolutePath.lastIndexOf("/") + 1);
            new File(filePath + dir).mkdirs();
//            System.out.println(filePath);
//            System.out.println(dir);
//            System.out.println(name);
            FileOutputStream fos = new FileOutputStream(filePath + dir + name);
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

    public static void deleteAllDir(String dirPath, String dirName) {
        File file = new File(dirPath + dirName);
        if(file.isFile()) {
            file.delete();
        }
        else if(file.isDirectory()) {
            File[] files = file.listFiles();
            for(int i = 0; i < files.length; i++) {
                deleteAllDir(dirPath + file.getName() + "/", files[i].getName());
            }
            file.delete();
        }
        else{
            System.out.println(file.getName());
        }
    }

    /**
     * 递归压缩文件夹
     * @param sourceFilePath 根文件夹路径
     * @param relativePath 子文件夹相对根文件夹路径
     * @throws IOException
     */
    public void fileToZip(String sourceFilePath, String relativePath) throws IOException {
        File sourceFile = new File(sourceFilePath);
        if(sourceFile.isFile()) {
//            String zipEntryName = relativePath + sourceFile.getName();
            ZipEntry zipEntry = new ZipEntry(relativePath + sourceFile.getName());
            System.out.println(relativePath + sourceFile.getName());
            zos.putNextEntry(zipEntry);
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(sourceFile));
            int read;
            while((read = bis.read(bufs, 0, 1024 * 10)) != -1) {
                zos.write(bufs, 0, read);
            }
            bis.close();
        }
        else if(sourceFile.isDirectory()) {
            File[] files = sourceFile.listFiles();
            for(int i = 0; i < files.length; i++) {
                if(new File(this.sourceFilePath).equals(sourceFile)) {
                    fileToZip(files[i].getAbsolutePath(), "");
                }
                else
                    fileToZip(files[i].getAbsolutePath(), relativePath + sourceFile.getName() + "/");
            }
        }
        else {
            System.err.println("invalid file!");
        }
    }

    /**
     * 获取压缩文件
     * @return 压缩文件
     * @throws IOException
     */
    public File buildZip() throws IOException {
        fileToZip(sourceFilePath, "");
        zos.close();
        fos.close();
        return zipFile;
    }

    public static void main(String[] args) throws IOException {
//        ExtractXML.unzipAllDoc("C:/Users/Billy/Desktop/zip/", "test.docx");
        ExtractXML.deleteAllDir("C:\\Users\\Billy\\Desktop\\paper\\", "test");
    }
}
