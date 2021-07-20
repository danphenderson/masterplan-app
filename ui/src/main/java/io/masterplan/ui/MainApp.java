package io.masterplan.ui;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;


import java.net.URL;

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
        URL fxmlLocation = getClass().getResource("window/MainView.fxml");
        System.out.println(fxmlLocation);

        Scene scene = FXMLLoader.load(fxmlLocation);
        stage.setTitle("MasterPlan");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() {
        // release UI resources
    }


}

