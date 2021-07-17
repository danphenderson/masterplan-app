package io.masterplan.client.ui;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

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
//        Scene scene = FXMLLoader.load(getClass().getResource("account/login/LoginView.fxml"));
        Scene scene = FXMLLoader.load(getClass().getResource("window/MainView.fxml"));
        stage.setTitle("MasterPlan");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() {
        // release UI resources
    }


}

