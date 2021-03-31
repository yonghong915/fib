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
 * 支票业务
 */
public class BEPS_ServiceElement121_A201 extends MessageBean{

	//出票日期
	private String issueDate;

	public String getIssueDate(){
		return issueDate;
	}

	public void setIssueDate(String issueDate){
		this.issueDate = issueDate;
	}

	//出票人名称
	private String drawerName;

	public String getDrawerName(){
		return drawerName;
	}

	public void setDrawerName(String drawerName){
		this.drawerName = drawerName;
	}

	//币种
	private String currencyType;

	public String getCurrencyType(){
		return currencyType;
	}

	public void setCurrencyType(String currencyType){
		this.currencyType = currencyType;
	}

	//票据金额
	private String chequeAmount;

	public String getChequeAmount(){
		return chequeAmount;
	}

	public void setChequeAmount(String chequeAmount){
		this.chequeAmount = chequeAmount;
	}

	//币种
	private String currencyType2;

	public String getCurrencyType2(){
		return currencyType2;
	}

	public void setCurrencyType2(String currencyType2){
		this.currencyType2 = currencyType2;
	}

	//牌价
	private String amount;

	public String getAmount(){
		return amount;
	}

	public void setAmount(String amount){
		this.amount = amount;
	}

	//票据张数
	private String number;

	public String getNumber(){
		return number;
	}

	public void setNumber(String number){
		this.number = number;
	}

	public Object getAttribute(String name){
		if("IssueDate".equals(name)){
			return  this.getIssueDate();
		}else if("DrawerName".equals(name)){
			return  this.getDrawerName();
		}else if("CurrencyType".equals(name)){
			return  this.getCurrencyType();
		}else if("ChequeAmount".equals(name)){
			return  this.getChequeAmount();
		}else if("CurrencyType2".equals(name)){
			return  this.getCurrencyType2();
		}else if("Amount".equals(name)){
			return  this.getAmount();
		}else if("Number".equals(name)){
			return  this.getNumber();
		}
		return null;
	}

	public void setAttribute(String name,Object value){
		if("IssueDate".equals(name)){
			this.setIssueDate((String)value);
		}else if("DrawerName".equals(name)){
			this.setDrawerName((String)value);
		}else if("CurrencyType".equals(name)){
			this.setCurrencyType((String)value);
		}else if("ChequeAmount".equals(name)){
			this.setChequeAmount((String)value);
		}else if("CurrencyType2".equals(name)){
			this.setCurrencyType2((String)value);
		}else if("Amount".equals(name)){
			this.setAmount((String)value);
		}else if("Number".equals(name)){
			this.setNumber((String)value);
		}
	}

	public void cover(MessageBean bean){
		BEPS_ServiceElement121_A201 newBean = (BEPS_ServiceElement121_A201) bean;

		if( null != newBean.getIssueDate() && 0 != newBean.getIssueDate().length()){
			issueDate = newBean.getIssueDate();
		}
		if( null != newBean.getDrawerName() && 0 != newBean.getDrawerName().length()){
			drawerName = newBean.getDrawerName();
		}
		if( null != newBean.getCurrencyType() && 0 != newBean.getCurrencyType().length()){
			currencyType = newBean.getCurrencyType();
		}
		if( null != newBean.getChequeAmount() && 0 != newBean.getChequeAmount().length()){
			chequeAmount = newBean.getChequeAmount();
		}
		if( null != newBean.getCurrencyType2() && 0 != newBean.getCurrencyType2().length()){
			currencyType2 = newBean.getCurrencyType2();
		}
		if( null != newBean.getAmount() && 0 != newBean.getAmount().length()){
			amount = newBean.getAmount();
		}
		if( null != newBean.getNumber() && 0 != newBean.getNumber().length()){
			number = newBean.getNumber();
		}
	}

	public void validate(){
		//出票日期非空检查
		if ( null == issueDate){
			throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.null", new String[]{"出票日期(issueDate)"}));
		}

			//出票日期数据长度检查
			try{
			if ( issueDate.getBytes("UTF-8").length> 10 ) {
				throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.maxLength", new String[]{"出票日期(issueDate)", "10"}));
			}
			}catch (UnsupportedEncodingException e) {
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("message.encoding.unsupport", new String[]{"BEPS_ServiceElement121_A201", "UTF-8"}));
			}

