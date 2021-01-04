package com.fib.gateway.message.xml.event;

import java.util.LinkedList;
import java.util.List;

public class EventQueue {
	private List<Event> queue = new LinkedList<>();

	public List<Event> getQueue() {
		return queue;
	}

	public void setQueue(List<Event> queue) {
		this.queue = queue;
	}

	public void postEvent(Event e) {
		synchronized (queue) {
			// 加入事件队列
			queue.add(e);
			// 唤醒处理线程
			queue.notify();
		}
	}

	public Event selectEvent() {
		Event e = null;
		synchronized (queue) {
			if (queue.isEmpty()) {
				try {
					queue.wait();
				} catch (InterruptedException excp) {

				}
			}
			if (!queue.isEmpty()) {
				e = queue.remove(0);
			}
		}
		return e;
	}
}
