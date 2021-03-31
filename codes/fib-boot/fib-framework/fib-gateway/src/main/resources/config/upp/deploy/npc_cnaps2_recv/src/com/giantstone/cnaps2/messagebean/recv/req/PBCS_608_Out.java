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
 * null
 */
public class PBCS_608_Out extends MessageBean{

	//业务头
	private com.giantstone.cnaps2.messagebean.recv.req.GrpHdr grpHdr = new com.giantstone.cnaps2.messagebean.recv.req.GrpHdr();

	public com.giantstone.cnaps2.messagebean.recv.req.GrpHdr getGrpHdr(){
		return grpHdr;
	}

	public void setGrpHdr(com.giantstone.cnaps2.messagebean.recv.req.GrpHdr grpHdr){
		this.grpHdr = grpHdr;
	}

	//计费与返还类型
	private String chargeType;

	public String getChargeType(){
		return chargeType;
	}

	public void setChargeType(String chargeType){
		this.chargeType = chargeType;
	}

	//计费直接参与者
	private String chargeParticipant;

	public String getChargeParticipant(){
		return chargeParticipant;
	}

	public void setChargeParticipant(String chargeParticipant){
		this.chargeParticipant = chargeParticipant;
	}

	//计费月份
	private String chargeMonth;

	public String getChargeMonth(){
		return chargeMonth;
	}

	public void setChargeMonth(String chargeMonth){
		this.chargeMonth = chargeMonth;
	}

	//计费开始日期
	private String startDate;

	public String getStartDate(){
		return startDate;
	}

	public void setStartDate(String startDate){
		this.startDate = startDate;
	}

	//计费终止日期
	private String endDate;

	public String getEndDate(){
		return endDate;
	}

	public void setEndDate(String endDate){
		this.endDate = endDate;
	}

	//币种
	private String currencyType;

	public String getCurrencyType(){
		return currencyType;
	}

	public void setCurrencyType(String currencyType){
		this.currencyType = currencyType;
	}

	//计费/返还总金额
	private String totalAmount;

	public String getTotalAmount(){
		return totalAmount;
	}

	public void setTotalAmount(String totalAmount){
		this.totalAmount = totalAmount;
	}

	//计费业务系统数目
	private String numberOfSystem;

	public String getNumberOfSystem(){
		return numberOfSystem;
	}

	public void setNumberOfSystem(String numberOfSystem){
		this.numberOfSystem = numberOfSystem;
	}

	//计费清单信息
	private List chargeInformation = new ArrayList(20);

	public List getChargeInformation(){
		return chargeInformation;
	}

	public com.giantstone.cnaps2.messagebean.recv.req.PBCS_608_OutChargeInformation getChargeInformationAt(int index){
		return (com.giantstone.cnaps2.messagebean.recv.req.PBCS_608_OutChargeInformation)chargeInformation.get(index);
	}

	public int getChargeInformationRowNum(){
		return chargeInformation.size();
	}

	public void setChargeInformation(List chargeInformation){
		this.chargeInformation = chargeInformation;
	}

	public com.giantstone.cnaps2.messagebean.recv.req.PBCS_608_OutChargeInformation createChargeInformation(){
		com.giantstone.cnaps2.messagebean.recv.req.PBCS_608_OutChargeInformation newRecord = new com.giantstone.cnaps2.messagebean.recv.req.PBCS_608_OutChargeInformation();
		chargeInformation.add(newRecord);
		return newRecord;
	}

	public void addChargeInformation(com.giantstone.cnaps2.messagebean.recv.req.PBCS_608_OutChargeInformation newRecord){
		chargeInformation.add(newRecord);
	}

	public void setChargeInformationAt(int index, com.giantstone.cnaps2.messagebean.recv.req.PBCS_608_OutChargeInformation newRecord){
		chargeInformation.set(index, newRecord);
	}

	public com.giantstone.cnaps2.messagebean.recv.req.PBCS_608_OutChargeInformation removeChargeInformationAt(int index){
		return (com.giantstone.cnaps2.messagebean.recv.req.PBCS_608_OutChargeInformation)chargeInformation.remove(index);
	}

	public void clearChargeInformation(){
		chargeInformation.clear();
	}

