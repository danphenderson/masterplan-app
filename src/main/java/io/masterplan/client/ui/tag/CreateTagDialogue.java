package io.masterplan.client.ui.tag;

import io.masterplan.client.components.Tag;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class CreateTagDialogue {

    public static final String DIALOGUE_TITLE = "Add Tag";

    public static final double MIN_WIDTH = 250;
    public static final double MIN_HEIGHT = 300;

    public static Tag showAndWait() {

        Stage stage = new Stage();

        CreateTagView createTagView = new CreateTagView(stage);

        Scene scene = new Scene(createTagView);

        stage.setTitle(DIALOGUE_TITLE);
        stage.initModality(Modality.NONE);

        stage.setScene(scene);

        createTagView.registerListeners();

        stage.setMinWidth(MIN_WIDTH);
        stage.setMinHeight(MIN_HEIGHT);
        stage.setWidth(MIN_WIDTH);
        stage.setHeight(MIN_HEIGHT);

        stage.showAndWait();

        createTagView.unregisterListeners();

        return createTagView.getCreatedTag();
    }
    
}
