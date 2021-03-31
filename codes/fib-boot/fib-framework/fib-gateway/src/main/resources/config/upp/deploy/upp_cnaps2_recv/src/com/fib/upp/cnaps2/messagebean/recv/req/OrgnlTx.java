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
 * 原业务组件
 */
public class OrgnlTx extends MessageBean{

	//原发起间接参与机构
	private String instructingIndirectParty;

	public String getInstructingIndirectParty(){
		return instructingIndirectParty;
	}

	public void setInstructingIndirectParty(String instructingIndirectParty){
		this.instructingIndirectParty = instructingIndirectParty;
	}

	//原接收间接参与机构
	private String instructedIndirectParty;

	public String getInstructedIndirectParty(){
		return instructedIndirectParty;
	}

	public void setInstructedIndirectParty(String instructedIndirectParty){
		this.instructedIndirectParty = instructedIndirectParty;
	}

	//原明细标识号
	private String originalTransactionIdentification;

	public String getOriginalTransactionIdentification(){
		return originalTransactionIdentification;
	}

	public void setOriginalTransactionIdentification(String originalTransactionIdentification){
		this.originalTransactionIdentification = originalTransactionIdentification;
	}

	//原业务类型编号
	private String originalTransactionTypeCode;

	public String getOriginalTransactionTypeCode(){
		return originalTransactionTypeCode;
	}

	public void setOriginalTransactionTypeCode(String originalTransactionTypeCode){
		this.originalTransactionTypeCode = originalTransactionTypeCode;
	}

	public Object getAttribute(String name){
		if("InstructingIndirectParty".equals(name)){
			return  this.getInstructingIndirectParty();
		}else if("InstructedIndirectParty".equals(name)){
			return  this.getInstructedIndirectParty();
		}else if("OriginalTransactionIdentification".equals(name)){
			return  this.getOriginalTransactionIdentification();
		}else if("OriginalTransactionTypeCode".equals(name)){
			return  this.getOriginalTransactionTypeCode();
		}
		return null;
	}

	public void setAttribute(String name,Object value){
		if("InstructingIndirectParty".equals(name)){
			this.setInstructingIndirectParty((String)value);
		}else if("InstructedIndirectParty".equals(name)){
			this.setInstructedIndirectParty((String)value);
		}else if("OriginalTransactionIdentification".equals(name)){
			this.setOriginalTransactionIdentification((String)value);
		}else if("OriginalTransactionTypeCode".equals(name)){
			this.setOriginalTransactionTypeCode((String)value);
		}
	}

	public void cover(MessageBean bean){
		OrgnlTx newBean = (OrgnlTx) bean;

		if( null != newBean.getInstructingIndirectParty() && 0 != newBean.getInstructingIndirectParty().length()){
			instructingIndirectParty = newBean.getInstructingIndirectParty();
		}
		if( null != newBean.getInstructedIndirectParty() && 0 != newBean.getInstructedIndirectParty().length()){
			instructedIndirectParty = newBean.getInstructedIndirectParty();
		}
		if( null != newBean.getOriginalTransactionIdentification() && 0 != newBean.getOriginalTransactionIdentification().length()){
			originalTransactionIdentification = newBean.getOriginalTransactionIdentification();
		}
		if( null != newBean.getOriginalTransactionTypeCode() && 0 != newBean.getOriginalTransactionTypeCode().length()){
			originalTransactionTypeCode = newBean.getOriginalTransactionTypeCode();
		}
	}

	public void validate(){
		//原发起间接参与机构不为空则按以下规则校验
		if( null != instructingIndirectParty){
			//原发起间接参与机构数据长度检查
			try{
			if ( instructingIndirectParty.getBytes("UTF-8").length> 14 ) {
				throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.maxLength", new String[]{"原发起间接参与机构(instructingIndirectParty)", "14"}));
			}
			}catch (UnsupportedEncodingException e) {
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("message.encoding.unsupport", new String[]{"OrgnlTx", "UTF-8"}));
			}

		}

		//原接收间接参与机构不为空则按以下规则校验
		if( null != instructedIndirectParty){
			//原接收间接参与机构数据长度检查
			try{
			if ( instructedIndirectParty.getBytes("UTF-8").length> 14 ) {
				throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.maxLength", new String[]{"原接收间接参与机构(instructedIndirectParty)", "14"}));
			}
			}catch (UnsupportedEncodingException e) {
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("message.encoding.unsupport", new String[]{"OrgnlTx", "UTF-8"}));
			}

		}

		//原明细标识号不为空则按以下规则校验
		if( null != originalTransactionIdentification){
			//原明细标识号数据长度检查
			try{
			if ( originalTransactionIdentification.getBytes("UTF-8").length> 16 ) {
				throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.maxLength", new String[]{"原明细标识号(originalTransactionIdentification)", "16"}));
			}
			}catch (UnsupportedEncodingException e) {
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("message.encoding.unsupport", new String[]{"OrgnlTx", "UTF-8"}));
			}

		}

		//原业务类型编号不为空则按以下规则校验
		if( null != originalTransactionTypeCode){
			//原业务类型编号数据长度检查
			try{
			if ( originalTransactionTypeCode.getBytes("UTF-8").length> 4 ) {
				throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.maxLength", new String[]{"原业务类型编号(originalTransactionTypeCode)", "4"}));
			}
			}catch (UnsupportedEncodingException e) {
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("message.encoding.unsupport", new String[]{"OrgnlTx", "UTF-8"}));
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
		if( null != instructingIndirectParty){
			buf.append("<a n=\"instructingIndirectParty\" t=\"str\">");
			buf.append(instructingIndirectParty);
			buf.append("</a>");
		}
		if( null != instructedIndirectParty){
			buf.append("<a n=\"instructedIndirectParty\" t=\"str\">");
			buf.append(instructedIndirectParty);
			buf.append("</a>");
		}
		if( null != originalTransactionIdentification){
			buf.append("<a n=\"originalTransactionIdentification\" t=\"str\">");
			buf.append(originalTransactionIdentification);
			buf.append("</a>");
		}
		if( null != originalTransactionTypeCode){
			buf.append("<a n=\"originalTransactionTypeCode\" t=\"str\">");
			buf.append(originalTransactionTypeCode);
			buf.append("</a>");
		}
		if( 0 == buf.length()){
			return null;
		}else{
			if ( isTable ){
				buf.insert(0,"<b>");
			}else{
				buf.insert(0,"<b c=\"com.fib.upp.cnaps2.messagebean.recv.req.OrgnlTx\">");
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
		if( null != instructingIndirectParty){
			return false;
		}
		if( null != instructedIndirectParty){
			return false;
		}
		if( null != originalTransactionIdentification){
			return false;
		}
		if( null != originalTransactionTypeCode){
			return false;
		}
		return true;
	}
}
