package io.masterplan.infrastucture.observable;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class ObservableSet<T> implements IObservable<ObservableSet<T>>, IReadOnlyObservableSet<T>, Set<T> {

    private final Set<T> set;
    private final HashSet<IListener<ObservableSet<T>>> listeners = new HashSet<>();

    public ObservableSet(Set<T> set) {
        this.set = set;
    }

    @Override
    public void startListen(IListener<ObservableSet<T>> listener) {
        listeners.add(listener);
        listener.onChange(this);
    }

    @Override
    public void stopListen(IListener<ObservableSet<T>> listener) {
        listeners.remove(listener);
    }

    private void updateListeners() {
        for(IListener<ObservableSet<T>> listener : listeners)
            listener.onChange(this);
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
    public <T1> T1[] toArray(T1[] a) {
        return set.toArray(a);
    }

    @Override
    public boolean add(T t) {
        boolean changed = set.add(t);
        if(changed) updateListeners();
        return changed;
    }

    @Override
    public boolean remove(Object o) {
        boolean changed = set.remove(o);
        if(changed) updateListeners();
        return changed;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return set.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        boolean changed = set.addAll(c);
        if(changed) updateListeners();
        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean changed = set.retainAll(c);
        if(changed) updateListeners();
        return changed;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean changed = set.removeAll(c);
        if(changed) updateListeners();
        return changed;
    }

    @Override
    public void clear() {
        boolean changed = (set.size() != 0);
        set.clear();
        if(changed) updateListeners();
    }
}
