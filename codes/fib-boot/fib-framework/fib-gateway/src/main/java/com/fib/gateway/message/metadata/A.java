package com.fib.gateway.message.metadata;

public class A {
	private String A;
	private String C;
	private int B;
	private String D;

	public String D() {
		return this.C;
	}

	public void B(String var1) {
		this.C = var1;
	}

	public int B() {
		return this.B;
	}

	public void A(int var1) {
		this.B = var1;
	}

	public String E() {
		return this.D;
	}

	public void C(String var1) {
		this.D = var1;
	}

	public String C() {
		return this.A;
	}

	public void A(String var1) {
		this.A = var1;
	}

	public boolean A(A var1) {
		if (null == var1) {
			return false;
		} else if (!this.C.equalsIgnoreCase(var1.D())) {
			return false;
		} else if (this.B != var1.B()) {
			return false;
		} else {
			return this.D.equalsIgnoreCase(var1.E());
		}
	}

	public A A() {
		A var1 = new A();
		var1.B(this.C);
		var1.C(this.D);
		var1.A(this.B);
		return var1;
	}
}
