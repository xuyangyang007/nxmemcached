package com.xyy.nxmemcached.command;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.charset.Charset;

import com.xyy.nxmemcached.common.Constants;

/**
 * 
 * @author yangyang.xu
 *
 */
public class TextDeleteCommand extends Command {
	
	private final static String DELETED = "DELETED\r\n";

    @Override
    public void encode() {
        int size = Constants.DELETE.length + 1 + this.keyBytes.length
                + Constants.CRLF.length;
        this.buf = Unpooled.directBuffer(size);
        buf.writeBytes(Constants.DELETE);
        buf.writeBytes(Constants.SPACE);
        buf.writeBytes(keyBytes);
        buf.writeBytes(Constants.CRLF);
    }

    @Override
    public CommandResponse decode(ByteBuf buf) {
    	if (buf.readableBytes() < 7) {
    		return null;
    	}
		String r = buf.toString( Charset.defaultCharset() );
		buf.skipBytes(buf.readableBytes());
		if (r.equals(DELETED)) {
			return CommandResponse.newSuccess(buf);
		} else {
			return CommandResponse.newError(buf);
		}
    }

}
