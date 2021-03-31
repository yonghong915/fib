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
 * 托收承付划回
 */
public class BEPS_ServiceElement121_A110 extends MessageBean{

	//票据日期
	private String date;

	public String getDate(){
		return date;
	}

	public void setDate(String date){
		this.date = date;
	}

	//票据号码
	private String number;

	public String getNumber(){
		return number;
	}

	public void setNumber(String number){
		this.number = number;
	}

	//币种
	private String currencyType;

	public String getCurrencyType(){
		return currencyType;
	}

	public void setCurrencyType(String currencyType){
		this.currencyType = currencyType;
	}

	//赔偿金额
	private String amendsAmount;

	public String getAmendsAmount(){
		return amendsAmount;
	}

	public void setAmendsAmount(String amendsAmount){
		this.amendsAmount = amendsAmount;
	}

	//币种
	private String currencyType2;

	public String getCurrencyType2(){
		return currencyType2;
	}

	public void setCurrencyType2(String currencyType2){
		this.currencyType2 = currencyType2;
	}

	//拒付金额
	private String rejectAmount;

	public String getRejectAmount(){
		return rejectAmount;
	}

	public void setRejectAmount(String rejectAmount){
		this.rejectAmount = rejectAmount;
	}

	//币种
	private String currencyType3;

	public String getCurrencyType3(){
		return currencyType3;
	}

	public void setCurrencyType3(String currencyType3){
		this.currencyType3 = currencyType3;
	}

	//原托金额
	private String originalAmount;

	public String getOriginalAmount(){
		return originalAmount;
	}

	public void setOriginalAmount(String originalAmount){
		this.originalAmount = originalAmount;
	}

	//币种
	private String currencyType4;

	public String getCurrencyType4(){
		return currencyType4;
	}

	public void setCurrencyType4(String currencyType4){
		this.currencyType4 = currencyType4;
	}

	//支付金额
	private String paymentAmount;

	public String getPaymentAmount(){
		return paymentAmount;
	}

	public void setPaymentAmount(String paymentAmount){
		this.paymentAmount = paymentAmount;
	}

	//币种
	private String currencyType5;

	public String getCurrencyType5(){
		return currencyType5;
	}

	public void setCurrencyType5(String currencyType5){
		this.currencyType5 = currencyType5;
	}

	//多付金额
	private String oddAmount;

	public String getOddAmount(){
		return oddAmount;
	}

	public void setOddAmount(String oddAmount){
		this.oddAmount = oddAmount;
	}

	public Object getAttribute(String name){
		if("Date".equals(name)){
			return  this.getDate();
		}else if("Number".equals(name)){
			return  this.getNumber();
		}else if("CurrencyType".equals(name)){
			return  this.getCurrencyType();
		}else if("AmendsAmount".equals(name)){
			return  this.getAmendsAmount();
		}else if("CurrencyType2".equals(name)){
			return  this.getCurrencyType2();
		}else if("RejectAmount".equals(name)){
			return  this.getRejectAmount();
		}else if("CurrencyType3".equals(name)){
			return  this.getCurrencyType3();
		}else if("OriginalAmount".equals(name)){
			return  this.getOriginalAmount();
		}else if("CurrencyType4".equals(name)){
			return  this.getCurrencyType4();
		}else if("PaymentAmount".equals(name)){
			return  this.getPaymentAmount();
		}else if("CurrencyType5".equals(name)){
			return  this.getCurrencyType5();
		}else if("OddAmount".equals(name)){
			return  this.getOddAmount();
		}
		return null;
	}

	public void setAttribute(String name,Object value){
		if("Date".equals(name)){
			this.setDate((String)value);
		}else if("Number".equals(name)){
			this.setNumber((String)value);
		}else if("CurrencyType".equals(name)){
			this.setCurrencyType((String)value);
		}else if("AmendsAmount".equals(name)){
			this.setAmendsAmount((String)value);
		}else if("CurrencyType2".equals(name)){
			this.setCurrencyType2((String)value);
		}else if("RejectAmount".equals(name)){
			this.setRejectAmount((String)value);
		}else if("CurrencyType3".equals(name)){
			this.setCurrencyType3((String)value);
		}else if("OriginalAmount".equals(name)){
			this.setOriginalAmount((String)value);
		}else if("CurrencyType4".equals(name)){
			this.setCurrencyType4((String)value);
		}else if("PaymentAmount".equals(name)){
			this.setPaymentAmount((String)value);
		}else if("CurrencyType5".equals(name)){
			this.setCurrencyType5((String)value);
		}else if("OddAmount".equals(name)){
			this.setOddAmount((String)value);
		}
	}

