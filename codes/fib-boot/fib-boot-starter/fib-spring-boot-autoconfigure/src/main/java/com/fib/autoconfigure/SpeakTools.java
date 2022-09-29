package com.fib.autoconfigure;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;

public class SpeakTools {

	public static boolean speakText(String content) {
		boolean isFinish = true;
		ActiveXComponent sap = new ActiveXComponent("Sapi.SpVoice");
		try {
			sap.setProperty("Volume", new Variant(100)); // 音量 0-100
			sap.setProperty("Rate", new Variant(-3)); // 语音朗读速度 -10 到 +10
			Dispatch sapo = sap.getObject(); // 获取执行对象
			Dispatch.call(sapo, "Speak", new Variant(content)); // 执行朗读
			sapo.safeRelease(); // 关闭执行对象
		} catch (Exception e) {
			isFinish = false;
			e.printStackTrace();
		} finally {
			sap.safeRelease(); // 关闭执行对象
		}
		return isFinish;
	}

	public static void main(String[] args) {
        String text = "根据自己最近的写的项目，总结整理了关于java语音播报功能的方法，可分为两种形式";
        boolean isFi = speakText(text);
        System.out.println(isFi);
	}
}
