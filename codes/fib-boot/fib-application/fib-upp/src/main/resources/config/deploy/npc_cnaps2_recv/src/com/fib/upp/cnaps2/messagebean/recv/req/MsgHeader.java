package com.fib.upp.cnaps2.messagebean.recv.req;

import com.fib.upp.message.bean.*;
import java.math.BigDecimal;
import java.io.UnsupportedEncodingException;
import com.fib.commons.util.I18nUtils;
import com.fib.upp.util.*;
import java.util.*;

/**
 * 报文头
 */
public class MsgHeader extends MessageBean{

	//起始标识
	private String beginFlag;

	public String getBeginFlag(){
		return beginFlag;
	}

	//结束标识
	private String endFlag;

	public String getEndFlag(){
		return endFlag;
	}

	public void setEndFlag(String endFlag){
		this.endFlag = endFlag;
	}

	//报文传输方向
	private String mesgDirection;

	public String getMesgDirection(){
		return mesgDirection;
	}

	public void setMesgDirection(String mesgDirection){
		this.mesgDirection = mesgDirection;
	}

	//通信级标识号
	private String mesgID;

	public String getMesgID(){
		return mesgID;
	}

	public void setMesgID(String mesgID){
		this.mesgID = mesgID;
	}

	//报文优先级
	private String mesgPriority;

	public String getMesgPriority(){
		return mesgPriority;
	}

	public void setMesgPriority(String mesgPriority){
		this.mesgPriority = mesgPriority;
	}

	//通信级参考号
	private String mesgRefID;

	public String getMesgRefID(){
		return mesgRefID;
	}

	public void setMesgRefID(String mesgRefID){
		this.mesgRefID = mesgRefID;
	}

	//报文类型代码
	private String mesgType;

	public String getMesgType(){
		return mesgType;
	}

	public void setMesgType(String mesgType){
		this.mesgType = mesgType;
	}

	//报文接收人
	private String origReceiver;

	public String getOrigReceiver(){
		return origReceiver;
	}

	public void setOrigReceiver(String origReceiver){
		this.origReceiver = origReceiver;
	}

	//接收系统号
	private String origReceiverSID;

	public String getOrigReceiverSID(){
		return origReceiverSID;
	}

	public void setOrigReceiverSID(String origReceiverSID){
		this.origReceiverSID = origReceiverSID;
	}

	//报文发起日期
	private String origSendDate;

	public String getOrigSendDate(){
		return origSendDate;
	}

	public void setOrigSendDate(String origSendDate){
		this.origSendDate = origSendDate;
	}

	//报文发起时间
	private String origSendTime;

	public String getOrigSendTime(){
		return origSendTime;
	}

	public void setOrigSendTime(String origSendTime){
		this.origSendTime = origSendTime;
	}

	//报文发起人
	private String origSender;

	public String getOrigSender(){
		return origSender;
	}

	public void setOrigSender(String origSender){
		this.origSender = origSender;
	}

	//发送系统号
	private String origSenderSID;

	public String getOrigSenderSID(){
		return origSenderSID;
	}

	public void setOrigSenderSID(String origSenderSID){
		this.origSenderSID = origSenderSID;
	}

	//保留域
	private String reserve;

	public String getReserve(){
		return reserve;
	}

	public void setReserve(String reserve){
		this.reserve = reserve;
	}

	//格式类型
	private String structType;

	public String getStructType(){
		return structType;
	}

	public void setStructType(String structType){
		this.structType = structType;
	}

	//版本号
	private String versionID;

	public String getVersionID(){
		return versionID;
	}

	public void setVersionID(String versionID){
		this.versionID = versionID;
	}

