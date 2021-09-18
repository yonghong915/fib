package com.fib.msgconverter;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class BufferTest {

	public static void main(String[] args) {
		String file = "C:\\Users\\fangyh\\Desktop\\20210914\\04071728202109141116019-test.txt";
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line = "";
			System.out.println("start");
			while ((line = br.readLine()) != null) {
				// System.out.println(line);
				if (null != line && line.length() > 0) {
					String[] strs = line.split("\\|", -1);
					System.out.println(strs[6]);
				}

			}
			System.out.println("end");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
