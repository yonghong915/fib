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
 * 批量包组头组件
 */
public class PKGGrpHdr extends MessageBean{

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

	//接收直接参与机构
	private String instructedDirectParty;

	public String getInstructedDirectParty(){
		return instructedDirectParty;
	}

	public void setInstructedDirectParty(String instructedDirectParty){
		this.instructedDirectParty = instructedDirectParty;
	}

	//明细业务总笔数
	private String numberOfTransactions;

	public String getNumberOfTransactions(){
		return numberOfTransactions;
	}

	public void setNumberOfTransactions(String numberOfTransactions){
		this.numberOfTransactions = numberOfTransactions;
	}

	//币种
	private String currencyType;

	public String getCurrencyType(){
		return currencyType;
	}

	public void setCurrencyType(String currencyType){
		this.currencyType = currencyType;
	}

	//明细业务总金额
	private String controlSum;

	public String getControlSum(){
		return controlSum;
	}

	public void setControlSum(String controlSum){
		this.controlSum = controlSum;
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
		}else if("InstructedDirectParty".equals(name)){
			return  this.getInstructedDirectParty();
		}else if("NumberOfTransactions".equals(name)){
			return  this.getNumberOfTransactions();
		}else if("CurrencyType".equals(name)){
			return  this.getCurrencyType();
		}else if("ControlSum".equals(name)){
			return  this.getControlSum();
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
		}else if("InstructedDirectParty".equals(name)){
			this.setInstructedDirectParty((String)value);
		}else if("NumberOfTransactions".equals(name)){
			this.setNumberOfTransactions((String)value);
		}else if("CurrencyType".equals(name)){
			this.setCurrencyType((String)value);
		}else if("ControlSum".equals(name)){
			this.setControlSum((String)value);
		}else if("SystemCode".equals(name)){
			this.setSystemCode((String)value);
		}else if("Remark".equals(name)){
			this.setRemark((String)value);
		}
	}

	public void cover(MessageBean bean){
		PKGGrpHdr newBean = (PKGGrpHdr) bean;

		if( null != newBean.getMessageIdentification() && 0 != newBean.getMessageIdentification().length()){
			messageIdentification = newBean.getMessageIdentification();
		}
		if( null != newBean.getCreationDateTime() && 0 != newBean.getCreationDateTime().length()){
			creationDateTime = newBean.getCreationDateTime();
		}
		if( null != newBean.getInstructingDirectParty() && 0 != newBean.getInstructingDirectParty().length()){
			instructingDirectParty = newBean.getInstructingDirectParty();
		}
		if( null != newBean.getInstructedDirectParty() && 0 != newBean.getInstructedDirectParty().length()){
			instructedDirectParty = newBean.getInstructedDirectParty();
		}
		if( null != newBean.getNumberOfTransactions() && 0 != newBean.getNumberOfTransactions().length()){
			numberOfTransactions = newBean.getNumberOfTransactions();
		}
		if( null != newBean.getCurrencyType() && 0 != newBean.getCurrencyType().length()){
			currencyType = newBean.getCurrencyType();
		}
		if( null != newBean.getControlSum() && 0 != newBean.getControlSum().length()){
			controlSum = newBean.getControlSum();
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
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("message.encoding.unsupport", new String[]{"PKGGrpHdr", "UTF-8"}));
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
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("message.encoding.unsupport", new String[]{"PKGGrpHdr", "UTF-8"}));
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
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("message.encoding.unsupport", new String[]{"PKGGrpHdr", "UTF-8"}));
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
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("message.encoding.unsupport", new String[]{"PKGGrpHdr", "UTF-8"}));
			}

		}

		//明细业务总笔数不为空则按以下规则校验
		if( null != numberOfTransactions){
			//明细业务总笔数数据长度检查
			try{
			if ( numberOfTransactions.getBytes("UTF-8").length> 15 ) {
				throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.maxLength", new String[]{"明细业务总笔数(numberOfTransactions)", "15"}));
			}
			}catch (UnsupportedEncodingException e) {
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("message.encoding.unsupport", new String[]{"PKGGrpHdr", "UTF-8"}));
			}

		}

		//币种不为空则按以下规则校验
		if( null != currencyType){
			//币种数据长度检查
			try{
			if ( currencyType.getBytes("UTF-8").length> 5 ) {
				throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.maxLength", new String[]{"币种(currencyType)", "5"}));
			}
			}catch (UnsupportedEncodingException e) {
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("message.encoding.unsupport", new String[]{"PKGGrpHdr", "UTF-8"}));
			}

		}

		//明细业务总金额不为空则按以下规则校验
		if( null != controlSum){
			//明细业务总金额数据长度检查
			try{
			if ( controlSum.getBytes("UTF-8").length> 18 ) {
				throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.maxLength", new String[]{"明细业务总金额(controlSum)", "18"}));
			}
			}catch (UnsupportedEncodingException e) {
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("message.encoding.unsupport", new String[]{"PKGGrpHdr", "UTF-8"}));
			}

		}

		//系统编号不为空则按以下规则校验
		if( null != systemCode){
			//系统编号数据长度检查
			try{
			if ( systemCode.getBytes("UTF-8").length> 15 ) {
				throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.maxLength", new String[]{"系统编号(systemCode)", "15"}));
			}
			}catch (UnsupportedEncodingException e) {
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("message.encoding.unsupport", new String[]{"PKGGrpHdr", "UTF-8"}));
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
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("message.encoding.unsupport", new String[]{"PKGGrpHdr", "UTF-8"}));
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
		if( null != instructedDirectParty){
			buf.append("<a n=\"instructedDirectParty\" t=\"str\">");
			buf.append(instructedDirectParty);
			buf.append("</a>");
		}
		if( null != numberOfTransactions){
			buf.append("<a n=\"numberOfTransactions\" t=\"str\">");
			buf.append(numberOfTransactions);
			buf.append("</a>");
		}
		if( null != currencyType){
			buf.append("<a n=\"currencyType\" t=\"str\">");
			buf.append(currencyType);
			buf.append("</a>");
		}
		if( null != controlSum){
			buf.append("<a n=\"controlSum\" t=\"str\">");
			buf.append(controlSum);
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
				buf.insert(0,"<b c=\"com.fib.upp.cnaps2.messagebean.recv.req.PKGGrpHdr\">");
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
		if( null != instructedDirectParty){
			return false;
		}
		if( null != numberOfTransactions){
			return false;
		}
		if( null != currencyType){
			return false;
		}
		if( null != controlSum){
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
