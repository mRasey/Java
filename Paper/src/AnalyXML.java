import org.dom4j.*;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 解析XML文件，加入批注
 */
public class AnalyXML {

    private static final String docPath = "C:\\Users\\Billy\\Desktop\\paper\\";// word文档的位置
    private static final String docName = "test";
    private static final String txtPath = docPath + "check_out.txt";//存储信息的TXT文件路径
    private static final String docXmlPath = docPath + docName + "/word/document.xml";//document.xml的文件路径
    private static final String comXmlPath = "res/comments.xml";//comment.xml的文件路径
    private static final String contentTypeXmlPath = docPath + docName + "/[Content_Types].xml";
    private static final String docXmlRelsPath = docPath + docName + "/word/_rels/document.xml.rels";
    private HashMap<Integer, ArrayList<String>> idToComment = new HashMap<>();
    private File txtFile;
    private File docXmlFile;
    private File comXmlFile;
    private File contentTypeFile;
    private File docXmlRelsFile;
    private int commentIdIndex = 0;//批注的ID，每次增加一
    private String currentTime = "";
    private Element p = DocumentHelper.createElement("w:p");
    private Element r = DocumentHelper.createElement("w:r");

    public AnalyXML() {
        txtFile = new File(AnalyXML.txtPath);
        docXmlFile = new File(AnalyXML.docXmlPath);
        comXmlFile = new File(AnalyXML.comXmlPath);
        contentTypeFile = new File(AnalyXML.contentTypeXmlPath);
        docXmlRelsFile = new File(AnalyXML.docXmlRelsPath);

//        new File(docPath + "result").mkdirs();

        Date date = new Date();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        currentTime = format.format(date).replace(' ', 'T') + "Z";
        System.out.println(currentTime);
    }

