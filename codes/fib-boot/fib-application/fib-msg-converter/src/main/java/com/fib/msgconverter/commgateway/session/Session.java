/**
s * 北京长信通信息技术有限公司
 * 2008-8-26 下午08:52:12
 */
package com.fib.msgconverter.commgateway.session;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.fib.msgconverter.commgateway.CommGateway;
import com.fib.msgconverter.commgateway.channel.Channel;
import com.fib.msgconverter.commgateway.channel.config.processor.Processor;
import com.fib.msgconverter.commgateway.job.AbstractJob;
import com.fib.msgconverter.commgateway.util.SensitiveInfoFilter;
import com.fib.msgconverter.commgateway.util.multilang.MultiLanguageResourceBundle;
import com.giantstone.common.map.serializer.MapSerializer;
import com.giantstone.common.util.CodeUtil;
import com.giantstone.common.util.ExceptionUtil;
import com.giantstone.message.MessagePacker;
import com.giantstone.message.bean.MessageBean;
import com.giantstone.message.metadata.MessageMetadataManager;

/**
 * 网关通讯会话
 * 
 * @author 刘恭亮
 * 
 */
public class Session {
	public static final String NEW_LINE = System.getProperty("line.separator");
	public static final String SENSITIVE_FIELD_SEPARATOR = ",";
	public static final String SENSITIVE_FIELD_REPLACE = "*";
	public static final byte SENSITIVE_FIELD_REPLACE_BYTE = (byte) 0x2A;

	// 会话状态，成功结束
	public static final String STATE_SUCCESS = "00";
	// 会话状态，失败结束
	public static final String STATE_FAILURE = "01";
	// 会话状态，进行中
	public static final String STATE_PROCEED = "02";
	// 会话状态，超时取消
	public static final String STATE_CANCEL = "03";
	// 会话状态，未完成
	public static final String STATE_UNFINISHED = "04";

	public static final String TYP_EXTERNAL = "00";
	public static final String TYP_INTERNAL = "01";

	private static SimpleDateFormat format = new SimpleDateFormat(
			"yyyy/MM/dd HH:mm:ss SSS");

	private String id;
	private String type = TYP_EXTERNAL;
	private String state = STATE_PROCEED;
	private long startTime;
	private long startNanoTime;
	private long endTime;
	private long endNanoTime;
	private long timeout;
	private long timeToSendReqNanoTime;
	private long timeToSendResNanoTime;
	private long responseSentNanoTime;
	private long requestSentTime;
	private long requestSentNanoTime;
	private long responseArrivedTime;
	private long responseArrivedNanoTime;
	private Channel sourceChannel;
	private Object source;
	private String sourceInfo;
	private byte[] sourceRequestMessage;
	private Object sourceRequestObject;
	private byte[] sourceResponseMessage;
	private Object sourceResponseObject;
	// private OutboundRule outboundRule;
	// private InboundRule inboundRule;
	// private RouteRule rule;

	private Processor processor;

	private Channel destChannel;
	private Object destSource;
	private String destSourceInfo;
	private byte[] destRequestMessage;
	private Object destRequestObject;
	private byte[] destResponseMessage;
	private Object destResponseObject;

	// private MessageBean requestMessageBean;
	// private MessageBean responseMessageBean;

	private Exception exception;

	private String errorMessage;

	private String errorType;

	private Object errorBean;

	private List eventList = new ArrayList();

	private long lastAliveTime = System.currentTimeMillis();

	private AbstractJob job;

	private String externalSerialNum;

	Session() {
		setStartTime(System.currentTimeMillis());
		setStartNanoTime(System.nanoTime());
	}

	public String toString4Monitor() {
		StringBuffer buf = new StringBuffer(10240);
		buf.append(NEW_LINE);
		// buf.append("------------------ Start At ");
		// buf.append(format.format(new Date(startTime)));
		// buf.append(" ------------------");
		buf.append(MultiLanguageResourceBundle.getInstance().getString(
				"Session.toString.1",
				new String[] { format.format(new Date(startTime)) }));

		buf.append(NEW_LINE);

		if (null != id) {
			// buf.append("id = ");
			// buf.append(id);
			buf.append(MultiLanguageResourceBundle.getInstance().getString(
					"Session.toString.id", new String[] { id }));
			buf.append(NEW_LINE);
		}
		buf.append(MultiLanguageResourceBundle.getInstance().getString(
				"Session.toString.externalSerialNumber",
				new String[] { null == externalSerialNum ? ""
						: externalSerialNum }));
		buf.append(NEW_LINE);

		// buf.append("state = ");
		// buf.append(getDescriptionByState(state));
		// buf.append(NEW_LINE);

		// buf.append("type = ");
		// buf.append(getDescriptionByType(type));
		// buf.append(NEW_LINE);

		// buf.append("timeout = ");
		// buf.append(timeout);
		// buf.append(NEW_LINE);

		// buf.append("startTime = ");
		// buf.append(new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss SSS")
		// .format(new Date(startTime)));
		// buf.append(NEW_LINE);

		// buf.append("endTime = ");
		// buf.append(new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss SSS")
		// .format(new Date(endTime)));
		// buf.append(NEW_LINE);

		// buf.append("lastAliveTime = ");
		// buf.append(new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss SSS")
		// .format(new Date(lastAliveTime)));
		// buf.append(NEW_LINE);

		// buf.append("spendTime = ");
		// buf.append(endTime - startTime);
		// buf.append(" ms");
		// buf.append(NEW_LINE);
		SimpleDateFormat format = new SimpleDateFormat(
				"yyyy-MM-dd  HH:mm:ss SSS");
		buf
				.append(MultiLanguageResourceBundle
						.getInstance()
						.getString(
								"Session.toString.2",
								new String[] {
										getDescriptionByState(state),
										getDescriptionByType(type),
										"" + timeout,
										(null == processor ? -1 : processor
												.getTimeout())
												+ "",
										format.format(new Date(startTime)),
										format.format(new Date(endTime)),
										format.format(new Date(lastAliveTime)),
										String
												.valueOf((endNanoTime - startNanoTime) / 1000000) }));
		buf.append(NEW_LINE);

		if (null != processor) {
			// buf.append("processor[id = ");
			// buf.append(processor.getId());
			// buf.append(", type = ");
			// buf.append(Processor.getTextByType(processor.getType()));
			// buf.append("]");
			buf.append(MultiLanguageResourceBundle.getInstance().getString(
					"Session.toString.processor",
					new String[] { processor.getId(),
							Processor.getTextByType(processor.getType()) }));
			buf.append(NEW_LINE);
		}

		if (!STATE_SUCCESS.equals(state)) {
			if (null != errorMessage) {
				// buf.append("errorMessage[" + errorMessage.length() + "] = ");
				// buf.append(errorMessage);
				buf.append(MultiLanguageResourceBundle.getInstance()
						.getString(
								"Session.toString.errorMessage",
								new String[] { "" + errorMessage.length(),
										errorMessage }));
				buf.append(NEW_LINE);
			}
			if (exception != null) {
				// buf.append("exception = ");
				// buf.append(ExceptionUtil.getExceptionDetail(exception));
				buf.append(MultiLanguageResourceBundle.getInstance().getString(
						"Session.toString.exception",
						new String[] { ExceptionUtil
								.getExceptionDetail(exception) }));
				buf.append(NEW_LINE);
			}
		}
		if (sourceChannel != null) {
			// buf.append("sourceChannel = ");
			// buf.append(sourceChannel.toString());
			buf.append(MultiLanguageResourceBundle.getInstance().getString(
					"Session.toString.sourceChannel",
					new String[] { sourceChannel.toString() }));
			buf.append(NEW_LINE);
		}

		if (source != null) {
			// buf.append("source = ");
			// buf.append(source.toString());
			buf.append(MultiLanguageResourceBundle.getInstance().getString(
					"Session.toString.source",
					new String[] { source.toString() }));
			buf.append(NEW_LINE);
		}

		if (destChannel != null) {
			// buf.append("destChannel = ");
			// buf.append(destChannel.toString());
			buf.append(MultiLanguageResourceBundle.getInstance().getString(
					"Session.toString.destChannel",
					new String[] { destChannel.toString() }));
			buf.append(NEW_LINE);
		}

		if (destSource != null) {
			// buf.append("destSource = ");
			// buf.append(destSource.toString());
			buf.append(MultiLanguageResourceBundle.getInstance().getString(
					"Session.toString.destSource",
					new String[] { destSource.toString() }));
			buf.append(NEW_LINE);
		}

		if (null != errorBean) {
			buf.append(MultiLanguageResourceBundle.getInstance().getString(
					"Session.toString.errorBean",
					new String[] { errorBean.getClass().toString(),
							errorBean.toString() }));
			buf.append(NEW_LINE);
		}

		// buf.append("------------------  End  At ");
		// buf.append(format.format(new Date(endTime)));
		// buf.append(" ------------------");
		buf.append(MultiLanguageResourceBundle.getInstance().getString(
				"Session.toString.3",
				new String[] { format.format(new Date(endTime)) }));
		buf.append(NEW_LINE);

		return buf.toString();
	}

