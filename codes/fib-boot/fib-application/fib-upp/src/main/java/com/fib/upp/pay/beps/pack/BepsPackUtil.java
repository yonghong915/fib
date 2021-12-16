package com.fib.upp.pay.beps.pack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fib.upp.service.IMessagePackRuleService;

/**
 * 
 * @author fangyh
 * @version 1.0
 * @since 1.0
 * @date 2021-01-25
 */

@Component
public class BepsPackUtil {
	@Autowired
	private IMessagePackRuleService messagePackRuleService;

//	private static IMessagePackRuleService messagePackRuleServiceTmp;
//
//	@PostConstruct
//	public void init() {
//		messagePackRuleServiceTmp = messagePackRuleService;
//	}

	private BepsPackUtil() {
		// do nothing
	}

	/**
	 * 人行报文类型与组包规则的映射
	 */
	static Map<String, MessagePackRule> messageTypePackRuleMap = new TreeMap<>();

	public List<List<String>> packPaymentOrder(String messageType, List<String> orderIdList) {
		// 根据小额报文类型获取组包规则
		MessagePackRule packRule = getPackRuleByMessageType(messageType);
		return packPaymentOrder(packRule, orderIdList);
	}

	private List<List<String>> packPaymentOrder(MessagePackRule packRule, List<String> orderIdList) {
//		if (CollUtil.isEmpty(orderIdList)) {
//			throw new CommonException("orderIdList is empty.");
//		}
		// key = packString（分包的标识号）; value = List<OrderId>（每个分包里的订单号）
		Map<String, List<String>> packMap = new HashMap<>();
		orderIdList.stream().forEach((String orderId) -> {
			BepsPackElement packElem = extractPackElement(orderId);
			String packString = packRule.getPackString(packElem);
			List<String> subOrderIdList = packMap.get(packString);
			// 如果这个list尚不存在，则new一个实例，并与分包标识号建立联系
			if (null == subOrderIdList) {
				subOrderIdList = new ArrayList<>();
				packMap.put(packString, subOrderIdList);
			}

			// 允许最大参与者数目控制，如果超过则新组一个包
			while (subOrderIdList.size() >= BepsMessagePackRule.maxPartnerNum) {
				int packSeq = packString.indexOf("pkg") + 3;// 取得当前包序号
				int packNO = Integer.parseInt(packString.substring(packSeq).trim());
				packNO = packNO + 1;// 包序号自增
				packString = packString.substring(0, packSeq) + String.format("%1$-3s", packNO);
				subOrderIdList = packMap.get(packString);// 新分包标识号对应的list
				// 如果这个list尚不存在，则new一个实例，并与分包标识号建立联系
				if (null == subOrderIdList) {
					subOrderIdList = new ArrayList<>();
					packMap.put(packString, subOrderIdList);
				}
			}
			// 将当前订单号加入list
			subOrderIdList.add(orderId);
		});
		return packMap.values().stream().collect(Collectors.toList());
	}

	private BepsPackElement extractPackElement(String orderId) {
		BepsPackElement packEle = new BepsPackElement();
		if ("111".equals(orderId)) {
			packEle.setTransactionType("beps.121.001.01");
			packEle.setSendClearingBank("31317296748");
			packEle.setReceiveClearingBank("323434555334");
			packEle.setReturnLimited("1");
			packEle.setOriginalMessageId("304242444");
			packEle.setBatchId("12");
		} else if ("222".equals(orderId)) {
			packEle.setTransactionType("beps.121.001.01");
			packEle.setSendClearingBank("31317296747");
			packEle.setReceiveClearingBank("323434555333");
			packEle.setReturnLimited("1");
			packEle.setOriginalMessageId("304242444");
			packEle.setBatchId("12");
		} else if ("333".equals(orderId)) {
			packEle.setTransactionType("beps.121.001.01");
			packEle.setSendClearingBank("31317296748");
			packEle.setReceiveClearingBank("323434555334");
			packEle.setReturnLimited("1");
			packEle.setOriginalMessageId("304242444");
			packEle.setBatchId("12");
		}

		return packEle;
	}

	public MessagePackRule getPackRuleByMessageType(String messageType) {
		synchronized (messageTypePackRuleMap) {
			if (messageTypePackRuleMap.isEmpty()) {
				rebuildMessagePackRuleMap();
			}

			MessagePackRule packRule = messageTypePackRuleMap.get(messageType);
			if (null == packRule) {
				rebuildMessagePackRuleMap();
			}

			packRule = messageTypePackRuleMap.get(messageType);
//			if (null == packRule) {
//				throw new CommonException("根据报文类型[" + messageType + "]找不到对应的打包规则，请先维护报文类型的打包规则");
//			}
			return packRule;
		}
	}

	private void rebuildMessagePackRuleMap() {
		// 查询小额报文组报规则表BEPS_MESSAGE_PACK_RULE
		List<MessagePackRule> ruleLst = messagePackRuleService.queryMessagePackRuleList();
//		if (CollUtil.isEmpty(ruleLst)) {
//			throw new CommonException("小额系统没有维护报文类型与队列类型的映射");
//		}
		messageTypePackRuleMap = ruleLst.stream().collect(
				Collectors.toMap(MessagePackRule::getMessageTypeCode, Function.identity(), (key1, key2) -> key2));

	}
}
