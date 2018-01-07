/**  
 * Project Name:nxmemcached  
 * File Name:ISerialization.java  
 * Package Name:com.xyy.nxmemcached.client  
 * Date:2018年1月7日上午10:35:20  
 * Copyright (c) 2018, vipshop.com All Rights Reserved.  
 *  
*/  
  
package com.xyy.nxmemcached.client;

import com.xyy.nxmemcached.exception.SerializationException;

/**  
 * ClassName:ISerialization <br/>  
 * Reason:   序列化. <br/>  
 * Date:     2018年1月7日 上午10:35:20 <br/>  
 * @author   yangyang.xu  
 * @version    
 * @since    JDK 1.7
 * @see        
 */
public interface ISerialization {

    public <T> byte[] serialize(T obj) throws SerializationException;

    public <T> T deserialize(byte[] data, Class<T> cls) throws SerializationException;

}
  
