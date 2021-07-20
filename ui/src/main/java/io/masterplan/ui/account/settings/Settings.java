package io.masterplan.ui.account.settings;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import io.masterplan.infrastucture.models.MainModel;


import java.io.IOException;

public class Settings extends SplitPane {

    @FXML
    private Pane container;

    @FXML
    private ImagePattern userImage;

    @FXML
    private Label username;

    private final MainModel mainModel;

    public Settings(MainModel mainModel){
        this.mainModel = mainModel;

        loadFXML();
    }

    private void loadFXML(){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Settings.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try{
            fxmlLoader.load();
        } catch(IOException exception){
            throw new RuntimeException(exception);
        }
    }


    @FXML
    private void userAccountClicked(MouseEvent mouseEvent) {
        container.getChildren().clear();

        //TODO Create a better 'settings' view.
        FXMLLoader loader = new FXMLLoader(getClass().getResource("./Settings.fxml"));

        Parent parent;
        try {
             parent = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        container.getChildren().add(parent);
    }

}
