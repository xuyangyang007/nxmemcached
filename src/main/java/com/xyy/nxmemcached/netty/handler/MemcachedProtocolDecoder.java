package com.xyy.nxmemcached.netty.handler;


import java.util.List;

import com.xyy.nxmemcached.command.Command;
import com.xyy.nxmemcached.command.CommandResponse;
import com.xyy.nxmemcached.common.Constants;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public class MemcachedProtocolDecoder extends ByteToMessageDecoder {

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		Channel channel = ctx.channel();
		Command command = (Command)channel.attr(Constants.DEFAULT_COMMAND).get();
		ByteBuf buf = in.readSlice(in.readableBytes()).retain();
		CommandResponse response = command.decode(buf);
		out.add(response);
	}

}
  
