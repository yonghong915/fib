package com.fib.upp.cnaps2.messagebean.send.req;

import com.fib.gateway.message.bean.*;
import com.fib.gateway.message.xml.message.*;
import com.fib.gateway.message.*;
import com.fib.gateway.message.util.*;
import java.math.BigDecimal;
import java.io.UnsupportedEncodingException;
import com.fib.gateway.message.xml.message.MultiLanguageResourceBundle;
import java.util.*;

/**
 * null
 */
public class HVPS_111_Out extends MessageBean{

	//报文标识号
	private String messageIdentification;

	public String getMessageIdentification(){
		return messageIdentification;
	}

	public void setMessageIdentification(String messageIdentification){
		this.messageIdentification = messageIdentification;
	}

	//报文发送时间
	private String creationDateTime;

	public String getCreationDateTime(){
		return creationDateTime;
	}

	public void setCreationDateTime(String creationDateTime){
		this.creationDateTime = creationDateTime;
	}

	//明细业务总笔数
	private String numberOfTransactions;

	public String getNumberOfTransactions(){
		return numberOfTransactions;
	}

	public void setNumberOfTransactions(String numberOfTransactions){
		this.numberOfTransactions = numberOfTransactions;
	}

	//Code
	private String settlementMethod;

	public String getSettlementMethod(){
		return settlementMethod;
	}

	public void setSettlementMethod(String settlementMethod){
		this.settlementMethod = settlementMethod;
	}

	//端到端标识号
	private String endToEndIdentification;

	public String getEndToEndIdentification(){
		return endToEndIdentification;
	}

	public void setEndToEndIdentification(String endToEndIdentification){
		this.endToEndIdentification = endToEndIdentification;
	}

	//交易标识号
	private String transactionIdentification;

	public String getTransactionIdentification(){
		return transactionIdentification;
	}

	public void setTransactionIdentification(String transactionIdentification){
		this.transactionIdentification = transactionIdentification;
	}

	//业务类型编码
	private String proprietary;

	public String getProprietary(){
		return proprietary;
	}

	public void setProprietary(String proprietary){
		this.proprietary = proprietary;
	}

	//币种
	private String currencyType;

	public String getCurrencyType(){
		return currencyType;
	}

	public void setCurrencyType(String currencyType){
		this.currencyType = currencyType;
	}

	//货币符号、金额
	private String interbankSettlementAmount;

	public String getInterbankSettlementAmount(){
		return interbankSettlementAmount;
	}

	public void setInterbankSettlementAmount(String interbankSettlementAmount){
		this.interbankSettlementAmount = interbankSettlementAmount;
	}

	//业务优先级
	private String settlementPriority;

	public String getSettlementPriority(){
		return settlementPriority;
	}

	public void setSettlementPriority(String settlementPriority){
		this.settlementPriority = settlementPriority;
	}

	//付款清算行行号
	private String memberIdentification;

	public String getMemberIdentification(){
		return memberIdentification;
	}

	public void setMemberIdentification(String memberIdentification){
		this.memberIdentification = memberIdentification;
	}

	public Object getAttribute(String name){
		if("MessageIdentification".equals(name)){
			return  this.getMessageIdentification();
		}else if("CreationDateTime".equals(name)){
			return  this.getCreationDateTime();
		}else if("NumberOfTransactions".equals(name)){
			return  this.getNumberOfTransactions();
		}else if("SettlementMethod".equals(name)){
			return  this.getSettlementMethod();
		}else if("EndToEndIdentification".equals(name)){
			return  this.getEndToEndIdentification();
		}else if("TransactionIdentification".equals(name)){
			return  this.getTransactionIdentification();
		}else if("Proprietary".equals(name)){
			return  this.getProprietary();
		}else if("CurrencyType".equals(name)){
			return  this.getCurrencyType();
		}else if("InterbankSettlementAmount".equals(name)){
			return  this.getInterbankSettlementAmount();
		}else if("SettlementPriority".equals(name)){
			return  this.getSettlementPriority();
		}else if("MemberIdentification".equals(name)){
			return  this.getMemberIdentification();
		}
		return null;
	}

