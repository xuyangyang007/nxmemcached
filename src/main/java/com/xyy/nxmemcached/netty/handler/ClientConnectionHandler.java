package com.xyy.nxmemcached.netty.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.handler.timeout.IdleStateEvent;

import com.xyy.nxmemcached.command.CommandResponse;
import com.xyy.nxmemcached.command.CommandResponseFuture;
import com.xyy.nxmemcached.common.Constants;

public class ClientConnectionHandler extends ChannelDuplexHandler {
    
    @Override
    public void userEventTriggered(final ChannelHandlerContext ctx, final Object evt) throws Exception {
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
        System.out.println(Constants.t.get());
        Channel channel = ctx.channel();
        CommandResponseFuture future = (CommandResponseFuture)channel.attr(Constants.DEFAULT_ATTRIBUTE).get();
        ByteBuf msgBuf = (ByteBuf) msg;
        future.setResponse(CommandResponse.newSuccess(msgBuf));
        future.done();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}