import java.io.*;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ExtractXML {

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

    public static void main(String[] args) throws IOException {
        ExtractXML.unzipAllDoc("C:/Users/Billy/Desktop/zip/", "test.docx");
    }
}
