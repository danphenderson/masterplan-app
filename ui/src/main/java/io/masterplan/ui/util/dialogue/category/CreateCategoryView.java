package io.masterplan.ui.util.dialogue.category;

import io.masterplan.infrastucture.components.Category;
import io.masterplan.ui.util.ColorConverter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import io.masterplan.ui.util.Viewable;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

public class CreateCategoryView extends VBox implements Viewable {

    // DESCRIPTION inputs
    @FXML private TextField titleInput;

    @FXML private TextArea description;

    @FXML private ColorPicker colorPicker;



    private final Stage stage;
    private Category cat = new Category();


    public CreateCategoryView(Stage stage) {
        this.stage = stage;
        loadFXML();
    }

    private void loadFXML() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("CreateCategoryView.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    @FXML
    private void initialize() {

    }

    @FXML
    private void onCreateBtn_click(ActionEvent ae) {
        cat.setName(titleInput.getText());
        cat.setDescription(description.getText());
        cat.setBackgroundColor(ColorConverter.convertToColor(colorPicker.getValue()));

        stage.close();
    }

    @FXML
    private void onCancelBtn_click(ActionEvent ae) {
        cat = null;
        stage.close();
    }

    public static Calendar toCalendar(LocalDate localDate) {
        Date date = localToDate(localDate);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    public static Date localToDate(LocalDate dateToConvert) {
        return java.util.Date.from(dateToConvert.atStartOfDay()
                .atZone(ZoneId.systemDefault())
                .toInstant());
    }

    public Category getCreatedCategory() {
        return cat;
    }

    public Node node() {
        return this;
    }

    public void registerListeners() { }

    public void unregisterListeners() { }
}