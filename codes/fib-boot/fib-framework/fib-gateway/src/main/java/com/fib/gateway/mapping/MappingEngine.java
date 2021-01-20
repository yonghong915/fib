package com.fib.gateway.mapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.fib.commons.util.ClassUtil;
import com.fib.gateway.mapping.config.FieldMappingRule;
import com.fib.gateway.mapping.config.MappingRule;
import com.fib.gateway.message.bean.MessageBean;
import com.fib.gateway.message.util.StringUtil;
import com.fib.gateway.message.xml.message.MultiLanguageResourceBundle;
import com.fib.gateway.message.xml.script.BeanShellEngine;

import com.fib.gateway.message.util.MapUtil;

/**
 * MB映射器
 * 
 */
public class MappingEngine {
	/**
	 * BeanShell执行器
	 */
	// private Interpreter bsh = null;
	private BeanShellEngine bsh = null;

	/**
	 * 脚本执行上下文。用于存储外部传入脚本的变量
	 */
	private Map context = new HashMap();

	/**
	 * 源对象：MB/MAP
	 */
	private Object sourceObject = null;

	public Object map(Object sourceObject, MappingRule rule) {
		if (null == rule) {
			// throw new RuntimeException("Mapping Rule is NULL!");
			throw new RuntimeException(
					MultiLanguageResourceBundle.getInstance().getString("MappingEngine.map.mappingRule.null"));
		}
		if (null == sourceObject) {
			// throw new RuntimeException(
			// "Source Object Which Need Mapping is NULL!");
			throw new RuntimeException(
					MultiLanguageResourceBundle.getInstance().getString("MappingEngine.map.sourceObject.null"));
		}
		if (!(sourceObject instanceof Map || sourceObject instanceof MessageBean)) {
			// throw new RuntimeException(
			// "sourceObject should be a Map or a MessageBean! but now it's "
			// + sourceObject.getClass().getName());
			throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString(
					"MappingEngine.map.sourceObject.wrong", new String[] { sourceObject.getClass().getName() }));
		}
		this.sourceObject = sourceObject;
		// this.context.clear();

