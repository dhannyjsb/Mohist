package co.aikar.util;

import com.google.common.base.Function;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

public class LoadingIntMap<V> extends Int2ObjectOpenHashMap<V>
{
    private final com.google.common.base.Function<Integer, V> loader;

    public LoadingIntMap(final com.google.common.base.Function<Integer, V> loader) {
        this.loader = loader;
    }

    public LoadingIntMap(final int expectedSize, final com.google.common.base.Function<Integer, V> loader) {
        super(expectedSize);
        this.loader = loader;
    }

    public LoadingIntMap(final int expectedSize, final float loadFactor, final com.google.common.base.Function<Integer, V> loader) {
        super(expectedSize, loadFactor);
        this.loader = loader;
    }

    @Override
    public V get(final int key) {
        V res = super.get(key);
        if (res == null) {
            res = this.loader.apply(key);
            if (res != null) {
                this.put(key, res);
            }
        }
        return res;
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
