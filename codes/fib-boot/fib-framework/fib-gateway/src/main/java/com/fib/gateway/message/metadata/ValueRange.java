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

	public boolean equalsTo(ValueRange var1) {
		if (null == var1) {
			return false;
		} else if (!this.value.equalsIgnoreCase(var1.getValue())) {
			return false;
		} else if (null == this.referenceId ^ null == var1.getReferenceId()) {
			return false;
		} else if (null != this.referenceId && null != var1.getReferenceId()
				&& !this.referenceId.equalsIgnoreCase(var1.getReferenceId())) {
			return false;
		} else if (null == this.reference ^ null == var1.getReference()) {
			return false;
		} else {
			return null == this.reference || null == var1.getReference() || this.reference.equalTo(var1.getReference());
		}
	}

	public ValueRange copy() {
		ValueRange var1 = new ValueRange();
		var1.setReference(this.reference);
		var1.setReferenceId(this.referenceId);
		var1.setShortText(this.shortText);
		var1.setValue(this.value);
		return var1;
	}
}
