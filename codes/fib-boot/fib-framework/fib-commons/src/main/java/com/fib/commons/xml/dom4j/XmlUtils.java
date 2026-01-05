package com.fib.commons.xml.dom4j;

import java.io.StringWriter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

public class XmlUtils {
	public static String toXml(Object obj) throws Exception {
		Document document = DocumentHelper.createDocument();
		Element root = createElement(obj);
		document.setRootElement(root);
		return formatXml(document);
	}

	private static Element createElement(Object obj) throws Exception {
		if (obj == null)
			return null;

		Class<?> clazz = obj.getClass();
		XmlRootElement rootAnnotation = clazz.getAnnotation(XmlRootElement.class);
		String rootName = (rootAnnotation != null && !rootAnnotation.name().isEmpty()) ? rootAnnotation.name() : clazz.getSimpleName();

		Element element = DocumentHelper.createElement(rootName);

		List<Field> fields = getAllFields(clazz);
		fields = fields.stream().filter(f -> f.isAnnotationPresent(XmlElement.class))
				.sorted(Comparator.comparingInt(f -> f.getAnnotation(XmlElement.class).order())).collect(Collectors.toList());

		// 处理所有字段
		for (Field field : fields) {
			field.setAccessible(true);
			Object value = field.get(obj);
			if (value == null)
				continue;

			// 处理属性
			XmlAttribute attr = field.getAnnotation(XmlAttribute.class);
			if (attr != null) {
				String attrName = attr.name().isEmpty() ? field.getName() : attr.name();
				element.addAttribute(attrName, value.toString());
				continue;
			}

			// 处理集合类型
			XmlElementWrapper wrapper = field.getAnnotation(XmlElementWrapper.class);
			if (wrapper != null && value instanceof Collection) {
				Element wrapperElement = element.addElement(wrapper.name());
				for (Object item : (Collection<?>) value) {
					Element child = createElement(item);
					if (child != null)
						wrapperElement.add(child);
				}
				continue;
			}

			// 处理普通元素
			XmlElement xmlElement = field.getAnnotation(XmlElement.class);
			if (xmlElement != null) {
				String elementName = xmlElement.name().isEmpty() ? field.getName() : xmlElement.name();
				if (isSimpleType(value)) {
					Element child = element.addElement(elementName);
					if (xmlElement.isCData()) {
						child.add(DocumentHelper.createCDATA(value.toString()));
					} else {
						child.setText(value.toString());
					}
				} else {
					Element child = createElement(value);
					if (child != null) {
						child.setName(elementName); // 覆盖类名
						element.add(child);
					}
				}
			}
		}
		return element;
	}

	private static List<Field> getAllFields(Class<?> clazz) {
		List<Field> fields = new ArrayList<>();
		while (clazz != null && clazz != Object.class) {
			fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
			clazz = clazz.getSuperclass();
		}
		return fields;
	}

	private static boolean isSimpleType(Object obj) {
		return obj instanceof String || obj instanceof Number || obj instanceof Boolean || obj instanceof Character;
	}

	private static String formatXml(Document document) throws Exception {
		OutputFormat format = OutputFormat.createPrettyPrint();
		format.setEncoding("UTF-8");
		format.setNewLineAfterDeclaration(false);
		StringWriter writer = new StringWriter();
		XMLWriter xmlWriter = new XMLWriter(writer, format);
		xmlWriter.write(document);
		return writer.toString();
	}
}
