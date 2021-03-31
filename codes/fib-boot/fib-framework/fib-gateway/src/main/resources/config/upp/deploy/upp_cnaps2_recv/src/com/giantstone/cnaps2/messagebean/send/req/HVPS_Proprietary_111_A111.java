package com.giantstone.cnaps2.messagebean.send.req;

import com.fib.gateway.message.bean.*;
import com.fib.gateway.message.xml.message.*;
import com.fib.gateway.message.*;
import com.fib.gateway.message.util.*;
import java.math.BigDecimal;
import java.io.UnsupportedEncodingException;
import com.fib.gateway.message.xml.message.MultiLanguageResourceBundle;
import java.util.*;

/**
 * 商业汇票
 */
public class HVPS_Proprietary_111_A111 extends MessageBean{

	//票据日期
	private String unstructured;

	public String getUnstructured(){
		return unstructured;
	}

	public void setUnstructured(String unstructured){
		this.unstructured = unstructured;
	}

	//出票金额
	private String unstructured2;

	public String getUnstructured2(){
		return unstructured2;
	}

	public void setUnstructured2(String unstructured2){
		this.unstructured2 = unstructured2;
	}

	//申请人账号
	private String unstructured3;

	public String getUnstructured3(){
		return unstructured3;
	}

	public void setUnstructured3(String unstructured3){
		this.unstructured3 = unstructured3;
	}

	// 申请人名称
	private String unstructured4;

	public String getUnstructured4(){
		return unstructured4;
	}

	public void setUnstructured4(String unstructured4){
		this.unstructured4 = unstructured4;
	}

	//实际结算金额
	private String unstructured5;

	public String getUnstructured5(){
		return unstructured5;
	}

	public void setUnstructured5(String unstructured5){
		this.unstructured5 = unstructured5;
	}

	//多余金额
	private String unstructured6;

	public String getUnstructured6(){
		return unstructured6;
	}

	public void setUnstructured6(String unstructured6){
		this.unstructured6 = unstructured6;
	}

	public Object getAttribute(String name){
		if("Unstructured".equals(name)){
			return  this.getUnstructured();
		}else if("Unstructured2".equals(name)){
			return  this.getUnstructured2();
		}else if("Unstructured3".equals(name)){
			return  this.getUnstructured3();
		}else if("Unstructured4".equals(name)){
			return  this.getUnstructured4();
		}else if("Unstructured5".equals(name)){
			return  this.getUnstructured5();
		}else if("Unstructured6".equals(name)){
			return  this.getUnstructured6();
		}
		return null;
	}

	public void setAttribute(String name,Object value){
		if("Unstructured".equals(name)){
			this.setUnstructured((String)value);
		}else if("Unstructured2".equals(name)){
			this.setUnstructured2((String)value);
		}else if("Unstructured3".equals(name)){
			this.setUnstructured3((String)value);
		}else if("Unstructured4".equals(name)){
			this.setUnstructured4((String)value);
		}else if("Unstructured5".equals(name)){
			this.setUnstructured5((String)value);
		}else if("Unstructured6".equals(name)){
			this.setUnstructured6((String)value);
		}
	}

	public void cover(MessageBean bean){
		HVPS_Proprietary_111_A111 newBean = (HVPS_Proprietary_111_A111) bean;

		if( null != newBean.getUnstructured() && 0 != newBean.getUnstructured().length()){
			unstructured = newBean.getUnstructured();
		}
		if( null != newBean.getUnstructured2() && 0 != newBean.getUnstructured2().length()){
			unstructured2 = newBean.getUnstructured2();
		}
		if( null != newBean.getUnstructured3() && 0 != newBean.getUnstructured3().length()){
			unstructured3 = newBean.getUnstructured3();
		}
		if( null != newBean.getUnstructured4() && 0 != newBean.getUnstructured4().length()){
			unstructured4 = newBean.getUnstructured4();
		}
		if( null != newBean.getUnstructured5() && 0 != newBean.getUnstructured5().length()){
			unstructured5 = newBean.getUnstructured5();
		}
		if( null != newBean.getUnstructured6() && 0 != newBean.getUnstructured6().length()){
			unstructured6 = newBean.getUnstructured6();
		}
	}

