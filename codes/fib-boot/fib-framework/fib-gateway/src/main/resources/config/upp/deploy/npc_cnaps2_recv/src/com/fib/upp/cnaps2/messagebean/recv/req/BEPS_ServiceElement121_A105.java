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
 * 退汇业务
 */
public class BEPS_ServiceElement121_A105 extends MessageBean{

	//原报文信息
	private com.giantstone.cnaps2.messagebean.recv.req.OrgnlGrpHdr orgnlGrpHdr = new com.giantstone.cnaps2.messagebean.recv.req.OrgnlGrpHdr();

	public com.giantstone.cnaps2.messagebean.recv.req.OrgnlGrpHdr getOrgnlGrpHdr(){
		return orgnlGrpHdr;
	}

	public void setOrgnlGrpHdr(com.giantstone.cnaps2.messagebean.recv.req.OrgnlGrpHdr orgnlGrpHdr){
		this.orgnlGrpHdr = orgnlGrpHdr;
	}

	//原明细业务信息
	private com.giantstone.cnaps2.messagebean.recv.req.OrgnlTx orgnlTx = new com.giantstone.cnaps2.messagebean.recv.req.OrgnlTx();

	public com.giantstone.cnaps2.messagebean.recv.req.OrgnlTx getOrgnlTx(){
		return orgnlTx;
	}

	public void setOrgnlTx(com.giantstone.cnaps2.messagebean.recv.req.OrgnlTx orgnlTx){
		this.orgnlTx = orgnlTx;
	}

	//退汇原因
	private String content;

	public String getContent(){
		return content;
	}

	public void setContent(String content){
		this.content = content;
	}

	public Object getAttribute(String name){
		if("OrgnlGrpHdr".equals(name)){
			return  this.getOrgnlGrpHdr();
		}else if("OrgnlTx".equals(name)){
			return  this.getOrgnlTx();
		}else if("content".equals(name)){
			return  this.getContent();
		}
		return null;
	}

	public void setAttribute(String name,Object value){
		if("OrgnlGrpHdr".equals(name)){
			this.setOrgnlGrpHdr((com.giantstone.cnaps2.messagebean.recv.req.OrgnlGrpHdr)value);
		}else if("OrgnlTx".equals(name)){
			this.setOrgnlTx((com.giantstone.cnaps2.messagebean.recv.req.OrgnlTx)value);
		}else if("content".equals(name)){
			this.setContent((String)value);
		}
	}

	public void cover(MessageBean bean){
		BEPS_ServiceElement121_A105 newBean = (BEPS_ServiceElement121_A105) bean;

		if( null != orgnlGrpHdr&&null != newBean.getOrgnlGrpHdr()){
			orgnlGrpHdr.cover(newBean.getOrgnlGrpHdr());
		}
		if( null != orgnlTx&&null != newBean.getOrgnlTx()){
			orgnlTx.cover(newBean.getOrgnlTx());
		}
		if( null != newBean.getContent() && 0 != newBean.getContent().length()){
			content = newBean.getContent();
		}
	}

	public void validate(){
		//原报文信息非空检查
		if ( null == orgnlGrpHdr || orgnlGrpHdr.isNull() ){
			throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.null", new String[]{"原报文信息(orgnlGrpHdr)"}));
		}

		//原报文信息正确性检查
			orgnlGrpHdr.validate();


		//原明细业务信息非空检查
		if ( null == orgnlTx || orgnlTx.isNull() ){
			throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.null", new String[]{"原明细业务信息(orgnlTx)"}));
		}

		//原明细业务信息正确性检查
			orgnlTx.validate();


		//退汇原因非空检查
		if ( null == content){
			throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.null", new String[]{"退汇原因(content)"}));
		}

			//退汇原因数据长度检查
			try{
			if ( content.getBytes("UTF-8").length> 256 ) {
				throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.maxLength", new String[]{"退汇原因(content)", "256"}));
			}
			}catch (UnsupportedEncodingException e) {
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("message.encoding.unsupport", new String[]{"BEPS_ServiceElement121_A105", "UTF-8"}));
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
		if( null != orgnlGrpHdr){
			str = orgnlGrpHdr.toString(true);
			if( null != str){
				buf.append("<a n=\"orgnlGrpHdr\" t=\"bean\">");
				buf.append(str);
				buf.append("</a>");
			}
		}
		if( null != orgnlTx){
			str = orgnlTx.toString(true);
			if( null != str){
				buf.append("<a n=\"orgnlTx\" t=\"bean\">");
				buf.append(str);
				buf.append("</a>");
			}
		}
		if( null != content){
			buf.append("<a n=\"content\" t=\"str\">");
			buf.append(content);
			buf.append("</a>");
		}
		if( 0 == buf.length()){
			return null;
		}else{
			if ( isTable ){
				buf.insert(0,"<b>");
			}else{
				buf.insert(0,"<b c=\"com.fib.upp.cnaps2.messagebean.recv.req.BEPS_ServiceElement121_A105\">");
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
		if( null != orgnlGrpHdr){
			if (  !orgnlGrpHdr.isNull() ){
				return false;
			}
		}
		if( null != orgnlTx){
			if (  !orgnlTx.isNull() ){
				return false;
			}
		}
		if( null != content){
			return false;
		}
		return true;
	}
}
