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
 * 业务应答信息组件
 */
public class RspnInf extends MessageBean{

	//业务状态
	private String status;

	public String getStatus(){
		return status;
	}

	public void setStatus(String status){
		this.status = status;
	}

	//业务拒绝处理码
	private String rejectCode;

	public String getRejectCode(){
		return rejectCode;
	}

	public void setRejectCode(String rejectCode){
		this.rejectCode = rejectCode;
	}

	//业务拒绝信息
	private String rejectInformation;

	public String getRejectInformation(){
		return rejectInformation;
	}

	public void setRejectInformation(String rejectInformation){
		this.rejectInformation = rejectInformation;
	}

	//业务处理参与机构
	private String processParty;

	public String getProcessParty(){
		return processParty;
	}

	public void setProcessParty(String processParty){
		this.processParty = processParty;
	}

	public Object getAttribute(String name){
		if("Status".equals(name)){
			return  this.getStatus();
		}else if("RejectCode".equals(name)){
			return  this.getRejectCode();
		}else if("RejectInformation".equals(name)){
			return  this.getRejectInformation();
		}else if("ProcessParty".equals(name)){
			return  this.getProcessParty();
		}
		return null;
	}

	public void setAttribute(String name,Object value){
		if("Status".equals(name)){
			this.setStatus((String)value);
		}else if("RejectCode".equals(name)){
			this.setRejectCode((String)value);
		}else if("RejectInformation".equals(name)){
			this.setRejectInformation((String)value);
		}else if("ProcessParty".equals(name)){
			this.setProcessParty((String)value);
		}
	}

	public void cover(MessageBean bean){
		RspnInf newBean = (RspnInf) bean;

		if( null != newBean.getStatus() && 0 != newBean.getStatus().length()){
			status = newBean.getStatus();
		}
		if( null != newBean.getRejectCode() && 0 != newBean.getRejectCode().length()){
			rejectCode = newBean.getRejectCode();
		}
		if( null != newBean.getRejectInformation() && 0 != newBean.getRejectInformation().length()){
			rejectInformation = newBean.getRejectInformation();
		}
		if( null != newBean.getProcessParty() && 0 != newBean.getProcessParty().length()){
			processParty = newBean.getProcessParty();
		}
	}

	public void validate(){
		//业务状态非空检查
		if ( null == status){
			throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.null", new String[]{"业务状态(status)"}));
		}

			//业务状态数据长度检查
			try{
			if ( status.getBytes("UTF-8").length> 4 ) {
				throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.maxLength", new String[]{"业务状态(status)", "4"}));
			}
			}catch (UnsupportedEncodingException e) {
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("message.encoding.unsupport", new String[]{"RspnInf", "UTF-8"}));
			}

		//业务拒绝处理码不为空则按以下规则校验
		if( null != rejectCode){
			//业务拒绝处理码数据长度检查
			try{
			if ( rejectCode.getBytes("UTF-8").length> 4 ) {
				throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.maxLength", new String[]{"业务拒绝处理码(rejectCode)", "4"}));
			}
			}catch (UnsupportedEncodingException e) {
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("message.encoding.unsupport", new String[]{"RspnInf", "UTF-8"}));
			}

		}

		//业务拒绝信息不为空则按以下规则校验
		if( null != rejectInformation){
			//业务拒绝信息数据长度检查
			try{
			if ( rejectInformation.getBytes("UTF-8").length> 105 ) {
				throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.maxLength", new String[]{"业务拒绝信息(rejectInformation)", "105"}));
			}
			}catch (UnsupportedEncodingException e) {
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("message.encoding.unsupport", new String[]{"RspnInf", "UTF-8"}));
			}

		}

		//业务处理参与机构不为空则按以下规则校验
		if( null != processParty){
			//业务处理参与机构数据长度检查
			try{
			if ( processParty.getBytes("UTF-8").length> 14 ) {
				throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.maxLength", new String[]{"业务处理参与机构(processParty)", "14"}));
			}
			}catch (UnsupportedEncodingException e) {
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("message.encoding.unsupport", new String[]{"RspnInf", "UTF-8"}));
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
		if( null != status){
			buf.append("<a n=\"status\" t=\"str\">");
			buf.append(status);
			buf.append("</a>");
		}
		if( null != rejectCode){
			buf.append("<a n=\"rejectCode\" t=\"str\">");
			buf.append(rejectCode);
			buf.append("</a>");
		}
		if( null != rejectInformation){
			buf.append("<a n=\"rejectInformation\" t=\"str\">");
			buf.append(rejectInformation);
			buf.append("</a>");
		}
		if( null != processParty){
			buf.append("<a n=\"processParty\" t=\"str\">");
			buf.append(processParty);
			buf.append("</a>");
		}
		if( 0 == buf.length()){
			return null;
		}else{
			if ( isTable ){
				buf.insert(0,"<b>");
			}else{
				buf.insert(0,"<b c=\"com.giantstone.cnaps2.messagebean.recv.req.RspnInf\">");
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
		if( null != status){
			return false;
		}
		if( null != rejectCode){
			return false;
		}
		if( null != rejectInformation){
			return false;
		}
		if( null != processParty){
			return false;
		}
		return true;
	}
}
