package com.xyy.nxmemcached;

public class Test {
    
    public static void main(String[] args) throws Exception {
        System.out.println("===");
        EchoClient client = new EchoClient("ip", port);
        client.start();
    }

}
