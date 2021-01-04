package com.fib.gateway.message.metadata;

import lombok.Data;

/**
 * 
 * @author fangyh
 * @version 1.0
 * @since 1.0
 * @date 2020-12-28
 */
@Data
public class ValueRange {
	private String databaseValueId;
	private String shortText;
	private String value;
	private String referenceId;
	private Message reference;

	public boolean equalsTo(ValueRange valuerange) {
		if (null == valuerange)
			return false;
		if (!value.equalsIgnoreCase(valuerange.getValue()))
			return false;
		if ((null == referenceId) ^ (null == valuerange.getReferenceId()))
			return false;
		if (null != referenceId && null != valuerange.getReferenceId()
				&& !referenceId.equalsIgnoreCase(valuerange.getReferenceId()))
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
