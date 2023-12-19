package com.fib.dom4j;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Node;

import com.fib.commons.xml.dom4j.Dom4jParser;

public class Dom4jTest {

	public static void main(String[] args) {
		Dom4jParser parser = Dom4jParser.newBuilder().build();
		try (InputStream in = new FileInputStream(
				"E:\\git_source\\fib-feature-buildenv\\fib\\codes\\fib-boot\\fib-application\\fib-test\\src\\main\\resources\\logback.xml")) {
			Document doc = parser.getDocument(in);
			//System.out.println(doc.asXML());
			List<Node> nodeList = parser.selectNodes(doc,"/default:configuration/default:root");
			System.out.println(nodeList.size());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

}
