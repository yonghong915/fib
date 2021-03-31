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
 * 国库资金信息
 */
public class FslInf extends MessageBean{

	//信息流水号
	private String flowNumber;

	public String getFlowNumber(){
		return flowNumber;
	}

	public void setFlowNumber(String flowNumber){
		this.flowNumber = flowNumber;
	}

	//币种
	private String currencyType;

	public String getCurrencyType(){
		return currencyType;
	}

	public void setCurrencyType(String currencyType){
		this.currencyType = currencyType;
	}

	//明细汇总金额
	private String amount;

	public String getAmount(){
		return amount;
	}

	public void setAmount(String amount){
		this.amount = amount;
	}

	//上报国库代码
	private String reportCode;

	public String getReportCode(){
		return reportCode;
	}

	public void setReportCode(String reportCode){
		this.reportCode = reportCode;
	}

	//接收国库代码
	private String receiveCode;

	public String getReceiveCode(){
		return receiveCode;
	}

	public void setReceiveCode(String receiveCode){
		this.receiveCode = receiveCode;
	}

	//报表日期
	private String reportForms;

	public String getReportForms(){
		return reportForms;
	}

	public void setReportForms(String reportForms){
		this.reportForms = reportForms;
	}

	//报表序号
	private String reportNumber;

	public String getReportNumber(){
		return reportNumber;
	}

	public void setReportNumber(String reportNumber){
		this.reportNumber = reportNumber;
	}

	public Object getAttribute(String name){
		if("FlowNumber".equals(name)){
			return  this.getFlowNumber();
		}else if("CurrencyType".equals(name)){
			return  this.getCurrencyType();
		}else if("Amount".equals(name)){
			return  this.getAmount();
		}else if("ReportCode".equals(name)){
			return  this.getReportCode();
		}else if("ReceiveCode".equals(name)){
			return  this.getReceiveCode();
		}else if("ReportForms".equals(name)){
			return  this.getReportForms();
		}else if("ReportNumber".equals(name)){
			return  this.getReportNumber();
		}
		return null;
	}

	public void setAttribute(String name,Object value){
		if("FlowNumber".equals(name)){
			this.setFlowNumber((String)value);
		}else if("CurrencyType".equals(name)){
			this.setCurrencyType((String)value);
		}else if("Amount".equals(name)){
			this.setAmount((String)value);
		}else if("ReportCode".equals(name)){
			this.setReportCode((String)value);
		}else if("ReceiveCode".equals(name)){
			this.setReceiveCode((String)value);
		}else if("ReportForms".equals(name)){
			this.setReportForms((String)value);
		}else if("ReportNumber".equals(name)){
			this.setReportNumber((String)value);
		}
	}

	public void cover(MessageBean bean){
		FslInf newBean = (FslInf) bean;

		if( null != newBean.getFlowNumber() && 0 != newBean.getFlowNumber().length()){
			flowNumber = newBean.getFlowNumber();
		}
		if( null != newBean.getCurrencyType() && 0 != newBean.getCurrencyType().length()){
			currencyType = newBean.getCurrencyType();
		}
		if( null != newBean.getAmount() && 0 != newBean.getAmount().length()){
			amount = newBean.getAmount();
		}
		if( null != newBean.getReportCode() && 0 != newBean.getReportCode().length()){
			reportCode = newBean.getReportCode();
		}
		if( null != newBean.getReceiveCode() && 0 != newBean.getReceiveCode().length()){
			receiveCode = newBean.getReceiveCode();
		}
		if( null != newBean.getReportForms() && 0 != newBean.getReportForms().length()){
			reportForms = newBean.getReportForms();
		}
		if( null != newBean.getReportNumber() && 0 != newBean.getReportNumber().length()){
			reportNumber = newBean.getReportNumber();
		}
	}

