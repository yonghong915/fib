// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.fib.autoconfigure.msgconverter.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.rmi.RemoteException;

public class ExceptionUtil
{

	private ExceptionUtil()
	{
	}

	public static void throwActualException(Throwable throwable)
	{
		throwable = getActualException(throwable);
		if (throwable instanceof RuntimeException)
			throw (RuntimeException)throwable;
		if (throwable instanceof Error)
			throw (Error)throwable;
		else
			throw new RuntimeException(throwable.getMessage(), throwable);
	}

	public static Throwable getActualException(Throwable throwable)
	{
		boolean flag = false;
		while (!flag) 
			if (throwable instanceof RemoteException)
				throwable = ((RemoteException)throwable).detail;
			else
			if (throwable instanceof InvocationTargetException)
				throwable = ((InvocationTargetException)throwable).getCause();
			else
				flag = true;
		return throwable;
	}

	public static String getExceptionDetail(Throwable throwable)
	{
		StringWriter stringwriter = new StringWriter();
		PrintWriter printwriter = new PrintWriter(stringwriter);
		throwable.printStackTrace(printwriter);
		return stringwriter.toString();
	}
}