	public String toString() {
		StringBuffer buf = new StringBuffer(10240);
		buf.append(NEW_LINE);
		// buf.append("------------------ Start At ");
		// buf.append(format.format(new Date(startTime)));
		// buf.append(" ------------------");
		buf.append(MultiLanguageResourceBundle.getInstance().getString(
				"Session.toString.1",
				new String[] { format.format(new Date(startTime)) }));

		buf.append(NEW_LINE);

		if (null != id) {
			// buf.append("id = ");
			// buf.append(id);
			buf.append(MultiLanguageResourceBundle.getInstance().getString(
					"Session.toString.id", new String[] { id }));
			buf.append(NEW_LINE);
		}

		buf.append(MultiLanguageResourceBundle.getInstance().getString(
				"Session.toString.externalSerialNumber",
				new String[] { null == externalSerialNum ? ""
						: externalSerialNum }));
		buf.append(NEW_LINE);

		// buf.append("state = ");
		// buf.append(getDescriptionByState(state));
		// buf.append(NEW_LINE);

		// buf.append("type = ");
		// buf.append(getDescriptionByType(type));
		// buf.append(NEW_LINE);

		// buf.append("timeout = ");
		// buf.append(timeout);
		// buf.append(NEW_LINE);

		// buf.append("startTime = ");
		// buf.append(new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss SSS")
		// .format(new Date(startTime)));
		// buf.append(NEW_LINE);

		// buf.append("endTime = ");
		// buf.append(new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss SSS")
		// .format(new Date(endTime)));
		// buf.append(NEW_LINE);

		// buf.append("lastAliveTime = ");
		// buf.append(new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss SSS")
		// .format(new Date(lastAliveTime)));
		// buf.append(NEW_LINE);

		// buf.append("spendTime = ");
		// buf.append(endTime - startTime);
		// buf.append(" ms");
		// buf.append(NEW_LINE);
		SimpleDateFormat format = new SimpleDateFormat(
				"yyyy-MM-dd  HH:mm:ss SSS");
		buf
				.append(MultiLanguageResourceBundle
						.getInstance()
						.getString(
								"Session.toString.2",
								new String[] {
										getDescriptionByState(state),
										getDescriptionByType(type),
										"" + timeout,
										(null == processor ? -1 : processor
												.getTimeout())
												+ "",
										format.format(new Date(startTime)),
										format.format(new Date(endTime)),
										format.format(new Date(lastAliveTime)),
										String
												.valueOf((endNanoTime - startNanoTime) / 1000000) }));
		buf.append(NEW_LINE);

		if (null != processor) {
			// buf.append("processor[id = ");
			// buf.append(processor.getId());
			// buf.append(", type = ");
			// buf.append(Processor.getTextByType(processor.getType()));
			// buf.append("]");
			buf.append(MultiLanguageResourceBundle.getInstance().getString(
					"Session.toString.processor",
					new String[] { processor.getId(),
							Processor.getTextByType(processor.getType()) }));
			buf.append(NEW_LINE);
		}

		if (!STATE_SUCCESS.equals(state)) {
			if (null != errorMessage) {
				// buf.append("errorMessage[" + errorMessage.length() + "] = ");
				// buf.append(errorMessage);
				buf.append(MultiLanguageResourceBundle.getInstance()
						.getString(
								"Session.toString.errorMessage",
								new String[] { "" + errorMessage.length(),
										errorMessage }));
				buf.append(NEW_LINE);
			}
			if (exception != null) {
				// buf.append("exception = ");
				// buf.append(ExceptionUtil.getExceptionDetail(exception));
				buf.append(MultiLanguageResourceBundle.getInstance().getString(
						"Session.toString.exception",
						new String[] { ExceptionUtil
								.getExceptionDetail(exception) }));
				buf.append(NEW_LINE);
			}
		}
		if (sourceChannel != null) {
			// buf.append("sourceChannel = ");
			// buf.append(sourceChannel.toString());
			buf.append(MultiLanguageResourceBundle.getInstance().getString(
					"Session.toString.sourceChannel",
					new String[] { sourceChannel.toString() }));
			buf.append(NEW_LINE);
		}

		if (source != null) {
			// buf.append("source = ");
			// buf.append(source.toString());
			buf.append(MultiLanguageResourceBundle.getInstance().getString(
					"Session.toString.source",
					new String[] { source.toString() }));
			buf.append(NEW_LINE);
		}

		if (destChannel != null) {
			// buf.append("destChannel = ");
			// buf.append(destChannel.toString());
			buf.append(MultiLanguageResourceBundle.getInstance().getString(
					"Session.toString.destChannel",
					new String[] { destChannel.toString() }));
			buf.append(NEW_LINE);
		}

		if (destSource != null) {
			// buf.append("destSource = ");
			// buf.append(destSource.toString());
			buf.append(MultiLanguageResourceBundle.getInstance().getString(
					"Session.toString.destSource",
					new String[] { destSource.toString() }));
			buf.append(NEW_LINE);
		}

		if (sourceRequestMessage != null) {
			// buf.append("sourceRequestMessage[" + sourceRequestMessage.length
			// + "] = ");
			// buf.append(NEW_LINE);
			// buf.append(CodeUtil.Bytes2FormattedText(sourceRequestMessage));
			buf
					.append(MultiLanguageResourceBundle
							.getInstance()
							.getString(
									"Session.toString.sourceReqMsg",
									new String[] {
											"" + sourceRequestMessage.length,
											CodeUtil
													.Bytes2FormattedText(sourceRequestMessage) }));
			buf.append(NEW_LINE);
		}

		if (null != sourceRequestObject) {
			StringBuffer sourceRequestObjectBuffer = new StringBuffer();
			if (sourceRequestObject instanceof MessageBean) {
				sourceRequestObjectBuffer
						.append(((MessageBean) sourceRequestObject).toString());
			} else if (sourceRequestObject instanceof Map) {
				// ByteArrayOutputStream out = new ByteArrayOutputStream();
				// try {
				// MapUtil.printMap((Map) sourceRequestObject, out);
				// sourceRequestObjectBuffer.append(out.toString());
				// } finally {
				// try {
				// out.close();
				// } catch (IOException e) {
				// // e.printStackTrace();
				// }
				// }
				sourceRequestObjectBuffer.append(MapSerializer
						.serialize((Map) sourceRequestObject));
			} else {
				sourceRequestObjectBuffer.append(MultiLanguageResourceBundle
						.getInstance().getString(
								"Session.toString.unsupportObjectType"));
			}
			buf.append(MultiLanguageResourceBundle.getInstance().getString(
					"Session.toString.sourceReqObj",
					new String[] { sourceRequestObject.getClass().toString(),
							sourceRequestObjectBuffer.toString() }));
			buf.append(NEW_LINE);
		}

		// if (requestMessageBean != null) {
		// buf.append("requestMessageBean = ");
		// buf.append(NEW_LINE);
		// buf.append(requestMessageBean.toString());
		// buf.append(NEW_LINE);
		// }

		// if (outboundRule != null) {
		// buf.append("outboundRule = ");
		// buf.append(outboundRule.toString());
		// buf.append(NEW_LINE);
		// }
		//
		// if (inboundRule != null) {
		// buf.append("inboundRule = ");
		// buf.append(inboundRule.toString());
		// buf.append(NEW_LINE);
		// }
		if (destRequestMessage != null) {
			// buf.append("destRequestMessage[" + destRequestMessage.length
			// + "] = ");
			// buf.append(NEW_LINE);
			// buf.append(CodeUtil.Bytes2FormattedText(destRequestMessage));
			buf
					.append(MultiLanguageResourceBundle
							.getInstance()
							.getString(
									"Session.toString.destReqMsg",
									new String[] {
											"" + destRequestMessage.length,
											CodeUtil
													.Bytes2FormattedText(destRequestMessage) }));
			buf.append(NEW_LINE);
		}

		if (null != destRequestObject) {
			StringBuffer destRequestObjectBuffer = new StringBuffer();
			if (destRequestObject instanceof MessageBean) {
				destRequestObjectBuffer
						.append(((MessageBean) destRequestObject).toString());
			} else if (destRequestObject instanceof Map) {
				// ByteArrayOutputStream out = new ByteArrayOutputStream();
				// try {
				// MapUtil.printMap((Map) destRequestObject, out);
				// destRequestObjectBuffer.append(out.toString());
				// } finally {
				// try {
				// out.close();
				// } catch (IOException e) {
				// // e.printStackTrace();
				// }
				// }
				destRequestObjectBuffer.append(MapSerializer
						.serialize((Map) destRequestObject));
			} else {
				destRequestObjectBuffer.append(MultiLanguageResourceBundle
						.getInstance().getString(
								"Session.toString.unsupportObjectType"));
			}
			buf.append(MultiLanguageResourceBundle.getInstance().getString(
					"Session.toString.destReqObj",
					new String[] { destRequestObject.getClass().toString(),
							destRequestObjectBuffer.toString() }));
			buf.append(NEW_LINE);
		}

		if (destResponseMessage != null) {
			// buf.append("destResponseMessage[" + destResponseMessage.length
			// + "] = ");
			// buf.append(NEW_LINE);
			// buf.append(CodeUtil.Bytes2FormattedText(destResponseMessage));
			buf
					.append(MultiLanguageResourceBundle
							.getInstance()
							.getString(
									"Session.toString.destResMsg",
									new String[] {
											"" + destResponseMessage.length,
											CodeUtil
													.Bytes2FormattedText(destResponseMessage) }));
			buf.append(NEW_LINE);
		}

		if (null != destResponseObject) {
			StringBuffer destResponseObjectBuffer = new StringBuffer();
			if (destResponseObject instanceof MessageBean) {
				destResponseObjectBuffer
						.append(((MessageBean) destResponseObject).toString());
			} else if (destResponseObject instanceof Map) {
				// ByteArrayOutputStream out = new ByteArrayOutputStream();
				// try {
				// MapUtil.printMap((Map) destResponseObject, out);
				// destResponseObjectBuffer.append(out.toString());
				// } finally {
				// try {
				// out.close();
				// } catch (IOException e) {
				// // e.printStackTrace();
				// }
				// }
				destResponseObjectBuffer.append(MapSerializer
						.serialize((Map) destResponseObject));
			} else {
				destResponseObjectBuffer.append(MultiLanguageResourceBundle
						.getInstance().getString(
								"Session.toString.unsupportObjectType"));
			}
			buf.append(MultiLanguageResourceBundle.getInstance().getString(
					"Session.toString.destResObj",
					new String[] { destResponseObject.getClass().toString(),
							destResponseObjectBuffer.toString() }));
			buf.append(NEW_LINE);
		}

		// if (responseMessageBean != null) {
		// buf.append("responseMessageBean = ");
		// buf.append(NEW_LINE);
		// buf.append(responseMessageBean.toString());
		// buf.append(NEW_LINE);
		// }
		if (sourceResponseMessage != null) {
			// buf.append("sourceResponseMessage[" +
			// sourceResponseMessage.length
			// + "] = ");
			// buf.append(NEW_LINE);
			// buf.append(CodeUtil.Bytes2FormattedText(sourceResponseMessage));
			buf
					.append(MultiLanguageResourceBundle
							.getInstance()
							.getString(
									"Session.toString.sourceResMsg",
									new String[] {
											"" + sourceResponseMessage.length,
											CodeUtil
													.Bytes2FormattedText(sourceResponseMessage) }));
			buf.append(NEW_LINE);
		}

		if (null != sourceResponseObject) {
			StringBuffer sourceResponseObjectBuffer = new StringBuffer();
			if (sourceResponseObject instanceof MessageBean) {
				sourceResponseObjectBuffer
						.append(((MessageBean) sourceResponseObject).toString());
			} else if (sourceResponseObject instanceof Map) {
				// ByteArrayOutputStream out = new ByteArrayOutputStream();
				// try {
				// MapUtil.printMap((Map) sourceResponseObject, out);
				// sourceResponseObjectBuffer.append(out.toString());
				// } finally {
				// try {
				// out.close();
				// } catch (IOException e) {
				// // e.printStackTrace();
				// }
				// }
				sourceResponseObjectBuffer.append(MapSerializer
						.serialize((Map) sourceResponseObject));
			} else {
				sourceResponseObjectBuffer.append(MultiLanguageResourceBundle
						.getInstance().getString(
								"Session.toString.unsupportObjectType"));
			}
			buf.append(MultiLanguageResourceBundle.getInstance().getString(
					"Session.toString.sourceResObj",
					new String[] { sourceResponseObject.getClass().toString(),
							sourceResponseObjectBuffer.toString() }));
			buf.append(NEW_LINE);
		}

		if (null != errorBean) {
			buf.append(MultiLanguageResourceBundle.getInstance().getString(
					"Session.toString.errorBean",
					new String[] { errorBean.getClass().toString(),
							errorBean.toString() }));
			buf.append(NEW_LINE);
		}

		// buf.append("------------------  End  At ");
		// buf.append(format.format(new Date(endTime)));
		// buf.append(" ------------------");
		buf.append(MultiLanguageResourceBundle.getInstance().getString(
				"Session.toString.3",
				new String[] { format.format(new Date(endTime)) }));
		buf.append(NEW_LINE);

		return buf.toString();
	}

