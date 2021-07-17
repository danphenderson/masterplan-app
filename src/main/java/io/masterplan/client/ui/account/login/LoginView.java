package io.masterplan.client.ui.account.login;


import javafx.scene.layout.VBox;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import io.masterplan.client.ui.account.login.create.CreationView;


public class LoginView extends VBox {

    @FXML
    private BorderPane mainContainer;


    @FXML
    private void initialize() {
        switchToLogin();
    }

    @FXML
    private void switchToLogin() {
        mainContainer.setCenter(new Login());
    }

    @FXML
    private void switchToCreate() {
        mainContainer.setCenter(new CreationView());
    }

}
