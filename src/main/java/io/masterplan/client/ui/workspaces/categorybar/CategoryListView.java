package io.masterplan.client.ui.workspaces.categorybar;


import io.masterplan.client.components.Category;
import io.masterplan.client.components.TodoElement;
import io.masterplan.client.observable.IReadOnlyObservable;
import io.masterplan.client.observable.Observable;
import io.masterplan.client.observable.ObservableManager;
import io.masterplan.client.ui.util.Viewable;
import io.masterplan.client.ui.workspaces.categorybar.category.CategoryBarView;
import io.masterplan.client.util.graph.ObservableGraphChange;
import io.masterplan.client.util.graph.ObservableVertex;
import io.masterplan.client.util.graph.ObservableVertexChange;
import javafx.scene.Node;
import javafx.scene.layout.VBox;


import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class CategoryListView extends VBox implements Viewable {

    private final Observable<ObservableVertex<TodoElement>> _rootVertex = new Observable<>();
    public final IReadOnlyObservable<ObservableVertex<TodoElement>> rootVertex = _rootVertex;

    public final Map<ObservableVertex<TodoElement>, Viewable> vertexToViewable = new HashMap<>();

    private final ObservableManager observableManager = new ObservableManager();


    public CategoryListView() {
        observableManager.addListener(_rootVertex, this::onRootVertexChange);

        // Binds managed and visible property to hide and reset the layout by just setting
        this.managedProperty().bindBidirectional(this.visibleProperty());
    }

    /** Getters and Setters for the visibility of the todoContainer **/
    public void hide() { this.setVisible(false); };
    public void show() { this.setVisible(true); };
    public boolean isShown() { return this.isVisible(); }
    public boolean isHidden() { return !isShown(); }
    public void toggleVisible() {
        if (isShown()) hide();
        else show();
    }

    public boolean isEmpty() {
        return this.getChildren().isEmpty();
    }

    public void setRootVertex(ObservableVertex<TodoElement> rootVertex) {
        _rootVertex.setValue(rootVertex);
    }

    public ObservableVertex<TodoElement> getRootVertex() {
        return _rootVertex.getValue();
    }

    private void onRootVertexChange(ObservableVertex<TodoElement> rootVertex) {

        vertexToViewable.clear();
        this.getChildren().clear();

        if(rootVertex == null)
            return;
        // memory leak - doesn't call stopListen on previous rootVertex

        observableManager.addListener(_rootVertex.getValue(), this::onRootVertexOutEdgesChange);
        observableManager.addListener(_rootVertex.getValue().getGraph(), this::onRootVertexRemoval);
    }

    private void onRootVertexOutEdgesChange(ObservableVertexChange<TodoElement> change) {
        for(var vertex : change.getAddedEdges()) {
            if (vertex.getElement() instanceof Category)
                addVertex(vertex);
        }
        for(var vertex : change.getRemovedEdges()) {
            if (vertex.getElement() instanceof Category)
                removeView(vertex);
        }
        if(change.getSorted()) {
            sort(change.getSortingComparator());
        }
    }

    private void onRootVertexRemoval(ObservableGraphChange<TodoElement> change) {
        if(change.getRemovedVertices().contains(_rootVertex.getValue()))
            return;

        for(var vertex : change.getRemovedVertices())
            if(vertexToViewable.containsKey(vertex))
                removeView(vertex);
    }

    public void addVertex(ObservableVertex<TodoElement> vertex) {
        Viewable viewable;
        if(vertex.getElement() instanceof Category) {
            CategoryBarView cView = new CategoryBarView();
            cView.setCategory(vertex);
            cView.registerListeners();
            viewable = cView;
        } else
            throw new IllegalArgumentException("addVertex(vertex) - vertex.getElement() must be of type Category");

        this.getChildren().add(viewable.node());
        vertexToViewable.put(vertex, viewable);
    }

    private void removeView(ObservableVertex<TodoElement> vertex) {
        Viewable viewable = vertexToViewable.get(vertex);
        viewable.unregisterListeners();
        this.getChildren().remove(viewable.node());
        vertexToViewable.remove(vertex);
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