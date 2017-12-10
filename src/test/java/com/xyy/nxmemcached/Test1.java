package com.xyy.nxmemcached;

import org.databene.contiperf.PerfTest;
import org.databene.contiperf.junit.ContiPerfRule;
import org.junit.Rule;
import org.junit.Test;

import com.xyy.nxmemcached.client.NxmemcachedClient;
import com.xyy.nxmemcached.client.thrift.ThriftMcClient;

import vo.User;

public class Test1 {
	
	@Rule
	public ContiPerfRule i = new ContiPerfRule();
    
	
	@Test
	@PerfTest(threads = 100, duration = 10000)
    public void test() throws Exception {
		NxmemcachedClient core = NxmemcachedClient.initSendCommandManager("ip:port",  5, 1000, 10000);
		final ThriftMcClient client = new ThriftMcClient(core);
		User user = new User();
		user.setUserName("xx");
		user.setUserId(1);
		user.setText("test");
		try {
			//client.set("test1", 10000, user, 10000000L);
			User vl = client.get("test1", 10000000L, User.class);
			System.out.println(vl.getUserName());
		} catch (Exception e) {
			e.printStackTrace();  
		}
    }

}
