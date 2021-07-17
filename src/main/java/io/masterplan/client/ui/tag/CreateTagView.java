package io.masterplan.client.ui.tag;

import io.masterplan.client.components.Tag;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import io.masterplan.client.observable.ObservableManager;
import io.masterplan.client.models.MainModel;
import io.masterplan.client.observable.ObservableSet;
import io.masterplan.client.ui.util.Viewable;

import java.io.IOException;

public class CreateTagView extends VBox implements Viewable{

    @FXML
    private TextField tagNameInput;

    @FXML
    private ColorPicker tagColorInput;

    @FXML
    private Button createBtn;

    @FXML
    private Button cancelBtn;

    @FXML
    private FlowPane tagsFlowPane;

    private final Stage stage;

    private final ObservableManager observableManager = new ObservableManager();

    private Tag tag = new Tag();

    public CreateTagView(Stage stage) {
        this.stage = stage;
        loadFXML();
    }

    private void loadFXML() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("CreateTagView.fxml"));
        fxmlLoader.setController(this);
        fxmlLoader.setRoot(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    @FXML
    private void initialize() {
        observableManager.addListener(MainModel.model.tags, this::onAllTagsChange);
    }

    private void onAllTagsChange(ObservableSet<Tag> tags) {

        System.out.println("Tags Changed, size: " + tags.size());

        for(Node node : tagsFlowPane.getChildren()) {
            if(node instanceof Viewable) {
                ((Viewable) node).unregisterListeners();
            }
        }

        tagsFlowPane.getChildren().clear();

        for(var tag : tags) {
            TagView tagView = new TagView(tag);
            tagView.setOnRemoveCallback(this::onTagRemoved);
            tagView.registerListeners();
            tagView.setOnMouseClicked(this::onTagViewClicked);
            tagsFlowPane.getChildren().add(tagView);
        }

    }

    private void onTagViewClicked(MouseEvent me) {
        TagView tagView = (TagView) me.getSource();
        this.tag = tagView.tag;
        this.tagNameInput.setText(tag.getName());
        this.tagColorInput.setValue(tag.getColor());
    }

    private void onTagRemoved(TagView tagView) {
        tagView.unregisterListeners();
    }

    @FXML
    private void onCreateBtn_click(ActionEvent ae) {

        if(MainModel.model.tags.contains(tag) && !(tagNameInput.getText().equals(tag.getName()) && tagColorInput.getValue().equals(tag.getColor()))) {
            this.tag = new Tag();
        }

        tag.setName(tagNameInput.getText());
        tag.setColor(tagColorInput.getValue());

        stage.close();
    }

    @FXML
    private void onCancelBtn_click(ActionEvent ae) {
        tag = null;
        stage.close();
    }

    public Tag getCreatedTag() {
        return tag;
    }

    @Override
    public Node node() {
        return this;
    }

    @Override
    public void registerListeners() {
        observableManager.startListen();
    }

    @Override
    public void unregisterListeners() {
        observableManager.stopListen();
        for(Node node : tagsFlowPane.getChildren())
            if(node instanceof Viewable)
                ((Viewable) node).unregisterListeners();

        tagsFlowPane.getChildren().clear();
    }
}
