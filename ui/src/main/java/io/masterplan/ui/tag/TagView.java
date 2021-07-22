package io.masterplan.ui.tag;

import io.masterplan.infrastucture.components.Colour;
import io.masterplan.infrastucture.components.Tag;
import io.masterplan.ui.util.ColorConverter;
import javafx.scene.layout.*;
import io.masterplan.infrastucture.observable.IListener;
import io.masterplan.infrastucture.observable.ObservableManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import io.masterplan.ui.util.Viewable;
import io.masterplan.ui.util.icon.Icon;

import java.io.IOException;

public class TagView extends HBox implements Viewable {

    public static final double CORNER_RADII = 5.0;

    @FXML
    private Button removeTagBtn;

    @FXML
    private Label tagName;

    @FXML
    private Icon removeIcon;

    public final Tag tag;
    private IListener<TagView> removeTagCallback = null;
    private final ObservableManager observableManager = new ObservableManager();


    public TagView(Tag tag) {
        this.tag = tag;

        observableManager.addListener(tag.name, this::onTagNameChange);
        observableManager.addListener(tag.color, this::onTagColorChange);

        loadFXML();
    }

    private void loadFXML() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("TagView.fxml"));
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
        setPadding(new Insets(2, 5, 2, 5));
        getChildren().remove(removeTagBtn);
    }

    public void setOnRemoveCallback(IListener<TagView> removeTagCallback) {
        this.removeTagCallback = removeTagCallback;
    }

    @FXML
    private void onRemoveButton_click(ActionEvent ae) {
        if(removeTagCallback != null)
            removeTagCallback.onChange(this);
    }

    private void onTagNameChange(String name) {
        tagName.setText(name);
    }

    private void onTagColorChange(Colour color) {
        changeBackgroundColor(ColorConverter.convertToFXColor(color));
        removeIcon.setIconColor(ColorConverter.convertToFXColor(color));
    }

    private void changeBackgroundColor(Color color) {
        tagName.setTextFill(color);
        setBorder(new Border(new BorderStroke(color, BorderStrokeStyle.SOLID, new CornerRadii(CORNER_RADII, false), BorderWidths.DEFAULT)));
    }

    @FXML
    private void onMouseEnter(MouseEvent me) {
        //removeTagBtn.setVisible(true);
        getChildren().add(removeTagBtn);
    }

    @FXML
    private void onMouseExit(MouseEvent me) {
        //removeTagBtn.setVisible(false);
        getChildren().remove(removeTagBtn);
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
    }
}
