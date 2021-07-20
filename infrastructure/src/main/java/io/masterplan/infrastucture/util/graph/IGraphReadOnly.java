package io.masterplan.infrastucture.util.graph;

import io.masterplan.infrastucture.util.collections.IReadOnlyCollection;
import io.masterplan.infrastucture.util.collections.IReadOnlyList;

import java.util.List;

/**
 *
 * @param <T>
 */
public interface IGraphReadOnly<T> {

    /**
     * @return the graph's vertices
     */
    IReadOnlyCollection<? extends IVertex<T>> getVertices();

    /**
     * @param v vertex to retrieve the out vertices from
     * @return the out vertices of v
     */
    IReadOnlyList<? extends IVertex<T>> getOutVertices(IVertex<T> v);

    /**
     * @param v the vertex to determine the out degree of
     * @return the out degree of vertex v
     */
    int getOutDegree(IVertex<T> v);

    /**
     * @param v vertex to retrieve the in vertices from
     * @return the in vertices of v
     */
    IReadOnlyList<? extends IVertex<T>> getInVertices(IVertex<T> v);

    /**
     * @param v the vertex to determine the in degree of
     * @return the in degree of vertex v
     */
    int getInDegree(IVertex<T> v);

    /**
     * Iterates through IGraphReadOnly.getVertices() and uses queryFunc to determine if the vertex should be returned
     * @param queryFunc the function to determine if a given element is in the query or not
     * @return the corresponding subset of IGraphReadOnly.getVertices() where queryFunc.query() returns true
     */
    List<? extends IVertex<T>> query(IQuery<T> queryFunc);

    /**
     * Iterates through IGraphReadOnly.getOutVertices(v) and uses queryFunc to determine if the vertex should be returned
     * @param queryFunc the function to determine if a given element is in the query or not
     * @param v the vertex to iterate
     * @return the corresponding subset of IGraphReadOnly.getOutVertices(v) where queryFunc.query() returns true
     */
    List<? extends IVertex<T>> query(IQuery<T> queryFunc, IVertex<T> v);

    /**
     * Iterates through all the reachable vertices from v and uses queryFunc to determine if the vertex should be returned
     * @param queryFunc the function to determine if a given element is in the query or not
     * @param v the vertex to iterate
     * @return the corresponding subset of reachable vertices from v where queryFunc.query() returns true
     */
    List<? extends IVertex<T>> queryReachable(IQuery<T> queryFunc, IVertex<T> v);

    /**
     * @return the number of vertices in the graph
     */
    int size();

}
