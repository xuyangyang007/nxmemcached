package com.xyy.nxmemcached.command;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import com.xyy.nxmemcached.common.Constants;

/**
 * 
 * @author yangyang.xu
 *
 */
public class TextDeleteCommand extends Command {

    @Override
    public void encode() {
        int size = Constants.DELETE.length + 1 + this.keyBytes.length
                + Constants.CRLF.length;
        if (isNoreply()) {
            size += 8;
        }
        this.buf = Unpooled.directBuffer(size);
        byte[] cmdBytes = Constants.DELETE;
        buf.writeBytes(cmdBytes);
        buf.writeBytes(Constants.SPACE);
        buf.writeBytes(keyBytes);
        buf.writeBytes(Constants.CRLF);
        if (isNoreply()) {
            buf.writeBytes(Constants.NO_REPLY);
        }
        buf.retain();
    }

    @Override
    public CommandResponse decode(ByteBuf buf) {
        return CommandResponse.newSuccess(buf);
    }

}
