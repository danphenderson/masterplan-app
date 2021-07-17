package io.masterplan.client.ui.workspaces.archivespace;



import io.masterplan.client.components.Category;
import io.masterplan.client.components.TodoElement;
import io.masterplan.client.components.task.Task;
import io.masterplan.client.models.MainModel;
import io.masterplan.client.observable.ObservableManager;
import io.masterplan.client.ui.util.Viewable;
import io.masterplan.client.ui.workspaces.archivespace.category.ArchiveCategoryView;
import io.masterplan.client.ui.workspaces.archivespace.task.ArchiveTaskView;
import io.masterplan.client.util.graph.ObservableGraphChange;
import io.masterplan.client.util.graph.ObservableVertex;
import javafx.scene.Node;
import javafx.scene.layout.VBox;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class ArchiveListView extends VBox implements Viewable {

    private final ObservableManager observableManager = new ObservableManager();

    public final Map<ObservableVertex<TodoElement>, Viewable> vertexToViewable = new HashMap<>();

    private final Map<ObservableVertex<TodoElement>, ArchiveVertexCallback> vertexToCallBack = new HashMap<>();

    public ArchiveListView() {
        observableManager.addListener(MainModel.model.obsGraph, this::onGraphChange);
    }

    private void onGraphChange(ObservableGraphChange<TodoElement> change) {

        for(var v : change.getAddedVertices()) {
            var callBack = new ArchiveVertexCallback(v, this::onArchiveCallBack);
            callBack.registerListeners();
            vertexToCallBack.put(v, callBack);
        }

        for(var v : change.getRemovedVertices()) {
            var callBack = vertexToCallBack.remove(v);
            callBack.unregisterListeners();
            removeVertex(v);
        }

        if(change.getSorted()) {
            sort(change.getSortingComparator());
        }

    }

    private void onArchiveCallBack(ArchiveVertexCallback.ChangeInfo change) {
        if(change.isArchived)
            addVertex(change.getVertex());
        else
            removeVertex(change.getVertex());
    }

    private void addVertex(ObservableVertex<TodoElement> vertex) {
        Viewable viewable;

        if (vertex.getElement() instanceof Task) {
            viewable = new ArchiveTaskView(vertex);
        }
        else if (vertex.getElement() instanceof Category) {
            viewable = new ArchiveCategoryView(vertex);
        }
        else
            throw new UnsupportedOperationException("change.getVertex().getElement() is not of type Task or Category");

        viewable.registerListeners();
        vertexToViewable.put(vertex, viewable);
        this.getChildren().add(viewable.node());
    }

    private void removeVertex(ObservableVertex<TodoElement> vertex) {
        Viewable viewable = vertexToViewable.get(vertex);

        if(viewable != null) {
            viewable.unregisterListeners();
            vertexToViewable.remove(vertex);
            this.getChildren().remove(viewable.node());
        }
    }

    private void sort(Comparator<TodoElement> c) {
        // TODO: Implement
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    public Node node() {
        return this;
    }

    @Override
    public void registerListeners() {
        observableManager.startListen();
    }

    @Override
    public void unregisterListeners() {
        observableManager.stopListen();
        for(var viewable : vertexToViewable.values())
            viewable.unregisterListeners();
        vertexToViewable.clear();
    }
}
