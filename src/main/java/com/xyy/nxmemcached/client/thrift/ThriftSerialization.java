package com.xyy.nxmemcached.client.thrift;

import org.apache.thrift.TBase;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TMemoryBuffer;

import com.xyy.nxmemcached.client.ISerialization;
import com.xyy.nxmemcached.exception.SerializationException;

public class ThriftSerialization implements ISerialization {
	
	private ThriftSerialization(){};
	private static ThriftSerialization serialization = new ThriftSerialization();
	
	public static ThriftSerialization getInstance() {
		return serialization;
	}
	
	public <T> byte[] serialize(T object) throws SerializationException {
		TMemoryBuffer mb0 = new TMemoryBuffer(32);
		TProtocol prot0 = new org.apache.thrift.protocol.TCompactProtocol(mb0);
		try {
			((TBase)object).write(prot0);
		} catch (TException e) {
			SerializationException exception = new SerializationException(e);
			throw exception;
		}
		return mb0.getArray();
	}
	
	public <T> T deserialize(byte[] data, Class<T> cls) throws SerializationException {
		try {
			T tbase = cls.newInstance();
			TMemoryBuffer tmb = new TMemoryBuffer(32);
			tmb.write(data);
			TProtocol tp = new org.apache.thrift.protocol.TCompactProtocol(tmb);
			((TBase)tbase).read(tp);
			return tbase;
		} catch (Exception e) {
			SerializationException exception = new SerializationException(e);
			throw exception;
		}
	}

}
  
