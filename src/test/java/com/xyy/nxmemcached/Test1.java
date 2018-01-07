package com.xyy.nxmemcached;

import org.databene.contiperf.PerfTest;
import org.databene.contiperf.junit.ContiPerfRule;
import org.junit.Rule;
import org.junit.Test;

import com.xyy.nxmemcached.client.NxmemcachedClient;
import com.xyy.nxmemcached.client.NxmemcachedManager;
import com.xyy.nxmemcached.client.protostuff.ProtostuffSerialization;
import com.xyy.nxmemcached.client.thrift.ThriftSerialization;

import vo.User;

public class Test1 {
	
	@Rule
	public ContiPerfRule i = new ContiPerfRule();
	
	@Test
	@PerfTest(threads = 100, duration = 5000000)
    public void test() throws Exception {
		NxmemcachedManager core = NxmemcachedManager.initNxmemcachedManager("ip:port", 1000, 1);
		final NxmemcachedClient client = new NxmemcachedClient(core, ThriftSerialization.getInstance());
		try {
			User vl = client.get("test1", 1000000L, User.class);
			System.out.println(vl.getUserName());
		} catch (Exception e) {
			e.printStackTrace();  
		}
    }
	
	@Test
	@PerfTest(threads = 100, duration = 10000)
    public void test2() throws Exception {
		NxmemcachedManager core = NxmemcachedManager.initNxmemcachedManager("ip:port", 1000, 1);
		NxmemcachedClient client = new NxmemcachedClient(core, ProtostuffSerialization.getInstance());
		
		try {
			User user = client.get("test2", 1000L, User.class);
			System.out.println(user.getUserName());
		} catch (Exception e) {
			e.printStackTrace();  
		}
    }

}
