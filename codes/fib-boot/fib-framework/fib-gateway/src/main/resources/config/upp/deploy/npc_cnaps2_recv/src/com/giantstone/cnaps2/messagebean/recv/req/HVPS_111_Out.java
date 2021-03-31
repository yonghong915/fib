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
public class HVPS_111_Out extends MessageBean{

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

	//明细业务总笔数
	private String numberOfTransactions;

	public String getNumberOfTransactions(){
		return numberOfTransactions;
	}

	public void setNumberOfTransactions(String numberOfTransactions){
		this.numberOfTransactions = numberOfTransactions;
	}

	//Code
	private String settlementMethod;

	public String getSettlementMethod(){
		return settlementMethod;
	}

	public void setSettlementMethod(String settlementMethod){
		this.settlementMethod = settlementMethod;
	}

	//端到端标识号
	private String endToEndIdentification;

	public String getEndToEndIdentification(){
		return endToEndIdentification;
	}

	public void setEndToEndIdentification(String endToEndIdentification){
		this.endToEndIdentification = endToEndIdentification;
	}

	//交易标识号
	private String transactionIdentification;

	public String getTransactionIdentification(){
		return transactionIdentification;
	}

	public void setTransactionIdentification(String transactionIdentification){
		this.transactionIdentification = transactionIdentification;
	}

	//业务类型编码
	private String proprietary;

	public String getProprietary(){
		return proprietary;
	}

	public void setProprietary(String proprietary){
		this.proprietary = proprietary;
	}

	//币种
	private String currencyType;

	public String getCurrencyType(){
		return currencyType;
	}

	public void setCurrencyType(String currencyType){
		this.currencyType = currencyType;
	}

	//货币符号、金额
	private String interbankSettlementAmount;

	public String getInterbankSettlementAmount(){
		return interbankSettlementAmount;
	}

	public void setInterbankSettlementAmount(String interbankSettlementAmount){
		this.interbankSettlementAmount = interbankSettlementAmount;
	}

	//业务优先级
	private String settlementPriority;

	public String getSettlementPriority(){
		return settlementPriority;
	}

	public void setSettlementPriority(String settlementPriority){
		this.settlementPriority = settlementPriority;
	}

	//Code
	private String chargeBearer;

	public String getChargeBearer(){
		return chargeBearer;
	}

	public void setChargeBearer(String chargeBearer){
		this.chargeBearer = chargeBearer;
	}

	//付款清算行行号
	private String memberIdentification;

	public String getMemberIdentification(){
		return memberIdentification;
	}

	public void setMemberIdentification(String memberIdentification){
		this.memberIdentification = memberIdentification;
	}

	//付款行行号
	private String identification;

	public String getIdentification(){
		return identification;
	}

	public void setIdentification(String identification){
		this.identification = identification;
	}

	//收款清算行行号
	private String memberIdentification2;

	public String getMemberIdentification2(){
		return memberIdentification2;
	}

	public void setMemberIdentification2(String memberIdentification2){
		this.memberIdentification2 = memberIdentification2;
	}

	//收款行行号
	private String identification2;

	public String getIdentification2(){
		return identification2;
	}

	public void setIdentification2(String identification2){
		this.identification2 = identification2;
	}

	//中介机构1
	private String memberIdentification3;

	public String getMemberIdentification3(){
		return memberIdentification3;
	}

	public void setMemberIdentification3(String memberIdentification3){
		this.memberIdentification3 = memberIdentification3;
	}

	//中介机构1名称
	private String name;

	public String getName(){
		return name;
	}

	public void setName(String name){
		this.name = name;
	}

	//中介机构2
	private String memberIdentification4;

	public String getMemberIdentification4(){
		return memberIdentification4;
	}

	public void setMemberIdentification4(String memberIdentification4){
		this.memberIdentification4 = memberIdentification4;
	}

	//中介机构2名称
	private String name2;

	public String getName2(){
		return name2;
	}

	public void setName2(String name2){
		this.name2 = name2;
	}

	//付款人名称
	private String name3;

	public String getName3(){
		return name3;
	}

	public void setName3(String name3){
		this.name3 = name3;
	}

	//付款人地址
	private String addressLine;

	public String getAddressLine(){
		return addressLine;
	}

	public void setAddressLine(String addressLine){
		this.addressLine = addressLine;
	}

	//付款人账号
	private String identification3;

	public String getIdentification3(){
		return identification3;
	}

	public void setIdentification3(String identification3){
		this.identification3 = identification3;
	}

	//付款人开户行行号
	private String memberIdentification5;

	public String getMemberIdentification5(){
		return memberIdentification5;
	}

	public void setMemberIdentification5(String memberIdentification5){
		this.memberIdentification5 = memberIdentification5;
	}

	//付款人开户行名称
	private String name4;

	public String getName4(){
		return name4;
	}

	public void setName4(String name4){
		this.name4 = name4;
	}

	//收款人开户行行号
	private String memberIdentification6;

	public String getMemberIdentification6(){
		return memberIdentification6;
	}