    /**
     * 读取段落和评论信息
     * @return this
     * @throws IOException
     */
    public AnalyXML readTxt() throws IOException {
        BufferedReader bfr = new BufferedReader(new FileReader(txtFile));
        String readIn = bfr.readLine();
        commentIdIndex = 0;
        while(readIn != null) {
            System.out.println(readIn);
            if(readIn.contains("id")) {
                readIn = bfr.readLine();
                ArrayList<String> comments = new ArrayList<>();
                if(readIn != null) {
                    do {
                        comments.add(readIn);
                        readIn = bfr.readLine();
                    } while (readIn != null && !readIn.startsWith("id"));
                    idToComment.put(commentIdIndex, comments);
                    commentIdIndex++;
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
    public AnalyXML addCommentsToCom(Element newComment) {
        newComment.addAttribute("w:id", commentIdIndex + "");
        newComment.addElement("w:p");
        Element newP = newComment.element("p");
        Element newR2 = newP.addElement("w:r");
        newR2.addElement("w:t");
        for(String string : idToComment.get(commentIdIndex)) {
            newR2.element("t").addText(string);
        }
        commentIdIndex++;
        return this;
    }

    /**
     * 将批注索引加入document.xml
     * @return this
     */
    public AnalyXML addCommentsToDoc(Element p) {
        List elements = p.elements("r");
        // 插入在第一个w:r之前
        elements.add(0, DocumentHelper.createElement("w:commentRangeStart"));
        p.element("w:commentRangeStart").addAttribute("w:id", commentIdIndex + "");
        elements.add(2, DocumentHelper.createElement("w:commentRangeEnd"));
        p.element("w:commentRangeEnd").addAttribute("w:id", commentIdIndex + "");
        elements.add(3, DocumentHelper.createElement("w:r"));
        Element newR = (Element) p.elements("w:r").get(p.elements("w:r").size() - 1);
        newR.addElement("w:commentReference");
        newR.element("commentReference").addAttribute("w:id", commentIdIndex + "");
        return this;
    }

    /**
     * 如果contentType.xml里缺少comment.xml就将其加入xml中
     * @param root 根节点
     * @return this
     */
    public AnalyXML addToContentType(Element root) {
        List elements = root.elements("Override");
        Iterator iterator = elements.iterator();
        while(iterator.hasNext()) {
            Element element = (Element) iterator.next();
            if(element.attributeValue("PartName").equals("/word/comments/xml")) {//如果含有则直接返回
                System.out.println("include contentType");
                return this;
            }
        }
//        Element element = DocumentHelper.createElement("Override");
//        elements.add(element);

        root.addElement("Override");
        Element element = (Element) root.elements("Override").get(root.elements("Override").size() - 1);
        element.addAttribute("PartName", "/word/comments.xml");
        element.addAttribute("ContentType", "application/vnd.openxmlformats-officedocument.wordprocessingml.comments+xml");
        return this;
    }

    /**
     * 如果document.xml.rels里缺少comment.xml就将其加入xml中
     * @param root 根节点
     * @return this
     */
    public AnalyXML addToDocXmlRels(Element root) {
        List elements = root.elements("Relationship");
        int maxId = 0;
        Iterator iterator = elements.iterator();
        while(iterator.hasNext()) {
            Element element = (Element) iterator.next();
            if(element.attributeValue("Target").equals("comments.xml")) {//如果含有则直接返回
                System.out.println("include docXmlRels");
                return this;
            }
            else {
                int id = Integer.parseInt(element.attributeValue("Id").substring(3));
                if(maxId < id)
                    maxId = id;
            }
        }
//        Element element = DocumentHelper.createElement("Relationship");
//        elements.add(element);

        root.addElement("Relationship");
        Element element = (Element) root.elements("Relationship").get(root.elements("Relationship").size() - 1);
        element.addAttribute("Id", "rId" + (maxId + 1));
        element.addAttribute("Type", "http://schemas.openxmlformats.org/officeDocument/2006/relationships/comments");
        element.addAttribute("Target", "comments.xml");
        return this;
    }

    /**
     * 输出到文件
     * @param document 要输出的内容
     * @return
     * @throws IOException
     */
    public AnalyXML output(Document document, String path) throws IOException {
        OutputFormat format = OutputFormat.createPrettyPrint();
        format.setEncoding("UTF-8");
        XMLWriter writer = new XMLWriter(new OutputStreamWriter(new FileOutputStream(path), "UTF-8"), format);
        writer.write(document);
        writer.flush();
        writer.close();
        return this;
    }

    /**
     * 主方法
     * @throws IOException
     * @throws DocumentException
     */
    public void run() throws IOException, DocumentException {
        ExtractXML.unzipAllDoc(docPath, docName + ".docx");
        readTxt();
        Document docDocument = new SAXReader().read(docXmlFile);
        Document comDocument = new SAXReader().read(comXmlFile);
        Document conDocument = new SAXReader().read(contentTypeFile);
        Document docRelsDocument = new SAXReader().read(docXmlRelsFile);

        commentIdIndex = 0;
        for(int i = 0; i < idToComment.size(); i++) {
            comDocument.getRootElement().addElement("w:comment");
            Element newComment = (Element) comDocument.getRootElement().elements("comment").get(commentIdIndex);
            addCommentsToCom(newComment);
        }

        commentIdIndex = 0;
        Iterator docPNodesItr = docDocument.getRootElement().element("body").elements("p").iterator();
        while(docPNodesItr.hasNext()) {
            Element nextPNode = (Element) docPNodesItr.next();
            addCommentsToDoc(nextPNode);
        }

        addToContentType(conDocument.getRootElement());
        addToDocXmlRels(docRelsDocument.getRootElement());

        //输出修改过的文件
//        output(comDocument, docPath + "result/comments.xml");
//        output(docDocument, docPath + "result/document.xml");
//        output(conDocument, docPath + "result/[Content_Types].xml");
//        output(docRelsDocument, docPath + "result/document.xml.rels");

        //删除原始文件
        docXmlFile.delete();
        contentTypeFile.delete();
        docXmlRelsFile.delete();

        output(comDocument, docPath + docName + "/word/comments.xml");
        output(docDocument, docXmlPath);
        output(conDocument, contentTypeXmlPath);
        output(docRelsDocument, docXmlRelsPath);

        new ExtractXML(docPath + docName, docPath + "result.docx").buildZip();
        ExtractXML.deleteAllDir(docPath, "test");
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