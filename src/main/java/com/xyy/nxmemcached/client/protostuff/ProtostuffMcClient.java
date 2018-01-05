package com.xyy.nxmemcached.client.protostuff;

import com.xyy.nxmemcached.client.NxmemcachedConfig;
import com.xyy.nxmemcached.command.CommandResponse;
import com.xyy.nxmemcached.command.CommandType;
import com.xyy.nxmemcached.command.TextGetOneCommand;
import com.xyy.nxmemcached.command.TextStoreCommand;
import com.xyy.nxmemcached.command.TextStoreCommand.Store;

import io.netty.buffer.ByteBuf;

public class ProtostuffMcClient {

	
	private NxmemcachedConfig client;
	public ProtostuffMcClient(NxmemcachedConfig client) {
		this.client = client;
	}
	
	/**  
	 * get:获取缓存 <br/>  
	 *  
	 * @author yangyang.xu  
	 * @param key 缓存key
	 * @param timeout 操作超时时间
	 * @param clasz
	 * @return
	 * @throws Exception  
	 */
	public final <T> T get(String key, Long timeout, Class<T> clasz) throws Exception {
		TextGetOneCommand command = new TextGetOneCommand();
		command.setKey(key);
		command.setKeyBytes(key.getBytes());
		command.setCommandType(CommandType.GET_ONE);
		command.encode();
		CommandResponse response = client.sendCommand(command, timeout);
		ByteBuf byteBuf = response.getContent();
		if (byteBuf == null) {
			return null;
		}
		return ProtostuffSerializationUtil.deserialize(response.byteList, clasz);
	}
	
	/**  
	 * set:(这里用一句话描述这个方法的作用). <br/>  
	 *  
	 * @author yangyang.xu  
	 * @param key 缓存key
	 * @param exp 缓存超时时间
	 * @param value 缓存实体
	 * @param timeout 操作超时时间
	 * @return
	 * @throws Exception  
	 */
	public final <T>boolean set(String key, final int exp, final T value, final long timeout) throws Exception {
		
		TextStoreCommand command = new TextStoreCommand();
		command.setKey(key);
		command.setKeyBytes(key.getBytes());
		command.setExpTime(exp);
		command.setStore(Store.SET);
		byte[] v = ProtostuffSerializationUtil.serialize(value);
		command.setValue(v);
		command.encode();
		CommandResponse response = client.sendCommand(command, timeout);
		return response.isSuccess();
	}
	
}
  
