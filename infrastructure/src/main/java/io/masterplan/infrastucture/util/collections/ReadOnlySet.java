package io.masterplan.infrastucture.util.collections;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class ReadOnlySet<T> implements IReadOnlySet<T> {

    private final Set<T> set;

    public ReadOnlySet(Set<T> set) {
        this.set = set;
    }

    @Override
    public int size() {
        return set.size();
    }

    @Override
    public boolean isEmpty() {
        return set.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return set.contains(o);
    }

    @Override
    public Iterator<T> iterator() {
        return set.iterator();
    }

    @Override
    public Object[] toArray() {
        return set.toArray();
    }

    @Override
    public <T1> T1[] toArray(T1[] t1s) {
        return set.toArray(t1s);
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        return set.containsAll(collection);
    }
}
