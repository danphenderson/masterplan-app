package io.masterplan.ui.workspaces.categorybar;


import io.masterplan.infrastucture.components.Category;
import io.masterplan.infrastucture.components.TodoElement;
import io.masterplan.infrastucture.models.MainModel;
import io.masterplan.infrastucture.observable.IListener;
import io.masterplan.infrastucture.observable.ObservableManager;
import io.masterplan.ui.util.Viewable;
import io.masterplan.ui.workspaces.categorybar.category.CategoryBarView;
import io.masterplan.infrastucture.util.graph.ObservableGraphChange;
import io.masterplan.infrastucture.util.graph.ObservableVertex;
import io.masterplan.infrastucture.util.graph.ObservableVertexChange;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;


import java.util.HashMap;
import java.util.Map;


public class CategoryListSpaceView extends VBox implements Viewable {

    private final ObservableManager observableManager = new ObservableManager();

    private final Map<ObservableVertex<TodoElement>, Viewable> vertexToViewable = new HashMap<>();
    private final Map<ObservableVertex<TodoElement>, IListener<ObservableVertexChange<TodoElement>>> vertexToListener = new HashMap<>();

    public CategoryListSpaceView() {
        observableManager.addListener(MainModel.model.obsGraph, this::onGraphChange);
        this.setMaxWidth(200);
        this.setBackground(new Background(new BackgroundFill(Color.web("#666666ff"), CornerRadii.EMPTY, Insets.EMPTY)));



    }

    private void onGraphChange(ObservableGraphChange<TodoElement> change) {

        for(var vertex : change.getAddedVertices()) {
            if(vertex.getElement() instanceof Category) {
                IListener<ObservableVertexChange<TodoElement>> listener = this::onVertexChange;
                vertexToListener.put(vertex, listener);
                observableManager.addListener(vertex, listener);

                if(vertex.getGraph().getInDegree(vertex) == 0)
                    addCategoryVertex(vertex);
            }
        }

        for(var vertex : change.getRemovedVertices()) {
            if (vertex.getElement() instanceof Category) {
                var listener = vertexToListener.remove(vertex);
                observableManager.removeListener(vertex, listener);
                removeCategoryVertex(vertex);
            }
        }

        if(change.getSorted()) {
            throw new UnsupportedOperationException("not yet supported");
        }

    }

    private void onVertexChange(ObservableVertexChange<TodoElement> change) {



        for(var vertex : change.getAddedEdges()) {

            if (vertex.getElement() instanceof Category) {
                if(vertex.getGraph().getInDegree(vertex) > 0)
                    removeCategoryVertex(vertex);
            }
        }

        for(var vertex : change.getRemovedEdges()) {
            if (vertex.getElement() instanceof Category) {
                if(vertex.getGraph().getInDegree(vertex) == 0) {
                    addCategoryVertex(vertex);
                }
            }
        }

    }

    private void addCategoryVertex(ObservableVertex<TodoElement> vertex) {
        System.out.println("Called add cat vertex");

        if(vertexToViewable.containsKey(vertex))
            return;

        System.out.println("Added Category Vertex: " + vertex.getElement().getName());

        var categoryBarView = new CategoryBarView();
        categoryBarView.setCategory(vertex);
        vertexToViewable.put(vertex, categoryBarView);
        categoryBarView.registerListeners();
        getChildren().add(categoryBarView.node());
    }

    private void removeCategoryVertex(ObservableVertex<TodoElement> vertex) {
        var viewable = vertexToViewable.remove(vertex);
        if(viewable != null) {
            System.out.println("Removed Category Vertex: " + vertex.getElement().getName());
            getChildren().remove(viewable.node());
            viewable.unregisterListeners();
        }
    }

    @Override
    public Node node() {
        return this;
    }

    public void registerListeners() {
        observableManager.startListen();

    }

    public void unregisterListeners() {
        observableManager.stopListen();
        for(var viewable : vertexToViewable.values()) {
            viewable.unregisterListeners();
        }

        vertexToViewable.clear();

        for(var vertex : vertexToListener.keySet()) {
            var listener = vertexToListener.get(vertex);
            observableManager.removeListener(vertex, listener);
        }

        vertexToListener.clear();

    }
}
