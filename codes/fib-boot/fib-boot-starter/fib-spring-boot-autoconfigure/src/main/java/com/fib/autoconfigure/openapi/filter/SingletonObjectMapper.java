package com.fib.autoconfigure.openapi.filter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;

/**
 * 单例ObjectMapper工具类（带JSON字段排序） 特性： 1. 线程安全的单例实现 2. 字段按字母顺序排序 3.
 * 支持Java8时间类型（LocalDateTime等） 4. 忽略null值、美化输出等通用配置
 */
public class SingletonObjectMapper {
	// ==================== 时间格式常量 ====================
	private static final String LOCAL_DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
	private static final String LOCAL_DATE_PATTERN = "yyyy-MM-dd";
	private static final String LOCAL_TIME_PATTERN = "HH:mm:ss";

	private SingletonObjectMapper() {
	}

	// 静态内部类（懒加载，JVM保证线程安全）
	private static class ObjectMapperHolder {
		// 初始化单例实例（仅在首次调用getInstance时加载）
		private static final ObjectMapper INSTANCE = createSortedObjectMapper();
	}

	// 对外暴露的获取单例方法
	public static ObjectMapper getInstance() {
		return ObjectMapperHolder.INSTANCE;
	}

	/**
	 * 创建带排序功能的ObjectMapper实例 包含通用的序列化配置，适配大多数业务场景
	 */
	private static ObjectMapper createSortedObjectMapper() {
		ObjectMapper objectMapper = JsonMapper.builder().configure(MapperFeature.PROPAGATE_TRANSIENT_MARKER, true).build();

		// 1. 核心：开启JSON字段按字母顺序排序（签名验签必备）
		objectMapper.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true);

		// 2. 忽略null值字段（避免null值影响签名）
		objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

		// 3. 关闭空对象序列化报错（如空List不会报错）
		objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

		// 4. 关闭日期序列化为时间戳（统一为字符串）
		objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		// objectMapper.configure(MapperFeature.PROPAGATE_TRANSIENT_MARKER, true);

		// 5. 配置Java8时间类型（LocalDateTime/LocalDate/LocalTime）序列化规则
		JavaTimeModule javaTimeModule = new JavaTimeModule();
		// LocalDateTime序列化/反序列化
		javaTimeModule.addSerializer(LocalDateTime.class,
				new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(LOCAL_DATE_TIME_PATTERN)));
		javaTimeModule.addDeserializer(LocalDateTime.class,
				new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(LOCAL_DATE_TIME_PATTERN)));
		// LocalDate序列化/反序列化
		javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern(LOCAL_DATE_PATTERN)));
		javaTimeModule.addDeserializer(LocalDate.class,
				new LocalDateDeserializer(DateTimeFormatter.ofPattern(LOCAL_DATE_PATTERN)));
		// LocalTime序列化/反序列化
		javaTimeModule.addSerializer(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ofPattern(LOCAL_TIME_PATTERN)));
		javaTimeModule.addDeserializer(LocalTime.class,
				new LocalTimeDeserializer(DateTimeFormatter.ofPattern(LOCAL_TIME_PATTERN)));
		objectMapper.registerModule(javaTimeModule);

		return objectMapper;
	}

	public static String toSortedJson(Object obj) {
		try {
			return getInstance().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException("JSON序列化失败", e);
		}
	}

	/**
	 * 快速将JSON字符串反序列化为指定类型对象
	 */
	public static <T> T fromJson(String json, Class<T> clazz) {
		try {
			return getInstance().readValue(json, clazz);
		} catch (Exception e) {
			throw new RuntimeException("JSON反序列化失败", e);
		}
	}
}
