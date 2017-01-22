package com.xyy.nxmemcached.client.thrift;

import org.apache.thrift.TBase;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TMemoryBuffer;

public class ThriftSerializeUtil {
	
	public static <T extends TBase> byte[] serialize(T object) throws TException {
		TMemoryBuffer mb0 = new TMemoryBuffer(32);
		TProtocol prot0 = new org.apache.thrift.protocol.TCompactProtocol(mb0);
		object.write(prot0);
		return mb0.getArray();
	}
	
	public static <T extends TBase> T deSerialize(byte[] cacheVal, Class<T> clasz) throws Exception {
		T tbase = clasz.newInstance();
		TMemoryBuffer tmb = new TMemoryBuffer(32);
		tmb.write(cacheVal);
		TProtocol tp = new org.apache.thrift.protocol.TCompactProtocol(tmb);
		tbase.read(tp);
		return tbase;
	}

}
  
