import java.io.*;
import java.util.Arrays;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class OperateZipFile {

    private static ZipOutputStream zos;
    private static byte[] bufs = new byte[1024 * 10];

    /**
     * 解压指定压缩文件中的某一个文件
     * @param zipFilePath 要解压的文件路径
     * @param unzipFileName 要解压的文件相对压缩文件的路径
     * @param outputPath 输出文件的路径
     * @throws IOException
     */
    public static void unzipSpecialFile(String zipFilePath, String zipFileName, String unzipFileName, String outputPath) throws IOException {
        int BUFFER = 2048;
        File file = new File(outputPath);
        if(!file.exists())
            file.mkdirs();
        ZipFile zipFile = new ZipFile(zipFilePath + "/" + zipFileName);
        zipFile.stream().forEach(entry -> {
            try {
                if (entry.getName().equals(unzipFileName)) {
                    BufferedInputStream bis = new BufferedInputStream(zipFile.getInputStream(entry));
                    FileOutputStream fos = new FileOutputStream(outputPath + "/" + entry.getName().substring(entry.getName().lastIndexOf("/") + 1));
                    BufferedOutputStream bos = new BufferedOutputStream(fos, BUFFER);
                    byte[] data = new byte[BUFFER];
                    int count;
                    while ((count = bis.read(data, 0, BUFFER)) != -1) {
                        bos.write(data, 0, count);
                    }
                    bos.flush();
                    bos.close();
                    bis.close();
                }
            }
            catch (IOException i) {
                i.printStackTrace();
            }
        });
    }

    public static void unzipAll(String filePath, String fileName, String outputPath) throws IOException {
        int BUFFER = 2048;
        File file = new File(outputPath);
        if(!file.exists())
            file.mkdirs();
        ZipFile zipFile = new ZipFile(filePath + fileName);
        zipFile.stream().forEach(entry -> {
            try {
                BufferedInputStream bis = new BufferedInputStream(zipFile.getInputStream(entry));
                String absolutePath = entry.getName();
                String dir = absolutePath.substring(0, absolutePath.lastIndexOf("/") + 1);
                String name = absolutePath.substring(absolutePath.lastIndexOf("/") + 1);
                if(absolutePath.endsWith("/"))
                    new File(outputPath + "/" + dir).mkdirs();
                else {
                    FileOutputStream fos = new FileOutputStream(outputPath + "/" + dir + "/" + name);
                    BufferedOutputStream bos = new BufferedOutputStream(fos, BUFFER);
                    byte[] data = new byte[BUFFER];
                    int count;
                    while ((count = bis.read(data, 0, BUFFER)) != -1) {
                        bos.write(data, 0, count);
                    }
                    bos.flush();
                    bos.close();
                }
                bis.close();
            }
            catch (IOException i) {
                i.printStackTrace();
            }
        });
    }

    /**
     * 删除指定的文件或文件夹
     * @param dirPath 要删除的文件或文件夹所在的文件夹的绝对路径
     * @param dirName 要删除的文件或文件夹的名称
     */
    public static void deleteAllDir(String dirPath, String dirName) {
        File file = new File(dirPath + "/" + dirName);
        if(file.isFile()) {
            file.delete();
        }
        else if(file.isDirectory()) {
            File[] files = file.listFiles();
            Arrays.stream(files).forEach(inputFile -> deleteAllDir(dirPath + "/" + file.getName() + "/", inputFile.getName()));
            file.delete();
        }
        else{
            System.err.println("error in deleting" + file.getName());
        }
    }

    /**
     * 递归压缩文件夹
     * @param sourceFilePath 根文件夹路径
     * @param relativePath 子文件夹相对根文件夹路径
     * @throws IOException
     */
    public static void fileToZip(String originPath, String sourceFilePath, String relativePath) throws IOException {
        File sourceFile = new File(sourceFilePath);
        if(sourceFile.isFile()) {
            ZipEntry zipEntry = new ZipEntry(originPath.substring(originPath.lastIndexOf("/")) + "/" + relativePath + sourceFile.getName());
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
            if(files != null) {
                if(files.length == 0) {
                    ZipEntry zipEntry = new ZipEntry(originPath.substring(originPath.lastIndexOf("/")) + "/" + relativePath + sourceFile.getName() + "/");
                    zos.putNextEntry(zipEntry);
                }
                else {
                    Arrays.stream(files).forEach(file -> {
                        try {
                            if (new File(originPath).equals(sourceFile)) {
                                fileToZip(originPath, file.getAbsolutePath(), "");
                            } else
                                fileToZip(originPath, file.getAbsolutePath(), relativePath + sourceFile.getName() + "/");
                        } catch (IOException i) {
                            i.printStackTrace();
                        }
                    });
                }
            }
        }
        else {
            System.err.println("invalid file");
        }
    }

    /**
     * 获取压缩文件
     * @param sourceFilePath 要压缩的文件或文件夹的绝对路径
     * @param outputFilePath 要输出的文件或文件夹所在文件夹的绝对路径
     * @return 压缩文件
     * @throws IOException
     */
    public static ZipFile buildZip(String sourceFilePath, String outputFilePath) throws IOException {
        zos = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(outputFilePath)));
        fileToZip(sourceFilePath, sourceFilePath, "");
        zos.close();
        return new ZipFile(outputFilePath);
    }

    public static void main(String[] args) throws IOException {
//        OperateZipFile.deleteAllDir("C:/Users/Billy/Desktop/test/", "test");
//        OperateZipFile.unzipSpecialFile(
//                "C:/Users/Billy/Desktop/test/",
//                "test.zip",
//                "test/test/1.txt",
//                "C:/Users/Billy/Desktop/233/"
//        );
//        OperateZipFile.unzipAll(
//                "C:/Users/Billy/Desktop/test/",
//                "test.zip",
//                "C:/Users/Billy/Desktop/1234567");
        OperateZipFile.buildZip("C:/Users/Billy/Desktop/test/test", "C:/Users/Billy/Desktop/test/test.zip");
    }
}

