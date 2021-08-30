/**
 * 北京长信通信息技术有限公司
 * 2008-8-29 下午12:06:02
 */
package com.fib.msgconverter.commgateway.channel.message.recognizer.impl;

import java.util.Map;

import com.fib.msgconverter.commgateway.channel.message.recognizer.AbstractMessageRecognizer;
import com.fib.msgconverter.commgateway.util.multilang.MultiLanguageResourceBundle;
import com.giantstone.common.script.BeanShellEngine;

/**
 * BeanShell脚步的识别器
 * 
 * @author 刘恭亮
 * 
 */
public class ScriptRecognizer extends AbstractMessageRecognizer {
	private static final String SCRIPT = "script";

	private BeanShellEngine bsh = null;
	private String script = null;

	public String recognize(byte[] message) {
		Object messageId = executeScript("message", message, script);
		return (String) messageId;
	}

	/**
	 * 获得脚本执行器
	 * 
	 * @return
	 */
	private BeanShellEngine getScriptExecutor() {
		if (null == bsh) {
			bsh = new BeanShellEngine();
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
	private Object executeScript(String name, Object object, String script) {
		BeanShellEngine executor = getScriptExecutor();
		// try {
		executor.set(name, object);
		// } catch (EvalError e) {
		// ExceptionUtil.throwActualException(e);
		// }

		Object result = null;
		// try {
		result = executor.eval(script);
		// } catch (EvalError e) {
		// ExceptionUtil.throwActualException(e);
		// }
		return result;
	}

	public void setParameters(Map parameters) {
		super.setParameters(parameters);

		parseParameters();
	}

	private void parseParameters() {
		String tmp = (String) parameters.get(SCRIPT);
		if (null == tmp) {
			// throw new RuntimeException("parameter[" + SCRIPT + "] is NULL!");
			throw new RuntimeException(MultiLanguageResourceBundle
					.getInstance().getString("parameter.null",
							new String[] { SCRIPT }));
		} else {
			tmp = tmp.trim();
			if (0 == tmp.length()) {
				// throw new RuntimeException("parameter[" + SCRIPT +
				// "] is Blank!");
				throw new RuntimeException(MultiLanguageResourceBundle
						.getInstance().getString("parameter.blank",
								new String[] { SCRIPT }));
			}
			script = tmp;
		}
	}

}
