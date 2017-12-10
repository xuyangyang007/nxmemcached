
  
package com.xyy.nxmemcached.client.thrift;

import org.apache.thrift.TBase;

import com.xyy.nxmemcached.client.NxmemcachedClient;
import com.xyy.nxmemcached.command.CommandResponse;
import com.xyy.nxmemcached.command.CommandType;
import com.xyy.nxmemcached.command.TextGetOneCommand;
import com.xyy.nxmemcached.command.TextStoreCommand;
import com.xyy.nxmemcached.command.TextStoreCommand.Store;

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
		try {
			ByteBuf byteBuf = response.getContent();
			if (byteBuf == null) {
				return null;
			}
			byte[] byteList = new byte[byteBuf.readableBytes()];
			byteBuf.readBytes(byteList, 0 , byteBuf.readableBytes());
			String x = new String(byteList);
			return ThriftSerializeUtil.deSerialize(byteList, clasz);
		} finally {
			response.getContent().release();
		}
	}
	
	public final <T extends TBase> boolean set(String key, final int exp, final T value, final long timeout) throws Exception {
		TextStoreCommand command = new TextStoreCommand();
		command.setKey(key);
		command.setKeyBytes(key.getBytes());
		command.setExpTime(exp);
		command.setStore(Store.SET);
		byte[] v = ThriftSerializeUtil.serialize(value);
		command.setValue(v);
		command.encode();
		CommandResponse response = client.sendCommand(command, timeout);
		return response.isSuccess();
	}

}
  
