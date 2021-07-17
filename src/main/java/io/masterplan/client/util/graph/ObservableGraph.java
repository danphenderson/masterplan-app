package io.masterplan.client.util.graph;



import io.masterplan.client.observable.IListener;
import io.masterplan.client.observable.IObservable;
import io.masterplan.client.util.collections.IReadOnlyCollection;
import io.masterplan.client.util.collections.IReadOnlyList;
import io.masterplan.client.util.collections.ReadOnlyCollection;
import io.masterplan.client.util.collections.ReadOnlyList;

import java.util.*;

public class ObservableGraph<T> implements IGraph<T>, IObservable<ObservableGraphChange<T>> {
    protected final IGraph<T> graph;
    private final Set<IListener<ObservableGraphChange<T>>> listeners = new HashSet<>();
    private final Map<IVertex<T>, ObservableVertex<T>> vertexToObservable = new HashMap<>();


    public ObservableGraph(IGraph<T> graph) {
        this.graph = graph;

        for(IVertex<T> v : graph.getVertices())
            vertexToObservable.put(v, new ObservableVertex<>(this, v));

    }

    public void startListen(IListener<ObservableGraphChange<T>> listener) {
        ObservableGraphChange<T> change = new ObservableGraphChange<>();

        change.addedVertices = new ArrayList<>();
        change.addedVertices.addAll(vertexToObservable.values());

        listeners.add(listener);
        listener.onChange(change);
    }

    public void stopListen(IListener<ObservableGraphChange<T>> listener) {
        listeners.remove(listener);
    }

    private void updateListeners(ObservableGraphChange<T> change) {

        ArrayList<IListener<ObservableGraphChange<T>>> listenerArr = new ArrayList<>(listeners);

        for(var listener : listenerArr)
            listener.onChange(change);
    }

    protected ObservableVertex<T> validateVertex(IVertex<T> v) {
        ObservableVertex<T> retV;

        if((retV = vertexToObservable.get(v)) != null)
            return retV;

        if(!(v instanceof ObservableVertex))
            throw new IllegalArgumentException("Given vertex is not or does not have a corresponding ObservableVertex");

        return (ObservableVertex<T>) v;
    }

    protected List<ObservableVertex<T>> convertIterableToObsVertexList(Iterable<? extends IVertex<T>> vertices) {
        ArrayList<ObservableVertex<T>> retList = new ArrayList<>();

        for(IVertex<T> vertex : vertices)
            retList.add(vertexToObservable.get(vertex));

        return retList;
    }

    protected Iterable<ObservableVertex<T>> convertIterableToObsVertex(Iterable<? extends IVertex<T>> vertices) {
        return () -> new Iterator<>() {
            private final Iterator<? extends IVertex<T>> iterator = vertices.iterator();

            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public ObservableVertex<T> next() {
                return vertexToObservable.get(iterator.next());
            }
        };
    }

    @Override
    public IReadOnlyCollection<ObservableVertex<T>> getVertices() {
        return new ReadOnlyCollection<>( convertIterableToObsVertexList(graph.getVertices()) );
    }

    @Override
    public IReadOnlyList<ObservableVertex<T>> getOutVertices(IVertex<T> v)  {
        ObservableVertex<T> obsV = validateVertex(v);
        return new ReadOnlyList<>( convertIterableToObsVertexList(graph.getOutVertices(obsV.vertex)) );
    }

    @Override
    public int getOutDegree(IVertex<T> v) {
        ObservableVertex<T> obsV = validateVertex(v);
        return graph.getOutDegree(obsV.vertex);
    }

    @Override
    public IReadOnlyList<ObservableVertex<T>> getInVertices(IVertex<T> v)  {
        ObservableVertex<T> obsV = validateVertex(v);
        return new ReadOnlyList<>( convertIterableToObsVertexList(graph.getInVertices(obsV.vertex)) );
    }

    @Override
    public int getInDegree(IVertex<T> v) {
        ObservableVertex<T> obsV = validateVertex(v);
        return graph.getInDegree(obsV.vertex);
    }

    @Override
    public List<ObservableVertex<T>> query(IQuery<T> queryFunc) {
        return convertIterableToObsVertexList(graph.query(queryFunc));
    }

    @Override
    public List<ObservableVertex<T>> query(IQuery<T> queryFunc, IVertex<T> v) {
        ObservableVertex<T> obsV = validateVertex(v);
        return convertIterableToObsVertexList(graph.query(queryFunc, obsV.vertex));
    }

    @Override
    public List<ObservableVertex<T>> queryReachable(IQuery<T> queryFunc, IVertex<T> v) {
        ObservableVertex<T> obsV = validateVertex(v);
        return convertIterableToObsVertexList(graph.queryReachable(queryFunc, obsV.vertex));
    }

