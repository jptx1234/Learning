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

	// ����һ����д�����ķ���
	public static void domToXml(Document document, File file) throws TransformerException {
		// 5.1. �õ�һ��TransformerFactory����
		TransformerFactory factory = TransformerFactory.newInstance();
		// 5.2. ͨ��TransformerFactory�õ�һ��Transformer����
		Transformer tf = factory.newTransformer();
		// 5.3. ͨ��Transformer����transform������ɻ�д
		tf.transform(new DOMSource(document), new StreamResult(file));
	}

	// ����һ����ȡDocument����
	public static Document getDocument(File file)
			throws ParserConfigurationException, SAXException, IOException {
		// 1.�õ�dom�Ľ���������
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		// 2.ͨ�������������õ��������
		DocumentBuilder builder = factory.newDocumentBuilder();
		// 3.ͨ������������ȡDocument����
		Document document = builder.parse(file);

		return document;
	}
}
