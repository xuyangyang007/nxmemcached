package com.xyy.nxmemcached.common;

import io.netty.util.AttributeKey;

public class Constants {
    public static final byte[] CRLF = { '\r', '\n' };
    public static final byte[] GET = { 'g', 'e', 't' };
    public static final byte[] GETS = { 'g', 'e', 't', 's' };
    public static final byte[] SPACE = {' '};
    public static final byte[] INCR = { 'i', 'n', 'c', 'r' };
    public static final byte[] DECR = { 'd', 'e', 'c', 'r' };
    public static final byte[] DELETE = { 'd', 'e', 'l', 'e', 't', 'e' };
    public static final byte[] TOUCH = { 't', 'o', 'u', 'c', 'h' };
    public static final byte[] NO_REPLY = { 'n', 'o', 'r', 'e', 'p', 'l', 'y' };

    public static final AttributeKey<Object> DEFAULT_ATTRIBUTE = AttributeKey.valueOf("response");

    public static ThreadLocal<String> t = new ThreadLocal<String>();
}
