package com.publiccms.component.cache;

import static java.util.Collections.synchronizedList;
import static java.util.Collections.synchronizedMap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.publiccms.common.spi.CacheEntity;

public class MemoryCacheEntity<K, V> implements CacheEntity<K, V>, java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private int size = 100;
    private List<K> cachedlist = synchronizedList(new ArrayList<K>());
    private Map<K, V> cachedMap = synchronizedMap(new HashMap<K, V>());

    public MemoryCacheEntity() {
    }

    public MemoryCacheEntity(int size) {
        this.size = size;
    }

    @Override
    public List<V> put(K key, V value) {
        cachedlist.add(key);
        cachedMap.put(key, value);
        return clearCache(size);
    }

    @Override
    public V get(K key) {
        return cachedMap.get(key);
    }

    @Override
    public Collection<V> clear() {
        Collection<V> values = cachedMap.values();
        cachedlist.clear();
        cachedMap.clear();
        return values;
    }

    @Override
    public V remove(K key) {
        int index = cachedlist.indexOf(key);
        if (0 < index) {
            return cachedMap.remove(cachedlist.remove(index));
        }
        return null;
    }

    private List<V> clearCache(int size) {
        List<V> list = null;
        if (size < cachedlist.size()) {
            list = new ArrayList<V>();
            for (int i = 0; i < size / 10; i++) {
                list.add(cachedMap.remove(cachedlist.remove(0)));
            }
        }
        return list;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
