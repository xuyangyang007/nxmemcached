package com.xyy.nxmemcached.command;

import io.netty.buffer.ByteBuf;

import java.util.concurrent.CountDownLatch;

public abstract class Command {
    
    protected String key;
    protected byte[] keyBytes;
    protected volatile Object result;
    protected CountDownLatch latch;
    protected CommandType commandType;
    protected volatile ByteBuf buf;
    
    public abstract void encode();

    public abstract boolean decode(ByteBuf buf);
    
    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
    }
    public byte[] getKeyBytes() {
        return keyBytes;
    }
    public void setKeyBytes(byte[] keyBytes) {
        this.keyBytes = keyBytes;
    }
    public Object getResult() {
        return result;
    }
    public void setResult(Object result) {
        this.result = result;
    }
    public CountDownLatch getLatch() {
        return latch;
    }
    public void setLatch(CountDownLatch latch) {
        this.latch = latch;
    }
    public CommandType getCommandType() {
        return commandType;
    }
    public void setCommandType(CommandType commandType) {
        this.commandType = commandType;
    }
    public ByteBuf getBuf() {
        return buf;
    }
    public void setBuf(ByteBuf buf) {
        this.buf = buf;
    }

}
