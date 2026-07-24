package com.fib.ecny.trans.comp.netty;

import org.apache.dubbo.common.serialize.SerializationException;
import org.apache.dubbo.common.timer.Timeout;
import org.apache.dubbo.remoting.Channel;
import org.apache.dubbo.remoting.RemotingException;
import org.apache.dubbo.remoting.TimeoutException;
import org.apache.dubbo.remoting.exchange.Response;
import org.apache.dubbo.remoting.exchange.support.DefaultFuture;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class WriteFuture extends CompletableFuture<Object> {
    private static final Map<Long, WriteFuture> FUTURES = new ConcurrentHashMap<>();

    private final Request request;

    private final Long id;

    private final int timeout;

    private final long start = System.currentTimeMillis();

    private volatile long sent;

    public WriteFuture(Request request, int timeout) {
        this.request = request;
        this.id = request.getRequestId();
        this.timeout = timeout;
        FUTURES.put(id, this);
    }

    public static WriteFuture getFuture(long id) {
        return FUTURES.get(id);
    }

    public static void sent(Channel channel, Request request) {
        WriteFuture future = FUTURES.get(request.getRequestId());
        if (future != null) {
            future.doSent();
        }
    }

    public static void received(Channel channel, Response response, boolean timeout) {
        try {
            WriteFuture future = FUTURES.remove(response.getId());
            if (future != null) {
                Timeout t = future.timeoutCheckTask;
                if (!timeout) {
                    // decrease Time
                    t.cancel();
                }
                future.doReceived(response);


            }
        } catch (Exception e) {
        }
    }

    private void doReceived(Response res) {
        if (res == null) {
            throw new IllegalStateException("response cannot be null");
        }
        if (res.getStatus() == Response.OK) {
            this.complete(res.getResult());
        } else if (res.getStatus() == Response.CLIENT_TIMEOUT || res.getStatus() == Response.SERVER_TIMEOUT) {
            this.completeExceptionally(
                    new TimeoutException(res.getStatus() == Response.SERVER_TIMEOUT, channel, res.getErrorMessage()));
        } else if (res.getStatus() == Response.SERIALIZATION_ERROR) {
            this.completeExceptionally(new SerializationException(res.getErrorMessage()));
        } else {
            this.completeExceptionally(new RemotingException(channel, res.getErrorMessage()));
        }
    }

    private void doSent() {
        sent = System.currentTimeMillis();
    }
}
