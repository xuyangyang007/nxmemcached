  
package com.xyy.nxmemcached.command;

import java.nio.charset.Charset;
import java.util.concurrent.CountDownLatch;

import com.xyy.nxmemcached.common.Constants;
import com.xyy.nxmemcached.seriable.Transcoder;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class TextStoreCommand extends Command {
	
	private final static String STORED = "STORED";
	private final static String NOT_STORED = "NOT_STORED"; 
	private final static String EXISTS = "EXISTS";
	private final static String NOT_FOUND = "NOT_FOUND";
	
	protected int expTime;
	protected long cas;
	protected ByteBuf value;
	private Store store ;
	
	public static enum Store {
		SET(new byte[] { 's', 'e', 't' }),
		Add(new byte[] { 'a', 'd', 'd' }), 
		REPLACE(new byte[] { 'r', 'e', 'p', 'l', 'a', 'c', 'e' }), 
		CAS(new byte[] { 'c', 'a', 's' }), 
		APPEND(new byte[] { 'a', 'p', 'p', 'e', 'n', 'd' }), 
		PREPEND(new byte[] { 'p', 'r', 'e', 'p', 'e', 'n', 'd' });
		
		private Store(byte[] store) {
			this.store = store;
		}

		private byte[] store;

		public byte[] getStore() {
			return store;
		}
	}
	
	public TextStoreCommand(String key, byte[] keyBytes, CommandType cmdType,
			CountDownLatch latch, int expTime, long cas, ByteBuf value, boolean noreply) {
		super(key, keyBytes, value, latch, cmdType, noreply);
		this.expTime = expTime;
		this.cas = cas;
		this.value = value;
	}

	@Override
	public void encode() {
		byte[] store = this.store.getStore() ;
		byte[] keyb = key.getBytes( Charset.defaultCharset() ) ;
		Transcoder transcoder = Transcoder.transcoder( value ) ; // 此步骤抽象出来
		byte[] data = transcoder.object2Bytes( value ) ; 
		byte[] fBytes = String.valueOf( transcoder.getFlag() ).getBytes() ; //TODO  
		byte[] expectBytes = String.valueOf(expTime).getBytes() ;
		byte[] length = String.valueOf( data.length ).getBytes() ;
		ByteBuf buf = null ;
		if( this.store == Store.CAS ) {
			buf = Unpooled.copiedBuffer( store , Constants.SPACE , keyb , Constants.SPACE ,
					fBytes , Constants.SPACE , expectBytes , Constants.SPACE , length , Constants.SPACE , String.valueOf( cas ).getBytes() , Constants.CRLF , data ,   Constants.CRLF) ;
		} else {
			buf = Unpooled.copiedBuffer(store, Constants.SPACE, keyb, Constants.SPACE, fBytes, Constants.SPACE, expectBytes, Constants.SPACE, length, Constants.SPACE, data, Constants.SPACE);
		}
		int readerIndex = buf.readableBytes() ;
		byte[] bytes = new byte[readerIndex] ;
		buf.readBytes(bytes) ;
	}

	@Override
	public boolean decode(ByteBuf buf) {
		String result = buf.toString(Charset.defaultCharset());
		if(result.equals(STORED)) {
			return true;
		} else {
			return false;
		}
	}

	public int getExpTime() {
		return expTime;
	}

	public void setExpTime(int expTime) {
		this.expTime = expTime;
	}

	public long getCas() {
		return cas;
	}

	public void setCas(long cas) {
		this.cas = cas;
	}

	public ByteBuf getValue() {
		return value;
	}

	public void setValue(ByteBuf value) {
		this.value = value;
	}

}
  