	public void cover(MessageBean bean){
		BEPS_ServiceElement121_A110 newBean = (BEPS_ServiceElement121_A110) bean;

		if( null != newBean.getDate() && 0 != newBean.getDate().length()){
			date = newBean.getDate();
		}
		if( null != newBean.getNumber() && 0 != newBean.getNumber().length()){
			number = newBean.getNumber();
		}
		if( null != newBean.getCurrencyType() && 0 != newBean.getCurrencyType().length()){
			currencyType = newBean.getCurrencyType();
		}
		if( null != newBean.getAmendsAmount() && 0 != newBean.getAmendsAmount().length()){
			amendsAmount = newBean.getAmendsAmount();
		}
		if( null != newBean.getCurrencyType2() && 0 != newBean.getCurrencyType2().length()){
			currencyType2 = newBean.getCurrencyType2();
		}
		if( null != newBean.getRejectAmount() && 0 != newBean.getRejectAmount().length()){
			rejectAmount = newBean.getRejectAmount();
		}
		if( null != newBean.getCurrencyType3() && 0 != newBean.getCurrencyType3().length()){
			currencyType3 = newBean.getCurrencyType3();
		}
		if( null != newBean.getOriginalAmount() && 0 != newBean.getOriginalAmount().length()){
			originalAmount = newBean.getOriginalAmount();
		}
		if( null != newBean.getCurrencyType4() && 0 != newBean.getCurrencyType4().length()){
			currencyType4 = newBean.getCurrencyType4();
		}
		if( null != newBean.getPaymentAmount() && 0 != newBean.getPaymentAmount().length()){
			paymentAmount = newBean.getPaymentAmount();
		}
		if( null != newBean.getCurrencyType5() && 0 != newBean.getCurrencyType5().length()){
			currencyType5 = newBean.getCurrencyType5();
		}
		if( null != newBean.getOddAmount() && 0 != newBean.getOddAmount().length()){
			oddAmount = newBean.getOddAmount();
		}
	}

	public void validate(){
		//票据日期非空检查
		if ( null == date){
			throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.null", new String[]{"票据日期(date)"}));
		}

			//票据日期数据长度检查
			try{
			if ( date.getBytes("UTF-8").length> 10 ) {
				throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.maxLength", new String[]{"票据日期(date)", "10"}));
			}
			}catch (UnsupportedEncodingException e) {
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("message.encoding.unsupport", new String[]{"BEPS_ServiceElement121_A110", "UTF-8"}));
			}

			//票据日期格式检查
			try{
				java.text.DateFormat dateformat = new java.text.SimpleDateFormat("yyyy-MM-dd");
				dateformat.setLenient(false);
				dateformat.parse(date);
			} catch (Exception e) {
				throw new RuntimeException(
					MultiLanguageResourceBundle.getInstance().getString("Message.field.pattern.illegal", new String[]{"票据日期(date)", "yyyy-MM-dd"}) + e.getMessage(), e);
			}

		//票据号码非空检查
		if ( null == number){
			throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.null", new String[]{"票据号码(number)"}));
		}

