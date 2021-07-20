package io.masterplan.infrastucture.util.collections;

import java.util.Collection;
import java.util.Iterator;

public interface IReadOnlyCollection<T> extends Iterable<T> {

    int size();

    boolean isEmpty();

    boolean contains(Object o);

    Iterator<T> iterator();

    Object[] toArray();

    <T1> T1[] toArray(T1[] t1s);

    boolean containsAll(Collection<?> collection);

}