	public Object getAttribute(String name){
		if("BeginFlag".equals(name)){
			return  this.getBeginFlag();
		}else if("EndFlag".equals(name)){
			return  this.getEndFlag();
		}else if("MesgDirection".equals(name)){
			return  this.getMesgDirection();
		}else if("MesgID".equals(name)){
			return  this.getMesgID();
		}else if("MesgPriority".equals(name)){
			return  this.getMesgPriority();
		}else if("MesgRefID".equals(name)){
			return  this.getMesgRefID();
		}else if("MesgType".equals(name)){
			return  this.getMesgType();
		}else if("OrigReceiver".equals(name)){
			return  this.getOrigReceiver();
		}else if("OrigReceiverSID".equals(name)){
			return  this.getOrigReceiverSID();
		}else if("OrigSendDate".equals(name)){
			return  this.getOrigSendDate();
		}else if("OrigSendTime".equals(name)){
			return  this.getOrigSendTime();
		}else if("OrigSender".equals(name)){
			return  this.getOrigSender();
		}else if("OrigSenderSID".equals(name)){
			return  this.getOrigSenderSID();
		}else if("Reserve".equals(name)){
			return  this.getReserve();
		}else if("StructType".equals(name)){
			return  this.getStructType();
		}else if("VersionID".equals(name)){
			return  this.getVersionID();
		}
		return null;
	}

	public void setAttribute(String name,Object value){
		if("EndFlag".equals(name)){
			this.setEndFlag((String)value);
		}else if("MesgDirection".equals(name)){
			this.setMesgDirection((String)value);
		}else if("MesgID".equals(name)){
			this.setMesgID((String)value);
		}else if("MesgPriority".equals(name)){
			this.setMesgPriority((String)value);
		}else if("MesgRefID".equals(name)){
			this.setMesgRefID((String)value);
		}else if("MesgType".equals(name)){
			this.setMesgType((String)value);
		}else if("OrigReceiver".equals(name)){
			this.setOrigReceiver((String)value);
		}else if("OrigReceiverSID".equals(name)){
			this.setOrigReceiverSID((String)value);
		}else if("OrigSendDate".equals(name)){
			this.setOrigSendDate((String)value);
		}else if("OrigSendTime".equals(name)){
			this.setOrigSendTime((String)value);
		}else if("OrigSender".equals(name)){
			this.setOrigSender((String)value);
		}else if("OrigSenderSID".equals(name)){
			this.setOrigSenderSID((String)value);
		}else if("Reserve".equals(name)){
			this.setReserve((String)value);
		}else if("StructType".equals(name)){
			this.setStructType((String)value);
		}else if("VersionID".equals(name)){
			this.setVersionID((String)value);
		}
	}

