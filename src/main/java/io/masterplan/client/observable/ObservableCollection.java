package io.masterplan.client.observable;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

public class ObservableCollection<T> implements IObservable<Collection<T>>, Collection<T> {

    private final HashSet<IListener<Collection<T>>> listeners = new HashSet<>();
    private final Collection<T> collection;

    public ObservableCollection(Collection<T> collection) {
        this.collection = collection;
    }

    @Override
    public void startListen(IListener<Collection<T>> listener) {
        listeners.add(listener);
        listener.onChange(this);
    }

    @Override
    public void stopListen(IListener<Collection<T>> listener) {
        listeners.remove(listener);
    }

    private void updateListeners() {
        for(IListener<Collection<T>> listener : listeners)
            listener.onChange(this);
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
        return collection.<T1>toArray(t1s);
    }

    @Override
    public boolean add(T t) {
        boolean changed = collection.add(t);
        if(changed) updateListeners();
        return changed;
    }

    @Override
    public boolean remove(Object o) {
        boolean changed = collection.remove(o);
        if(changed) updateListeners();
        return changed;
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        return this.collection.containsAll(collection);
    }

    @Override
    public boolean addAll(Collection<? extends T> collection) {
        boolean changed = this.collection.addAll(collection);
        if(changed) updateListeners();
        return changed;
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        boolean changed = this.collection.removeAll(collection);
        if(changed) updateListeners();
        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        boolean changed = this.collection.retainAll(collection);
        if(changed) updateListeners();
        return changed;
    }

    @Override
    public void clear() {
        this.collection.clear();
        updateListeners();
    }
}
