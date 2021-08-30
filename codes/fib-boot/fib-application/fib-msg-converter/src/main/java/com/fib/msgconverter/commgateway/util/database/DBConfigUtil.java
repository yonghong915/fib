package com.fib.msgconverter.commgateway.util.database;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import com.fib.msgconverter.commgateway.channel.config.recognizer.RecognizerConfig;
import com.fib.msgconverter.commgateway.channel.nio.config.FilterConfig;
import com.fib.msgconverter.commgateway.channel.nio.config.ReaderConfig;
import com.fib.msgconverter.commgateway.channel.nio.config.WriterConfig;
import com.fib.msgconverter.commgateway.config.base.DynamicObjectConfig;
import com.fib.msgconverter.commgateway.dao.filter.dao.Filter;
import com.fib.msgconverter.commgateway.dao.filter.dao.FilterDAO;
import com.fib.msgconverter.commgateway.dao.parameter.dao.Parameter;
import com.fib.msgconverter.commgateway.dao.parameter.dao.ParameterDAO;
import com.fib.msgconverter.commgateway.dao.readerwriter.dao.ReaderWriter;
import com.fib.msgconverter.commgateway.dao.readerwriter.dao.ReaderWriterDAO;
import com.fib.msgconverter.commgateway.dao.readerwriterfilterrelation.dao.ReaderWriterFilterRelation;
import com.fib.msgconverter.commgateway.dao.readerwriterfilterrelation.dao.ReaderWriterFilterRelationDAO;
import com.fib.msgconverter.commgateway.dao.readerwriterparameterrelation.dao.ReaderWriterParameterRelation;
import com.fib.msgconverter.commgateway.dao.readerwriterparameterrelation.dao.ReaderWriterParameterRelationDAO;
import com.fib.msgconverter.commgateway.dao.recognizer.dao.Recognizer;
import com.fib.msgconverter.commgateway.dao.recognizer.dao.RecognizerDAO;
import com.fib.msgconverter.commgateway.dao.recognizerparameterrelation.dao.RecognizerParameterRelation;
import com.fib.msgconverter.commgateway.dao.recognizerparameterrelation.dao.RecognizerParameterRelationDAO;
import com.fib.msgconverter.commgateway.dao.recognizerrelation.dao.RecognizerRelation;
import com.fib.msgconverter.commgateway.dao.recognizerrelation.dao.RecognizerRelationDAO;

public class DBConfigUtil {
	public static final String CLASS_COMPONENT = "com.fib.msgconverter.commgateway.channel.message.recognizer.impl.CompositeRecognizer";
	public static final String CLASS_INDEX = "com.fib.msgconverter.commgateway.channel.message.recognizer.impl.IndexRecognizer";
	public static final String CLASS_SEPARATOR = "com.fib.msgconverter.commgateway.channel.message.recognizer.impl.SeparatorRecognizer";
	public static final String CLASS_SEPARATOR_MESSAGE = "com.fib.msgconverter.commgateway.channel.message.recognizer.impl.SeparatorMessageRecognizer";
	public static final String CLASS_XPATH = "com.fib.msgconverter.commgateway.channel.message.recognizer.impl.XPathRecognizer";
	public static final String CLASS_SCRIPT = "com.fib.msgconverter.commgateway.channel.message.recognizer.impl.ScriptRecognizer";
	public static final String CLASS_8583 = "com.fib.msgconverter.commgateway.channel.message.recognizer.impl.ISO8583MessageRecognizer";

	public static final String RECOGNIZER_TYPE_USER_DEFINED = "USER_DEFINED";
	public static final String DB_RECOGNIZER_USER_DEFINED = "0";
	public static final String RECOGNIZER_TYPE_INDEX = "INDEX";
	public static final String DB_RECOGNIZER_TYPE_INDEX = "1";
	public static final String RECOGNIZER_TYPE_SEPARATOR = "SEPARATOR";
	public static final String DB_RECOGNIZER_TYPE_SEPARATOR = "2";
	public static final String RECOGNIZER_TYPE_XPATH = "XPATH";
	public static final String DB_RECOGNIZER_TYPE_XPATH = "3";
	public static final String RECOGNIZER_TYPE_SCRIPT = "SCRIPT";
	public static final String DB_RECOGNIZER_TYPE_SCRIPT = "4";
	public static final String RECOGNIZER_TYPE_8583 = "8583";
	public static final String DB_RECOGNIZER_TYPE_8583 = "5";
	public static final String RECOGNIZER_TYPE_SEPARATOR_MESSAGE = "SEPARATOR_MESSAGE";
	public static final String DB_RECOGNIZER_TYPE_SEPARATOR_MESSAGE = "6";
	public static final String RECOGNIZER_TYPE_COMPOSITE = "COMPOSITE";
	public static final String DB_RECOGNIZER_TYPE_COMPOSITE = "9";

