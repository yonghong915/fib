package com.fib.msgconverter.commgateway.channel.nio.reader.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.fib.msgconverter.commgateway.channel.nio.reader.AbstractNioReader;
import com.fib.msgconverter.commgateway.util.multilang.MultiLanguageResourceBundle;
import com.giantstone.common.script.BeanShellEngine;

/**
 * 脚本读取器，根据脚本返回的结果判断是否读取完毕
 * 
 * @author 白帆
 * 
 */
public class ScriptCheckNioReader extends AbstractNioReader {
	public static final String SCRIPT = "script";
	private String script = null;
	private BeanShellEngine bsh = null;

	public boolean checkMessageComplete() {
		Map parameters = new HashMap();
		parameters.put("MESSAGE", messageBuffer.toBytes());

		Object result = executeScript(script, parameters);

		return Boolean.valueOf((String) result);
	}

	public void setParameters(Map parameters) {
		super.setParameters(parameters);
		parseParameters(parameters);
	}

	private void parseParameters(Map parameters) {
		String tmp = (String) this.parameters.get(SCRIPT);
		if (null == tmp) {
			// throw new RuntimeException("parameter[" + SCRIPT + "] is NULL!");
			throw new RuntimeException(MultiLanguageResourceBundle
					.getInstance().getString("parameter.null",
							new String[] { SCRIPT }));
		} else {
			tmp = tmp.trim();
			if (0 == tmp.length()) {
				// throw new RuntimeException("parameter[" + SCRIPT
				// + "] is Blank!");
				throw new RuntimeException(MultiLanguageResourceBundle
						.getInstance().getString("parameter.blank",
								new String[] { SCRIPT }));
			}
		}

		script = tmp;
	}

	private BeanShellEngine getScriptExecutor() {
		if (null == bsh) {
			bsh = new BeanShellEngine();
		}
		return bsh;
	}

	private Object executeScript(String script, Map parameters) {
		BeanShellEngine executor = getScriptExecutor();
		// try {
		Iterator iterator = parameters.keySet().iterator();
		while (iterator.hasNext()) {
			String key = (String) iterator.next();
			executor.set(key, parameters.get(key));
		}
		// } catch (EvalError e) {
		// // throw new RuntimeException(
		// // "set parameters to BeanShell Interpreter failed:"
		// // + e.getMessage(), e);
		// throw new RuntimeException(MultiLanguageResourceBundle
		// .getInstance().getString(
		// "script.BeanShell.setParameters.failed",
		// new String[] { e.getMessage() }), e);
		// }

		Object result = null;
		// try {
		result = executor.eval(script);
		// } catch (EvalError e) {
		// // throw new RuntimeException("execute Script failed: "
		// // + e.getMessage(), e);
		// throw new RuntimeException(MultiLanguageResourceBundle
		// .getInstance().getString("script.executeScript.failed",
		// new String[] { e.getMessage() }), e);
		// }
		return result;
	}

}
