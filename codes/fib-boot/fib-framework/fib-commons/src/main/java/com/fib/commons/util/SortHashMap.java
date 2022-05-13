package com.fib.commons.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fib.commons.exception.CommonException;

import cn.hutool.core.lang.Assert;

public class SortHashMap<K, V> extends HashMap<K, V> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private transient List<K> sortList;

	public SortHashMap(int initialCapacity) {
		super(initialCapacity);
		this.sortList = new ArrayList<>(initialCapacity);
	}

	public SortHashMap(int initialCapacity, float loadFactor) {
		super(initialCapacity, loadFactor);
		this.sortList = new ArrayList<>(initialCapacity);
	}

	public SortHashMap(Map<? extends K, ? extends V> m) {
		super(m);
		this.sortList = new ArrayList<>(m.size());
		this.sortList.addAll(m.keySet());
	}

	@Override
	public V put(K key, V value) {
		if (!super.containsKey(key)) {
			this.sortList.add(key);
		}
		return super.put(key, value);
	}

	public V put(int index, K key, V value) {
		Assert.isTrue(index >= 0, () -> new CommonException("unlawfully index: " + index));
		Assert.isTrue(super.size() >= index, () -> new CommonException("unlawfully index: " + index));
		if (super.containsKey(key)) {
			this.sortList.remove(key);
			this.sortList.add(index, key);
		} else {
			this.sortList.add(index, key);
		}
		return super.put(key, value);
	}

	public V get(int index) {
		return super.get(this.sortList.get(index));
	}

	public K getKey(int index) {
		return this.sortList.get(index);
	}

	@Override
	public V remove(Object key) {
		if (super.containsKey(key)) {
			this.sortList.remove(key);
		}
		return super.remove(key);
	}

	@Override
	public void clear() {
		super.clear();
		this.sortList.clear();
	}

	@Override
	public boolean equals(Object o) {
		return super.equals(o);
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}
}