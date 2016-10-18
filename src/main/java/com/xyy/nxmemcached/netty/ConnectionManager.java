package com.xyy.nxmemcached.netty;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.TreeMap;

import com.xyy.nxmemcached.algorithm.HashAlgorithm;


/**
 * @author yangyang.xu
 *
 */
public class ConnectionManager {
    
    private transient volatile TreeMap<Long, List<ConnectionPool>> ketamaSessions = new TreeMap<Long, List<ConnectionPool>>();
    
    private static final int NUM_REPS = 160;
    
    
    private final void buildMap(Collection<ConnectionPool> list, HashAlgorithm alg) {
        TreeMap<Long, List<ConnectionPool>> sessionMap = new TreeMap<Long, List<ConnectionPool>>();

        for (ConnectionPool session : list) {
            String sockStr = null;
            /**
             * Duplicate 160 X weight references
             */
            int numReps = NUM_REPS;
            if (alg == HashAlgorithm.KETAMA_HASH) {
                for (int i = 0; i < numReps / 4; i++) {
                    byte[] digest = HashAlgorithm.computeMd5(sockStr + "-" + i);
                    for (int h = 0; h < 4; h++) {
                        long k = (long) (digest[3 + h * 4] & 0xFF) << 24
                                | (long) (digest[2 + h * 4] & 0xFF) << 16
                                | (long) (digest[1 + h * 4] & 0xFF) << 8
                                | digest[h * 4] & 0xFF;
                        this.getSessionList(sessionMap, k).add(session);
                    }

                }
            } else {
                for (int i = 0; i < numReps; i++) {
                    long key = alg.hash(sockStr + "-" + i);
                    this.getSessionList(sessionMap, key).add(session);
                }
            }
        }
        this.ketamaSessions = sessionMap;
    }

    private List<ConnectionPool> getSessionList(
            TreeMap<Long, List<ConnectionPool>> sessionMap, long k) {
        List<ConnectionPool> sessionList = sessionMap.get(k);
        if (sessionList == null) {
            sessionList = new ArrayList<ConnectionPool>();
            sessionMap.put(k, sessionList);
        }
        return sessionList;
    }

}
