import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * 解析XML文件，加入批注
 */
public class AnalyXML {

    private static final String txtPath = "C:\\Users\\Billy\\Desktop\\paper\\check_out.txt";//存储信息的TXT文件路径
    private static final String docXmlPath = "C:\\Users\\Billy\\Desktop\\paper\\document.xml";//document.xml的文件路径
    private static final String comXmlPath = "C:\\Users\\Billy\\Desktop\\paper\\comments.xml";//comment.xml的文件路径
    private HashMap<String, ArrayList<String>> paraIdToComment = new HashMap<>();
    private File txtFile;
    private File docXmlFile;
    private File comXmlFile;
    private int commentIdIndex = 0;//批注的ID，每次增加一
    private String currentTime = "";

    public AnalyXML() {
        txtFile = new File(AnalyXML.txtPath);
        docXmlFile = new File(AnalyXML.docXmlPath);
        comXmlFile = new File(AnalyXML.comXmlPath);

    }

    /**
     * 读取段落和评论信息
     * @return this
     * @throws IOException
     */
    public AnalyXML readTxt() throws IOException {
        BufferedReader bfr = new BufferedReader(new FileReader(txtFile));
        String readIn = bfr.readLine();
        while(readIn != null) {
            System.out.println(readIn);
            if(readIn.contains("paraId")) {
                String paraId = readIn.substring(readIn.indexOf("=") + 1);
                readIn = bfr.readLine();
                ArrayList<String> comments = new ArrayList<>();
                if(readIn != null) {
                    do {
                        comments.add(readIn);
                        readIn = bfr.readLine();
                    } while (readIn != null && !readIn.startsWith("paraId"));
                    paraIdToComment.put(paraId, comments);
                }
            }
        }
        bfr.close();
        return this;
    }

    /**
     * 将批注加入comment.xml
     * @return this
     */
    public AnalyXML addCommentsToCom(Element newComment, String paraId) {
//        for(int i = 0; i < paraIdToComment.size(); i++) {
//            root.addElement("comment");
//        }
//        Iterator commentIterator = root.elements("comment").iterator();
//        while(commentIterator.hasNext()) {
//            Element newComment = (Element) commentIterator.next();
            newComment.addAttribute("id", commentIdIndex + "");
            newComment.addAttribute("author", "BeiHang");
            newComment.addAttribute("data", "2016-08-21T21:56:00Z");
            newComment.addAttribute("initials", "Billy");
            newComment.addElement("p");
            Element newP = newComment.element("p");
            newP.addAttribute("paraId", paraId);
            newP.addAttribute("textId", "DONTKNOW");
            newP.addAttribute("rsidR", "DONTKNOW");
            newP.addAttribute("rsidRDefault", "DONTKNOW");
            newP.addElement("pPr");
            newP.element("pPr").addElement("pStyle");
            newP.element("pPr").element("pStyle").addAttribute("val", "a8");
            Element newR1 = newP.addElement("r");
            newR1.addElement("rPr");
            newR1.element("rPr").addElement("rStyle");
            newR1.element("rPr").element("rStyle").addAttribute("hint", "eastAsia");
            newR1.addElement("annotationRef");
            Element newR2 = newP.addElement("r");
            newR2.addElement("rPr");
            newR2.element("rPr").addElement("rFonts");
            newR2.element("rPr").element("rFonts").addAttribute("hint", "eastAsia");
            newR2.addElement("t");
            for(String string : paraIdToComment.get(paraId)) {
                newR2.element("t").addText(string);
            }
            commentIdIndex++;
//        }
        return this;
    }

    /**
     * 将批注索引加入document.xml
     * @return this
     */
    public AnalyXML addCommentsToDoc(Element p, String paraId) {
        p.addElement("commentRangeStart");
        p.element("commentRangeStart").addAttribute("id", commentIdIndex + "");
        p.addElement("commentRangeEnd");
        p.element("commentRangeEnd").addAttribute("id", commentIdIndex + "");
        return this;
    }

    /**
     * 输出到文件
     * @param document
     * @return
     * @throws IOException
     */
    public AnalyXML output(Document document) throws IOException {
        OutputFormat format = OutputFormat.createPrettyPrint();
        format.setEncoding("UTF-8");
        XMLWriter writer = new XMLWriter(new OutputStreamWriter(new FileOutputStream(comXmlPath + "_result"), "UTF-8"), format);
        writer.write(document);
        writer.flush();
        writer.close();
        return this;
    }

    public void run() throws IOException, DocumentException {
        readTxt();
        Document docDocument = new SAXReader().read(docXmlFile);
        Document comDocument = new SAXReader().read(comXmlFile);
        for(String paraId : paraIdToComment.keySet()) {
            comDocument.getRootElement().addElement("comment");
            Element newComment = (Element) comDocument.getRootElement().elements("comment").get(commentIdIndex);
            addCommentsToCom(newComment, paraId);
        }
        output(comDocument);
//        System.out.println(docDocument.asXML());
    }

    public void test() {
        for(ArrayList<String> arrayList : paraIdToComment.values()) {
            for(String string : arrayList) {
                System.out.println(string);
            }
        }
    }

    /**
     * 主方法
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException, DocumentException {
        new AnalyXML().run();
    }

}
