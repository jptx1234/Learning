package domTest;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class DomUtils {

	// 创建一个回写操作的方法
	public static void domToXml(Document document, File file) throws TransformerException {
		// 5.1. 得到一个TransformerFactory对象
		TransformerFactory factory = TransformerFactory.newInstance();
		// 5.2. 通过TransformerFactory得到一个Transformer对象
		Transformer tf = factory.newTransformer();
		// 5.3. 通过Transformer调用transform方法完成回写
		tf.transform(new DOMSource(document), new StreamResult(file));
	}

	// 创建一个获取Document对象
	public static Document getDocument(File file)
			throws ParserConfigurationException, SAXException, IOException {
		// 1.得到dom的解析器工厂
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		// 2.通过解析器工厂得到解决对象
		DocumentBuilder builder = factory.newDocumentBuilder();
		// 3.通过解析器来获取Document对象
		Document document = builder.parse(file);

		return document;
	}
}