	public static RecognizerConfig transformRecognizerConfig(
			String recognizerId, Connection conn) {
		RecognizerDAO recognizerDao = new RecognizerDAO();
		recognizerDao.setConnection(conn);
		Recognizer recognizerDto = recognizerDao.selectByPK(recognizerId);

		RecognizerConfig config = new RecognizerConfig();
		config.setId("" + recognizerDto.getId());
		config.setClassName(recognizerDto.getClassName());
		config.setType(RECOGNIZER_TYPE_USER_DEFINED);

		RecognizerParameterRelationDAO rprDao = new RecognizerParameterRelationDAO();
		rprDao.setConnection(conn);

		List<RecognizerParameterRelation> rprList = rprDao
				.getAllParameter4Recognizer(recognizerDto.getId());
		ParameterDAO paramDao = new ParameterDAO();
		paramDao.setConnection(conn);
		if (null != rprList && 0 < rprList.size()) {
			Parameter param = null;
			for (int i = 0; i < rprList.size(); i++) {
				param = paramDao.selectByPK(rprList.get(i).getParameterId());
				config.setParameter(param.getParameterName(), param
						.getParameterValue());
			}
		}

		if (null == config.getClassName()) {
			if (DB_RECOGNIZER_TYPE_COMPOSITE.equalsIgnoreCase(config.getType())) {
				RecognizerRelationDAO recognizerRelationDao = new RecognizerRelationDAO();
				recognizerRelationDao.setConnection(conn);

				List<RecognizerRelation> rrList = recognizerRelationDao
						.getAllSubRecognizer4Recognizer(recognizerDto.getId());
				if (null != rrList && 0 < rrList.size()) {
					List componentList = new ArrayList();
					for (int i = 0; i < rrList.size(); i++) {
						componentList.add(transformRecognizerConfig(rrList.get(
								i).getSubRecognizerId(), conn));
					}
					config.setComponentList(componentList);
				}
				config.setType(RECOGNIZER_TYPE_COMPOSITE);
				config.setClassName(CLASS_COMPONENT);
			} else if (DB_RECOGNIZER_TYPE_INDEX.equalsIgnoreCase(config
					.getType())) {
				config.setType(RECOGNIZER_TYPE_INDEX);
				config.setClassName(CLASS_INDEX);
			} else if (DB_RECOGNIZER_TYPE_SEPARATOR.equalsIgnoreCase(config
					.getType())) {
				config.setType(RECOGNIZER_TYPE_SEPARATOR);
				config.setClassName(CLASS_SEPARATOR);
			} else if (DB_RECOGNIZER_TYPE_XPATH.equalsIgnoreCase(config
					.getType())) {
				config.setType(RECOGNIZER_TYPE_XPATH);
				config.setClassName(CLASS_XPATH);
			} else if (DB_RECOGNIZER_TYPE_SCRIPT.equalsIgnoreCase(config
					.getType())) {
				config.setType(RECOGNIZER_TYPE_SCRIPT);
				config.setClassName(CLASS_SCRIPT);
			} else if (DB_RECOGNIZER_TYPE_8583.equalsIgnoreCase(config
					.getType())) {
				config.setType(RECOGNIZER_TYPE_8583);
				config.setClassName(CLASS_8583);
			} else if (DB_RECOGNIZER_TYPE_SEPARATOR_MESSAGE
					.equalsIgnoreCase(config.getType())) {
				config.setType(RECOGNIZER_TYPE_SEPARATOR_MESSAGE);
				config.setClassName(CLASS_SEPARATOR_MESSAGE);
			} else if (DB_RECOGNIZER_USER_DEFINED.equals(config.getType())) {
				throw new RuntimeException(
						"recognizer type is 'user_defined'!class_name must not be null!");
			}
		}
		return config;
	}

	public static DynamicObjectConfig transformReaderOrWriterConfig(String id,
			boolean isReader, Connection conn) {
		ReaderWriterDAO rwDao = new ReaderWriterDAO();
		rwDao.setConnection(conn);

		ReaderWriter rw = rwDao.selectByPK(id);
		DynamicObjectConfig readerOrWriterConfig = null;
		if (isReader) {
			readerOrWriterConfig = new ReaderConfig();
		} else {
			readerOrWriterConfig = new WriterConfig();
		}
		readerOrWriterConfig.setClassName(rw.getClassName());

		ReaderWriterParameterRelationDAO rwprDao = new ReaderWriterParameterRelationDAO();
		rwprDao.setConnection(conn);

		List<ReaderWriterParameterRelation> rwprList = rwprDao
				.getAllParameter4ReaderOrWriter(rw.getId());
		ParameterDAO paramDao = new ParameterDAO();
		paramDao.setConnection(conn);
		Parameter param = null;
		for (int i = 0; i < rwprList.size(); i++) {
			param = paramDao.selectByPK(rwprList.get(i).getParameterId());
			readerOrWriterConfig.setParameter(param.getParameterName(), param
					.getParameterValue());
		}

		ReaderWriterFilterRelationDAO rwfrDao = new ReaderWriterFilterRelationDAO();
		rwfrDao.setConnection(conn);

		List<ReaderWriterFilterRelation> rwfrList = rwfrDao
				.getAllFilter4ReaderOrWriter(rw.getId());

		for (int i = 0; i < rwfrList.size(); i++) {
			FilterDAO filterDao = new FilterDAO();
			filterDao.setConnection(conn);

			Filter filter = filterDao.selectByPK(rwfrList.get(i).getFilterId());
			FilterConfig filterConfig = new FilterConfig();
			filterConfig.setClassName(filter.getClassName());

			// FilterParameterRelationDAO fprDao = new
			// FilterParameterRelationDAO();
			// List<FilterParameterRelation> fprList =
			// fprDao.getAllParameter4Filter(filter.getId());
			// for(int i = 0; i < fprList.size(); i++){
			// param = paramDao.selectByPK(fprList.get(i).getParameterId());
			//				
			// }
			if (isReader) {
				((ReaderConfig) readerOrWriterConfig).getFilterConfigList()
						.add(filterConfig);
			} else {
				((WriterConfig) readerOrWriterConfig).getFilterConfigList()
						.add(filterConfig);
			}
		}
		return readerOrWriterConfig;
	}
}
