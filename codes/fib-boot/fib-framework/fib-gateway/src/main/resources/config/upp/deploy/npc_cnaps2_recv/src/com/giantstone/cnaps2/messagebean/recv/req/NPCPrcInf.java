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
 * NPC信息组件
 */
public class NPCPrcInf extends MessageBean{

	//NPC处理状态
	private String processStatus;

	public String getProcessStatus(){
		return processStatus;
	}

	public void setProcessStatus(String processStatus){
		this.processStatus = processStatus;
	}

	//NPC处理码
	private String processCode;

	public String getProcessCode(){
		return processCode;
	}

	public void setProcessCode(String processCode){
		this.processCode = processCode;
	}

	//NPC拒绝信息
	private String rejectInformation;

	public String getRejectInformation(){
		return rejectInformation;
	}

	public void setRejectInformation(String rejectInformation){
		this.rejectInformation = rejectInformation;
	}

	//NPC轧差日期
	private String nettingDate;

	public String getNettingDate(){
		return nettingDate;
	}

	public void setNettingDate(String nettingDate){
		this.nettingDate = nettingDate;
	}

	//NPC轧差场次
	private String nettingRound;

	public String getNettingRound(){
		return nettingRound;
	}

	public void setNettingRound(String nettingRound){
		this.nettingRound = nettingRound;
	}

	//NPC清算日期/终态日期
	private String settlementDate;

	public String getSettlementDate(){
		return settlementDate;
	}

	public void setSettlementDate(String settlementDate){
		this.settlementDate = settlementDate;
	}

	//NPC接收时间
	private String receiveTime;

	public String getReceiveTime(){
		return receiveTime;
	}

	public void setReceiveTime(String receiveTime){
		this.receiveTime = receiveTime;
	}

	//NPC转发时间
	private String transmitTime;

	public String getTransmitTime(){
		return transmitTime;
	}

	public void setTransmitTime(String transmitTime){
		this.transmitTime = transmitTime;
	}

	public Object getAttribute(String name){
		if("ProcessStatus".equals(name)){
			return  this.getProcessStatus();
		}else if("ProcessCode".equals(name)){
			return  this.getProcessCode();
		}else if("RejectInformation".equals(name)){
			return  this.getRejectInformation();
		}else if("NettingDate".equals(name)){
			return  this.getNettingDate();
		}else if("NettingRound".equals(name)){
			return  this.getNettingRound();
		}else if("SettlementDate".equals(name)){
			return  this.getSettlementDate();
		}else if("ReceiveTime".equals(name)){
			return  this.getReceiveTime();
		}else if("TransmitTime".equals(name)){
			return  this.getTransmitTime();
		}
		return null;
	}

	public void setAttribute(String name,Object value){
		if("ProcessStatus".equals(name)){
			this.setProcessStatus((String)value);
		}else if("ProcessCode".equals(name)){
			this.setProcessCode((String)value);
		}else if("RejectInformation".equals(name)){
			this.setRejectInformation((String)value);
		}else if("NettingDate".equals(name)){
			this.setNettingDate((String)value);
		}else if("NettingRound".equals(name)){
			this.setNettingRound((String)value);
		}else if("SettlementDate".equals(name)){
			this.setSettlementDate((String)value);
		}else if("ReceiveTime".equals(name)){
			this.setReceiveTime((String)value);
		}else if("TransmitTime".equals(name)){
			this.setTransmitTime((String)value);
		}
	}

	public void cover(MessageBean bean){
		NPCPrcInf newBean = (NPCPrcInf) bean;

		if( null != newBean.getProcessStatus() && 0 != newBean.getProcessStatus().length()){
			processStatus = newBean.getProcessStatus();
		}
		if( null != newBean.getProcessCode() && 0 != newBean.getProcessCode().length()){
			processCode = newBean.getProcessCode();
		}
		if( null != newBean.getRejectInformation() && 0 != newBean.getRejectInformation().length()){
			rejectInformation = newBean.getRejectInformation();
		}
		if( null != newBean.getNettingDate() && 0 != newBean.getNettingDate().length()){
			nettingDate = newBean.getNettingDate();
		}
		if( null != newBean.getNettingRound() && 0 != newBean.getNettingRound().length()){
			nettingRound = newBean.getNettingRound();
		}
		if( null != newBean.getSettlementDate() && 0 != newBean.getSettlementDate().length()){
			settlementDate = newBean.getSettlementDate();
		}
		if( null != newBean.getReceiveTime() && 0 != newBean.getReceiveTime().length()){
			receiveTime = newBean.getReceiveTime();
		}
		if( null != newBean.getTransmitTime() && 0 != newBean.getTransmitTime().length()){
			transmitTime = newBean.getTransmitTime();
		}
	}

