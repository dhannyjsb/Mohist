package co.aikar.util;

import com.google.common.collect.ForwardingMap;

import java.util.HashMap;
import java.util.Map;

public class Counter<T> extends ForwardingMap<T, Long>
{
    private final Map<T, Long> counts;

    public Counter() {
        this.counts = new HashMap<T, Long>();
    }

    public long decrement(final T key) {
        return this.increment(key, -1L);
    }

    public long increment(final T key) {
        return this.increment(key, 1L);
    }

    public long decrement(final T key, final long amount) {
        return this.decrement(key, -amount);
    }

    public long increment(final T key, final long amount) {
        Long count = this.getCount(key);
        count += amount;
        this.counts.put(key, count);
        return count;
    }

    public long getCount(final T key) {
        return this.counts.getOrDefault(key, 0L);
    }

    @Override
    protected Map<T, Long> delegate() {
        return this.counts;
    }
}
