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
 * 业务头组件
 */
public class GrpHdr extends MessageBean{

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

	//发起直接参与机构
	private String instructingDirectParty;

	public String getInstructingDirectParty(){
		return instructingDirectParty;
	}

	public void setInstructingDirectParty(String instructingDirectParty){
		this.instructingDirectParty = instructingDirectParty;
	}

	//发起参与机构
	private String instructingParty;

	public String getInstructingParty(){
		return instructingParty;
	}

	public void setInstructingParty(String instructingParty){
		this.instructingParty = instructingParty;
	}

	//接收直接参与机构
	private String instructedDirectParty;

	public String getInstructedDirectParty(){
		return instructedDirectParty;
	}

	public void setInstructedDirectParty(String instructedDirectParty){
		this.instructedDirectParty = instructedDirectParty;
	}

	//接收参与机构
	private String instructedParty;

	public String getInstructedParty(){
		return instructedParty;
	}

	public void setInstructedParty(String instructedParty){
		this.instructedParty = instructedParty;
	}

	//系统编号
	private String systemCode;

	public String getSystemCode(){
		return systemCode;
	}

	public void setSystemCode(String systemCode){
		this.systemCode = systemCode;
	}

	//备注
	private String remark;

	public String getRemark(){
		return remark;
	}

	public void setRemark(String remark){
		this.remark = remark;
	}

	public Object getAttribute(String name){
		if("MessageIdentification".equals(name)){
			return  this.getMessageIdentification();
		}else if("CreationDateTime".equals(name)){
			return  this.getCreationDateTime();
		}else if("InstructingDirectParty".equals(name)){
			return  this.getInstructingDirectParty();
		}else if("InstructingParty".equals(name)){
			return  this.getInstructingParty();
		}else if("InstructedDirectParty".equals(name)){
			return  this.getInstructedDirectParty();
		}else if("InstructedParty".equals(name)){
			return  this.getInstructedParty();
		}else if("SystemCode".equals(name)){
			return  this.getSystemCode();
		}else if("Remark".equals(name)){
			return  this.getRemark();
		}
		return null;
	}

	public void setAttribute(String name,Object value){
		if("MessageIdentification".equals(name)){
			this.setMessageIdentification((String)value);
		}else if("CreationDateTime".equals(name)){
			this.setCreationDateTime((String)value);
		}else if("InstructingDirectParty".equals(name)){
			this.setInstructingDirectParty((String)value);
		}else if("InstructingParty".equals(name)){
			this.setInstructingParty((String)value);
		}else if("InstructedDirectParty".equals(name)){
			this.setInstructedDirectParty((String)value);
		}else if("InstructedParty".equals(name)){
			this.setInstructedParty((String)value);
		}else if("SystemCode".equals(name)){
			this.setSystemCode((String)value);
		}else if("Remark".equals(name)){
			this.setRemark((String)value);
		}
	}

	public void cover(MessageBean bean){
		GrpHdr newBean = (GrpHdr) bean;

		if( null != newBean.getMessageIdentification() && 0 != newBean.getMessageIdentification().length()){
			messageIdentification = newBean.getMessageIdentification();
		}
		if( null != newBean.getCreationDateTime() && 0 != newBean.getCreationDateTime().length()){
			creationDateTime = newBean.getCreationDateTime();
		}
		if( null != newBean.getInstructingDirectParty() && 0 != newBean.getInstructingDirectParty().length()){
			instructingDirectParty = newBean.getInstructingDirectParty();
		}
		if( null != newBean.getInstructingParty() && 0 != newBean.getInstructingParty().length()){
			instructingParty = newBean.getInstructingParty();
		}
		if( null != newBean.getInstructedDirectParty() && 0 != newBean.getInstructedDirectParty().length()){
			instructedDirectParty = newBean.getInstructedDirectParty();
		}
		if( null != newBean.getInstructedParty() && 0 != newBean.getInstructedParty().length()){
			instructedParty = newBean.getInstructedParty();
		}
		if( null != newBean.getSystemCode() && 0 != newBean.getSystemCode().length()){
			systemCode = newBean.getSystemCode();
		}
		if( null != newBean.getRemark() && 0 != newBean.getRemark().length()){
			remark = newBean.getRemark();
		}
	}

	public void validate(){
		//报文标识号不为空则按以下规则校验
		if( null != messageIdentification){
			//报文标识号数据长度检查
			try{
			if ( messageIdentification.getBytes("UTF-8").length> 35 ) {
				throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.maxLength", new String[]{"报文标识号(messageIdentification)", "35"}));
			}
			}catch (UnsupportedEncodingException e) {
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("message.encoding.unsupport", new String[]{"GrpHdr", "UTF-8"}));
			}

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
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("message.encoding.unsupport", new String[]{"GrpHdr", "UTF-8"}));
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