	public void setMemberIdentification6(String memberIdentification6){
		this.memberIdentification6 = memberIdentification6;
	}

	//收款人开户行名称
	private String name5;

	public String getName5(){
		return name5;
	}

	public void setName5(String name5){
		this.name5 = name5;
	}

	//收款人名称
	private String name6;

	public String getName6(){
		return name6;
	}

	public void setName6(String name6){
		this.name6 = name6;
	}

	//收款人地址
	private String addressLine2;

	public String getAddressLine2(){
		return addressLine2;
	}

	public void setAddressLine2(String addressLine2){
		this.addressLine2 = addressLine2;
	}

	//收款人账号
	private String identification4;

	public String getIdentification4(){
		return identification4;
	}

	public void setIdentification4(String identification4){
		this.identification4 = identification4;
	}

	//业务种类编码
	private String proprietary2;

	public String getProprietary2(){
		return proprietary2;
	}

	public void setProprietary2(String proprietary2){
		this.proprietary2 = proprietary2;
	}

	//附加域list
	private List hvps = new ArrayList(20);

	public List getHvps(){
		return hvps;
	}

	public com.giantstone.cnaps2.messagebean.recv.req.HVPS_111_OutHvps getHvpsAt(int index){
		return (com.giantstone.cnaps2.messagebean.recv.req.HVPS_111_OutHvps)hvps.get(index);
	}

	public int getHvpsRowNum(){
		return hvps.size();
	}

	public void setHvps(List hvps){
		this.hvps = hvps;
	}

	public com.giantstone.cnaps2.messagebean.recv.req.HVPS_111_OutHvps createHvps(){
		com.giantstone.cnaps2.messagebean.recv.req.HVPS_111_OutHvps newRecord = new com.giantstone.cnaps2.messagebean.recv.req.HVPS_111_OutHvps();
		hvps.add(newRecord);
		return newRecord;
	}

	public void addHvps(com.giantstone.cnaps2.messagebean.recv.req.HVPS_111_OutHvps newRecord){
		hvps.add(newRecord);
	}

	public void setHvpsAt(int index, com.giantstone.cnaps2.messagebean.recv.req.HVPS_111_OutHvps newRecord){
		hvps.set(index, newRecord);
	}

	public com.giantstone.cnaps2.messagebean.recv.req.HVPS_111_OutHvps removeHvpsAt(int index){
		return (com.giantstone.cnaps2.messagebean.recv.req.HVPS_111_OutHvps)hvps.remove(index);
	}

	public void clearHvps(){
		hvps.clear();
	}

	public Object getAttribute(String name){
		if("MessageIdentification".equals(name)){
			return  this.getMessageIdentification();
		}else if("CreationDateTime".equals(name)){
			return  this.getCreationDateTime();
		}else if("NumberOfTransactions".equals(name)){
			return  this.getNumberOfTransactions();
		}else if("SettlementMethod".equals(name)){
			return  this.getSettlementMethod();
		}else if("EndToEndIdentification".equals(name)){
			return  this.getEndToEndIdentification();
		}else if("TransactionIdentification".equals(name)){
			return  this.getTransactionIdentification();
		}else if("Proprietary".equals(name)){
			return  this.getProprietary();
		}else if("CurrencyType".equals(name)){
			return  this.getCurrencyType();
		}else if("InterbankSettlementAmount".equals(name)){
			return  this.getInterbankSettlementAmount();
		}else if("SettlementPriority".equals(name)){
			return  this.getSettlementPriority();
		}else if("ChargeBearer".equals(name)){
			return  this.getChargeBearer();
		}else if("MemberIdentification".equals(name)){
			return  this.getMemberIdentification();
		}else if("Identification".equals(name)){
			return  this.getIdentification();
		}else if("MemberIdentification2".equals(name)){
			return  this.getMemberIdentification2();
		}else if("Identification2".equals(name)){
			return  this.getIdentification2();
		}else if("MemberIdentification3".equals(name)){
			return  this.getMemberIdentification3();
		}else if("Name".equals(name)){
			return  this.getName();
		}else if("MemberIdentification4".equals(name)){
			return  this.getMemberIdentification4();
		}else if("Name2".equals(name)){
			return  this.getName2();
		}else if("Name3".equals(name)){
			return  this.getName3();
		}else if("AddressLine".equals(name)){
			return  this.getAddressLine();
		}else if("Identification3".equals(name)){
			return  this.getIdentification3();
		}else if("MemberIdentification5".equals(name)){
			return  this.getMemberIdentification5();
		}else if("Name4".equals(name)){
			return  this.getName4();
		}else if("MemberIdentification6".equals(name)){
			return  this.getMemberIdentification6();
		}else if("Name5".equals(name)){
			return  this.getName5();
		}else if("Name6".equals(name)){
			return  this.getName6();
		}else if("AddressLine2".equals(name)){
			return  this.getAddressLine2();
		}else if("Identification4".equals(name)){
			return  this.getIdentification4();
		}else if("Proprietary2".equals(name)){
			return  this.getProprietary2();
		}else if("hvps".equals(name)){
			return  this.getHvps();
		}
		return null;
	}