	public String toString4TimeDebugRecord() {

		StringBuffer buf = new StringBuffer(10240);
		buf.append(NEW_LINE);
		buf.append("------------------ Start At ");
		buf.append(format.format(new Date(startTime)));
		buf.append(" ------------------");

		buf.append(NEW_LINE);

		if (null != id) {
			buf.append("id = ");
			buf.append(id);
			buf.append(NEW_LINE);
		}
		buf.append("externalSerialNum = ");
		buf.append(null == externalSerialNum ? "" : externalSerialNum);
		buf.append(NEW_LINE);

		buf.append("state = ");
		buf.append(getDescriptionByState(state));
		buf.append(NEW_LINE);

		buf.append("type = ");
		buf.append(getDescriptionByType(type));
		buf.append(NEW_LINE);

		buf.append("startTime = ");
		buf.append(new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss SSS")
				.format(new Date(startTime)));
		buf.append(NEW_LINE);

		buf.append("endTime = ");
		buf.append(SessionManager.FORMAT.format(new Date(endTime)));
		buf.append(NEW_LINE);

		buf.append("requestSentTime = ");
		if (requestSentTime != 0) {
			buf.append(SessionManager.FORMAT.format(new Date(requestSentTime)));
		} else {
			buf.append("null");
		}
		buf.append(NEW_LINE);

		buf.append("responseArrivedTime = ");
		if (responseArrivedTime != 0) {
			buf.append(SessionManager.FORMAT.format(new Date(
					responseArrivedTime)));
		} else {
			buf.append("null");
		}
		buf.append(NEW_LINE);

		long totalSpendTime = (endNanoTime - startNanoTime) / 1000000;
		long requestSendWaste = 0;
		if (0 != requestSentNanoTime) {
			requestSendWaste = (requestSentNanoTime - timeToSendReqNanoTime) / 1000000;
		}
		long responseSendWaste = 0;
		if (0 != responseSentNanoTime) {
			responseSendWaste = (responseSentNanoTime - timeToSendResNanoTime) / 1000000;
		}
		long destSpendTime = 0;
		if (0 != responseArrivedNanoTime) {
			destSpendTime = (responseArrivedNanoTime - requestSentNanoTime) / 1000000;
		} else {
			if (0 != requestSentNanoTime) {
				destSpendTime = (endNanoTime - requestSentNanoTime) / 1000000;
			} 
		}
		long gatewaySpendTime = totalSpendTime - destSpendTime;
		if (requestSendWaste > 0) {
			gatewaySpendTime -= requestSendWaste;
		}
		if (responseSendWaste > 0) {
			gatewaySpendTime -= responseSendWaste;
		}
		buf.append("totalSpendTime = ");
		buf.append(totalSpendTime);
		buf.append(" ms");
		buf.append(NEW_LINE);

		buf.append("gatewaySpendTime = ");
		buf.append(gatewaySpendTime);
		buf.append(" ms");
		buf.append(NEW_LINE);

		buf.append("sessionCreateSpendTime = ");
		buf.append((sessionCreateTime - startNanoTime) / 1000000);
		buf.append(" ms");
		buf.append(NEW_LINE);

		if (requestSendWaste >= 0) {
			buf.append("requestSendSpendTime = ");
			buf.append(requestSendWaste);
			buf.append(" ms");
			buf.append(NEW_LINE);
		}

		if (destSpendTime >= 0) {
			buf.append("destSpendTime = ");
			buf.append(destSpendTime);
			buf.append(" ms");
			buf.append(NEW_LINE);
		}

		if (responseSendWaste >= 0) {
			buf.append("responseSendSpendTime = ");
			buf.append(responseSendWaste);
			buf.append(" ms");
			buf.append(NEW_LINE);
		}
		if (reqMsgRecognizeSpendTime >= 0) {
			buf.append("requestMessageRecognizeTime = "
					+ reqMsgRecognizeSpendTime + " ms");
			buf.append(NEW_LINE);
		}

		if (resMsgRecognizeSpendTime >= 0) {
			buf.append("responseMessageRecognizeTime = "
					+ resMsgRecognizeSpendTime + " ms");
			buf.append(NEW_LINE);
		}

		if (reqParseSpendTime >= 0) {
			buf.append("requestParseTime = " + reqParseSpendTime + " ms");
			buf.append(NEW_LINE);
		}
		if (reqMappingSpendTime >= 0) {
			buf.append("requestMappingTime = " + reqMappingSpendTime + " ms");
			buf.append(NEW_LINE);
		}
		if (reqPackSpendTime >= 0) {
			buf.append("requestPackTime = " + reqPackSpendTime + " ms");
			buf.append(NEW_LINE);
		}
		if (resParseSpendTime >= 0) {
			buf.append("responseParseTime = " + resParseSpendTime + " ms");
			buf.append(NEW_LINE);
		}
		if (resMappingSpendTime >= 0) {
			buf.append("responseMappingTime = " + resMappingSpendTime + " ms");
			buf.append(NEW_LINE);
		}
		if (resPackSpendTime >= 0) {
			buf.append("responsePackTime = " + resPackSpendTime + " ms");
			buf.append(NEW_LINE);
		}

		if (null != processor) {
			buf.append("processor[id = ");
			buf.append(processor.getId());
			buf.append(", type = ");
			buf.append(Processor.getTextByType(processor.getType()));
			buf.append("]");
			buf.append(NEW_LINE);
		}

		if (!STATE_SUCCESS.equals(state)) {
			if (null != errorMessage) {
				buf.append("errorMessage[" + errorMessage.length() + "] = ");
				buf.append(errorMessage);
				buf.append(NEW_LINE);
			}
			if (exception != null) {
				buf.append("exception = ");
				buf.append(ExceptionUtil.getExceptionDetail(exception));
				buf.append(NEW_LINE);
			}
		}

		buf.append("------------------  End  At ");
		buf.append(format.format(new Date(endTime)));
		buf.append(" ------------------");
		buf.append(NEW_LINE);
		return buf.toString();

	}

