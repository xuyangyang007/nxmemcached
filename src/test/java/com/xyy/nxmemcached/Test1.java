package com.xyy.nxmemcached;

import com.xyy.nxmemcached.client.NxmemcachedClient;
import com.xyy.nxmemcached.client.thrift.ThriftMcClient;

import vo.User;

public class Test1 {
    
    public static void main(String[] args) throws Exception {
        System.out.println("===");
        NxmemcachedClient core = NxmemcachedClient.initSendCommandManager("ip:port",  5, 1000, 10000);
        ThriftMcClient client = new ThriftMcClient(core);
        User user = new User();
        user.setUserName("xx");
        user.setUserId(1);
        user.setText("test");
        client.set("test1", 10000, user, 10000000L);
        
        User vl = client.get("test1", 10000000L, User.class);
        
        System.out.println(vl.getUserName());
    }

}
