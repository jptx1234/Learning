package com.github.jptx1234.productManSysConVer.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

public class Utils {
	public static Document getDocument(File file)
			throws DocumentException {
		SAXReader sr = new SAXReader();
		Document document = sr.read(file);
		return document;
	}

	public static void documentToXml(Document document, File file)
			throws IOException {
		OutputFormat format = OutputFormat.createPrettyPrint(); // 格式 化后的xml
		format.setEncoding("utf-8");

		// OutputFormat format = OutputFormat.createCompactFormat(); // 无格式化的
		XMLWriter writer = new XMLWriter(new FileOutputStream(file), format);
		writer.write(document);
		writer.close();
	}
	
	public static String getRandomUUID(){
		return UUID.randomUUID().toString();
	}
}
