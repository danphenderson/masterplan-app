package io.masterplan.infrastucture.observable;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ObservableMap<K, V> implements Map<K, V>, IObservable<ObservableMap<K, V>>, IReadOnlyObservableMap<K, V> {

    private final Map<K, V> map;
    private final HashSet<IListener<ObservableMap<K, V>>> listeners = new HashSet<>();

    public ObservableMap(Map<K, V> map) {
        this.map = map;
    }

    @Override
    public void startListen(IListener<ObservableMap<K, V>> listener) {
        listeners.add(listener);
        listener.onChange(this);
    }

    @Override
    public void stopListen(IListener<ObservableMap<K, V>> listener) {
        listeners.remove(listener);
    }

    private void updateListeners() {
        for(IListener<ObservableMap<K, V>> listener : listeners)
            listener.onChange(this);
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
    public V put(K key, V value) {
        V oldVal = map.put(key, value);
        updateListeners();
        return oldVal;
    }

    @Override
    public V remove(Object key) {
        V oldVal = map.remove(key);
        updateListeners();
        return oldVal;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        map.putAll(m);
        updateListeners();
    }

    @Override
    public void clear() {
        boolean changed = (map.size() != 0);

        map.clear();

        if( changed )
            updateListeners();
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
    public Set<Entry<K, V>> entrySet() {
        return map.entrySet();
    }


}
