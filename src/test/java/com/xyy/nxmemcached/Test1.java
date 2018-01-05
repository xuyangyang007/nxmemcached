package com.xyy.nxmemcached;

import org.databene.contiperf.PerfTest;
import org.databene.contiperf.junit.ContiPerfRule;
import org.junit.Rule;
import org.junit.Test;

import com.xyy.nxmemcached.client.NxmemcachedConfig;
import com.xyy.nxmemcached.client.thrift.ThriftMcClient;

import vo.User;

public class Test1 {
	
	@Rule
	public ContiPerfRule i = new ContiPerfRule();
    
	
	@Test
	@PerfTest(threads = 100, duration = 10000)
    public void test() throws Exception {
		NxmemcachedConfig core = NxmemcachedConfig.initSendCommandManager("ip:port",  5, 1000, 10000);
		final ThriftMcClient client = new ThriftMcClient(core);
		try {
			User vl = client.get("test1", 1000000L, User.class);
			System.out.println(vl.getUserName());
		} catch (Exception e) {
			e.printStackTrace();  
		}
    }

}
