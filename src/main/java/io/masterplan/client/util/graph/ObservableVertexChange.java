package io.masterplan.client.util.graph;

import io.masterplan.client.util.collections.IReadOnlyList;
import io.masterplan.client.util.collections.ReadOnlyList;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ObservableVertexChange<T> {
    protected boolean sorted = false;
    protected Comparator<T> sortingComparator = null;
    protected List<ObservableVertex<T>> addedEdges = null;
    protected List<ObservableVertex<T>> removedEdges = null;

    public ObservableVertexChange() { }

    public boolean getSorted() { return sorted; }

    public Comparator<T> getSortingComparator() { return sortingComparator; }

    public IReadOnlyList<ObservableVertex<T>> getAddedEdges() {
        if(addedEdges == null)
            addedEdges = new ArrayList<>();
        return new ReadOnlyList<>(addedEdges);
    }

    public int addedEdgesSize() { return addedEdges == null ? 0 : addedEdges.size(); }

    public IReadOnlyList<ObservableVertex<T>> getRemovedEdges() {
        if(removedEdges == null)
            removedEdges = new ArrayList<>();
        return new ReadOnlyList<>(removedEdges);
    }

    public int removedEdgesSize() { return removedEdges == null ? 0 : removedEdges.size(); }

}
