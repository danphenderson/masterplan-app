package io.masterplan.infrastucture.util.graph;

import java.util.Comparator;
import java.util.List;

/**
 *
 * @param <T>
 */
public interface IGraphWriteOnly<T> {

    /**
     * Adds element to the graph and returns its corresponding vertex. No edges have been added to the returned vertex.
     * @param element the element to add to the graph
     * @return the vertex that was added to the graph and contains element
     */
    IVertex<T> addVertex(T element);

    /**
     * Adds element to the graph and returns a vertex corresponding to that element which has a incoming vertex inVertex.
     * @param element the element to add to the graph
     * @param inVertex the vertex to add a directed edge to the returned vertex
     * @return the vertex that was added to the graph and contains element
     */
    IVertex<T> addVertex(T element, IVertex<T> inVertex);

    /**
     * Removes v from the graph and all corresponding directed edges that connect to v
     * @param v the vertex to remove from the graph
     */
    void removeVertex(IVertex<T> v);

    /**
     *
     * @param v
     * @return
     */
    List<? extends IVertex<T>> removeVertexReachable(IVertex<T> v);

    /**
     * Adds a directed edge from v1 to v2
     * @param v1 the from vertex
     * @param v2 the to vertex
     */
    void addDirectedEdge(IVertex<T> v1, IVertex<T> v2);

    /**
     * Removes the directed edge, if any, from v1 to v2
     * @param v1 the from vertex
     * @param v2 the to vertex
     */
    void removeDirectedEdge(IVertex<T> v1, IVertex<T> v2);

    /**
     * Sorts the backing list of IGraphReadOnly.getVertices()
     * @param c the comparator to sort the elements by
     */
    void sort(Comparator<T> c);

    /**
     * Sorts the outgoing and incoming vertices of all outgoing vertices of vertex v
     * @param c the comparator to sort the elements by
     * @param v the vertex whose direct children's
     */
    void sort(Comparator<T> c, IVertex<T> v);

    /**
     * Sorts the outgoing and incoming vertices of all reachable vertices of v
     * @param c the comparator to sort the elements by
     * @param v the vertex whose reachable vertices will be sorted
     */
    void sortReachable(Comparator<T> c, IVertex<T> v);

}
