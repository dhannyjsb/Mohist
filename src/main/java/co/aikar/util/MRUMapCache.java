package co.aikar.util;

import java.util.AbstractMap;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class MRUMapCache<K, V> extends AbstractMap<K, V>
{
    final Map<K, V> backingMap;
    Object cacheKey;
    V cacheValue;

    public MRUMapCache(final Map<K, V> backingMap) {
        this.backingMap = backingMap;
    }

    @Override
    public int size() {
        return this.backingMap.size();
    }

    @Override
    public boolean isEmpty() {
        return this.backingMap.isEmpty();
    }

    @Override
    public boolean containsKey(final Object key) {
        return (key != null && key.equals(this.cacheKey)) || this.backingMap.containsKey(key);
    }

    @Override
    public boolean containsValue(final Object value) {
        return (value != null && value == this.cacheValue) || this.backingMap.containsValue(value);
    }

    @Override
    public V get(final Object key) {
        if (this.cacheKey != null && this.cacheKey.equals(key)) {
            return this.cacheValue;
        }
        this.cacheKey = key;
        return this.cacheValue = this.backingMap.get(key);
    }

    @Override
    public V put(final K key, final V value) {
        this.cacheKey = key;
        return this.cacheValue = this.backingMap.put(key, value);
    }

    @Override
    public V remove(final Object key) {
        if (key != null && key.equals(this.cacheKey)) {
            this.cacheKey = null;
        }
        return this.backingMap.remove(key);
    }

    @Override
    public void putAll(final Map<? extends K, ? extends V> m) {
        this.backingMap.putAll(m);
    }

    @Override
    public void clear() {
        this.cacheKey = null;
        this.cacheValue = null;
        this.backingMap.clear();
    }

    @Override
    public Set<K> keySet() {
        return this.backingMap.keySet();
    }

    @Override
    public Collection<V> values() {
        return this.backingMap.values();
    }

    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        return this.backingMap.entrySet();
    }

    public static <K, V> Map<K, V> of(final Map<K, V> map) {
        return new MRUMapCache<K, V>(map);
    }
}
