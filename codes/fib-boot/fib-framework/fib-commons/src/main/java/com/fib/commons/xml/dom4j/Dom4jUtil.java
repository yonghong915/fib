package com.fib.commons.xml.dom4j;

import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

public class Dom4jUtil {
	public static void main(String[] args) {
		Dom4jParser parser = Dom4jParser.newBuilder().build();
		parser.parse(null);
	}
	
	public static Document parse(URL url) {
		SAXReader reader = new SAXReader();
		Document document = null;
		try {
			document = reader.read(url);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return document;
	}

	public static String parse(Document document) {
		Element rootEle = document.getRootElement();
		for (Iterator<Element> it = rootEle.elementIterator(); it.hasNext();) {
			Element element = it.next();
			// do something
		}

		List<Node> list = document.selectNodes("//foo/bar");
		Node node = document.selectSingleNode("//foo/bar/author");
		String name = node.valueOf("@name");
		return "";
	}

	public void treeWalk(Document document) {
		treeWalk(document.getRootElement());
	}

	public void treeWalk(Element element) {
		for (int i = 0, size = element.nodeCount(); i < size; i++) {
			Node node = element.node(i);
			if (node instanceof Element) {
				treeWalk((Element) node);
			} else {
				// do something…
			}
		}
	}

	public Document createDocument() {
		Document document = DocumentHelper.createDocument();
		Element root = document.addElement("root");

		Element author1 = root.addElement("author").addAttribute("name", "James").addAttribute("location", "UK").addText("James Strachan");

		Element author2 = root.addElement("author").addAttribute("name", "Bob").addAttribute("location", "US").addText("Bob McWhirter");

		return document;
	}

	public void write(Document document) {
		FileWriter out;
		try {
			out = new FileWriter("foo.xml");
			document.write(out);
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void write1(Document document) throws IOException {

		// lets write to a file
		try (FileWriter fileWriter = new FileWriter("output.xml")) {
			XMLWriter writer = new XMLWriter(fileWriter);
			writer.write(document);
			writer.close();
		}

		// Pretty print the document to System.out
//	        OutputFormat format = OutputFormat.createPrettyPrint();
//	        writer = new XMLWriter(System.out, format);
//	        writer.write( document );

		// Compact format to System.out
//	        format = OutputFormat.createCompactFormat();
//	        writer = new XMLWriter(System.out, format);
//	        writer.write(document);
//	        writer.close();
	}

	public void part() {
		String text = "<person> <name>James</name> </person>";
		try {
			Document document = DocumentHelper.parseText(text);
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private Document doc;

	public void serializetoXML(OutputStream out, String aEncodingScheme) throws Exception {
		OutputFormat outformat = OutputFormat.createPrettyPrint();
		outformat.setEncoding(aEncodingScheme);
		XMLWriter writer = new XMLWriter(out, outformat);
		writer.write(this.doc);
		writer.flush();
	}

	//private Document doc;
	private OutputFormat outFormat;

	public void seployFileCreator() {
		this.outFormat = OutputFormat.createPrettyPrint();
	}

	public void seployFileCreator(OutputFormat outFormat) {
		this.outFormat = outFormat;
	}

	public void writeAsXML(OutputStream out) throws Exception {
		XMLWriter writer = new XMLWriter(out, this.outFormat);
		writer.write(this.doc);
	}

	public void writeAsXML(OutputStream out, String encoding) throws Exception {
		this.outFormat.setEncoding(encoding);
		this.writeAsXML(out);
	}
}
