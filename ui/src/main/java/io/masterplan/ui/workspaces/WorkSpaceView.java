package io.masterplan.ui.workspaces;

import io.masterplan.infrastucture.components.TodoElement;
import io.masterplan.infrastucture.models.MainModel;
import io.masterplan.infrastucture.observable.ObservableManager;
import io.masterplan.ui.util.Viewable;
import io.masterplan.ui.workspaces.categorybar.CategoryListSpaceView;
import io.masterplan.ui.workspaces.editbar.EditBarContainer;
import io.masterplan.ui.workspaces.listspace.ListSpaceView;
import io.masterplan.infrastucture.util.graph.ObservableVertex;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.SplitPane;

import java.io.IOException;

/**
 *
 */
public class WorkSpaceView extends SplitPane implements Viewable {

    @FXML
    private ListSpaceView listSpaceView;

    @FXML
    private EditBarContainer editBarContainer;

    @FXML
    private CategoryListSpaceView categoryListSpaceView;

    private final ObservableManager observableManager = new ObservableManager();

    public WorkSpaceView() {
        loadFXML();
    }


    private void loadFXML() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("WorkSpaceView.fxml"));
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
        observableManager.addListener(MainModel.model.editVertex, this::onEditVertexChange);
    }

    private void onEditVertexChange(ObservableVertex<TodoElement> vertex) {
        if(vertex == null) {
            editBarContainer.unregisterListeners();
            getItems().remove(editBarContainer);
            System.out.println("vertex null");
        }
        else{
            System.out.println(vertex.getElement().getName());
            editBarContainer.registerListeners();
            if(!getItems().contains(editBarContainer))
                getItems().add(editBarContainer);
        }
    }

    /**
     *
     */
    public void showArchive() {

    }

    @Override
    public Node node() {
        return this;
    }

    @Override
    public void registerListeners() {
        observableManager.startListen();
        editBarContainer.registerListeners();
        listSpaceView.registerListeners();
        categoryListSpaceView.registerListeners();
    }

    @Override
    public void unregisterListeners() {
        observableManager.stopListen();
        editBarContainer.unregisterListeners();
        listSpaceView.unregisterListeners();
        categoryListSpaceView.unregisterListeners();
    }
}
