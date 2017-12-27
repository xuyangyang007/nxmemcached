package com.xyy.nxmemcached.netty.handler;

import java.nio.charset.Charset;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadLocalRandom;

import com.xyy.nxmemcached.command.Command;
import com.xyy.nxmemcached.command.CommandResponse;
import com.xyy.nxmemcached.command.CommandResponseFuture;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.handler.timeout.IdleStateEvent;
/**
 * @author yangyang.xu
 *
 */
@Sharable
public class ClientConnectionHandler extends ChannelDuplexHandler {
	
	LinkedBlockingQueue<Command> commandQueue = new LinkedBlockingQueue<>();
    
    @Override
    public void userEventTriggered(final ChannelHandlerContext ctx, final Object evt) throws Exception {
    	System.out.println(" idl ");
        if (!(evt instanceof IdleStateEvent)) {
            super.userEventTriggered(ctx, evt);
            return;
        }
    }
    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
    	//System.out.println(" write " + commandQueue.size());
    	Command command = (Command)msg;
    	commandQueue.offer(command);
        super.write(ctx, command.getBuf(), promise);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        while(buf.readableBytes() > 0) {
        	Command command = commandQueue.poll();
        	CommandResponseFuture future = command.getFuture();
	      //  System.out.println(buf.toString(Charset.forName("utf-8")) + " size:"+ buf.readableBytes());
	        
	        CommandResponse response = command.decode(buf);
	        future.setResponse(response);
	        future.done();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}