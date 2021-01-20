package com.fib.gateway.message.xml.script;

import com.fib.gateway.message.xml.message.MultiLanguageResourceBundle;

public class CustomerException extends RuntimeException {
	private int errorLineNumber;
	private String errorText;
	private String script;

	public CustomerException() {
	}

	public CustomerException(String var1, Throwable var2) {
		super(var1, var2);
	}

	public CustomerException(String var1) {
		super(var1);
	}

	public CustomerException(Throwable var1) {
		super(var1);
	}

	public String toString() {
		return MultiLanguageResourceBundle.getInstance().getString("BeanShell.eval.TargetError",
				new String[] { "" + this.errorLineNumber, this.errorText == null ? "" : this.errorText,
						super.toString(), this.script == null ? "" : this.script });
	}

	public int getErrorLineNumber() {
		return this.errorLineNumber;
	}

	public void setErrorLineNumber(int var1) {
		this.errorLineNumber = var1;
	}

	public String getErrorText() {
		return this.errorText;
	}

	public void setErrorText(String var1) {
		this.errorText = var1;
	}

	public String getScript() {
		return this.script;
	}

	public void setScript(String var1) {
		this.script = var1;
	}
}