	public void validate(){
		//信息流水号不为空则按以下规则校验
		if( null != flowNumber){
			//信息流水号数据长度检查
			try{
			if ( flowNumber.getBytes("UTF-8").length> 20 ) {
				throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.maxLength", new String[]{"信息流水号(flowNumber)", "20"}));
			}
			}catch (UnsupportedEncodingException e) {
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("message.encoding.unsupport", new String[]{"FslInf", "UTF-8"}));
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
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("message.encoding.unsupport", new String[]{"FslInf", "UTF-8"}));
			}

		}

		//明细汇总金额不为空则按以下规则校验
		if( null != amount){
			//明细汇总金额数据长度检查
			try{
			if ( amount.getBytes("UTF-8").length> 19 ) {
				throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.maxLength", new String[]{"明细汇总金额(amount)", "19"}));
			}
			}catch (UnsupportedEncodingException e) {
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("message.encoding.unsupport", new String[]{"FslInf", "UTF-8"}));
			}

		}

		//上报国库代码不为空则按以下规则校验
		if( null != reportCode){
			//上报国库代码数据长度检查
			try{
			if ( reportCode.getBytes("UTF-8").length> 10 ) {
				throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.maxLength", new String[]{"上报国库代码(reportCode)", "10"}));
			}
			}catch (UnsupportedEncodingException e) {
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("message.encoding.unsupport", new String[]{"FslInf", "UTF-8"}));
			}

		}

		//接收国库代码不为空则按以下规则校验
		if( null != receiveCode){
			//接收国库代码数据长度检查
			try{
			if ( receiveCode.getBytes("UTF-8").length> 10 ) {
				throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.maxLength", new String[]{"接收国库代码(receiveCode)", "10"}));
			}
			}catch (UnsupportedEncodingException e) {
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("message.encoding.unsupport", new String[]{"FslInf", "UTF-8"}));
			}

		}

		//报表日期不为空则按以下规则校验
		if( null != reportForms){
			//报表日期数据长度检查
			try{
			if ( reportForms.getBytes("UTF-8").length> 10 ) {
				throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.maxLength", new String[]{"报表日期(reportForms)", "10"}));
			}
			}catch (UnsupportedEncodingException e) {
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("message.encoding.unsupport", new String[]{"FslInf", "UTF-8"}));
			}

			//报表日期格式检查
			try{
				java.text.DateFormat dateformat = new java.text.SimpleDateFormat("yyyy-mm-dd");
				dateformat.setLenient(false);
				dateformat.parse(reportForms);
			} catch (Exception e) {
				throw new RuntimeException(
					MultiLanguageResourceBundle.getInstance().getString("Message.field.pattern.illegal", new String[]{"报表日期(reportForms)", "yyyy-mm-dd"}) + e.getMessage(), e);
			}

		}

		//报表序号不为空则按以下规则校验
		if( null != reportNumber){
			//报表序号数据长度检查
			try{
			if ( reportNumber.getBytes("UTF-8").length> 10 ) {
				throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.maxLength", new String[]{"报表序号(reportNumber)", "10"}));
			}
			}catch (UnsupportedEncodingException e) {
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("message.encoding.unsupport", new String[]{"FslInf", "UTF-8"}));
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
		if( null != flowNumber){
			buf.append("<a n=\"flowNumber\" t=\"str\">");
			buf.append(flowNumber);
			buf.append("</a>");
		}
		if( null != currencyType){
			buf.append("<a n=\"currencyType\" t=\"str\">");
			buf.append(currencyType);
			buf.append("</a>");
		}
		if( null != amount){
			buf.append("<a n=\"amount\" t=\"str\">");
			buf.append(amount);
			buf.append("</a>");
		}
		if( null != reportCode){
			buf.append("<a n=\"reportCode\" t=\"str\">");
			buf.append(reportCode);
			buf.append("</a>");
		}
		if( null != receiveCode){
			buf.append("<a n=\"receiveCode\" t=\"str\">");
			buf.append(receiveCode);
			buf.append("</a>");
		}
		if( null != reportForms){
			buf.append("<a n=\"reportForms\" t=\"datetime\">");
			buf.append(reportForms);
			buf.append("</a>");
		}
		if( null != reportNumber){
			buf.append("<a n=\"reportNumber\" t=\"str\">");
			buf.append(reportNumber);
			buf.append("</a>");
		}
		if( 0 == buf.length()){
			return null;
		}else{
			if ( isTable ){
				buf.insert(0,"<b>");
			}else{
				buf.insert(0,"<b c=\"com.giantstone.cnaps2.messagebean.recv.req.FslInf\">");
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
		if( null != flowNumber){
			return false;
		}
		if( null != currencyType){
			return false;
		}
		if( null != amount){
			return false;
		}
		if( null != reportCode){
			return false;
		}
		if( null != receiveCode){
			return false;
		}
		if( null != reportForms){
			return false;
		}
		if( null != reportNumber){
			return false;
		}
		return true;
	}
}
