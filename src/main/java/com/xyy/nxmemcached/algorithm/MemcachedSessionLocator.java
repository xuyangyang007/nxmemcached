package com.xyy.nxmemcached.algorithm;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.TreeMap;

import com.xyy.nxmemcached.netty.ConnectionPool;


public class MemcachedSessionLocator {

	static final int NUM_REPS = 160;
	private transient volatile TreeMap<Long, List<ConnectionPool>> ketamaSessions = new TreeMap<Long, List<ConnectionPool>>();
	private final HashAlgorithm hashAlg;
	private final Random random = new Random();

	public MemcachedSessionLocator() {
		this.hashAlg = HashAlgorithm.KETAMA_HASH;
	}

	public MemcachedSessionLocator(List<ConnectionPool> list, HashAlgorithm alg) {
		super();
		this.hashAlg = alg;
		this.buildMap(list, alg);
	}

	private final void buildMap(Collection<ConnectionPool> list, HashAlgorithm alg) {
		TreeMap<Long, List<ConnectionPool>> sessionMap = new TreeMap<Long, List<ConnectionPool>>();

		for (ConnectionPool session : list) {
			InetSocketAddress serverAddress = session.getMcServerAddr();
			String sockStr = serverAddress.getAddress().getHostAddress() + ":" + serverAddress.getPort();
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

	public final ConnectionPool getSessionByKey(final String key) {
		if (this.ketamaSessions == null || this.ketamaSessions.size() == 0) {
			return null;
		}
		long hash = this.hashAlg.hash(key);
		ConnectionPool rv = this.getSessionByHash(hash);
		return rv;
	}

	public final ConnectionPool getSessionByHash(final long hash) {
		TreeMap<Long, List<ConnectionPool>> sessionMap = this.ketamaSessions;
		if (sessionMap.size() == 0) {
			return null;
		}
		Long resultHash = hash;
		
		if (!sessionMap.containsKey(resultHash)) {
			resultHash = sessionMap.ceilingKey(resultHash);
			if (resultHash == null && sessionMap.size() > 0) {
				resultHash = sessionMap.firstKey();
			}
		}
		List<ConnectionPool> sessionList = sessionMap.get(resultHash);
		if (sessionList == null || sessionList.size() == 0) {
			return null;
		}
		int size = sessionList.size();
		return sessionList.get(this.random.nextInt(size));
	}

	public final long nextHash(long hashVal, String key, int tries) {
		long tmpKey = this.hashAlg.hash(tries + key);
		hashVal += (int) (tmpKey ^ tmpKey >>> 32);
		hashVal &= 0xffffffffL;
		return hashVal;
	}

	public final void updateSessions(final Collection<ConnectionPool> list) {
		this.buildMap(list, this.hashAlg);
	}
}