	public void setAttribute(String name,Object value){
		if("MessageIdentification".equals(name)){
			this.setMessageIdentification((String)value);
		}else if("CreationDateTime".equals(name)){
			this.setCreationDateTime((String)value);
		}else if("NumberOfTransactions".equals(name)){
			this.setNumberOfTransactions((String)value);
		}else if("SettlementMethod".equals(name)){
			this.setSettlementMethod((String)value);
		}else if("EndToEndIdentification".equals(name)){
			this.setEndToEndIdentification((String)value);
		}else if("TransactionIdentification".equals(name)){
			this.setTransactionIdentification((String)value);
		}else if("Proprietary".equals(name)){
			this.setProprietary((String)value);
		}else if("CurrencyType".equals(name)){
			this.setCurrencyType((String)value);
		}else if("InterbankSettlementAmount".equals(name)){
			this.setInterbankSettlementAmount((String)value);
		}else if("SettlementPriority".equals(name)){
			this.setSettlementPriority((String)value);
		}else if("ChargeBearer".equals(name)){
			this.setChargeBearer((String)value);
		}else if("MemberIdentification".equals(name)){
			this.setMemberIdentification((String)value);
		}else if("Identification".equals(name)){
			this.setIdentification((String)value);
		}else if("MemberIdentification2".equals(name)){
			this.setMemberIdentification2((String)value);
		}else if("Identification2".equals(name)){
			this.setIdentification2((String)value);
		}else if("MemberIdentification3".equals(name)){
			this.setMemberIdentification3((String)value);
		}else if("Name".equals(name)){
			this.setName((String)value);
		}else if("MemberIdentification4".equals(name)){
			this.setMemberIdentification4((String)value);
		}else if("Name2".equals(name)){
			this.setName2((String)value);
		}else if("Name3".equals(name)){
			this.setName3((String)value);
		}else if("AddressLine".equals(name)){
			this.setAddressLine((String)value);
		}else if("Identification3".equals(name)){
			this.setIdentification3((String)value);
		}else if("MemberIdentification5".equals(name)){
			this.setMemberIdentification5((String)value);
		}else if("Name4".equals(name)){
			this.setName4((String)value);
		}else if("MemberIdentification6".equals(name)){
			this.setMemberIdentification6((String)value);
		}else if("Name5".equals(name)){
			this.setName5((String)value);
		}else if("Name6".equals(name)){
			this.setName6((String)value);
		}else if("AddressLine2".equals(name)){
			this.setAddressLine2((String)value);
		}else if("Identification4".equals(name)){
			this.setIdentification4((String)value);
		}else if("Proprietary2".equals(name)){
			this.setProprietary2((String)value);
		}else if("hvps".equals(name)){
			this.setHvps((List)value);
		}
	}

