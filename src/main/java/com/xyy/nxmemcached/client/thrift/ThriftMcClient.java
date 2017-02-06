
  
package com.xyy.nxmemcached.client.thrift;

import org.apache.thrift.TBase;

import com.xyy.nxmemcached.client.NxmemcachedClient;
import com.xyy.nxmemcached.command.CommandResponse;
import com.xyy.nxmemcached.command.CommandType;
import com.xyy.nxmemcached.command.TextGetOneCommand;

import io.netty.buffer.ByteBuf;

public class ThriftMcClient {
	
	private NxmemcachedClient client;
	public ThriftMcClient(NxmemcachedClient client) {
		this.client = client;
	}
	
	
	public final <T extends TBase> T get(String key, Long timeout, Class<T> clasz) throws Exception {
		TextGetOneCommand command = new TextGetOneCommand();
		command.setKey(key);
		command.setKeyBytes(key.getBytes());
		command.setCommandType(CommandType.GET_ONE);
		command.encode();
		CommandResponse response = client.sendCommand(command, timeout);
		ByteBuf byteBuf = response.getContent();
		byte[] byteList = byteBuf.array();
		return ThriftSerializeUtil.deSerialize(byteList, clasz);
	}

}
  
