package io.masterplan.ui.workspaces.editbar;


import io.masterplan.infrastucture.components.Category;
import io.masterplan.infrastucture.components.TodoElement;
import io.masterplan.infrastucture.models.MainModel;
import io.masterplan.infrastucture.observable.IReadOnlyObservable;
import io.masterplan.infrastucture.observable.Observable;
import io.masterplan.infrastucture.observable.ObservableManager;
import io.masterplan.infrastucture.util.graph.ObservableVertex;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;


import java.io.IOException;

public class CategoryEditBar extends VBox implements IEditBar {

    public final Observable<ObservableVertex<TodoElement>> _categoryVertex = new Observable<>();
    private final IReadOnlyObservable<ObservableVertex<TodoElement>> categoryVertex = _categoryVertex;

    private final ObservableManager observableManager = new ObservableManager();

    // DESCRIPTION inputs
    @FXML private TextField titleInput;

    // DATE inputs
    @FXML private Label createdDateInput;

    @FXML private TextArea description;

    @FXML private ColorPicker colorPicker;



    public CategoryEditBar() {
        loadFXML();
    }

    private void loadFXML() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("CategoryEditBar.fxml"));
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
        observableManager.addListener(_categoryVertex, this::onCategoryChange);
    }

    @FXML
    private void onConfirmChangesBtn_click(ActionEvent ae) {
        if(categoryVertex.getValue() == null)
            return;

        Category cat = (Category) this.categoryVertex.getValue().getElement();

        cat.setDescription(description.getText());

        cat.setName(titleInput.getText());

        cat.setBackgroundColor(colorPicker.getValue());
    }

    @FXML
    private void onMinimizeBtn_click(ActionEvent ae) {
        MainModel.model.editVertex.setValue(null);
    }



    private void onCategoryChange(ObservableVertex<TodoElement> catVertex) {

        if (catVertex == null) {
            description.setText("");
            titleInput.setText("");
            createdDateInput.setText("");
        }
        else {
            Category cat = (Category) catVertex.getElement();
            description.setText(cat.getDescription());
            titleInput.setText(cat.getName());
            createdDateInput.setText(cat.creationDate.getTime().toString());
            colorPicker.setValue(cat.backgroundColor.getValue());
        }
    }


    public void setEditVertex(ObservableVertex<TodoElement> categoryVertex) {
        if(categoryVertex != null && !(categoryVertex.getElement() instanceof Category))
            throw new IllegalArgumentException("EditBar.onTaskChange() - taskVertex.getElement() must be of type Task");

        this._categoryVertex.setValue(categoryVertex);
    }

    public Node node() {
        return this;
    }

    public void registerListeners() {
        observableManager.startListen();
    }

    public void unregisterListeners() {
        observableManager.stopListen();
    }

}
