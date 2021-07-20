package io.masterplan.ui.workspaces.listspace;


import io.masterplan.infrastucture.components.Category;
import io.masterplan.infrastucture.components.TodoElement;
import io.masterplan.infrastucture.components.task.Task;
import io.masterplan.infrastucture.observable.IReadOnlyObservable;
import io.masterplan.infrastucture.observable.Observable;
import io.masterplan.infrastucture.observable.ObservableManager;
import io.masterplan.ui.util.Viewable;
import io.masterplan.ui.util.dialogue.category.CreateCategoryDialog;
import io.masterplan.ui.util.dialogue.task.CreateTaskDialog;
import io.masterplan.ui.workspaces.listspace.category.CategoryView;
import io.masterplan.ui.workspaces.listspace.task.TaskView;
import io.masterplan.infrastucture.util.graph.IQuery;
import io.masterplan.infrastucture.util.graph.ObservableGraphChange;
import io.masterplan.infrastucture.util.graph.ObservableVertex;
import io.masterplan.infrastucture.util.graph.ObservableVertexChange;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class ListView extends VBox implements Viewable {

    @FXML
    private VBox todoContainer;

    @FXML
    private Button addTaskBtn;

    @FXML
    public HBox addBtnContainer;

    @FXML
    private Button addCategoryBtn;

    private final Observable<ObservableVertex<TodoElement>> _rootVertex = new Observable<>();
    public final IReadOnlyObservable<ObservableVertex<TodoElement>> rootVertex = _rootVertex;

    private final Observable<IQuery<TodoElement>> _query = new Observable<>();
    public final IReadOnlyObservable<IQuery<TodoElement>> query = _query;

    public final Map<ObservableVertex<TodoElement>, Viewable> vertexToViewable = new HashMap<>();

    private final ObservableManager observableManager = new ObservableManager();


    public ListView() {
        loadFXML();
    }


    private void loadFXML() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ListView.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @FXML
    private void initialize() {
        observableManager.addListener(_rootVertex, this::onRootVertexChange);
        observableManager.addListener(_query, this::onQueryChange);

        addTaskBtn.setOnAction(this::addTaskBtn_click);
        addCategoryBtn.setOnAction(this::addCategoryBtn_click);

        // Binds managed and visible property to hide and reset the layout by just setting
        todoContainer.managedProperty().bindBidirectional(todoContainer.visibleProperty());
    }

    /** Getters and Setters for the visibility of the todoContainer **/
    public void hideTodo() { todoContainer.setVisible(false); };
    public void showTodo() { todoContainer.setVisible(true); };
    public boolean isTodoShown() { return todoContainer.isVisible(); }
    public boolean isTodoHidden() { return !isTodoShown(); }
    public void toggleTodo() {
        if (isTodoShown()) hideTodo();
        else showTodo();
    }

    public boolean isTodoEmpty() {
        return todoContainer.getChildren().isEmpty();
    }

    private void addTaskBtn_click(ActionEvent e) {
        if(_rootVertex.getValue() == null)
            return;

        Task task = CreateTaskDialog.showAndWait();

        if(task != null) {
            _rootVertex.getValue().getGraph().addVertex(task, _rootVertex.getValue());
        }
    }

    private void addCategoryBtn_click(ActionEvent e) {
        if(_rootVertex.getValue() == null)
            return;

        Category category = CreateCategoryDialog.showAndWait();

        if(category != null) {
            _rootVertex.getValue().getGraph().addVertex(category, _rootVertex.getValue());
        }
    }

    public void setRootVertex(ObservableVertex<TodoElement> rootVertex) {
        _rootVertex.setValue(rootVertex);
    }

    public ObservableVertex<TodoElement> getRootVertex() {
        return _rootVertex.getValue();
    }

    private void onQueryChange(IQuery<TodoElement> query) {
        if(_rootVertex.getValue() == null)
            return;

        for(var vertices : new ArrayList<>(vertexToViewable.keySet()))
            removeView(vertices);

        vertexToViewable.clear();

        if(query != null) {
            for(var vertex : _rootVertex.getValue().getGraph().query(_query.getValue(), _rootVertex.getValue())) {
                addVertex(vertex);
            }
        }

    }

    private void onRootVertexChange(ObservableVertex<TodoElement> rootVertex) {

        vertexToViewable.clear();
        todoContainer.getChildren().clear();

        if(rootVertex == null)
            return;
        // memory leak - doesn't call stopListen on previous rootVertex

        observableManager.addListener(_rootVertex.getValue(), this::onRootVertexOutEdgesChange);
        observableManager.addListener(_rootVertex.getValue().getGraph(), this::onRootVertexRemoval);
    }

    private void onRootVertexOutEdgesChange(ObservableVertexChange<TodoElement> change) {
        for(var vertex : change.getAddedEdges()) {
            if(_query.getValue() != null && _query.getValue().query(vertex.getElement())) {
                addVertex(vertex);
            }
        }
        for(var vertex : change.getRemovedEdges()) {
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

        if (vertexToViewable.containsKey(vertex))
                return;
        if(vertex.getElement() instanceof Category) {
            CategoryView cView = new CategoryView();
            cView.setQuery(_query.getValue());
            cView.setCategory(vertex);
            cView.registerListeners();
            viewable = cView;
        }
        else if(vertex.getElement() instanceof Task) {
            TaskView tView = new TaskView();
            tView.setQuery(_query.getValue());
            tView.setRootTask(vertex);
            tView.registerListeners();
            viewable = tView;
        } else
            throw new UnsupportedOperationException("not implemented");

        todoContainer.getChildren().add(viewable.node());
        vertexToViewable.put(vertex, viewable);
    }

    private void removeView(ObservableVertex<TodoElement> vertex) {
        Viewable viewable = vertexToViewable.get(vertex);
        if(viewable != null) {
            viewable.unregisterListeners();
            todoContainer.getChildren().remove(viewable.node());
            vertexToViewable.remove(vertex);
        }
    }


    private void sort(Comparator<TodoElement> c) {
        // TODO: Implement
        throw new UnsupportedOperationException("not yet implemented");
    }

    public void setQuery(IQuery<TodoElement> query) {
        this._query.setValue(query);
    }

    public IQuery<TodoElement> getQuery() {
        return this._query.getValue();
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











