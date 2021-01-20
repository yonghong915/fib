package com.fib.gateway.message.xml.script;

import java.io.StringReader;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.fib.gateway.message.util.ExceptionUtil;
import com.fib.gateway.message.xml.message.MultiLanguageResourceBundle;

import bsh.BshClassManager;
import bsh.EvalError;
import bsh.Interpreter;
import bsh.NameSpace;
import bsh.ParseException;
import bsh.TargetError;

public class BeanShellEngine {

	private static Interpreter parentInterpreter = null;
	private Interpreter interpreter = null;

	public BeanShellEngine() {
		BshClassManager var1 = new BshClassManager();
		var1.setClassLoader(Thread.currentThread().getContextClassLoader());
		this.interpreter = new Interpreter(new StringReader(""), System.out, System.err, false,
				new NameSpace(parentInterpreter.getNameSpace(), var1, "global"), (Interpreter) null, (String) null);
	}

	public static BeanShellEngine createBeanShellEngine(Map var0) {
		BeanShellEngine var1 = new BeanShellEngine();
		if (var0 != null) {
			Entry var2 = null;
			Iterator var3 = var0.entrySet().iterator();

			while (var3.hasNext()) {
				var2 = (Entry) var3.next();
				var1.set((String) var2.getKey(), var2.getValue());
			}
		}

		return var1;
	}

	public void set(String var1, Object var2) {
		if (null != var1 && 0 != var1.length()) {
			try {
				this.interpreter.set(var1, var2);
			} catch (EvalError var4) {
				ExceptionUtil.throwActualException(var4);
			}

		} else {
			throw new IllegalArgumentException("name is null");
		}
	}

	public Object get(String var1) {
		if (null != var1 && 0 != var1.length()) {
			try {
				return this.interpreter.get(var1);
			} catch (EvalError var3) {
				ExceptionUtil.throwActualException(var3);
				return null;
			}
		} else {
			throw new IllegalArgumentException("name is null");
		}
	}

	public Object eval(String var1) {
		if (null != var1 && 0 != var1.length()) {
			Object var2 = null;

			try {
				var2 = this.interpreter.eval(var1);
				return var2;
			} catch (TargetError var5) {
				if (var5.getTarget() instanceof CustomerException) {
					CustomerException var4 = (CustomerException) var5.getTarget();
					var4.setErrorLineNumber(var5.getErrorLineNumber());
					var4.setErrorText(var5.getErrorText());
					var4.setScript(var1);
					throw var4;
				} else {
					throw new RuntimeException(MultiLanguageResourceBundle.getInstance()
							.getString("BeanShell.eval.TargetError", new String[] { "" + var5.getErrorLineNumber(),
									var5.getErrorText(), var5.getTarget().toString(), var1 }));
				}
			} catch (ParseException var6) {
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance()
						.getString("BeanShell.eval.ParseException", new String[] { var6.getMessage(), var1 }));
			} catch (EvalError var7) {
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString(
						"BeanShell.eval.EvalError",
						new String[] { "" + var7.getErrorLineNumber(), var7.getErrorText(), var1, var7.getMessage() }));
			}
		} else {
			throw new IllegalArgumentException("source is null");
		}
	}

	static {
		BshClassManager var0 = new BshClassManager();
		var0.setClassLoader(Thread.currentThread().getContextClassLoader());
		parentInterpreter = new Interpreter(new StringReader(""), System.out, System.err, false,
				new NameSpace(var0, "global"), (Interpreter) null, (String) null);
	}
}
