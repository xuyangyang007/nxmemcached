/**  
 * Project Name:nxmemcached  
 * File Name:NormalTest.java  
 * Package Name:com.xyy.nxmemcached  
 * Date:2017年12月27日下午1:55:42  
 * Copyright (c) 2017, vipshop.com All Rights Reserved.  
 *  
*/  
  
package com.xyy.nxmemcached;

import org.databene.contiperf.PerfTest;
import org.junit.Test;

import com.xyy.nxmemcached.client.NxmemcachedConfig;
import com.xyy.nxmemcached.client.thrift.ThriftMcClient;

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
		NxmemcachedConfig core = NxmemcachedConfig.initSendCommandManager("ip:port",  5, 1000, 10000);
		final ThriftMcClient client = new ThriftMcClient(core);
		User user = new User();
		user.setUserName("xx");
		user.setUserId(1);
		user.setText("test");
		try {
			for (int i=1;i<=10000;i++) {
			//client.set("test1", 100000, user, 10000000L);
				User vl = client.get("test1", 10000000L, User.class);
				System.out.println(vl.getUserName() + " " +i);
			}
		} catch (Exception e) {
			e.printStackTrace();  
		}
    }

}
  
