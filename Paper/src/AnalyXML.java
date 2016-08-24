import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
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

    private static final String txtPath = "C:\\Users\\Billy\\Desktop\\paper\\check_out.txt";//存储信息的TXT文件路径
    private static final String docXmlPath = "C:\\Users\\Billy\\Desktop\\paper\\document.xml";//document.xml的文件路径
    private static final String comXmlPath = "C:\\Users\\Billy\\Desktop\\paper\\comments.xml";//comment.xml的文件路径
    private HashMap<PNode, ArrayList<String>> paraIdToComment = new HashMap<>();
    private File txtFile;
    private File docXmlFile;
    private File comXmlFile;
    private int commentIdIndex = 0;//批注的ID，每次增加一
    private String currentTime = "";

    public AnalyXML() {
        txtFile = new File(AnalyXML.txtPath);
        docXmlFile = new File(AnalyXML.docXmlPath);
        comXmlFile = new File(AnalyXML.comXmlPath);

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
                    paraIdToComment.put(new PNode(paraId), comments);
                }
            }
        }
        bfr.close();
        return this;
    }

    /**
     * 判断hashMap中是否含有指定paraId
     * @param paraId 指定paraId
     * @return 找到的pNode或者null
     */
    public PNode hasThisParaId(String paraId) {
        for(PNode pNode : paraIdToComment.keySet()) {
            if(pNode.paraId.equals(paraId))
                return pNode;
        }
        return null;
    }

    /**
     * 将p节点的各种Id保存起来
     * @return this
     */
    public AnalyXML addIdsToHashMap(Element root) {
        Iterator pNodes = root.elements("p").iterator();
        while(pNodes.hasNext()) {
            Element nextP = (Element) pNodes.next();
            PNode pNode = hasThisParaId(nextP.attributeValue("paraId"));
            if(pNode != null) {
                pNode.textId = nextP.attributeValue("textId");
                pNode.rsidR = nextP.attributeValue("rsidR");
                pNode.rsidRDefault = nextP.attributeValue("rsidRDefault");
            }
        }
        return this;
    }

    /**
     * 将批注加入comment.xml
     * @return this
     */
    public AnalyXML addCommentsToCom(Element newComment, PNode pNode) {
        newComment.addAttribute("w:id", commentIdIndex + "");
        newComment.addAttribute("w:author", "BeiHang");
        newComment.addAttribute("w:data", currentTime);
        newComment.addAttribute("w:initials", "Billy");
        newComment.addElement("w:p");
        Element newP = newComment.element("p");
        newP.addAttribute("w14:paraId", pNode.paraId);
        newP.addAttribute("w14:textId", "DONTKNOW");
        newP.addAttribute("w:rsidR", pNode.rsidRDefault);
        newP.addAttribute("w:rsidRDefault", pNode.rsidRDefault);
        newP.addElement("w:pPr");
        newP.element("pPr").addElement("w:pStyle");
        newP.element("pPr").element("pStyle").addAttribute("w:val", "a8");
        Element newR1 = newP.addElement("w:r");
        newR1.addElement("w:rPr");
        newR1.element("rPr").addElement("w:rStyle");
        newR1.element("rPr").element("rStyle").addAttribute("w:hint", "eastAsia");
        newR1.addElement("w:annotationRef");
        Element newR2 = newP.addElement("w:r");
        newR2.addElement("w:rPr");
        newR2.element("rPr").addElement("w:rFonts");
        newR2.element("rPr").element("rFonts").addAttribute("w:hint", "eastAsia");
        newR2.addElement("w:t");
        for(String string : paraIdToComment.get(pNode)) {
            newR2.element("t").addText(string);
        }
        commentIdIndex++;
        return this;
    }

    /**
     * 将批注索引加入document.xml
     * @return this
     */
    public AnalyXML addCommentsToDoc(Element p, String paraId) {
        PNode pNode = hasThisParaId(paraId);
        if(pNode != null) {
            p.addElement("w:commentRangeStart");
            p.element("commentRangeStart").addAttribute("w:id", commentIdIndex + "");
            p.addElement("w:commentRangeEnd");
            p.element("commentRangeEnd").addAttribute("w:id", commentIdIndex + "");
            p.addElement("w:r");
            Element newR = (Element) p.elements("r").get(p.elements("r").size() - 1);
            newR.addElement("w:rPr");
            newR.element("rPr").addElement("w:rStyle");
            newR.element("rPr").element("rStyle").addAttribute("w:val", "a7");
            newR.addElement("w:commentReference");
            newR.element("w:commentReference").addAttribute("w:id", commentIdIndex + "");
            commentIdIndex++;
        }
        return this;
    }

    /**
     * 输出到文件
     * @param document 要输出的内容
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

    /**
     * 主方法
     * @throws IOException
     * @throws DocumentException
     */
    public void run() throws IOException, DocumentException {
        readTxt();
        Document docDocument = new SAXReader().read(docXmlFile);
        Document comDocument = new SAXReader().read(comXmlFile);
        addIdsToHashMap(docDocument.getRootElement().element("body"));
        commentIdIndex = 0;
        for(PNode pNode : paraIdToComment.keySet()) {
            comDocument.getRootElement().addElement("comment");
            Element newComment = (Element) comDocument.getRootElement().elements("comment").get(commentIdIndex);
            addCommentsToCom(newComment, pNode);
        }

        commentIdIndex = 0;
        Iterator docPNodesItr = docDocument.getRootElement().element("body").elements("p").iterator();
        while(docPNodesItr.hasNext()) {
            Element nextPNode = (Element) docPNodesItr.next();
            String paraId = nextPNode.attributeValue("paraId");
            addCommentsToDoc(nextPNode, paraId);
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

class PNode {

    String paraId = "UNKNOW";
    String textId = "UNKNOW";
    String rsidR = "UNKNOW";
    String rsidRDefault = "UNKNOW";

    PNode(String paraId) {
        this.paraId = paraId;
    }

    PNode(String paraId, String textId, String rsidR, String rsidRDefault) {
        this.paraId = paraId;
        this.textId = textId;
        this.rsidR = rsidR;
        this.rsidRDefault = rsidRDefault;
    }
}