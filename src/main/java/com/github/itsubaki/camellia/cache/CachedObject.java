package com.github.itsubaki.camellia.cache;

import java.util.concurrent.atomic.AtomicLong;

public class CachedObject<V> {
	private V object;
	private long lastUsed = System.currentTimeMillis();
	private AtomicLong hit = new AtomicLong(0);

	public CachedObject(V object) {
		this.object = object;
	}

	public V get() {
		hit();
		return object;
	}

	public long getLastUsed() {
		return lastUsed;
	}

	public void hit() {
		hit.incrementAndGet();
		lastUsed = System.currentTimeMillis();
	}

	public long getHitCount() {
		return hit.get();
	}

}
