package com.giantstone.cnaps2.messagebean.recv.req;

import com.fib.gateway.message.bean.*;
import com.fib.gateway.message.xml.message.*;
import com.fib.gateway.message.*;
import com.fib.gateway.message.util.*;
import java.math.BigDecimal;
import java.io.UnsupportedEncodingException;
import com.fib.gateway.message.xml.message.MultiLanguageResourceBundle;
import java.util.*;

/**
 * CustomerCreditTransferInformation
 */
public class BEPS_121_OutCcti extends MessageBean{

	//附言
	private String additionalInformation;

	public String getAdditionalInformation(){
		return additionalInformation;
	}

	public void setAdditionalInformation(String additionalInformation){
		this.additionalInformation = additionalInformation;
	}

	//付款人地址
	private String addressLine;

	public String getAddressLine(){
		return addressLine;
	}

	public void setAddressLine(String addressLine){
		this.addressLine = addressLine;
	}

	//收款人地址
	private String addressLine2;

	public String getAddressLine2(){
		return addressLine2;
	}

	public void setAddressLine2(String addressLine2){
		this.addressLine2 = addressLine2;
	}

	//货币符号、金额
	private String amount;

	public String getAmount(){
		return amount;
	}

	public void setAmount(String amount){
		this.amount = amount;
	}

	//币种
	private String currencyType;

	public String getCurrencyType(){
		return currencyType;
	}

	public void setCurrencyType(String currencyType){
		this.currencyType = currencyType;
	}

	//附加数据
	private com.fib.gateway.message.bean.MessageBean customerCreditTransfer;

	public com.fib.gateway.message.bean.MessageBean getCustomerCreditTransfer(){
		return customerCreditTransfer;
	}

	public void setCustomerCreditTransfer(com.fib.gateway.message.bean.MessageBean customerCreditTransfer){
		this.customerCreditTransfer = customerCreditTransfer;
	}

	//付款人账号
	private String identification;

	public String getIdentification(){
		return identification;
	}

	public void setIdentification(String identification){
		this.identification = identification;
	}

	//付款行行号
	private String identification2;

	public String getIdentification2(){
		return identification2;
	}

	public void setIdentification2(String identification2){
		this.identification2 = identification2;
	}

	//收款行行号
	private String identification3;

	public String getIdentification3(){
		return identification3;
	}

	public void setIdentification3(String identification3){
		this.identification3 = identification3;
	}

	//收款人账号
	private String identification4;

	public String getIdentification4(){
		return identification4;
	}

	public void setIdentification4(String identification4){
		this.identification4 = identification4;
	}

	//付款人开户行行号
	private String issuer;

	public String getIssuer(){
		return issuer;
	}

	public void setIssuer(String issuer){
		this.issuer = issuer;
	}

	//收款人开户行行号
	private String issuer2;

	public String getIssuer2(){
		return issuer2;
	}

	public void setIssuer2(String issuer2){
		this.issuer2 = issuer2;
	}

	//付款人名称
	private String name;

	public String getName(){
		return name;
	}

	public void setName(String name){
		this.name = name;
	}

	//收款人名称
	private String name2;

	public String getName2(){
		return name2;
	}

	public void setName2(String name2){
		this.name2 = name2;
	}

	//业务类型编码
	private String proprietary;

	public String getProprietary(){
		return proprietary;
	}

	public void setProprietary(String proprietary){
		this.proprietary = proprietary;
	}

	//业务种类编码
	private String proprietary2;

	public String getProprietary2(){
		return proprietary2;
	}

	public void setProprietary2(String proprietary2){
		this.proprietary2 = proprietary2;
	}

	//明细标识号
	private String transactionIdentification;

	public String getTransactionIdentification(){
		return transactionIdentification;
	}

	public void setTransactionIdentification(String transactionIdentification){
		this.transactionIdentification = transactionIdentification;
	}

	public Object getAttribute(String name){
		if("AdditionalInformation".equals(name)){
			return  this.getAdditionalInformation();
		}else if("AddressLine".equals(name)){
			return  this.getAddressLine();
		}else if("AddressLine2".equals(name)){
			return  this.getAddressLine2();
		}else if("Amount".equals(name)){
			return  this.getAmount();
		}else if("CurrencyType".equals(name)){
			return  this.getCurrencyType();
		}else if("CustomerCreditTransfer".equals(name)){
			return  this.getCustomerCreditTransfer();
		}else if("Identification".equals(name)){
			return  this.getIdentification();
		}else if("Identification2".equals(name)){
			return  this.getIdentification2();
		}else if("Identification3".equals(name)){
			return  this.getIdentification3();
		}else if("Identification4".equals(name)){
			return  this.getIdentification4();
		}else if("Issuer".equals(name)){
			return  this.getIssuer();
		}else if("Issuer2".equals(name)){
			return  this.getIssuer2();
		}else if("Name".equals(name)){
			return  this.getName();
		}else if("Name2".equals(name)){
			return  this.getName2();
		}else if("Proprietary".equals(name)){
			return  this.getProprietary();
		}else if("Proprietary2".equals(name)){
			return  this.getProprietary2();
		}else if("TransactionIdentification".equals(name)){
			return  this.getTransactionIdentification();
		}
		return null;
	}

