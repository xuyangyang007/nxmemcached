package com.xyy.nxmemcached.command;

import io.netty.channel.Channel;
import io.netty.util.CharsetUtil;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.xyy.nxmemcached.algorithm.MemcachedSessionLocator;
import com.xyy.nxmemcached.common.Constants;
import com.xyy.nxmemcached.exception.CacheException;
import com.xyy.nxmemcached.netty.ConnectionPool;
import com.xyy.nxmemcached.netty.Connector;

public class SendCommandManager {
	
	
	MemcachedSessionLocator locator = null;
	
    public SendCommandManager(MemcachedSessionLocator locator) {
		super();
		this.locator = locator;
	}

//	public static void main(String[] args) throws CacheException, InterruptedException, ExecutionException {
//        Connector connector = new Connector(2, 30000, 30000);
//        InetSocketAddress address = new InetSocketAddress("ip", 11211);
//        ConnectionPool pool = new ConnectionPool(connector, address, 2);
//        Channel channel = pool.getChannel();
//        Thread.sleep(30000000);
//    }

    public void sendCommand(Command command, long optTimeOut) throws InterruptedException, CacheException, TimeoutException {
//        TextGetOneCommand command = new TextGetOneCommand();
//        command.setKey("m_1");
//        command.setKeyBytes("m_1".getBytes());
//        command.setCommandType(CommandType.GET_ONE);
//        command.encode();
    	String key = command.getKey();
    	ConnectionPool session = locator.getSessionByKey(key);
    	Channel channel = session.getChannel();
        CommandResponseFuture responseFuture = new CommandResponseFuture();
        channel.attr(Constants.DEFAULT_ATTRIBUTE).set(responseFuture);
        channel.writeAndFlush(command.getBuf());
        CommandResponse response1 = responseFuture.get(optTimeOut, TimeUnit.MILLISECONDS);
        System.out.println(response1.getContent().toString(CharsetUtil.UTF_8));
    }

}
