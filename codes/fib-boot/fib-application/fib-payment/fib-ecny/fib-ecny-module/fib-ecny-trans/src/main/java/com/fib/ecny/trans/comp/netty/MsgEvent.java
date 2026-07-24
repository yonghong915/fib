package com.fib.ecny.trans.comp.netty;

import io.netty.channel.Channel;
import lombok.Data;

@Data
public class MsgEvent {
    private Channel channel;
    private MsgDto msg;

    // getter/setter
    public static MsgEvent newInstance() {
        return new MsgEvent();
    }

    public void clear() {
        this.channel = null;
        this.msg = null;
    }
}
