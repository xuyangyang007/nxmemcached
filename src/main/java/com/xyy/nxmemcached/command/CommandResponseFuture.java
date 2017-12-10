package com.xyy.nxmemcached.command;

import io.netty.channel.Channel;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
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
    
    public CommandResponse get(long timeout, TimeUnit unit) throws TimeoutException, InterruptedException {
        if (!latch.await(timeout, unit)) {
            throw new TimeoutException();
        }
        return response;
    }

    public boolean done() {
        if (isProcessed.getAndSet(true)) {
            return false;
        }
        
        isDone = true;
        latch.countDown();
        return true;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean isDone) {
        this.isDone = isDone;
    }

    public boolean isCancel() {
        return isCancel;
    }

    public void setCancel(boolean isCancel) {
        this.isCancel = isCancel;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public CommandResponse getResponse() {
        return response;
    }

    public void setResponse(CommandResponse response) {
        this.response = response;
    }

    public CountDownLatch getLatch() {
        return latch;
    }

    public AtomicBoolean getIsProcessed() {
        return isProcessed;
    }

}
