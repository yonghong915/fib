package com.fib.commons.serializer.xml;

import com.fib.commons.serializer.Serializer;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
 * @author fangyh
 * @version 1.0.0
 * @date 2020-12-14
 * @since 1.0.0
 */
public class XmlSerializer implements Serializer {

	@Override
	public <T> byte[] serialize(T obj) {
		XStream xstream = new XStream(new DomDriver());
		String xml = xstream.toXML(obj);
		return xml.getBytes();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T deserialize(byte[] bytes, Class<T> clazz) {
		XStream xstream = new XStream(new DomDriver());
		return (T) xstream.fromXML(new String(bytes));
	}
}
