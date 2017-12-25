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
import com.xyy.nxmemcached.common.Constants;
import com.xyy.nxmemcached.exception.CacheException;
import com.xyy.nxmemcached.netty.ConnectionPool;
import com.xyy.nxmemcached.netty.Connector;

import io.netty.channel.Channel;

/**
 * @author yangyang.xu
 *
 */
public class NxmemcachedConfig {
	
	private static final int MAX_CONNECTIONS = 2;
	
	MemcachedSessionLocator locator = null;
	
	private String servers;
	private int threadPoolSize;
	private int connectTimeOut;
	private int idleTime;
	
	private volatile static NxmemcachedConfig client;
	
	private NxmemcachedConfig(String servers,  int threadPoolSize, int connectTimeOut, int idleTime) throws CacheException {
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
		this.locator = new MemcachedSessionLocator(sessionList, HashAlgorithm.KETAMA_HASH);
	}
	
	public static NxmemcachedConfig initSendCommandManager(String servers,  int threadPoolSize, int connectTimeOut, int idleTime) throws CacheException {
		if (client == null) {
			synchronized (NxmemcachedConfig.class) {
				if (client == null) {
					client = new NxmemcachedConfig(servers,  threadPoolSize, connectTimeOut, idleTime);
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
		channel.attr(Constants.DEFAULT_ATTRIBUTE).set(responseFuture);
		channel.attr(Constants.DEFAULT_COMMAND).set(command);
		channel.writeAndFlush(command.getBuf());
		CommandResponse response = responseFuture.get(optTimeOut, TimeUnit.MILLISECONDS);
		session.returnChannel(channel);
		return response;
	}

	public String getServers() {
		return servers;
	}

	public void setServers(String servers) {
		this.servers = servers;
	}

	public int getThreadPoolSize() {
		return threadPoolSize;
	}

	public void setThreadPoolSize(int threadPoolSize) {
		this.threadPoolSize = threadPoolSize;
	}

	public int getConnectTimeOut() {
		return connectTimeOut;
	}

	public void setConnectTimeOut(int connectTimeOut) {
		this.connectTimeOut = connectTimeOut;
	}

	public int getIdleTime() {
		return idleTime;
	}

	public void setIdleTime(int idleTime) {
		this.idleTime = idleTime;
	}

}
  
