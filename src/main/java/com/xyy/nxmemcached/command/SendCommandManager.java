package com.xyy.nxmemcached.command;

import io.netty.channel.Channel;

import java.net.InetSocketAddress;

import com.xyy.nxmemcached.exception.CacheException;
import com.xyy.nxmemcached.netty.ConnectionPool;
import com.xyy.nxmemcached.netty.Connector;

public class SendCommandManager {
    
    
    public static void main(String[] args) throws CacheException, InterruptedException {
        Connector connector = new Connector(2, 30000, 30000);
        InetSocketAddress address = new InetSocketAddress("ip", 11211);
        ConnectionPool pool = new ConnectionPool(connector, address, 2);
        Channel channel = pool.getChannel();
        TextGetOneCommand command = new TextGetOneCommand();
        command.setKey("m_1");
        command.setKeyBytes("m_1".getBytes());
        command.setCommandType(CommandType.GET_ONE);
        command.encode();
        channel.writeAndFlush(command.getBuf(), channel.voidPromise());
        Thread.sleep(30000);
    }

}
