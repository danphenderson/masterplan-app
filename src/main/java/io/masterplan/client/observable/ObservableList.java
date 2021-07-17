package io.masterplan.client.observable;

import java.util.*;

public class ObservableList<T> implements IObservable<ObservableList<T>>, List<T>, IReadOnlyObservableList<T> {

    private final List<T> list;
    private final HashSet<IListener<ObservableList<T>>> listeners = new HashSet<>();


    public ObservableList(List<T> list) {
        this.list = list;
    }


    private void updateListeners() {
        for(IListener<ObservableList<T>> listener : new ArrayList<>(listeners))
            listener.onChange(this);
    }

    @Override
    public void startListen(IListener<ObservableList<T>> listener) {
        listeners.add(listener);
        listener.onChange(this);
    }

    @Override
    public void stopListen(IListener<ObservableList<T>> listener) {
        listeners.remove(listener);
    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return list.contains(o);
    }

    @Override
    public Iterator<T> iterator() {
        return list.iterator();
    }

    @Override
    public Object[] toArray() {
        return list.toArray();
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        return list.<T1>toArray(a);
    }

    @Override
    public boolean add(T t) {
        list.add(t);
        updateListeners();
        return true;
    }

    @Override
    public boolean remove(Object o) {
        boolean changed = list.remove(o);

        if( changed )
            updateListeners();

        return changed;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return list.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        boolean changed = list.addAll(c);

        if( changed )
            updateListeners();

        return changed;
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        boolean changed = list.addAll(index, c);

        if( changed )
            updateListeners();

        return changed;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean changed = list.removeAll(c);

        if( changed )
            updateListeners();

        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean changed = list.retainAll(c);

        if( changed )
            updateListeners();

        return changed;
    }

    @Override
    public void clear() {
        boolean changed = (list.size() != 0);

        list.clear();

        if( changed )
            updateListeners();
    }

    @Override
    public T get(int index) {
        return list.get(index);
    }

    @Override
    public T set(int index, T element) {
        T oldVal = list.set(index, element);
        updateListeners();
        return oldVal;
    }

    @Override
    public void add(int index, T element) {
        list.add(index, element);
        updateListeners();
    }

    @Override
    public T remove(int index) {
        T val = list.remove(index);
        updateListeners();
        return val;
    }

    @Override
    public void sort(Comparator<? super T> c) {
        list.sort(c);
        updateListeners();
    }

    @Override
    public int indexOf(Object o) {
        return list.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return list.lastIndexOf(o);
    }

    @Override
    public ListIterator<T> listIterator() {
        return list.listIterator();
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        return list.listIterator(index);
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        return list.subList(fromIndex, toIndex);
    }



}
