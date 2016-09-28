package com.xyy.nxmemcached.exception;

public class CacheException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public CacheException() {
        super();
    }

    public CacheException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
        super(arg0, arg1, arg2, arg3);
    }

    public CacheException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    public CacheException(String arg0) {
        super(arg0);
    }

    public CacheException(Throwable arg0) {
        super(arg0);
    }

}
