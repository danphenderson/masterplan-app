package io.masterplan.infrastucture.util.graph;

import io.masterplan.infrastucture.util.collections.IReadOnlyCollection;
import io.masterplan.infrastucture.util.collections.IReadOnlyList;
import io.masterplan.infrastucture.util.collections.ReadOnlyCollection;
import io.masterplan.infrastucture.util.collections.ReadOnlyList;

import java.util.*;

public class Graph<T> implements IGraph<T> {

    public class Vertex implements IVertex<T> {

        private final T element;
        private final List<Vertex> inVertices = new ArrayList<>();
        private final List<Vertex> outVertices = new ArrayList<>();

        public Vertex (T element) { this.element = element; }

        @Override
        public T getElement() {
            return element;
        }

        @Override
        public IGraph<T> getGraph() {
            return Graph.this;
        }
    }


    private final Map<T, Vertex> elementToVertex = new HashMap<>();


    public Graph() { }


    private Vertex validateVertex(IVertex<T> iVertex) {
        if(!(iVertex instanceof Graph.Vertex))
            throw new IllegalArgumentException("Graph.validateVertex() - given vertex is not of type Graph.Vertex");

        return (Vertex) iVertex;
    }

    private void validateVertex(Vertex v) {
        if(elementToVertex.get(v.element) != v)
            throw new IllegalArgumentException("Graph.validateVertex() - given vertex does not exist in graph");
    }

    @Override
    public Vertex addVertex(T element) {
        if(element == null)
            throw new IllegalArgumentException("Graph.addVertex() - element can not be null.");

        if(elementToVertex.containsKey(element))
            throw new IllegalArgumentException("Graph.addVertex() - element already exists in graph; duplicate elements are not allowed.");

        Vertex vertex = new Vertex(element);

        elementToVertex.put(element, vertex);

        return vertex;
    }

    @Override
    public IVertex<T> addVertex(T element, IVertex<T> inVertex) {
        Vertex vertex = addVertex(element);
        addDirectedEdge(inVertex, vertex);
        return vertex;
    }

    @Override /* this removes a vertex  */
    public void removeVertex(IVertex<T> v) {
        Vertex vertex = validateVertex(v);
        removeVertex(vertex);
    }

    public void removeVertex(Vertex v) {
        for(var inVertex : v.inVertices)
            inVertex.outVertices.remove(v);

        v.inVertices.clear();

        elementToVertex.remove(v.element);
    }

    @Override
    public List<Vertex> removeVertexReachable(IVertex<T> v) {
        Vertex vertex = validateVertex(v);
        return removeVertexReachable(vertex);
    }

    public List<Vertex> removeVertexReachable(Vertex v) {
        List<Vertex> list = new ArrayList<>();
        removeVertexReachable(v, list);
        return list;
    }

    private void removeVertexReachable(Vertex v, List<Vertex> list) {
        // note: order of for loops maters

        for(var outVertex : new ArrayList<>(v.outVertices)) {
            if(outVertex.inVertices.size() == 1)
                removeVertexReachable(outVertex, list);
        }

        for(var inVertex : v.inVertices) {
            inVertex.outVertices.remove(v);
        }

        v.inVertices.clear();

        list.add(v);
        elementToVertex.remove(v.element);
    }

    // returns true if v2 is reachable from v1, false otherwise
    private boolean DFS(Vertex v1, Vertex v2) {
        return DFS(v1, v2, new HashSet<>());
    }

    // returns true if v2 is reachable from v1, false otherwise
    private boolean DFS(Vertex v1, Vertex v2, Set<Vertex> visited) {
        if(visited.contains(v2))
            return false;

        if(v1 == v2)
            return true;
        else
            visited.add(v2);

        for(Vertex v : v2.outVertices)
            if(DFS(v1, v))
                return true;

        return false;
    }

    private void checkForCircularity(Vertex v1, Vertex v2) {
        if( DFS(v1, v2) )
            throw new IllegalArgumentException("v1 is reachable from v2, circularity detected");
    }

    @Override
    public void addDirectedEdge(IVertex<T> v1, IVertex<T> v2) {
        Vertex vertex1 = validateVertex(v1);
        Vertex vertex2 = validateVertex(v2);

        addDirectedEdge(vertex1, vertex2);
    }

    public void addDirectedEdge(Vertex v1, Vertex v2)  {
        validateVertex(v1);
        validateVertex(v2);

        checkForCircularity(v1, v2);

        v1.outVertices.add(v2);
        v2.inVertices.add(v1);
    }

    @Override
    public void removeDirectedEdge(IVertex<T> v1, IVertex<T> v2) {
        Vertex vertex1 = validateVertex(v1);
        Vertex vertex2 = validateVertex(v2);

        removeDirectedEdge(vertex1, vertex2);
    }

