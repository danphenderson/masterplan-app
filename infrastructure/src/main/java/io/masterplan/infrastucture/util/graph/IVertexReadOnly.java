package io.masterplan.infrastucture.util.graph;

/**
 *
 * @param <T>
 */
public interface IVertexReadOnly<T> {

    /**
     * @return the element of the vertex
     */
    T getElement();

    /**
     * @return the graph the vertex is a part of
     */
    IGraph<T> getGraph();

}
