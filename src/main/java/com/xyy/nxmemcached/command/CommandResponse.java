package com.xyy.nxmemcached.command;

import io.netty.buffer.ByteBuf;

public class CommandResponse {
    
    private volatile boolean success = false;
    
    private volatile ByteBuf content;
    
    private volatile Throwable cause;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public ByteBuf getContent() {
        return content;
    }

    public void setContent(ByteBuf content) {
        this.content = content;
    }

    public Throwable getCause() {
        return cause;
    }

    public void setCause(Throwable cause) {
        this.cause = cause;
    }
    
    public static CommandResponse newSuccess(ByteBuf buf) {
        CommandResponse response = new CommandResponse();
        response.setSuccess(true);
        response.setContent(buf);
        return response;
    }
    
}
