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
 * 计费清单信息
 */
public class PBCS_608_OutChargeInformation extends MessageBean{

	//计费/返还总金额
	private String amount;

	public String getAmount(){
		return amount;
	}

	public void setAmount(String amount){
		this.amount = amount;
	}

	//计费所属系统
	private String chargeSystem;

	public String getChargeSystem(){
		return chargeSystem;
	}

	public void setChargeSystem(String chargeSystem){
		this.chargeSystem = chargeSystem;
	}

	//币种
	private String currencyType2;

	public String getCurrencyType2(){
		return currencyType2;
	}

	public void setCurrencyType2(String currencyType2){
		this.currencyType2 = currencyType2;
	}

	public Object getAttribute(String name){
		if("Amount".equals(name)){
			return  this.getAmount();
		}else if("ChargeSystem".equals(name)){
			return  this.getChargeSystem();
		}else if("CurrencyType2".equals(name)){
			return  this.getCurrencyType2();
		}
		return null;
	}

	public void setAttribute(String name,Object value){
		if("Amount".equals(name)){
			this.setAmount((String)value);
		}else if("ChargeSystem".equals(name)){
			this.setChargeSystem((String)value);
		}else if("CurrencyType2".equals(name)){
			this.setCurrencyType2((String)value);
		}
	}

	public void cover(MessageBean bean){
		PBCS_608_OutChargeInformation newBean = (PBCS_608_OutChargeInformation) bean;

		if( null != newBean.getAmount() && 0 != newBean.getAmount().length()){
			amount = newBean.getAmount();
		}
		if( null != newBean.getChargeSystem() && 0 != newBean.getChargeSystem().length()){
			chargeSystem = newBean.getChargeSystem();
		}
		if( null != newBean.getCurrencyType2() && 0 != newBean.getCurrencyType2().length()){
			currencyType2 = newBean.getCurrencyType2();
		}
	}

	public void validate(){
		//计费/返还总金额非空检查
		if ( null == amount){
			throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.null", new String[]{"计费/返还总金额(amount)"}));
		}

			//计费/返还总金额数据长度检查
			if ( amount.getBytes().length > 19 ) {
				throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.maxLength", new String[]{"计费/返还总金额(amount)", "19"}));
			}

		//计费所属系统非空检查
		if ( null == chargeSystem){
			throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.null", new String[]{"计费所属系统(chargeSystem)"}));
		}

			//计费所属系统数据长度检查
			if ( chargeSystem.getBytes().length > 35 ) {
				throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.maxLength", new String[]{"计费所属系统(chargeSystem)", "35"}));
			}

		//币种不为空则按以下规则校验
		if( null != currencyType2){
			//币种数据长度检查
			if ( currencyType2.getBytes().length > 5 ) {
				throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.maxLength", new String[]{"币种(currencyType2)", "5"}));
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
		if( null != amount){
			buf.append("<a n=\"amount\" t=\"str\">");
			buf.append(amount);
			buf.append("</a>");
		}
		if( null != chargeSystem){
			buf.append("<a n=\"chargeSystem\" t=\"str\">");
			buf.append(chargeSystem);
			buf.append("</a>");
		}
		if( null != currencyType2){
			buf.append("<a n=\"currencyType2\" t=\"str\">");
			buf.append(currencyType2);
			buf.append("</a>");
		}
		if( 0 == buf.length()){
			return null;
		}else{
			if ( isTable ){
				buf.insert(0,"<b>");
			}else{
				buf.insert(0,"<b c=\"com.giantstone.cnaps2.messagebean.recv.req.PBCS_608_OutChargeInformation\">");
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
		if( null != amount){
			return false;
		}
		if( null != chargeSystem){
			return false;
		}
		if( null != currencyType2){
			return false;
		}
		return true;
	}
}