	public void cover(MessageBean bean){
		HVPS_111_Out newBean = (HVPS_111_Out) bean;

		if( null != newBean.getMessageIdentification() && 0 != newBean.getMessageIdentification().length()){
			messageIdentification = newBean.getMessageIdentification();
		}
		if( null != newBean.getCreationDateTime() && 0 != newBean.getCreationDateTime().length()){
			creationDateTime = newBean.getCreationDateTime();
		}
		if( null != newBean.getNumberOfTransactions() && 0 != newBean.getNumberOfTransactions().length()){
			numberOfTransactions = newBean.getNumberOfTransactions();
		}
		if( null != newBean.getSettlementMethod() && 0 != newBean.getSettlementMethod().length()){
			settlementMethod = newBean.getSettlementMethod();
		}
		if( null != newBean.getEndToEndIdentification() && 0 != newBean.getEndToEndIdentification().length()){
			endToEndIdentification = newBean.getEndToEndIdentification();
		}
		if( null != newBean.getTransactionIdentification() && 0 != newBean.getTransactionIdentification().length()){
			transactionIdentification = newBean.getTransactionIdentification();
		}
		if( null != newBean.getProprietary() && 0 != newBean.getProprietary().length()){
			proprietary = newBean.getProprietary();
		}
		if( null != newBean.getCurrencyType() && 0 != newBean.getCurrencyType().length()){
			currencyType = newBean.getCurrencyType();
		}
		if( null != newBean.getInterbankSettlementAmount() && 0 != newBean.getInterbankSettlementAmount().length()){
			interbankSettlementAmount = newBean.getInterbankSettlementAmount();
		}
		if( null != newBean.getSettlementPriority() && 0 != newBean.getSettlementPriority().length()){
			settlementPriority = newBean.getSettlementPriority();
		}
		if( null != newBean.getChargeBearer() && 0 != newBean.getChargeBearer().length()){
			chargeBearer = newBean.getChargeBearer();
		}
		if( null != newBean.getMemberIdentification() && 0 != newBean.getMemberIdentification().length()){
			memberIdentification = newBean.getMemberIdentification();
		}
		if( null != newBean.getIdentification() && 0 != newBean.getIdentification().length()){
			identification = newBean.getIdentification();
		}
		if( null != newBean.getMemberIdentification2() && 0 != newBean.getMemberIdentification2().length()){
			memberIdentification2 = newBean.getMemberIdentification2();
		}
		if( null != newBean.getIdentification2() && 0 != newBean.getIdentification2().length()){
			identification2 = newBean.getIdentification2();
		}
		if( null != newBean.getMemberIdentification3() && 0 != newBean.getMemberIdentification3().length()){
			memberIdentification3 = newBean.getMemberIdentification3();
		}
		if( null != newBean.getName() && 0 != newBean.getName().length()){
			name = newBean.getName();
		}
		if( null != newBean.getMemberIdentification4() && 0 != newBean.getMemberIdentification4().length()){
			memberIdentification4 = newBean.getMemberIdentification4();
		}
		if( null != newBean.getName2() && 0 != newBean.getName2().length()){
			name2 = newBean.getName2();
		}
		if( null != newBean.getName3() && 0 != newBean.getName3().length()){
			name3 = newBean.getName3();
		}
		if( null != newBean.getAddressLine() && 0 != newBean.getAddressLine().length()){
			addressLine = newBean.getAddressLine();
		}
		if( null != newBean.getIdentification3() && 0 != newBean.getIdentification3().length()){
			identification3 = newBean.getIdentification3();
		}
		if( null != newBean.getMemberIdentification5() && 0 != newBean.getMemberIdentification5().length()){
			memberIdentification5 = newBean.getMemberIdentification5();
		}
		if( null != newBean.getName4() && 0 != newBean.getName4().length()){
			name4 = newBean.getName4();
		}
		if( null != newBean.getMemberIdentification6() && 0 != newBean.getMemberIdentification6().length()){
			memberIdentification6 = newBean.getMemberIdentification6();
		}
		if( null != newBean.getName5() && 0 != newBean.getName5().length()){
			name5 = newBean.getName5();
		}
		if( null != newBean.getName6() && 0 != newBean.getName6().length()){
			name6 = newBean.getName6();
		}
		if( null != newBean.getAddressLine2() && 0 != newBean.getAddressLine2().length()){
			addressLine2 = newBean.getAddressLine2();
		}
		if( null != newBean.getIdentification4() && 0 != newBean.getIdentification4().length()){
			identification4 = newBean.getIdentification4();
		}
		if( null != newBean.getProprietary2() && 0 != newBean.getProprietary2().length()){
			proprietary2 = newBean.getProprietary2();
		}
		if(null!=hvps&&null!=newBean.getHvps()){
			int hvpsSize=hvps.size();
			if(hvpsSize>newBean.getHvps().size()){
				hvpsSize=newBean.getHvps().size();
			}
			for( int i =0; i < hvpsSize; i++) {
				getHvpsAt(i).cover(newBean.getHvpsAt(i));
			}
		}
	}

	public void validate(){
		//报文标识号非空检查
		if ( null == messageIdentification){
			throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.null", new String[]{"报文标识号(messageIdentification)"}));
		}

			//报文标识号数据长度检查
			try{
			if ( messageIdentification.getBytes("UTF-8").length> 35 ) {
				throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.maxLength", new String[]{"报文标识号(messageIdentification)", "35"}));
			}
			}catch (UnsupportedEncodingException e) {
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("message.encoding.unsupport", new String[]{"HVPS_111_Out", "UTF-8"}));
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
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("message.encoding.unsupport", new String[]{"HVPS_111_Out", "UTF-8"}));
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

		//明细业务总笔数非空检查
		if ( null == numberOfTransactions){
			throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.null", new String[]{"明细业务总笔数(numberOfTransactions)"}));
		}

