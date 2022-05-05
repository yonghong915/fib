package com.fib.upp.util;

import java.io.StringReader;
import java.util.Iterator;
import java.util.Map;

import com.fib.commons.exception.CommonException;

import bsh.BshClassManager;
import bsh.EvalError;
import bsh.Interpreter;
import bsh.NameSpace;
import bsh.ParseException;
import bsh.TargetError;

public class BeanShellEngine {

	static BshClassManager localBshClassManager = new BshClassManager();
	static {
		localBshClassManager.setClassLoader(Thread.currentThread().getContextClassLoader());
	}

	private static Interpreter parentInterpreter = new Interpreter(new StringReader(""), System.out, System.err, false,
			new NameSpace(localBshClassManager, "global"), null, null);
	private Interpreter interpreter = null;

	public BeanShellEngine() {
		localBshClassManager.setClassLoader(Thread.currentThread().getContextClassLoader());
		this.interpreter = new Interpreter(new StringReader(""), System.out, System.err, false,
				new NameSpace(parentInterpreter.getNameSpace(), localBshClassManager, "global"), null, null);
	}

	public static BeanShellEngine createBeanShellEngine(Map<String, Object> paramMap) {
		BeanShellEngine localBeanShellEngine = new BeanShellEngine();
		if (paramMap != null) {
			Map.Entry<String, Object> localEntry = null;
			Iterator<Map.Entry<String, Object>> iter = paramMap.entrySet().iterator();
			while (iter.hasNext()) {
				localEntry = iter.next();
				localBeanShellEngine.set(localEntry.getKey(), localEntry.getValue());
			}
		}
		return localBeanShellEngine;
	}

	public void set(String paramString, Object paramObject) {
		if ((null == paramString) || (0 == paramString.length()))
			throw new IllegalArgumentException("name is null");
		try {
			this.interpreter.set(paramString, paramObject);
		} catch (EvalError localEvalError) {
			ExceptionUtil.throwActualException(localEvalError);
		}
	}

	public Object get(String paramString) {
		if ((null == paramString) || (0 == paramString.length()))
			throw new IllegalArgumentException("name is null");
		try {
			return this.interpreter.get(paramString);
		} catch (EvalError localEvalError) {
			ExceptionUtil.throwActualException(localEvalError);
		}
		return null;
	}

	public Object eval(String paramString) {
		if ((null == paramString) || (0 == paramString.length()))
			throw new IllegalArgumentException("source is null");
		Object localObject = null;
		try {
			localObject = this.interpreter.eval(paramString);
		} catch (TargetError localTargetError) {
			if ((localTargetError.getTarget() instanceof CustomerException localCustomerException)) {
				localCustomerException.setErrorLineNumber(localTargetError.getErrorLineNumber());
				localCustomerException.setErrorText(localTargetError.getErrorText());
				localCustomerException.setScript(paramString);
				throw localCustomerException;
			}
			throw new CommonException("BeanShell.eval.TargetError");
		} catch (ParseException localParseException) {
			throw new CommonException("BeanShell.eval.ParseException");
		} catch (EvalError localEvalError) {
			throw new CommonException("BeanShell.eval.EvalError");
		}
		return localObject;
	}

}
