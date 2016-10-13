package com.xyy.nxmemcached.netty;

import io.netty.channel.Channel;

import java.net.InetSocketAddress;
import java.util.concurrent.LinkedBlockingQueue;

import com.xyy.nxmemcached.exception.CacheException;

public class ConnectionPool {
    
    private Connector connector;
    
    private LinkedBlockingQueue<Channel> channelList;
    
    InetSocketAddress mcServerAddr;
    
    private Integer connections = 2;
    
    public ConnectionPool(Connector connector, InetSocketAddress mcServerAddr, Integer connections) {
        channelList = new LinkedBlockingQueue<Channel>();
        this.mcServerAddr = mcServerAddr;
        this.connector = connector;
        this.connections = connections;
    }
    
    public Channel getChannel() throws CacheException {
        Channel channel = channelList.poll();
        if (channel != null && channel.isActive()) {
            return channel;
        }
        String host = mcServerAddr.getAddress().getHostAddress();
        int port = mcServerAddr.getPort();

        channel = connector.connect(host, port);

        channelList.offer(channel);

        return channel;
    }


}
