/**  
 * Project Name:nxmemcached  
 * File Name:NxmemcachedClient.java  
 * Package Name:com.xyy.nxmemcached.client  
 * Date:2018年1月7日上午10:32:45  
 * Copyright (c) 2018, vipshop.com All Rights Reserved.  
 *  
*/  
  
package com.xyy.nxmemcached.client;

import com.xyy.nxmemcached.client.protostuff.ProtostuffSerialization;
import com.xyy.nxmemcached.command.CommandResponse;
import com.xyy.nxmemcached.command.CommandType;
import com.xyy.nxmemcached.command.TextDeleteCommand;
import com.xyy.nxmemcached.command.TextGetOneCommand;
import com.xyy.nxmemcached.command.TextStoreCommand;
import com.xyy.nxmemcached.command.TextStoreCommand.Store;

/**  
 * ClassName:NxmemcachedClient <br/>  
 * Date:     2018年1月7日 上午10:32:45 <br/>  
 * @author   yangyang.xu  
 * @version    
 * @since    JDK 1.7
 * @see        
 */
public class NxmemcachedClient {
	
	private NxmemcachedManager manager;
	
	private ISerialization serialization;
	
	public NxmemcachedClient(NxmemcachedManager manager, ISerialization serialization) {
		this.manager = manager;
		this.serialization = serialization;
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
		CommandResponse response = manager.sendCommand(command, timeout);
		if (response == null || response.byteList == null) {
			return null;
		}
		return serialization.deserialize(response.byteList, clasz);
	}
	
	/**  
	 * set:设置缓存. <br/>  
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
		byte[] v = serialization.serialize(value);
		command.setValue(v);
		command.encode();
		CommandResponse response = manager.sendCommand(command, timeout);
		return response.isSuccess();
	}
	
	/**  
	 * delete:删除缓存. <br/>  
	 *  
	 * @author yangyang.xu  
	 * @param key  缓存key
	 * @param timeout 操作超时时间
	 * @return
	 * @throws Exception  
	 * @since JDK 1.7
	 */
	public boolean delete(String key, final long timeout) throws Exception {
		TextDeleteCommand command = new TextDeleteCommand();
		command.setKey(key);
		command.setKeyBytes(key.getBytes());
		command.encode();
		CommandResponse response = manager.sendCommand(command, timeout);
		return response.isSuccess();
	}

}
  
