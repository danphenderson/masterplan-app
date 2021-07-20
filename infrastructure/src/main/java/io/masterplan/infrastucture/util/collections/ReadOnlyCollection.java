package io.masterplan.infrastucture.util.collections;

import java.util.Collection;
import java.util.Iterator;

public class ReadOnlyCollection<T> implements IReadOnlyCollection<T>{

    private Collection<T> collection;

    public ReadOnlyCollection(Collection<T> collection) {
        this.collection = collection;
    }

    @Override
    public int size() {
        return collection.size();
    }

    @Override
    public boolean isEmpty() {
        return collection.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return collection.contains(o);
    }

    @Override
    public Iterator<T> iterator() {
        return collection.iterator();
    }

    @Override
    public Object[] toArray() {
        return collection.toArray();
    }

    @Override
    public <T1> T1[] toArray(T1[] t1s) {
        return collection.toArray(t1s);
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        return collection.containsAll(collection);
    }
}
