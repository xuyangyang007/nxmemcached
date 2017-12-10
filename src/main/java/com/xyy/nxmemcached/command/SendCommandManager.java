package com.xyy.nxmemcached.command;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.xyy.nxmemcached.algorithm.MemcachedSessionLocator;
import com.xyy.nxmemcached.common.Constants;
import com.xyy.nxmemcached.exception.CacheException;
import com.xyy.nxmemcached.netty.ConnectionPool;

import io.netty.channel.Channel;

public class SendCommandManager {
	
	MemcachedSessionLocator locator = null;
	
    public SendCommandManager(MemcachedSessionLocator locator) {
		super();
		this.locator = locator;
	}

    public CommandResponse sendCommand(Command command, long optTimeOut) throws InterruptedException, CacheException, TimeoutException {
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
        channel.attr(Constants.DEFAULT_COMMAND).set(command);
        channel.writeAndFlush(command.getBuf());
        CommandResponse response = responseFuture.get(optTimeOut, TimeUnit.MILLISECONDS);
        session.returnChannel(channel);
        return response;
    }

}
