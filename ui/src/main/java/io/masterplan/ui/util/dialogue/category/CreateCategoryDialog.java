package io.masterplan.ui.util.dialogue.category;

import io.masterplan.infrastucture.components.Category;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class CreateCategoryDialog {

    public static final String DIALOGUE_TITLE = "Create Category";

    public static final double MIN_WIDTH = 250;
    public static final double MIN_HEIGHT = 300;

    public static Category showAndWait() {

        Stage stage = new Stage();

        CreateCategoryView createCategoryView = new CreateCategoryView(stage);

        Scene scene = new Scene(createCategoryView);

        stage.setTitle(DIALOGUE_TITLE);
        stage.initModality(Modality.NONE);

        stage.setScene(scene);

        createCategoryView.registerListeners();

        stage.setMinWidth(MIN_WIDTH);
        stage.setMinHeight(MIN_HEIGHT);
        stage.setWidth(MIN_WIDTH);
        stage.setHeight(MIN_HEIGHT);

        stage.showAndWait();

        createCategoryView.unregisterListeners();

        return createCategoryView.getCreatedCategory();
    }

}
