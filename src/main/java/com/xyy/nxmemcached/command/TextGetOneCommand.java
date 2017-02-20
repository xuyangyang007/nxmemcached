package com.xyy.nxmemcached.command;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.rubyeye.xmemcached.command.text.TextGetCommand.ParseStatus;

import com.xyy.nxmemcached.common.Constants;


public class TextGetOneCommand  extends Command {
	
	private ParseStatus parseStatus = ParseStatus.NULL;

	public static enum ParseStatus {
		NULL, VALUE, KEY, FLAG, DATA_LEN, DATA_LEN_DONE, CAS, CAS_DONE, DATA, END
	}
	
	public static final int MIN_LENGTH = 5;

    @Override
    public void encode() {
        byte[] cmdBytes = this.commandType == CommandType.GET_ONE
                || this.commandType == CommandType.GET_MANY ? Constants.GET
                : Constants.GETS;
        this.buf = Unpooled.directBuffer(cmdBytes.length
              + Constants.CRLF.length + 1 + this.keyBytes.length);
        buf.writeBytes(cmdBytes);
        buf.writeBytes(Constants.SPACE);
        buf.writeBytes(keyBytes);
        buf.writeBytes(Constants.CRLF);
        buf.retain();
    }

    @Override
    public CommandResponse decode(ByteBuf buf) {
    	CommandResponse response = CommandResponse.newSuccess(null);
    	while (true) {
    		if (buf == null || buf.readableBytes() < MIN_LENGTH) {
    			return CommandResponse.newError(buf);
    		}
    		switch (this.parseStatus) {
    		case NULL:
    			if (buf.getByte(0) == 'E' && buf.getByte(1) == 'N') {
    				this.parseStatus = ParseStatus.END;
    			} else if (buf.getByte(0) == 'V') {
    				this.parseStatus = ParseStatus.VALUE;
    			}
    		case END:
    			if (buf.readableBytes() < 5) {
    				return CommandResponse.newSuccess(null);
    			}
    			buf.skipBytes(5);
    			return response;
    		case VALUE:
    			if (buf.readableBytes() < 6) {
    				return CommandResponse.newSuccess(null);
    			}
    			buf.skipBytes(6);
    			this.parseStatus = ParseStatus.KEY;
    			continue;
    		case KEY:
    			if (buf.readableBytes() < this.keyBytes.length + 1) {
    				return CommandResponse.newSuccess(null);
    			}
    			buf.skipBytes(this.keyBytes.length + 1);
    			this.parseStatus = ParseStatus.FLAG;
    			continue;
    		case FLAG:
    			int index = buf.indexOf(0, buf.readableBytes(), (byte)' ');
    			buf.skipBytes(index);
    		case DATA_LEN:
    			index = buf.indexOf(0, buf.readableBytes(), (byte)'\r');
    			buf.skipBytes(index);
    		case DATA_LEN_DONE:
    			if (buf.readableBytes() < 1) {
    				return CommandResponse.newSuccess(null);
    			} else if (buf.getByte(0) == '\n'){
    				buf.skipBytes(1);
    				this.parseStatus = ParseStatus.DATA;
    				continue;
    			} else {
    				this.parseStatus = ParseStatus.CAS;
    				continue;
    			}
    		case CAS:
    			// TODO
    		case CAS_DONE:
    			// TODO
    		case DATA:
    			
			default:
				return CommandResponse.newError(buf);
    		}
    	}
    }

}
