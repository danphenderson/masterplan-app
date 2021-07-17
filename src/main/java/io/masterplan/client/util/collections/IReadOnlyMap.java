package io.masterplan.client.util.collections;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public interface IReadOnlyMap<K, V> {

    int size();

    boolean isEmpty();

    boolean containsKey(Object key);

    boolean containsValue(Object value);

    V get(Object key);

    Set<K> keySet();

    Collection<V> values();

    Set<Map.Entry<K, V>> entrySet();

}
