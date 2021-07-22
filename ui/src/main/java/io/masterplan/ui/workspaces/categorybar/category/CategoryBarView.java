package io.masterplan.ui.workspaces.categorybar.category;


import io.masterplan.infrastucture.components.Category;
import io.masterplan.infrastucture.components.Colour;
import io.masterplan.infrastucture.components.TodoElement;
import io.masterplan.infrastucture.models.MainModel;
import io.masterplan.infrastucture.observable.IReadOnlyObservable;
import io.masterplan.infrastucture.observable.Observable;
import io.masterplan.infrastucture.observable.ObservableManager;
import io.masterplan.ui.util.ColorConverter;
import io.masterplan.ui.util.Viewable;
import io.masterplan.infrastucture.util.graph.ObservableVertex;
import io.masterplan.infrastucture.util.graph.ObservableVertexChange;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import io.masterplan.ui.workspaces.categorybar.CategoryListView;


import java.io.IOException;
import java.util.List;


public class CategoryBarView extends GridPane implements Viewable {

    @FXML
    private CategoryListView categoryListView;

    @FXML
    private Button toggleBtn;

    @FXML
    private HBox titleContainer;

    @FXML
    private Label categoryName;

    @FXML
    private HBox toggleContainer;


    private final Observable<ObservableVertex<TodoElement>> _categoryVertex = new Observable<>();
    public final IReadOnlyObservable<ObservableVertex<TodoElement>> categoryVertex = _categoryVertex;



    private final ObservableManager observableManager = new ObservableManager();



    public CategoryBarView() {
        loadFXML();
    }


    private void loadFXML() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("CategoryBarView.fxml"));
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
        this.managedProperty().bindBidirectional(this.visibleProperty());

        setOnMouseClicked((e) -> {
            e.consume();
            MainModel.model.selectedVertex.setValue(_categoryVertex.getValue());
        });


    }

    @FXML
    private void toggleBtnHandler(ActionEvent e) {
        categoryListView.toggleVisible();

        if(categoryListView.isShown())
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

        categoryListView.setRootVertex(categoryVertex);

        observableManager.addListener(categoryVertex, this::onAdjacenceyListChange);
        observableManager.addListener(cat.name, this::onCategoryNameChange);
        observableManager.addListener(cat.backgroundColor, this::onCategoryColorChange);
        observableManager.addListener(cat.isArchived, this::onArchiveChange);

    }

    private void onAdjacenceyListChange(ObservableVertexChange<TodoElement> change) {

        List<ObservableVertex<TodoElement>> numCategoryQueryRes = _categoryVertex.getValue().getGraph().query(
                (e) -> e instanceof Category,
                _categoryVertex.getValue()
        );

        toggleBtn.setVisible(numCategoryQueryRes.size() > 0);
    }


    private void onArchiveChange(boolean isArchived) {
        this.setVisible(!isArchived);
    }

    private void onCategoryNameChange(String name) {
        categoryName.setText(name);
    }

    private void onCategoryColorChange(Colour color) {
        if (color == null)
            return;
        Color col = ColorConverter.convertToFXColor(color);
        titleContainer.setBackground(new Background(new BackgroundFill(col, CornerRadii.EMPTY, Insets.EMPTY)));
        toggleContainer.setBackground(new Background(new BackgroundFill(col, CornerRadii.EMPTY, Insets.EMPTY)));
    }

    public void setCategory(ObservableVertex<TodoElement> categoryVertex) {
        if(!(categoryVertex.getElement() instanceof Category))
            throw new IllegalArgumentException("CategoryView() - rootVertex.getElement() is not of type Category");

        _categoryVertex.setValue(categoryVertex);
    }

    public ObservableVertex<TodoElement> getCategory() {
        return _categoryVertex.getValue();
    }

    @Override
    public Node node() {
        return this;
    }

    @Override
    public void registerListeners() {
        categoryListView.registerListeners();
        observableManager.startListen();
    }

    @Override
    public void unregisterListeners() {
        categoryListView.unregisterListeners();
        observableManager.stopListen();
    }
}
