package com.fib.msgconverter;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

public class ConTest {
	class Handler {
		void handle() {
		}
	}

	private final CopyOnWriteArraySet<Handler> handlers = new CopyOnWriteArraySet<>();

	public void addHandler(Handler h) {
		handlers.add(h);
	}

	private long internalState;

	private synchronized void changeState() {
		internalState = 1;
	}

	public void update() {
		changeState();
		for (Handler handler : handlers)
			handler.handle();
	}

	public static void main(String[] args) {
		// Collections.synchronizedCollection(null)
		List list = new CopyOnWriteArrayList();
		Set set = new CopyOnWriteArraySet();
		Map<String,String> map = new ConcurrentHashMap<>();

		
	}

}
