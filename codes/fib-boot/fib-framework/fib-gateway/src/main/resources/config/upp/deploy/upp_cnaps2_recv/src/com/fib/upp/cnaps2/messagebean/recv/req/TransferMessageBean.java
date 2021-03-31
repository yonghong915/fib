package com.fib.upp.cnaps2.messagebean.recv.req;

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
public class TransferMessageBean extends MessageBean{

	//报文编号
	private String msgNo;

	public String getMsgNo(){
		return msgNo;
	}

	public void setMsgNo(String msgNo){
		this.msgNo = msgNo;
	}

	//账号
	private String accountNo;

	public String getAccountNo(){
		return accountNo;
	}

	public void setAccountNo(String accountNo){
		this.accountNo = accountNo;
	}

	//账务日期
	private String accountDate;

	public String getAccountDate(){
		return accountDate;
	}

	public void setAccountDate(String accountDate){
		this.accountDate = accountDate;
	}

	//账户余额
	private String accountBalance;

	public String getAccountBalance(){
		return accountBalance;
	}

	public void setAccountBalance(String accountBalance){
		this.accountBalance = accountBalance;
	}

	//余额方向
	private String balanceDirection;

	public String getBalanceDirection(){
		return balanceDirection;
	}

	public void setBalanceDirection(String balanceDirection){
		this.balanceDirection = balanceDirection;
	}

	public Object getAttribute(String name){
		if("msgNo".equals(name)){
			return  this.getMsgNo();
		}else if("accountNo".equals(name)){
			return  this.getAccountNo();
		}else if("accountDate".equals(name)){
			return  this.getAccountDate();
		}else if("accountBalance".equals(name)){
			return  this.getAccountBalance();
		}else if("balanceDirection".equals(name)){
			return  this.getBalanceDirection();
		}
		return null;
	}

	public void setAttribute(String name,Object value){
		if("msgNo".equals(name)){
			this.setMsgNo((String)value);
		}else if("accountNo".equals(name)){
			this.setAccountNo((String)value);
		}else if("accountDate".equals(name)){
			this.setAccountDate((String)value);
		}else if("accountBalance".equals(name)){
			this.setAccountBalance((String)value);
		}else if("balanceDirection".equals(name)){
			this.setBalanceDirection((String)value);
		}
	}

	public void cover(MessageBean bean){
		TransferMessageBean newBean = (TransferMessageBean) bean;

		if( null != newBean.getMsgNo() && 0 != newBean.getMsgNo().length()){
			msgNo = newBean.getMsgNo();
		}
		if( null != newBean.getAccountNo() && 0 != newBean.getAccountNo().length()){
			accountNo = newBean.getAccountNo();
		}
		if( null != newBean.getAccountDate() && 0 != newBean.getAccountDate().length()){
			accountDate = newBean.getAccountDate();
		}
		if( null != newBean.getAccountBalance() && 0 != newBean.getAccountBalance().length()){
			accountBalance = newBean.getAccountBalance();
		}
		if( null != newBean.getBalanceDirection() && 0 != newBean.getBalanceDirection().length()){
			balanceDirection = newBean.getBalanceDirection();
		}
	}

	public void validate(){
		//报文编号不为空则按以下规则校验
		if( null != msgNo){
			//报文编号数据长度检查
			try{
			if ( msgNo.getBytes("GBK").length> 4 ) {
				throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.maxLength", new String[]{"报文编号(msgNo)", "4"}));
			}
			}catch (UnsupportedEncodingException e) {
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("message.encoding.unsupport", new String[]{"TransferMessageBean", "GBK"}));
			}

		}

		//账号不为空则按以下规则校验
		if( null != accountNo){
			//账号数据长度检查
			try{
			if ( accountNo.getBytes("GBK").length> 32 ) {
				throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.maxLength", new String[]{"账号(accountNo)", "32"}));
			}
			}catch (UnsupportedEncodingException e) {
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("message.encoding.unsupport", new String[]{"TransferMessageBean", "GBK"}));
			}

		}

		//账务日期不为空则按以下规则校验
		if( null != accountDate){
			//账务日期数据长度检查
			try{
			if ( accountDate.getBytes("GBK").length> 8 ) {
				throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.maxLength", new String[]{"账务日期(accountDate)", "8"}));
			}
			}catch (UnsupportedEncodingException e) {
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("message.encoding.unsupport", new String[]{"TransferMessageBean", "GBK"}));
			}

		}

		//账户余额不为空则按以下规则校验
		if( null != accountBalance){
			//账户余额数据长度检查
			try{
			if ( accountBalance.getBytes("GBK").length> 18 ) {
				throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.maxLength", new String[]{"账户余额(accountBalance)", "18"}));
			}
			}catch (UnsupportedEncodingException e) {
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("message.encoding.unsupport", new String[]{"TransferMessageBean", "GBK"}));
			}

		}

		//余额方向不为空则按以下规则校验
		if( null != balanceDirection){
			//余额方向数据长度检查
			try{
			if ( balanceDirection.getBytes("GBK").length> 1 ) {
				throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.maxLength", new String[]{"余额方向(balanceDirection)", "1"}));
			}
			}catch (UnsupportedEncodingException e) {
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("message.encoding.unsupport", new String[]{"TransferMessageBean", "GBK"}));
			}

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
		if( null != msgNo){
			buf.append("<a n=\"msgNo\" t=\"str\">");
			buf.append(msgNo);
			buf.append("</a>");
		}
		if( null != accountNo){
			buf.append("<a n=\"accountNo\" t=\"str\">");
			buf.append(accountNo);
			buf.append("</a>");
		}
		if( null != accountDate){
			buf.append("<a n=\"accountDate\" t=\"str\">");
			buf.append(accountDate);
			buf.append("</a>");
		}
		if( null != accountBalance){
			buf.append("<a n=\"accountBalance\" t=\"str\">");
			buf.append(accountBalance);
			buf.append("</a>");
		}
		if( null != balanceDirection){
			buf.append("<a n=\"balanceDirection\" t=\"str\">");
			buf.append(balanceDirection);
			buf.append("</a>");
		}
		if( 0 == buf.length()){
			return null;
		}else{
			if ( isTable ){
				buf.insert(0,"<b>");
			}else{
				buf.insert(0,"<b c=\"com.fib.upp.cnaps2.messagebean.recv.req.TransferMessageBean\">");
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
		if( null != msgNo){
			return false;
		}
		if( null != accountNo){
			return false;
		}
		if( null != accountDate){
			return false;
		}
		if( null != accountBalance){
			return false;
		}
		if( null != balanceDirection){
			return false;
		}
		return true;
	}
}
