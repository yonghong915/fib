package com.fib.upp.message.metadata;

/**
 * 
 * @author fangyh
 * @version 1.0
 * @date 2022-04-29 10:47:47
 */
public class ValueRange {
	private String shortText;
	private String value;
	private String referenceId;
	private Message reference;

	public String getShortText() {
		return shortText;
	}

	public void setShortText(String s) {
		shortText = s;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String s) {
		value = s;
	}

	public String getReferenceId() {
		return referenceId;
	}

	public void setReferenceId(String s) {
		referenceId = s;
	}

	public Message getReference() {
		return reference;
	}

	public void setReference(Message message) {
		reference = message;
	}

	public boolean equalsTo(ValueRange valuerange) {
		if (null == valuerange)
			return false;
		if (!value.equalsIgnoreCase(valuerange.getValue()))
			return false;
		if ((null == referenceId) ^ (null == valuerange.getReferenceId()))
			return false;
		if (null != referenceId && null != valuerange.getReferenceId() && !referenceId.equalsIgnoreCase(valuerange.getReferenceId()))
			return false;
		if ((null == reference) ^ (null == valuerange.getReference()))
			return false;
		return null == reference || null == valuerange.getReference() || reference.equalTo(valuerange.getReference());
	}

	public ValueRange copy() {
		ValueRange valuerange = new ValueRange();
		valuerange.setReference(reference);
		valuerange.setReferenceId(referenceId);
		valuerange.setShortText(shortText);
		valuerange.setValue(value);
		return valuerange;
	}
}