	public void setAttribute(String name,Object value){
		if("AdditionalInformation".equals(name)){
			this.setAdditionalInformation((String)value);
		}else if("AddressLine".equals(name)){
			this.setAddressLine((String)value);
		}else if("AddressLine2".equals(name)){
			this.setAddressLine2((String)value);
		}else if("Amount".equals(name)){
			this.setAmount((String)value);
		}else if("CurrencyType".equals(name)){
			this.setCurrencyType((String)value);
		}else if("CustomerCreditTransfer".equals(name)){
			this.setCustomerCreditTransfer((com.fib.gateway.message.bean.MessageBean)value);
		}else if("Identification".equals(name)){
			this.setIdentification((String)value);
		}else if("Identification2".equals(name)){
			this.setIdentification2((String)value);
		}else if("Identification3".equals(name)){
			this.setIdentification3((String)value);
		}else if("Identification4".equals(name)){
			this.setIdentification4((String)value);
		}else if("Issuer".equals(name)){
			this.setIssuer((String)value);
		}else if("Issuer2".equals(name)){
			this.setIssuer2((String)value);
		}else if("Name".equals(name)){
			this.setName((String)value);
		}else if("Name2".equals(name)){
			this.setName2((String)value);
		}else if("Proprietary".equals(name)){
			this.setProprietary((String)value);
		}else if("Proprietary2".equals(name)){
			this.setProprietary2((String)value);
		}else if("TransactionIdentification".equals(name)){
			this.setTransactionIdentification((String)value);
		}
	}

	public void cover(MessageBean bean){
		BEPS_121_OutCcti newBean = (BEPS_121_OutCcti) bean;

		if( null != newBean.getAdditionalInformation() && 0 != newBean.getAdditionalInformation().length()){
			additionalInformation = newBean.getAdditionalInformation();
		}
		if( null != newBean.getAddressLine() && 0 != newBean.getAddressLine().length()){
			addressLine = newBean.getAddressLine();
		}
		if( null != newBean.getAddressLine2() && 0 != newBean.getAddressLine2().length()){
			addressLine2 = newBean.getAddressLine2();
		}
		if( null != newBean.getAmount() && 0 != newBean.getAmount().length()){
			amount = newBean.getAmount();
		}
		if( null != newBean.getCurrencyType() && 0 != newBean.getCurrencyType().length()){
			currencyType = newBean.getCurrencyType();
		}
		if( null != customerCreditTransfer&&null != newBean.getCustomerCreditTransfer()){
			customerCreditTransfer.cover(newBean.getCustomerCreditTransfer());
		}
		if( null != newBean.getIdentification() && 0 != newBean.getIdentification().length()){
			identification = newBean.getIdentification();
		}
		if( null != newBean.getIdentification2() && 0 != newBean.getIdentification2().length()){
			identification2 = newBean.getIdentification2();
		}
		if( null != newBean.getIdentification3() && 0 != newBean.getIdentification3().length()){
			identification3 = newBean.getIdentification3();
		}
		if( null != newBean.getIdentification4() && 0 != newBean.getIdentification4().length()){
			identification4 = newBean.getIdentification4();
		}
		if( null != newBean.getIssuer() && 0 != newBean.getIssuer().length()){
			issuer = newBean.getIssuer();
		}
		if( null != newBean.getIssuer2() && 0 != newBean.getIssuer2().length()){
			issuer2 = newBean.getIssuer2();
		}
		if( null != newBean.getName() && 0 != newBean.getName().length()){
			name = newBean.getName();
		}
		if( null != newBean.getName2() && 0 != newBean.getName2().length()){
			name2 = newBean.getName2();
		}
		if( null != newBean.getProprietary() && 0 != newBean.getProprietary().length()){
			proprietary = newBean.getProprietary();
		}
		if( null != newBean.getProprietary2() && 0 != newBean.getProprietary2().length()){
			proprietary2 = newBean.getProprietary2();
		}
		if( null != newBean.getTransactionIdentification() && 0 != newBean.getTransactionIdentification().length()){
			transactionIdentification = newBean.getTransactionIdentification();
		}
	}

