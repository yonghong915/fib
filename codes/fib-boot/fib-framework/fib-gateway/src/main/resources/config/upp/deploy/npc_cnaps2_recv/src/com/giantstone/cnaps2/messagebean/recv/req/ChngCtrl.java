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
 * 数据变更组件
 */
public class ChngCtrl extends MessageBean{

	//变更类型
	private String changeType;

	public String getChangeType(){
		return changeType;
	}

	public void setChangeType(String changeType){
		this.changeType = changeType;
	}

	//生效类型
	private String effectiveType;

	public String getEffectiveType(){
		return effectiveType;
	}

	public void setEffectiveType(String effectiveType){
		this.effectiveType = effectiveType;
	}

	//生效日期
	private String effectiveDate;

	public String getEffectiveDate(){
		return effectiveDate;
	}

	public void setEffectiveDate(String effectiveDate){
		this.effectiveDate = effectiveDate;
	}

	//失效日期
	private String ineffectiveDate;

	public String getIneffectiveDate(){
		return ineffectiveDate;
	}

	public void setIneffectiveDate(String ineffectiveDate){
		this.ineffectiveDate = ineffectiveDate;
	}

	public Object getAttribute(String name){
		if("ChangeType".equals(name)){
			return  this.getChangeType();
		}else if("EffectiveType".equals(name)){
			return  this.getEffectiveType();
		}else if("EffectiveDate".equals(name)){
			return  this.getEffectiveDate();
		}else if("IneffectiveDate".equals(name)){
			return  this.getIneffectiveDate();
		}
		return null;
	}

	public void setAttribute(String name,Object value){
		if("ChangeType".equals(name)){
			this.setChangeType((String)value);
		}else if("EffectiveType".equals(name)){
			this.setEffectiveType((String)value);
		}else if("EffectiveDate".equals(name)){
			this.setEffectiveDate((String)value);
		}else if("IneffectiveDate".equals(name)){
			this.setIneffectiveDate((String)value);
		}
	}

	public void cover(MessageBean bean){
		ChngCtrl newBean = (ChngCtrl) bean;

		if( null != newBean.getChangeType() && 0 != newBean.getChangeType().length()){
			changeType = newBean.getChangeType();
		}
		if( null != newBean.getEffectiveType() && 0 != newBean.getEffectiveType().length()){
			effectiveType = newBean.getEffectiveType();
		}
		if( null != newBean.getEffectiveDate() && 0 != newBean.getEffectiveDate().length()){
			effectiveDate = newBean.getEffectiveDate();
		}
		if( null != newBean.getIneffectiveDate() && 0 != newBean.getIneffectiveDate().length()){
			ineffectiveDate = newBean.getIneffectiveDate();
		}
	}

	public void validate(){
		//变更类型非空检查
		if ( null == changeType){
			throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.null", new String[]{"变更类型(changeType)"}));
		}

			//变更类型数据长度检查
			try{
			if ( changeType.getBytes("UTF-8").length> 4 ) {
				throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.maxLength", new String[]{"变更类型(changeType)", "4"}));
			}
			}catch (UnsupportedEncodingException e) {
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("message.encoding.unsupport", new String[]{"ChngCtrl", "UTF-8"}));
			}

		//生效类型非空检查
		if ( null == effectiveType){
			throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.null", new String[]{"生效类型(effectiveType)"}));
		}

			//生效类型数据长度检查
			try{
			if ( effectiveType.getBytes("UTF-8").length> 4 ) {
				throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.maxLength", new String[]{"生效类型(effectiveType)", "4"}));
			}
			}catch (UnsupportedEncodingException e) {
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("message.encoding.unsupport", new String[]{"ChngCtrl", "UTF-8"}));
			}

		//生效日期不为空则按以下规则校验
		if( null != effectiveDate){
			//生效日期数据长度检查
			try{
			if ( effectiveDate.getBytes("UTF-8").length> 10 ) {
				throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.maxLength", new String[]{"生效日期(effectiveDate)", "10"}));
			}
			}catch (UnsupportedEncodingException e) {
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("message.encoding.unsupport", new String[]{"ChngCtrl", "UTF-8"}));
			}

			//生效日期格式检查
			try{
				java.text.DateFormat dateformat = new java.text.SimpleDateFormat("yyyy-mm-dd");
				dateformat.setLenient(false);
				dateformat.parse(effectiveDate);
			} catch (Exception e) {
				throw new RuntimeException(
					MultiLanguageResourceBundle.getInstance().getString("Message.field.pattern.illegal", new String[]{"生效日期(effectiveDate)", "yyyy-mm-dd"}) + e.getMessage(), e);
			}

		}

		//失效日期不为空则按以下规则校验
		if( null != ineffectiveDate){
			//失效日期数据长度检查
			try{
			if ( ineffectiveDate.getBytes("UTF-8").length> 10 ) {
				throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.maxLength", new String[]{"失效日期(ineffectiveDate)", "10"}));
			}
			}catch (UnsupportedEncodingException e) {
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("message.encoding.unsupport", new String[]{"ChngCtrl", "UTF-8"}));
			}

			//失效日期格式检查
			try{
				java.text.DateFormat dateformat = new java.text.SimpleDateFormat("yyyy-mm-dd");
				dateformat.setLenient(false);
				dateformat.parse(ineffectiveDate);
			} catch (Exception e) {
				throw new RuntimeException(
					MultiLanguageResourceBundle.getInstance().getString("Message.field.pattern.illegal", new String[]{"失效日期(ineffectiveDate)", "yyyy-mm-dd"}) + e.getMessage(), e);
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
		if( null != changeType){
			buf.append("<a n=\"changeType\" t=\"str\">");
			buf.append(changeType);
			buf.append("</a>");
		}
		if( null != effectiveType){
			buf.append("<a n=\"effectiveType\" t=\"str\">");
			buf.append(effectiveType);
			buf.append("</a>");
		}
		if( null != effectiveDate){
			buf.append("<a n=\"effectiveDate\" t=\"datetime\">");
			buf.append(effectiveDate);
			buf.append("</a>");
		}
		if( null != ineffectiveDate){
			buf.append("<a n=\"ineffectiveDate\" t=\"datetime\">");
			buf.append(ineffectiveDate);
			buf.append("</a>");
		}
		if( 0 == buf.length()){
			return null;
		}else{
			if ( isTable ){
				buf.insert(0,"<b>");
			}else{
				buf.insert(0,"<b c=\"com.giantstone.cnaps2.messagebean.recv.req.ChngCtrl\">");
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
		if( null != changeType){
			return false;
		}
		if( null != effectiveType){
			return false;
		}
		if( null != effectiveDate){
			return false;
		}
		if( null != ineffectiveDate){
			return false;
		}
		return true;
	}
}
