package com.fib.upp;

import org.openjdk.jol.info.ClassLayout;
import org.openjdk.jol.info.GraphLayout;

public class ObjectSizeTest {

	public static void main(String[] args) {
		int[] arr = new int[10];
		//Integer obj = Integer.valueOf(1);
		String classLayout = ClassLayout.parseInstance(arr).toPrintable();
		System.out.println(classLayout);
		
		System.out.println(GraphLayout.parseInstance(arr).toPrintable());
	}
}
