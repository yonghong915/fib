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
 * 商业汇票业务
 */
public class BEPS_ServiceElement121_A111 extends MessageBean{

	//票据日期
	private String issueDate;

	public String getIssueDate(){
		return issueDate;
	}

	public void setIssueDate(String issueDate){
		this.issueDate = issueDate;
	}

	//出票金额
	private String draftAmount;

	public String getDraftAmount(){
		return draftAmount;
	}

	public void setDraftAmount(String draftAmount){
		this.draftAmount = draftAmount;
	}

	//申请人账号
	private String applyAccount;

	public String getApplyAccount(){
		return applyAccount;
	}

	public void setApplyAccount(String applyAccount){
		this.applyAccount = applyAccount;
	}

	//申请人名称
	private String applyName;

	public String getApplyName(){
		return applyName;
	}

	public void setApplyName(String applyName){
		this.applyName = applyName;
	}

	//实际结算金额
	private String settlementAmount;

	public String getSettlementAmount(){
		return settlementAmount;
	}

	public void setSettlementAmount(String settlementAmount){
		this.settlementAmount = settlementAmount;
	}

	//多余金额
	private String oddAmount;

	public String getOddAmount(){
		return oddAmount;
	}

	public void setOddAmount(String oddAmount){
		this.oddAmount = oddAmount;
	}

	public Object getAttribute(String name){
		if("IssueDate".equals(name)){
			return  this.getIssueDate();
		}else if("DraftAmount".equals(name)){
			return  this.getDraftAmount();
		}else if("ApplyAccount".equals(name)){
			return  this.getApplyAccount();
		}else if("ApplyName".equals(name)){
			return  this.getApplyName();
		}else if("SettlementAmount".equals(name)){
			return  this.getSettlementAmount();
		}else if("OddAmount".equals(name)){
			return  this.getOddAmount();
		}
		return null;
	}

	public void setAttribute(String name,Object value){
		if("IssueDate".equals(name)){
			this.setIssueDate((String)value);
		}else if("DraftAmount".equals(name)){
			this.setDraftAmount((String)value);
		}else if("ApplyAccount".equals(name)){
			this.setApplyAccount((String)value);
		}else if("ApplyName".equals(name)){
			this.setApplyName((String)value);
		}else if("SettlementAmount".equals(name)){
			this.setSettlementAmount((String)value);
		}else if("OddAmount".equals(name)){
			this.setOddAmount((String)value);
		}
	}

	public void cover(MessageBean bean){
		BEPS_ServiceElement121_A111 newBean = (BEPS_ServiceElement121_A111) bean;

		if( null != newBean.getIssueDate() && 0 != newBean.getIssueDate().length()){
			issueDate = newBean.getIssueDate();
		}
		if( null != newBean.getDraftAmount() && 0 != newBean.getDraftAmount().length()){
			draftAmount = newBean.getDraftAmount();
		}
		if( null != newBean.getApplyAccount() && 0 != newBean.getApplyAccount().length()){
			applyAccount = newBean.getApplyAccount();
		}
		if( null != newBean.getApplyName() && 0 != newBean.getApplyName().length()){
			applyName = newBean.getApplyName();
		}
		if( null != newBean.getSettlementAmount() && 0 != newBean.getSettlementAmount().length()){
			settlementAmount = newBean.getSettlementAmount();
		}
		if( null != newBean.getOddAmount() && 0 != newBean.getOddAmount().length()){
			oddAmount = newBean.getOddAmount();
		}
	}

	public void validate(){
		//票据日期非空检查
		if ( null == issueDate){
			throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.null", new String[]{"票据日期(issueDate)"}));
		}

			//票据日期数据长度检查
			try{
			if ( issueDate.getBytes("UTF-8").length> 10 ) {
				throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.maxLength", new String[]{"票据日期(issueDate)", "10"}));
			}
			}catch (UnsupportedEncodingException e) {
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("message.encoding.unsupport", new String[]{"BEPS_ServiceElement121_A111", "UTF-8"}));
			}

			//票据日期格式检查
			try{
				java.text.DateFormat dateformat = new java.text.SimpleDateFormat("yyyy-MM-dd");
				dateformat.setLenient(false);
				dateformat.parse(issueDate);
			} catch (Exception e) {
				throw new RuntimeException(
					MultiLanguageResourceBundle.getInstance().getString("Message.field.pattern.illegal", new String[]{"票据日期(issueDate)", "yyyy-MM-dd"}) + e.getMessage(), e);
			}

