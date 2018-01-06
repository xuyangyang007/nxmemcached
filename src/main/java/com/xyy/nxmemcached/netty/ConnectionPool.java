package com.xyy.nxmemcached.netty;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import com.xyy.nxmemcached.exception.CacheException;

import io.netty.channel.Channel;

/**
 * @author yangyang.xu
 *
 */
public class ConnectionPool {
    
    private Connector connector;
    
    private List<Channel> channelList;
    
    InetSocketAddress mcServerAddr;
    
    public ConnectionPool(Connector connector, InetSocketAddress mcServerAddr, Integer connections) throws CacheException {
        channelList = new ArrayList<Channel>();
        this.mcServerAddr = mcServerAddr;
        this.connector = connector;
        String host = mcServerAddr.getAddress().getHostAddress();
        int port = mcServerAddr.getPort();

        for (int i=1;i<=connections;i++) {
        	Channel channel = connector.connect(host, port);
        	channelList.add(channel);
        }
    }
    
    public Channel getChannel() throws CacheException, InterruptedException {
    	Integer index = 0;
    	if (channelList.size() > 1) {
    		index = ThreadLocalRandom.current().nextInt(channelList.size());
    	}
        Channel channel = channelList.get(index);
        if (channel != null && channel.isActive()) {
            return channel;
        }
        synchronized (this) {
	        String host = mcServerAddr.getAddress().getHostAddress();
	        int port = mcServerAddr.getPort();
	
	        channel = connector.connect(host, port);
	
	        channelList.set(index, channel);
        }

        return channel;
    }

	public InetSocketAddress getMcServerAddr() {
		return mcServerAddr;
	}

	public void setMcServerAddr(InetSocketAddress mcServerAddr) {
		this.mcServerAddr = mcServerAddr;
	}

}