		//发起直接参与机构不为空则按以下规则校验
		if( null != instructingDirectParty){
			//发起直接参与机构数据长度检查
			try{
			if ( instructingDirectParty.getBytes("UTF-8").length> 14 ) {
				throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.maxLength", new String[]{"发起直接参与机构(instructingDirectParty)", "14"}));
			}
			}catch (UnsupportedEncodingException e) {
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("message.encoding.unsupport", new String[]{"GrpHdr", "UTF-8"}));
			}

		}

		//发起参与机构不为空则按以下规则校验
		if( null != instructingParty){
			//发起参与机构数据长度检查
			try{
			if ( instructingParty.getBytes("UTF-8").length> 14 ) {
				throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.maxLength", new String[]{"发起参与机构(instructingParty)", "14"}));
			}
			}catch (UnsupportedEncodingException e) {
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("message.encoding.unsupport", new String[]{"GrpHdr", "UTF-8"}));
			}

		}

		//接收直接参与机构不为空则按以下规则校验
		if( null != instructedDirectParty){
			//接收直接参与机构数据长度检查
			try{
			if ( instructedDirectParty.getBytes("UTF-8").length> 14 ) {
				throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.maxLength", new String[]{"接收直接参与机构(instructedDirectParty)", "14"}));
			}
			}catch (UnsupportedEncodingException e) {
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("message.encoding.unsupport", new String[]{"GrpHdr", "UTF-8"}));
			}

		}

		//接收参与机构不为空则按以下规则校验
		if( null != instructedParty){
			//接收参与机构数据长度检查
			try{
			if ( instructedParty.getBytes("UTF-8").length> 14 ) {
				throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.maxLength", new String[]{"接收参与机构(instructedParty)", "14"}));
			}
			}catch (UnsupportedEncodingException e) {
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("message.encoding.unsupport", new String[]{"GrpHdr", "UTF-8"}));
			}

		}

		//系统编号不为空则按以下规则校验
		if( null != systemCode){
			//系统编号数据长度检查
			try{
			if ( systemCode.getBytes("UTF-8").length> 4 ) {
				throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.maxLength", new String[]{"系统编号(systemCode)", "4"}));
			}
			}catch (UnsupportedEncodingException e) {
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("message.encoding.unsupport", new String[]{"GrpHdr", "UTF-8"}));
			}

		}

		//备注不为空则按以下规则校验
		if( null != remark){
			//备注数据长度检查
			try{
			if ( remark.getBytes("UTF-8").length> 256 ) {
				throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.maxLength", new String[]{"备注(remark)", "256"}));
			}
			}catch (UnsupportedEncodingException e) {
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("message.encoding.unsupport", new String[]{"GrpHdr", "UTF-8"}));
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
		if( null != instructingDirectParty){
			buf.append("<a n=\"instructingDirectParty\" t=\"str\">");
			buf.append(instructingDirectParty);
			buf.append("</a>");
		}
		if( null != instructingParty){
			buf.append("<a n=\"instructingParty\" t=\"str\">");
			buf.append(instructingParty);
			buf.append("</a>");
		}
		if( null != instructedDirectParty){
			buf.append("<a n=\"instructedDirectParty\" t=\"str\">");
			buf.append(instructedDirectParty);
			buf.append("</a>");
		}
		if( null != instructedParty){
			buf.append("<a n=\"instructedParty\" t=\"str\">");
			buf.append(instructedParty);
			buf.append("</a>");
		}
		if( null != systemCode){
			buf.append("<a n=\"systemCode\" t=\"str\">");
			buf.append(systemCode);
			buf.append("</a>");
		}
		if( null != remark){
			buf.append("<a n=\"remark\" t=\"str\">");
			buf.append(remark);
			buf.append("</a>");
		}
		if( 0 == buf.length()){
			return null;
		}else{
			if ( isTable ){
				buf.insert(0,"<b>");
			}else{
				buf.insert(0,"<b c=\"com.giantstone.cnaps2.messagebean.recv.req.GrpHdr\">");
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
		if( null != instructingDirectParty){
			return false;
		}
		if( null != instructingParty){
			return false;
		}
		if( null != instructedDirectParty){
			return false;
		}
		if( null != instructedParty){
			return false;
		}
		if( null != systemCode){
			return false;
		}
		if( null != remark){
			return false;
		}
		return true;
	}
}
