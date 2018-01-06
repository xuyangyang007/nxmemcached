# nxmemcached
使用技术：netty+一致性哈希,支持thrift,protostuff序列化
demo

		NxmemcachedManager core = NxmemcachedManager.initNxmemcachedManager(
				"ip1:port,ip2:port", // mc服务器列表
				1000,                // 连接超时时间
				1);                  // 每台服务器对应的连接池
		final ThriftMcClient client = new ThriftMcClient(core);
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

