package io.masterplan.ui.workspaces.listspace;

import io.masterplan.infrastucture.components.Category;
import io.masterplan.infrastucture.components.TodoElement;
import io.masterplan.infrastucture.components.task.Task;
import io.masterplan.infrastucture.observable.IListener;
import io.masterplan.infrastucture.observable.IReadOnlyObservable;
import io.masterplan.infrastucture.observable.Observable;
import io.masterplan.infrastucture.observable.ObservableManager;
import io.masterplan.ui.tag.TagDisplayView;
import io.masterplan.ui.util.Viewable;
import io.masterplan.infrastucture.util.graph.IQuery;
import io.masterplan.infrastucture.util.graph.ObservableGraphChange;
import io.masterplan.infrastucture.util.graph.ObservableVertex;
import io.masterplan.infrastucture.util.graph.ObservableVertexChange;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;


import java.io.IOException;

public class ListSpaceViewHeader extends GridPane implements Viewable {

    @FXML
    private Label headerName;

    @FXML
    private Label categoryContents;
    private static final String categoryContentsPattern = "%d Categories | %d Tasks";

    @FXML
    private TagDisplayView tagDisplayView = new TagDisplayView();

    @FXML
    private TextField searchInput;

    private final Observable<ObservableVertex<TodoElement>> _rootCategory = new Observable<>();
    public final IReadOnlyObservable<ObservableVertex<TodoElement>> rootCategory = _rootCategory;

    private IListener<IQuery<TodoElement>> searchQueryCallBack = null;

    private final ObservableManager observableManager = new ObservableManager();

    public ListSpaceViewHeader() {
        loadFXML();
    }


    private void loadFXML() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ListSpaceViewHeader.fxml"));
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
        _rootCategory.startListen(this::onRootCategoryChange);
    }

    @FXML
    private void onSearchInput_action(Event ae) {
        if(searchQueryCallBack == null)
            return;

        IQuery<TodoElement> queryFunc = (e) -> {
            if(e.getName().toLowerCase().contains(searchInput.getText().toLowerCase()))
                return true;

            for(var tag : e.tags) {
                if(tag.getName().toLowerCase().contains(searchInput.getText().toLowerCase()))
                    return true;
            }

            return false;
        };

        searchQueryCallBack.onChange(queryFunc);
    }

    private void onRootCategoryChange(ObservableVertex<TodoElement> rootCategory) {
        if(rootCategory == null) {
            headerName.setText("No category");
            categoryContents.setText("N/A");
            return;
        }

        if(!(rootCategory.getElement() instanceof Category))
            throw new IllegalArgumentException("ListViewHeader.onRootCategoryChange() - rootCategory.getElement is not of type Category");

        observableManager.addListener(rootCategory, this::onRootCategoryOutVerticesChange);
        observableManager.addListener(rootCategory.getGraph(), this::onRootCategoryOutVerticesRemoved);
        observableManager.addListener(rootCategory.getElement().name, this::onRootCategoryNameChange);
        tagDisplayView.setVertex(rootCategory);
    }

    private void onRootCategoryNameChange(String name) {
        headerName.setText(name);
    }

    private void onRootCategoryOutVerticesChange(ObservableVertexChange<TodoElement> change) {
        if(change.removedEdgesSize() != 0 && change.addedEdgesSize() != 0)
            rootCategoryOutVerticesChanged();
    }

    private void onRootCategoryOutVerticesRemoved(ObservableGraphChange<TodoElement> change) {
        if(change.removedVerticesSize() != 0 || change.addedVerticesSize() != 0)
            rootCategoryOutVerticesChanged();
    }

    private void rootCategoryOutVerticesChanged() {
        int numCategories = 0;
        int numTasks = 0;

        for(var vertex : _rootCategory.getValue().getGraph().getOutVertices(_rootCategory.getValue())) {
            if(vertex.getElement() instanceof Category)
                numCategories++;
            else if(vertex.getElement() instanceof Task)
                numTasks++;
        }

        categoryContents.setText(String.format(categoryContentsPattern, numCategories, numTasks));
    }


    public void setRootCategory(ObservableVertex<TodoElement> rootCategory) {
        _rootCategory.setValue(rootCategory);
    }

    public ObservableVertex<TodoElement> getRootCategory() {
        return _rootCategory.getValue();
    }

    public void setSearchQueryCallBack(IListener<IQuery<TodoElement>> searchQueryCallBack) {
        this.searchQueryCallBack = searchQueryCallBack;
    }

    public IListener<IQuery<TodoElement>> getSearchQueryCallBack() {
        return this.searchQueryCallBack;
    }

    @Override
    public Node node() {
        return this;
    }

    @Override
    public void registerListeners() {
        observableManager.startListen();
        tagDisplayView.registerListeners();
    }

    @Override
    public void unregisterListeners() {
        observableManager.stopListen();
        tagDisplayView.unregisterListeners();
    }
}