	public String toString4ExceptionManager() {
		StringBuffer buf = new StringBuffer(1024);
		buf.append(NEW_LINE);
		buf.append("##");
		if (null != id) {
			buf.append(id);
		}
		buf.append("|");
		buf.append(null == externalSerialNum ? "" : externalSerialNum);
		buf.append("|");
		buf.append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
				.format(new Date(startTime)));
		buf.append("|");
		buf.append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
				.format(new Date(endTime)));
		buf.append("|");
		if (null != errorType) {
			buf.append(errorType);
		}
		buf.append(NEW_LINE);

		buf.append("##source request message:");
		buf.append(NEW_LINE);
		buf.append(new String(CodeUtil
				.BytetoHex(null == sourceRequestMessage ? new byte[0]
						: sourceRequestMessage)));
		buf.append(NEW_LINE);
		buf.append("##source response message:");
		buf.append(NEW_LINE);
		buf.append(new String(CodeUtil
				.BytetoHex(null == sourceResponseMessage ? new byte[0]
						: sourceResponseMessage)));
		buf.append(NEW_LINE);

		buf.append("##dest request message:");
		buf.append(NEW_LINE);
		buf.append(new String(CodeUtil
				.BytetoHex(null == destRequestMessage ? new byte[0]
						: destRequestMessage)));
		buf.append(NEW_LINE);
		buf.append("##dest response message:");
		buf.append(NEW_LINE);
		buf.append(new String(CodeUtil
				.BytetoHex(null == destResponseMessage ? new byte[0]
						: destResponseMessage)));
		buf.append(NEW_LINE);

		buf.append("##exception:");
		buf.append(NEW_LINE);
		if (null != exception) {
			ByteArrayOutputStream exceptionOutputStream = null;
			PrintStream ps = null;
			try {
				exceptionOutputStream = new ByteArrayOutputStream(512);
				ps = new PrintStream(exceptionOutputStream);
				exception.printStackTrace(ps);
				ps.flush();
				buf.append(exceptionOutputStream.toString());
				buf.append(NEW_LINE);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					ps.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		return buf.toString();
	}

	public String toString4ShieldSensitiveInfo() {
		StringBuffer buf = new StringBuffer(10240);
		buf.append(NEW_LINE);
		buf.append(MultiLanguageResourceBundle.getInstance().getString(
				"Session.toString.1",
				new String[] { format.format(new Date(startTime)) }));

		buf.append(NEW_LINE);

		if (null != id) {
			buf.append(MultiLanguageResourceBundle.getInstance().getString(
					"Session.toString.id", new String[] { id }));
			buf.append(NEW_LINE);
		}

		buf.append(MultiLanguageResourceBundle.getInstance().getString(
				"Session.toString.externalSerialNumber",
				new String[] { null == externalSerialNum ? ""
						: externalSerialNum }));
		buf.append(NEW_LINE);
		SimpleDateFormat format = new SimpleDateFormat(
				"yyyy-MM-dd  HH:mm:ss SSS");
		buf
				.append(MultiLanguageResourceBundle
						.getInstance()
						.getString(
								"Session.toString.2",
								new String[] {
										getDescriptionByState(state),
										getDescriptionByType(type),
										"" + timeout,
										(null == processor ? -1 : processor
												.getTimeout())
												+ "",
										format.format(new Date(startTime)),
										format.format(new Date(endTime)),
										format.format(new Date(lastAliveTime)),
										String
												.valueOf((endNanoTime - startNanoTime) / 1000000) }));
		buf.append(NEW_LINE);

		if (null != processor) {
			buf.append(MultiLanguageResourceBundle.getInstance().getString(
					"Session.toString.processor",
					new String[] { processor.getId(),
							Processor.getTextByType(processor.getType()) }));
			buf.append(NEW_LINE);
		}

		if (!STATE_SUCCESS.equals(state)) {
			if (null != errorMessage) {
				buf.append(MultiLanguageResourceBundle.getInstance()
						.getString(
								"Session.toString.errorMessage",
								new String[] { "" + errorMessage.length(),
										errorMessage }));
				buf.append(NEW_LINE);
			}
			if (exception != null) {
				buf.append(MultiLanguageResourceBundle.getInstance().getString(
						"Session.toString.exception",
						new String[] { ExceptionUtil
								.getExceptionDetail(exception) }));
				buf.append(NEW_LINE);
			}
		}
		if (sourceChannel != null) {
			buf.append(MultiLanguageResourceBundle.getInstance().getString(
					"Session.toString.sourceChannel",
					new String[] { sourceChannel.toString() }));
			buf.append(NEW_LINE);
		}

		if (source != null) {
			buf.append(MultiLanguageResourceBundle.getInstance().getString(
					"Session.toString.source",
					new String[] { source.toString() }));
			buf.append(NEW_LINE);
		}

		if (destChannel != null) {
			buf.append(MultiLanguageResourceBundle.getInstance().getString(
					"Session.toString.destChannel",
					new String[] { destChannel.toString() }));
			buf.append(NEW_LINE);
		}

		if (destSource != null) {
			buf.append(MultiLanguageResourceBundle.getInstance().getString(
					"Session.toString.destSource",
					new String[] { destSource.toString() }));
			buf.append(NEW_LINE);
		}

		if (null != sourceRequestObject) {
			StringBuffer sourceRequestObjectBuffer = new StringBuffer();
			if (sourceRequestObject instanceof MessageBean) {
				MessageBean mb = (MessageBean) sourceRequestObject;
				mb.shieldSensitiveFields();
				sourceRequestObjectBuffer.append(mb.toString());

				MessagePacker packer = new MessagePacker();
				packer.setMessage(MessageMetadataManager.getMessageByClass(mb
						.getClass().getName()));
				packer.setMessageBean(mb);
				sourceRequestMessage = packer.pack();
				mb.unshieldSensitiveFields();
			} else if (sourceRequestObject instanceof Map) {
				String mapXml = SensitiveInfoFilter.filtSensitiveInfo(
						MapSerializer.serialize((Map) sourceRequestObject),
						CommGateway.getSensitiveFields(), CommGateway
								.getSensitiveReplaceChar());
				if (null != mapXml) {
					sourceRequestObjectBuffer.append(mapXml);
					if (null != processor) {
						try {
							sourceRequestMessage = mapXml.getBytes(processor
									.getSourceMapCharset());
						} catch (UnsupportedEncodingException e) {
							e.printStackTrace();
						}
					}
				}
			} else {
				sourceRequestObjectBuffer.append(MultiLanguageResourceBundle
						.getInstance().getString(
								"Session.toString.unsupportObjectType"));
			}
			buf.append(MultiLanguageResourceBundle.getInstance().getString(
					"Session.toString.sourceReqObj",
					new String[] { sourceRequestObject.getClass().toString(),
							sourceRequestObjectBuffer.toString() }));
			buf.append(NEW_LINE);
		}

		if (sourceRequestMessage != null) {
			buf
					.append(MultiLanguageResourceBundle
							.getInstance()
							.getString(
									"Session.toString.sourceReqMsg",
									new String[] {
											"" + sourceRequestMessage.length,
											CodeUtil
													.Bytes2FormattedText(sourceRequestMessage) }));
			buf.append(NEW_LINE);
		}

		if (null != destRequestObject) {
			StringBuffer destRequestObjectBuffer = new StringBuffer();
			if (destRequestObject instanceof MessageBean) {
				MessageBean mb = (MessageBean) destRequestObject;
				mb.shieldSensitiveFields();
				destRequestObjectBuffer.append(mb.toString());

				MessagePacker packer = new MessagePacker();
				packer.setMessage(MessageMetadataManager.getMessageByClass(mb
						.getClass().getName()));
				packer.setMessageBean(mb);
				destRequestMessage = packer.pack();
				mb.unshieldSensitiveFields();
			} else if (destRequestObject instanceof Map) {
				String mapXml = SensitiveInfoFilter.filtSensitiveInfo(
						MapSerializer.serialize((Map) destRequestObject),
						CommGateway.getSensitiveFields(), CommGateway
								.getSensitiveReplaceChar());
				if (null != mapXml) {
					destRequestObjectBuffer.append(mapXml);
					if (null != processor) {
						try {
							destRequestMessage = mapXml.getBytes(processor
									.getDestMapCharset());
						} catch (UnsupportedEncodingException e) {
							e.printStackTrace();
						}
					}
				}
			} else {
				destRequestObjectBuffer.append(MultiLanguageResourceBundle
						.getInstance().getString(
								"Session.toString.unsupportObjectType"));
			}
			buf.append(MultiLanguageResourceBundle.getInstance().getString(
					"Session.toString.destReqObj",
					new String[] { destRequestObject.getClass().toString(),
							destRequestObjectBuffer.toString() }));
			buf.append(NEW_LINE);
		}

		if (destRequestMessage != null) {
			buf
					.append(MultiLanguageResourceBundle
							.getInstance()
							.getString(
									"Session.toString.destReqMsg",
									new String[] {
											"" + destRequestMessage.length,
											CodeUtil
													.Bytes2FormattedText(destRequestMessage) }));
			buf.append(NEW_LINE);
		}

		if (null != destResponseObject) {
			StringBuffer destResponseObjectBuffer = new StringBuffer();
			if (destResponseObject instanceof MessageBean) {
				MessageBean mb = (MessageBean) destResponseObject;
				mb.shieldSensitiveFields();
				destResponseObjectBuffer.append(mb.toString());

				MessagePacker packer = new MessagePacker();
				packer.setMessage(MessageMetadataManager.getMessageByClass(mb
						.getClass().getName()));
				packer.setMessageBean(mb);
				destResponseMessage = packer.pack();

				mb.unshieldSensitiveFields();
			} else if (destResponseObject instanceof Map) {
				String mapXml = SensitiveInfoFilter.filtSensitiveInfo(
						MapSerializer.serialize((Map) destResponseObject),
						CommGateway.getSensitiveFields(), CommGateway
								.getSensitiveReplaceChar());
				if (null != mapXml) {
					destResponseObjectBuffer.append(mapXml);
					if (null != processor) {
						try {
							destResponseMessage = mapXml.getBytes(processor
									.getDestMapCharset());
						} catch (UnsupportedEncodingException e) {
							e.printStackTrace();
						}
					}
				}
			} else {
				destResponseObjectBuffer.append(MultiLanguageResourceBundle
						.getInstance().getString(
								"Session.toString.unsupportObjectType"));
			}
			buf.append(MultiLanguageResourceBundle.getInstance().getString(
					"Session.toString.destResObj",
					new String[] { destResponseObject.getClass().toString(),
							destResponseObjectBuffer.toString() }));
			buf.append(NEW_LINE);
		}

		if (destResponseMessage != null) {
			buf
					.append(MultiLanguageResourceBundle
							.getInstance()
							.getString(
									"Session.toString.destResMsg",
									new String[] {
											"" + destResponseMessage.length,
											CodeUtil
													.Bytes2FormattedText(destResponseMessage) }));
			buf.append(NEW_LINE);
		}

		if (null != sourceResponseObject) {
			StringBuffer sourceResponseObjectBuffer = new StringBuffer();
			if (sourceResponseObject instanceof MessageBean) {
				MessageBean mb = (MessageBean) sourceResponseObject;
				mb.shieldSensitiveFields();
				sourceResponseObjectBuffer.append(mb.toString());

				MessagePacker packer = new MessagePacker();
				packer.setMessage(MessageMetadataManager.getMessageByClass(mb
						.getClass().getName()));
				packer.setMessageBean(mb);

				sourceResponseMessage = packer.pack();
				mb.unshieldSensitiveFields();
			} else if (sourceResponseObject instanceof Map) {
				String mapXml = SensitiveInfoFilter.filtSensitiveInfo(
						MapSerializer.serialize((Map) sourceResponseObject),
						CommGateway.getSensitiveFields(), CommGateway
								.getSensitiveReplaceChar());
				if (null != mapXml) {
					sourceResponseObjectBuffer.append(mapXml);
					if (null != processor) {
						try {
							sourceResponseMessage = mapXml.getBytes(processor
									.getSourceMapCharset());
						} catch (UnsupportedEncodingException e) {
							e.printStackTrace();
						}
					}
				}
			} else {
				sourceResponseObjectBuffer.append(MultiLanguageResourceBundle
						.getInstance().getString(
								"Session.toString.unsupportObjectType"));
			}
			buf.append(MultiLanguageResourceBundle.getInstance().getString(
					"Session.toString.sourceResObj",
					new String[] { sourceResponseObject.getClass().toString(),
							sourceResponseObjectBuffer.toString() }));
			buf.append(NEW_LINE);
		}

		if (sourceResponseMessage != null) {
			buf
					.append(MultiLanguageResourceBundle
							.getInstance()
							.getString(
									"Session.toString.sourceResMsg",
									new String[] {
											"" + sourceResponseMessage.length,
											CodeUtil
													.Bytes2FormattedText(sourceResponseMessage) }));
			buf.append(NEW_LINE);
		}

		if (null != errorBean) {
			MessageBean mb = (MessageBean) errorBean;
			mb.shieldSensitiveFields();
			buf.append(MultiLanguageResourceBundle.getInstance().getString(
					"Session.toString.errorBean",
					new String[] { errorBean.getClass().toString(),
							mb.toString() }));
			mb.unshieldSensitiveFields();
			buf.append(NEW_LINE);
		}

		// buf.append("------------------  End  At ");
		// buf.append(format.format(new Date(endTime)));
		// buf.append(" ------------------");
		buf.append(MultiLanguageResourceBundle.getInstance().getString(
				"Session.toString.3",
				new String[] { format.format(new Date(endTime)) }));
		buf.append(NEW_LINE);

		return buf.toString();
	}

	/**
	 * @return the startTime
	 */
	public long getStartTime() {
		return startTime;
	}

	/**
	 * @param startTime
	 *            the startTime to set
	 */
	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	/**
	 * @return the endTime
	 */
	public long getEndTime() {
		return endTime;
	}

	public long getStartNanoTime() {
		return startNanoTime;
	}

	public void setStartNanoTime(long startNanoTime) {
		this.startNanoTime = startNanoTime;
	}

	/**
	 * @param endTime
	 *            the endTime to set
	 */
	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public long getEndNanoTime() {
		return endNanoTime;
	}

	public void setEndNanoTime(long endNanoTime) {
		this.endNanoTime = endNanoTime;
	}

	/**
	 * @return the eventList
	 */
	public List getEventList() {
		return eventList;
	}

	/**
	 * @param eventList
	 *            the eventList to set
	 */
	public void setEventList(List eventList) {
		this.eventList = eventList;
	}

	/**
	 * @return the sourceChannel
	 */
	public Channel getSourceChannel() {
		return sourceChannel;
	}

	/**
	 * @param sourceChannel
	 *            the sourceChannel to set
	 */
	public void setSourceChannel(Channel sourceChannel) {
		this.sourceChannel = sourceChannel;
	}

	/**
	 * @return the source
	 */
	public Object getSource() {
		return source;
	}

	/**
	 * @param source
	 *            the source to set
	 */
	public void setSource(Object source) {
		this.source = source;
	}

	/**
	 * @return the sourceRequestMessage
	 */
	public byte[] getSourceRequestMessage() {
		return sourceRequestMessage;
	}

	/**
	 * @param sourceRequestMessage
	 *            the sourceRequestMessage to set
	 */
	public void setSourceRequestMessage(byte[] sourceRequestMessage) {
		this.sourceRequestMessage = sourceRequestMessage;
	}

	/**
	 * @return the sourceResponseMessage
	 */
	public byte[] getSourceResponseMessage() {
		return sourceResponseMessage;
	}

	/**
	 * @param sourceResponseMessage
	 *            the sourceResponseMessage to set
	 */
	public void setSourceResponseMessage(byte[] sourceResponseMessage) {
		this.sourceResponseMessage = sourceResponseMessage;
	}

	/**
	 * @return the destChannel
	 */
	public Channel getDestChannel() {
		return destChannel;
	}

	/**
	 * @param destChannel
	 *            the destChannel to set
	 */
	public void setDestChannel(Channel destChannel) {
		this.destChannel = destChannel;
	}

	/**
	 * @return the destSource
	 */
	public Object getDestSource() {
		return destSource;
	}

	/**
	 * @param destSource
	 *            the destSource to set
	 */
	public void setDestSource(Object destSource) {
		this.destSource = destSource;
	}

	/**
	 * @return the destRequestMessage
	 */
	public byte[] getDestRequestMessage() {
		return destRequestMessage;
	}

	/**
	 * @param destRequestMessage
	 *            the destRequestMessage to set
	 */
	public void setDestRequestMessage(byte[] destRequestMessage) {
		this.destRequestMessage = destRequestMessage;
	}

	/**
	 * @return the destResponseMessage
	 */
	public byte[] getDestResponseMessage() {
		return destResponseMessage;
	}

	/**
	 * @param destResponseMessage
	 *            the destResponseMessage to set
	 */
	public void setDestResponseMessage(byte[] destResponseMessage) {
		this.destResponseMessage = destResponseMessage;
	}

	// /**
	// * @return the requestMessageBean
	// */
	// public MessageBean getRequestMessageBean() {
	// return requestMessageBean;
	// }
	//
	// /**
	// * @param requestMessageBean
	// * the requestMessageBean to set
	// */
	// public void setRequestMessageBean(MessageBean requestMessageBean) {
	// this.requestMessageBean = requestMessageBean;
	// }
	//
	// /**
	// * @return the responseMessageBean
	// */
	// public MessageBean getResponseMessageBean() {
	// return responseMessageBean;
	// }
	//
	// /**
	// * @param responseMessageBean
	// * the responseMessageBean to set
	// */
	// public void setResponseMessageBean(MessageBean responseMessageBean) {
	// this.responseMessageBean = responseMessageBean;
	// }

	/**
	 * @return the exception
	 */
	public Exception getException() {
		return exception;
	}

	/**
	 * @param exception
	 *            the exception to set
	 */
	public void setException(Exception exception) {
		this.exception = exception;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public Processor getProcessor() {
		return processor;
	}

	public void setProcessor(Processor processor) {
		this.processor = processor;
	}

	// /**
	// * @return the outboundRule
	// */
	// public OutboundRule getOutboundRule() {
	// return outboundRule;
	// }
	//
	// /**
	// * @param outboundRule
	// * the outboundRule to set
	// */
	// public void setOutboundRule(OutboundRule outboundRule) {
	// this.outboundRule = outboundRule;
	// }
	//
	// /**
	// * @return the inboundRule
	// */
	// public InboundRule getInboundRule() {
	// return inboundRule;
	// }
	//
	// /**
	// * @param inboundRule
	// * the inboundRule to set
	// */
	// public void setInboundRule(InboundRule inboundRule) {
	// this.inboundRule = inboundRule;
	// }
	//
	// public RouteRule getRule() {
	// return rule;
	// }
	//
	// public void setRule(RouteRule rule) {
	// this.rule = rule;
	// }

	public static String getDescriptionByState(String state) {
		if (STATE_SUCCESS.equals(state)) {
			return "Success";
		} else if (STATE_FAILURE.equals(state)) {
			return "Failed";
		} else if (STATE_PROCEED.equals(state)) {
			return "Proceeding";
		} else if (STATE_CANCEL.equals(state)) {
			return "Timeout";
		} else if (STATE_UNFINISHED.equals(state)) {
			return "Closed";
		} else {
			// throw new IllegalArgumentException("Unkown Session State :" +
			// state);
			throw new IllegalArgumentException(MultiLanguageResourceBundle
					.getInstance().getString(
							"Session.getDescriptionByState.unkown",
							new String[] { state }));
		}
	}

	public static String getDescriptionByType(String type) {
		if (TYP_INTERNAL.equals(type)) {
			return "Internal";
		} else if (TYP_EXTERNAL.equals(type)) {
			return "External";
		} else {
			// throw new IllegalArgumentException("Unkown Type :" + type);
			throw new IllegalArgumentException(MultiLanguageResourceBundle
					.getInstance().getString("type.unsupport",
							new String[] { type }));
		}
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getErrorType() {
		return errorType;
	}

	public void setErrorType(String errorType) {
		this.errorType = errorType;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public AbstractJob getJob() {
		return job;
	}

	public void setJob(AbstractJob job) {
		this.job = job;
	}

	public long getLastAliveTime() {
		return lastAliveTime;
	}

	public void setLastAliveTime(long lastAliveTime) {
		this.lastAliveTime = lastAliveTime;
	}

	public long getTimeout() {
		return timeout;
	}

	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}

	public long getRequestSentTime() {
		return requestSentTime;
	}

	public void setRequestSentTime(long requestSentTime) {
		this.requestSentTime = requestSentTime;
	}

	public long getRequestSentNanoTime() {
		return requestSentNanoTime;
	}

	public void setRequestSentNanoTime(long requestSentNanoTime) {
		this.requestSentNanoTime = requestSentNanoTime;
	}

	public long getResponseArrivedTime() {
		return responseArrivedTime;
	}

	public void setResponseArrivedTime(long responseArrivedTime) {
		this.responseArrivedTime = responseArrivedTime;
	}

	public long getResponseArrivedNanoTime() {
		return responseArrivedNanoTime;
	}

	public void setResponseArrivedNanoTime(long responseArrivedNanoTime) {
		this.responseArrivedNanoTime = responseArrivedNanoTime;
	}

	public String getSourceInfo() {
		return sourceInfo;
	}

	public void setSourceInfo(String sourceInfo) {
		this.sourceInfo = sourceInfo;
	}

	public String getDestSourceInfo() {
		return destSourceInfo;
	}

	public void setDestSourceInfo(String destSourceInfo) {
		this.destSourceInfo = destSourceInfo;
	}

	public Object getSourceRequestObject() {
		return sourceRequestObject;
	}

	public void setSourceRequestObject(Object sourceRequestObject) {
		this.sourceRequestObject = sourceRequestObject;
	}

	public Object getSourceResponseObject() {
		return sourceResponseObject;
	}

	public void setSourceResponseObject(Object sourceResponseObject) {
		this.sourceResponseObject = sourceResponseObject;
	}

	public Object getDestRequestObject() {
		return destRequestObject;
	}

	public void setDestRequestObject(Object destRequestObject) {
		this.destRequestObject = destRequestObject;
	}

	public Object getDestResponseObject() {
		return destResponseObject;
	}

	public void setDestResponseObject(Object destResponseObject) {
		this.destResponseObject = destResponseObject;
	}

	public Object getErrorBean() {
		return errorBean;
	}

	public void setErrorBean(Object errorBean) {
		this.errorBean = errorBean;
	}

	public long getTimeToSendReqNanoTime() {
		return timeToSendReqNanoTime;
	}

	public void setTimeToSendReqNanoTime(long timeToSendReqNanoTime) {
		this.timeToSendReqNanoTime = timeToSendReqNanoTime;
	}

	public long getTimeToSendResNanoTime() {
		return timeToSendResNanoTime;
	}

	public void setTimeToSendResNanoTime(long timeToSendResNanoTime) {
		this.timeToSendResNanoTime = timeToSendResNanoTime;
	}

	public long getResponseSentNanoTime() {
		return responseSentNanoTime;
	}

	public void setResponseSentNanoTime(long responseSentNanoTime) {
		this.responseSentNanoTime = responseSentNanoTime;
	}

	public String getExternalSerialNum() {
		return externalSerialNum;
	}

	public void setExternalSerialNum(String externalSerialNum) {
		this.externalSerialNum = externalSerialNum;
	}

	public long reqParseSpendTime = -1;
	public long reqMappingSpendTime = -1;
	public long reqPackSpendTime = -1;
	public long resParseSpendTime = -1;
	public long resMappingSpendTime = -1;
	public long resPackSpendTime = -1;
	public long reqMsgRecognizeSpendTime = -1;
	public long resMsgRecognizeSpendTime = -1;
	public long sessionCreateTime;
}
