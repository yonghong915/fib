package com.fib.midbiz.service;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;


public class Dom4jAnnotationParser {
	 // 定义注解
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface XmlElement {
        String name() default "";
        int order() default Integer.MAX_VALUE;
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface XmlList {
        String wrapper() default "";
        String itemName() default "";
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    public @interface XmlRootElement {
        String name();
    }

    // 反序列化工具类
    public static class XmlDeserializer {
        private static final Map<Class<?>, Object> primitiveDefaults = new HashMap<>();

        static {
            primitiveDefaults.put(boolean.class, false);
            primitiveDefaults.put(byte.class, (byte) 0);
            primitiveDefaults.put(short.class, (short) 0);
            primitiveDefaults.put(int.class, 0);
            primitiveDefaults.put(long.class, 0L);
            primitiveDefaults.put(float.class, 0.0f);
            primitiveDefaults.put(double.class, 0.0d);
            primitiveDefaults.put(char.class, '\0');
        }

        @SuppressWarnings("unchecked")
        public static <T> T fromXml(String xml, Class<T> clazz) throws Exception {
            Document document = DocumentHelper.parseText(xml);
            Element root = document.getRootElement();

            // 验证根元素名称
            XmlRootElement rootAnnotation = clazz.getAnnotation(XmlRootElement.class);
            if (rootAnnotation == null) {
                throw new RuntimeException("Class must be annotated with @XmlRootElement");
            }
            if (!rootAnnotation.name().equals(root.getName())) {
                throw new RuntimeException("Root element name mismatch. Expected: " + 
                        rootAnnotation.name() + ", Actual: " + root.getName());
            }

            return (T) parseElement(root, clazz);
        }

        private static Object parseElement(Element element, Class<?> clazz) throws Exception {
            Object instance = clazz.getDeclaredConstructor().newInstance();

            // 获取所有字段并按顺序排序
            List<Field> fields = new ArrayList<>();
            for (Field field : clazz.getDeclaredFields()) {
                if (field.isAnnotationPresent(XmlElement.class)) {
                    fields.add(field);
                }
            }
            fields.sort(Comparator.comparingInt(f -> f.getAnnotation(XmlElement.class).order()));

            // 处理每个字段
            for (Field field : fields) {
                field.setAccessible(true);
                XmlElement annotation = field.getAnnotation(XmlElement.class);
                String elementName = annotation.name().isEmpty() ? field.getName() : annotation.name();

                // 处理集合类型
                if (field.isAnnotationPresent(XmlList.class)) {
                    XmlList listAnnotation = field.getAnnotation(XmlList.class);
                    String wrapperName = listAnnotation.wrapper().isEmpty() ? field.getName() : listAnnotation.wrapper();
                    String itemName = listAnnotation.itemName().isEmpty() ? field.getName() + "Item" : listAnnotation.itemName();

                    Element wrapper = element.element(wrapperName);
                    if (wrapper != null) {
                        List<Element> itemElements = wrapper.elements(itemName);
                        if (!itemElements.isEmpty()) {
                            // 获取集合的泛型类型
                            Type genericType = field.getGenericType();
                            Class<?> itemClass = getCollectionItemClass(genericType);
                            
                            Collection<Object> collection = createCollection(field.getType());
                            
                            for (Element itemElement : itemElements) {
                                Object itemValue = parseValue(itemElement, itemClass);
                                if (itemValue != null) {
                                    collection.add(itemValue);
                                }
                            }
                            field.set(instance, collection);
                        }
                    }
                } 
                // 处理嵌套对象
                else if (!isPrimitiveOrWrapper(field.getType())) {
                    Element childElement = element.element(elementName);
                    if (childElement != null) {
                        Object value = parseElement(childElement, field.getType());
                        field.set(instance, value);
                    }
                }
                // 处理基本类型
                else {
                    Element childElement = element.element(elementName);
                    if (childElement != null) {
                        Object value = parseValue(childElement, field.getType());
                        field.set(instance, value);
                    }
                }
            }
            return instance;
        }

        private static Object parseValue(Element element, Class<?> targetType) {
            String text = element.getTextTrim();
            
            if (text.isEmpty() && targetType.isPrimitive()) {
                return primitiveDefaults.get(targetType);
            }
            
            try {
                if (targetType == String.class) return text;
                if (targetType == int.class || targetType == Integer.class) return Integer.parseInt(text);
                if (targetType == long.class || targetType == Long.class) return Long.parseLong(text);
                if (targetType == double.class || targetType == Double.class) return Double.parseDouble(text);
                if (targetType == float.class || targetType == Float.class) return Float.parseFloat(text);
                if (targetType == boolean.class || targetType == Boolean.class) return Boolean.parseBoolean(text);
                if (targetType == char.class || targetType == Character.class) return text.charAt(0);
                if (targetType == byte.class || targetType == Byte.class) return Byte.parseByte(text);
                if (targetType == short.class || targetType == Short.class) return Short.parseShort(text);
            } catch (NumberFormatException e) {
                System.err.println("Error parsing value for element: " + element.getName() + 
                                   ", value: " + text + ", target type: " + targetType);
            }
            return null;
        }

        private static Class<?> getCollectionItemClass(Type genericType) {
            if (genericType instanceof ParameterizedType) {
                ParameterizedType pt = (ParameterizedType) genericType;
                Type[] actualTypeArguments = pt.getActualTypeArguments();
                if (actualTypeArguments.length > 0 && actualTypeArguments[0] instanceof Class) {
                    return (Class<?>) actualTypeArguments[0];
                }
            }
            return Object.class;
        }

        private static Collection<Object> createCollection(Class<?> collectionType) {
            if (List.class.isAssignableFrom(collectionType)) {
                return new ArrayList<>();
            }
            if (Set.class.isAssignableFrom(collectionType)) {
                return new HashSet<>();
            }
            if (Queue.class.isAssignableFrom(collectionType)) {
                return new LinkedList<>();
            }
            return new ArrayList<>();
        }

        private static boolean isPrimitiveOrWrapper(Class<?> type) {
            return type.isPrimitive() ||
                    type == String.class ||
                    type == Integer.class ||
                    type == Long.class ||
                    type == Double.class ||
                    type == Float.class ||
                    type == Boolean.class ||
                    type == Character.class ||
                    type == Byte.class ||
                    type == Short.class;
        }
    }

    // ===== 示例对象定义 =====
    
    @XmlRootElement(name = "address")
    public static class Address {
        @XmlElement(order = 1)
        private String city;
        
        @XmlElement(order = 2)
        private String street;
        
        public Address() {}
        
        public Address(String city, String street) {
            this.city = city;
            this.street = street;
        }
        
        @Override
        public String toString() {
            return city + ", " + street;
        }
    }

    @XmlRootElement(name = "phone")
    public static class Phone {
        @XmlElement(order = 1)
        private String type;
        
        @XmlElement(order = 2)
        private String number;
        
        public Phone() {}
        
        public Phone(String type, String number) {
            this.type = type;
            this.number = number;
        }
        
        @Override
        public String toString() {
            return type + ": " + number;
        }
    }

    @XmlRootElement(name = "person")
    public static class Person {
        @XmlElement(order = 1, name = "fullName")
        private String name;
        
        @XmlElement(order = 2)
        private int age;
        
        @XmlElement(order = 3)
        private boolean active;
        
        @XmlElement(order = 4)
        @XmlList(wrapper = "addresses", itemName = "address")
        private List<Address> addresses;
        
        @XmlElement(order = 5)
        @XmlList(wrapper = "phones", itemName = "phone")
        private List<Phone> phones;
        
        @XmlElement(order = 6)
        private Address mainAddress;
        
        public Person() {}
        
        public void setName(String name) { this.name = name; }
        public void setAge(int age) { this.age = age; }
        public void setActive(boolean active) { this.active = active; }
        public void setAddresses(List<Address> addresses) { this.addresses = addresses; }
        public void setPhones(List<Phone> phones) { this.phones = phones; }
        public void setMainAddress(Address mainAddress) { this.mainAddress = mainAddress; }
        
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("Person: ").append(name).append("\n");
            sb.append("Age: ").append(age).append("\n");
            sb.append("Active: ").append(active).append("\n");
            sb.append("Main Address: ").append(mainAddress).append("\n");
            sb.append("Addresses:\n");
            for (Address addr : addresses) {
                sb.append("  - ").append(addr).append("\n");
            }
            sb.append("Phones:\n");
            for (Phone phone : phones) {
                sb.append("  - ").append(phone).append("\n");
            }
            return sb.toString();
        }
    }

    // ===== 测试代码 =====
    public static void main(String[] args) {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<person>\n" +
                "  <fullName>John Doe</fullName>\n" +
                "  <age>35</age>\n" +
                "  <active>true</active>\n" +
                "  <addresses>\n" +
                "    <address>\n" +
                "      <city>New York</city>\n" +
                "      <street>5th Ave</street>\n" +
                "    </address>\n" +
                "    <address>\n" +
                "      <city>Boston</city>\n" +
                "      <street>Freedom Trail</street>\n" +
                "    </address>\n" +
                "  </addresses>\n" +
                "  <phones>\n" +
                "    <phone>\n" +
                "      <type>Home</type>\n" +
                "      <number>123-456-7890</number>\n" +
                "    </phone>\n" +
                "    <phone>\n" +
                "      <type>Work</type>\n" +
                "      <number>987-654-3210</number>\n" +
                "    </phone>\n" +
                "  </phones>\n" +
                "  <mainAddress>\n" +
                "    <city>New York</city>\n" +
                "    <street>5th Ave</street>\n" +
                "  </mainAddress>\n" +
                "</person>";

        try {
            System.out.println("XML to parse:");
            System.out.println(xml);
            
            Person person = XmlDeserializer.fromXml(xml, Person.class);
            
            System.out.println("\nParsed object:");
            System.out.println(person);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