	public void setAttribute(String name,Object value){
		if("MessageIdentification".equals(name)){
			this.setMessageIdentification((String)value);
		}else if("CreationDateTime".equals(name)){
			this.setCreationDateTime((String)value);
		}else if("NumberOfTransactions".equals(name)){
			this.setNumberOfTransactions((String)value);
		}else if("SettlementMethod".equals(name)){
			this.setSettlementMethod((String)value);
		}else if("EndToEndIdentification".equals(name)){
			this.setEndToEndIdentification((String)value);
		}else if("TransactionIdentification".equals(name)){
			this.setTransactionIdentification((String)value);
		}else if("Proprietary".equals(name)){
			this.setProprietary((String)value);
		}else if("CurrencyType".equals(name)){
			this.setCurrencyType((String)value);
		}else if("InterbankSettlementAmount".equals(name)){
			this.setInterbankSettlementAmount((String)value);
		}else if("SettlementPriority".equals(name)){
			this.setSettlementPriority((String)value);
		}else if("MemberIdentification".equals(name)){
			this.setMemberIdentification((String)value);
		}
	}

	public void cover(MessageBean bean){
		HVPS_111_Out newBean = (HVPS_111_Out) bean;

		if( null != newBean.getMessageIdentification() && 0 != newBean.getMessageIdentification().length()){
			messageIdentification = newBean.getMessageIdentification();
		}
		if( null != newBean.getCreationDateTime() && 0 != newBean.getCreationDateTime().length()){
			creationDateTime = newBean.getCreationDateTime();
		}
		if( null != newBean.getNumberOfTransactions() && 0 != newBean.getNumberOfTransactions().length()){
			numberOfTransactions = newBean.getNumberOfTransactions();
		}
		if( null != newBean.getSettlementMethod() && 0 != newBean.getSettlementMethod().length()){
			settlementMethod = newBean.getSettlementMethod();
		}
		if( null != newBean.getEndToEndIdentification() && 0 != newBean.getEndToEndIdentification().length()){
			endToEndIdentification = newBean.getEndToEndIdentification();
		}
		if( null != newBean.getTransactionIdentification() && 0 != newBean.getTransactionIdentification().length()){
			transactionIdentification = newBean.getTransactionIdentification();
		}
		if( null != newBean.getProprietary() && 0 != newBean.getProprietary().length()){
			proprietary = newBean.getProprietary();
		}
		if( null != newBean.getCurrencyType() && 0 != newBean.getCurrencyType().length()){
			currencyType = newBean.getCurrencyType();
		}
		if( null != newBean.getInterbankSettlementAmount() && 0 != newBean.getInterbankSettlementAmount().length()){
			interbankSettlementAmount = newBean.getInterbankSettlementAmount();
		}
		if( null != newBean.getSettlementPriority() && 0 != newBean.getSettlementPriority().length()){
			settlementPriority = newBean.getSettlementPriority();
		}
		if( null != newBean.getMemberIdentification() && 0 != newBean.getMemberIdentification().length()){
			memberIdentification = newBean.getMemberIdentification();
		}
	}

	public void validate(){
		//报文标识号非空检查
		if ( null == messageIdentification){
			throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.null", new String[]{"报文标识号(messageIdentification)"}));
		}

			//报文标识号数据长度检查
			try{
			if ( messageIdentification.getBytes("UTF-8").length> 35 ) {
				throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.maxLength", new String[]{"报文标识号(messageIdentification)", "35"}));
			}
			}catch (UnsupportedEncodingException e) {
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("message.encoding.unsupport", new String[]{"HVPS_111_Out", "UTF-8"}));
			}

		//报文发送时间不为空则按以下规则校验
		if( null != creationDateTime){
			//报文发送时间数据长度检查
			try{
			if ( creationDateTime.getBytes("UTF-8").length> 21 ) {
				throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.maxLength", new String[]{"报文发送时间(creationDateTime)", "21"}));
			}
			}catch (UnsupportedEncodingException e) {
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("message.encoding.unsupport", new String[]{"HVPS_111_Out", "UTF-8"}));
			}