	public void validate(){
		//NPC处理状态不为空则按以下规则校验
		if( null != processStatus){
			//NPC处理状态数据长度检查
			try{
			if ( processStatus.getBytes("UTF-8").length> 4 ) {
				throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.maxLength", new String[]{"NPC处理状态(processStatus)", "4"}));
			}
			}catch (UnsupportedEncodingException e) {
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("message.encoding.unsupport", new String[]{"NPCPrcInf", "UTF-8"}));
			}

		}

		//NPC处理码不为空则按以下规则校验
		if( null != processCode){
			//NPC处理码数据长度检查
			try{
			if ( processCode.getBytes("UTF-8").length> 8 ) {
				throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.maxLength", new String[]{"NPC处理码(processCode)", "8"}));
			}
			}catch (UnsupportedEncodingException e) {
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("message.encoding.unsupport", new String[]{"NPCPrcInf", "UTF-8"}));
			}

		}

		//NPC拒绝信息不为空则按以下规则校验
		if( null != rejectInformation){
			//NPC拒绝信息数据长度检查
			try{
			if ( rejectInformation.getBytes("UTF-8").length> 105 ) {
				throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.maxLength", new String[]{"NPC拒绝信息(rejectInformation)", "105"}));
			}
			}catch (UnsupportedEncodingException e) {
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("message.encoding.unsupport", new String[]{"NPCPrcInf", "UTF-8"}));
			}

		}

		//NPC轧差日期不为空则按以下规则校验
		if( null != nettingDate){
			//NPC轧差日期数据长度检查
			try{
			if ( nettingDate.getBytes("UTF-8").length> 10 ) {
				throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.maxLength", new String[]{"NPC轧差日期(nettingDate)", "10"}));
			}
			}catch (UnsupportedEncodingException e) {
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("message.encoding.unsupport", new String[]{"NPCPrcInf", "UTF-8"}));
			}

			//NPC轧差日期格式检查
			try{
				java.text.DateFormat dateformat = new java.text.SimpleDateFormat("yyyy-MM-dd");
				dateformat.setLenient(false);
				dateformat.parse(nettingDate);
			} catch (Exception e) {
				throw new RuntimeException(
					MultiLanguageResourceBundle.getInstance().getString("Message.field.pattern.illegal", new String[]{"NPC轧差日期(nettingDate)", "yyyy-MM-dd"}) + e.getMessage(), e);
			}

		}

		//NPC轧差场次不为空则按以下规则校验
		if( null != nettingRound){
			//NPC轧差场次数据长度检查
			try{
			if ( nettingRound.getBytes("UTF-8").length> 2 ) {
				throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.maxLength", new String[]{"NPC轧差场次(nettingRound)", "2"}));
			}
			}catch (UnsupportedEncodingException e) {
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("message.encoding.unsupport", new String[]{"NPCPrcInf", "UTF-8"}));
			}

		}

		//NPC清算日期/终态日期不为空则按以下规则校验
		if( null != settlementDate){
			//NPC清算日期/终态日期数据长度检查
			try{
			if ( settlementDate.getBytes("UTF-8").length> 10 ) {
				throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.maxLength", new String[]{"NPC清算日期/终态日期(settlementDate)", "10"}));
			}
			}catch (UnsupportedEncodingException e) {
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("message.encoding.unsupport", new String[]{"NPCPrcInf", "UTF-8"}));
			}

			//NPC清算日期/终态日期格式检查
			try{
				java.text.DateFormat dateformat = new java.text.SimpleDateFormat("yyyy-MM-dd");
				dateformat.setLenient(false);
				dateformat.parse(settlementDate);
			} catch (Exception e) {
				throw new RuntimeException(
					MultiLanguageResourceBundle.getInstance().getString("Message.field.pattern.illegal", new String[]{"NPC清算日期/终态日期(settlementDate)", "yyyy-MM-dd"}) + e.getMessage(), e);
			}

		}

		//NPC接收时间不为空则按以下规则校验
		if( null != receiveTime){
			//NPC接收时间数据长度检查
			try{
			if ( receiveTime.getBytes("UTF-8").length> 21 ) {
				throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.maxLength", new String[]{"NPC接收时间(receiveTime)", "21"}));
			}
			}catch (UnsupportedEncodingException e) {
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("message.encoding.unsupport", new String[]{"NPCPrcInf", "UTF-8"}));
			}

			//NPC接收时间格式检查
			try{
				java.text.DateFormat dateformat = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
				dateformat.setLenient(false);
				dateformat.parse(receiveTime);
			} catch (Exception e) {
				throw new RuntimeException(
					MultiLanguageResourceBundle.getInstance().getString("Message.field.pattern.illegal", new String[]{"NPC接收时间(receiveTime)", "yyyy-MM-dd'T'HH:mm:ss"}) + e.getMessage(), e);
			}

		}

		//NPC转发时间不为空则按以下规则校验
		if( null != transmitTime){
			//NPC转发时间数据长度检查
			try{
			if ( transmitTime.getBytes("UTF-8").length> 21 ) {
				throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.maxLength", new String[]{"NPC转发时间(transmitTime)", "21"}));
			}
			}catch (UnsupportedEncodingException e) {
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("message.encoding.unsupport", new String[]{"NPCPrcInf", "UTF-8"}));
			}

			//NPC转发时间格式检查
			try{
				java.text.DateFormat dateformat = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
				dateformat.setLenient(false);
				dateformat.parse(transmitTime);
			} catch (Exception e) {
				throw new RuntimeException(
					MultiLanguageResourceBundle.getInstance().getString("Message.field.pattern.illegal", new String[]{"NPC转发时间(transmitTime)", "yyyy-MM-dd'T'HH:mm:ss"}) + e.getMessage(), e);
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
		if( null != processStatus){
			buf.append("<a n=\"processStatus\" t=\"str\">");
			buf.append(processStatus);
			buf.append("</a>");
		}
		if( null != processCode){
			buf.append("<a n=\"processCode\" t=\"str\">");
			buf.append(processCode);
			buf.append("</a>");
		}
		if( null != rejectInformation){
			buf.append("<a n=\"rejectInformation\" t=\"str\">");
			buf.append(rejectInformation);
			buf.append("</a>");
		}
		if( null != nettingDate){
			buf.append("<a n=\"nettingDate\" t=\"datetime\">");
			buf.append(nettingDate);
			buf.append("</a>");
		}
		if( null != nettingRound){
			buf.append("<a n=\"nettingRound\" t=\"str\">");
			buf.append(nettingRound);
			buf.append("</a>");
		}
		if( null != settlementDate){
			buf.append("<a n=\"settlementDate\" t=\"datetime\">");
			buf.append(settlementDate);
			buf.append("</a>");
		}
		if( null != receiveTime){
			buf.append("<a n=\"receiveTime\" t=\"datetime\">");
			buf.append(receiveTime);
			buf.append("</a>");
		}
		if( null != transmitTime){
			buf.append("<a n=\"transmitTime\" t=\"datetime\">");
			buf.append(transmitTime);
			buf.append("</a>");
		}
		if( 0 == buf.length()){
			return null;
		}else{
			if ( isTable ){
				buf.insert(0,"<b>");
			}else{
				buf.insert(0,"<b c=\"com.giantstone.cnaps2.messagebean.recv.req.NPCPrcInf\">");
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
		if( null != processStatus){
			return false;
		}
		if( null != processCode){
			return false;
		}
		if( null != rejectInformation){
			return false;
		}
		if( null != nettingDate){
			return false;
		}
		if( null != nettingRound){
			return false;
		}
		if( null != settlementDate){
			return false;
		}
		if( null != receiveTime){
			return false;
		}
		if( null != transmitTime){
			return false;
		}
		return true;
	}
}