	public void validate(){
		//票据日期不为空则按以下规则校验
		if( null != unstructured){
			//票据日期数据长度检查
			try{
			if ( unstructured.getBytes("UTF-8").length> 15 ) {
				throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.maxLength", new String[]{"票据日期(unstructured)", "15"}));
			}
			}catch (UnsupportedEncodingException e) {
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("message.encoding.unsupport", new String[]{"HVPS_Proprietary_111_A111", "UTF-8"}));
			}

		}

		//出票金额不为空则按以下规则校验
		if( null != unstructured2){
			//出票金额数据长度检查
			try{
			if ( unstructured2.getBytes("UTF-8").length> 145 ) {
				throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.maxLength", new String[]{"出票金额(unstructured2)", "145"}));
			}
			}catch (UnsupportedEncodingException e) {
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("message.encoding.unsupport", new String[]{"HVPS_Proprietary_111_A111", "UTF-8"}));
			}

		}

		//申请人账号不为空则按以下规则校验
		if( null != unstructured3){
			//申请人账号数据长度检查
			try{
			if ( unstructured3.getBytes("UTF-8").length> 145 ) {
				throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.maxLength", new String[]{"申请人账号(unstructured3)", "145"}));
			}
			}catch (UnsupportedEncodingException e) {
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("message.encoding.unsupport", new String[]{"HVPS_Proprietary_111_A111", "UTF-8"}));
			}

		}

		// 申请人名称不为空则按以下规则校验
		if( null != unstructured4){
			// 申请人名称数据长度检查
			try{
			if ( unstructured4.getBytes("UTF-8").length> 180 ) {
				throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.maxLength", new String[]{" 申请人名称(unstructured4)", "180"}));
			}
			}catch (UnsupportedEncodingException e) {
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("message.encoding.unsupport", new String[]{"HVPS_Proprietary_111_A111", "UTF-8"}));
			}

		}

		//实际结算金额不为空则按以下规则校验
		if( null != unstructured5){
			//实际结算金额数据长度检查
			try{
			if ( unstructured5.getBytes("UTF-8").length> 145 ) {
				throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.maxLength", new String[]{"实际结算金额(unstructured5)", "145"}));
			}
			}catch (UnsupportedEncodingException e) {
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("message.encoding.unsupport", new String[]{"HVPS_Proprietary_111_A111", "UTF-8"}));
			}

		}

		//多余金额不为空则按以下规则校验
		if( null != unstructured6){
			//多余金额数据长度检查
			try{
			if ( unstructured6.getBytes("UTF-8").length> 145 ) {
				throw new RuntimeException(
				MultiLanguageResourceBundle.getInstance().getString("Message.field.maxLength", new String[]{"多余金额(unstructured6)", "145"}));
			}
			}catch (UnsupportedEncodingException e) {
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("message.encoding.unsupport", new String[]{"HVPS_Proprietary_111_A111", "UTF-8"}));
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
		if( null != unstructured){
			buf.append("<a n=\"unstructured\" t=\"str\">");
			buf.append(unstructured);
			buf.append("</a>");
		}
		if( null != unstructured2){
			buf.append("<a n=\"unstructured2\" t=\"str\">");
			buf.append(unstructured2);
			buf.append("</a>");
		}
		if( null != unstructured3){
			buf.append("<a n=\"unstructured3\" t=\"str\">");
			buf.append(unstructured3);
			buf.append("</a>");
		}
		if( null != unstructured4){
			buf.append("<a n=\"unstructured4\" t=\"str\">");
			buf.append(unstructured4);
			buf.append("</a>");
		}
		if( null != unstructured5){
			buf.append("<a n=\"unstructured5\" t=\"str\">");
			buf.append(unstructured5);
			buf.append("</a>");
		}
		if( null != unstructured6){
			buf.append("<a n=\"unstructured6\" t=\"str\">");
			buf.append(unstructured6);
			buf.append("</a>");
		}
		if( 0 == buf.length()){
			return null;
		}else{
			if ( isTable ){
				buf.insert(0,"<b>");
			}else{
				buf.insert(0,"<b c=\"com.giantstone.cnaps2.messagebean.send.req.HVPS_Proprietary_111_A111\">");
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
		if( null != unstructured){
			return false;
		}
		if( null != unstructured2){
			return false;
		}
		if( null != unstructured3){
			return false;
		}
		if( null != unstructured4){
			return false;
		}
		if( null != unstructured5){
			return false;
		}
		if( null != unstructured6){
			return false;
		}
		return true;
	}
}
