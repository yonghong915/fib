package com.fib.upp.modules.ccbc;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fib.commons.util.SortHashMap;

import cn.hutool.core.util.HexUtil;

/**
 * 城银清接收报文处理入口
 * 
 * @author fangyh
 * @version 1.0
 * @date 2022-05-13 10:45:09
 */
public class CCBCMsgCtrler {
	private static final Logger LOGGER = LoggerFactory.getLogger(CCBCMsgCtrler.class);

	public static void main(String[] args) {
		String msgHead = "01313600000001323500000004202204131234321234567801111111111111111120220513SET00101066666666666666666666666666666666666666666600000000000000000000000000000000";
		String msgBody = "20022123136986709562003212313698670957";
		// 发起行行号 313698670956
		LOGGER.info("{}", "313698670956".getBytes().length);

		LOGGER.info("{}", msgHead.getBytes().length);
		String msgContent = msgHead + msgBody;
		LOGGER.info("{}", "msgContent=" + msgContent);

		String srcHex = HexUtil.encodeHexStr(msgContent, StandardCharsets.UTF_8);
		LOGGER.info("{}", "srcHex=" + srcHex);

		SortHashMap<String, Integer> headDef = new SortHashMap<>(32);
		headDef.put("bizType", 2);
		headDef.put("sndOrgCode", 12);
		headDef.put("rvcOrgCode", 12);
		headDef.put("sndDt", 8);
		headDef.put("sndTm", 6);

		headDef.put("revr", 8);
		headDef.put("chal", 2);
		headDef.put("usrReser", 16);
		headDef.put("clearDt", 8);
		headDef.put("msgTpCd", 6);
		headDef.put("bizTpCd", 2);
		headDef.put("TransferTp", 1);
		headDef.put("msgIdx", 42);
		headDef.put("reser", 32);

		Map<String, String> headField = new HashMap<>();
		int startIdx = 0;
		int endIdx = 0;

		int headLen = headDef.size();
		for (int i = 0; i < headLen; i++) {
			String key = headDef.getKey(i);
			int len = headDef.get(i).intValue() * 2;

			endIdx += len;
			String val = srcHex.substring(startIdx, endIdx);
			headField.put(key, HexUtil.decodeHexStr(val, StandardCharsets.UTF_8));
			startIdx += len;
		}
		LOGGER.info("{}", "headField=" + headField);
		Map<String, String> bodyField = new HashMap<>();
		srcHex = srcHex.substring(endIdx);

		int fieldCdLen = 4 * 2;
		int lenFlgLen = 1 * 2;
		startIdx = 0;
		endIdx = 0;
		do {
			endIdx += fieldCdLen;
			String fieldCd = HexUtil.decodeHexStr(srcHex.substring(startIdx, endIdx), StandardCharsets.UTF_8);

			LOGGER.info("{}", "fieldCd=" + fieldCd);
			startIdx += fieldCdLen;

			endIdx += lenFlgLen;
			String lenFlg = HexUtil.decodeHexStr(srcHex.substring(startIdx, endIdx), StandardCharsets.UTF_8);

			LOGGER.info("{}", "lenFlg=" + lenFlg);
			startIdx += lenFlgLen;

			endIdx += Integer.parseInt(lenFlg) * 2;
			String dataLen = HexUtil.decodeHexStr(srcHex.substring(startIdx, endIdx), StandardCharsets.UTF_8);
			LOGGER.info("{}", "dataLen=" + dataLen);
			startIdx += Integer.parseInt(lenFlg) * 2;

			endIdx += Integer.parseInt(dataLen) * 2;
			String data = HexUtil.decodeHexStr(srcHex.substring(startIdx, endIdx), StandardCharsets.UTF_8);
			LOGGER.info("{}", "data=" + data);
			bodyField.put(fieldCd, data);

			startIdx += Integer.parseInt(dataLen) * 2;
		} while (srcHex.length() != endIdx);

		LOGGER.info("{}", "bodyField=" + bodyField);
		LOGGER.info("{}", "aaaaa= " + "12".substring(2));
	}

}
