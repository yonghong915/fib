package com.fib.autoconfigure.msgconverter.channel.event;

/**
 * 事件处理接口
 * 
 * @author fangyh
 * @version 1.0
 * @date 2022-05-20 11:53:02
 */
public interface EventHandler {

	/**
	 * 处理请求达到
	 * 
	 * @param event
	 */
	void handleRequestArrived(Event event);

}