			//明细业务总笔数数据长度检查
			try{
			if ( numberOfTransactions.getBytes("UTF-8").length> 15 ) {
				throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.maxLength", new String[]{"明细业务总笔数(numberOfTransactions)", "15"}));
			}
			}catch (UnsupportedEncodingException e) {
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("message.encoding.unsupport", new String[]{"HVPS_111_Out", "UTF-8"}));
			}

		//Code非空检查
		if ( null == settlementMethod){
			throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.null", new String[]{"Code(settlementMethod)"}));
		}

			//Code数据长度检查
			try{
			if ( settlementMethod.getBytes("UTF-8").length> 4 ) {
				throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.maxLength", new String[]{"Code(settlementMethod)", "4"}));
			}
			}catch (UnsupportedEncodingException e) {
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("message.encoding.unsupport", new String[]{"HVPS_111_Out", "UTF-8"}));
			}

		//端到端标识号非空检查
		if ( null == endToEndIdentification){
			throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.null", new String[]{"端到端标识号(endToEndIdentification)"}));
		}

			//端到端标识号数据长度检查
			try{
			if ( endToEndIdentification.getBytes("UTF-8").length> 35 ) {
				throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.maxLength", new String[]{"端到端标识号(endToEndIdentification)", "35"}));
			}
			}catch (UnsupportedEncodingException e) {
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("message.encoding.unsupport", new String[]{"HVPS_111_Out", "UTF-8"}));
			}

		//交易标识号非空检查
		if ( null == transactionIdentification){
			throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.null", new String[]{"交易标识号(transactionIdentification)"}));
		}

			//交易标识号数据长度检查
			try{
			if ( transactionIdentification.getBytes("UTF-8").length> 35 ) {
				throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.maxLength", new String[]{"交易标识号(transactionIdentification)", "35"}));
			}
			}catch (UnsupportedEncodingException e) {
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("message.encoding.unsupport", new String[]{"HVPS_111_Out", "UTF-8"}));
			}

		//业务类型编码非空检查
		if ( null == proprietary){
			throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.null", new String[]{"业务类型编码(proprietary)"}));
		}

			//业务类型编码数据长度检查
			try{
			if ( proprietary.getBytes("UTF-8").length> 4 ) {
				throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.maxLength", new String[]{"业务类型编码(proprietary)", "4"}));
			}
			}catch (UnsupportedEncodingException e) {
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("message.encoding.unsupport", new String[]{"HVPS_111_Out", "UTF-8"}));
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
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("message.encoding.unsupport", new String[]{"HVPS_111_Out", "UTF-8"}));
			}

		}

		//货币符号、金额非空检查
		if ( null == interbankSettlementAmount){
			throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.null", new String[]{"货币符号、金额(interbankSettlementAmount)"}));
		}

			//货币符号、金额数据长度检查
			try{
			if ( interbankSettlementAmount.getBytes("UTF-8").length> 19 ) {
				throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.maxLength", new String[]{"货币符号、金额(interbankSettlementAmount)", "19"}));
			}
			}catch (UnsupportedEncodingException e) {
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("message.encoding.unsupport", new String[]{"HVPS_111_Out", "UTF-8"}));
			}

		//业务优先级非空检查
		if ( null == settlementPriority){
			throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.null", new String[]{"业务优先级(settlementPriority)"}));
		}

			//业务优先级数据长度检查
			try{
			if ( settlementPriority.getBytes("UTF-8").length> 16 ) {
				throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.maxLength", new String[]{"业务优先级(settlementPriority)", "16"}));
			}
			}catch (UnsupportedEncodingException e) {
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("message.encoding.unsupport", new String[]{"HVPS_111_Out", "UTF-8"}));
			}

		//业务优先级指定值域检查
		if ( !"NORM".equals( settlementPriority) && !"HIGH".equals( settlementPriority) && !"URGT".equals( settlementPriority) ) {
			throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.value.in", new String[]{")业务优先级(settlementPriority)", "NORM/HIGH/URGT"}));
		}

		//Code非空检查
		if ( null == chargeBearer){
			throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.null", new String[]{"Code(chargeBearer)"}));
		}

			//Code数据长度检查
			try{
			if ( chargeBearer.getBytes("UTF-8").length> 4 ) {
				throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.maxLength", new String[]{"Code(chargeBearer)", "4"}));
			}
			}catch (UnsupportedEncodingException e) {
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("message.encoding.unsupport", new String[]{"HVPS_111_Out", "UTF-8"}));
			}

		//付款清算行行号非空检查
		if ( null == memberIdentification){
			throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.null", new String[]{"付款清算行行号(memberIdentification)"}));
		}

			//付款清算行行号数据长度检查
			try{
			if ( memberIdentification.getBytes("UTF-8").length> 14 ) {
				throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.maxLength", new String[]{"付款清算行行号(memberIdentification)", "14"}));
			}
			}catch (UnsupportedEncodingException e) {
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("message.encoding.unsupport", new String[]{"HVPS_111_Out", "UTF-8"}));
			}

		//付款行行号非空检查
		if ( null == identification){
			throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.null", new String[]{"付款行行号(identification)"}));
		}

			//付款行行号数据长度检查
			try{
			if ( identification.getBytes("UTF-8").length> 14 ) {
				throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.maxLength", new String[]{"付款行行号(identification)", "14"}));
			}
			}catch (UnsupportedEncodingException e) {
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("message.encoding.unsupport", new String[]{"HVPS_111_Out", "UTF-8"}));
			}

		//收款清算行行号非空检查
		if ( null == memberIdentification2){
			throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.null", new String[]{"收款清算行行号(memberIdentification2)"}));
		}

			//收款清算行行号数据长度检查
			try{
			if ( memberIdentification2.getBytes("UTF-8").length> 14 ) {
				throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.maxLength", new String[]{"收款清算行行号(memberIdentification2)", "14"}));
			}
			}catch (UnsupportedEncodingException e) {
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("message.encoding.unsupport", new String[]{"HVPS_111_Out", "UTF-8"}));
			}

		//收款行行号非空检查
		if ( null == identification2){
			throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.null", new String[]{"收款行行号(identification2)"}));
		}

			//收款行行号数据长度检查
			try{
			if ( identification2.getBytes("UTF-8").length> 14 ) {
				throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.maxLength", new String[]{"收款行行号(identification2)", "14"}));
			}
			}catch (UnsupportedEncodingException e) {
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("message.encoding.unsupport", new String[]{"HVPS_111_Out", "UTF-8"}));
			}

		//中介机构1不为空则按以下规则校验
		if( null != memberIdentification3){
			//中介机构1数据长度检查
			try{
			if ( memberIdentification3.getBytes("UTF-8").length> 35 ) {
				throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.maxLength", new String[]{"中介机构1(memberIdentification3)", "35"}));
			}
			}catch (UnsupportedEncodingException e) {
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("message.encoding.unsupport", new String[]{"HVPS_111_Out", "UTF-8"}));
			}

		}

		//中介机构1名称不为空则按以下规则校验
		if( null != name){
			//中介机构1名称数据长度检查
			try{
			if ( name.getBytes("UTF-8").length> 140 ) {
				throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.maxLength", new String[]{"中介机构1名称(name)", "140"}));
			}
			}catch (UnsupportedEncodingException e) {
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("message.encoding.unsupport", new String[]{"HVPS_111_Out", "UTF-8"}));
			}

		}

		//中介机构2不为空则按以下规则校验
		if( null != memberIdentification4){
			//中介机构2数据长度检查
			try{
			if ( memberIdentification4.getBytes("UTF-8").length> 35 ) {
				throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.maxLength", new String[]{"中介机构2(memberIdentification4)", "35"}));
			}
			}catch (UnsupportedEncodingException e) {
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("message.encoding.unsupport", new String[]{"HVPS_111_Out", "UTF-8"}));
			}

		}

		//中介机构2名称不为空则按以下规则校验
		if( null != name2){
			//中介机构2名称数据长度检查
			try{
			if ( name2.getBytes("UTF-8").length> 140 ) {
				throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.maxLength", new String[]{"中介机构2名称(name2)", "140"}));
			}
			}catch (UnsupportedEncodingException e) {
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("message.encoding.unsupport", new String[]{"HVPS_111_Out", "UTF-8"}));
			}

		}

		//付款人名称不为空则按以下规则校验
		if( null != name3){
			//付款人名称数据长度检查
			try{
			if ( name3.getBytes("UTF-8").length> 140 ) {
				throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.maxLength", new String[]{"付款人名称(name3)", "140"}));
			}
			}catch (UnsupportedEncodingException e) {
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("message.encoding.unsupport", new String[]{"HVPS_111_Out", "UTF-8"}));
			}

		}

		//付款人地址不为空则按以下规则校验
		if( null != addressLine){
			//付款人地址数据长度检查
			try{
			if ( addressLine.getBytes("UTF-8").length> 70 ) {
				throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.maxLength", new String[]{"付款人地址(addressLine)", "70"}));
			}
			}catch (UnsupportedEncodingException e) {
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("message.encoding.unsupport", new String[]{"HVPS_111_Out", "UTF-8"}));
			}

		}

		//付款人账号非空检查
		if ( null == identification3){
			throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.null", new String[]{"付款人账号(identification3)"}));
		}

			//付款人账号数据长度检查
			try{
			if ( identification3.getBytes("UTF-8").length> 34 ) {
				throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.maxLength", new String[]{"付款人账号(identification3)", "34"}));
			}
			}catch (UnsupportedEncodingException e) {
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("message.encoding.unsupport", new String[]{"HVPS_111_Out", "UTF-8"}));
			}

		//付款人开户行行号非空检查
		if ( null == memberIdentification5){
			throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.null", new String[]{"付款人开户行行号(memberIdentification5)"}));
		}

			//付款人开户行行号数据长度检查
			try{
			if ( memberIdentification5.getBytes("UTF-8").length> 35 ) {
				throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.maxLength", new String[]{"付款人开户行行号(memberIdentification5)", "35"}));
			}
			}catch (UnsupportedEncodingException e) {
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("message.encoding.unsupport", new String[]{"HVPS_111_Out", "UTF-8"}));
			}

		//付款人开户行名称不为空则按以下规则校验
		if( null != name4){
			//付款人开户行名称数据长度检查
			try{
			if ( name4.getBytes("UTF-8").length> 140 ) {
				throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.maxLength", new String[]{"付款人开户行名称(name4)", "140"}));
			}
			}catch (UnsupportedEncodingException e) {
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("message.encoding.unsupport", new String[]{"HVPS_111_Out", "UTF-8"}));
			}

		}

		//收款人开户行行号非空检查
		if ( null == memberIdentification6){
			throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.null", new String[]{"收款人开户行行号(memberIdentification6)"}));
		}

			//收款人开户行行号数据长度检查
			try{
			if ( memberIdentification6.getBytes("UTF-8").length> 35 ) {
				throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.maxLength", new String[]{"收款人开户行行号(memberIdentification6)", "35"}));
			}
			}catch (UnsupportedEncodingException e) {
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("message.encoding.unsupport", new String[]{"HVPS_111_Out", "UTF-8"}));
			}

		//收款人开户行名称不为空则按以下规则校验
		if( null != name5){
			//收款人开户行名称数据长度检查
			try{
			if ( name5.getBytes("UTF-8").length> 140 ) {
				throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.maxLength", new String[]{"收款人开户行名称(name5)", "140"}));
			}
			}catch (UnsupportedEncodingException e) {
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("message.encoding.unsupport", new String[]{"HVPS_111_Out", "UTF-8"}));
			}

		}

		//收款人名称不为空则按以下规则校验
		if( null != name6){
			//收款人名称数据长度检查
			try{
			if ( name6.getBytes("UTF-8").length> 140 ) {
				throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.maxLength", new String[]{"收款人名称(name6)", "140"}));
			}
			}catch (UnsupportedEncodingException e) {
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("message.encoding.unsupport", new String[]{"HVPS_111_Out", "UTF-8"}));
			}

		}

		//收款人地址不为空则按以下规则校验
		if( null != addressLine2){
			//收款人地址数据长度检查
			try{
			if ( addressLine2.getBytes("UTF-8").length> 70 ) {
				throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.maxLength", new String[]{"收款人地址(addressLine2)", "70"}));
			}
			}catch (UnsupportedEncodingException e) {
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("message.encoding.unsupport", new String[]{"HVPS_111_Out", "UTF-8"}));
			}

		}

		//收款人账号非空检查
		if ( null == identification4){
			throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.null", new String[]{"收款人账号(identification4)"}));
		}

			//收款人账号数据长度检查
			try{
			if ( identification4.getBytes("UTF-8").length> 34 ) {
				throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.maxLength", new String[]{"收款人账号(identification4)", "34"}));
			}
			}catch (UnsupportedEncodingException e) {
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("message.encoding.unsupport", new String[]{"HVPS_111_Out", "UTF-8"}));
			}

		//业务种类编码非空检查
		if ( null == proprietary2){
			throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.null", new String[]{"业务种类编码(proprietary2)"}));
		}

			//业务种类编码数据长度检查
			try{
			if ( proprietary2.getBytes("UTF-8").length> 35 ) {
				throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.maxLength", new String[]{"业务种类编码(proprietary2)", "35"}));
			}
			}catch (UnsupportedEncodingException e) {
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("message.encoding.unsupport", new String[]{"HVPS_111_Out", "UTF-8"}));
			}

		if ( 0 != hvps.size() ) { 		//附加域list正确性检查
		for( int i = 0; i < hvps.size(); i++){
			((com.giantstone.cnaps2.messagebean.recv.req.HVPS_111_OutHvps)hvps.get(i)).validate();
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
		if( null != numberOfTransactions){
			buf.append("<a n=\"numberOfTransactions\" t=\"str\">");
			buf.append(numberOfTransactions);
			buf.append("</a>");
		}
		if( null != settlementMethod){
			buf.append("<a n=\"settlementMethod\" t=\"str\">");
			buf.append(settlementMethod);
			buf.append("</a>");
		}
		if( null != endToEndIdentification){
			buf.append("<a n=\"endToEndIdentification\" t=\"str\">");
			buf.append(endToEndIdentification);
			buf.append("</a>");
		}
		if( null != transactionIdentification){
			buf.append("<a n=\"transactionIdentification\" t=\"str\">");
			buf.append(transactionIdentification);
			buf.append("</a>");
		}
		if( null != proprietary){
			buf.append("<a n=\"proprietary\" t=\"str\">");
			buf.append(proprietary);
			buf.append("</a>");
		}
		if( null != currencyType){
			buf.append("<a n=\"currencyType\" t=\"str\">");
			buf.append(currencyType);
			buf.append("</a>");
		}
		if( null != interbankSettlementAmount){
			buf.append("<a n=\"interbankSettlementAmount\" t=\"str\">");
			buf.append(interbankSettlementAmount);
			buf.append("</a>");
		}
		if( null != settlementPriority){
			buf.append("<a n=\"settlementPriority\" t=\"str\">");
			buf.append(settlementPriority);
			buf.append("</a>");
		}
		if( null != chargeBearer){
			buf.append("<a n=\"chargeBearer\" t=\"str\">");
			buf.append(chargeBearer);
			buf.append("</a>");
		}
		if( null != memberIdentification){
			buf.append("<a n=\"memberIdentification\" t=\"str\">");
			buf.append(memberIdentification);
			buf.append("</a>");
		}
		if( null != identification){
			buf.append("<a n=\"identification\" t=\"str\">");
			buf.append(identification);
			buf.append("</a>");
		}
		if( null != memberIdentification2){
			buf.append("<a n=\"memberIdentification2\" t=\"str\">");
			buf.append(memberIdentification2);
			buf.append("</a>");
		}
		if( null != identification2){
			buf.append("<a n=\"identification2\" t=\"str\">");
			buf.append(identification2);
			buf.append("</a>");
		}
		if( null != memberIdentification3){
			buf.append("<a n=\"memberIdentification3\" t=\"str\">");
			buf.append(memberIdentification3);
			buf.append("</a>");
		}
		if( null != name){
			buf.append("<a n=\"name\" t=\"str\">");
			buf.append(name);
			buf.append("</a>");
		}
		if( null != memberIdentification4){
			buf.append("<a n=\"memberIdentification4\" t=\"str\">");
			buf.append(memberIdentification4);
			buf.append("</a>");
		}
		if( null != name2){
			buf.append("<a n=\"name2\" t=\"str\">");
			buf.append(name2);
			buf.append("</a>");
		}
		if( null != name3){
			buf.append("<a n=\"name3\" t=\"str\">");
			buf.append(name3);
			buf.append("</a>");
		}
		if( null != addressLine){
			buf.append("<a n=\"addressLine\" t=\"str\">");
			buf.append(addressLine);
			buf.append("</a>");
		}
		if( null != identification3){
			buf.append("<a n=\"identification3\" t=\"str\">");
			buf.append(identification3);
			buf.append("</a>");
		}
		if( null != memberIdentification5){
			buf.append("<a n=\"memberIdentification5\" t=\"str\">");
			buf.append(memberIdentification5);
			buf.append("</a>");
		}
		if( null != name4){
			buf.append("<a n=\"name4\" t=\"str\">");
			buf.append(name4);
			buf.append("</a>");
		}
		if( null != memberIdentification6){
			buf.append("<a n=\"memberIdentification6\" t=\"str\">");
			buf.append(memberIdentification6);
			buf.append("</a>");
		}
		if( null != name5){
			buf.append("<a n=\"name5\" t=\"str\">");
			buf.append(name5);
			buf.append("</a>");
		}
		if( null != name6){
			buf.append("<a n=\"name6\" t=\"str\">");
			buf.append(name6);
			buf.append("</a>");
		}
		if( null != addressLine2){
			buf.append("<a n=\"addressLine2\" t=\"str\">");
			buf.append(addressLine2);
			buf.append("</a>");
		}
		if( null != identification4){
			buf.append("<a n=\"identification4\" t=\"str\">");
			buf.append(identification4);
			buf.append("</a>");
		}
		if( null != proprietary2){
			buf.append("<a n=\"proprietary2\" t=\"str\">");
			buf.append(proprietary2);
			buf.append("</a>");
		}
		com.giantstone.cnaps2.messagebean.recv.req.HVPS_111_OutHvps hvpsField = null;
		for( int i =0; i < hvps.size(); i++) {
			hvpsField = (com.giantstone.cnaps2.messagebean.recv.req.HVPS_111_OutHvps) hvps.get(i);
			str = hvpsField.toString(true,true);
			if( null != str){
				tableBuf.append(str);
			}
		}
		if ( 0 != tableBuf.length()){
			buf.append("<a n=\"hvps\" t=\"list\" c=\"java.util.ArrayList\" rc=\"com.giantstone.cnaps2.messagebean.recv.req.HVPS_111_OutHvps\">");
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
				buf.insert(0,"<b c=\"com.giantstone.cnaps2.messagebean.recv.req.HVPS_111_Out\">");
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
		if( null != numberOfTransactions){
			return false;
		}
		if( null != settlementMethod){
			return false;
		}
		if( null != endToEndIdentification){
			return false;
		}
		if( null != transactionIdentification){
			return false;
		}
		if( null != proprietary){
			return false;
		}
		if( null != currencyType){
			return false;
		}
		if( null != interbankSettlementAmount){
			return false;
		}
		if( null != settlementPriority){
			return false;
		}
		if( null != chargeBearer){
			return false;
		}
		if( null != memberIdentification){
			return false;
		}
		if( null != identification){
			return false;
		}
		if( null != memberIdentification2){
			return false;
		}
		if( null != identification2){
			return false;
		}
		if( null != memberIdentification3){
			return false;
		}
		if( null != name){
			return false;
		}
		if( null != memberIdentification4){
			return false;
		}
		if( null != name2){
			return false;
		}
		if( null != name3){
			return false;
		}
		if( null != addressLine){
			return false;
		}
		if( null != identification3){
			return false;
		}
		if( null != memberIdentification5){
			return false;
		}
		if( null != name4){
			return false;
		}
		if( null != memberIdentification6){
			return false;
		}
		if( null != name5){
			return false;
		}
		if( null != name6){
			return false;
		}
		if( null != addressLine2){
			return false;
		}
		if( null != identification4){
			return false;
		}
		if( null != proprietary2){
			return false;
		}
		com.giantstone.cnaps2.messagebean.recv.req.HVPS_111_OutHvps hvpsField = null;
		for( int i =0; i < hvps.size(); i++) {
			hvpsField = (com.giantstone.cnaps2.messagebean.recv.req.HVPS_111_OutHvps) hvps.get(i);
			if ( null != hvps && !hvpsField.isNull() ){
				return false;
			}
		}
		return true;
	}
}
