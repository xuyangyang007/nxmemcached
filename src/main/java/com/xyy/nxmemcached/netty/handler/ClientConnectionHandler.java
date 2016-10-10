package com.xyy.nxmemcached.netty.handler;

import com.xyy.nxmemcached.command.CommandResponse;
import com.xyy.nxmemcached.command.CommandResponseFuture;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.AttributeKey;

public class ClientConnectionHandler extends ChannelDuplexHandler {
    
    private static final AttributeKey<Object> DEFAULT_ATTRIBUTE       = AttributeKey.valueOf("response");
    
    @Override
    public void userEventTriggered(final ChannelHandlerContext ctx, final Object evt) throws Exception {
        System.out.println("====");
        if (!(evt instanceof IdleStateEvent)) {
            super.userEventTriggered(ctx, evt);
            return;
        }
    }
    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        super.write(ctx, msg, promise);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Channel channel = ctx.channel();
        CommandResponseFuture future = (CommandResponseFuture)channel.attr(DEFAULT_ATTRIBUTE).get();
        ByteBuf msgBuf = (ByteBuf) msg;
        CommandResponse response = new CommandResponse();
        response.setContent(msgBuf.retain());
        response.setSuccess(true);
        future.setResponse(response);
        future.done();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}