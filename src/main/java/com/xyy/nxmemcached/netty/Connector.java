package com.xyy.nxmemcached.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.DefaultThreadFactory;

import java.util.concurrent.TimeUnit;

import com.xyy.nxmemcached.ClientConnectionHandler;

public class Connector {

    private Bootstrap bootstrap = new Bootstrap();

    public Connector(int threadPoolSize, int connectTimeOut, int idleTime) {
        init(threadPoolSize, connectTimeOut, idleTime);
    }

    private void init(int threadPoolSize, int connectTimeOut, final int idleTime) {
        final ClientConnectionHandler handler = new ClientConnectionHandler();
        EventLoopGroup workerGroup = new NioEventLoopGroup(threadPoolSize, new DefaultThreadFactory("memcached-client",
                Boolean.TRUE));
        bootstrap.group(workerGroup).channel(NioSocketChannel.class).option(ChannelOption.SO_KEEPALIVE, Boolean.TRUE)
                .option(ChannelOption.TCP_NODELAY, Boolean.TRUE)
                .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, connectTimeOut)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new IdleStateHandler(idleTime, 0, 0, TimeUnit.MILLISECONDS));
                        // TODO
                        pipeline.addLast(null, handler);
                    }
                });
    }

}
