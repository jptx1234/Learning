package com.github.jptx1234.productManSysConVer.model;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;

import com.github.jptx1234.productManSysConVer.domain.Product;
import com.github.jptx1234.productManSysConVer.exception.NoSuchProductException;
import com.github.jptx1234.productManSysConVer.exception.UUIDNotUniqueException;
import com.github.jptx1234.productManSysConVer.exception.XMLFileException;
import com.github.jptx1234.productManSysConVer.utils.Utils;

public class ProductXMLOPs {
	private File xmlFile = null;
	
	public void setXMLFile (String fileName) throws XMLFileException{
		xmlFile=new File(fileName);
		if (xmlFile.exists()) {
			if (xmlFile.isDirectory()) {
				throw new XMLFileException("指定的XML文件"+fileName+"是个目录");
			}
			Document document = null;
			try {
				document=Utils.getDocument(xmlFile);
			} catch (DocumentException e) {
				throw new XMLFileException(fileName+"读取失败");
			}
			Element rootElement = document.getRootElement();
			if (rootElement == null) {
				rootElement = DocumentHelper.createElement("products");
				document.setRootElement(rootElement);
				try {
					Utils.documentToXml(document, xmlFile);
				} catch (IOException e) {
					throw new XMLFileException(fileName+"初始化失败");
				}
			}else {
				if (!rootElement.getName().equals("products")) {
					throw new XMLFileException(fileName+"所存储的不是本程序的数据文件");
				}
			}
		}else {
			try {
				xmlFile.createNewFile();
			} catch (IOException e) {
				throw new XMLFileException("XML文件"+fileName+"创建失败");
			}
			Document document = DocumentHelper.createDocument(DocumentHelper.createElement("products"));
			try {
				Utils.documentToXml(document, xmlFile);
			} catch (IOException e) {
				throw new XMLFileException(fileName+"初始化失败");
			}
		}
	}
	
	private Product generateProduct(Element element){
		Product product = null;
		if (element != null) {
			product = new Product(element.attributeValue("uuid"),element.elementText("name"),element.elementText("price"),element.elementText("num"),element.elementText("description"));
		}
		
		return product;
	}
	
	public ArrayList<Product> listAllProducts() throws DocumentException{
		ArrayList<Product> products=new ArrayList<Product>();
		Document document = Utils.getDocument(xmlFile);
		List<Node> proList=document.selectNodes("/products/product");
		for (Node node : proList) {
			Element element = (Element)node;
			Product product = new Product();
			product.setUUID(element.attributeValue("id"));
			product.setName(element.elementText("name"));
			product.setPrice(element.elementText("price"));
			product.setNum(element.elementText("num"));
			product.setDescription(element.elementText("description"));
			
			products.add(product);
		}
		
		return products;
	}
	
	public Product findProduct(String uuid) throws DocumentException, UUIDNotUniqueException{
		uuid=fuzzyUUID2realUUID(uuid);
		Document document=Utils.getDocument(xmlFile);
		Element element=(Element)document.selectSingleNode("//product[@id='"+uuid+"']");
		Product product=generateProduct(element);
		
		return product;
	}
	
	
	public Product deleteProduct(String uuid) throws DocumentException, IOException, NoSuchProductException, UUIDNotUniqueException{
		uuid=fuzzyUUID2realUUID(uuid);
		Document document=Utils.getDocument(xmlFile);
		Node node = document.selectSingleNode("//product[@id='"+uuid+"']");
		if (node == null) {
			throw new NoSuchProductException();
		}
		Element element = (Element) node;
		Product product=generateProduct(element);
		if (element != null) {
			element.getParent().remove(element);
		}
		Utils.documentToXml(document, xmlFile);
		
		return product;
	}
	
	public boolean addProduct(Product product) throws DocumentException, IOException{
		Document document = Utils.getDocument(xmlFile);
		if (document.selectSingleNode("//product[@id='"+product.getUUID()+"']") != null) {
			return false;
		}
		Element element = DocumentHelper.createElement("product");
		element.addAttribute("uuid", product.getUUID());
		element.addElement("name").addText(product.getName());
		element.addElement("price").addText(product.getPrice());
		element.addElement("num").addText(product.getNum());
		element.addElement("description").addText(product.getDescription());
		
		document.getRootElement().add(element);
		Utils.documentToXml(document, xmlFile);
		
		return true;
	}
	
	public void updateProduct(Product product) throws DocumentException, IOException{
		Document document = Utils.getDocument(xmlFile);
		Node node = document.selectSingleNode("//product[@id='"+product.getUUID()+"']");
		if (node == null) {
			addProduct(product);
		}else {
			Element element = (Element) node;
			element.element("name").setText(product.getName());
			element.element("price").setText(product.getPrice());
			element.element("num").setText(product.getNum());
			element.element("description").setText(product.getDescription());
			
			Utils.documentToXml(document, xmlFile);
		}
	}
	
	public String fuzzyUUID2realUUID(String fuzzyUUID) throws DocumentException, UUIDNotUniqueException{
		Document document = Utils.getDocument(xmlFile);
		List<Node> nodes= document.selectNodes("//@id");
		String realUUID = null;
		for (Node node : nodes) {
			String thisID = ""+(((Attribute)node).getValue());
			if (thisID.startsWith(fuzzyUUID)) {
				if (realUUID != null) {
					throw new UUIDNotUniqueException(fuzzyUUID);
				}else {
					realUUID = thisID;
				}
			}
		}
		
		return realUUID;
	}
	
}
