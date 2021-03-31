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
 * BEPS_121_bean
 */
public class BEPS_121_bean extends MessageBean{

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
	private com.fib.upp.cnaps2.messagebean.recv.req.BEPS_121_Out bEPS_121_Out = new com.fib.upp.cnaps2.messagebean.recv.req.BEPS_121_Out();

	public com.fib.upp.cnaps2.messagebean.recv.req.BEPS_121_Out getBEPS_121_Out(){
		return bEPS_121_Out;
	}

	public void setBEPS_121_Out(com.fib.upp.cnaps2.messagebean.recv.req.BEPS_121_Out bEPS_121_Out){
		this.bEPS_121_Out = bEPS_121_Out;
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
		}else if("BEPS_121_Out".equals(name)){
			return  this.getBEPS_121_Out();
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
		}else if("BEPS_121_Out".equals(name)){
			this.setBEPS_121_Out((com.fib.upp.cnaps2.messagebean.recv.req.BEPS_121_Out)value);
		}else if("checkEscortFlag".equals(name)){
			this.setCheckEscortFlag((String)value);
		}
	}

	public void cover(MessageBean bean){
		BEPS_121_bean newBean = (BEPS_121_bean) bean;

		if( null != msgHeader&&null != newBean.getMsgHeader()){
			msgHeader.cover(newBean.getMsgHeader());
		}
		if( null != newBean.getDigitalSignature() && 0 != newBean.getDigitalSignature().length()){
			digitalSignature = newBean.getDigitalSignature();
		}
		if( null != bEPS_121_Out&&null != newBean.getBEPS_121_Out()){
			bEPS_121_Out.cover(newBean.getBEPS_121_Out());
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
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("message.encoding.unsupport", new String[]{"BEPS_121_bean", "UTF-8"}));
			}

			//数字签名输入类型检查
			if(!CodeUtil.isNumeric(digitalSignature)){
				throw new RuntimeException(
					MultiLanguageResourceBundle.getInstance().getString("Message.parameter.type.mustNum", new String[]{"数字签名(digitalSignature)"}));
			}

		}

		//报文体正确性检查
		if ( null != bEPS_121_Out && !bEPS_121_Out.isNull() ){ 
			bEPS_121_Out.validate();
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
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("message.encoding.unsupport", new String[]{"BEPS_121_bean", "UTF-8"}));
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
			buf.append("<a n=\"digitalSignature\" t=\"num\">");
			buf.append(digitalSignature);
			buf.append("</a>");
		}
		if( null != bEPS_121_Out){
			str = bEPS_121_Out.toString(true);
			if( null != str){
				buf.append("<a n=\"bEPS_121_Out\" t=\"bean\">");
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
				buf.insert(0,"<b c=\"com.fib.upp.cnaps2.messagebean.recv.req.BEPS_121_bean\">");
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
		if( null != bEPS_121_Out){
			if (  !bEPS_121_Out.isNull() ){
				return false;
			}
		}
		if( null != checkEscortFlag){
			return false;
		}
		return true;
	}
}
