package com.fib.gateway.message.xml.message.bean.generator;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.rmi.RemoteException;

public class ExceptionUtil {
	private ExceptionUtil() {
	}

	public static void throwActualException(Throwable var0) {
		var0 = getActualException(var0);
		if (var0 instanceof RuntimeException) {
			throw (RuntimeException) var0;
		} else if (var0 instanceof Error) {
			throw (Error) var0;
		} else {
			throw new RuntimeException(var0.getMessage(), var0);
		}
	}

	public static Throwable getActualException(Throwable var0) {
		boolean var1 = false;

		while (!var1) {
			if (var0 instanceof RemoteException) {
				var0 = ((RemoteException) var0).detail;
			} else if (var0 instanceof InvocationTargetException) {
				var0 = ((InvocationTargetException) var0).getCause();
			} else {
				var1 = true;
			}
		}

		return var0;
	}

	public static String getExceptionDetail(Throwable var0) {
		StringWriter var1 = new StringWriter();
		PrintWriter var2 = new PrintWriter(var1);
		var0.printStackTrace(var2);
		return var1.toString();
	}
}
