/**  
 * Project Name:nxmemcached  
 * File Name:NormalTest.java  
 * Package Name:com.xyy.nxmemcached  
 * Date:2017年12月27日下午1:55:42  
 * Copyright (c) 2017, vipshop.com All Rights Reserved.  
 *  
*/  
  
package com.xyy.nxmemcached;

import org.junit.Test;

import com.xyy.nxmemcached.client.NxmemcachedClient;
import com.xyy.nxmemcached.client.NxmemcachedManager;
import com.xyy.nxmemcached.client.protostuff.ProtostuffSerialization;
import com.xyy.nxmemcached.client.thrift.ThriftSerialization;
import com.xyy.nxmemcached.exception.CacheException;

import vo.User;

/**  
 * ClassName:NormalTest <br/>  
 * Date:     2017年12月27日 下午1:55:42 <br/>  
 * @author   yangyang.xu  
 * @version    
 * @since    JDK 1.7
 * @see        
 */
public class NormalTest {
	
	@Test
    public void test() throws Exception {
		NxmemcachedManager core = NxmemcachedManager.initNxmemcachedManager(
				"ip:port,ip:port", // mc服务器列表
				1000,                // 连接超时时间
				1);                  // 每台服务器对应的连接池大小
		final NxmemcachedClient client = new NxmemcachedClient(core, 
				ProtostuffSerialization.getInstance());// 使用portostuff作为序列化
		User user = new User();
		user.setUserName("xx");
		user.setUserId(1);
		user.setText("test");
		try {
			client.set("test1", // key 
					100000,     // 缓存超时时间(单位：秒)
					user,       // 缓存对象
					3000L);     // 操作超时时间(单位：毫秒)
		} catch (Exception e) {
			e.printStackTrace();  
		}
    }
	
	@Test
	public void testProto() throws CacheException {
		NxmemcachedManager core = NxmemcachedManager.initNxmemcachedManager("ip:port",  1000, 1);
		NxmemcachedClient client = new NxmemcachedClient(core, ProtostuffSerialization.getInstance());
		User user = new User();
		user.setUserName("yy");
		user.setUserId(2);
		user.setText("test");
		try {
			boolean success = client.set("test2", 100000, user, 1000L);
			System.out.println(success);
		} catch (Exception e) {
			e.printStackTrace();  
		}
	}

}
  
