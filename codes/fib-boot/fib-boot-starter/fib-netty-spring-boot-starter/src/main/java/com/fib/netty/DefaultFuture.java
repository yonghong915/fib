package com.fib.netty;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.dubbo.common.threadpool.ThreadlessExecutor;
import org.apache.dubbo.common.utils.NamedThreadFactory;

import com.fib.netty.vo.Request;
import com.fib.netty.vo.Response;

import io.netty.channel.Channel;

import org.apache.dubbo.common.timer.HashedWheelTimer;
import org.apache.dubbo.common.timer.Timeout;
import org.apache.dubbo.common.timer.Timer;
import org.apache.dubbo.common.timer.TimerTask;

public class DefaultFuture extends CompletableFuture<Object> {

	private static final Map<Long, Channel> CHANNELS = new ConcurrentHashMap<>();

	private static final Map<Long, DefaultFuture> FUTURES = new ConcurrentHashMap<>();

	private final Long id;
	private final Request request;
	private final int timeout;
	private Timeout timeoutCheckTask;
	private ExecutorService executor;
	public static final Timer TIME_OUT_TIMER = new HashedWheelTimer(new NamedThreadFactory("dubbo-future-timeout", true), 30, TimeUnit.MILLISECONDS);

	public ExecutorService getExecutor() {
		return executor;
	}

	public void setExecutor(ExecutorService executor) {
		this.executor = executor;
	}

	private DefaultFuture(Channel channel, Request request, int timeout) {
		this.request = request;
		this.id = request.getId();
		this.timeout = timeout > 0 ? timeout : 5000;
		// put into waiting map.
		FUTURES.put(id, this);
		CHANNELS.put(id, channel);
	}

	public static DefaultFuture newFuture(Channel channel, Request request, int timeout, ExecutorService executor) {
		final DefaultFuture future = new DefaultFuture(channel, request, timeout);
		future.setExecutor(executor);
		// ThreadlessExecutor needs to hold the waiting future in case of circuit
		// return.
//		if (executor instanceof ThreadlessExecutor exe) {
//			exe.setWaitingFuture(future);
//		}
		// timeout check
		timeoutCheck(future);
		return future;
	}

	public static DefaultFuture getFuture(long id) {
		return FUTURES.get(id);
	}

	public static boolean hasFuture(Channel channel) {
		return CHANNELS.containsValue(channel);
	}

	private static void timeoutCheck(DefaultFuture future) {
		TimeoutCheckTask task = new TimeoutCheckTask(future.getId());
		future.timeoutCheckTask = TIME_OUT_TIMER.newTimeout(task, future.getTimeout(), TimeUnit.MILLISECONDS);
	}

	public static void received(Response response) {
		received(response, false);
	}

	public static void received(Response response, boolean timeout) {
		try {
			DefaultFuture future = FUTURES.remove(response.getId());
			if (future != null) {
				Timeout t = future.timeoutCheckTask;
				if (!timeout) {
					// decrease Time
					t.cancel();
				}
				future.doReceived(response);
			} else {
				//
			}
		} finally {
			CHANNELS.remove(response.getId());
		}
	}

	private void doReceived(Response res) {
		if (res == null) {
			throw new IllegalStateException("response cannot be null");
		}
		this.complete(res);

//		if (executor != null && executor instanceof ThreadlessExecutor threadlessExecutor) {
//			if (threadlessExecutor.isWaiting()) {
//				threadlessExecutor.notifyReturn(new IllegalStateException("The result has returned, but the biz thread is still waiting"
//						+ " which is not an expected state, interrupt the thread manually by returning an exception."));
//			}
//		}
	}

	private long getId() {
		return id;
	}

	public Request getRequest() {
		return request;
	}

	private int getTimeout() {
		return timeout;
	}

	private static class TimeoutCheckTask implements TimerTask {

		private final Long requestID;

		TimeoutCheckTask(Long requestID) {
			this.requestID = requestID;
		}

		@Override
		public void run(Timeout timeout) {
			DefaultFuture future = DefaultFuture.getFuture(requestID);
			if (future == null || future.isDone()) {
				return;
			}

			if (future.getExecutor() != null) {
				future.getExecutor().execute(() -> notifyTimeout(future));
			} else {
				notifyTimeout(future);
			}
		}

		private void notifyTimeout(DefaultFuture future) {
			Response timeoutResponse = new Response(future.getId());
			DefaultFuture.received(timeoutResponse, true);
		}
	}

}
