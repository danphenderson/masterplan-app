package io.masterplan.client.observable;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public interface IReadOnlyObservableList<T> extends IObservable<ObservableList<T>> {

    int size();

    boolean isEmpty();

    boolean contains(Object o);

    Iterator<T> iterator();

    Object[] toArray();

    <T1> T1[] toArray(T1[] a);

    boolean containsAll(Collection<?> c);

    T get(int index);

    int indexOf(Object o);

    int lastIndexOf(Object o);

    ListIterator<T> listIterator();

    ListIterator<T> listIterator(int index);

    List<T> subList(int fromIndex, int toIndex);

}