			//报文发送时间格式检查
			try{
				java.text.DateFormat dateformat = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
				dateformat.setLenient(false);
				dateformat.parse(creationDateTime);
			} catch (Exception e) {
				throw new RuntimeException(
					MultiLanguageResourceBundle.getInstance().getString("Message.field.pattern.illegal", new String[]{"报文发送时间(creationDateTime)", "yyyy-MM-dd'T'HH:mm:ss"}) + e.getMessage(), e);
			}

		}

		//明细业务总笔数非空检查
		if ( null == numberOfTransactions){
			throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.null", new String[]{"明细业务总笔数(numberOfTransactions)"}));
		}

			//明细业务总笔数数据长度检查
			try{
			if ( numberOfTransactions.getBytes("UTF-8").length> 15 ) {
				throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.maxLength", new String[]{"明细业务总笔数(numberOfTransactions)", "15"}));
			}
			}catch (UnsupportedEncodingException e) {
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("message.encoding.unsupport", new String[]{"HVPS_111_Out", "UTF-8"}));
			}

		//Code非空检查
		if ( null == settlementMethod){
			throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.null", new String[]{"Code(settlementMethod)"}));
		}

			//Code数据长度检查
			try{
			if ( settlementMethod.getBytes("UTF-8").length> 4 ) {
				throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.maxLength", new String[]{"Code(settlementMethod)", "4"}));
			}
			}catch (UnsupportedEncodingException e) {
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("message.encoding.unsupport", new String[]{"HVPS_111_Out", "UTF-8"}));
			}

		//端到端标识号非空检查
		if ( null == endToEndIdentification){
			throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.null", new String[]{"端到端标识号(endToEndIdentification)"}));
		}

			//端到端标识号数据长度检查
			try{
			if ( endToEndIdentification.getBytes("UTF-8").length> 35 ) {
				throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.maxLength", new String[]{"端到端标识号(endToEndIdentification)", "35"}));
			}
			}catch (UnsupportedEncodingException e) {
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("message.encoding.unsupport", new String[]{"HVPS_111_Out", "UTF-8"}));
			}

		//交易标识号非空检查
		if ( null == transactionIdentification){
			throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.null", new String[]{"交易标识号(transactionIdentification)"}));
		}

			//交易标识号数据长度检查
			try{
			if ( transactionIdentification.getBytes("UTF-8").length> 35 ) {
				throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.maxLength", new String[]{"交易标识号(transactionIdentification)", "35"}));
			}
			}catch (UnsupportedEncodingException e) {
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("message.encoding.unsupport", new String[]{"HVPS_111_Out", "UTF-8"}));
			}

		//业务类型编码非空检查
		if ( null == proprietary){
			throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.null", new String[]{"业务类型编码(proprietary)"}));
		}

			//业务类型编码数据长度检查
			try{
			if ( proprietary.getBytes("UTF-8").length> 4 ) {
				throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.maxLength", new String[]{"业务类型编码(proprietary)", "4"}));
			}
			}catch (UnsupportedEncodingException e) {
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("message.encoding.unsupport", new String[]{"HVPS_111_Out", "UTF-8"}));
			}

		//币种不为空则按以下规则校验
		if( null != currencyType){
			//币种数据长度检查
			try{
			if ( currencyType.getBytes("UTF-8").length> 5 ) {
				throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.maxLength", new String[]{"币种(currencyType)", "5"}));
			}
			}catch (UnsupportedEncodingException e) {
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("message.encoding.unsupport", new String[]{"HVPS_111_Out", "UTF-8"}));
			}

		}

		//货币符号、金额非空检查
		if ( null == interbankSettlementAmount){
			throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.null", new String[]{"货币符号、金额(interbankSettlementAmount)"}));
		}

			//货币符号、金额数据长度检查
			try{
			if ( interbankSettlementAmount.getBytes("UTF-8").length> 19 ) {
				throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.maxLength", new String[]{"货币符号、金额(interbankSettlementAmount)", "19"}));
			}
			}catch (UnsupportedEncodingException e) {
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("message.encoding.unsupport", new String[]{"HVPS_111_Out", "UTF-8"}));
			}

		//业务优先级非空检查
		if ( null == settlementPriority){
			throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.null", new String[]{"业务优先级(settlementPriority)"}));
		}

			//业务优先级数据长度检查
			try{
			if ( settlementPriority.getBytes("UTF-8").length> 16 ) {
				throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.maxLength", new String[]{"业务优先级(settlementPriority)", "16"}));
			}
			}catch (UnsupportedEncodingException e) {
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("message.encoding.unsupport", new String[]{"HVPS_111_Out", "UTF-8"}));
			}

		//业务优先级指定值域检查
		if ( !"NORM".equals( settlementPriority) && !"HIGH".equals( settlementPriority) && !"URGT".equals( settlementPriority) ) {
			throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.value.in", new String[]{")业务优先级(settlementPriority)", "NORM/HIGH/URGT"}));
		}

		//付款清算行行号非空检查
		if ( null == memberIdentification){
			throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.null", new String[]{"付款清算行行号(memberIdentification)"}));
		}

			//付款清算行行号数据长度检查
			try{
			if ( memberIdentification.getBytes("UTF-8").length> 14 ) {
				throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.maxLength", new String[]{"付款清算行行号(memberIdentification)", "14"}));
			}
			}catch (UnsupportedEncodingException e) {
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("message.encoding.unsupport", new String[]{"HVPS_111_Out", "UTF-8"}));
			}


	}

	public String toString(){
		return toString(false);
	}

	public String toString(boolean isWrap){
		return toString(isWrap,false);
	}

	public String toString(boolean isWrap,boolean isTable){
		StringBuffer buf = new StringBuffer(10240);
		StringBuffer tableBuf = new StringBuffer(2048);
		String str = null;
		if( null != messageIdentification){
			buf.append("<a n=\"messageIdentification\" t=\"str\">");
			buf.append(messageIdentification);
			buf.append("</a>");
		}
		if( null != creationDateTime){
			buf.append("<a n=\"creationDateTime\" t=\"datetime\">");
			buf.append(creationDateTime);
			buf.append("</a>");
		}
		if( null != numberOfTransactions){
			buf.append("<a n=\"numberOfTransactions\" t=\"str\">");
			buf.append(numberOfTransactions);
			buf.append("</a>");
		}
		if( null != settlementMethod){
			buf.append("<a n=\"settlementMethod\" t=\"str\">");
			buf.append(settlementMethod);
			buf.append("</a>");
		}
		if( null != endToEndIdentification){
			buf.append("<a n=\"endToEndIdentification\" t=\"str\">");
			buf.append(endToEndIdentification);
			buf.append("</a>");
		}
		if( null != transactionIdentification){
			buf.append("<a n=\"transactionIdentification\" t=\"str\">");
			buf.append(transactionIdentification);
			buf.append("</a>");
		}
		if( null != proprietary){
			buf.append("<a n=\"proprietary\" t=\"str\">");
			buf.append(proprietary);
			buf.append("</a>");
		}
		if( null != currencyType){
			buf.append("<a n=\"currencyType\" t=\"str\">");
			buf.append(currencyType);
			buf.append("</a>");
		}
		if( null != interbankSettlementAmount){
			buf.append("<a n=\"interbankSettlementAmount\" t=\"str\">");
			buf.append(interbankSettlementAmount);
			buf.append("</a>");
		}
		if( null != settlementPriority){
			buf.append("<a n=\"settlementPriority\" t=\"str\">");
			buf.append(settlementPriority);
			buf.append("</a>");
		}
		if( null != memberIdentification){
			buf.append("<a n=\"memberIdentification\" t=\"str\">");
			buf.append(memberIdentification);
			buf.append("</a>");
		}
		if( 0 == buf.length()){
			return null;
		}else{
			if ( isTable ){
				buf.insert(0,"<b>");
			}else{
				buf.insert(0,"<b c=\"com.fib.upp.cnaps2.messagebean.send.req.HVPS_111_Out\">");
			}
			buf.append("</b>");
			if( !isWrap ){
				buf = new StringBuffer(buf.toString());
				buf.insert(0,"<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
				return buf.toString();
			}else{
				return buf.toString();
			}
		}
	}
	public boolean isNull(){
		if( null != messageIdentification){
			return false;
		}
		if( null != creationDateTime){
			return false;
		}
		if( null != numberOfTransactions){
			return false;
		}
		if( null != settlementMethod){
			return false;
		}
		if( null != endToEndIdentification){
			return false;
		}
		if( null != transactionIdentification){
			return false;
		}
		if( null != proprietary){
			return false;
		}
		if( null != currencyType){
			return false;
		}
		if( null != interbankSettlementAmount){
			return false;
		}
		if( null != settlementPriority){
			return false;
		}
		if( null != memberIdentification){
			return false;
		}
		return true;
	}
}
