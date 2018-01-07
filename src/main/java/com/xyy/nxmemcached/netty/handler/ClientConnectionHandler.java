package com.xyy.nxmemcached.netty.handler;

import java.util.concurrent.LinkedTransferQueue;

import com.xyy.nxmemcached.command.Command;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.handler.timeout.IdleStateEvent;
/**
 * @author yangyang.xu
 *
 */
public class ClientConnectionHandler extends ChannelDuplexHandler {
	
	LinkedTransferQueue<Command> commandQueue = new LinkedTransferQueue<>();
	
	Command currCommand = null;
    
    @Override
    public void userEventTriggered(final ChannelHandlerContext ctx, final Object evt) throws Exception {
        if (!(evt instanceof IdleStateEvent)) {
            super.userEventTriggered(ctx, evt);
            return;
        }
        System.out.println(" idl ");
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
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
	public LinkedTransferQueue<Command> getCommandQueue() {
		return commandQueue;
	}
	public void setCommandQueue(LinkedTransferQueue<Command> commandQueue) {
		this.commandQueue = commandQueue;
	}
}