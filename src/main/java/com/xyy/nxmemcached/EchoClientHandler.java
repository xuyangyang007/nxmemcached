package com.xyy.nxmemcached;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

public class EchoClientHandler extends SimpleChannelInboundHandler<ByteBuf> {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.write(Unpooled.copiedBuffer("get m_1 \r \n", CharsetUtil.UTF_8));
        ctx.flush();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        byte[] req = new byte[msg.readableBytes()];
                msg.readBytes(req);
                String body = new String(req,"UTF-8");
        System.out.println("Client received: " + body);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}