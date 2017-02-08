package com.xyy.nxmemcached.netty.handler;


import java.util.List;

import com.xyy.nxmemcached.command.CommandResponse;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public class MemcachedProtocolDecoder extends ByteToMessageDecoder {

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		 Channel channel = ctx.channel();
		 out.add(CommandResponse.newSuccess(in));
	}

}
  
