package com.xyy.nxmemcached.command;

import io.netty.buffer.ByteBuf;

public abstract class Command {
    
    protected String key;
    protected byte[] keyBytes;
    protected volatile Object result;
    protected CommandType commandType;
    protected volatile ByteBuf buf;
    protected boolean noreply;
    public Command() {}
    
    
    public Command(String key, byte[] keyBytes, Object result,  CommandType commandType, boolean noreply) {
		super();
		this.key = key;
		this.keyBytes = keyBytes;
		this.result = result;
		this.commandType = commandType;
		this.noreply = noreply;
	}

	public abstract void encode();

    public abstract CommandResponse decode(ByteBuf buf);
    
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

    public boolean isNoreply() {
        return noreply;
    }

    public void setNoreply(boolean noreply) {
        this.noreply = noreply;
    }
}
