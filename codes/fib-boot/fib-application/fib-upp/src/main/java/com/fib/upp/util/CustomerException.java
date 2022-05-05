package com.fib.upp.util;

public class CustomerException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int errorLineNumber;
	private String errorText;
	private String script;

	public CustomerException() {
	}

	public CustomerException(String s, Throwable throwable) {
		super(s, throwable);
	}

	public CustomerException(String s) {
		super(s);
	}

	public CustomerException(Throwable throwable) {
		super(throwable);
	}

	@Override
	public String toString() {
		return "BeanShell.eval.TargetError";
	}

	public int getErrorLineNumber() {
		return errorLineNumber;
	}

	public void setErrorLineNumber(int i) {
		errorLineNumber = i;
	}

	public String getErrorText() {
		return errorText;
	}

	public void setErrorText(String s) {
		errorText = s;
	}

	public String getScript() {
		return script;
	}

	public void setScript(String s) {
		script = s;
	}
}
