package com.xyy.nxmemcached.client;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.xyy.nxmemcached.algorithm.HashAlgorithm;
import com.xyy.nxmemcached.algorithm.MemcachedSessionLocator;
import com.xyy.nxmemcached.command.Command;
import com.xyy.nxmemcached.command.CommandResponse;
import com.xyy.nxmemcached.command.CommandResponseFuture;
import com.xyy.nxmemcached.exception.CacheException;
import com.xyy.nxmemcached.netty.ConnectionPool;
import com.xyy.nxmemcached.netty.Connector;

import io.netty.channel.Channel;

/**
 * @author yangyang.xu
 *
 */
public class NxmemcachedManager {
	// 每台服务器最大连接数
	private int maxConnection = 1;
	
	MemcachedSessionLocator locator = null;
	// 服务器列表格式 ip:port,ip:port
	private String servers;
	// 线程池大小，默认是mc服务器数量*maxConnection
	private int threadPoolSize;
	// 连接mc超时时间
	private int connectTimeOut = 3000;
	// 连接空闲时，各多久发一次心跳
	private int idleTime = 5000;
	
	private volatile static NxmemcachedManager client;
	
	List<ConnectionPool> sessionList = null;
	
	
	private NxmemcachedManager(String servers,  int connectTimeOut, int maxConnection) throws CacheException {
		if (servers == null) {
			throw new CacheException("servers cannot be null!");
		}
		String[] severList = servers.split(",");
		if (severList == null || severList.length == 0) {
			throw new CacheException("servers cannot be null!");
		}
		this.maxConnection = maxConnection;
		this.connectTimeOut = connectTimeOut;
		threadPoolSize = severList.length * maxConnection;
		
		Connector connector = new Connector(threadPoolSize, connectTimeOut, idleTime);
		List<ConnectionPool> sessionListTemp = new ArrayList<>();
		for (String server : severList) {
			String[] ipPort = server.split(":");
			String ip = ipPort[0];
			Integer port = Integer.parseInt(ipPort[1]);
			InetSocketAddress mcServer = new InetSocketAddress(ip, port);
			ConnectionPool connectionPool = new ConnectionPool(connector, mcServer, maxConnection);
			sessionListTemp.add(connectionPool);
		}
		sessionList = sessionListTemp;
		this.locator = new MemcachedSessionLocator(sessionListTemp, HashAlgorithm.KETAMA_HASH);
	}
	
	public static NxmemcachedManager initNxmemcachedManager(String servers, int connectTimeOut, int maxConnection) throws CacheException {
		if (client == null) {
			synchronized (NxmemcachedManager.class) {
				if (client == null) {
					client = new NxmemcachedManager(servers, connectTimeOut, maxConnection);
				}
			}
		}
		return client;
	}
	
	public CommandResponse sendCommand(Command command, long optTimeOut) throws InterruptedException, CacheException, TimeoutException {
		String key = command.getKey();
		ConnectionPool session = locator.getSessionByKey(key);
		Channel channel = session.getChannel();
		
		CommandResponseFuture responseFuture = new CommandResponseFuture();
		command.setFuture(responseFuture);
		
		channel.writeAndFlush(command);
		
		
		CommandResponse response = responseFuture.get(optTimeOut, TimeUnit.MILLISECONDS);
		return response;
	}

	public int getMaxConnection() {
		return maxConnection;
	}

	public MemcachedSessionLocator getLocator() {
		return locator;
	}

	public String getServers() {
		return servers;
	}

	public int getThreadPoolSize() {
		return threadPoolSize;
	}

	public int getConnectTimeOut() {
		return connectTimeOut;
	}

	public int getIdleTime() {
		return idleTime;
	}
	
	public void removeFailMcServer(InetSocketAddress addr) {
		
	}
}
  
