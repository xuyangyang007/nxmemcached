/**  
 * Project Name:nxmemcached  
 * File Name:SerializationException.java  
 * Package Name:com.xyy.nxmemcached.exception  
 * Date:2018年1月7日上午10:39:41  
 * Copyright (c) 2018, vipshop.com All Rights Reserved.  
 *  
*/  
  
package com.xyy.nxmemcached.exception;  
/**  
 * ClassName:SerializationException <br/>  
 * Date:     2018年1月7日 上午10:39:41 <br/>  
 * @author   yangyang.xu  
 * @version    
 * @since    JDK 1.7
 * @see        
 */
public class SerializationException extends Exception {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

	public SerializationException() {
		  
		super();  
		
	}

	public SerializationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		  
		super(message, cause, enableSuppression, writableStackTrace);  
	}

	public SerializationException(String message, Throwable cause) {
		  
		super(message, cause);  
		
	}

	public SerializationException(String message) {
		  
		super(message);  
		
	}

	public SerializationException(Throwable cause) {
		  
		super(cause);  
		
	}
    
    

}
  
