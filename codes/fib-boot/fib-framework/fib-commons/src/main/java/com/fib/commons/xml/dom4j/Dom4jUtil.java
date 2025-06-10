package com.fib.commons.xml.dom4j;

import java.io.OutputStream;
import java.net.URL;

import org.dom4j.Document;
import org.dom4j.DocumentException;
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

	private Document doc;

	public void serializetoXML(OutputStream out, String aEncodingScheme) throws Exception {
		OutputFormat outformat = OutputFormat.createPrettyPrint();
		outformat.setEncoding(aEncodingScheme);
		XMLWriter writer = new XMLWriter(out, outformat);
		writer.write(this.doc);
		writer.flush();
	}

	// private Document doc;
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
