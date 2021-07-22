package io.masterplan.infrastucture.models;

import io.masterplan.infrastucture.components.Category;
import io.masterplan.infrastucture.components.Tag;
import io.masterplan.infrastucture.components.TodoElement;
import io.masterplan.infrastucture.components.task.Task;
import io.masterplan.infrastucture.observable.Observable;
import io.masterplan.infrastucture.observable.ObservableSet;
import io.masterplan.infrastucture.util.graph.*;




import java.util.HashSet;

public class MainModel {

    public static final MainModel model = new MainModel();

    public final ObservableGraph<TodoElement> obsGraph;
    public final Observable<ObservableVertex<TodoElement>> selectedVertex = new Observable<>();
    public final Observable<ObservableVertex<TodoElement>> editVertex = new Observable<>();
    public final ObservableSet<Tag> tags = new ObservableSet<>(new HashSet<>());


    private MainModel() {
        // deserialize graph
        this.obsGraph = new ObservableGraph<>(new Graph<>());
        selectedVertex.setValue(obsGraph.addVertex(new Category("Main")));

        obsGraph.startListen(this::onEditVertexRemoved);
        obsGraph.startListen(this::onSelectedVertexRemoved);
    }

    private void onEditVertexRemoved(ObservableGraphChange<TodoElement> change) {
        if(change.getRemovedVertices().contains(editVertex.getValue()))
            editVertex.setValue(null);
    }

    private void onSelectedVertexRemoved(ObservableGraphChange<TodoElement> change) {
        if(change.getRemovedVertices().contains(selectedVertex.getValue()))
            selectedVertex.setValue(null);
    }

}
