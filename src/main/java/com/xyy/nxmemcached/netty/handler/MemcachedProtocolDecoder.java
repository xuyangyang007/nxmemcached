package com.xyy.nxmemcached.netty.handler;


import java.util.List;
import java.util.concurrent.LinkedTransferQueue;

import com.xyy.nxmemcached.command.Command;
import com.xyy.nxmemcached.command.CommandResponse;
import com.xyy.nxmemcached.command.CommandResponseFuture;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public class MemcachedProtocolDecoder extends ByteToMessageDecoder {
	
	Command currCommand = null;

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		ClientConnectionHandler handler = ctx.pipeline().get(ClientConnectionHandler.class);
        ByteBuf buf = (ByteBuf) in;
        LinkedTransferQueue<Command> queue = handler.getCommandQueue();
        if (buf.readableBytes() < 3) {
        	return;
        }
    	Command command = null;
    	if (currCommand == null) {
    		command = queue.poll();
    		currCommand = command;
    	} else {
    		command = currCommand;
    	}
    	CommandResponseFuture future = command.getFuture();
        
        CommandResponse response = command.decode(buf);
        if (response == null) {
        	return;
        }
        currCommand = null;
        future.setResponse(response);
        future.done();
		out.add(in);
	}

}
  
