package io.masterplan.client.ui.util.dialogue.task;

import io.masterplan.client.components.task.Task;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class CreateTaskDialog {

    public static final String DIALOGUE_TITLE = "Create Task";

    public static final double MIN_WIDTH = 250;
    public static final double MIN_HEIGHT = 300;

    public static Task showAndWait() {

        Stage stage = new Stage();

        CreateTaskView createTaskView = new CreateTaskView(stage);

        Scene scene = new Scene(createTaskView);

        stage.setTitle(DIALOGUE_TITLE);
        stage.initModality(Modality.NONE);

        stage.setScene(scene);

        createTaskView.registerListeners();

        stage.setMinWidth(MIN_WIDTH);
        stage.setMinHeight(MIN_HEIGHT);
        stage.setWidth(MIN_WIDTH);
        stage.setHeight(MIN_HEIGHT);

        stage.showAndWait();

        createTaskView.unregisterListeners();

        return createTaskView.getCreatedTask();
    }

}
