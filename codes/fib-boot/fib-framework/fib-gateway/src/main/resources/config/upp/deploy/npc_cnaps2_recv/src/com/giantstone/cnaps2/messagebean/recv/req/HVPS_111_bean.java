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
 * HVPS_111_bean
 */
public class HVPS_111_bean extends MessageBean{

	//报文头
	private com.giantstone.cnaps2.messagebean.recv.req.MsgHeader msgHeader = new com.giantstone.cnaps2.messagebean.recv.req.MsgHeader();

	public com.giantstone.cnaps2.messagebean.recv.req.MsgHeader getMsgHeader(){
		return msgHeader;
	}

	public void setMsgHeader(com.giantstone.cnaps2.messagebean.recv.req.MsgHeader msgHeader){
		this.msgHeader = msgHeader;
	}

	//数字签名
	private String digitalSignature;

	public String getDigitalSignature(){
		return digitalSignature;
	}

	public void setDigitalSignature(String digitalSignature){
		this.digitalSignature = digitalSignature;
	}

	//报文体
	private com.giantstone.cnaps2.messagebean.recv.req.HVPS_111_Out hVPS_111_Out = new com.giantstone.cnaps2.messagebean.recv.req.HVPS_111_Out();

	public com.giantstone.cnaps2.messagebean.recv.req.HVPS_111_Out getHVPS_111_Out(){
		return hVPS_111_Out;
	}

	public void setHVPS_111_Out(com.giantstone.cnaps2.messagebean.recv.req.HVPS_111_Out hVPS_111_Out){
		this.hVPS_111_Out = hVPS_111_Out;
	}

	//核押验签结果
	private String checkEscortFlag;

	public String getCheckEscortFlag(){
		return checkEscortFlag;
	}

	public void setCheckEscortFlag(String checkEscortFlag){
		this.checkEscortFlag = checkEscortFlag;
	}

	public Object getAttribute(String name){
		if("MsgHeader".equals(name)){
			return  this.getMsgHeader();
		}else if("DigitalSignature".equals(name)){
			return  this.getDigitalSignature();
		}else if("HVPS_111_Out".equals(name)){
			return  this.getHVPS_111_Out();
		}else if("checkEscortFlag".equals(name)){
			return  this.getCheckEscortFlag();
		}
		return null;
	}

	public void setAttribute(String name,Object value){
		if("MsgHeader".equals(name)){
			this.setMsgHeader((com.giantstone.cnaps2.messagebean.recv.req.MsgHeader)value);
		}else if("DigitalSignature".equals(name)){
			this.setDigitalSignature((String)value);
		}else if("HVPS_111_Out".equals(name)){
			this.setHVPS_111_Out((com.giantstone.cnaps2.messagebean.recv.req.HVPS_111_Out)value);
		}else if("checkEscortFlag".equals(name)){
			this.setCheckEscortFlag((String)value);
		}
	}

	public void cover(MessageBean bean){
		HVPS_111_bean newBean = (HVPS_111_bean) bean;

		if( null != msgHeader&&null != newBean.getMsgHeader()){
			msgHeader.cover(newBean.getMsgHeader());
		}
		if( null != newBean.getDigitalSignature() && 0 != newBean.getDigitalSignature().length()){
			digitalSignature = newBean.getDigitalSignature();
		}
		if( null != hVPS_111_Out&&null != newBean.getHVPS_111_Out()){
			hVPS_111_Out.cover(newBean.getHVPS_111_Out());
		}
		if( null != newBean.getCheckEscortFlag() && 0 != newBean.getCheckEscortFlag().length()){
			checkEscortFlag = newBean.getCheckEscortFlag();
		}
	}

	public void validate(){
		//报文头正确性检查
		if ( null != msgHeader && !msgHeader.isNull() ){ 
			msgHeader.validate();
		}

		//数字签名不为空则按以下规则校验
		if( null != digitalSignature){
			//数字签名数据长度检查
			try{
			if ( digitalSignature.getBytes("UTF-8").length> 2048 ) {
				throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.maxLength", new String[]{"数字签名(digitalSignature)", "2048"}));
			}
			}catch (UnsupportedEncodingException e) {
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("message.encoding.unsupport", new String[]{"HVPS_111_bean", "UTF-8"}));
			}

		}

		//报文体正确性检查
		if ( null != hVPS_111_Out && !hVPS_111_Out.isNull() ){ 
			hVPS_111_Out.validate();
		}

		//核押验签结果不为空则按以下规则校验
		if( null != checkEscortFlag){
			//核押验签结果数据长度检查
			try{
			if ( checkEscortFlag.getBytes("UTF-8").length> 12 ) {
				throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.maxLength", new String[]{"核押验签结果(checkEscortFlag)", "12"}));
			}
			}catch (UnsupportedEncodingException e) {
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("message.encoding.unsupport", new String[]{"HVPS_111_bean", "UTF-8"}));
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
		if( null != msgHeader){
			str = msgHeader.toString(true);
			if( null != str){
				buf.append("<a n=\"msgHeader\" t=\"bean\">");
				buf.append(str);
				buf.append("</a>");
			}
		}
		if( null != digitalSignature){
			buf.append("<a n=\"digitalSignature\" t=\"str\">");
			buf.append(digitalSignature);
			buf.append("</a>");
		}
		if( null != hVPS_111_Out){
			str = hVPS_111_Out.toString(true);
			if( null != str){
				buf.append("<a n=\"hVPS_111_Out\" t=\"bean\">");
				buf.append(str);
				buf.append("</a>");
			}
		}
		if( null != checkEscortFlag){
			buf.append("<a n=\"checkEscortFlag\" t=\"str\">");
			buf.append(checkEscortFlag);
			buf.append("</a>");
		}
		if( 0 == buf.length()){
			return null;
		}else{
			if ( isTable ){
				buf.insert(0,"<b>");
			}else{
				buf.insert(0,"<b c=\"com.giantstone.cnaps2.messagebean.recv.req.HVPS_111_bean\">");
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
		if( null != msgHeader){
			if (  !msgHeader.isNull() ){
				return false;
			}
		}
		if( null != digitalSignature){
			return false;
		}
		if( null != hVPS_111_Out){
			if (  !hVPS_111_Out.isNull() ){
				return false;
			}
		}
		if( null != checkEscortFlag){
			return false;
		}
		return true;
	}
}
