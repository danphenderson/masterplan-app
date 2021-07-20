package io.masterplan.ui.workspaces.archivespace;


import io.masterplan.infrastucture.components.Category;
import io.masterplan.infrastucture.components.TodoElement;
import io.masterplan.infrastucture.models.MainModel;
import io.masterplan.infrastucture.observable.ObservableManager;
import io.masterplan.ui.util.Viewable;
import io.masterplan.infrastucture.util.graph.ObservableVertex;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.VBox;


import java.io.IOException;

public class ArchiveListSpaceView extends VBox implements Viewable {
    @FXML
    private ArchiveListView archiveListView;

    @FXML
    private ScrollPane scrollPane;

    private final double SCROLL_SPEED_MODIFIER = 6.0;

    private final ObservableManager observableManager = new ObservableManager();

    public ArchiveListSpaceView(){
        loadFXML();
    }

    private void loadFXML(){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ArchiveListSpaceView.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void initialize(){
        observableManager.addListener(MainModel.model.selectedVertex, this::onRootVertexChange);

        // Set scroll event handler for scrollPane view
        scrollPane.getContent().setOnScroll(this::scrollHandler);
    }

    private void onRootVertexChange(ObservableVertex<TodoElement> rootVertex) {
        if(!(rootVertex.getElement() instanceof Category))
            throw new IllegalArgumentException("ListView() - rootVertex is not of type Category");
    }

    /**
     * Scales the speed of scrolling for scrollPane
     * @param e mouse scroll event
     */
    private void scrollHandler(ScrollEvent e) {
        double deltaY = e.getDeltaY() * SCROLL_SPEED_MODIFIER;
        double width = scrollPane.getContent().getBoundsInLocal().getWidth();
        double vValue = scrollPane.getVvalue();
        scrollPane.setVvalue(vValue + -deltaY/width);
    }

    @Override
    public Node node() {
        return null;
    }

    @Override
    public void registerListeners() {
        archiveListView.registerListeners();
        observableManager.startListen();
    }

    @Override
    public void unregisterListeners() {
        archiveListView.unregisterListeners();
        observableManager.stopListen();
    }
}
