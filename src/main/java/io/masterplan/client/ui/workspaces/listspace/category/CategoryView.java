package io.masterplan.client.ui.workspaces.listspace.category;


import io.masterplan.client.components.Category;
import io.masterplan.client.components.TodoElement;
import io.masterplan.client.components.task.Task;
import io.masterplan.client.models.MainModel;
import io.masterplan.client.observable.IReadOnlyObservable;
import io.masterplan.client.observable.Observable;
import io.masterplan.client.observable.ObservableManager;
import io.masterplan.client.ui.tag.TagDisplayView;
import io.masterplan.client.ui.util.Viewable;
import io.masterplan.client.util.graph.IQuery;
import io.masterplan.client.util.graph.ObservableVertex;
import io.masterplan.client.util.graph.ObservableVertexChange;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import io.masterplan.client.ui.workspaces.listspace.ListView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class CategoryView extends GridPane implements Viewable {

    @FXML
    private ListView listView;

    @FXML
    private Button toggleBtn;

    @FXML
    private HBox buttonContainer;
    @FXML
    private HBox toggleContainer;
    @FXML
    private HBox titleContainer;
    @FXML
    private HBox fillerContainer;

    @FXML
    private HBox remainingContainer;

    @FXML
    private Label categoryName;

    @FXML
    private TagDisplayView tagDisplayView;


    private final Observable<ObservableVertex<TodoElement>> _categoryVertex = new Observable<>();
    public final IReadOnlyObservable<ObservableVertex<TodoElement>> categoryVertex = _categoryVertex;

    private final Observable<IQuery<TodoElement>> _query = new Observable<>();
    public final IReadOnlyObservable<IQuery<TodoElement>> query = _query;


    private final ObservableManager observableManager = new ObservableManager();


    public CategoryView() {
        loadFXML();
    }


    private void loadFXML() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("CategoryView.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    @FXML
    private void initialize() {

        observableManager.addListener(_categoryVertex, this::onCategoryVertexChange);
        observableManager.addListener(_query, this::onQueryChange);


        this.managedProperty().bindBidirectional(this.visibleProperty());

        setOnMouseClicked((e) -> {
            e.consume();
            MainModel.model.editVertex.setValue(_categoryVertex.getValue());
        });

        if (listView.isTodoEmpty())
            toggleBtn.setVisible(false);

        // Set styling for hover
        List<Node> gridChildren = new ArrayList<>(getChildren());
        gridChildren.remove(listView);

        gridChildren.forEach(e -> {
            e.setOnMouseEntered(event -> {
                buttonContainer.setStyle("-fx-border-color: cadetblue;");
                fillerContainer.setStyle("-fx-border-color: cadetblue;");
            });
            e.setOnMouseExited(event -> {
                buttonContainer.setStyle("-fx-border-color: transparent;");
                fillerContainer.setStyle("-fx-border-color: transparent;");
            });
        });
    }

    private void onQueryChange(IQuery<TodoElement> query) {
        listView.setQuery(query);
    }

    /**
     * handler for Toggling todos and rotating btn
     * @param e mouse event
     */
    public void toggleBtnHandler(ActionEvent e) {
        listView.toggleTodo();

        if(listView.isTodoShown())
            toggleBtn.setRotate(0);
        else
            toggleBtn.setRotate(270);
    }

    private void onCategoryVertexChange(ObservableVertex<TodoElement> categoryVertex) {
        if(categoryVertex == null) {
            categoryName.setText("No Category");
            return;
        }

        Category cat = (Category) categoryVertex.getElement();

        listView.setRootVertex(categoryVertex);

        observableManager.addListener(categoryVertex, this::onAdjacenceyListChange);
        observableManager.addListener(cat.name, this::onCategoryNameChange);
        observableManager.addListener(cat.backgroundColor, this::onCategoryColorChange);
        observableManager.addListener(cat.isArchived, this::onArchiveChange);
        tagDisplayView.setVertex(categoryVertex);

    }

    private void onAdjacenceyListChange(ObservableVertexChange<TodoElement> change) {

        List<ObservableVertex<TodoElement>> numTaskQueryRes = _categoryVertex.getValue().getGraph().query(
                (e) -> e instanceof Task,
                _categoryVertex.getValue()
        );

        int totalElements = _categoryVertex.getValue().getGraph().getOutDegree(_categoryVertex.getValue());

        toggleBtn.setVisible(totalElements > 0);
    }

    @FXML
    private void onRemoveVertexBtn_click(ActionEvent e) {
        if(_categoryVertex.getValue() == null)
            return;

        _categoryVertex.getValue().getGraph().removeVertex(_categoryVertex.getValue());

        System.out.println("Removing vertex. Graph size:" + _categoryVertex.getValue().getGraph().getVertices().size());
    }

    @FXML
    private void onRemoveGraphBtn_click(ActionEvent e) {
        if(_categoryVertex.getValue() == null)
            return;

        _categoryVertex.getValue().getGraph().removeVertexReachable(_categoryVertex.getValue());

        System.out.println("Removing vertex. Graph size: " + _categoryVertex.getValue().getGraph().getVertices().size());
    }

    @FXML
    private void onArchive_click(ActionEvent e)  {
        if(_categoryVertex.getValue() == null)
            return;

        _categoryVertex.getValue().getElement().setArchive(!_categoryVertex.getValue().getElement().isArchived());
    }

    private void onArchiveChange(boolean isArchived) {
        this.setVisible(!isArchived);
    }




    private void onCategoryNameChange(String name) {
        categoryName.setText(name);
    }

    private void onCategoryColorChange(Color color) {
        if (color == null)
            return;
        toggleContainer.setBackground(new Background(new BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY)));
        buttonContainer.setBackground(new Background(new BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY)));
        titleContainer.setBackground(new Background(new BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY)));
    }

    public void setCategory(ObservableVertex<TodoElement> categoryVertex) {
        if(!(categoryVertex.getElement() instanceof Category))
            throw new IllegalArgumentException("CategoryView() - rootVertex.getElement() is not of type Category");

        _categoryVertex.setValue(categoryVertex);
    }

    public ObservableVertex<TodoElement> getCategory() {
        return _categoryVertex.getValue();
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
        listView.registerListeners();
        tagDisplayView.registerListeners();
        observableManager.startListen();
    }

    @Override
    public void unregisterListeners() {
        listView.unregisterListeners();
        tagDisplayView.unregisterListeners();
        observableManager.stopListen();
    }
}