			//出票日期格式检查
			try{
				java.text.DateFormat dateformat = new java.text.SimpleDateFormat("yyyy-MM-dd");
				dateformat.setLenient(false);
				dateformat.parse(issueDate);
			} catch (Exception e) {
				throw new RuntimeException(
					MultiLanguageResourceBundle.getInstance().getString("Message.field.pattern.illegal", new String[]{"出票日期(issueDate)", "yyyy-MM-dd"}) + e.getMessage(), e);
			}

		//出票人名称非空检查
		if ( null == drawerName){
			throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.null", new String[]{"出票人名称(drawerName)"}));
		}

			//出票人名称数据长度检查
			try{
			if ( drawerName.getBytes("UTF-8").length> 60 ) {
				throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.maxLength", new String[]{"出票人名称(drawerName)", "60"}));
			}
			}catch (UnsupportedEncodingException e) {
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("message.encoding.unsupport", new String[]{"BEPS_ServiceElement121_A201", "UTF-8"}));
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
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("message.encoding.unsupport", new String[]{"BEPS_ServiceElement121_A201", "UTF-8"}));
			}

		}

		//票据金额非空检查
		if ( null == chequeAmount){
			throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.null", new String[]{"票据金额(chequeAmount)"}));
		}

			//票据金额数据长度检查
			try{
			if ( chequeAmount.getBytes("UTF-8").length> 19 ) {
				throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.maxLength", new String[]{"票据金额(chequeAmount)", "19"}));
			}
			}catch (UnsupportedEncodingException e) {
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("message.encoding.unsupport", new String[]{"BEPS_ServiceElement121_A201", "UTF-8"}));
			}

		//币种不为空则按以下规则校验
		if( null != currencyType2){
			//币种数据长度检查
			try{
			if ( currencyType2.getBytes("UTF-8").length> 5 ) {
				throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.maxLength", new String[]{"币种(currencyType2)", "5"}));
			}
			}catch (UnsupportedEncodingException e) {
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("message.encoding.unsupport", new String[]{"BEPS_ServiceElement121_A201", "UTF-8"}));
			}

		}

		//牌价不为空则按以下规则校验
		if( null != amount){
			//牌价数据长度检查
			try{
			if ( amount.getBytes("UTF-8").length> 19 ) {
				throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.maxLength", new String[]{"牌价(amount)", "19"}));
			}
			}catch (UnsupportedEncodingException e) {
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("message.encoding.unsupport", new String[]{"BEPS_ServiceElement121_A201", "UTF-8"}));
			}

		}

		//票据张数非空检查
		if ( null == number){
			throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.null", new String[]{"票据张数(number)"}));
		}

			//票据张数数据长度检查
			try{
			if ( number.getBytes("UTF-8").length> 4 ) {
				throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.maxLength", new String[]{"票据张数(number)", "4"}));
			}
			}catch (UnsupportedEncodingException e) {
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("message.encoding.unsupport", new String[]{"BEPS_ServiceElement121_A201", "UTF-8"}));
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
		if( null != issueDate){
			buf.append("<a n=\"issueDate\" t=\"datetime\">");
			buf.append(issueDate);
			buf.append("</a>");
		}
		if( null != drawerName){
			buf.append("<a n=\"drawerName\" t=\"str\">");
			buf.append(drawerName);
			buf.append("</a>");
		}
		if( null != currencyType){
			buf.append("<a n=\"currencyType\" t=\"str\">");
			buf.append(currencyType);
			buf.append("</a>");
		}
		if( null != chequeAmount){
			buf.append("<a n=\"chequeAmount\" t=\"str\">");
			buf.append(chequeAmount);
			buf.append("</a>");
		}
		if( null != currencyType2){
			buf.append("<a n=\"currencyType2\" t=\"str\">");
			buf.append(currencyType2);
			buf.append("</a>");
		}
		if( null != amount){
			buf.append("<a n=\"amount\" t=\"str\">");
			buf.append(amount);
			buf.append("</a>");
		}
		if( null != number){
			buf.append("<a n=\"number\" t=\"str\">");
			buf.append(number);
			buf.append("</a>");
		}
		if( 0 == buf.length()){
			return null;
		}else{
			if ( isTable ){
				buf.insert(0,"<b>");
			}else{
				buf.insert(0,"<b c=\"com.giantstone.cnaps2.messagebean.recv.req.BEPS_ServiceElement121_A201\">");
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
		if( null != issueDate){
			return false;
		}
		if( null != drawerName){
			return false;
		}
		if( null != currencyType){
			return false;
		}
		if( null != chequeAmount){
			return false;
		}
		if( null != currencyType2){
			return false;
		}
		if( null != amount){
			return false;
		}
		if( null != number){
			return false;
		}
		return true;
	}
}
