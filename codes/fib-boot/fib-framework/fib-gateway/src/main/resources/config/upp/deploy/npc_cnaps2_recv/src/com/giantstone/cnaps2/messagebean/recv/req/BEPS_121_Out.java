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
public class BEPS_121_Out extends MessageBean{

	//批量包组头
	private com.giantstone.cnaps2.messagebean.recv.req.PKGGrpHdr pKGGrpHdr = new com.giantstone.cnaps2.messagebean.recv.req.PKGGrpHdr();

	public com.giantstone.cnaps2.messagebean.recv.req.PKGGrpHdr getPKGGrpHdr(){
		return pKGGrpHdr;
	}

	public void setPKGGrpHdr(com.giantstone.cnaps2.messagebean.recv.req.PKGGrpHdr pKGGrpHdr){
		this.pKGGrpHdr = pKGGrpHdr;
	}

	//NPC处理信息组件
	private com.giantstone.cnaps2.messagebean.recv.req.NPCPrcInf nPCPrcInf = new com.giantstone.cnaps2.messagebean.recv.req.NPCPrcInf();

	public com.giantstone.cnaps2.messagebean.recv.req.NPCPrcInf getNPCPrcInf(){
		return nPCPrcInf;
	}

	public void setNPCPrcInf(com.giantstone.cnaps2.messagebean.recv.req.NPCPrcInf nPCPrcInf){
		this.nPCPrcInf = nPCPrcInf;
	}

	//CustomerCreditTransferInformation
	private List ccti = new ArrayList(20);

	public List getCcti(){
		return ccti;
	}

	public com.giantstone.cnaps2.messagebean.recv.req.BEPS_121_OutCcti getCctiAt(int index){
		return (com.giantstone.cnaps2.messagebean.recv.req.BEPS_121_OutCcti)ccti.get(index);
	}

	public int getCctiRowNum(){
		return ccti.size();
	}

	public void setCcti(List ccti){
		this.ccti = ccti;
	}

	public com.giantstone.cnaps2.messagebean.recv.req.BEPS_121_OutCcti createCcti(){
		com.giantstone.cnaps2.messagebean.recv.req.BEPS_121_OutCcti newRecord = new com.giantstone.cnaps2.messagebean.recv.req.BEPS_121_OutCcti();
		ccti.add(newRecord);
		return newRecord;
	}

	public void addCcti(com.giantstone.cnaps2.messagebean.recv.req.BEPS_121_OutCcti newRecord){
		ccti.add(newRecord);
	}

	public void setCctiAt(int index, com.giantstone.cnaps2.messagebean.recv.req.BEPS_121_OutCcti newRecord){
		ccti.set(index, newRecord);
	}

	public com.giantstone.cnaps2.messagebean.recv.req.BEPS_121_OutCcti removeCctiAt(int index){
		return (com.giantstone.cnaps2.messagebean.recv.req.BEPS_121_OutCcti)ccti.remove(index);
	}

	public void clearCcti(){
		ccti.clear();
	}

	public Object getAttribute(String name){
		if("PKGGrpHdr".equals(name)){
			return  this.getPKGGrpHdr();
		}else if("NPCPrcInf".equals(name)){
			return  this.getNPCPrcInf();
		}else if("ccti".equals(name)){
			return  this.getCcti();
		}
		return null;
	}

	public void setAttribute(String name,Object value){
		if("PKGGrpHdr".equals(name)){
			this.setPKGGrpHdr((com.giantstone.cnaps2.messagebean.recv.req.PKGGrpHdr)value);
		}else if("NPCPrcInf".equals(name)){
			this.setNPCPrcInf((com.giantstone.cnaps2.messagebean.recv.req.NPCPrcInf)value);
		}else if("ccti".equals(name)){
			this.setCcti((List)value);
		}
	}

	public void cover(MessageBean bean){
		BEPS_121_Out newBean = (BEPS_121_Out) bean;

		if( null != pKGGrpHdr&&null != newBean.getPKGGrpHdr()){
			pKGGrpHdr.cover(newBean.getPKGGrpHdr());
		}
		if( null != nPCPrcInf&&null != newBean.getNPCPrcInf()){
			nPCPrcInf.cover(newBean.getNPCPrcInf());
		}
		if(null!=ccti&&null!=newBean.getCcti()){
			int cctiSize=ccti.size();
			if(cctiSize>newBean.getCcti().size()){
				cctiSize=newBean.getCcti().size();
			}
			for( int i =0; i < cctiSize; i++) {
				getCctiAt(i).cover(newBean.getCctiAt(i));
			}
		}
	}

	public void validate(){
		//批量包组头非空检查
		if ( null == pKGGrpHdr || pKGGrpHdr.isNull() ){
			throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.null", new String[]{"批量包组头(pKGGrpHdr)"}));
		}

		//批量包组头正确性检查
			pKGGrpHdr.validate();


		//NPC处理信息组件正确性检查
		if ( null != nPCPrcInf && !nPCPrcInf.isNull() ){ 
			nPCPrcInf.validate();
		}

		if ( 0 != ccti.size() ) { 		//CustomerCreditTransferInformation正确性检查
		for( int i = 0; i < ccti.size(); i++){
			((com.giantstone.cnaps2.messagebean.recv.req.BEPS_121_OutCcti)ccti.get(i)).validate();
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
		if( null != pKGGrpHdr){
			str = pKGGrpHdr.toString(true);
			if( null != str){
				buf.append("<a n=\"pKGGrpHdr\" t=\"bean\">");
				buf.append(str);
				buf.append("</a>");
			}
		}
		if( null != nPCPrcInf){
			str = nPCPrcInf.toString(true);
			if( null != str){
				buf.append("<a n=\"nPCPrcInf\" t=\"bean\">");
				buf.append(str);
				buf.append("</a>");
			}
		}
		com.giantstone.cnaps2.messagebean.recv.req.BEPS_121_OutCcti cctiField = null;
		for( int i =0; i < ccti.size(); i++) {
			cctiField = (com.giantstone.cnaps2.messagebean.recv.req.BEPS_121_OutCcti) ccti.get(i);
			str = cctiField.toString(true,true);
			if( null != str){
				tableBuf.append(str);
			}
		}
		if ( 0 != tableBuf.length()){
			buf.append("<a n=\"ccti\" t=\"list\" c=\"java.util.ArrayList\" rc=\"com.giantstone.cnaps2.messagebean.recv.req.BEPS_121_OutCcti\">");
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
				buf.insert(0,"<b c=\"com.giantstone.cnaps2.messagebean.recv.req.BEPS_121_Out\">");
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
		if( null != pKGGrpHdr){
			if (  !pKGGrpHdr.isNull() ){
				return false;
			}
		}
		if( null != nPCPrcInf){
			if (  !nPCPrcInf.isNull() ){
				return false;
			}
		}
		com.giantstone.cnaps2.messagebean.recv.req.BEPS_121_OutCcti cctiField = null;
		for( int i =0; i < ccti.size(); i++) {
			cctiField = (com.giantstone.cnaps2.messagebean.recv.req.BEPS_121_OutCcti) ccti.get(i);
			if ( null != ccti && !cctiField.isNull() ){
				return false;
			}
		}
		return true;
	}
}
