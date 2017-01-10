package com.xyy.nxmemcached.seriable;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.caucho.hessian.io.HessianInput;
import com.caucho.hessian.io.HessianOutput;

public class HessianSerializable implements Serializable {

	private final static int INIT_SIZE = 1024 ;
	
	@Override
	public byte[] object2Bytes(Object object) throws IOException {
		ByteArrayOutputStream baos = null ;
		HessianOutput output = null ;
		try {
			baos = new ByteArrayOutputStream( INIT_SIZE ) ; 
			output = new HessianOutput( baos ) ;
			output.startCall() ;
			output.writeObject(object) ;
			output.completeCall() ;
		} catch ( IOException  e ) { 
			throw e  ;
		} finally {
			if( output != null ) { 
				try {
					baos.close() ;
				} catch ( IOException e ) {
					e.printStackTrace() ;
				}
			}
		}
		return baos != null ? baos.toByteArray() : null ;
	}

	@Override
	public Object bytes2Object(byte[] bytes) throws IOException {
		Object object = null ;
		ByteArrayInputStream bais = null ;
		HessianInput input = null ;
		try {
			 bais = new ByteArrayInputStream( bytes ) ;
			 input = new HessianInput( bais ) ;
			 input.startReply() ;
			 object = input.readObject() ;
			 input.completeReply() ;
		} catch( IOException e ) {
			throw e  ;
		} catch (Throwable e) {
			throw new IOException( e ) ; 
		} 
		finally {
			if( bais != null ) {
				try {
					bais.close() ;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return object ;
	}
	
}
