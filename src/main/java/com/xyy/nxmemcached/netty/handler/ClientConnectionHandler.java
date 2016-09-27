package com.xyy.nxmemcached.netty.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;

public class ClientConnectionHandler extends ChannelDuplexHandler {
    
    @Override
    public void userEventTriggered(final ChannelHandlerContext ctx, final Object evt) throws Exception {
        
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("[[===");
        ByteBuf msgBuf = (ByteBuf) msg;
        if (msgBuf != null && msgBuf.refCnt() > 0) {
            msgBuf.release();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}