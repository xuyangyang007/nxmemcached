package com.xyy.nxmemcached.command;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author yangyang.xu
 *
 */
public class CommandResponseFuture {
    
    private final CountDownLatch latch = new CountDownLatch(1);
    
    private volatile boolean isDone = false;
    
    private volatile boolean isCancel = false;
    
    private final AtomicBoolean isProcessed = new AtomicBoolean(false);
    
    private volatile Channel channel;
    
    private CommandResponse response;
    
    
    public boolean cancel(Throwable cause) {
        if (isProcessed.getAndSet(true)) {
            return false;
        }
        response = new CommandResponse();
        response.setSuccess(false);
        response.setCause(cause);
        isCancel = true;
        latch.countDown();
        return true;
    }
    
    public CommandResponse get() throws InterruptedException {
        latch.await();
        return response;
    }



}
