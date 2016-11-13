package com.publiccms.common.spi;

import java.util.Collection;
import java.util.List;

public interface CacheEntity<K, V> {
    
    public List<V> put(K key, V value);

    public V remove(K key);

    public V get(K key);

    public Collection<V> clear();
}
