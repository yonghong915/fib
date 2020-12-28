package com.fib.commons.serializer;

import static org.junit.Assert.assertEquals;

import java.io.Serializable;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fib.commons.serializer.jdk.JdkSerializer;
import com.fib.commons.serializer.json.JsonSerializer;
import com.fib.commons.serializer.protostuff.ProtoStuffSerializer;
import com.fib.commons.serializer.xml.XmlSerializer;

public class SerializerTest {
	private Logger logger = LoggerFactory.getLogger(SerializerTest.class);

	@Test
	public void testProtoStuff() {

		User user = new User();
		user.setName("fangyh");
		user.setAge(10);
		logger.info("before serial for protostuff：{}", user);

		byte[] userBytes = SerializationUtils.getInstance().loadSerializerInstance(ProtoStuffSerializer.class)
				.serialize(user);
		User user2 = SerializationUtils.getInstance().loadSerializerInstance(ProtoStuffSerializer.class)
				.deserialize(userBytes, User.class);
		logger.info("deserialize obj for protostuff：{}", user2);

		assertEquals(user.getAge(), user2.getAge());
	}

	@Test
	public void testJdk() {
		User user = new User();
		user.setName("fangyh");
		user.setAge(10);
		logger.info("before serial for jdk：{}", user);

		byte[] userBytes = SerializationUtils.getInstance().loadSerializerInstance(JdkSerializer.class).serialize(user);
		User user2 = SerializationUtils.getInstance().loadSerializerInstance(JdkSerializer.class).deserialize(userBytes,
				User.class);
		logger.info("deserialize obj for jdk：{}", user2);

		assertEquals(user.getAge(), user2.getAge());
	}

	@Test
	public void testJson() {

		User user = new User();
		user.setName("fangyh");
		user.setAge(10);
		logger.info("before serial for json：{}", user);

		byte[] userBytes = SerializationUtils.getInstance().loadSerializerInstance(JsonSerializer.class)
				.serialize(user);
		User user2 = SerializationUtils.getInstance().loadSerializerInstance(JsonSerializer.class)
				.deserialize(userBytes, User.class);
		logger.info("deserialize obj for json：{}", user2);

		assertEquals(user.getAge(), user2.getAge());
	}

	@Test
	public void testXml() {
		User user = new User();
		user.setName("fangyh");
		user.setAge(10);
		logger.info("before serial for xml：{}", user);

		byte[] userBytes = SerializationUtils.getInstance().loadSerializerInstance(XmlSerializer.class).serialize(user);
		User user2 = SerializationUtils.getInstance().loadSerializerInstance(XmlSerializer.class).deserialize(userBytes,
				User.class);
		logger.info("deserialize obj for xml：{}", user2);

		assertEquals(user.getAge(), user2.getAge());
	}

	class User implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private String name;
		private int age;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public int getAge() {
			return age;
		}

		public void setAge(int age) {
			this.age = age;
		}

		@Override
		public String toString() {
			return getClass().getName() + "(name=" + getName() + ",age=" + getAge() + ")";
		}
	}
}
