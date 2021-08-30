package com.fib.msgconverter.commgateway.channel.config.processor.event;

import com.fib.msgconverter.commgateway.util.multilang.MultiLanguageResourceBundle;

public class ActionConfig {
	// class
	public static final String TYP_CLASS_TXT = "class";
	public static final int TYP_CLASS = 0x89;

	// job
	public static final String TYP_JOB_TXT = "job";
	public static final int TYP_JOB = 0x88;

	/**
	 * action type
	 */
	private int type;
	private String clazz;
	private ScheduleRule scheduleRule;
	private ProcessorRule processorRule;

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getClazz() {
		return clazz;
	}

	public void setClazz(String clazz) {
		this.clazz = clazz;
	}

	public ScheduleRule getScheduleRule() {
		return scheduleRule;
	}

	public void setScheduleRule(ScheduleRule scheduleRule) {
		this.scheduleRule = scheduleRule;
	}

	public ProcessorRule getProcessorRule() {
		return processorRule;
	}

	public void setProcessorRule(ProcessorRule processorRule) {
		this.processorRule = processorRule;
	}

	public static int getTypeByText(String text) {
		if (TYP_CLASS_TXT.equals(text)) {
			return TYP_CLASS;
		} else if (TYP_JOB_TXT.equals(text)) {
			return TYP_JOB;
		} else {
			// throw new IllegalArgumentException("Unkown Type[" + text + "]!");
			throw new IllegalArgumentException(MultiLanguageResourceBundle
					.getInstance().getString("type.unsupport",
							new String[] { text }));
		}
	}
}