    @Override
    public int size() {
        return graph.size();
    }

    @Override
    public ObservableVertex<T> addVertex(T element) {

        IVertex<T> vertex = graph.addVertex(element);

        ObservableVertex<T> obsVertex = new ObservableVertex<>(this, vertex);
        vertexToObservable.put(vertex, obsVertex);

        ObservableGraphChange<T> change = new ObservableGraphChange<>();
        change.addedVertices = new ArrayList<>();
        change.addedVertices.add(obsVertex);

        updateListeners(change);

        return obsVertex;
    }

    /* TODO: Second param is outVertex */
    @Override
    public ObservableVertex<T> addVertex(T element, IVertex<T> inVertex) {
        ObservableVertex<T> obsInVertex = validateVertex(inVertex);

        IVertex<T> vertex = graph.addVertex(element, obsInVertex.vertex);
        ObservableVertex<T> obsVertex = new ObservableVertex<>(this, vertex);
        vertexToObservable.put(vertex, obsVertex);

        ObservableGraphChange<T> changeGraph = new ObservableGraphChange<>();
        changeGraph.addedVertices = new ArrayList<>();
        changeGraph.addedVertices.add(obsVertex);

        updateListeners(changeGraph);

        ObservableVertexChange<T> changeVertex = new ObservableVertexChange<>();
        changeVertex.addedEdges = new ArrayList<>();
        changeVertex.addedEdges.add(obsVertex);

        obsInVertex.updateListeners(changeVertex);

        return obsVertex;
    }

    @Override
    public void removeVertex(IVertex<T> v ) {
        ObservableVertex<T> obsV = validateVertex(v);

        graph.removeVertex(obsV.vertex);

        ObservableGraphChange<T> changeGraph = new ObservableGraphChange<>();
        changeGraph.removedVertices = new ArrayList<>();
        changeGraph.removedVertices.add(obsV);

        updateListeners(changeGraph);
    }

    @Override
    public List<ObservableVertex<T>> removeVertexReachable(IVertex<T> v) {

        ObservableVertex<T> obsV = validateVertex(v);

        ObservableVertexChange<T> changeVertex = new ObservableVertexChange<>();
        changeVertex.removedEdges = new ArrayList<>();
        changeVertex.removedEdges.add(obsV);

        List<? extends IVertex<T>> removedVertices = graph.removeVertexReachable(obsV.vertex);
        List<ObservableVertex<T>> removedObsVertices = convertIterableToObsVertexList(removedVertices);

        ObservableGraphChange<T> changeGraph = new ObservableGraphChange<>();

        changeGraph.removedVertices = new ArrayList<>(removedObsVertices);
        updateListeners(changeGraph);

        return removedObsVertices;
    }

    @Override
    public void addDirectedEdge(IVertex<T> v1, IVertex<T> v2) {

        ObservableVertex<T> obsV1 = validateVertex(v1);
        ObservableVertex<T> obsV2 = validateVertex(v2);

        graph.addDirectedEdge(obsV1.vertex, obsV2.vertex);

        ObservableVertexChange<T> change = new ObservableVertexChange<>();
        change.addedEdges = new ArrayList<>();
        change.addedEdges.add(obsV2);

        obsV1.updateListeners(change);
    }

    @Override
    public void removeDirectedEdge(IVertex<T> v1, IVertex<T> v2) {
        ObservableVertex<T> obsV1 = validateVertex(v1);
        ObservableVertex<T> obsV2 = validateVertex(v2);

        graph.removeDirectedEdge(obsV1.vertex, obsV2.vertex);

        ObservableVertexChange<T> change = new ObservableVertexChange<>();
        change.removedEdges = new ArrayList<>();
        change.removedEdges.add(obsV2);

        obsV1.updateListeners(change);
    }

    @Override
    public void sort(Comparator<T> c) {
        graph.sort(c);

        ObservableGraphChange<T> change = new ObservableGraphChange<>();
        change.sorted = true;

        updateListeners(change);
    }

    @Override
    public void sort(Comparator<T> c, IVertex<T> v) {
        ObservableVertex<T> obsV = validateVertex(v);

        graph.sort(c, obsV.vertex);

        ObservableVertexChange<T> change = new ObservableVertexChange<>();
        change.sorted = true;
        change.sortingComparator = c;

        obsV.updateListeners(change);
    }

    @Override
    public void sortReachable(Comparator<T> c, IVertex<T> v) {
        ObservableVertex<T> obsV = validateVertex(v);
        graph.sortReachable(c, obsV.vertex);

        ObservableVertexChange<T> change = new ObservableVertexChange<>();
        change.sorted = true;
        change.sortingComparator = c;

        obsV.updateListeners(change);
    }
}
