package io.masterplan.client.util.graph;

/**
 *
 * @param <T>
 */
public interface IQuery<T> {

    /**
     * @param t the element to determine if it is part of the query or not
     * @return true if the element is in the query, false otherwise.
     */
    boolean query(T t);

}
