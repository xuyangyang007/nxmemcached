# nxmemcached
使用技术：netty+一致性哈希,连接池、failover、心跳机制保障可用性。<br/>
支持thrift,protostuff序列化，建议用protostuff序列化策略，因为thrift方式需要用thrift工具生成模板类<br/>

**DEMO**

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

