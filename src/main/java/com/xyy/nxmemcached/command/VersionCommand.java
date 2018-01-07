/**  
 * Project Name:nxmemcached  
 * File Name:VersionCommand.java  
 * Package Name:com.xyy.nxmemcached.command  
 * Date:2018年1月7日上午11:57:39  
 * Copyright (c) 2018, vipshop.com All Rights Reserved.  
 *  
*/  
  
package com.xyy.nxmemcached.command;

import com.xyy.nxmemcached.common.Constants;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**  
 * ClassName:VersionCommand <br/>  
 * Function: T获取版本号. <br/>  
 * Date:     2018年1月7日 上午11:57:39 <br/>  
 * @author   yangyang.xu  
 * @version    
 * @since    JDK 1.7
 * @see        
 */
public class VersionCommand extends Command  {
	
	byte[] req = new byte[] { 'v', 'e', 'r', 's', 'i', 'o', 'n' };

	@Override
	public void encode() {
		this.buf = Unpooled.directBuffer(req.length);
		this.buf.writeBytes(req);
	}

	@Override
	public CommandResponse decode(ByteBuf buf) {
		return null;
	}

}
  
