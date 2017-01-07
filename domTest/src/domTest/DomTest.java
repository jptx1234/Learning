package domTest;

import java.io.File;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.dom4j.Attribute;
import org.dom4j.io.SAXReader;
import org.omg.CORBA.OBJ_ADAPTER;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class DomTest {

	public static void main (String[] args) throws Exception {
//		parseXML(new File("WebRoot/bookstore.xml"));
//		create();
//		del();
		t1();
	}
	
	public static void t1(){
		try {
			SAXReader sr = new SAXReader();
			org.dom4j.Document document = sr.read("WebRoot/bookstore.xml");
			List<org.dom4j.Node> nodes = document.selectNodes("//@category");
			for (org.dom4j.Node node : nodes) {
				Attribute attribute = (Attribute) node;
				System.out.println(attribute.getValue());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void create() throws Exception{
		File xmlFile = new File("WebRoot/bookstore.xml");
		Document document = DomUtils.getDocument(xmlFile);
		Element bookEle = document.createElement("book");
		bookEle.setAttribute("shuxing", "computer");
		Element title = document.createElement("title");
		title.setTextContent("书标题");
		title.setAttribute("lang", "zh-cn");
		Element author = document.createElement("author");
		author.setTextContent("vzch");
		Element price=document.createElement("price");
		price.setTextContent("19.90");
		
		bookEle.appendChild(title);
		bookEle.appendChild(author);
		bookEle.appendChild(price);
		
		document.getElementsByTagName("bookstore").item(0).appendChild(bookEle);
		
		DomUtils.domToXml(document, xmlFile);
	}
	
	public static void del() throws Exception{
		File file = new File("WebRoot/bookstore.xml");
		Document document=DomUtils.getDocument(file);
		
		NodeList bookEle=document.getElementsByTagName("book");
		for (int i=0;i< bookEle.getLength();i++){
			NodeList bookChild = bookEle.item(i).getChildNodes();
			for (int j = 0; j < bookChild.getLength(); j++) {
				Node childNode = bookChild.item(j);
				if (childNode.getNodeName().equals("title") && childNode.getTextContent().equals("书标题")) {
					bookEle.item(i).getParentNode().removeChild(bookEle.item(i));
				}
			}
			
		}
		DomUtils.domToXml(document, file);
		
	}
	
	public static void parseXML(File file) throws Exception{
		DocumentBuilderFactory documentBuilderFactory=DocumentBuilderFactory.newInstance();
		DocumentBuilder builder= documentBuilderFactory.newDocumentBuilder();
		Document document=builder.parse(file);
		
		NodeList nodeList=document.getElementsByTagName("book");
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node bookNode=nodeList.item(i);
			StringBuilder sb=new StringBuilder("第"+(i+1)+"本书");
			if (bookNode.hasAttributes()) {
				sb.append("，它还有很多属性，分别为：");
				sb.append(parseAttr(bookNode));
			}
			NodeList bookChildList=bookNode.getChildNodes();
			sb.append(",它的信息为：");
			for (int j = 0; j < bookChildList.getLength(); j++) {
				Node childNode=bookChildList.item(j);
				if (childNode.getNodeType()==Node.ELEMENT_NODE) {
					sb.append(childNode.getNodeName());
					sb.append(":");
					sb.append(childNode.getTextContent());
					if (childNode.hasAttributes()) {
						sb.append("这个信息还有一些属性，分别为");
						sb.append(parseAttr(childNode));
					}
					sb.append(";");
				}
			}
			sb.append("。");
			
			
			System.out.println(sb);
		}
	}
	
	public static String parseAttr(Node node){
		StringBuilder sb = new StringBuilder();
		NamedNodeMap attrMap = node.getAttributes();
		for (int i = 0; i < attrMap.getLength(); i++) {
			Node attr = attrMap.item(i);
			sb.append(attr.getNodeName());
			sb.append(":");
			sb.append(attr.getNodeValue());
			sb.append(",");
		}
		
		return sb.toString();
	}

}
