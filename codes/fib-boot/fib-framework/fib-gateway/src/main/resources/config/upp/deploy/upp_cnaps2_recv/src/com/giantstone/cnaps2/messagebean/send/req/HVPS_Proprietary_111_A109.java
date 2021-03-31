package com.giantstone.cnaps2.messagebean.send.req;

import com.fib.gateway.message.bean.*;
import com.fib.gateway.message.xml.message.*;
import com.fib.gateway.message.*;
import com.fib.gateway.message.util.*;
import java.math.BigDecimal;
import java.io.UnsupportedEncodingException;
import com.fib.gateway.message.xml.message.MultiLanguageResourceBundle;
import java.util.*;

/**
 * 委托收款
 */
public class HVPS_Proprietary_111_A109 extends MessageBean{

	//票据日期
	private String unstructured;

	public String getUnstructured(){
		return unstructured;
	}

	public void setUnstructured(String unstructured){
		this.unstructured = unstructured;
	}

	//凭证种类
	private String unstructured2;

	public String getUnstructured2(){
		return unstructured2;
	}

	public void setUnstructured2(String unstructured2){
		this.unstructured2 = unstructured2;
	}

	public Object getAttribute(String name){
		if("Unstructured".equals(name)){
			return  this.getUnstructured();
		}else if("Unstructured2".equals(name)){
			return  this.getUnstructured2();
		}
		return null;
	}

	public void setAttribute(String name,Object value){
		if("Unstructured".equals(name)){
			this.setUnstructured((String)value);
		}else if("Unstructured2".equals(name)){
			this.setUnstructured2((String)value);
		}
	}

	public void cover(MessageBean bean){
		HVPS_Proprietary_111_A109 newBean = (HVPS_Proprietary_111_A109) bean;

		if( null != newBean.getUnstructured() && 0 != newBean.getUnstructured().length()){
			unstructured = newBean.getUnstructured();
		}
		if( null != newBean.getUnstructured2() && 0 != newBean.getUnstructured2().length()){
			unstructured2 = newBean.getUnstructured2();
		}
	}

	public void validate(){
		//票据日期不为空则按以下规则校验
		if( null != unstructured){
			//票据日期数据长度检查
			try{
			if ( unstructured.getBytes("UTF-8").length> 15 ) {
				throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.maxLength", new String[]{"票据日期(unstructured)", "15"}));
			}
			}catch (UnsupportedEncodingException e) {
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("message.encoding.unsupport", new String[]{"HVPS_Proprietary_111_A109", "UTF-8"}));
			}

		}

		//凭证种类不为空则按以下规则校验
		if( null != unstructured2){
			//凭证种类数据长度检查
			try{
			if ( unstructured2.getBytes("UTF-8").length> 7 ) {
				throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.maxLength", new String[]{"凭证种类(unstructured2)", "7"}));
			}
			}catch (UnsupportedEncodingException e) {
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("message.encoding.unsupport", new String[]{"HVPS_Proprietary_111_A109", "UTF-8"}));
			}

		}

		//凭证种类指定值域检查
		if ( null != unstructured2 ) {
		if ( !"/F1A/99".equals( unstructured2) && !"/F1A/01".equals( unstructured2) && !"/F1A/02".equals( unstructured2) && !"/F1A/03".equals( unstructured2) && !"/F1A/04".equals( unstructured2) ) {
			throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.value.in", new String[]{")凭证种类(unstructured2)", "/F1A/99//F1A/01//F1A/02//F1A/03//F1A/04"}));
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
		if( null != unstructured){
			buf.append("<a n=\"unstructured\" t=\"str\">");
			buf.append(unstructured);
			buf.append("</a>");
		}
		if( null != unstructured2){
			buf.append("<a n=\"unstructured2\" t=\"str\">");
			buf.append(unstructured2);
			buf.append("</a>");
		}
		if( 0 == buf.length()){
			return null;
		}else{
			if ( isTable ){
				buf.insert(0,"<b>");
			}else{
				buf.insert(0,"<b c=\"com.giantstone.cnaps2.messagebean.send.req.HVPS_Proprietary_111_A109\">");
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
		if( null != unstructured){
			return false;
		}
		if( null != unstructured2){
			return false;
		}
		return true;
	}
}
