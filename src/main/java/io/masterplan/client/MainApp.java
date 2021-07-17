package io.masterplan.client;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class MainApp extends Application {

    public static void main(String[] args) {
        Application.launch(args);
    }
    @Override
    public void init() {
        // load UI resources
    }

    @Override
    public void start(Stage stage) throws Exception {

        var mainResource = getClass().getResource("ui/window/MainView.fxml");
        Objects.requireNonNull(mainResource);

        Scene scene = FXMLLoader.load(mainResource);
        stage.setTitle("MasterPlan");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() {
        // release UI resources
    }


}

