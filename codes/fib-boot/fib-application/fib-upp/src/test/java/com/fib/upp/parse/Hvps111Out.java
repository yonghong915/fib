package com.fib.upp.parse;

import java.util.ArrayList;
import java.util.List;

public class Hvps111Out {
	private String messageIdentification;
	private String creationDateTime;
	private String numberOfTransactions;
	private String currencyType;
	private String interbankSettlementAmount;
	private List<String> hvps = new ArrayList<>(20);

	public List<String> getHvps() {
		return hvps;
	}

	public void setHvps(List<String> hvps) {
		this.hvps = hvps;
	}

	public String getInterbankSettlementAmount() {
		return interbankSettlementAmount;
	}

	public void setInterbankSettlementAmount(String interbankSettlementAmount) {
		this.interbankSettlementAmount = interbankSettlementAmount;
	}

	public String getCurrencyType() {
		return currencyType;
	}

	public void setCurrencyType(String currencyType) {
		this.currencyType = currencyType;
	}

	public String getNumberOfTransactions() {
		return numberOfTransactions;
	}

	public void setNumberOfTransactions(String numberOfTransactions) {
		this.numberOfTransactions = numberOfTransactions;
	}

	public String getCreationDateTime() {
		return creationDateTime;
	}

	public void setCreationDateTime(String creationDateTime) {
		this.creationDateTime = creationDateTime;
	}

	public String getMessageIdentification() {
		return messageIdentification;
	}

	public void setMessageIdentification(String messageIdentification) {
		this.messageIdentification = messageIdentification;
	}

	@Override
	public String toString() {
		return "Hvps111Out [messageIdentification=" + messageIdentification + ", creationDateTime=" + creationDateTime + ", numberOfTransactions="
				+ numberOfTransactions + ", currencyType=" + currencyType + ", interbankSettlementAmount=" + interbankSettlementAmount + "]";
	}

}
