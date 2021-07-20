package io.masterplan.ui.util.dialogue.task;

import io.masterplan.infrastucture.components.task.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
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

public class CreateTaskView extends VBox implements Viewable {


    // DESCRIPTION inputs
    @FXML private TextField titleInput;
    @FXML private TextArea description;

    // DATE inputs
    @FXML private Label createdDateInput;
    @FXML private DatePicker dueDateInput;


    private final Stage stage;
    private Task task = new Task();


    public CreateTaskView(Stage stage) {
        this.stage = stage;
        loadFXML();
    }

    private void loadFXML() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("CreateTaskView.fxml"));
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

        task.setName(titleInput.getText());

        task.setDescription(description.getText());

        LocalDate date = dueDateInput.getValue();
        if (date != null) {
            task.setDueDate(toCalendar(date));
            System.out.println(task.creationDate.getTime().toString());
        }
        else
            System.out.println("Due date NULL");

        stage.close();
    }

    @FXML
    private void onCancelBtn_click(ActionEvent ae) {
        task = null;
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

    public Task getCreatedTask() {
        return task;
    }

    public Node node() {
        return this;
    }

    public void registerListeners() { }

    public void unregisterListeners() { }
}