	public Object getAttribute(String name){
		if("GrpHdr".equals(name)){
			return  this.getGrpHdr();
		}else if("ChargeType".equals(name)){
			return  this.getChargeType();
		}else if("ChargeParticipant".equals(name)){
			return  this.getChargeParticipant();
		}else if("ChargeMonth".equals(name)){
			return  this.getChargeMonth();
		}else if("StartDate".equals(name)){
			return  this.getStartDate();
		}else if("EndDate".equals(name)){
			return  this.getEndDate();
		}else if("CurrencyType".equals(name)){
			return  this.getCurrencyType();
		}else if("TotalAmount".equals(name)){
			return  this.getTotalAmount();
		}else if("NumberOfSystem".equals(name)){
			return  this.getNumberOfSystem();
		}else if("ChargeInformation".equals(name)){
			return  this.getChargeInformation();
		}
		return null;
	}

	public void setAttribute(String name,Object value){
		if("GrpHdr".equals(name)){
			this.setGrpHdr((com.giantstone.cnaps2.messagebean.recv.req.GrpHdr)value);
		}else if("ChargeType".equals(name)){
			this.setChargeType((String)value);
		}else if("ChargeParticipant".equals(name)){
			this.setChargeParticipant((String)value);
		}else if("ChargeMonth".equals(name)){
			this.setChargeMonth((String)value);
		}else if("StartDate".equals(name)){
			this.setStartDate((String)value);
		}else if("EndDate".equals(name)){
			this.setEndDate((String)value);
		}else if("CurrencyType".equals(name)){
			this.setCurrencyType((String)value);
		}else if("TotalAmount".equals(name)){
			this.setTotalAmount((String)value);
		}else if("NumberOfSystem".equals(name)){
			this.setNumberOfSystem((String)value);
		}else if("ChargeInformation".equals(name)){
			this.setChargeInformation((List)value);
		}
	}

	public void cover(MessageBean bean){
		PBCS_608_Out newBean = (PBCS_608_Out) bean;

		if( null != grpHdr&&null != newBean.getGrpHdr()){
			grpHdr.cover(newBean.getGrpHdr());
		}
		if( null != newBean.getChargeType() && 0 != newBean.getChargeType().length()){
			chargeType = newBean.getChargeType();
		}
		if( null != newBean.getChargeParticipant() && 0 != newBean.getChargeParticipant().length()){
			chargeParticipant = newBean.getChargeParticipant();
		}
		if( null != newBean.getChargeMonth() && 0 != newBean.getChargeMonth().length()){
			chargeMonth = newBean.getChargeMonth();
		}
		if( null != newBean.getStartDate() && 0 != newBean.getStartDate().length()){
			startDate = newBean.getStartDate();
		}
		if( null != newBean.getEndDate() && 0 != newBean.getEndDate().length()){
			endDate = newBean.getEndDate();
		}
		if( null != newBean.getCurrencyType() && 0 != newBean.getCurrencyType().length()){
			currencyType = newBean.getCurrencyType();
		}
		if( null != newBean.getTotalAmount() && 0 != newBean.getTotalAmount().length()){
			totalAmount = newBean.getTotalAmount();
		}
		if( null != newBean.getNumberOfSystem() && 0 != newBean.getNumberOfSystem().length()){
			numberOfSystem = newBean.getNumberOfSystem();
		}
		if(null!=chargeInformation&&null!=newBean.getChargeInformation()){
			int chargeInformationSize=chargeInformation.size();
			if(chargeInformationSize>newBean.getChargeInformation().size()){
				chargeInformationSize=newBean.getChargeInformation().size();
			}
			for( int i =0; i < chargeInformationSize; i++) {
				getChargeInformationAt(i).cover(newBean.getChargeInformationAt(i));
			}
		}
	}

	public void validate(){
		//业务头非空检查
		if ( null == grpHdr || grpHdr.isNull() ){
			throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.null", new String[]{"业务头(grpHdr)"}));
		}

		//业务头正确性检查
			grpHdr.validate();


		//计费与返还类型非空检查
		if ( null == chargeType){
			throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.null", new String[]{"计费与返还类型(chargeType)"}));
		}

			//计费与返还类型数据长度检查
			try{
			if ( chargeType.getBytes("UTF-8").length> 35 ) {
				throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.maxLength", new String[]{"计费与返还类型(chargeType)", "35"}));
			}
			}catch (UnsupportedEncodingException e) {
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("message.encoding.unsupport", new String[]{"PBCS_608_Out", "UTF-8"}));
			}

