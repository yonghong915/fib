package com.fib.msgconverter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class FileTest {

	public static void main(String[] args) {
		try {
			FileInputStream fis = new FileInputStream(new File("f:/sch_tble.sql"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.gc();

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

}
