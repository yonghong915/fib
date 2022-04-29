// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.fib.upp.util;


public class CustomerException extends RuntimeException
{

	private int errorLineNumber;
	private String errorText;
	private String script;

	public CustomerException()
	{
	}

	public CustomerException(String s, Throwable throwable)
	{
		super(s, throwable);
	}

	public CustomerException(String s)
	{
		super(s);
	}

	public CustomerException(Throwable throwable)
	{
		super(throwable);
	}

	public String toString()
	{
		return "BeanShell.eval.TargetError";
	}

	public int getErrorLineNumber()
	{
		return errorLineNumber;
	}

	public void setErrorLineNumber(int i)
	{
		errorLineNumber = i;
	}

	public String getErrorText()
	{
		return errorText;
	}

	public void setErrorText(String s)
	{
		errorText = s;
	}

	public String getScript()
	{
		return script;
	}

	public void setScript(String s)
	{
		script = s;
	}
}
