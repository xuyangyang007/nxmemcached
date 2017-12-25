# nxmemcached
使用技术：netty+一致性哈希,支持thrift,protostuff序列化
demo
<code>
		NxmemcachedConfig core = NxmemcachedConfig.initSendCommandManager("ip:port",  5, 1000, 10000);
		final ThriftMcClient client = new ThriftMcClient(core);
		try {
			client.set("test1", 10000, user, 3000L);
			User vl = client.get("test1", 10000000L, User.class);
		} catch (Exception e) {
			e.printStackTrace();  
		}
</code>