    public void removeDirectedEdge(Vertex v1, Vertex v2) {
        validateVertex(v1);
        validateVertex(v2);

        v1.outVertices.remove(v2);
        v2.inVertices.remove(v1);
    }

    @Override
    public void sort(Comparator<T> c) {  // TODO: Implement
        throw new UnsupportedOperationException("Graph.sort() - not yet implmented");
    }
    @Override
    public void sort(Comparator<T> c, IVertex<T> v)  {
        Vertex vertex = validateVertex(v);
        sort(c, vertex);
    }

    public void sort(Comparator<T> c, Vertex v) {
        validateVertex(v);
        Comparator<Vertex> vComparator = (v1, v2) -> c.compare(v1.element, v2.element);
        v.outVertices.sort(vComparator);
        v.inVertices.sort(vComparator);
    }

    @Override
    public void sortReachable(Comparator<T> c, IVertex<T> v) {
        Vertex vertex = validateVertex(v);
        sortReachable(c, vertex);
    }

    public void sortReachable(Comparator<T> c, Vertex v) {
        validateVertex(v);
        Comparator<Vertex> vComparator = (v1, v2) -> c.compare(v1.element, v2.element);
        sortReachable(vComparator, v, new HashSet<>());
    }

    private void sortReachable(Comparator<Vertex> vComparator, Vertex v, Set<Vertex> sorted) {
        if(sorted.contains(v))
            return;
        else
            sorted.add(v);

        v.outVertices.sort(vComparator);
        v.inVertices.sort(vComparator);

        for(Vertex vOut : v.outVertices)
            sortReachable(vComparator, vOut, sorted);
    }

    @Override
    public List<Vertex> query(IQuery<T> queryFunc) {
        ArrayList<Vertex> queryRes = new ArrayList<>();

        for(Vertex v : this.getVertices())
            if(queryFunc.query(v.element))
                queryRes.add(v);

        return queryRes;
    }

    @Override
    public List<Vertex> query(IQuery<T> queryFunc, IVertex<T> v) {
        Vertex vertex = validateVertex(v);
        return query(queryFunc, vertex);
    }

    public List<Vertex> query(IQuery<T> queryFunc, Vertex v) {
        validateVertex(v);

        ArrayList<Vertex> queryRes = new ArrayList<>();

        for(Vertex vOut : v.outVertices)
            if(queryFunc.query(vOut.element))
                queryRes.add(vOut);

        return queryRes;
    }

    @Override
    public List<Vertex> queryReachable(IQuery<T> queryFunc, IVertex<T> v) {
        Vertex vertex = validateVertex(v);
        return queryReachable(queryFunc, vertex);
    }

    public List<Vertex> queryReachable(IQuery<T> queryFunc, Vertex v) {
        validateVertex(v);

        List<Vertex> queryRes = new ArrayList<>();
        Set<Vertex> queried = new HashSet<>();

        queryReachable(queryRes, queryFunc, v, queried);

        return queryRes;
    }

    private void queryReachable(List<Vertex> queryRes, IQuery<T> queryFunc, Vertex v, Set<Vertex> queried) {
        if(queried.contains(v))
            return;
        else
            queried.add(v);

        if(queryFunc.query(v.element))
            queryRes.add(v);

        for(Vertex vOut : v.outVertices)
            queryReachable(queryRes, queryFunc, vOut, queried);
    }

    @Override
    public IReadOnlyList<Vertex> getOutVertices(IVertex<T> v) {
        Vertex vertex = validateVertex(v);
        return getOutVertices(vertex);
    }

    public IReadOnlyList<Vertex> getOutVertices(Vertex v) {
        validateVertex(v);
        return new ReadOnlyList<>(v.outVertices);
    }

    @Override
    public int getOutDegree(IVertex<T> v) {
        Vertex vertex = validateVertex(v);
        return getOutDegree(vertex);
    }

    public int getOutDegree(Vertex v)  {
        validateVertex(v);
        return v.outVertices.size();
    }

    @Override
    public IReadOnlyList<Vertex> getInVertices(IVertex<T> v) {
        Vertex vertex = validateVertex(v);
        return getInVertices(vertex);
    }

    public IReadOnlyList<Vertex> getInVertices(Vertex v){
        validateVertex(v);
        return new ReadOnlyList<>(v.inVertices);
    }

    @Override
    public int getInDegree(IVertex<T> v) {
        Vertex vertex = validateVertex(v);
        return getInDegree(vertex);
    }

    public int getInDegree(Vertex v) {
        validateVertex(v);
        return v.inVertices.size();
    }

    @Override
    public IReadOnlyCollection<Vertex> getVertices() {
        return new ReadOnlyCollection<>(elementToVertex.values());
    }

    public int size() {
        return elementToVertex.size();
    }

}
