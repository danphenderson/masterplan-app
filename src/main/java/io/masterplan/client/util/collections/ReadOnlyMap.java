package io.masterplan.client.util.collections;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class ReadOnlyMap<K, V> implements IReadOnlyMap<K, V> {

    private final Map<K, V> map;

    public ReadOnlyMap(Map<K, V> map) {
        this.map = map;
    }


    @Override
    public int size() {
        return map.size();
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return map.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return map.containsValue(value);
    }

    @Override
    public V get(Object key) {
        return map.get(key);
    }

    @Override
    public Set<K> keySet() {
        return map.keySet();
    }

    @Override
    public Collection<V> values() {
        return map.values();
    }

    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        return map.entrySet();
    }
}
