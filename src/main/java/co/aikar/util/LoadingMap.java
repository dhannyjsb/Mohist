package co.aikar.util;

import com.google.common.base.Function;

import java.lang.reflect.Constructor;
import java.util.*;

public class LoadingMap<K, V> extends AbstractMap<K, V>
{
    private final Map<K, V> backingMap;
    private final java.util.function.Function<K, V> loader;

    public LoadingMap(final Map<K, V> backingMap, final java.util.function.Function<K, V> loader) {
        this.backingMap = backingMap;
        this.loader = loader;
    }

    public static <K, V> Map<K, V> of(final Map<K, V> backingMap, final Function<K, V> loader) {
        return new LoadingMap<>(backingMap, loader);
    }

    public static <K, V> Map<K, V> newAutoMap(final Map<K, V> backingMap, final Class<? extends K> keyClass, final Class<? extends V> valueClass) {
        return new LoadingMap<>(backingMap, new AutoInstantiatingLoader<>(keyClass, valueClass));
    }

    public static <K, V> Map<K, V> newAutoMap(final Map<K, V> backingMap, final Class<? extends V> valueClass) {
        return newAutoMap(backingMap, null, valueClass);
    }

    public static <K, V> Map<K, V> newHashAutoMap(final Class<? extends K> keyClass, final Class<? extends V> valueClass) {
        return newAutoMap(new HashMap<>(), keyClass, valueClass);
    }

    public static <K, V> Map<K, V> newHashAutoMap(final Class<? extends V> valueClass) {
        return newHashAutoMap(null, valueClass);
    }

    public static <K, V> Map<K, V> newHashAutoMap(final Class<? extends K> keyClass, final Class<? extends V> valueClass, final int initialCapacity, final float loadFactor) {
        return newAutoMap(new HashMap<>(initialCapacity, loadFactor), keyClass, valueClass);
    }

    public static <K, V> Map<K, V> newHashAutoMap(final Class<? extends V> valueClass, final int initialCapacity, final float loadFactor) {
        return newHashAutoMap(null, valueClass, initialCapacity, loadFactor);
    }

    public static <K, V> Map<K, V> newHashMap(final Function<K, V> loader) {
        return new LoadingMap<>(new HashMap<>(), loader);
    }

    public static <K, V> Map<K, V> newHashMap(final Function<K, V> loader, final int initialCapacity) {
        return new LoadingMap<>(new HashMap<K, V>(initialCapacity), loader);
    }

    public static <K, V> Map<K, V> newHashMap(final Function<K, V> loader, final int initialCapacity, final float loadFactor) {
        return new LoadingMap<>(new HashMap<>(initialCapacity, loadFactor), loader);
    }

    public static <K, V> Map<K, V> newIdentityHashMap(final Function<K, V> loader) {
        return new LoadingMap<>(new IdentityHashMap<>(), loader);
    }

    public static <K, V> Map<K, V> newIdentityHashMap(final Function<K, V> loader, final int initialCapacity) {
        return new LoadingMap<>(new IdentityHashMap<>(initialCapacity), loader);
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
        return this.backingMap.containsKey(key);
    }

    @Override
    public boolean containsValue(final Object value) {
        return this.backingMap.containsValue(value);
    }

    @Override
    public V get(final Object key) {
        return this.backingMap.computeIfAbsent((K)key, this.loader);
    }

    @Override
    public V put(final K key, final V value) {
        return this.backingMap.put(key, value);
    }

    @Override
    public V remove(final Object key) {
        return this.backingMap.remove(key);
    }

    @Override
    public void putAll(final Map<? extends K, ? extends V> m) {
        this.backingMap.putAll(m);
    }

    @Override
    public void clear() {
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
    public boolean equals(final Object o) {
        return this.backingMap.equals(o);
    }

    @Override
    public int hashCode() {
        return this.backingMap.hashCode();
    }

    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        return this.backingMap.entrySet();
    }

    public LoadingMap<K, V> clone() {
        return new LoadingMap<>(this.backingMap, this.loader);
    }

    private static class AutoInstantiatingLoader<K, V> implements Function<K, V>
    {
        final Constructor<? extends V> constructor;
        private final Class<? extends V> valueClass;

        AutoInstantiatingLoader(final Class<? extends K> keyClass, final Class<? extends V> valueClass) {
            try {
                this.valueClass = valueClass;
                if (keyClass != null) {
                    this.constructor = valueClass.getConstructor(keyClass);
                }
                else {
                    this.constructor = null;
                }
            }
            catch (NoSuchMethodException e) {
                throw new IllegalStateException(valueClass.getName() + " does not have a constructor for " + ((keyClass != null) ? keyClass.getName() : null));
            }
        }

        @Override
        public V apply(final K input) {
            try {
                return (this.constructor != null) ? this.constructor.newInstance(input) : this.valueClass.newInstance();
            }
            catch (Exception e) {
                throw new ExceptionInInitializerError(e);
            }
        }

        @Override
        public int hashCode() {
            return super.hashCode();
        }

        @Override
        public boolean equals(final Object object) {
            return false;
        }
    }

    public abstract static class Feeder<T> implements Function<T, T>
    {
        @Override
        public T apply(final Object input) {
            return this.apply();
        }

        public abstract T apply();
    }
}
