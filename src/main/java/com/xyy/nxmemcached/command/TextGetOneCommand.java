package com.xyy.nxmemcached.command;

import java.nio.charset.Charset;

import com.xyy.nxmemcached.common.Constants;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;


public class TextGetOneCommand  extends Command {
	
	private ParseStatus parseStatus = ParseStatus.NULL;
	
	private Integer dataLength = 0;

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
    	//System.out.println(buf.toString(Charset.forName("utf-8")) + " size:"+ buf.capacity());
    	CommandResponse response = CommandResponse.newSuccess(null);
    	while (true) {
    		if (buf == null || buf.readableBytes() < MIN_LENGTH) {
    			return CommandResponse.newError(buf);
    		}
    		Integer readInex = buf.readerIndex();
    		switch (this.parseStatus) {
    		case NULL:
    			if (buf.getByte(readInex) == 'E' && buf.getByte(readInex + 1) == 'N') {
    				this.parseStatus = ParseStatus.END;
    			} else if (buf.getByte(readInex) == 'V') {
    				this.parseStatus = ParseStatus.VALUE;
    			}
    			continue;
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
    			int index = buf.indexOf(buf.readerIndex(), buf.readableBytes(), (byte)' ');
    			buf.skipBytes(index - buf.readerIndex() +1);
    			this.parseStatus = ParseStatus.DATA_LEN;
    			continue;
    		case DATA_LEN:
    			index = buf.indexOf(buf.readerIndex(), buf.readableBytes(), (byte)'\r');
    			int ll = index - buf.readerIndex();
    			ByteBuf temp = buf.slice(buf.readerIndex(), ll);
    			String dataStr = temp.toString(Charset.defaultCharset());
    			dataLength = Integer.parseInt(dataStr);
    			buf.skipBytes(ll + 2);
    			this.parseStatus = ParseStatus.DATA_LEN_DONE;
    			continue;
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
    			this.parseStatus = ParseStatus.NULL;
    			return CommandResponse.newSuccess(buf.slice(buf.readerIndex(), dataLength));
			default:
				return CommandResponse.newError(buf);
    		}
    	}
    }

}