	public void validate(){
		//结束标识不为空则按以下规则校验
		if( null != endFlag){
			//结束标识数据长度检查
			try{
			if ( endFlag.getBytes("UTF-8").length> 1 ) {
				throw new RuntimeException(
				I18nUtils.getMessage("Message.field.maxLength", new Object[]{"结束标识(endFlag)", "1"}));
			}
			}catch (UnsupportedEncodingException e) {
				throw new RuntimeException(I18nUtils.getMessage("message.encoding.unsupport", new Object[]{"MsgHeader", "UTF-8"}));
			}

		}

		//报文传输方向不为空则按以下规则校验
		if( null != mesgDirection){
			//报文传输方向数据长度检查
			try{
			if ( mesgDirection.getBytes("UTF-8").length> 1 ) {
				throw new RuntimeException(
				I18nUtils.getMessage("Message.field.maxLength", new Object[]{"报文传输方向(mesgDirection)", "1"}));
			}
			}catch (UnsupportedEncodingException e) {
				throw new RuntimeException(I18nUtils.getMessage("message.encoding.unsupport", new Object[]{"MsgHeader", "UTF-8"}));
			}

		}

		//通信级标识号不为空则按以下规则校验
		if( null != mesgID){
			//通信级标识号数据长度检查
			try{
			if ( mesgID.getBytes("UTF-8").length> 20 ) {
				throw new RuntimeException(
				I18nUtils.getMessage("Message.field.maxLength", new Object[]{"通信级标识号(mesgID)", "20"}));
			}
			}catch (UnsupportedEncodingException e) {
				throw new RuntimeException(I18nUtils.getMessage("message.encoding.unsupport", new Object[]{"MsgHeader", "UTF-8"}));
			}

		}

		//报文优先级不为空则按以下规则校验
		if( null != mesgPriority){
			//报文优先级数据长度检查
			try{
			if ( mesgPriority.getBytes("UTF-8").length> 1 ) {
				throw new RuntimeException(
				I18nUtils.getMessage("Message.field.maxLength", new Object[]{"报文优先级(mesgPriority)", "1"}));
			}
			}catch (UnsupportedEncodingException e) {
				throw new RuntimeException(I18nUtils.getMessage("message.encoding.unsupport", new Object[]{"MsgHeader", "UTF-8"}));
			}

			//报文优先级输入类型检查
			if(!CodeUtil.isNumeric(mesgPriority)){
				throw new RuntimeException(
					I18nUtils.getMessage("Message.parameter.type.mustNum", new Object[]{"报文优先级(mesgPriority)"}));
			}

		}

		//通信级参考号不为空则按以下规则校验
		if( null != mesgRefID){
			//通信级参考号数据长度检查
			try{
			if ( mesgRefID.getBytes("UTF-8").length> 20 ) {
				throw new RuntimeException(
				I18nUtils.getMessage("Message.field.maxLength", new Object[]{"通信级参考号(mesgRefID)", "20"}));
			}
			}catch (UnsupportedEncodingException e) {
				throw new RuntimeException(I18nUtils.getMessage("message.encoding.unsupport", new Object[]{"MsgHeader", "UTF-8"}));
			}

		}

		//报文类型代码不为空则按以下规则校验
		if( null != mesgType){
			//报文类型代码数据长度检查
			try{
			if ( mesgType.getBytes("UTF-8").length> 20 ) {
				throw new RuntimeException(
				I18nUtils.getMessage("Message.field.maxLength", new Object[]{"报文类型代码(mesgType)", "20"}));
			}
			}catch (UnsupportedEncodingException e) {
				throw new RuntimeException(I18nUtils.getMessage("message.encoding.unsupport", new Object[]{"MsgHeader", "UTF-8"}));
			}

		}

		//报文接收人不为空则按以下规则校验
		if( null != origReceiver){
			//报文接收人数据长度检查
			try{
			if ( origReceiver.getBytes("UTF-8").length> 14 ) {
				throw new RuntimeException(
				I18nUtils.getMessage("Message.field.maxLength", new Object[]{"报文接收人(origReceiver)", "14"}));
			}
			}catch (UnsupportedEncodingException e) {
				throw new RuntimeException(I18nUtils.getMessage("message.encoding.unsupport", new Object[]{"MsgHeader", "UTF-8"}));
			}

		}

		//接收系统号不为空则按以下规则校验
		if( null != origReceiverSID){
			//接收系统号数据长度检查
			try{
			if ( origReceiverSID.getBytes("UTF-8").length> 4 ) {
				throw new RuntimeException(
				I18nUtils.getMessage("Message.field.maxLength", new Object[]{"接收系统号(origReceiverSID)", "4"}));
			}
			}catch (UnsupportedEncodingException e) {
				throw new RuntimeException(I18nUtils.getMessage("message.encoding.unsupport", new Object[]{"MsgHeader", "UTF-8"}));
			}

		}

		//报文发起日期不为空则按以下规则校验
		if( null != origSendDate){
			//报文发起日期数据长度检查
			try{
			if ( origSendDate.getBytes("UTF-8").length> 8 ) {
				throw new RuntimeException(
				I18nUtils.getMessage("Message.field.maxLength", new Object[]{"报文发起日期(origSendDate)", "8"}));
			}
			}catch (UnsupportedEncodingException e) {
				throw new RuntimeException(I18nUtils.getMessage("message.encoding.unsupport", new Object[]{"MsgHeader", "UTF-8"}));
			}

			//报文发起日期格式检查
			try{
				java.text.DateFormat dateformat = new java.text.SimpleDateFormat("null");
				dateformat.setLenient(false);
				dateformat.parse(origSendDate);
			} catch (Exception e) {
				throw new RuntimeException(
					I18nUtils.getMessage("Message.field.pattern.illegal", new Object[]{"报文发起日期(origSendDate)", "null"}) + e.getMessage(), e);
			}

		}

		//报文发起时间不为空则按以下规则校验
		if( null != origSendTime){
			//报文发起时间数据长度检查
			try{
			if ( origSendTime.getBytes("UTF-8").length> 6 ) {
				throw new RuntimeException(
				I18nUtils.getMessage("Message.field.maxLength", new Object[]{"报文发起时间(origSendTime)", "6"}));
			}
			}catch (UnsupportedEncodingException e) {
				throw new RuntimeException(I18nUtils.getMessage("message.encoding.unsupport", new Object[]{"MsgHeader", "UTF-8"}));
			}

			//报文发起时间格式检查
			try{
				java.text.DateFormat dateformat = new java.text.SimpleDateFormat("null");
				dateformat.setLenient(false);
				dateformat.parse(origSendTime);
			} catch (Exception e) {
				throw new RuntimeException(
					I18nUtils.getMessage("Message.field.pattern.illegal", new Object[]{"报文发起时间(origSendTime)", "null"}) + e.getMessage(), e);
			}

		}

		//报文发起人不为空则按以下规则校验
		if( null != origSender){
			//报文发起人数据长度检查
			try{
			if ( origSender.getBytes("UTF-8").length> 14 ) {
				throw new RuntimeException(
				I18nUtils.getMessage("Message.field.maxLength", new Object[]{"报文发起人(origSender)", "14"}));
			}
			}catch (UnsupportedEncodingException e) {
				throw new RuntimeException(I18nUtils.getMessage("message.encoding.unsupport", new Object[]{"MsgHeader", "UTF-8"}));
			}

		}

		//发送系统号不为空则按以下规则校验
		if( null != origSenderSID){
			//发送系统号数据长度检查
			try{
			if ( origSenderSID.getBytes("UTF-8").length> 4 ) {
				throw new RuntimeException(
				I18nUtils.getMessage("Message.field.maxLength", new Object[]{"发送系统号(origSenderSID)", "4"}));
			}
			}catch (UnsupportedEncodingException e) {
				throw new RuntimeException(I18nUtils.getMessage("message.encoding.unsupport", new Object[]{"MsgHeader", "UTF-8"}));
			}

		}

		//保留域不为空则按以下规则校验
		if( null != reserve){
			//保留域数据长度检查
			try{
			if ( reserve.getBytes("UTF-8").length> 9 ) {
				throw new RuntimeException(
				I18nUtils.getMessage("Message.field.maxLength", new Object[]{"保留域(reserve)", "9"}));
			}
			}catch (UnsupportedEncodingException e) {
				throw new RuntimeException(I18nUtils.getMessage("message.encoding.unsupport", new Object[]{"MsgHeader", "UTF-8"}));
			}

		}

		//格式类型不为空则按以下规则校验
		if( null != structType){
			//格式类型数据长度检查
			try{
			if ( structType.getBytes("UTF-8").length> 3 ) {
				throw new RuntimeException(
				I18nUtils.getMessage("Message.field.maxLength", new Object[]{"格式类型(structType)", "3"}));
			}
			}catch (UnsupportedEncodingException e) {
				throw new RuntimeException(I18nUtils.getMessage("message.encoding.unsupport", new Object[]{"MsgHeader", "UTF-8"}));
			}

		}

		//版本号不为空则按以下规则校验
		if( null != versionID){
			//版本号数据长度检查
			try{
			if ( versionID.getBytes("UTF-8").length> 2 ) {
				throw new RuntimeException(
				I18nUtils.getMessage("Message.field.maxLength", new Object[]{"版本号(versionID)", "2"}));
			}
			}catch (UnsupportedEncodingException e) {
				throw new RuntimeException(I18nUtils.getMessage("message.encoding.unsupport", new Object[]{"MsgHeader", "UTF-8"}));
			}

			//版本号输入类型检查
			if(!CodeUtil.isNumeric(versionID)){
				throw new RuntimeException(
					I18nUtils.getMessage("Message.parameter.type.mustNum", new Object[]{"版本号(versionID)"}));
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
		if( null != endFlag){
			buf.append("<a n=\"endFlag\" t=\"str\">");
			buf.append(getEndFlag());
			buf.append("</a>");
		}
		if( null != mesgDirection){
			buf.append("<a n=\"mesgDirection\" t=\"str\">");
			buf.append(getMesgDirection());
			buf.append("</a>");
		}
		if( null != mesgID){
			buf.append("<a n=\"mesgID\" t=\"str\">");
			buf.append(getMesgID());
			buf.append("</a>");
		}
		if( null != mesgPriority){
			buf.append("<a n=\"mesgPriority\" t=\"num\">");
			buf.append(getMesgPriority());
			buf.append("</a>");
		}
		if( null != mesgRefID){
			buf.append("<a n=\"mesgRefID\" t=\"str\">");
			buf.append(getMesgRefID());
			buf.append("</a>");
		}
		if( null != mesgType){
			buf.append("<a n=\"mesgType\" t=\"str\">");
			buf.append(getMesgType());
			buf.append("</a>");
		}
		if( null != origReceiver){
			buf.append("<a n=\"origReceiver\" t=\"str\">");
			buf.append(getOrigReceiver());
			buf.append("</a>");
		}
		if( null != origReceiverSID){
			buf.append("<a n=\"origReceiverSID\" t=\"str\">");
			buf.append(getOrigReceiverSID());
			buf.append("</a>");
		}
		if( null != origSendDate){
			buf.append("<a n=\"origSendDate\" t=\"datetime\">");
			buf.append(getOrigSendDate());
			buf.append("</a>");
		}
		if( null != origSendTime){
			buf.append("<a n=\"origSendTime\" t=\"datetime\">");
			buf.append(getOrigSendTime());
			buf.append("</a>");
		}
		if( null != origSender){
			buf.append("<a n=\"origSender\" t=\"str\">");
			buf.append(getOrigSender());
			buf.append("</a>");
		}
		if( null != origSenderSID){
			buf.append("<a n=\"origSenderSID\" t=\"str\">");
			buf.append(getOrigSenderSID());
			buf.append("</a>");
		}
		if( null != reserve){
			buf.append("<a n=\"reserve\" t=\"str\">");
			buf.append(getReserve());
			buf.append("</a>");
		}
		if( null != structType){
			buf.append("<a n=\"structType\" t=\"str\">");
			buf.append(getStructType());
			buf.append("</a>");
		}
		if( null != versionID){
			buf.append("<a n=\"versionID\" t=\"num\">");
			buf.append(getVersionID());
			buf.append("</a>");
		}
		if( 0 == buf.length()){
			return null;
		}else{
			if ( isTable ){
				buf.insert(0,"<b>");
			}else{
				buf.insert(0,"<b c=\"com.fib.upp.cnaps2.messagebean.recv.req.MsgHeader\">");
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
		if( null != beginFlag){
			return false;
		}
		if( null != endFlag){
			return false;
		}
		if( null != mesgDirection){
			return false;
		}
		if( null != mesgID){
			return false;
		}
		if( null != mesgPriority){
			return false;
		}
		if( null != mesgRefID){
			return false;
		}
		if( null != mesgType){
			return false;
		}
		if( null != origReceiver){
			return false;
		}
		if( null != origReceiverSID){
			return false;
		}
		if( null != origSendDate){
			return false;
		}
		if( null != origSendTime){
			return false;
		}
		if( null != origSender){
			return false;
		}
		if( null != origSenderSID){
			return false;
		}
		if( null != reserve){
			return false;
		}
		if( null != structType){
			return false;
		}
		if( null != versionID){
			return false;
		}
		return true;
	}
}
