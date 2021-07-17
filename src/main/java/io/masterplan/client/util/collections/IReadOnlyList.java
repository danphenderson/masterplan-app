package io.masterplan.client.util.collections;

import java.util.ListIterator;

public interface IReadOnlyList<T> extends IReadOnlyCollection<T> {

    T get(int index);

    int indexOf(Object o);

    int lastIndexOf(Object o);

    ListIterator<T> listIterator();

    ListIterator<T> listIterator(int index);

}