		//计费直接参与者非空检查
		if ( null == chargeParticipant){
			throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.null", new String[]{"计费直接参与者(chargeParticipant)"}));
		}

			//计费直接参与者数据长度检查
			try{
			if ( chargeParticipant.getBytes("UTF-8").length> 14 ) {
				throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.maxLength", new String[]{"计费直接参与者(chargeParticipant)", "14"}));
			}
			}catch (UnsupportedEncodingException e) {
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("message.encoding.unsupport", new String[]{"PBCS_608_Out", "UTF-8"}));
			}

		//计费月份非空检查
		if ( null == chargeMonth){
			throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.null", new String[]{"计费月份(chargeMonth)"}));
		}

			//计费月份数据长度检查
			try{
			if ( chargeMonth.getBytes("UTF-8").length> 6 ) {
				throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.maxLength", new String[]{"计费月份(chargeMonth)", "6"}));
			}
			}catch (UnsupportedEncodingException e) {
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("message.encoding.unsupport", new String[]{"PBCS_608_Out", "UTF-8"}));
			}

		//计费开始日期非空检查
		if ( null == startDate){
			throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.null", new String[]{"计费开始日期(startDate)"}));
		}

			//计费开始日期数据长度检查
			try{
			if ( startDate.getBytes("UTF-8").length> 10 ) {
				throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.maxLength", new String[]{"计费开始日期(startDate)", "10"}));
			}
			}catch (UnsupportedEncodingException e) {
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("message.encoding.unsupport", new String[]{"PBCS_608_Out", "UTF-8"}));
			}

			//计费开始日期格式检查
			try{
				java.text.DateFormat dateformat = new java.text.SimpleDateFormat("yyyy-MM-dd");
				dateformat.setLenient(false);
				dateformat.parse(startDate);
			} catch (Exception e) {
				throw new RuntimeException(
					MultiLanguageResourceBundle.getInstance().getString("Message.field.pattern.illegal", new String[]{"计费开始日期(startDate)", "yyyy-MM-dd"}) + e.getMessage(), e);
			}

		//计费终止日期非空检查
		if ( null == endDate){
			throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.null", new String[]{"计费终止日期(endDate)"}));
		}

			//计费终止日期数据长度检查
			try{
			if ( endDate.getBytes("UTF-8").length> 10 ) {
				throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.maxLength", new String[]{"计费终止日期(endDate)", "10"}));
			}
			}catch (UnsupportedEncodingException e) {
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("message.encoding.unsupport", new String[]{"PBCS_608_Out", "UTF-8"}));
			}

			//计费终止日期格式检查
			try{
				java.text.DateFormat dateformat = new java.text.SimpleDateFormat("yyyy-MM-dd");
				dateformat.setLenient(false);
				dateformat.parse(endDate);
			} catch (Exception e) {
				throw new RuntimeException(
					MultiLanguageResourceBundle.getInstance().getString("Message.field.pattern.illegal", new String[]{"计费终止日期(endDate)", "yyyy-MM-dd"}) + e.getMessage(), e);
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
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("message.encoding.unsupport", new String[]{"PBCS_608_Out", "UTF-8"}));
			}

		}

		//计费/返还总金额非空检查
		if ( null == totalAmount){
			throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.null", new String[]{"计费/返还总金额(totalAmount)"}));
		}

			//计费/返还总金额数据长度检查
			try{
			if ( totalAmount.getBytes("UTF-8").length> 19 ) {
				throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.maxLength", new String[]{"计费/返还总金额(totalAmount)", "19"}));
			}
			}catch (UnsupportedEncodingException e) {
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("message.encoding.unsupport", new String[]{"PBCS_608_Out", "UTF-8"}));
			}

		//计费业务系统数目非空检查
		if ( null == numberOfSystem){
			throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.null", new String[]{"计费业务系统数目(numberOfSystem)"}));
		}

			//计费业务系统数目数据长度检查
			try{
			if ( numberOfSystem.getBytes("UTF-8").length> 35 ) {
				throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.maxLength", new String[]{"计费业务系统数目(numberOfSystem)", "35"}));
			}
			}catch (UnsupportedEncodingException e) {
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("message.encoding.unsupport", new String[]{"PBCS_608_Out", "UTF-8"}));
			}

		if ( 0 != chargeInformation.size() ) { 		//计费清单信息正确性检查
		for( int i = 0; i < chargeInformation.size(); i++){
			((com.giantstone.cnaps2.messagebean.recv.req.PBCS_608_OutChargeInformation)chargeInformation.get(i)).validate();
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
		if( null != grpHdr){
			str = grpHdr.toString(true);
			if( null != str){
				buf.append("<a n=\"grpHdr\" t=\"bean\">");
				buf.append(str);
				buf.append("</a>");
			}
		}
		if( null != chargeType){
			buf.append("<a n=\"chargeType\" t=\"str\">");
			buf.append(chargeType);
			buf.append("</a>");
		}
		if( null != chargeParticipant){
			buf.append("<a n=\"chargeParticipant\" t=\"str\">");
			buf.append(chargeParticipant);
			buf.append("</a>");
		}
		if( null != chargeMonth){
			buf.append("<a n=\"chargeMonth\" t=\"str\">");
			buf.append(chargeMonth);
			buf.append("</a>");
		}
		if( null != startDate){
			buf.append("<a n=\"startDate\" t=\"datetime\">");
			buf.append(startDate);
			buf.append("</a>");
		}
		if( null != endDate){
			buf.append("<a n=\"endDate\" t=\"datetime\">");
			buf.append(endDate);
			buf.append("</a>");
		}
		if( null != currencyType){
			buf.append("<a n=\"currencyType\" t=\"str\">");
			buf.append(currencyType);
			buf.append("</a>");
		}
		if( null != totalAmount){
			buf.append("<a n=\"totalAmount\" t=\"str\">");
			buf.append(totalAmount);
			buf.append("</a>");
		}
		if( null != numberOfSystem){
			buf.append("<a n=\"numberOfSystem\" t=\"str\">");
			buf.append(numberOfSystem);
			buf.append("</a>");
		}
		com.giantstone.cnaps2.messagebean.recv.req.PBCS_608_OutChargeInformation chargeInformationField = null;
		for( int i =0; i < chargeInformation.size(); i++) {
			chargeInformationField = (com.giantstone.cnaps2.messagebean.recv.req.PBCS_608_OutChargeInformation) chargeInformation.get(i);
			str = chargeInformationField.toString(true,true);
			if( null != str){
				tableBuf.append(str);
			}
		}
		if ( 0 != tableBuf.length()){
			buf.append("<a n=\"chargeInformation\" t=\"list\" c=\"java.util.ArrayList\" rc=\"com.giantstone.cnaps2.messagebean.recv.req.PBCS_608_OutChargeInformation\">");
			buf.append(tableBuf);
			buf.append("</a>");
			tableBuf.delete(0,tableBuf.length());
		}
		if( 0 == buf.length()){
			return null;
		}else{
			if ( isTable ){
				buf.insert(0,"<b>");
			}else{
				buf.insert(0,"<b c=\"com.giantstone.cnaps2.messagebean.recv.req.PBCS_608_Out\">");
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
		if( null != grpHdr){
			if (  !grpHdr.isNull() ){
				return false;
			}
		}
		if( null != chargeType){
			return false;
		}
		if( null != chargeParticipant){
			return false;
		}
		if( null != chargeMonth){
			return false;
		}
		if( null != startDate){
			return false;
		}
		if( null != endDate){
			return false;
		}
		if( null != currencyType){
			return false;
		}
		if( null != totalAmount){
			return false;
		}
		if( null != numberOfSystem){
			return false;
		}
		com.giantstone.cnaps2.messagebean.recv.req.PBCS_608_OutChargeInformation chargeInformationField = null;
		for( int i =0; i < chargeInformation.size(); i++) {
			chargeInformationField = (com.giantstone.cnaps2.messagebean.recv.req.PBCS_608_OutChargeInformation) chargeInformation.get(i);
			if ( null != chargeInformation && !chargeInformationField.isNull() ){
				return false;
			}
		}
		return true;
	}
}
