package UsageDemo;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

public class Parser {
    public static void main(String[] args){
        String xmlFileName = "bypass.xml";
        String pathname = "/home/web/Desktop/Java-Sec/XML/DocumentBuilderXXE/src/main/java/XXE/"
                + xmlFileName;
        File f = new File(pathname);
        documentBuilder(f);
    }

    public static void documentBuilder(File f){
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            //解析xml文档，先获取
            Document doc = builder.parse(f);
            //通过user名字来获取dom节点
            NodeList nodeList = doc.getElementsByTagName("user");
            Element e = (Element)nodeList.item(0);
            //获取值
            System.out.println("姓名："+e.getElementsByTagName("name").item(0).getFirstChild().getNodeValue());
            System.out.println("性别："+e.getElementsByTagName("sex").item(0).getFirstChild().getNodeValue());
            System.out.println("年龄："+e.getElementsByTagName("age").item(0).getFirstChild().getNodeValue());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