	public void validate(){
		//附言不为空则按以下规则校验
		if( null != additionalInformation){
			//附言数据长度检查
			if ( additionalInformation.getBytes().length > 256 ) {
				throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.maxLength", new String[]{"附言(additionalInformation)", "256"}));
			}

		}

		//付款人地址不为空则按以下规则校验
		if( null != addressLine){
			//付款人地址数据长度检查
			if ( addressLine.getBytes().length > 70 ) {
				throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.maxLength", new String[]{"付款人地址(addressLine)", "70"}));
			}

		}

		//收款人地址不为空则按以下规则校验
		if( null != addressLine2){
			//收款人地址数据长度检查
			if ( addressLine2.getBytes().length > 70 ) {
				throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.maxLength", new String[]{"收款人地址(addressLine2)", "70"}));
			}

		}

		//货币符号、金额非空检查
		if ( null == amount){
			throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.null", new String[]{"货币符号、金额(amount)"}));
		}

			//货币符号、金额数据长度检查
			if ( amount.getBytes().length > 18 ) {
				throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.maxLength", new String[]{"货币符号、金额(amount)", "18"}));
			}

		//币种不为空则按以下规则校验
		if( null != currencyType){
			//币种数据长度检查
			if ( currencyType.getBytes().length > 5 ) {
				throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.maxLength", new String[]{"币种(currencyType)", "5"}));
			}

		}

		//附加数据正确性检查
		if ( null != customerCreditTransfer && !customerCreditTransfer.isNull() ){ 
			customerCreditTransfer.validate();
		}

		//付款人账号非空检查
		if ( null == identification){
			throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.null", new String[]{"付款人账号(identification)"}));
		}

			//付款人账号数据长度检查
			if ( identification.getBytes().length > 32 ) {
				throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.maxLength", new String[]{"付款人账号(identification)", "32"}));
			}

		//付款行行号非空检查
		if ( null == identification2){
			throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.null", new String[]{"付款行行号(identification2)"}));
		}

			//付款行行号数据长度检查
			if ( identification2.getBytes().length > 14 ) {
				throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.maxLength", new String[]{"付款行行号(identification2)", "14"}));
			}

		//收款行行号非空检查
		if ( null == identification3){
			throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.null", new String[]{"收款行行号(identification3)"}));
		}

			//收款行行号数据长度检查
			if ( identification3.getBytes().length > 14 ) {
				throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.maxLength", new String[]{"收款行行号(identification3)", "14"}));
			}

		//收款人账号非空检查
		if ( null == identification4){
			throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.null", new String[]{"收款人账号(identification4)"}));
		}

			//收款人账号数据长度检查
			if ( identification4.getBytes().length > 32 ) {
				throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.maxLength", new String[]{"收款人账号(identification4)", "32"}));
			}

		//付款人开户行行号非空检查
		if ( null == issuer){
			throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.null", new String[]{"付款人开户行行号(issuer)"}));
		}

			//付款人开户行行号数据长度检查
			if ( issuer.getBytes().length > 14 ) {
				throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.maxLength", new String[]{"付款人开户行行号(issuer)", "14"}));
			}

		//收款人开户行行号非空检查
		if ( null == issuer2){
			throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.null", new String[]{"收款人开户行行号(issuer2)"}));
		}

			//收款人开户行行号数据长度检查
			if ( issuer2.getBytes().length > 14 ) {
				throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.maxLength", new String[]{"收款人开户行行号(issuer2)", "14"}));
			}

		//付款人名称非空检查
		if ( null == name){
			throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.null", new String[]{"付款人名称(name)"}));
		}

			//付款人名称数据长度检查
			if ( name.getBytes().length > 60 ) {
				throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.maxLength", new String[]{"付款人名称(name)", "60"}));
			}

		//收款人名称非空检查
		if ( null == name2){
			throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.null", new String[]{"收款人名称(name2)"}));
		}

			//收款人名称数据长度检查
			if ( name2.getBytes().length > 60 ) {
				throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.maxLength", new String[]{"收款人名称(name2)", "60"}));
			}

		//业务类型编码不为空则按以下规则校验
		if( null != proprietary){
			//业务类型编码数据长度检查
			if ( proprietary.getBytes().length > 4 ) {
				throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.maxLength", new String[]{"业务类型编码(proprietary)", "4"}));
			}

		}

		//业务种类编码非空检查
		if ( null == proprietary2){
			throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.null", new String[]{"业务种类编码(proprietary2)"}));
		}

			//业务种类编码数据长度检查
			if ( proprietary2.getBytes().length > 5 ) {
				throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.maxLength", new String[]{"业务种类编码(proprietary2)", "5"}));
			}

		//明细标识号非空检查
		if ( null == transactionIdentification){
			throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.null", new String[]{"明细标识号(transactionIdentification)"}));
		}

			//明细标识号数据长度检查
			if ( transactionIdentification.getBytes().length > 16 ) {
				throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.maxLength", new String[]{"明细标识号(transactionIdentification)", "16"}));
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
		if( null != additionalInformation){
			buf.append("<a n=\"additionalInformation\" t=\"str\">");
			buf.append(additionalInformation);
			buf.append("</a>");
		}
		if( null != addressLine){
			buf.append("<a n=\"addressLine\" t=\"str\">");
			buf.append(addressLine);
			buf.append("</a>");
		}
		if( null != addressLine2){
			buf.append("<a n=\"addressLine2\" t=\"str\">");
			buf.append(addressLine2);
			buf.append("</a>");
		}
		if( null != amount){
			buf.append("<a n=\"amount\" t=\"str\">");
			buf.append(amount);
			buf.append("</a>");
		}
		if( null != currencyType){
			buf.append("<a n=\"currencyType\" t=\"str\">");
			buf.append(currencyType);
			buf.append("</a>");
		}
		if( null != customerCreditTransfer){
			str = customerCreditTransfer.toString(true);
			if( null != str){
				buf.append("<a n=\"customerCreditTransfer\" t=\"bean\">");
				buf.append(str);
				buf.append("</a>");
			}
		}
		if( null != identification){
			buf.append("<a n=\"identification\" t=\"str\">");
			buf.append(identification);
			buf.append("</a>");
		}
		if( null != identification2){
			buf.append("<a n=\"identification2\" t=\"str\">");
			buf.append(identification2);
			buf.append("</a>");
		}
		if( null != identification3){
			buf.append("<a n=\"identification3\" t=\"str\">");
			buf.append(identification3);
			buf.append("</a>");
		}
		if( null != identification4){
			buf.append("<a n=\"identification4\" t=\"str\">");
			buf.append(identification4);
			buf.append("</a>");
		}
		if( null != issuer){
			buf.append("<a n=\"issuer\" t=\"str\">");
			buf.append(issuer);
			buf.append("</a>");
		}
		if( null != issuer2){
			buf.append("<a n=\"issuer2\" t=\"str\">");
			buf.append(issuer2);
			buf.append("</a>");
		}
		if( null != name){
			buf.append("<a n=\"name\" t=\"str\">");
			buf.append(name);
			buf.append("</a>");
		}
		if( null != name2){
			buf.append("<a n=\"name2\" t=\"str\">");
			buf.append(name2);
			buf.append("</a>");
		}
		if( null != proprietary){
			buf.append("<a n=\"proprietary\" t=\"str\">");
			buf.append(proprietary);
			buf.append("</a>");
		}
		if( null != proprietary2){
			buf.append("<a n=\"proprietary2\" t=\"str\">");
			buf.append(proprietary2);
			buf.append("</a>");
		}
		if( null != transactionIdentification){
			buf.append("<a n=\"transactionIdentification\" t=\"str\">");
			buf.append(transactionIdentification);
			buf.append("</a>");
		}
		if( 0 == buf.length()){
			return null;
		}else{
			if ( isTable ){
				buf.insert(0,"<b>");
			}else{
				buf.insert(0,"<b c=\"com.giantstone.cnaps2.messagebean.recv.req.BEPS_121_OutCcti\">");
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
		if( null != additionalInformation){
			return false;
		}
		if( null != addressLine){
			return false;
		}
		if( null != addressLine2){
			return false;
		}
		if( null != amount){
			return false;
		}
		if( null != currencyType){
			return false;
		}
		if( null != customerCreditTransfer){
			if (  !customerCreditTransfer.isNull() ){
				return false;
			}
		}
		if( null != identification){
			return false;
		}
		if( null != identification2){
			return false;
		}
		if( null != identification3){
			return false;
		}
		if( null != identification4){
			return false;
		}
		if( null != issuer){
			return false;
		}
		if( null != issuer2){
			return false;
		}
		if( null != name){
			return false;
		}
		if( null != name2){
			return false;
		}
		if( null != proprietary){
			return false;
		}
		if( null != proprietary2){
			return false;
		}
		if( null != transactionIdentification){
			return false;
		}
		return true;
	}
}
