package com.xyy.nxmemcached.netty;

import io.netty.channel.Channel;
import io.netty.util.internal.ThreadLocalRandom;

import java.net.InetSocketAddress;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionPool {
    
    private Connector connector;
    
    private ConcurrentHashMap<String, ConnectionPool> connectionPool = new ConcurrentHashMap<String, ConnectionPool>();
    
    private Channel[] channelList;
    
    InetSocketAddress mcServerAddr;
    
    private Integer connections = 2;
    
    public ConnectionPool(Connector connector, InetSocketAddress mcServerAddr, Integer connections) {
        channelList = new Channel[connections];
        this.mcServerAddr = mcServerAddr;
        this.connector = connector;
        this.connections = connections;
    }
    
    public void getChannel() {
        final int index = ThreadLocalRandom.current().nextInt(0, connections);
    }


}
