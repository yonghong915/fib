/**
 * 北京长信通信息技术有限公司
 * 2008-8-26 下午07:42:47
 */
package com.fib.msgconverter.commgateway.event;

import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import com.giantstone.common.util.ClassUtil;

/**
 * 统一事件队列
 * 
 * @author 刘恭亮
 * 
 */
public class EventQueue {

	/**
	 * 控制队列是否启用
	 */
	// private boolean run = true;
	//
	// public boolean isRun() {
	// return run;
	// }
	//
	// public void setRun(boolean run) {
	// this.run = run;
	// }
	/**
	 * 事件队列
	 */
	private List queue = new LinkedList();

	public List getQueue() {
		return queue;
	}

	public void setQueue(List queue) {
		this.queue = queue;
	}

	/**
	 * 事件处理器池
	 */
	private EventHandlerThread[] handlers = null;

	public EventHandlerThread[] getHandlers() {
		return handlers;
	}

	public void setHandlers(EventHandlerThread[] handlers) {
		this.handlers = handlers;
	}

	/**
	 * 日志记录器
	 */
	private Logger logger;

	/**
	 * 事件处理器类名
	 */
	private String eventHandlerClass = "com.fib.msgconverter.commgateway.event.EventHandlerThread";

	public EventQueue() {
	}

	/**
	 * 获取活动的事件处理器数量
	 * 
	 * @return
	 */
	public int getAliveEventHandlerNum() {
		if (null == handlers) {
			return -1;
		} else {
			int num = 0;
			for (int i = 0; i < handlers.length; i++) {
				if (null != handlers[i] && handlers[i].isAlive()) {
					num++;
				}
			}

			return num;
		}
	}

	/**
	 * 重置事件处理器池
	 */
	public void resetEventHandlerPool() {
		if (null == handlers) {
			throw new RuntimeException("Please init event handler pool first!");
		}

		if (0 == handlers.length) {
			return;
		}

		for (int i = 0; i < handlers.length; i++) {
			if (null == handlers[i] || !handlers[i].isAlive()) {
				handlers[i] = (EventHandlerThread) ClassUtil
						.createClassInstance(eventHandlerClass);
				handlers[i].setQueue(this);
				handlers[i].setLogger(logger);

				handlers[i].setContextClassLoader(Thread.currentThread()
						.getContextClassLoader());
				handlers[i].start();
			}
		}
	}

	/**
	 * 开始派发事件
	 * 
	 * @param handlerNumber
	 */
	public void startDispatch(int handlerNumber) {
		handlers = new EventHandlerThread[handlerNumber];
		for (int i = 0; i < handlerNumber; i++) {
			handlers[i] = (EventHandlerThread) ClassUtil
					.createClassInstance(eventHandlerClass);
			handlers[i].setQueue(this);
			handlers[i].setLogger(logger);

			// **** add by liugl 20090226
			// 保证每个handler线程使用和主线程相同的ClassLoader
			handlers[i].setContextClassLoader(Thread.currentThread()
					.getContextClassLoader());
			//
			handlers[i].start();
		}
	}

	public void stopDispatch() {
		if (null == handlers || 0 == handlers.length) {
			return;
		}
		for (int i = 0; i < handlers.length; i++) {
			handlers[i].setRun(false);
			handlers[i].interrupt();
			try {
				handlers[i].join(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 投递一个事件到EQ
	 * 
	 * @param e
	 */
	public void postEvent(Event e) {

		// if (!run) {
		// // 停止处理新请求
		// if (Event.EVENT_REQUEST_ARRIVED == e.getEventType()) {
		// // e.setEventType(Event.EVENT_CHANNEL_SHUTDOWN);
		// // TODO
		// return;
		// }
		// }

		synchronized (queue) {
			// 加入事件队列
			queue.add(e);
			// 唤醒处理线程
			queue.notify();
		}

	}

	/**
	 * 从EQ中领取一个事件
	 * 
	 * @return
	 */
	public Event selectEvent() {
		Event e = null;
		synchronized (queue) {
			if (queue.size() == 0) {
				try {
					queue.wait();
				} catch (InterruptedException excp) {
					// excp.printStackTrace();
				}
			}

			if (queue.size() > 0) {
				e = (Event) queue.remove(0);
			}
		}
		return e;
	}

	protected Object clone() {
		return null;
	}

	/**
	 * @return the eventHandlerClass
	 */
	public String getEventHandlerClass() {
		return eventHandlerClass;
	}

	/**
	 * @param eventHandlerClass
	 *            the eventHandlerClass to set
	 */
	public void setEventHandlerClass(String eventHandlerClass) {
		this.eventHandlerClass = eventHandlerClass;
	}

	/**
	 * @return the logger
	 */
	public Logger getLogger() {
		return logger;
	}

	/**
	 * @param logger
	 *            the logger to set
	 */
	public void setLogger(Logger logger) {
		this.logger = logger;
	}

}