			//票据号码数据长度检查
			try{
			if ( number.getBytes("UTF-8").length> 32 ) {
				throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.maxLength", new String[]{"票据号码(number)", "32"}));
			}
			}catch (UnsupportedEncodingException e) {
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("message.encoding.unsupport", new String[]{"BEPS_ServiceElement121_A110", "UTF-8"}));
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
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("message.encoding.unsupport", new String[]{"BEPS_ServiceElement121_A110", "UTF-8"}));
			}

		}

		//赔偿金额不为空则按以下规则校验
		if( null != amendsAmount){
			//赔偿金额数据长度检查
			try{
			if ( amendsAmount.getBytes("UTF-8").length> 19 ) {
				throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.maxLength", new String[]{"赔偿金额(amendsAmount)", "19"}));
			}
			}catch (UnsupportedEncodingException e) {
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("message.encoding.unsupport", new String[]{"BEPS_ServiceElement121_A110", "UTF-8"}));
			}

		}

		//币种不为空则按以下规则校验
		if( null != currencyType2){
			//币种数据长度检查
			try{
			if ( currencyType2.getBytes("UTF-8").length> 5 ) {
				throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.maxLength", new String[]{"币种(currencyType2)", "5"}));
			}
			}catch (UnsupportedEncodingException e) {
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("message.encoding.unsupport", new String[]{"BEPS_ServiceElement121_A110", "UTF-8"}));
			}

		}

		//拒付金额不为空则按以下规则校验
		if( null != rejectAmount){
			//拒付金额数据长度检查
			try{
			if ( rejectAmount.getBytes("UTF-8").length> 19 ) {
				throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.maxLength", new String[]{"拒付金额(rejectAmount)", "19"}));
			}
			}catch (UnsupportedEncodingException e) {
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("message.encoding.unsupport", new String[]{"BEPS_ServiceElement121_A110", "UTF-8"}));
			}

		}

		//币种不为空则按以下规则校验
		if( null != currencyType3){
			//币种数据长度检查
			try{
			if ( currencyType3.getBytes("UTF-8").length> 5 ) {
				throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.maxLength", new String[]{"币种(currencyType3)", "5"}));
			}
			}catch (UnsupportedEncodingException e) {
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("message.encoding.unsupport", new String[]{"BEPS_ServiceElement121_A110", "UTF-8"}));
			}

		}

		//原托金额不为空则按以下规则校验
		if( null != originalAmount){
			//原托金额数据长度检查
			try{
			if ( originalAmount.getBytes("UTF-8").length> 19 ) {
				throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.maxLength", new String[]{"原托金额(originalAmount)", "19"}));
			}
			}catch (UnsupportedEncodingException e) {
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("message.encoding.unsupport", new String[]{"BEPS_ServiceElement121_A110", "UTF-8"}));
			}

		}

		//币种不为空则按以下规则校验
		if( null != currencyType4){
			//币种数据长度检查
			try{
			if ( currencyType4.getBytes("UTF-8").length> 5 ) {
				throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.maxLength", new String[]{"币种(currencyType4)", "5"}));
			}
			}catch (UnsupportedEncodingException e) {
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("message.encoding.unsupport", new String[]{"BEPS_ServiceElement121_A110", "UTF-8"}));
			}

		}

		//支付金额不为空则按以下规则校验
		if( null != paymentAmount){
			//支付金额数据长度检查
			try{
			if ( paymentAmount.getBytes("UTF-8").length> 19 ) {
				throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.maxLength", new String[]{"支付金额(paymentAmount)", "19"}));
			}
			}catch (UnsupportedEncodingException e) {
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("message.encoding.unsupport", new String[]{"BEPS_ServiceElement121_A110", "UTF-8"}));
			}

		}

		//币种不为空则按以下规则校验
		if( null != currencyType5){
			//币种数据长度检查
			try{
			if ( currencyType5.getBytes("UTF-8").length> 5 ) {
				throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.maxLength", new String[]{"币种(currencyType5)", "5"}));
			}
			}catch (UnsupportedEncodingException e) {
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("message.encoding.unsupport", new String[]{"BEPS_ServiceElement121_A110", "UTF-8"}));
			}

		}

		//多付金额不为空则按以下规则校验
		if( null != oddAmount){
			//多付金额数据长度检查
			try{
			if ( oddAmount.getBytes("UTF-8").length> 19 ) {
				throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.maxLength", new String[]{"多付金额(oddAmount)", "19"}));
			}
			}catch (UnsupportedEncodingException e) {
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("message.encoding.unsupport", new String[]{"BEPS_ServiceElement121_A110", "UTF-8"}));
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
		if( null != date){
			buf.append("<a n=\"date\" t=\"datetime\">");
			buf.append(date);
			buf.append("</a>");
		}
		if( null != number){
			buf.append("<a n=\"number\" t=\"str\">");
			buf.append(number);
			buf.append("</a>");
		}
		if( null != currencyType){
			buf.append("<a n=\"currencyType\" t=\"str\">");
			buf.append(currencyType);
			buf.append("</a>");
		}
		if( null != amendsAmount){
			buf.append("<a n=\"amendsAmount\" t=\"str\">");
			buf.append(amendsAmount);
			buf.append("</a>");
		}
		if( null != currencyType2){
			buf.append("<a n=\"currencyType2\" t=\"str\">");
			buf.append(currencyType2);
			buf.append("</a>");
		}
		if( null != rejectAmount){
			buf.append("<a n=\"rejectAmount\" t=\"str\">");
			buf.append(rejectAmount);
			buf.append("</a>");
		}
		if( null != currencyType3){
			buf.append("<a n=\"currencyType3\" t=\"str\">");
			buf.append(currencyType3);
			buf.append("</a>");
		}
		if( null != originalAmount){
			buf.append("<a n=\"originalAmount\" t=\"str\">");
			buf.append(originalAmount);
			buf.append("</a>");
		}
		if( null != currencyType4){
			buf.append("<a n=\"currencyType4\" t=\"str\">");
			buf.append(currencyType4);
			buf.append("</a>");
		}
		if( null != paymentAmount){
			buf.append("<a n=\"paymentAmount\" t=\"str\">");
			buf.append(paymentAmount);
			buf.append("</a>");
		}
		if( null != currencyType5){
			buf.append("<a n=\"currencyType5\" t=\"str\">");
			buf.append(currencyType5);
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
				buf.insert(0,"<b c=\"com.giantstone.cnaps2.messagebean.recv.req.BEPS_ServiceElement121_A110\">");
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
		if( null != date){
			return false;
		}
		if( null != number){
			return false;
		}
		if( null != currencyType){
			return false;
		}
		if( null != amendsAmount){
			return false;
		}
		if( null != currencyType2){
			return false;
		}
		if( null != rejectAmount){
			return false;
		}
		if( null != currencyType3){
			return false;
		}
		if( null != originalAmount){
			return false;
		}
		if( null != currencyType4){
			return false;
		}
		if( null != paymentAmount){
			return false;
		}
		if( null != currencyType5){
			return false;
		}
		if( null != oddAmount){
			return false;
		}
		return true;
	}
}
