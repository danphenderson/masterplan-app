package io.masterplan.client.ui.workspaces.editbar;


import io.masterplan.client.components.Category;
import io.masterplan.client.components.TodoElement;
import io.masterplan.client.components.task.Task;
import io.masterplan.client.models.MainModel;
import io.masterplan.client.observable.ObservableManager;
import io.masterplan.client.ui.util.Viewable;
import io.masterplan.client.util.graph.ObservableVertex;
import javafx.scene.Node;
import javafx.scene.layout.VBox;


public class EditBarContainer extends VBox implements Viewable {

    private final ObservableManager observableManager = new ObservableManager();

    private IEditBar activeEditBar = null;

    public EditBarContainer() {
        observableManager.addListener(MainModel.model.editVertex, this::onEditVertexChange);

        this.setMinWidth(200);
        this.setMaxWidth(250);
    }

    private void onEditVertexChange(ObservableVertex<TodoElement> editVertex) {

        System.out.println("EditVertex change");

        if(activeEditBar != null)
            activeEditBar.unregisterListeners();

        getChildren().clear();

        if(editVertex == null) {
            return;
        }else if(editVertex.getElement() instanceof Task) {
            activeEditBar = new TaskEditBar();
        }
        else if(editVertex.getElement() instanceof Category) {
            activeEditBar = new CategoryEditBar();
        }
        else {
            throw new IllegalArgumentException("editVertex.getElement() must either be of type Task or Category");
        }

        activeEditBar.setEditVertex(MainModel.model.editVertex.getValue());
        activeEditBar.registerListeners();
        getChildren().add(activeEditBar.node());
    }

    public Node node() {
        return this;
    }

    public void registerListeners() {
        observableManager.startListen();
        if(activeEditBar != null)
            activeEditBar.registerListeners();
    }

    public void unregisterListeners() {
        observableManager.stopListen();
        if(activeEditBar != null)
            activeEditBar.unregisterListeners();
    }




}