		//出票金额非空检查
		if ( null == draftAmount){
			throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.null", new String[]{"出票金额(draftAmount)"}));
		}

			//出票金额数据长度检查
			try{
			if ( draftAmount.getBytes("UTF-8").length> 22 ) {
				throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.maxLength", new String[]{"出票金额(draftAmount)", "22"}));
			}
			}catch (UnsupportedEncodingException e) {
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("message.encoding.unsupport", new String[]{"BEPS_ServiceElement121_A111", "UTF-8"}));
			}

		//申请人账号不为空则按以下规则校验
		if( null != applyAccount){
			//申请人账号数据长度检查
			try{
			if ( applyAccount.getBytes("UTF-8").length> 32 ) {
				throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.maxLength", new String[]{"申请人账号(applyAccount)", "32"}));
			}
			}catch (UnsupportedEncodingException e) {
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("message.encoding.unsupport", new String[]{"BEPS_ServiceElement121_A111", "UTF-8"}));
			}

		}

		//申请人名称不为空则按以下规则校验
		if( null != applyName){
			//申请人名称数据长度检查
			try{
			if ( applyName.getBytes("UTF-8").length> 60 ) {
				throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.maxLength", new String[]{"申请人名称(applyName)", "60"}));
			}
			}catch (UnsupportedEncodingException e) {
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("message.encoding.unsupport", new String[]{"BEPS_ServiceElement121_A111", "UTF-8"}));
			}

		}

		//实际结算金额不为空则按以下规则校验
		if( null != settlementAmount){
			//实际结算金额数据长度检查
			try{
			if ( settlementAmount.getBytes("UTF-8").length> 22 ) {
				throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.maxLength", new String[]{"实际结算金额(settlementAmount)", "22"}));
			}
			}catch (UnsupportedEncodingException e) {
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("message.encoding.unsupport", new String[]{"BEPS_ServiceElement121_A111", "UTF-8"}));
			}

			//实际结算金额输入类型检查
			if(!CodeUtil.isNumeric(settlementAmount)){
				throw new RuntimeException(
					MultiLanguageResourceBundle.getInstance().getString("Message.parameter.type.mustNum", new String[]{"实际结算金额(settlementAmount)"}));
			}

		}

		//多余金额不为空则按以下规则校验
		if( null != oddAmount){
			//多余金额数据长度检查
			try{
			if ( oddAmount.getBytes("UTF-8").length> 22 ) {
				throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.maxLength", new String[]{"多余金额(oddAmount)", "22"}));
			}
			}catch (UnsupportedEncodingException e) {
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("message.encoding.unsupport", new String[]{"BEPS_ServiceElement121_A111", "UTF-8"}));
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
		if( null != issueDate){
			buf.append("<a n=\"issueDate\" t=\"datetime\">");
			buf.append(issueDate);
			buf.append("</a>");
		}
		if( null != draftAmount){
			buf.append("<a n=\"draftAmount\" t=\"str\">");
			buf.append(draftAmount);
			buf.append("</a>");
		}
		if( null != applyAccount){
			buf.append("<a n=\"applyAccount\" t=\"str\">");
			buf.append(applyAccount);
			buf.append("</a>");
		}
		if( null != applyName){
			buf.append("<a n=\"applyName\" t=\"str\">");
			buf.append(applyName);
			buf.append("</a>");
		}
		if( null != settlementAmount){
			buf.append("<a n=\"settlementAmount\" t=\"num\">");
			buf.append(settlementAmount);
			buf.append("</a>");
		}
		if( null != oddAmount){
			buf.append("<a n=\"oddAmount\" t=\"str\">");
			buf.append(oddAmount);
			buf.append("</a>");
		}
		if( 0 == buf.length()){
			return null;
		}else{
			if ( isTable ){
				buf.insert(0,"<b>");
			}else{
				buf.insert(0,"<b c=\"com.giantstone.cnaps2.messagebean.recv.req.BEPS_ServiceElement121_A111\">");
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
		if( null != issueDate){
			return false;
		}
		if( null != draftAmount){
			return false;
		}
		if( null != applyAccount){
			return false;
		}
		if( null != applyName){
			return false;
		}
		if( null != settlementAmount){
			return false;
		}
		if( null != oddAmount){
			return false;
		}
		return true;
	}
}
