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
 * 批量包业务明细组件
 */
public class PKGTx extends MessageBean{

	//发起直接参与机构
	private String instructingDirectParty;

	public String getInstructingDirectParty(){
		return instructingDirectParty;
	}

	public void setInstructingDirectParty(String instructingDirectParty){
		this.instructingDirectParty = instructingDirectParty;
	}

	//接收直接参与机构
	private String instructedDirectParty;

	public String getInstructedDirectParty(){
		return instructedDirectParty;
	}

	public void setInstructedDirectParty(String instructedDirectParty){
		this.instructedDirectParty = instructedDirectParty;
	}

	//业务类型编号
	private String transactionTypeCode;

	public String getTransactionTypeCode(){
		return transactionTypeCode;
	}

	public void setTransactionTypeCode(String transactionTypeCode){
		this.transactionTypeCode = transactionTypeCode;
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
		if("InstructingDirectParty".equals(name)){
			return  this.getInstructingDirectParty();
		}else if("InstructedDirectParty".equals(name)){
			return  this.getInstructedDirectParty();
		}else if("TransactionTypeCode".equals(name)){
			return  this.getTransactionTypeCode();
		}else if("TransactionIdentification".equals(name)){
			return  this.getTransactionIdentification();
		}
		return null;
	}

	public void setAttribute(String name,Object value){
		if("InstructingDirectParty".equals(name)){
			this.setInstructingDirectParty((String)value);
		}else if("InstructedDirectParty".equals(name)){
			this.setInstructedDirectParty((String)value);
		}else if("TransactionTypeCode".equals(name)){
			this.setTransactionTypeCode((String)value);
		}else if("TransactionIdentification".equals(name)){
			this.setTransactionIdentification((String)value);
		}
	}

	public void cover(MessageBean bean){
		PKGTx newBean = (PKGTx) bean;

		if( null != newBean.getInstructingDirectParty() && 0 != newBean.getInstructingDirectParty().length()){
			instructingDirectParty = newBean.getInstructingDirectParty();
		}
		if( null != newBean.getInstructedDirectParty() && 0 != newBean.getInstructedDirectParty().length()){
			instructedDirectParty = newBean.getInstructedDirectParty();
		}
		if( null != newBean.getTransactionTypeCode() && 0 != newBean.getTransactionTypeCode().length()){
			transactionTypeCode = newBean.getTransactionTypeCode();
		}
		if( null != newBean.getTransactionIdentification() && 0 != newBean.getTransactionIdentification().length()){
			transactionIdentification = newBean.getTransactionIdentification();
		}
	}

	public void validate(){
		//发起直接参与机构不为空则按以下规则校验
		if( null != instructingDirectParty){
			//发起直接参与机构数据长度检查
			try{
			if ( instructingDirectParty.getBytes("UTF-8").length> 19 ) {
				throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.maxLength", new String[]{"发起直接参与机构(instructingDirectParty)", "19"}));
			}
			}catch (UnsupportedEncodingException e) {
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("message.encoding.unsupport", new String[]{"PKGTx", "UTF-8"}));
			}

		}

		//接收直接参与机构不为空则按以下规则校验
		if( null != instructedDirectParty){
			//接收直接参与机构数据长度检查
			try{
			if ( instructedDirectParty.getBytes("UTF-8").length> 19 ) {
				throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.maxLength", new String[]{"接收直接参与机构(instructedDirectParty)", "19"}));
			}
			}catch (UnsupportedEncodingException e) {
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("message.encoding.unsupport", new String[]{"PKGTx", "UTF-8"}));
			}

		}

		//业务类型编号不为空则按以下规则校验
		if( null != transactionTypeCode){
			//业务类型编号数据长度检查
			try{
			if ( transactionTypeCode.getBytes("UTF-8").length> 15 ) {
				throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.maxLength", new String[]{"业务类型编号(transactionTypeCode)", "15"}));
			}
			}catch (UnsupportedEncodingException e) {
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("message.encoding.unsupport", new String[]{"PKGTx", "UTF-8"}));
			}

		}

		//明细标识号不为空则按以下规则校验
		if( null != transactionIdentification){
			//明细标识号数据长度检查
			try{
			if ( transactionIdentification.getBytes("UTF-8").length> 15 ) {
				throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.maxLength", new String[]{"明细标识号(transactionIdentification)", "15"}));
			}
			}catch (UnsupportedEncodingException e) {
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("message.encoding.unsupport", new String[]{"PKGTx", "UTF-8"}));
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
		if( null != instructingDirectParty){
			buf.append("<a n=\"instructingDirectParty\" t=\"str\">");
			buf.append(instructingDirectParty);
			buf.append("</a>");
		}
		if( null != instructedDirectParty){
			buf.append("<a n=\"instructedDirectParty\" t=\"str\">");
			buf.append(instructedDirectParty);
			buf.append("</a>");
		}
		if( null != transactionTypeCode){
			buf.append("<a n=\"transactionTypeCode\" t=\"str\">");
			buf.append(transactionTypeCode);
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
				buf.insert(0,"<b c=\"com.giantstone.cnaps2.messagebean.recv.req.PKGTx\">");
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
		if( null != instructingDirectParty){
			return false;
		}
		if( null != instructedDirectParty){
			return false;
		}
		if( null != transactionTypeCode){
			return false;
		}
		if( null != transactionIdentification){
			return false;
		}
		return true;
	}
}
