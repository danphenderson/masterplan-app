package io.masterplan.ui.workspaces.archivespace.task;


import io.masterplan.infrastucture.components.TodoElement;
import io.masterplan.infrastucture.components.task.Task;
import io.masterplan.infrastucture.models.MainModel;
import io.masterplan.infrastucture.observable.IReadOnlyObservable;
import io.masterplan.infrastucture.observable.Observable;
import io.masterplan.infrastucture.observable.ObservableManager;
import io.masterplan.ui.util.Viewable;
import io.masterplan.infrastucture.util.graph.ObservableVertex;
import io.masterplan.infrastucture.util.graph.ObservableVertexChange;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;


import java.io.IOException;
import java.util.List;

public class ArchiveTaskView extends GridPane implements Viewable {

    @FXML
    private Button archiveButton;

    @FXML
    private Button toggleBtn;

    @FXML
    private HBox buttonContainer;

    @FXML
    private HBox remainingContainer;

    @FXML
    private Label taskName;

    @FXML
    private CheckBox completedCheckBox;

    @FXML
    private Label tasksRemainingLabel;
    private static final String tasksRemainingFormat = "%d remaining";

    private final Observable<ObservableVertex<TodoElement>> _rootTask = new Observable<>();
    public final IReadOnlyObservable<ObservableVertex<TodoElement>> rootTask = _rootTask;

    private ObservableVertex<TodoElement> taskVertex;
    private final ObservableManager observableManager = new ObservableManager();



    public ArchiveTaskView() { loadFXML(); }
    public ArchiveTaskView(ObservableVertex<TodoElement> vertex) {
        loadFXML();
        setTask(vertex);
    }

    private void loadFXML() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ArchiveTaskView.fxml"));
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

        observableManager.addListener(_rootTask, this::onRootTaskChange);

        setOnMouseClicked((e) -> {
            e.consume();
            MainModel.model.editVertex.setValue(_rootTask.getValue());
        });

        getChildren().forEach(e -> {
            e.setOnMouseEntered(event -> {
                remainingContainer.setStyle ("-fx-border-color: cadetblue;");
                buttonContainer.setStyle("-fx-border-color: cadetblue;");
            });
            e.setOnMouseExited(event -> {
                remainingContainer.setStyle("-fx-border-color: transparent;");
                buttonContainer.setStyle("-fx-border-color: transparent;");
            });
        });
    }



    private void onRootTaskChange(ObservableVertex<TodoElement> rootTask) {

        if(rootTask == null) {
            taskName.setText("No Task");
            tasksRemainingLabel.setText("N/A");
            completedCheckBox.selectedProperty().setValue(false);
            return;
        }

        observableManager.addListener(_rootTask.getValue(), this::onRemainingTasksChange);
        observableManager.addListener(_rootTask.getValue().getElement().name, this::onTaskNameChange);
        observableManager.addListener(((Task) _rootTask.getValue().getElement()).isCompleted, this::onTaskCompletedChange);
        observableManager.addListener(((Task) _rootTask.getValue().getElement()).isBookmarked, this::onBookMarkChange);
        completedCheckBox.selectedProperty().addListener(this::onTaskCompleted_click);

    }

    @FXML
    private void onBookmark_click(ActionEvent e)  {
        if(_rootTask.getValue() == null)
            return;
        Task task = ((Task) _rootTask.getValue().getElement());
        task.setBookmark(!task.isBookmarked()); // toggles bookmark bool
    }

    private void onBookMarkChange(boolean bookmarked) {
        if(bookmarked) { // set the Syt
            taskName.setId("NameBookmarked");
        }
        else {
            taskName.setId("Name"); // a css stile sheet, label node has two styleclasses
        }
    }

    @FXML
    private void onArchive_click(ActionEvent e){
        if(_rootTask.getValue() == null)
            return;

        Task task = (Task) rootTask.getValue().getElement();
        boolean curArchive = task.isArchived();
        task.setArchive(!curArchive);

        this.managedProperty().bindBidirectional(this.visibleProperty());
        this.setVisible(curArchive); //Hides the task at hand
    }

    private void onTaskCompleted_click(ObservableValue<? extends Boolean> observableValue, Boolean oldVal, Boolean newVal) {
        if(_rootTask.getValue() == null)
            return;

        Task task = (Task) _rootTask.getValue().getElement();
        task.setCompleted(newVal);
    }

    private void onTaskCompletedChange(boolean completed) {
        if(completed) {
            taskName.setTextFill(Color.GREEN);
        }
        else {
            taskName.setTextFill(Color.BLACK);
        }
    }

    @FXML
    private void onRemoveVertexBtn_click(ActionEvent e) {
        if(_rootTask.getValue() == null)
            return;

        _rootTask.getValue().getGraph().removeVertex(_rootTask.getValue());

        System.out.println("Removing vertex. Graph size: " + _rootTask.getValue().getGraph().getVertices().size());
    }

    @FXML
    private void onRemoveGraphBtn_click(ActionEvent e) {
        if(_rootTask.getValue() == null)
            return;

        _rootTask.getValue().getGraph().removeVertexReachable(_rootTask.getValue());

        System.out.println("Removing vertex. Graph size: " + _rootTask.getValue().getGraph().getVertices().size());
    }


    private void onTaskNameChange(String name) {
        taskName.setText(name);
    }

    private void onRemainingTasksChange(ObservableVertexChange<TodoElement> change) {

        List<ObservableVertex<TodoElement>> numTaskQueryRes = _rootTask.getValue().getGraph().query(
                (e) -> e instanceof Task,
                _rootTask.getValue()
        );

        tasksRemainingLabel.setText(String.format(tasksRemainingFormat, numTaskQueryRes.size()));

        int totalElements = _rootTask.getValue().getGraph().getOutDegree(_rootTask.getValue());

        toggleBtn.setVisible(totalElements > 0);
    }

    public void setTask(ObservableVertex<TodoElement> rootTask) {
        if(!(rootTask.getElement() instanceof Task))
            throw new IllegalArgumentException("TaskView() - rootVertex.getElement() is not of type Task");

        _rootTask.setValue(rootTask);
    }

    public ObservableVertex<TodoElement> getRootTask() {
        return _rootTask.getValue();
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
    }
}
