package com.xyy.nxmemcached.command;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutionException;

import com.xyy.nxmemcached.exception.CacheException;
import com.xyy.nxmemcached.netty.ConnectionPool;
import com.xyy.nxmemcached.netty.Connector;

public class SendCommandManager {
    
    
    public static void main(String[] args) throws CacheException, InterruptedException, ExecutionException {
        Connector connector = new Connector(2, 30000, 30000);
        InetSocketAddress address = new InetSocketAddress("10.199.198.219", 11211);
        ConnectionPool pool = new ConnectionPool(connector, address, 2);
        Channel channel = pool.getChannel();
        TextGetOneCommand command = new TextGetOneCommand();
        command.setKey("m_1");
        command.setKeyBytes("m_1".getBytes());
        command.setCommandType(CommandType.GET_ONE);
        command.encode();
        ChannelFuture future = channel.writeAndFlush(command.getBuf());
        Object obj = future.get();
        TextGetOneCommand command2 = new TextGetOneCommand();
        command2.setKey("m_1");
        command2.setKeyBytes("m_1".getBytes());
        command2.setCommandType(CommandType.GET_ONE);
        command2.encode();
        channel.writeAndFlush(command2.getBuf());
        Thread.sleep(30000000);
    }

}
