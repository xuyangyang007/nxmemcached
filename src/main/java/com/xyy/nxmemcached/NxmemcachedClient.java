package com.xyy.nxmemcached;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

import com.xyy.nxmemcached.algorithm.HashAlgorithm;
import com.xyy.nxmemcached.algorithm.MemcachedSessionLocator;
import com.xyy.nxmemcached.command.SendCommandManager;
import com.xyy.nxmemcached.exception.CacheException;
import com.xyy.nxmemcached.netty.ConnectionPool;
import com.xyy.nxmemcached.netty.Connector;

/**
 * @author yangyang.xu
 *
 */
public class NxmemcachedClient {
	
	private static final int MAX_CONNECTIONS = 2;
	
	private SendCommandManager sendCommandManager = null;
	
	public NxmemcachedClient(String servers,  int threadPoolSize, int connectTimeOut, int idleTime) throws CacheException {
		if (servers == null) {
			throw new CacheException("servers cannot be null!");
		}
		Connector connector = new Connector(threadPoolSize, connectTimeOut, idleTime);
		List<ConnectionPool> sessionList = new ArrayList<>();
		String[] severList = servers.split(",");
		for (String server : severList) {
			String []ipPort = server.split(":");
			String ip = ipPort[0];
			Integer port = Integer.parseInt(ipPort[1]);
			InetSocketAddress mcServer = new InetSocketAddress(ip, port);
			ConnectionPool connectionPool = new ConnectionPool(connector, mcServer, MAX_CONNECTIONS);
			sessionList.add(connectionPool);
		}
		MemcachedSessionLocator locator = new MemcachedSessionLocator(sessionList, HashAlgorithm.KETAMA_HASH);
		sendCommandManager = new SendCommandManager(locator);
	}

}
  
