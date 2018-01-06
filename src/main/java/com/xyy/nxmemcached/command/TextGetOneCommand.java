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
    }

    @Override
    public CommandResponse decode(ByteBuf buf) {
    	//System.out.println("=== " + buf.toString(Charset.forName("utf-8")) + " size:"+ buf);
    	CommandResponse response = null;
    	if (this.getFuture() != null && this.getFuture().getResponse() != null) {
    		response = this.getFuture().getResponse();
    	} else {
    		response = CommandResponse.newSuccess(null);
    		this.getFuture().setResponse(response);
    	}
    	while (true) {
    		if (buf == null || buf.readableBytes() < MIN_LENGTH) {
    			return null;
    		}
    		Integer readInex = buf.readerIndex();
    		switch (this.parseStatus) {
    		case NULL:
    			if (buf.readableBytes() < 2) {
    				return null;
    			}
    			if (buf.getByte(readInex) == 'E' && buf.getByte(readInex + 1) == 'N') {
    				this.parseStatus = ParseStatus.END;
    			} else if (buf.getByte(readInex) == 'V') {
    				this.parseStatus = ParseStatus.VALUE;
    			}
    			continue;
    		case END:
    			if (buf.readableBytes() < 5) {
    				return null;
    			}
    			buf.skipBytes(5);
    			this.parseStatus = ParseStatus.END;
    			return response;
    		case VALUE:
    			if (buf.readableBytes() < 6) {
    				return null;
    			}
    			buf.skipBytes(6);
    			this.parseStatus = ParseStatus.KEY;
    			continue;
    		case KEY:
    			if (buf.readableBytes() < this.keyBytes.length + 1) {
    				return null;
    			}
    			buf.skipBytes(this.keyBytes.length + 1);
    			this.parseStatus = ParseStatus.FLAG;
    			continue;
    		case FLAG:
    			int index = buf.indexOf(buf.readerIndex(), buf.readerIndex() + buf.readableBytes(), (byte)' ');
    			if (buf.readableBytes() < index - buf.readerIndex() +1) {
    				return null;
    			}
    			buf.skipBytes(index - buf.readerIndex() +1);
    			this.parseStatus = ParseStatus.DATA_LEN;
    			continue;
    		case DATA_LEN:
    			//System.out.println("=====\r\n" + buf.toString(Charset.forName("utf-8")) + " ====ERROR size:"+ buf.capacity());
    			index = buf.indexOf(buf.readerIndex(), buf.readerIndex() + buf.readableBytes(), (byte)'\r');
    			int ll = index - buf.readerIndex();
    			if (buf.readableBytes() < ll + 2) {
    				return null;
    			}
    			ByteBuf temp = buf.slice(buf.readerIndex(), ll);
    			String dataStr = temp.toString(Charset.defaultCharset());
    			dataLength = Integer.parseInt(dataStr);
    			buf.skipBytes(ll + 2);
    			this.parseStatus = ParseStatus.DATA_LEN_DONE;
    			continue;
    		case DATA_LEN_DONE:
    			if (buf.readableBytes() < 1) {
    				return null;
    			} else {
    				this.parseStatus = ParseStatus.DATA;
    				continue;
    			}
    		case CAS:
    			// TODO
    		case CAS_DONE:
    			// TODO
    		case DATA:
    			if (buf.readableBytes() < dataLength + 2) {
    				return null;
    			}
    			this.parseStatus = ParseStatus.NULL;
    			//response = CommandResponse.newSuccess(buf.slice(buf.readerIndex(), dataLength));
    			byte[] byteList = new byte[dataLength];
    			buf.readBytes(byteList, 0 , dataLength);
    			response.byteList = byteList;
    			buf.skipBytes(2);
    			continue;
			default:
				return null;
    		}
    	}
    }

}
