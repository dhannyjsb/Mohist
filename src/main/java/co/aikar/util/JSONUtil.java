package co.aikar.util;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class JSONUtil
{
    public static JSONPair pair(final String key, final Object obj) {
        return new JSONPair(key, obj);
    }

    public static JSONPair pair(final long key, final Object obj) {
        return new JSONPair(String.valueOf(key), obj);
    }

    public static Map createObject(final JSONPair... data) {
        return appendObjectData(new LinkedHashMap(), data);
    }

    public static Map appendObjectData(final Map parent, final JSONPair... data) {
        for (final JSONPair JSONPair : data) {
            parent.put(JSONPair.key, JSONPair.val);
        }
        return parent;
    }

    public static List toArray(final Object... data) {
        return Lists.newArrayList(data);
    }

    public static <E> List toArrayMapper(final E[] collection, final Function<E, Object> mapper) {
        return toArrayMapper(Lists.newArrayList(collection), mapper);
    }

    public static <E> List toArrayMapper(final Iterable<E> collection, final Function<E, Object> mapper) {
        final List array = Lists.newArrayList();
        for (final E e : collection) {
            final Object object = mapper.apply(e);
            if (object != null) {
                array.add(object);
            }
        }
        return array;
    }

    public static <E> Map toObjectMapper(final E[] collection, final Function<E, JSONPair> mapper) {
        return toObjectMapper(Lists.newArrayList(collection), mapper);
    }

    public static <E> Map toObjectMapper(final Iterable<E> collection, final Function<E, JSONPair> mapper) {
        final Map object = Maps.newLinkedHashMap();
        for (final E e : collection) {
            final JSONPair JSONPair = mapper.apply(e);
            if (JSONPair != null) {
                object.put(JSONPair.key, JSONPair.val);
            }
        }
        return object;
    }

    public static class JSONPair
    {
        final String key;
        final Object val;

        JSONPair(final String key, final Object val) {
            this.key = key;
            this.val = val;
        }
    }
}