		Object destObject = createTarget(rule);
		context.put("target", destObject);
		List fieldMappingRules = rule.getFieldMappingRules();
		int i = 0;
		try {
			for (i = 0; i < fieldMappingRules.size(); i++) {
				fieldMapping(sourceObject, destObject, (FieldMappingRule) fieldMappingRules.get(i));
			}
		} catch (Exception e) {
			e.printStackTrace();
			FieldMappingRule fRule = (FieldMappingRule) fieldMappingRules.get(i);
			// throw new RuntimeException("Field Mapping Error: "
			// + fRule.toString() + ":" + e.getMessage(), e);
			throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString(
					"MappingEngine.map.fieldMapping.error", new String[] { fRule.toString(), e.getMessage() }));
		}

		List manualMappingRules = rule.getScripts();
		for (i = 0; i < manualMappingRules.size(); i++) {
			this.executeScript(sourceObject, (String) manualMappingRules.get(i));
		}
		return destObject;
	}

	// public void clearContext() {
	// context.clear();
	// // 清除bsh中相关变量
	// if (bsh != null) {
	// Iterator it = context.keySet().iterator();
	// while (it.hasNext()) {
	// // try {
	// bsh.unset((String) it.next());
	// // } catch (EvalError e) {
	// // e.printStackTrace();
	// // }
	// }
	// }
	// }

	public void setParameter(String name, Object value) {
		context.put(name, value);
		// 新增的参数，设置到bsh
		if (bsh != null) {
			// try {
			bsh.set(name, value);
			// } catch (EvalError e) {
			// // throw new RuntimeException(
			// // "set global param[" + name
			// // + "] to BeanShell Interpreter failed:"
			// // + e.getMessage(), e);
			// throw new RuntimeException(MultiLanguageResourceBundle
			// .getInstance().getString(
			// "MappingEngine.setGlobalParameter.error",
			// new String[] { name, e.getMessage() }), e);
			// }
		}
	}

	private Object createTarget(MappingRule rule) {
		if (MappingRule.MAP == rule.getTargetType()) {
			return new HashMap(64);
		} else if (MappingRule.MB == rule.getTargetType()) {
			ClassUtil.setClassLoader(Thread.currentThread().getContextClassLoader());
			return ClassUtil.createClassInstance(rule.getTargetClass());
		} else {
			// throw new RuntimeException("Unkown Target Type!");
			throw new RuntimeException(MultiLanguageResourceBundle.getInstance()
					.getString("MappingEngine.createTarget.targetType.unkown"));
		}
	}

	private void fieldMapping(Object sourceObject, Object destObject, FieldMappingRule rule) {
		switch (rule.getType()) {
		case FieldMappingRule.VALUE_MAPPING:
			valueMapping(sourceObject, destObject, rule, false);
			break;
		case FieldMappingRule.VALUE:
			assignValue(destObject, rule, false);
			break;
		case FieldMappingRule.VALUE_TRANSLATE_MAPPING:
			valueMapping(sourceObject, destObject, rule, true);
			break;
		case FieldMappingRule.SCRIPT:
			assignValue(destObject, rule, true);
			break;
		default:
			throw new RuntimeException("Unkown Mapping Type");
		}
	}

	private void valueMapping(Object src, Object dest, FieldMappingRule aRule, boolean needTranslate) {
		String srcFieldPath = aRule.getFrom();
		String destFieldPath = aRule.getTo();

		// 检查字段路径中是否有List
		int firstListIndexInSource = srcFieldPath.indexOf("[]");
		int firstListIndexInDest = destFieldPath.indexOf("[]");

		if (null != aRule.getSourceParameter()) {
			Object currentSource = context.get(aRule.getSourceParameter());
			if (null == currentSource) {
				// throw new RuntimeException("Can't find parameter["
				// + aRule.getSourceParameter() + "] in Context");
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString(
						"MappingEngine.valueMapping.currentSource.null", new String[] { aRule.getSourceParameter() }));
			}
			src = currentSource;
		}

		if (-1 == firstListIndexInDest) {
			// 路径中不含List，直接赋值
			Object value = getValue(src, srcFieldPath);
			if (needTranslate) {
				// if (null != aRule.getTranslateRuleId()) {
				// value = ValueTranslateRuleManager.getValue(aRule
				// .getTranslateRuleId(), value);
				// } else {
				Object targetValue = aRule.getTranslateRules().get(value);
				// }
				if (targetValue == null) {
					if (null != aRule.getDefaultValue()) {
						targetValue = aRule.getDefaultValue();
					} else {
						// throw new RuntimeException(
						// "value-translate: can't find source value["
						// + targetValue + "]'s target value!");
						throw new RuntimeException(MultiLanguageResourceBundle.getInstance()
								.getString("MappingEngine.valueMapping.value.null", new String[] { "" + targetValue }));
					}

				}
				value = targetValue;
			} else {
				if (null != aRule.getValue() && 0 < aRule.getValue().trim().length()) {
					value = executeScript("currentValue", value, aRule.getValue());
				}
			}
			setValue(dest, destFieldPath, value, aRule.getTargetClass(), aRule.isForcibleTypeConversion());
		} else {
			// 路径中含List，需取出List，循环赋值
			if (firstListIndexInSource + 3 >= srcFieldPath.length()) {
				// throw new RuntimeException("source field[" + aRule.getFrom()
				// + "] is List, need element's name!");
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString(
						"MappingEngine.valueMapping.sourceField.list.elementName.null",
						new String[] { aRule.getFrom() }));
			}
			if (firstListIndexInDest + 3 >= destFieldPath.length()) {
				// throw new RuntimeException("target field[" + aRule.getTo()
				// + "] is List, need element's name!");
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString(
						"MappingEngine.valueMapping.targetField.list.elementName.null",
						new String[] { aRule.getTo() }));
			}

			String srcListPath = srcFieldPath.substring(0, firstListIndexInSource);
			String destListPath = destFieldPath.substring(0, firstListIndexInDest);

			// 取得源对象中的List
			Object srcListObject = getValue(src, srcListPath);
			if (null == srcListObject) {
				// System.out.println("warning! source List[" + srcListPath
				// + "] is null!");
				System.out.println(MultiLanguageResourceBundle.getInstance()
						.getString("MappingEngine.valueMapping.sourceList.null", new String[] { srcListPath }));
				return;
			}
			if (!(srcListObject instanceof List)) {
				// throw new RuntimeException("source field[" + srcListPath
				// + "] is not a List! :"
				// + srcListObject.getClass().getName() + " : "
				// + srcListObject);
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString(
						"MappingEngine.valueMapping.sourceList.notList",
						new String[] { srcListPath, srcListObject.getClass().getName(), "" + srcListObject }));
			}
			List srcList = (List) srcListObject;

			// Object parent = null;
			// if (-1 == destListPath.indexOf('.')) {
			// parent = dest;
			// } else {
			// parent = getDestinationObject(dest, destListPath.substring(0,
			// destListPath.lastIndexOf('.')));
			// }
			// List destList = (List) getDestinationObject(dest, destListPath);
			List destList = null;
			Object destListObject = getValue(dest, destListPath);
			if (null == destListObject) {
				// 仅目标对象为Map时才有可能
				destList = new ArrayList();
				setValue(dest, destListPath, destList);
			} else if (!(destListObject instanceof List)) {
				// throw new RuntimeException("target field[" + destListPath
				// + "] is not a List! :"
				// + destListObject.getClass().getName() + " : "
				// + destListObject);
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString(
						"MappingEngine.valueMapping.targetList.notList",
						new String[] { destListPath, destListObject.getClass().getName(), "" + destListObject }));
			} else {
				destList = (List) destListObject;
			}

			Object srcObject = null;
			Object destObject = null;
			for (int i = 0; i < srcList.size(); i++) {
				srcObject = srcList.get(i);
				destObject = null;
				if (i < destList.size()) {
					destObject = destList.get(i);
				} else {
					// destObject = getSubObjectInList(parent, destFieldPath
					// .substring(0, firstListIndexInDest));
					destObject = createListElement(dest, destListPath);
				}
				FieldMappingRule newRule = new FieldMappingRule();
				newRule.setFrom(srcFieldPath.substring(firstListIndexInSource + 3));
				newRule.setTo(destFieldPath.substring(firstListIndexInDest + 3));

				// if (null == aRule.getValue()
				// || 0 == aRule.getValue().trim().length()) {
				if (FieldMappingRule.VALUE_MAPPING == aRule.getType()
						|| FieldMappingRule.VALUE_TRANSLATE_MAPPING == aRule.getType()) {
					if (needTranslate) {
						newRule.setType(FieldMappingRule.VALUE_TRANSLATE_MAPPING);
						newRule.setTranslateRules(aRule.getTranslateRules());
						newRule.setDefaultValue(aRule.getDefaultValue());
					} else {
						newRule.setType(FieldMappingRule.VALUE_MAPPING);
					}
					newRule.setValue(aRule.getValue());
				} else {
					newRule.setType(FieldMappingRule.VALUE);
					newRule.setValue(aRule.getValue());
				}

				fieldMapping(srcObject, destObject, newRule);
			}
		}
	}

	private Object createListElement(Object dest, String destListPath) {
		if (dest instanceof Map) {
			List list = (List) MapUtil.getValue((Map) dest, destListPath);
			Map record = new HashMap();
			list.add(record);
			return record;
		} else {
			int index = destListPath.lastIndexOf(".");
			Object parent = null;
			String attributeName = null;
			if (-1 == index) {
				parent = dest;
				attributeName = destListPath;
			} else {
				parent = ClassUtil.getBeanValueByPath(dest, destListPath.substring(0, index));
				attributeName = destListPath.substring(index + 1);
			}
			Object record = ClassUtil.invoke(parent, "create" + StringUtil.toUpperCaseFirstOne(attributeName), null,
					null);
			return record;
		}
	}

	private void assignValue(Object dest, FieldMappingRule rule, boolean isScript) {
		Object value = rule.getValue();

		String destFieldPath = rule.getTo();
		if (isScript) {
			// 脚本则执行之，以返回值做值
			value = executeScript(sourceObject, (String) value);
		} else {
			// 值映射，则做值的参数替换
			value = replaceVariable((String) value);
		}

		setValue(dest, destFieldPath, value, rule.getTargetClass(), rule.isForcibleTypeConversion());
	}

	/**
	 * 替换值变量，即值为一个由${}标出的Context中的变量
	 * 
	 * @param value
	 * @return
	 */
	private Object replaceVariable(String value) {
		if (null == value || 0 == value.length()) {
			return null;
		}
		int startIndex = value.indexOf("${");
		if (startIndex == -1) {
			return value;
		}
		startIndex += 2;
		int endIndex = value.indexOf("}", startIndex);
		if (-1 == endIndex) {
			// 非变量表达式，不报错
			return value;
		}
		String variableName = value.substring(startIndex, endIndex).trim();
		Object variableValue = context.get(variableName);
		if (null == variableValue) {
			// throw new RuntimeException("Can't find variable :" + value);
			throw new RuntimeException(MultiLanguageResourceBundle.getInstance()
					.getString("MappingEngine.replaceVariable.variable.null", new String[] { value }));
		}
		return variableValue;

		// 变量类型不全是String，因此不能做字符串处理，如需实现：
		// [前缀字符串]${变量名}[后缀字符串]
		// 必须用脚本！
		// StringBuffer buf = new StringBuffer(128);
		// if (startIndex > 0) {
		// buf.append(value.substring(0, startIndex));
		// }
		// int endIndex = -1;
		// String variableName = null;
		// Object variableValue = null;
		// while (startIndex != -1) {
		// startIndex += 2;
		// endIndex = value.indexOf("}", startIndex);
		// if(-1==endIndex){
		//
		// }
		// variableName = value.substring(startIndex, endIndex).trim();
		// variableValue = context.get(variableName);
		// buf.append()
		// }
		//
		// return buf.toString();
	}

	/**
	 * 取对象的字段值
	 * 
	 * @param o
	 * @param name
	 * @return
	 */
	private Object getValue(Object o, String name) {
		if (o instanceof MessageBean) {
			return ClassUtil.getBeanValueByPath(o, name);
		} else {
			return MapUtil.getValue((Map) o, name);
		}
	}

	/**
	 * 设置对象的字段值
	 * 
	 * @param obj
	 * @param name
	 * @param value
	 */
	private void setValue(Object obj, String name, Object value) {
		if (obj instanceof Map) {
			MapUtil.setValue((Map) obj, name, value);
		} else {
			ClassUtil.setBeanValueByPath(obj, name, value);
		}
	}

	/**
	 * 设置对象的字段值，带类型转换
	 * 
	 * @param o
	 * @param name
	 * @param value
	 * @param targetClass
	 * @param force
	 */
	private void setValue(Object o, String name, Object value, Class targetClass, boolean force) {
		if (o instanceof Map) {
			value = dataTypeConvert(value, targetClass, force);
			MapUtil.setValue((Map) o, name, value);
		} else {
			// 取得Bean属性name的Class
			targetClass = getTargetClass((MessageBean) o, name);
			// 类型转换
			value = dataTypeConvert(value, targetClass, force);
			ClassUtil.setBeanValueByPath(o, name, value, targetClass);
		}
	}

	private Class getTargetClass(MessageBean mb, String name) {
		int index = name.lastIndexOf(".");
		Object parent = null;
		String attributeName = null;
		if (-1 == index) {
			parent = mb;
			attributeName = name;
		} else {
			parent = ClassUtil.getBeanValueByPath(mb, name.substring(0, index));
			attributeName = name.substring(index + 1);
		}
		return ClassUtil.getBeanFieldClass(parent, attributeName);
	}

	/**
	 * 数据类型转换
	 * 
	 * @param value
	 * @param targetClass
	 * @param force
	 * @return
	 */
	private Object dataTypeConvert(Object value, Class targetClass, boolean force) {
		if (null == value || null == targetClass) {
			return value;
		}
		// 值的类型与目标相同，无需转换
		if (value.getClass().equals(targetClass)) {
			return value;
		}
		// byte[]必须严格匹配
		if (byteArray.getClass().equals(value.getClass())) {
			if (!byteArray.getClass().equals(targetClass)) {
				// throw new RuntimeException("value[" + value
				// + "] is byte[]! but targetClass is "
				// + targetClass.getName());
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString(
						"MappingEngine.dataTypeConvert.byte[].class.wrong",
						new String[] { "" + value, targetClass.getName() }));
			}
			return value;
		}
		// 目标类型是字符串，直接转换
		if (targetClass.equals(String.class)) {
			return value.toString();
		}
		if (value.getClass().equals(String.class)) {
			value = convertString2Other((String) value, targetClass, force);
		} else if (value.getClass().equals(Integer.class)) {
			value = convertInt2Other((Integer) value, targetClass, force);
		} else if (value.getClass().equals(Long.class)) {
			value = convertLong2Other((Long) value, targetClass, force);
		} else if (value.getClass().equals(Double.class)) {
			value = convertDouble2Other((Double) value, targetClass, force);
		} else if (value.getClass().equals(Float.class)) {
			value = convertFloat2Other((Float) value, targetClass, force);
		} else if (value.getClass().equals(Short.class)) {
			value = convertShort2Other((Short) value, targetClass, force);
		} else if (value.getClass().equals(Byte.class)) {
			value = convertByte2Other((Byte) value, targetClass, force);
		} else {
			// throw new RuntimeException("Unsupport value[" + value
			// + "] dataType!" + value.getClass().getName());
			throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString(
					"MappingEngine.dataTypeConvert.dataTypeValue.unsupport",
					new String[] { "" + value, value.getClass().getName() }));
		}

		return value;
	}

	private Object convertString2Other(String value, Class targetClass, boolean force) {
		if (byteArray.getClass().equals(targetClass)) {
			// throw new RuntimeException("String can't convert to byte[]!");
			throw new RuntimeException(MultiLanguageResourceBundle.getInstance()
					.getString("MappingEngine.convertString2Other.canNotToByte[]"));
		} else if (Double.class.equals(targetClass) || Double.TYPE.equals(targetClass)) {
			return new Double(value);
		} else if (Float.class.equals(targetClass) || Float.TYPE.equals(targetClass)) {
			return new Float(value);
		} else if (Long.class.equals(targetClass) || Long.TYPE.equals(targetClass)) {
			return new Long(value);
		} else if (Integer.class.equals(targetClass) || Integer.TYPE.equals(targetClass)) {
			return new Integer(value);
		} else if (Short.class.equals(targetClass) || Short.TYPE.equals(targetClass)) {
			return new Short(value);
		} else {
			// throw new RuntimeException("String can't convert to "
			// + targetClass.getName());
			throw new RuntimeException(MultiLanguageResourceBundle.getInstance()
					.getString("MappingEngine.convertString2Other.canNotTo", new String[] { targetClass.getName() }));
		}
	}

	private Object convertByte2Other(Byte value, Class targetClass, boolean force) {
		if (byteArray.getClass().equals(targetClass)) {
			// throw new RuntimeException("Byte can't convert to byte[]!");
			throw new RuntimeException(MultiLanguageResourceBundle.getInstance()
					.getString("MappingEngine.convertByte2Other.canNotToByte[]"));
		} else if (Byte.TYPE.equals(targetClass)) {
			return value;
		} else if (Double.class.equals(targetClass) || Double.TYPE.equals(targetClass)) {
			return new Double(value);
		} else if (Float.class.equals(targetClass) || Float.TYPE.equals(targetClass)) {
			return new Float(value);
		} else if (Long.class.equals(targetClass) || Long.TYPE.equals(targetClass)) {
			return Long.valueOf(value);
		} else if (Integer.class.equals(targetClass) || Integer.TYPE.equals(targetClass)) {
			return Integer.valueOf(value);
		} else if (Short.class.equals(targetClass) || Short.TYPE.equals(targetClass)) {
			return Short.valueOf(value);
		} else {
			// throw new RuntimeException("Byte can't convert to "
			// + targetClass.getName());
			throw new RuntimeException(
					MultiLanguageResourceBundle.getInstance().getString("MappingEngine.convertByte2Other.canNotTo"));
		}
	}

	private Object convertShort2Other(Short value, Class targetClass, boolean force) {
		if (byteArray.getClass().equals(targetClass)) {
			// throw new RuntimeException("Short can't convert to byte[]!");
			throw new RuntimeException(MultiLanguageResourceBundle.getInstance()
					.getString("MappingEngine.convertShort2Other.canNotToByte[]"));
		} else if (Short.TYPE.equals(targetClass)) {
			return value;
		} else if (Double.class.equals(targetClass) || Double.TYPE.equals(targetClass)) {
			return new Double(value);
		} else if (Float.class.equals(targetClass) || Float.TYPE.equals(targetClass)) {
			return new Float(value);
		} else if (Long.class.equals(targetClass) || Long.TYPE.equals(targetClass)) {
			return Long.valueOf(value);
		} else if (Integer.class.equals(targetClass) || Integer.TYPE.equals(targetClass)) {
			return Integer.valueOf(value);
		} else if (Byte.class.equals(targetClass) || Byte.TYPE.equals(targetClass)) {
			if (!force) {
				// throw new
				// RuntimeException("Short shouldn't convert to Byte!");
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance()
						.getString("MappingEngine.convertShort2Other.shouldNotToByte"));
			}
			return Byte.valueOf(value.byteValue());
		} else {
			// throw new RuntimeException("Short can't convert to "
			// + targetClass.getName());
			throw new RuntimeException(MultiLanguageResourceBundle.getInstance()
					.getString("MappingEngine.convertShort2Other.canNotTo", new String[] { targetClass.getName() }));
		}
	}

	private Object convertFloat2Other(Float value, Class targetClass, boolean force) {
		if (byteArray.getClass().equals(targetClass)) {
			// throw new RuntimeException("Float can't convert to byte[]!");
			throw new RuntimeException(MultiLanguageResourceBundle.getInstance()
					.getString("MappingEngine.convertFloat2Other.canNotToByte[]"));
		} else if (Float.TYPE.equals(targetClass)) {
			return value;
		} else if (Long.class.equals(targetClass) || Long.TYPE.equals(targetClass)) {
			if (!force) {
				// throw new
				// RuntimeException("Float shouldn't convert to Long!");
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance()
						.getString("MappingEngine.convertFloat2Other.shouldNotToLong"));
			}
			return Long.valueOf(value.longValue());
		} else if (Double.class.equals(targetClass) || Double.TYPE.equals(targetClass)) {
			return Double.valueOf(value);
		} else if (Integer.class.equals(targetClass) || Integer.TYPE.equals(targetClass)) {
			if (!force) {
				// throw new RuntimeException(
				// "Float shouldn't convert to Integer!");
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance()
						.getString("MappingEngine.convertFloat2Other.shouldNotToInt"));
			}
			return Integer.valueOf(value.intValue());
		} else if (Short.class.equals(targetClass) || Short.TYPE.equals(targetClass)) {
			if (!force) {
				// throw new
				// RuntimeException("Float shouldn't convert to Short!");
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance()
						.getString("MappingEngine.convertFloat2Other.shouldNotToShort"));
			}
			return Short.valueOf(value.shortValue());
		} else if (Byte.class.equals(targetClass) || Byte.TYPE.equals(targetClass)) {
			if (!force) {
				// throw new
				// RuntimeException("Float shouldn't convert to Byte!");
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance()
						.getString("MappingEngine.convertFloat2Other.shouldNotToByte"));
			}
			return Byte.valueOf(value.byteValue());
		} else {
			// throw new RuntimeException("Float can't convert to "
			// + targetClass.getName());
			throw new RuntimeException(
					MultiLanguageResourceBundle.getInstance().getString("MappingEngine.convertFloat2Other.canNotTo"));
		}
	}

	private Object convertDouble2Other(Double value, Class targetClass, boolean force) {
		if (byteArray.getClass().equals(targetClass)) {
			// throw new RuntimeException("Double can't convert to byte[]!");
			throw new RuntimeException(MultiLanguageResourceBundle.getInstance()
					.getString("MappingEngine.convertDouble2Other.canNotToByte[]"));
		} else if (Double.TYPE.equals(targetClass)) {
			return value;
		} else if (Long.class.equals(targetClass) || Long.TYPE.equals(targetClass)) {
			if (!force) {
				// throw new
				// RuntimeException("Double shouldn't convert to Long!");
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance()
						.getString("MappingEngine.convertDouble2Other.shouldNotToLong"));
			}
			return Long.valueOf(value.longValue());
		} else if (Float.class.equals(targetClass) || Float.TYPE.equals(targetClass)) {
			if (!force) {
				// throw new
				// RuntimeException("Double shouldn't convert to Float!");
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance()
						.getString("MappingEngine.convertDouble2Other.shouldNotToFloat"));
			}
			return new Float(value);
		} else if (Integer.class.equals(targetClass) || Integer.TYPE.equals(targetClass)) {
			if (!force) {
				// throw new RuntimeException(
				// "Double shouldn't convert to Integer!");
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance()
						.getString("MappingEngine.convertDouble2Other.shouldNotToInt"));
			}
			return Integer.valueOf(value.intValue());
		} else if (Short.class.equals(targetClass) || Short.TYPE.equals(targetClass)) {
			if (!force) {
				// throw new
				// RuntimeException("Double shouldn't convert to Short!");
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance()
						.getString("MappingEngine.convertDouble2Other.shouldNotToShort"));
			}
			return Short.valueOf(value.shortValue());
		} else if (Byte.class.equals(targetClass) || Byte.TYPE.equals(targetClass)) {
			if (!force) {
				// throw new
				// RuntimeException("Double shouldn't convert to Byte!");
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance()
						.getString("MappingEngine.convertDouble2Other.shouldNotToByte"));
			}
			return Byte.valueOf(value.byteValue());
		} else {
			// throw new RuntimeException("Double can't convert to "
			// + targetClass.getName());
			throw new RuntimeException(MultiLanguageResourceBundle.getInstance()
					.getString("MappingEngine.convertDouble2Other.canNotTo", new String[] { targetClass.getName() }));
		}
	}

	private Object convertLong2Other(Long value, Class targetClass, boolean force) {
		if (byteArray.getClass().equals(targetClass)) {
			// throw new RuntimeException("Long can't convert to byte[]!");
			throw new RuntimeException(MultiLanguageResourceBundle.getInstance()
					.getString("MappingEngine.convertLong2Other.canNotToByte[]"));
		} else if (Long.TYPE.equals(targetClass)) {
			return value;
		} else if (Double.class.equals(targetClass) || Double.TYPE.equals(targetClass)) {
			return new Double(value);
		} else if (Float.class.equals(targetClass) || Float.TYPE.equals(targetClass)) {
			return new Float(value);
		} else if (Integer.class.equals(targetClass) || Integer.TYPE.equals(targetClass)) {
			if (!force) {
				// throw new
				// RuntimeException("Long shouldn't convert to Integer!");
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance()
						.getString("MappingEngine.convertLong2Other.shouldNotToInt"));
			}
			return Integer.valueOf(value.intValue());
		} else if (Short.class.equals(targetClass) || Short.TYPE.equals(targetClass)) {
			if (!force) {
				// throw new
				// RuntimeException("Long shouldn't convert to Short!");
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance()
						.getString("MappingEngine.convertLong2Other.shouldNotToShort"));
			}
			return Short.valueOf(value.shortValue());
		} else if (Byte.class.equals(targetClass) || Byte.TYPE.equals(targetClass)) {
			if (!force) {
				// throw new
				// RuntimeException("Long shouldn't convert to Byte!");
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance()
						.getString("MappingEngine.convertLong2Other.shouldNotToByte"));
			}
			return Byte.valueOf(value.byteValue());
		} else {
			// throw new RuntimeException("Long can't convert to "
			// + targetClass.getName());
			throw new RuntimeException(MultiLanguageResourceBundle.getInstance()
					.getString("MappingEngine.convertLong2Other.canNotTo", new String[] { targetClass.getName() }));
		}
	}

	private Object convertInt2Other(Integer value, Class targetClass, boolean force) {
		if (byteArray.getClass().equals(targetClass)) {
			// throw new RuntimeException("Integer can't convert to byte[]!");
			throw new RuntimeException(MultiLanguageResourceBundle.getInstance()
					.getString("MappingEngine.convertInt2Other.canNotToByte[]"));
		} else if (Integer.TYPE.equals(targetClass)) {
			return value;
		} else if (Double.class.equals(targetClass) || Double.TYPE.equals(targetClass)) {
			return new Double(value);
		} else if (Float.class.equals(targetClass) || Float.TYPE.equals(targetClass)) {
			return new Float(value);
		} else if (Long.class.equals(targetClass) || Long.TYPE.equals(targetClass)) {
			return Long.valueOf(value);
		} else if (Short.class.equals(targetClass) || Short.TYPE.equals(targetClass)) {
			if (!force) {
				// throw new RuntimeException(
				// "Integer shouldn't convert to Short!");
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance()
						.getString("MappingEngine.convertInt2Other.shouldNotToShort"));
			}
			return Short.valueOf(value.shortValue());
		} else if (Byte.class.equals(targetClass) || Byte.TYPE.equals(targetClass)) {
			if (!force) {
				// throw new
				// RuntimeException("Integer shouldn't convert to Byte!");
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance()
						.getString("MappingEngine.convertInt2Other.shouldNotToByte"));
			}
			return Byte.valueOf(value.byteValue());
		} else {
			// throw new RuntimeException("Integer can't convert to "
			// + targetClass.getName());
			throw new RuntimeException(MultiLanguageResourceBundle.getInstance()
					.getString("MappingEngine.convertInt2Other.canNotTo", new String[] { targetClass.getName() }));
		}
	}

	private static byte[] byteArray = new byte[0];

	/**
	 * 获得脚本执行器
	 * 
	 * @return
	 */
	private BeanShellEngine getScriptExecutor() {
		if (null == bsh) {
			// bsh = new Interpreter();
			bsh = new BeanShellEngine();

			// **** add by liugl 20090226
			// 将主线程的ClassLoader传入BeanShell
			// bsh.setClassLoader(Thread.currentThread().getContextClassLoader());
			//

			// 设置外部参数
			Map.Entry en = null;
			Iterator it = context.entrySet().iterator();
			while (it.hasNext()) {
				en = (Entry) it.next();
				// try {
				bsh.set((String) en.getKey(), en.getValue());
				// } catch (EvalError e) {
				// // throw new RuntimeException("set global param["
				// // + en.getKey()
				// // + "] to BeanShell Interpreter failed:"
				// // + e.getMessage(), e);
				// throw new RuntimeException(MultiLanguageResourceBundle
				// .getInstance().getString(
				// "MappingEngine.setGlobalParameter.error",
				// new String[] { "" + en.getKey(),
				// e.getMessage() }));
				// }
			}
		}
		return bsh;
	}

	/**
	 * 执行脚本
	 * 
	 * @param map
	 * @param script
	 * @return
	 */
	private Object executeScript(Object object, String script) {
		BeanShellEngine executor = getScriptExecutor();
		// try {
		executor.set("source", new DataObject(object));
		if (object instanceof Map) {
			executor.set("sourceMap", object);
			executor.set("map", object);
		} else if (object instanceof MessageBean) {
			executor.set("sourceBean", object);
			executor.set("bean", object);
		}
		// 移动到getScriptExecutor中 executor.set("context", context);
		// } catch (EvalError e) {
		// // ExceptionUtil.throwActualException(e);
		// // throw new RuntimeException(
		// // "set param[source/map/bean] to BeanShell Interpreter failed:"
		// // + e.getMessage(), e);
		// throw new RuntimeException(MultiLanguageResourceBundle
		// .getInstance().getString(
		// "MappingEngine.executeScript.setParam.failed",
		// new String[] { e.getMessage() }), e);
		// }

		Object result = null;
		// try {
		result = executor.eval(script);
		// } catch (EvalError e) {
		// // ExceptionUtil.throwActualException(e);
		// // throw new RuntimeException("execute Script failed: line["
		// // + e.getErrorLineNumber() + "] error[" + e.getErrorText()
		// // + "] stackTrace[" + e.getScriptStackTrace() + "]", e);
		// // throw new RuntimeException("execute Script failed: "
		// // + e.getMessage(), e);
		// throw new RuntimeException(MultiLanguageResourceBundle
		// .getInstance().getString(
		// "MappingEngine.executeScript.failed",
		// new String[] { e.getMessage() }), e);
		// }
		return result;
	}

	private Object executeScript(String name, Object value, String script) {
		BeanShellEngine executor = getScriptExecutor();
		// try {
		executor.set(name, value);
		// 移动到getScriptExecutor中 executor.set("context", context);
		// } catch (EvalError e) {
		// // ExceptionUtil.throwActualException(e);
		// // throw new RuntimeException(
		// // "set param[source/map/bean] to BeanShell Interpreter failed:"
		// // + e.getMessage(), e);
		// throw new RuntimeException(MultiLanguageResourceBundle
		// .getInstance().getString(
		// "MappingEngine.executeScript.setParam.failed",
		// new String[] { e.getMessage() }));
		// }

		// Object result = null;
		// try {
		return executor.eval(script);
		// } catch (EvalError e) {
		// // ExceptionUtil.throwActualException(e);
		// // throw new RuntimeException("execute Script failed: line["
		// // + e.getErrorLineNumber() + "] error[" + e.getErrorText()
		// // + "] stackTrace[" + e.getScriptStackTrace() + "]", e);
		// // throw new RuntimeException("execute Script failed: "
		// // + e.getMessage(), e);
		// throw new RuntimeException(MultiLanguageResourceBundle
		// .getInstance().getString(
		// "MappingEngine.executeScript.failed",
		// new String[] { e.getMessage() }), e);
		// }
		// return result;
	}

	public Object getSourceObject() {
		return sourceObject;
	}

	public void setSourceObject(Object sourceObject) {
		this.sourceObject = sourceObject;
	}

	// public Map getContext() {
	// return context;
	// }

	// public void setContext(Map context) {
	// this.context = context;
	// }

	public class DataObject {
		Object obj;

		DataObject(Object obj) {
			this.obj = obj;
		}

		public Object get(String path) {
			return getValue(obj, path);
		}

		// public void set(String path, Object value) {
		// if (obj instanceof Map) {
		// MapUtil.setValue((Map) obj, path, value);
		// } else {
		// ClassUtil.setBeanValueByPath(obj, path, value);
		// }
		// }
	}
}
