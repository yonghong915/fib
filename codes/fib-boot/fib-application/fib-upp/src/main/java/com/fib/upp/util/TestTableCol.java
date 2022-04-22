package com.fib.upp.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigInteger;

import com.fib.upp.modules.payment.entity.PaymentPreferInfo;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ReflectUtil;

public class TestTableCol {
	public static void main(String[] args) {
		TestTableCol test = new TestTableCol();
		test.buildCreateTblSql(PaymentPreferInfo.class);
	}

	public String buildCreateTblSql(Class<?> entityClazz) {
		String line = " \r\n";
		StringBuilder createTblSlq = new StringBuilder();
		createTblSlq.append("create table ").append(CharSequenceUtil.toUnderlineCase(entityClazz.getSimpleName()))
				.append(" (");
		createTblSlq.append(line);
		Field[] fields = ReflectUtil.getFieldsDirectly(entityClazz, false);
		for (Field field : fields) {
			String name = field.getName();
			boolean isStatic = Modifier.isStatic(field.getModifiers());
			if (!isStatic) {
				Class<?> typeClazz = field.getType();
				String colName = CharSequenceUtil.toUnderlineCase(name);
				createTblSlq.append("    ").append(colName);
				if (BigInteger.class == typeClazz) {
					createTblSlq.append(" BIGINT(20) ,");
					createTblSlq.append(line);
				} else if (String.class == typeClazz) {
					createTblSlq.append(" VARCHAR(50) ,");
					createTblSlq.append(line);
				}
			}
		}
		createTblSlq.append(" );");
		return createTblSlq.toString();
	}
}
