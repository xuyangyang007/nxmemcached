package com.xyy.nxmemcached.command;

import io.netty.channel.Channel;
import io.netty.util.CharsetUtil;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutionException;

import com.xyy.nxmemcached.common.Constants;
import com.xyy.nxmemcached.exception.CacheException;
import com.xyy.nxmemcached.netty.ConnectionPool;
import com.xyy.nxmemcached.netty.Connector;

public class SendCommandManager {
    
    
    public static void main(String[] args) throws CacheException, InterruptedException, ExecutionException {
        Connector connector = new Connector(2, 30000, 30000);
        InetSocketAddress address = new InetSocketAddress("ip", 11211);
        ConnectionPool pool = new ConnectionPool(connector, address, 2);
        Channel channel = pool.getChannel();
        sendCommand(channel);
        sendCommand(channel);
        Thread.sleep(30000000);
    }

    private static void sendCommand(Channel channel) throws InterruptedException {
        TextGetOneCommand command = new TextGetOneCommand();
        command.setKey("m_1");
        command.setKeyBytes("m_1".getBytes());
        command.setCommandType(CommandType.GET_ONE);
        command.encode();
        CommandResponseFuture responseFuture = new CommandResponseFuture();
        channel.attr(Constants.DEFAULT_ATTRIBUTE).set(responseFuture);
        channel.writeAndFlush(command.getBuf());
        CommandResponse response1 = responseFuture.get();
        System.out.println(response1.getContent().toString(CharsetUtil.UTF_8));
    }

}
